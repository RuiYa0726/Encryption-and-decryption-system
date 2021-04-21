import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;

public class Failed {

	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Failed window = new Failed();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Failed() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("\u52A0\u5BC6\u4E0E\u89E3\u5BC6\u5185\u5BB9\u4E0D\u540C");
		label.setFont(new Font("ËÎÌו", Font.PLAIN, 30));
		label.setBounds(72, 44, 295, 61);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u4F20\u8F93\u5931\u8D25\uFF01");
		label_1.setFont(new Font("ËÎÌו", Font.PLAIN, 30));
		label_1.setBounds(138, 107, 156, 29);
		frame.getContentPane().add(label_1);
	}
}
