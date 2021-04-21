import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import DES.DESUtil;
import RSA.RSA;
import AES.AES;
import AES.word;
import HASH.MD5;
import HASH.SHA1;

public class DecryptSystem  extends JFrame{

	public JFrame frame;
	String content;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	String ToB;//发送给B的密文
	int toBLenKey;
	int ToBMLen;
	BigInteger N;//RSA中n的值
	BigInteger be;//RSA中B的公钥
	BigInteger bd;//RSA中B的私钥
	BigInteger Ad;//RSA中A的公钥
	BigInteger c;
	int BDESlen;
	int BRSAlen;//RSA加密的对称密钥长度
	word[] BAESCipherText;//AES加密的密文
	int BAESlen;//AES加密的原密文长度
	String M;//明文
	String RSAM;//RSA加密部分的信息
	String Signature="";
	String StrBigInoutm="";
	private JTextField textField_9;
	private File openFile;                        //文件类
	private FileInputStream fileInputStream;       //字节文件输入流 
	private FileOutputStream fileOutputStream;     //字节文件输出流
	private OutputStreamWriter outputStreamWriter; //字符文件输出
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DecryptSystem window = new DecryptSystem();
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
	public DecryptSystem() {
		initialize();
	}

	
	//byte[]的输出函数
	static void Print(byte[] s) {
		for(int i=0;i<s.length;i++) {
			if(i%8==0)
				System.out.printf(" ");
			System.out.print(s[i]);
		}
		System.out.printf("\n");
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("194588陶蕊");
		frame.setBounds(100, 100, 947, 1138);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u6B22\u8FCE\u60A8\uFF0C\u7528\u6237B\uFF01");
		lblNewLabel.setBounds(269, 21, 255, 54);
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 30));
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(279, 83, 538, 122);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		Font font = new Font("宋体", Font.PLAIN, 30);
		
		
		JLabel lbln = new JLabel("\u6A21\u6570n\uFF1A");
		lbln.setBounds(72, 254, 123, 35);
		lbln.setFont(new Font("宋体", Font.PLAIN, 30));
		frame.getContentPane().add(lbln);
		
		JLabel lblAe = new JLabel("B\u7684\u516C\u94A5\uFF1A");
		lblAe.setBounds(72, 304, 150, 35);
		lblAe.setFont(new Font("宋体", Font.PLAIN, 30));
		frame.getContentPane().add(lblAe);
		
		JLabel lbld = new JLabel("B\u7684\u79C1\u94A5\uFF1A");
		lbld.setBounds(72, 356, 150, 29);
		lbld.setFont(new Font("宋体", Font.PLAIN, 30));
		frame.getContentPane().add(lbld);
		
		textField_1 = new JTextField();
		textField_1.setBounds(171, 257, 169, 33);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(213, 307, 482, 33);
		textField_2.setColumns(10);
		frame.getContentPane().add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setBounds(213, 356, 482, 33);
		textField_3.setColumns(10);
		frame.getContentPane().add(textField_3);
		
		JButton btnNewButton = new JButton("\u5BFC\u5165");
		btnNewButton.setBounds(712, 257, 102, 35);
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 30));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); //文件选择
		        chooser.setFont(font);
				chooser.showOpenDialog(chooser);        //打开文件选择窗	
				openFile = chooser.getSelectedFile();  	//获取选择的文件
				openFile = new File(openFile.getPath());
				try {
					if(!openFile.exists()){      //如果文件不存在
						openFile.createNewFile();//创建文件
					}
					fileInputStream = new FileInputStream(openFile); //创建文件输入流
					byte b[] = new byte[(int) openFile.length()];  //定义文件大小的字节数据
					fileInputStream.read(b);          //将文件数据存储在b数组
					content = new String(b,"UTF-8"); //将字节数据转换为UTF-8编码的字符串
					textField_9.setText(content);	//获取选择文件的路径
					fileInputStream.close();          //关闭文件输入流
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		JButton btnRsak = new JButton("RSA\u89E3\u5BC6");
		btnRsak.setBounds(69, 406, 153, 43);
		btnRsak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RSA rsa=new RSA();
				String Input=ToB.substring(ToB.length()-BRSAlen,ToB.length());
				BigInteger input = new BigInteger(Input,16);//将字符串转换成10进制大整数
				BigInteger outm=rsa.decrypt(input,N,bd);//解密明文
				String StrBigInoutm=outm.toString(16);
				textField_4.setText(StrBigInoutm);
			}
		});
		btnRsak.setFont(new Font("宋体", Font.PLAIN, 30));
		frame.getContentPane().add(btnRsak);
		
		textField_4 = new JTextField();
		textField_4.setBounds(333, 410, 482, 40);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblk = new JLabel("\u5BC6\u94A5K\uFF1A");
		lblk.setBounds(243, 413, 108, 29);
		lblk.setFont(new Font("宋体", Font.PLAIN, 30));
		frame.getContentPane().add(lblk);
		
		JPanel panel = new JPanel();
		panel.setBounds(58, 212, 788, 265);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Border titleBorder1 = BorderFactory.createTitledBorder("RSA");
		panel.setBorder(titleBorder1);
		((javax.swing.border.TitledBorder) panel.getBorder()).setTitleFont(new Font("宋体",Font.PLAIN,28));
        panel.repaint();
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton_2 = new JButton("\u5BFC\u5165");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); //文件选择
		        chooser.setFont(font);
				chooser.showOpenDialog(chooser);        //打开文件选择窗	
				openFile = chooser.getSelectedFile();  	//获取选择的文件
				openFile = new File(openFile.getPath());
				try {
					if(!openFile.exists()){      //如果文件不存在
						openFile.createNewFile();//创建文件
					}
					fileInputStream = new FileInputStream(openFile); //创建文件输入流
					byte b[] = new byte[(int) openFile.length()];  //定义文件大小的字节数据
					fileInputStream.read(b);          //将文件数据存储在b数组
					content = new String(b,"UTF-8"); //将字节数据转换为UTF-8编码的字符串
					textField_2.setText(content);	//获取选择文件的路径
					fileInputStream.close();          //关闭文件输入流
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_2.setFont(new Font("宋体", Font.PLAIN, 30));
		btnNewButton_2.setBounds(652, 94, 104, 35);
		panel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("\u5BFC\u5165");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); //文件选择
		        chooser.setFont(font);
				chooser.showOpenDialog(chooser);        //打开文件选择窗	
				openFile = chooser.getSelectedFile();  	//获取选择的文件
				openFile = new File(openFile.getPath());
				try {
					if(!openFile.exists()){      //如果文件不存在
						openFile.createNewFile();//创建文件
					}
					fileInputStream = new FileInputStream(openFile); //创建文件输入流
					byte b[] = new byte[(int) openFile.length()];  //定义文件大小的字节数据
					fileInputStream.read(b);          //将文件数据存储在b数组
					content = new String(b,"UTF-8"); //将字节数据转换为UTF-8编码的字符串
					textField_3.setText(content);	//获取选择文件的路径
					fileInputStream.close();          //关闭文件输入流
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_3.setFont(new Font("宋体", Font.PLAIN, 30));
		btnNewButton_3.setBounds(652, 142, 104, 35);
		panel.add(btnNewButton_3);
		
		JLabel lblA = new JLabel("A\u7684\u516C\u94A5\uFF1A");
		lblA.setFont(new Font("宋体", Font.PLAIN, 29));
		lblA.setBounds(400, 48, 132, 29);
		panel.add(lblA);
		
		textField_9 = new JTextField();
		textField_9.setBounds(519, 46, 126, 35);
		panel.add(textField_9);
		textField_9.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("\u5BFC\u5165");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); //文件选择
		        chooser.setFont(font);
				chooser.showOpenDialog(chooser);        //打开文件选择窗	
				openFile = chooser.getSelectedFile();  	//获取选择的文件
				openFile = new File(openFile.getPath());
				try {
					if(!openFile.exists()){      //如果文件不存在
						openFile.createNewFile();//创建文件
					}
					fileInputStream = new FileInputStream(openFile); //创建文件输入流
					byte b[] = new byte[(int) openFile.length()];  //定义文件大小的字节数据
					fileInputStream.read(b);          //将文件数据存储在b数组
					content = new String(b,"UTF-8"); //将字节数据转换为UTF-8编码的字符串
					textField_1.setText(content);	//获取选择文件的路径
					fileInputStream.close();          //关闭文件输入流
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_4.setFont(new Font("宋体", Font.PLAIN, 30));
		btnNewButton_4.setBounds(289, 44, 102, 35);
		panel.add(btnNewButton_4);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(58, 483, 788, 235);
		frame.getContentPane().add(panel_1);
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Border titleBorder2 = BorderFactory.createTitledBorder("DES/AES");
		panel_1.setBorder(titleBorder2);
		panel_1.setLayout(null);
		
		textField_5 = new JTextField();
		textField_5.setBounds(119, 104, 221, 100);
		panel_1.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblm = new JLabel("\u660E\u6587M\uFF1A");
		lblm.setBounds(17, 108, 108, 29);
		panel_1.add(lblm);
		lblm.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JRadioButton rdbtnDes = new JRadioButton("DES");
		rdbtnDes.setBounds(17, 43, 86, 35);
		panel_1.add(rdbtnDes);
		rdbtnDes.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JRadioButton rdbtnAes = new JRadioButton("AES");
		rdbtnAes.setBounds(139, 42, 86, 37);
		panel_1.add(rdbtnAes);
		rdbtnAes.setFont(new Font("宋体", Font.PLAIN, 30));
		
		ButtonGroup buttonGroup=new ButtonGroup();
		buttonGroup.add(rdbtnDes);
		buttonGroup.add(rdbtnAes);
		
		JButton btnDesres = new JButton("DES/AES\u89E3\u5BC6");
		btnDesres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			String Input=ToB.substring(0,ToB.length()-BRSAlen);
			if(rdbtnDes.isSelected()) {
				//DES解密
				DESUtil des=new DESUtil();
				byte sKey[]= {1,1,1,0,0,1,0,1,0,0,1,1,1,0,1,1,0,1,1,1,1,0,1,0,0,1,1,0,0,1,0,1,1,0,1,0,0,1,0,1,0,0,1,1,0,1,0,0,1,0,1,1,1,1,0,1,0,1,1,1,1,0,1,0};
			    int Len=Input.length();//密文长度
				byte Aplaintext[];
				char[] strChar=Input.toCharArray();
				byte ciphertext[]=new byte[Len];
				for(int i=0;i<Len;i++) {
					char c=strChar[i];
					if(c=='0') {//c=='0'
						ciphertext[i] = (byte) 0;
					}else {//c=='1'
						ciphertext[i] = (byte) 1;
					}
				}
				if(BDESlen<64)
				{
					Aplaintext=des.Decrypt_1(ciphertext,sKey,BDESlen);
				}
				else if(BDESlen%64==0)
				{
					Aplaintext=des.Decrypt_2(ciphertext,sKey,BDESlen);
				}
				else
				{
					Aplaintext=des.Decrypt_3(ciphertext,sKey,BDESlen);
					}
			    int lenn=Aplaintext.length;
			    String aplaintext="";
			    for(int i=0;i<lenn;i++) {
					if(Aplaintext[i]==(byte)0) {
						aplaintext=aplaintext+'0';
					}
					else if(Aplaintext[i]==(byte)1)
					{
						aplaintext=aplaintext+'1';
					}
				}				 
			    // 反向转换
			 	String newStr = des.toString(aplaintext);
			 	M=newStr.substring(0,ToBMLen);
			 	RSAM=newStr.substring(ToBMLen,newStr.length());
			    textField_5.setText(M);
			    textField_6.setText(RSAM);;
			}else if(rdbtnAes.isSelected()){
				//AES解密
				String key="00012001710198aeda79171460153594";
				AES aes=new AES();
				String AESResult;
				AESResult=aes.DecryptAes(aes,BAESCipherText,BAESlen);
				M=AESResult.substring(0,ToBMLen);
				RSAM=AESResult.substring(ToBMLen,AESResult.length());
				textField_5.setText(M);
				textField_6.setText(RSAM);
			}else {
				NullSelected NS = new NullSelected();
				NS.frame.setVisible(true);
			}
			}
		});
		btnDesres.setBounds(267, 43, 240, 35);
		panel_1.add(btnDesres);
		btnDesres.setFont(new Font("宋体", Font.PLAIN, 30));
		
		textField_6 = new JTextField();
		textField_6.setBounds(555, 104, 200, 100);
		panel_1.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel lblHashm = new JLabel("RSA(Hash(M))\uFF1A");
		lblHashm.setBounds(348, 104, 215, 43);
		panel_1.add(lblHashm);
		lblHashm.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JButton button = new JButton("\u6BD4\u8F83");
		button.setBounds(369, 981, 153, 37);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//
				if(Signature.equals(StrBigInoutm))
				{
					Success sc=new Success();
					sc.frame.setVisible(true);
				}
				else {
					Failed fd=new Failed();
					fd.frame.setVisible(true);
				}
			}
		});
		button.setFont(new Font("宋体", Font.PLAIN, 30));
		frame.getContentPane().add(button);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(58, 728, 391, 232);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		panel_2.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Border titleBorder3 = BorderFactory.createTitledBorder("Hash");
		panel_2.setBorder(titleBorder3);
		
		textField_7 = new JTextField();
		textField_7.setBounds(137, 99, 228, 112);
		panel_2.add(textField_7);
		textField_7.setColumns(10);
		
		JLabel lblHashm_1 = new JLabel("Hash(M):");
		lblHashm_1.setBounds(21, 99, 123, 35);
		panel_2.add(lblHashm_1);
		lblHashm_1.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JRadioButton rdbtnSha = new JRadioButton("SHA-1");
		rdbtnSha.setBounds(17, 40, 123, 42);
		panel_2.add(rdbtnSha);
		rdbtnSha.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JRadioButton rdbtnMd = new JRadioButton("MD5");
		rdbtnMd.setBounds(137, 45, 91, 37);
		panel_2.add(rdbtnMd);
		rdbtnMd.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JButton btnm = new JButton("\u7B7E\u540DM");
		btnm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnSha.isSelected()) {
					//SHA-1签名
					SHA1 sha1=new SHA1();
					Signature=sha1.hex_sha1(M);
					textField_7.setText(Signature);
				}else if(rdbtnMd.isSelected()){
					//MD5签名
					MD5 md5=new MD5();
					Signature=md5.start(M);
					textField_7.setText(Signature);
				}else {
					NullSelected NS = new NullSelected();
					NS.frame.setVisible(true);
				}
			}
		});
		btnm.setBounds(232, 41, 123, 37);
		panel_2.add(btnm);
		btnm.setFont(new Font("宋体", Font.PLAIN, 30));
		((javax.swing.border.TitledBorder) panel_2.getBorder()).setTitleFont(new Font("宋体",Font.PLAIN,28));
        panel_2.repaint();
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(455, 728, 391, 232);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		panel_3.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Border titleBorder4 = BorderFactory.createTitledBorder("RSA");
		panel_3.setBorder(titleBorder1);
		
		textField_8 = new JTextField();
		textField_8.setBounds(139, 99, 220, 112);
		panel_3.add(textField_8);
		textField_8.setColumns(10);
		
		JLabel lblHashm_1_1 = new JLabel("Hash(M):");
		lblHashm_1_1.setBounds(21, 93, 123, 35);
		panel_3.add(lblHashm_1_1);
		lblHashm_1_1.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JButton btnNewButton_1 = new JButton("RSA\u89E3\u5BC6");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//导入A的公私钥并解密RSAM
				RSA rsa=new RSA();
				BigInteger input = new BigInteger(RSAM,16);//将字符串转换成10进制大整数
				BigInteger outm=rsa.decrypt(input,N,Ad);//解密明文
				StrBigInoutm=outm.toString(16);
				textField_8.setText(StrBigInoutm);
			}
		});
		btnNewButton_1.setFont(new Font("宋体", Font.PLAIN, 30));
		btnNewButton_1.setBounds(21, 45, 211, 37);
		panel_3.add(btnNewButton_1);
		
		JButton button_1 = new JButton("\u63A5\u6536\u5BC6\u6587");
		button_1.setBounds(72, 123, 176, 37);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText(ToB);
			}
		});
		button_1.setFont(new Font("宋体", Font.PLAIN, 30));
		frame.getContentPane().add(button_1);
		((javax.swing.border.TitledBorder) panel_3.getBorder()).setTitleFont(new Font("宋体",Font.PLAIN,28));
        panel_3.repaint();
		
		((javax.swing.border.TitledBorder) panel_1.getBorder()).setTitleFont(new Font("宋体",Font.PLAIN,28));
        panel_1.repaint();
        textField.setFont(font);
        textField_1.setFont(font);
        textField_2.setFont(font);
        textField_3.setFont(font);
        textField_4.setFont(font);
        textField_5.setFont(font);
        textField_6.setFont(font);
        textField_7.setFont(font);
        textField_8.setFont(font);
        textField_9.setFont(font);
        //用来设置窗口随屏幕大小改变
      	sizeWindowOnScreen(this, 0.6, 0.6);
	}
	
	public void sizeWindowOnScreen(DecryptSystem decryptSystem, double widthRate,
			double heightRate)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
 
		decryptSystem.setSize(new Dimension((int) (screenSize.width * widthRate),
				(int) (screenSize.height * heightRate)));
		}
}
