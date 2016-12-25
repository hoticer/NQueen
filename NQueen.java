import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Stack;

@SuppressWarnings("serial")
public class NQueen extends JFrame {

	static int N; // 皇后数
	static Rule[] rule;// 规则
	static Stack<Rule> rules = new Stack<>();// 规则集
	static Stack<Integer> result = new Stack<>(); // 结果
	static int[] queen; // 某个解的所有皇后位置所在列数集合
	static int jnum; // 解的数目
	static int resnum = 0; // 已展示的数目
	static long startTime;// 开始时间
	static long endTime;// 结束时间
	static long time;// 所花时间

	JPanel pan1 = new JPanel();
	JPanel pan2 = new JPanel();
	JPanel pan3 = new JPanel();
	JLabel ln = new JLabel("N:");
	JLabel bz = new JLabel("By:TYUT计科1301徐延");
	JLabel sum = new JLabel();
	private JTextField jtx = new JTextField(3);// 输入N的文本框
	private JButton ok = new JButton("ok");// 开始
	private JButton nextB = new JButton("next");// 展示下一个解法

	public NQueen() {
		super("N皇后");
		this.setSize(800, 600);
		// 使界面在屏幕中央
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension dim = tool.getScreenSize();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		this.setLocation((width - 800) / 2, (height - 600) / 2);
		this.setResizable(false);

		pan1.setBackground(Color.CYAN);
		pan1.add(ln);
		pan1.add(jtx);
		pan1.add(ok);
		pan1.add(nextB);
		pan1.add(sum);
		pan1.setBorder(new LineBorder(Color.gray));
		pan3.setBorder(new LineBorder(Color.gray));
		pan3.setBackground(Color.CYAN);
		pan3.add(bz);
		nextB.setVisible(false);
		this.setLayout(new BorderLayout());
		this.add(pan1, BorderLayout.NORTH);
		this.add(pan2, BorderLayout.CENTER);
		this.add(pan3, BorderLayout.SOUTH);

		// 按回车键点击ok
		jtx.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) // 按回车键执行相应操作;
					ok.doClick();
			}
		});

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startTime = System.currentTimeMillis();
				// 捕获输入不是数字的异常
				try {
					N = Integer.parseInt(jtx.getText().trim());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "请输入数字");
					jtx.setText("");
					return;
				}
				if (N <= 3) {
					JOptionPane.showMessageDialog(null, "请输入大于3的数!");
					jtx.setText("");
					return;
				}
				if (N > 14) {
					JOptionPane.showMessageDialog(null, "请输入小于15的数!");
					jtx.setText("");
					return;
				}
				// 重新初始化pan2界面
				pan2.removeAll();
				ok.setVisible(false);
				ok.setVisible(true);
				nextB.setVisible(true);

				// 生成N*N格子
				GridLayout grid = new GridLayout(N, N);
				pan2.setLayout(grid);
				Button[][] buttons = new Button[N][N];
				for (int i = 0; i < buttons.length; i++) {
					for (int j = 0; j < buttons[i].length; j++) {
						buttons[i][j] = new Button();
						buttons[i][j].setEnabled(false);
						pan2.add(buttons[i][j]);
					}
				}

				// 解析解并填充方格
				workout();

				endTime = System.currentTimeMillis();
				time = endTime - startTime;// 计算花时

				jnum = result.size() / N;
				sum.setText("共" + jnum + "种解,花时" + time + "ms");// 设置sumJlabel的值
				resnum = 0;
				for (int i = 0; i < N; i++)
					buttons[i][result.get(i) - 1].setBackground(Color.yellow);
			}
		});

		nextB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int bnum = pan2.getComponentCount();// 方格总数
				if (++resnum < jnum) {
					// 重置格子颜色
					for (int i = 0; i < bnum; i++)
						pan2.getComponent(i).setBackground(Color.white);
					// 为下一解填充方格颜色
					for (int i = 0; i < N; i++)
						pan2.getComponent(result.get(i + resnum * N) - 1 + i * N).setBackground(Color.yellow);
				} else {
					JOptionPane.showMessageDialog(null, "已展示所有解法");
				}
			}
		});

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 计算出所有解法皇后所在列数并通过String型返回
	public static void workout() {
		rule = new Rule[N * N];
		int i = 0;

		// 初始化规则
		for (int j = 1; j <= N; j++)
			for (int k = 1; k <= N; k++)
				rule[i++] = new Rule(j, k);
		result.clear();

		queen = new int[N + 1];
		backtrack(1);
	}

	// 回溯
	public static void backtrack(int i) {

		if (i > N)
			for (int j = 1; j <= N; j++)
				result.push(queen[j]);
		else {
			// 相应规则加入规则集
			for (int j = N * i - 1; j >= 0 + N * (i - 1); j--)
				rules.push(rule[j]);

			while (!rules.isEmpty()) {
				// 取规则生成皇后位置
				queen[i] = rules.remove(rules.size() - 1).j;

				// 判断位置是否合法
				if (term(i)) {
					backtrack(i + 1); // 循环调用
				}

				if (queen[i] == N) {
					queen[i] = 0;
					break;
				}
			}
		}
	}

	// 判断放的位置是否合法
	public static boolean term(int n) {
		for (int i = 1; i < n; i++) {
			// 不能在同一列或对角线上
			if (queen[i] == queen[n] || Math.abs(queen[i] - queen[n]) == Math.abs(i - n))
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		new NQueen();
	}
}

// 规则
class Rule {
	int i;
	int j;

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public Rule(int i, int j) {
		super();
		this.i = i;
		this.j = j;
	}
}
