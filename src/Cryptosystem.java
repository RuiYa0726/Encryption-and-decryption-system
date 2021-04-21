import java.awt.EventQueue;
import DES.DESUtil;
import RSA.RSA;
import AES.AES;
import AES.word;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.Random;

import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import HASH.MD5;
import HASH.SHA1;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;

public class Cryptosystem extends JFrame{

	public JFrame frame;
	String content;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_8;
	private JTextField textField_4;
	private File openFile;                        //文件类
	private FileInputStream fileInputStream;       //字节文件输入流 
	private FileOutputStream fileOutputStream;     //字节文件输出流
	private OutputStreamWriter outputStreamWriter; //字符文件输出
	RSA rsa=new RSA();
	BigInteger p = BigInteger.probablePrime(new Random().nextInt(100) + 200, new Random());
	BigInteger q = BigInteger.probablePrime(new Random().nextInt(100) + 200, new Random());
	BigInteger n = p.multiply(q);
	BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));//欧拉函数，BigInteger.ONE：基本常量1
	BigInteger Ae1;
	BigInteger Be1;
	BigInteger Ad;
	BigInteger Bd;
	BigInteger m;
	BigInteger c;
	String Inputs="";
	private JTextField textField;
	private JTextField textField_7;
	String key;
	String Ciphertext="";
	String toB="";
	int ToBLenKey_1;
	int ToBMLen_1;
	int DESlen=0;//DES初始明文长度
	int AESlen=0;//AES初始明文长度
	int RSAlen=0;//RSA加密对称密钥的密文长度
	word[] AESCipherText;//AES加密的密文
	
	//byte[]的输出函数
	static void Print(byte[] s) {
		for(int i=0;i<s.length;i++) {
			if(i%8==0)
				System.out.printf(" ");
				System.out.print(s[i]);
				}
		System.out.printf("\n");
			}
	
	//每隔8位添加一个空格
	public String addblankinmiddle(String str) {
		//字符串长度
		int strlenth=str.length();
		//需要加空格数量
		int blankcount=0;
		//判断字符串长度
		if(strlenth<=8) {
		blankcount=0;
		}else {
		blankcount= strlenth%8>0?strlenth/8:str.length()/8-1; //需要加空格数量
		}
		//插入空格
		if(blankcount>0) {
		for(int i=0;i<blankcount;i++) {
		str=str.substring(0, (i+1)*8+i)+" "+str.substring((i+1)*8+i,strlenth+i);
		}
		}else {
		System.out.println("输入的字符串不多于8位，不需要添加空格");
		}
		//返回
		return str;
		}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cryptosystem window = new Cryptosystem();
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
	public Cryptosystem() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame("194588陶蕊");
		frame.getContentPane().setFont(new Font("宋体", Font.PLAIN, 30));
		frame.setBounds(100, 100, 871, 1441);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel lbla = new JLabel("\u6B22\u8FCE\u60A8\uFF0C\u7528\u6237A\uFF01");
		lbla.setBounds(323, 21, 304, 45);
		lbla.setFont(new Font("宋体", Font.PLAIN, 30));
		Font font = new Font("宋体", Font.PLAIN, 30);
		
		
		JButton btnrsaa = new JButton("\u4E00\u952E\u751F\u6210RSA\u4FE1\u606F\u53CAA\u7684\u516C\u79C1\u94A5");
		btnrsaa.setBounds(79, 391, 431, 37);
		btnrsaa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				textField_2.setText(p.toString(16));
				textField_3.setText(q.toString(16));
				textField_4.setText(n.toString(16));
				do {
				    Ae1 = new BigInteger(new Random().nextInt(phi_n.bitLength() - 1) + 1, new Random());
				} while (Ae1.compareTo(phi_n) != -1 || Ae1.gcd(phi_n).intValue() != 1);
				textField_5.setText(Ae1.toString(16));
				Ad = Ae1.modPow(new BigInteger("-1"), phi_n);//modPow模逆运算
				textField_6.setText(Ad.toString(16));		
			}
		});
		btnrsaa.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JButton button_1 = new JButton("\u9009\u62E9\u6587\u4EF6\uFF1A");
		button_1.setBounds(79, 256, 189, 37);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//String TextPutIns_1=textField_1.getText();
				JFileChooser chooser = new JFileChooser(); //文件选择
				//chooser.setBounds(100, 200, 500, 500);	
				//chooser.setSize(1000, 1000);
		        chooser.setFont(font);
				chooser.showOpenDialog(chooser);        //打开文件选择窗	
				openFile = chooser.getSelectedFile();  	//获取选择的文件
				textField_1.setText(openFile.getPath());	//获取选择文件的路径
				openFile = new File(textField_1.getText());
				try {
					if(!openFile.exists()){      //如果文件不存在
						openFile.createNewFile();//创建文件
					}
					fileInputStream = new FileInputStream(openFile); //创建文件输入流
					byte b[] = new byte[(int) openFile.length()];  //定义文件大小的字节数据
					fileInputStream.read(b);          //将文件数据存储在b数组
					content = new String(b,"UTF-8"); //将字节数据转换为UTF-8编码的字符串
					fileInputStream.close();          //关闭文件输入流
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		button_1.setFont(new Font("宋体", Font.PLAIN, 30));	
		JLabel label = new JLabel("\u8F93\u5165\u5B57\u7B26\u4E32\uFF1A");
		label.setBounds(79, 103, 189, 50);
		label.setFont(new Font("宋体", Font.PLAIN, 30));
		
		
		JLabel label_1 = new JLabel("\u6216");
		label_1.setBounds(79, 215, 32, 29);
		label_1.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JLabel lblp = new JLabel("\u7D20\u6570p\uFF1A");
		lblp.setBounds(79, 449, 108, 29);
		lblp.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JLabel lblq = new JLabel("\u7D20\u6570q\uFF1A");
		lblq.setBounds(79, 499, 108, 29);
		lblq.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JLabel lbln = new JLabel("\u6A21\u6570n\uFF1A");
		lbln.setBounds(79, 557, 108, 29);
		lbln.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JLabel lble = new JLabel("\u516C\u94A5\uFF1A");
		lble.setBounds(79, 609, 108, 29);
		lble.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JLabel lblq_1 = new JLabel("\u79C1\u94A5\uFF1A");
		lblq_1.setBounds(79, 667, 108, 29);
		lblq_1.setFont(new Font("宋体", Font.PLAIN, 30));
		
		textField_2 = new JTextField();
		textField_2.setBounds(179, 447, 585, 37);
		textField_2.setColumns(10);
        textField_2.setFont(font);
		textField_3 = new JTextField();
		textField_3.setBounds(179, 497, 585, 37);
		textField_3.setColumns(10);
        textField_3.setFont(font);
		textField_5 = new JTextField();
		textField_5.setBounds(179, 607, 464, 37);
		textField_5.setColumns(10);
        textField_5.setFont(font);
		textField_6 = new JTextField();
		textField_6.setBounds(179, 659, 464, 37);
		textField_6.setColumns(10);
        textField_6.setFont(font);
		JLabel lbla_1 = new JLabel("\u52A0\u5BC6\u7ED3\u679C\uFF1A");
		lbla_1.setBounds(79, 775, 179, 29);
		lbla_1.setFont(new Font("宋体", Font.PLAIN, 30));
		
		byte sKey_1[]= {0,0,1,1,0,0,0,1,0,0,1,1,0,0,1,0,0,0,1,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,1,1,0,1,0,1,0,0,1,1,0,1,1,0,0,0,1,1,0,1,1,1,0,0,1,1,1,0,0,0};
		byte sKey_2[]= {1,1,1,0,0,1,0,1,0,0,1,1,1,0,1,1,0,1,1,1,1,0,1,0,0,1,1,0,0,1,0,1,1,0,1,0,0,1,0,1,0,0,1,1,0,1,0,0,1,0,1,1,1,1,0,1,0,1,1,1,1,0,1,0};
		byte sKey_3[]= {0,1,0,1,0,1,0,0,1,0,1,1,0,0,1,0,0,0,1,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,1,1,0,1,0,1,0,0,1,1,0,1,1,0,0,0,1,1,0,1,1,1,0,0,1,1,1,0,0,0};
		byte sKey_4[]= {0,0,1,1,0,0,0,1,0,0,1,1,0,0,1,0,0,0,1,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,1,1,0,1,0,1,0,0,1,1,0,1,1,0,0,0,1,1,0,1,1,1,0,0,1,1,1,0,0,0};
		byte sKey_5[]= {0,0,1,1,0,0,0,1,0,0,1,1,0,0,1,0,0,0,1,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,1,1,0,1,0,1,0,0,1,1,0,1,1,0,0,0,1,1,0,1,1,1,0,0,1,1,1,0,0,0};
		
		JButton btnb = new JButton("\u53D1\u9001\u81F3\u7528\u6237B");
		btnb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecryptSystem decryptSystem=new DecryptSystem();
				decryptSystem.frame.setVisible(true);	
				decryptSystem.toBLenKey=ToBLenKey_1;
				decryptSystem.ToB=toB;
				decryptSystem.ToBMLen=ToBMLen_1;
				decryptSystem.N=n;
				decryptSystem.be=Be1;
				decryptSystem.bd=Bd;
				decryptSystem.BDESlen=DESlen;
				decryptSystem.BRSAlen=RSAlen;
				decryptSystem.BAESCipherText=AESCipherText;
				decryptSystem.BAESlen=AESlen;
				decryptSystem.Ad=Ad;
				decryptSystem.c=c;
				System.out.println("发送给B的密钥长度为："+decryptSystem.toBLenKey);
				System.out.println("发送给B的密文为："+decryptSystem.ToB);
				System.out.println("发送给B的明文为："+decryptSystem.ToBMLen);
				System.out.println("发送给B的N："+decryptSystem.N);
				System.out.println("发送给B的RSA公钥为："+decryptSystem.be);
				System.out.println("发送给B的RSA私钥为："+decryptSystem.bd);
				System.out.println("发送给B的DES明文长度为："+decryptSystem.BDESlen);
			}
		});
		btnb.setBounds(294, 1300, 270, 37);
		btnb.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JPanel panel = new JPanel();
		panel.setBounds(63, 78, 723, 255);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Border titleBorder1 = BorderFactory.createTitledBorder("输入");
		panel.setBorder(titleBorder1);
		((javax.swing.border.TitledBorder) panel.getBorder()).setTitleFont(new Font("宋体",Font.PLAIN,28));
        panel.repaint();

		
		JLabel lblHash = new JLabel("\u9009\u62E9Hash\uFF1A");
		lblHash.setBounds(79, 723, 153, 30);
		lblHash.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(63, 339, 799, 578);
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Border titleBorder2 = BorderFactory.createTitledBorder("Hash|RSA");
		panel_1.setBorder(titleBorder2);
		panel_1.setLayout(null);
		
		textField_4 = new JTextField();
		textField_4.setBounds(114, 214, 468, 35);
		panel_1.add(textField_4);
		textField_4.setColumns(10);
		((javax.swing.border.TitledBorder) panel_1.getBorder()).setTitleFont(new Font("宋体",Font.PLAIN,28));
        panel_1.repaint();
        textField_4.setFont(font);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(56, 940, 730, 339);
		panel_2.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Border titleBorder3 = BorderFactory.createTitledBorder("DES/AES/RSA");
		panel_2.setBorder(titleBorder3);
		panel_2.setLayout(null);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lbla);
		frame.getContentPane().add(btnrsaa);
		frame.getContentPane().add(button_1);
		frame.getContentPane().add(label);
		frame.getContentPane().add(label_1);
		frame.getContentPane().add(lblp);
		frame.getContentPane().add(lblq);
		frame.getContentPane().add(lbln);
		frame.getContentPane().add(lble);
		frame.getContentPane().add(lblq_1);
		frame.getContentPane().add(textField_2);
		frame.getContentPane().add(textField_3);
		frame.getContentPane().add(textField_5);
		frame.getContentPane().add(textField_6);
		frame.getContentPane().add(lbla_1);
		frame.getContentPane().add(btnb);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		textField_1 = new JTextField();
		textField_1.setBounds(227, 174, 475, 41);

        textField_1.setFont(font);
		panel.add(textField_1);
		textField_1.setColumns(10);
		textField_7 = new JTextField();
		textField_7.setBounds(227, 35, 475, 124);
        textField_7.setFont(font);
		panel.add(textField_7);
		textField_7.setColumns(10);

		frame.getContentPane().add(lblHash);
		frame.getContentPane().add(panel_1);
		
		JRadioButton rdbtnSha = new JRadioButton("SHA-1");
		rdbtnSha.setFont(new Font("宋体", Font.PLAIN, 30));
		rdbtnSha.setBounds(173, 375, 114, 35);
		panel_1.add(rdbtnSha);
		rdbtnSha.setVisible(true);
		
		JRadioButton rdbtnMd = new JRadioButton("MD5");
		rdbtnMd.setFont(new Font("宋体", Font.PLAIN, 30));
		rdbtnMd.setBounds(318, 374, 99, 37);
		panel_1.add(rdbtnMd);
		
		ButtonGroup buttonGroup=new ButtonGroup();
		buttonGroup.add(rdbtnSha);
		buttonGroup.add(rdbtnMd);

		JButton btnRsa = new JButton("Hash\u7B7E\u540D\u53CARSA\u52A0\u5BC6");
		btnRsa.setBounds(418, 374, 294, 36);
		panel_1.add(btnRsa);
		btnRsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				String TextPutIns=textField_7.getText();
				String FilePutIns=textField_1.getText();
				String Signature="";
				if(TextPutIns.equals("")) {//输入为空
					if(FilePutIns.equals("")) {//文件上传也为空
						NullInput NI = new NullInput();
						NI.frame.setVisible(true);
					}
					else {//读取文件内容
						Inputs=content;					
					}
				}else {
					Inputs=TextPutIns;
				}
				ToBMLen_1=Inputs.length();
				if(rdbtnSha.isSelected()) {
					//SHA-1签名
					SHA1 sha1=new SHA1();
					Signature=sha1.hex_sha1(Inputs);
				//System.out.println(SHAText);
				}else if(rdbtnMd.isSelected()){
					//MD5签名
					MD5 md5=new MD5();
					Signature=md5.start(Inputs);
				}else {
					NullSelected NS = new NullSelected();
					NS.frame.setVisible(true);
				}
				m = new BigInteger(Signature,16);//将字符串转换成10进制大整数
				c=rsa.encrypt(m,n,Ae1);//密文
				BigInteger outm=rsa.decrypt(c,n,Ad);//解密明文
				System.out.println("哈希签名的结果为："+Signature);
				System.out.println("RSA加密的密文为："+c.toString(16));
				System.out.println("RSA解密的密文为："+outm.toString(16));
				textField.setText(c.toString(16));
			}
		});
		btnRsa.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JButton btnNewButton = new JButton("\u4FDD\u5B58");
		btnNewButton.addActionListener(new ActionListener() {//保存文件
			public void actionPerformed(ActionEvent e) {
			     JFileChooser chooser = new JFileChooser();
	                if (chooser.showSaveDialog(btnNewButton)==JFileChooser.APPROVE_OPTION) {
	                    File file = chooser.getSelectedFile();
	                    FileOutputStream fos= null;
	                    try {
	                        fos=new FileOutputStream(file.getPath());
	                        fos.write(textField_4.getText().getBytes());
	                        fos.close();
	                    } catch (Exception e1) {
	                        // TODO Auto-generated catch block
	                        e1.printStackTrace();
	                    }
	            }
					//SuccessSaved SuSa = new SuccessSaved();
					//SuSa.frame.setVisible(true);          
			}
		});
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 30));
		btnNewButton.setBounds(594, 215, 114, 37);
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u4FDD\u5B58");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			     JFileChooser chooser = new JFileChooser();
	                if (chooser.showSaveDialog(btnNewButton_1)==JFileChooser.APPROVE_OPTION) {
	                    File file = chooser.getSelectedFile();
	                    FileOutputStream fos= null;
	                    try {
	                        fos=new FileOutputStream(file.getPath());
	                        fos.write(textField_5.getText().getBytes());
	                        fos.close();
	                    } catch (Exception e1) {
	                        // TODO Auto-generated catch block
	                        e1.printStackTrace();
	                    }
	            }
			}
		});
		btnNewButton_1.setFont(new Font("宋体", Font.PLAIN, 30));
		btnNewButton_1.setBounds(594, 270, 114, 37);
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("\u4FDD\u5B58");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			     JFileChooser chooser = new JFileChooser();
	                if (chooser.showSaveDialog(btnNewButton_2)==JFileChooser.APPROVE_OPTION) {
	                    File file = chooser.getSelectedFile();
	                    FileOutputStream fos= null;
	                    try {
	                        fos=new FileOutputStream(file.getPath());
	                        fos.write(textField_6.getText().getBytes());
	                        fos.close();
	                    } catch (Exception e1) {
	                        // TODO Auto-generated catch block
	                        e1.printStackTrace();
	                    }
	            }
			}
		});
		btnNewButton_2.setFont(new Font("宋体", Font.PLAIN, 30));
		btnNewButton_2.setBounds(594, 322, 114, 35);
		panel_1.add(btnNewButton_2);
		
		JButton btnb_1 = new JButton("\u751F\u6210B\u7684\u516C\u79C1\u94A5");
		btnb_1.setBounds(463, 51, 240, 37);
		panel_1.add(btnb_1);
		btnb_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do {
				    Be1 = new BigInteger(new Random().nextInt(phi_n.bitLength() - 1) + 1, new Random());
				} while (Be1.compareTo(phi_n) != -1 || Be1.gcd(phi_n).intValue() != 1);
				textField_5.setText(Be1.toString(16));
				Bd = Be1.modPow(new BigInteger("-1"), phi_n);//modPow模逆运算
				textField_6.setText(Bd.toString(16));
			}
		});
		btnb_1.setFont(new Font("宋体", Font.PLAIN, 30));
		
		textField = new JTextField();
		textField.setBounds(160, 427, 375, 130);
		panel_1.add(textField);
		textField.setFont(font);
		textField.setColumns(10);
		frame.getContentPane().add(panel_2);
		
		ButtonGroup buttonGroup_1=new ButtonGroup();
		
		JLabel label_3 = new JLabel("\u9009\u62E9\u5BF9\u79F0\u52A0\u5BC6\u7B97\u6CD5\u53CA\u5BC6\u94A5\uFF1A");
		label_3.setBounds(21, 54, 380, 32);
		panel_2.add(label_3);
		label_3.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(381, 54, 308, 32);
		panel_2.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"------\u8BF7\u9009\u62E9------", "DES\u53CA\u5BC6\u94A5\uFF1Ae53b7a65a534c000", "AES\u53CA\u5BC6\u94A5\uFF1A00012001710198aeda79171460153594"}));
		comboBox.setBackground(Color.WHITE);
		comboBox.setFont(font);
		
		textField_8 = new JTextField();
		textField_8.setBounds(166, 168, 543, 143);
        textField_8.setFont(font);
		panel_2.add(textField_8);
		textField_8.setColumns(10);
		
		JButton btnrsa = new JButton("\u5BF9\u79F0\u52A0\u5BC6&RSA\u52A0\u5BC6\u5BF9\u79F0\u5BC6\u94A5");
		btnrsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//组合明文与加密后的密文
				String Input=c.toString(16);
				Input=Inputs+Input;
				System.out.println("明文字符串Input:"+Input);
				String Key_1=comboBox.getSelectedItem().toString();
				byte Aplaintext[];
				byte ciphertext[];		
				if(Key_1.equals("DES及密钥：e53b7a65a534c000")) {
					//DES加密
					key="e53b7a65a534c000";
					DESUtil des=new DESUtil();
					// 转换为二进制字符串
					String binaryStr = des.toBinary(Input);
					System.out.println("二进制Input"+binaryStr);
					int len=binaryStr.length();
					DESlen=len;
					byte plaintext[]=new byte[len];
					for(int i=0;i<len;i++) {
						char c=binaryStr.charAt(i);
						if(c=='0') {//c=='0'
							plaintext[i] = (byte) 0;
						}else {//c=='1'
							plaintext[i] = (byte) 1;
						}
					}	
					System.out.printf("加密前明文是：");
					Print(plaintext);
					byte sKey[]= {1,1,1,0,0,1,0,1,0,0,1,1,1,0,1,1,0,1,1,1,1,0,1,0,0,1,1,0,0,1,0,1,1,0,1,0,0,1,0,1,0,0,1,1,0,1,0,0,1,0,1,1,1,1,0,1,0,1,1,1,1,0,1,0};
					if(len==0||len<64)
					{
						ciphertext = des.Encrypt_1(plaintext,sKey);
						Aplaintext=des.Decrypt_1(ciphertext,sKey,len);
					}
					else if(len%64==0)
					{
						ciphertext = des.Encrypt_2(plaintext,sKey);
						Aplaintext=des.Decrypt_2(ciphertext,sKey,len);
					}
					else
					{
						ciphertext = des.Encrypt_3(plaintext,sKey);
						Aplaintext=des.Decrypt_3(ciphertext,sKey,len);
					}
					int llen=ciphertext.length;
					int lenn=Aplaintext.length;
					
					String aplaintext="";
					for(int i=0;i<llen;i++) { 
						if(ciphertext[i]==(byte)0) {
							Ciphertext=Ciphertext+'0';
						}
						else if(ciphertext[i]==(byte)1)
						{
							Ciphertext=Ciphertext+'1';
						}
					}
					for(int i=0;i<lenn;i++) {
						if(Aplaintext[i]==(byte)0) {
							aplaintext=aplaintext+'0';
						}
						else if(Aplaintext[i]==(byte)1)
						{
							aplaintext=aplaintext+'1';
						}
					}	
					System.out.println ("01字符串Ciphertext:"+Ciphertext);
					System.out.println ("01字符串aplaintext："+aplaintext);
				    // 反向转换
				 	String newStr = des.toString(aplaintext);
				 	System.out.println("解密后的明文是："+newStr);
					textField_8.setText(Ciphertext);
					ToBLenKey_1=16;
				}else if(Key_1.equals("AES及密钥：00012001710198aeda79171460153594")){
					//AES加密
					//解决AES明文长度问题;
					AESlen=Input.length();
					key="00012001710198aeda79171460153594";
					ToBLenKey_1=32;
					AES aes=new AES();
					String Result_3,Result_2;
					AESCipherText=aes.EncryptAes(aes, Input);
					Result_3=aes.wordArrStr(AESCipherText);
					System.out.println("明文" + Input);  
				    System.out.println("密文" + Result_3); 	
				 	Result_2=aes.DecryptAes(aes,AESCipherText,Input.length());
				    Ciphertext=Result_3;
					textField_8.setText(Result_3);
				}else {
					NullSelected_1 NS = new NullSelected_1();
					NS.frame.setVisible(true);
				}
			//RSA加密B的公钥
			BigInteger BigInkey = new BigInteger(key,16);//将字符串转换成10进制大整数
			BigInteger BigInc=rsa.encrypt(BigInkey,n,Be1);//密文
			BigInteger BigInoutm=rsa.decrypt(BigInc,n,Bd);//解密明文
			String StrBigInc=BigInc.toString(16);
			RSAlen=StrBigInc.length();//RSA加密的B的密文长度
			System.out.println("RSA加密的Bkey密文为："+StrBigInc);
			String StrBigInoutm=BigInoutm.toString(16);
			System.out.println("RSA解密的Bkey密文为："+StrBigInoutm);
			//组合DES、AES加密结果 和 B公钥加密结果
			toB=Ciphertext+StrBigInc;
			}		
		});
		btnrsa.setBounds(21, 107, 417, 40);
		panel_2.add(btnrsa);
		btnrsa.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JLabel label_4 = new JLabel("\u52A0\u5BC6\u7ED3\u679C\uFF1A");
		label_4.setBounds(21, 159, 153, 45);
		panel_2.add(label_4);
		label_4.setFont(new Font("宋体", Font.PLAIN, 30));
		
		((javax.swing.border.TitledBorder) panel_2.getBorder()).setTitleFont(new Font("宋体",Font.PLAIN,28));
        panel_2.repaint();
        //用来设置窗口随屏幕大小改变
        sizeWindowOnScreen(this, 0.6, 0.6);
      //  JScrollPane   sp   =   new   JScrollPane(panel_3);
    //    frame.getContentPane().add(sp);
      //分别设置水平和垂直滚动条总是出现
    //    sp.setHorizontalScrollBarPolicy(       
     //           JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    //    sp.setVerticalScrollBarPolicy(   
         //       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
  	}

  	public void sizeWindowOnScreen(Cryptosystem cryptosystem, double widthRate,
  			double heightRate)
  	{
  		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   
  		cryptosystem.setSize(new Dimension((int) (screenSize.width * widthRate),
  				(int) (screenSize.height * heightRate)));
  		}

}
