package AES;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class AES {
	int Nb;                 
	int Nk;
	int Nr;                 
	word[][] RoundKey;      
	word[][] InvRoundKey;   
	   byte[] key = {
			(byte) 0x00, (byte) 0x01, (byte) 0x20, (byte) 0x01, 
			(byte) 0x71, (byte) 0x01, (byte) 0x98, (byte) 0xae, 
			(byte) 0xda, (byte) 0x79, (byte) 0x17, (byte) 0x14, 
			(byte) 0x60, (byte) 0x15, (byte) 0x35, (byte) 0x94};
	/**
	 * 
	 * @param plaintext  
	 * @param CipherKey  
	 * @return
	 */
	public word[] encrypt(word[] plaintext, word[] CipherKey) {
	    Nb = 4;
	    Nk = CipherKey.length;
	    Nr = Nk + 6;

	    RoundKey = KeyExpansion(CipherKey);
	    word[] ciphertext = new word[plaintext.length];
	    for (int i = 0; i < plaintext.length; i++) 
	        ciphertext[i] = new word(plaintext[i]);
	    ciphertext = AddRoundKey(ciphertext, RoundKey[0]);

	    for (int i = 1; i < Nr + 1; i++) {
	        ciphertext = ByteSub(ciphertext);
	        ciphertext = ShiftRow(ciphertext);
	        if (i != Nr) ciphertext = MixColumn(ciphertext);
	        ciphertext = AddRoundKey(ciphertext, RoundKey[i]);
	    }
	    return ciphertext;
	}

	/**
	 * @param ciphertext  
	 * @param CipherKey  
	 * @return
	 */
	public word[] decrypt(word[] ciphertext, word[] CipherKey) {
	    Nb = 4;
	    Nk = CipherKey.length;
	    Nr = Nk + 6;

	    InvRoundKey = InvKeyExpansion(CipherKey);
	    word[] plaintext = new word[ciphertext.length];
	    for (int i = 0; i < ciphertext.length; i++) 
	        plaintext[i] = new word(ciphertext[i]);
	    plaintext = AddRoundKey(plaintext, InvRoundKey[Nr]);
	    for (int i = Nr - 1; i >= 0; i--) {
	        plaintext = InvByteSub(plaintext);
	        plaintext = InvShiftRow(plaintext);
	        if (i != 0) plaintext = InvMixColumn(plaintext);
	        plaintext = AddRoundKey(plaintext, InvRoundKey[i]);
	    }
	    return plaintext;
	}
/**************************************************************************************************/
	/**
	 * @param state
	 * @return
	 */
	public word[] ByteSub(word[] state) {
	    for (int i = 0; i < Nb; i++) 
	        for (int j = 0; j < 4; j++) {
	            state[i].word[j] = word.inverse(state[i].word[j]);
	            state[i].word[j] = AffineTransformation(state[i].word[j], 'C');
	        }
	    return state;
	}

	/**
	 * @param state
	 * @return
	 */
	public word[] ShiftRow(word[] state) {
	    byte[][] b = new byte[4][Nb];
	    for (int j = 0; j < Nb; j++) 
	        for (int i = 0; i < 4; i++) 
	            b[i][j] = state[j].word[i];
	    for (int i = 1; i < 4; i++) 
	        for (int k = 0; k < i; k++) {
	            byte t = b[i][0];
	            for (int j = 0; j < Nb - 1; j++) 
	                b[i][j] = b[i][j + 1];
	            b[i][Nb - 1] = t;
	        }
	    for (int j = 0; j < Nb; j++) 
	        for (int i = 0; i < 4; i++) 
	            state[j].word[i] = b[i][j];
	    return state;
	}

	/**
	 * @param state
	 * @param key
	 * @return
	 */
	public word[] AddRoundKey(word[] state, word[] key) {
	    for (int i = 0; i < Nb; i++) 
	        state[i] = word.add(state[i], key[i]);
	    return state;
	}

	/**
	 * @param state
	 * @return
	 */
	public word[] MixColumn(word[] state) {
	    byte[] b = {(byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x03};
	    word a = new word(b);
	    for (int i = 0; i < Nb; i++) 
	        state[i] = word.multiply(a, state[i]);
	    return state;
	}

	/**
	 * @param CipherKey
	 * @return
	 */
	public word[][] KeyExpansion(word[] CipherKey) {
	    word[] W = new word[Nb * (Nr + 1)];
	    word Temp;
	    if (Nk <= 6) {
	        for (int i = 0; i < Nk; i++) 
	            W[i] = CipherKey[i];
	        for (int i = Nk; i < W.length; i++) {
	            Temp = new word(W[i - 1]);
	            if (i % Nk ==0) 
	                Temp = word.add(SubByte(Rotl(Temp)), Rcon(i / Nk));
	            W[i] = word.add(W[i - Nk], Temp);
	        }
	    } else {
	        for (int i = 0; i < Nk; i++) 
	            W[i] = CipherKey[i];
	        for (int i = Nk; i < W.length; i++) {
	            Temp = new word(W[i - 1]);
	            if (i % Nk ==0) 
	                Temp = word.add(SubByte(Rotl(Temp)), Rcon(i / Nk));
	            else if (i % Nk == 4) 
	                Temp = SubByte(Temp);
	            W[i] = word.add(W[i - Nk], Temp);
	        }
	    }
	    word[][] RoundKey = new word[Nr + 1][Nb];
	    for (int i = 0; i < Nr + 1; i++) 
	        for (int j = 0; j < Nb; j++) 
	            RoundKey[i][j] = W[Nb * i + j];
	    return RoundKey;
	}

	/**
	 * @param state
	 * @return
	 */
	public word[] InvByteSub(word[] state) {
	    for (int i = 0; i < Nb; i++) 
	        for (int j = 0; j < 4; j++) {
	            state[i].word[j] = AffineTransformation(state[i].word[j], 'D');
	            state[i].word[j] = word.inverse(state[i].word[j]);
	        }
	    return state;
	}

	/**
	 * @param state
	 * @return
	 */
	public word[] InvShiftRow(word[] state) {
	    byte[][] b = new byte[4][Nb];
	    for (int j = 0; j < Nb; j++) 
	        for (int i = 0; i < 4; i++) 
	            b[i][j] = state[j].word[i];
	    for (int i = 1; i < 4; i++) 
	        for (int k = 0; k < Nb - i; k++) {
	            byte t = b[i][0];
	            for (int j = 0; j < Nb - 1; j++) 
	                b[i][j] = b[i][j + 1];
	            b[i][Nb - 1] = t;
	        }
	    for (int j = 0; j < Nb; j++) 
	        for (int i = 0; i < 4; i++) 
	            state[j].word[i] = b[i][j];
	    return state;
	}
	
	/**
	 * @param state
	 * @return
	 */
	public word[] InvMixColumn(word[] state) {
	    byte[] b = {(byte) 0x0E, (byte) 0x09, (byte) 0x0D, (byte) 0x0B};
	    word a = new word(b);
	    for (int i = 0; i < Nb; i++) 
	        state[i] = word.multiply(a, state[i]);
	    return state;
	}

	/**
	 * @param CipherKey
	 * @return
	 */
	public word[][] InvKeyExpansion(word[] CipherKey) {
	    word[][] InvRoundKey = KeyExpansion(CipherKey);
	    for (int i = 1; i < Nr; i++) 
	        InvRoundKey[i] = InvMixColumn(InvRoundKey[i]);
	    return InvRoundKey;
	}
/**************************************************************************************************/
	public word SubByte(word a) {
	    word w = new word(a);
	    for (int i = 0; i < 4; i++) {
	        w.word[i] = word.inverse(w.word[i]);
	        w.word[i] = AffineTransformation(w.word[i], 'C');
	    }
	    return w;
	}

	public word Rotl(word a) {
	    word w = new word(a);
	    byte b = w.word[0];
	    for (int i = 0; i < 3; i++) 
	        w.word[i] = w.word[i + 1];
	    w.word[3] = b;
	    return w;
	}

	public word Rcon(int n) {
	    word Rcon = new word(new byte[4]);
	    byte RC = 1;
	    for (int i = 1; i < n; i++) 
	        RC = word.xtime(RC);
	    Rcon.word[0] = RC;
	    return Rcon;
	}

	/**
	 * @param b
	 * @param sign 
	 * @return
	 */
	public byte AffineTransformation(byte b, char sign) {
	    byte[] x = Integer.toBinaryString((b & 0xff) + 0x100).substring(1).getBytes();
	    for (int i = 0; i < x.length; i++) x[i] -= '0';
	    if (sign == 'C') {
	        byte[] x_ = new byte[8];
	        byte b_ = 0;
	        for (int i = 0; i < 8; i++) {
	            x_[i] = (byte) (x[i] ^ x[(i + 1) % 8] ^ x[(i + 2) % 8] ^ x[(i + 3) % 8] ^ x[(i + 4) % 8]);
	            b_ += x_[i] * Math.pow(2, 7 - i);
	        }
	        return (byte) (b_ ^ 0x63);
	    } else {
	        byte[] x_ = new byte[8];
	        byte b_ = 0;
	        for (int i = 0; i < 8; i++) {
	            x_[i] = (byte) (x[(i + 1) % 8] ^ x[(i + 3) % 8] ^ x[(i + 6) % 8]);
	            b_ += x_[i] * Math.pow(2, 7 - i);
	        }
	        return (byte) (b_ ^ 0x05);
	    }
	}
	public word[] toWordArr(byte[] b) {
		int len = b.length / 4;
		if (b.length % 4 != 0) len++;
		word[] w = new word[len];
		for (int i = 0; i < len; i++) {
			byte[] c = new byte[4];
			if (i * 4 < b.length) {
				for (int j = 0; j < 4; j++)
					c[j] = b[i * 4 + j];
			}
			w[i] = new word(c);
		}
		return w;
	}

	public String wordArrStr(word[] w) {
		String str = "";
		for (word word : w)
			str += word;
		return str;
	}
	
	//byte[]的输出函数
	public void Print(byte[] s) {
			for(int i=0;i<s.length;i++) {
				if(i%8==0)
					System.out.printf(" ");
				System.out.print(s[i]);
			}
			System.out.printf("\n");
	}
	
	
	public String ASCII(String a) {//ASCII码与字符串的相互转换
		int len=a.length();
		char[] c=new char[len/2];
		for(int i=0,j=0;(i<len)&&(j<len/2);i=i+2,j++)
		{
			String b=a.substring(i,i+2);
			int d = Integer.parseInt(b,16);
			c[j]=(char)d;
		}
		String e=String.valueOf(c);
		return e;
	}
	
	   public byte generateByte(String s) {//将字符串变成byte字节
	        byte tmp = 0;
	        char[] arr = s.toCharArray();
	        if(arr.length > 2){
	            return tmp;
	        }
	        int t0 = Integer.parseInt(Character.toString(arr[0]), 16);
	        int t1 = Integer.parseInt(Character.toString(arr[1]), 16);
	        byte tmp0 = (byte)t0;
	        byte tmp1 = (byte)t1;
	        tmp = (byte) (tmp0 << 4);
	        tmp = (byte) (tmp | tmp1);
	        return tmp;
	    }
	   
	   public word[] EncryptAes(AES aes,String Input) {
		   	word[] CipherKey = aes.toWordArr(key);	
		   	System.out.println("密钥" + aes.wordArrStr(CipherKey));
		   	int len=Input.length();
			byte[] plain;
			word[] plaintext;
			int Len=0;
			if(len==16) {
				Len=4;
			}else {
				Len=(len/16+1)*4;
			}
			word[] cipherText=new word[Len];
			System.out.println("cipherText word[]密文长度："+cipherText.length);
			word[] FuZhucipherText;
			if(len<16) {//输入小于128位,后面补0
				while(len<16) {
					Input=Input+"0";
					len=Input.length();}
			    plain = Input.getBytes();//将输入变为byte字节
			    plaintext = aes.toWordArr(plain);
			    cipherText = aes.encrypt(plaintext, CipherKey);
			}else if(len%16==0) {//输入是128位的倍数
				for(int i=0;i<len/16;i++) {
					String input=Input.substring(i*16,(i+1)*16);
				    plain = input.getBytes();//将输入变为byte字节
				    plaintext = aes.toWordArr(plain);//将输入变成word类型
				    FuZhucipherText = aes.encrypt(plaintext, CipherKey);
				    for(int j=0;j<4;j++) {
				    	cipherText[4*i+j]=FuZhucipherText[j];
				    }
				}
				System.out.println("cipherText的长度为"+cipherText.length);
			}else{//输入大于128位且不是128位的倍数,后面补0
				while(len%16!=0) {
					Input=Input+"0";
					len=Input.length();}
				for(int i=0;i<len/16;i++) {
					String input=Input.substring(i*16,(i+1)*16);
				    plain = input.getBytes();//将输入变为byte字节
				    plaintext = aes.toWordArr(plain);//将输入变成word类型
				    FuZhucipherText = aes.encrypt(plaintext, CipherKey);
				    for(int j=0;j<4;j++) {
				    	cipherText[4*i+j]=FuZhucipherText[j];}
				   }
			}
		   	return cipherText;
	   }
	   
	   public String DecryptAes(AES aes,word[] Input,int Inputlen) {
		   	word[] CipherKey = aes.toWordArr(key);	
		   	word[] cipherText=new word[4];
			word[] newPlainText;//解密后的明文word[]类型
            String newPlainText_2="";//最终的解密输出
			byte[] cipher;
			for(int i=0;i<(Input.length/4);i++) {
					for(int j=0;j<4;j++) {
						cipherText[j]=Input[i*4+j];
					}
				    newPlainText = aes.decrypt(cipherText, CipherKey);
				    String newPlainText_1=aes.wordArrStr(newPlainText);
				    newPlainText_1=aes.ASCII(newPlainText_1);
				    newPlainText_2=newPlainText_2+newPlainText_1;
				}	
			newPlainText_2=newPlainText_2.substring(0,Inputlen);
			return newPlainText_2;
	   }
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		//AES块明文为128位  
		AES aes=new AES();
		String Input="hello world!";
		word[] Result_1;
		String Result_3,Result_2;
		Result_1=aes.EncryptAes(aes,Input);
		Result_3=aes.wordArrStr(Result_1);
		System.out.println("明文" + Input);  
	    System.out.println("密文" + Result_3); 	
	 	Result_2=aes.DecryptAes(aes,Result_1,Input.length());
	    System.out.println("解密明文" + Result_2); 	
	}
}
