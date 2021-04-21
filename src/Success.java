import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

public class Success {

	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Success window = new Success();
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
	public Success() {
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
		
		JLabel label = new JLabel("\u4FE1\u606F\u4F20\u8F93\u5E76\u89E3\u5BC6\u6210\u529F\uFF01");
		label.setFont(new Font("ËÎÌו", Font.PLAIN, 30));
		label.setBounds(54, 52, 349, 45);
		frame.getContentPane().add(label);
		
		JLabel lblNewLabel = new JLabel("\u4FE1\u606F\u65E0\u7BE1\u6539\uFF01");
		lblNewLabel.setFont(new Font("ËÎÌו", Font.PLAIN, 30));
		lblNewLabel.setBounds(118, 107, 204, 35);
		frame.getContentPane().add(lblNewLabel);
	}
}
