import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

public class NullSelected_1 {

	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NullSelected_1 window = new NullSelected_1();
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
	public NullSelected_1() {
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
		
		JLabel label = new JLabel("\u6CA1\u6709\u9009\u62E9\u5BF9\u79F0\u52A0\u5BC6\u7B97\u6CD5\uFF01");
		label.setFont(new Font("ËÎÌו", Font.PLAIN, 30));
		label.setBounds(40, 71, 344, 46);
		frame.getContentPane().add(label);
	}

}
