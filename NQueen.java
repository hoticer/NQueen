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

	static int N; // �ʺ���
	static Rule[] rule;// ����
	static Stack<Rule> rules = new Stack<>();// ����
	static Stack<Integer> result = new Stack<>(); // ���
	static int[] queen; // ĳ��������лʺ�λ��������������
	static int jnum; // �����Ŀ
	static int resnum = 0; // ��չʾ����Ŀ
	static long startTime;// ��ʼʱ��
	static long endTime;// ����ʱ��
	static long time;// ����ʱ��

	JPanel pan1 = new JPanel();
	JPanel pan2 = new JPanel();
	JPanel pan3 = new JPanel();
	JLabel ln = new JLabel("N:");
	JLabel bz = new JLabel("By:TYUT�ƿ�1301����");
	JLabel sum = new JLabel();
	private JTextField jtx = new JTextField(3);// ����N���ı���
	private JButton ok = new JButton("ok");// ��ʼ
	private JButton nextB = new JButton("next");// չʾ��һ���ⷨ

	public NQueen() {
		super("N�ʺ�");
		this.setSize(800, 600);
		// ʹ��������Ļ����
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

		// ���س������ok
		jtx.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) // ���س���ִ����Ӧ����;
					ok.doClick();
			}
		});

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startTime = System.currentTimeMillis();
				// �������벻�����ֵ��쳣
				try {
					N = Integer.parseInt(jtx.getText().trim());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "����������");
					jtx.setText("");
					return;
				}
				if (N <= 3) {
					JOptionPane.showMessageDialog(null, "���������3����!");
					jtx.setText("");
					return;
				}
				if (N > 14) {
					JOptionPane.showMessageDialog(null, "������С��15����!");
					jtx.setText("");
					return;
				}
				// ���³�ʼ��pan2����
				pan2.removeAll();
				ok.setVisible(false);
				ok.setVisible(true);
				nextB.setVisible(true);

				// ����N*N����
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

				// �����Ⲣ��䷽��
				workout();

				endTime = System.currentTimeMillis();
				time = endTime - startTime;// ���㻨ʱ

				jnum = result.size() / N;
				sum.setText("��" + jnum + "�ֽ�,��ʱ" + time + "ms");// ����sumJlabel��ֵ
				resnum = 0;
				for (int i = 0; i < N; i++)
					buttons[i][result.get(i) - 1].setBackground(Color.yellow);
			}
		});

		nextB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int bnum = pan2.getComponentCount();// ��������
				if (++resnum < jnum) {
					// ���ø�����ɫ
					for (int i = 0; i < bnum; i++)
						pan2.getComponent(i).setBackground(Color.white);
					// Ϊ��һ����䷽����ɫ
					for (int i = 0; i < N; i++)
						pan2.getComponent(result.get(i + resnum * N) - 1 + i * N).setBackground(Color.yellow);
				} else {
					JOptionPane.showMessageDialog(null, "��չʾ���нⷨ");
				}
			}
		});

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// ��������нⷨ�ʺ�����������ͨ��String�ͷ���
	public static void workout() {
		rule = new Rule[N * N];
		int i = 0;

		// ��ʼ������
		for (int j = 1; j <= N; j++)
			for (int k = 1; k <= N; k++)
				rule[i++] = new Rule(j, k);
		result.clear();

		queen = new int[N + 1];
		backtrack(1);
	}

	// ����
	public static void backtrack(int i) {

		if (i > N)
			for (int j = 1; j <= N; j++)
				result.push(queen[j]);
		else {
			// ��Ӧ����������
			for (int j = N * i - 1; j >= 0 + N * (i - 1); j--)
				rules.push(rule[j]);

			while (!rules.isEmpty()) {
				// ȡ�������ɻʺ�λ��
				queen[i] = rules.remove(rules.size() - 1).j;

				// �ж�λ���Ƿ�Ϸ�
				if (term(i)) {
					backtrack(i + 1); // ѭ������
				}

				if (queen[i] == N) {
					queen[i] = 0;
					break;
				}
			}
		}
	}

	// �жϷŵ�λ���Ƿ�Ϸ�
	public static boolean term(int n) {
		for (int i = 1; i < n; i++) {
			// ������ͬһ�л�Խ�����
			if (queen[i] == queen[n] || Math.abs(queen[i] - queen[n]) == Math.abs(i - n))
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		new NQueen();
	}
}

// ����
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
