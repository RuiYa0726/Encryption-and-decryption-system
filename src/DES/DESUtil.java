package DES;

public class DESUtil {
	//子密钥产生
	//置换选择1矩阵
	public int[] replace1C = {
	        57, 49, 41, 33, 25, 17,  9, 
	         1, 58, 50, 42, 34, 26, 18, 
	        10,  2, 59, 51, 43, 35, 27, 
	        19, 11,  3, 60, 52, 44, 36
	};
	public int[] replace1D = {
	        63, 55, 47, 39, 31, 23, 15, 
	         7, 62, 54, 46, 38, 30, 22, 
	        14,  6, 61, 53, 45, 37, 29, 
	        21, 13,  5, 28, 20, 12,  4
	};

	//循环左移位数表
	public int[] moveNum = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

	//置换选择2矩阵
	public int[] replace2 = {
	    14, 17, 11, 24,  1,  5, 
	     3, 28, 15,  6, 21, 10, 
	    23, 19, 12,  4, 26,  8, 
	    16,  7, 27, 20, 13,  2, 
	    41, 52, 31, 37, 47, 55, 
	    30, 40, 51, 45, 33, 48, 
	    44, 49, 39, 56, 34, 53, 
	    46, 42, 50, 36, 29, 32
	};
	/**
	 * 子密钥的产生
	 * @param sKey  64位密钥
	 * @return      16个48位子密钥
	 */
	public byte[][] generateKeys(byte[] sKey) {
	    byte[] C = new byte[28];
	    byte[] D = new byte[28];
	    byte[][] keys = new byte[16][48];
	    //置换选择1
	    for (int i = 0; i < 28; i++) {
	        C[i] = sKey[replace1C[i] - 1];
	        D[i] = sKey[replace1D[i] - 1];
	    }
	    for (int i = 0; i < 16; i++) {
	        //循环左移
	        C = RSHR(C, moveNum[i]);
	        D = RSHR(D, moveNum[i]);
	        //置换选择2
	        for (int j = 0; j < 48; j++) {
	            if (replace2[j] <= 28) keys[i][j] = C[replace2[j] - 1];
	            else keys[i][j] = D[replace2[j] - 28-1];
	        }
	    }
	    return keys;
	}
	
	/**
	 * 循环左移
	 * @param b  数组
	 * @param n  位数
	 * @return
	 */
	public byte[] RSHR(byte[] b, int n) {
	    String s = new String(b);
	    s = (s + s.substring(0, n)).substring(n);
	    return s.getBytes();
	}	
	//初始置换矩阵
	public int[] IP = {
	        58, 50, 42, 34, 26, 18, 10,  2, 
	        60, 52, 44, 36, 28, 20, 12,  4, 
	        62, 54, 46, 38, 30, 22, 14,  6, 
	        64, 56, 48, 40, 32, 24, 16,  8, 
	        57, 49, 41, 33, 25, 17,  9,  1, 
	        59, 51, 43, 35, 27, 19, 11,  3, 
	        61, 53, 45, 37, 29, 21, 13,  5, 
	        63, 55, 47, 39, 31, 23, 15,  7
	};	
	/**
	 * 初始置换IP
	 * @param text  64位数据
	 * @return
	 */
	public byte[] IP(byte[] text) {
	    byte[] newtext = new byte[64];
	    for (int i = 0; i < 64; i++) 
	        newtext[i] = text[IP[i] - 1];
	    return newtext;
	}
	//轮函数
	//选择运算矩阵
	public int[] E = {
	        32,  1,  2,  3,  4,  5, 
	         4,  5,  6,  7,  8,  9, 
	         8,  9, 10, 11, 12, 13, 
	        12, 13, 14, 15, 16, 17, 
	        16, 17, 18, 19, 20, 21, 
	        20, 21, 22, 23, 24, 25, 
	        24, 25, 26, 27, 28, 29, 
	        28, 29, 30, 31, 32,  1
	};

	//代替函数组
	public int[][][] S = {
	        //S1
	        {
	            {14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7}, 
	            { 0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8}, 
	            { 4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0}, 
	            {15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13}
	        }, 
	        //S2
	        {
	            {15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10}, 
	            { 3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5}, 
	            { 0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15}, 
	            {13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9}
	        }, 
	        //S3
	        {
	            {10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8}, 
	            {13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1}, 
	            {13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7}, 
	            { 1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12}
	        }, 
	        //S4
	        {
	            { 7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15}, 
	            {13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9}, 
	            {10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4}, 
	            { 3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14}
	        }, 
	        //S5
	        {
	            { 2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9}, 
	            {14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6}, 
	            { 4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14}, 
	            {11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3}
	        }, 
	        //S6
	        {
	            {12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11}, 
	            {10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8}, 
	            { 9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6}, 
	            { 4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13}
	        }, 
	        //S7
	        {
	            { 4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1}, 
	            {13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6}, 
	            { 1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2}, 
	            { 6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12}
	        }, 
	        //S8
	        {
	            {13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7}, 
	            { 1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2}, 
	            { 7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8}, 
	            { 2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11}
	        }
	};

	//置换运算矩阵
	public int[] P = {
	        16,  7, 20, 21, 
	        29, 12, 28, 17, 
	         1, 15, 23, 26, 
	         5, 18, 31, 10, 
	         2,  8, 24, 14, 
	        32, 27,  3,  9, 
	        19, 13, 30,  6, 
	        22, 11,  4, 25
	};
	/**
	 * 轮函数
	 * @param A  32位输入
	 * @param K  48位子密钥
	 * @return   32位输出
	 */
	public byte[] f(byte[] A, byte[] K) {
	    byte[] t = new byte[48];
	    byte[] r = new byte[32];
	    byte[] result = new byte[32];
	    //选择运算E
	    for (int i = 0; i < 48; i++) 
	        t[i] = A[E[i] - 1];
	    //模2相加
	    for (int i = 0; i < 48; i++) 
	        t[i] = (byte) (t[i] ^ K[i]);
	    //代替函数组S
	    for (int i = 0, a = 0; i < 48; i += 6, a += 4) {
	        int j = t[i] * 2 + t[i + 5];   //b1b6
	        int k = t[i + 1] * 8 + t[i + 2] * 4 + t[i + 3] * 2 + t[i + 4];   //b2b3b4b5
	        byte[] b = Integer.toBinaryString(S[i / 6][j][k] + 16).substring(1).getBytes();
	        for (int n = 0; n < 4; n++) 
	            r[a + n] = (byte) (b[n] - '0');
	    }
	    //置换运算P
	    for (int i = 0; i < 32; i++) 
	        result[i] = r[P[i] - 1];
	    return result;
	}
	//逆初始置换矩阵
	public int[] rIP = {
	        40,  8, 48, 16, 56, 24, 64, 32, 
	        39,  7, 47, 15, 55, 23, 63, 31, 
	        38,  6, 46, 14, 54, 22, 62, 30, 
	        37,  5, 45, 13, 53, 21, 61, 29, 
	        36,  4, 44, 12, 52, 20, 60, 28, 
	        35,  3, 43, 11, 51, 19, 59, 27, 
	        34,  2, 42, 10, 50, 18, 58, 26, 
	        33,  1, 41,  9, 49, 17, 57, 25
	};
	/**
	 * 逆初始置换IP^-1
	 * @param text  64位数据
	 * @return
	 */
	public byte[] rIP(byte[] text) {
	    byte[] newtext = new byte[64];
	    for (int i = 0; i < 64; i++) 
	        newtext[i] = text[rIP[i] - 1];
	    return newtext;
	}
	/**
	 * 加密
	 * @param plaintext  64位明文
	 * @param sKey       64位密钥
	 * @return           64位密文
	 */
	public byte[] encrypt(byte[] plaintext, byte[] sKey) {
	    byte[][] L = new byte[17][32];
	    byte[][] R = new byte[17][32];
	    byte[] ciphertext = new byte[64];
	    //子密钥的产生
	    byte[][] K = this.generateKeys(sKey);
	    //初始置换IP
	    plaintext = this.IP(plaintext);
	    //将明文分成左半部分L0和右半部分R0
	    for (int i = 0; i < 32; i++) {
	        L[0][i] = plaintext[i];
	        R[0][i] = plaintext[i + 32];
	    }
	    //加密迭代
	    for (int i = 1; i <= 16; i++) {
	        L[i] = R[i - 1];
	        R[i] = xor(L[i - 1], this.f(R[i - 1], K[i - 1]));
	    }
	    //以R16为左半部分，L16为右半部分合并
	    for (int i = 0; i < 32; i++) {
	        ciphertext[i] = R[16][i];
	        ciphertext[i + 32] = L[16][i];
	    }
	    //逆初始置换IP^-1
	    ciphertext = this.rIP(ciphertext);
	    return ciphertext;
	}
	/**
	 * 解密
	 * @param ciphertext  64位密文
	 * @param sKey        64位密钥
	 * @return            64位明文
	 */
	public byte[] decrypt(byte[] ciphertext, byte[] sKey,int len) {
	    byte[][] L = new byte[17][32];
	    byte[][] R = new byte[17][32];
	    byte[] plaintext = new byte[64];
	    byte[] plaintext_1 = new byte[len];
	    //子密钥的产生
	    byte[][] K = this.generateKeys(sKey);
	    //初始置换IP
	    ciphertext = this.IP(ciphertext);
	    //将密文分成左半部分R16和右半部分L16
	    for (int i = 0; i < 32; i++) {
	        R[16][i] = ciphertext[i];
	        L[16][i] = ciphertext[i + 32];
	    }
	    //解密迭代
	    for (int i = 16; i >= 1; i--) {
	        L[i - 1] = xor(R[i], this.f(L[i], K[i - 1]));
	        R[i - 1] = L[i];
	        R[i] = xor(L[i - 1], this.f(R[i - 1], K[i - 1]));
	    }
	    //以L0为左半部分，R0为右半部分合并
	    for (int i = 0; i < 32; i++) {
	        plaintext[i] = L[0][i];
	        plaintext[i + 32] = R[0][i];
	    }
	    //逆初始置换IP^-1
	    plaintext = this.rIP(plaintext);
	    for(int i=0;i<len;i++) {
	      plaintext_1[i]=plaintext[i];
	    }
	    return plaintext_1;
	}
	/**
	 * 两数组异或
	 * @param a
	 * @param b
	 * @return
	 */
	static byte[] xor(byte[] a, byte[] b) {
	    byte[] c = new byte[a.length];
	    for (int i = 0; i < a.length; i++) 
	        c[i] = (byte) (a[i] ^ b[i]);
	    return c;
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
	
	
    public static String toBinary(String str){
        //把字符串转成字符数组
    	 int len=str.length();//明文长度
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            //toBinaryString(int i)返回变量的二进制表示的字符串
            //toHexString(int i) 八进制
            //toOctalString(int i) 十六进制
        	String a=Integer.toBinaryString(strChar[i]);
        	while(a.length()<8) {
        		a="0"+a;
        	}
            result +=a;
        }
        return result;
    }
    
    public static String toString(String binary) {
	     char[] aplaintextArray = binary.toCharArray();
	        String aplaintextString = "";
	        for(int i=0;i<aplaintextArray.length;i++){
	            if(i%8==0 && i>0){
	            	aplaintextString +=" ";
	            }
	            aplaintextString += aplaintextArray[i];
	        }
       	    String[] tempStr= aplaintextString.split(" ");
               char[] tempChar=new char[tempStr.length];
               for(int i=0;i<tempStr.length;i++) {
                  tempChar[i]=BinstrToChar(tempStr[i]);
               }
               return String.valueOf(tempChar);
       }


        //将二进制字符串转换成int数组
        public static int[] BinstrToIntArray(String binStr) {       
            char[] temp=binStr.toCharArray();
            int[] result=new int[temp.length];   
            for(int i=0;i<temp.length;i++) {
                result[i]=temp[i]-48;
            }
            return result;
        }


        //将二进制转换成字符
        public static char BinstrToChar(String binStr){
            int[] temp=BinstrToIntArray(binStr);
            int sum=0;
            for(int i=0; i<temp.length;i++){
                sum +=temp[temp.length-1-i]<<i;
            }   
            return (char)sum;
       }
	
	public byte[] Encrypt_1(byte[] plaintext, byte[] sKey) {//明文长度小于64
		int len=plaintext.length;
		byte ciphertext_1[]= new byte[64];//密文
		byte plaintext_1[]=new byte[64];
		for(int n=0;n<len;n++){
			plaintext_1[n]=plaintext[n];}
		for(int j=len;j<64;j++){
			plaintext_1[j]=0;}
		ciphertext_1=this.encrypt(plaintext_1, sKey);
		return ciphertext_1;
	}
	public byte[] Decrypt_1(byte[] ciphertext, byte[] sKey,int len) {//明文长度小于64
		byte Aplaintext_1[]=this.decrypt(ciphertext, sKey,len);
		byte Aplaintext[]=new byte[len];
		for(int i=0;i<len;i++)
		{
			Aplaintext[i]=Aplaintext_1[i];
		}
		return Aplaintext;
	}
	
	public byte[] Encrypt_2(byte[] plaintext, byte[] sKey) {//明文长度是64的倍数
		int len=plaintext.length;
		byte[] ciphertext_2 = new byte[len];//密文长度是64的倍数
		for(int j=0;j<(len/64);j++) {
			byte plaintext_1[]=new byte[64];
			for(int n=j*64;n<(j+1)*64;n++) {
				plaintext_1[n%64]=plaintext[n];
			}
			byte[] ciphertext_4 = new byte[64];
			ciphertext_4=this.encrypt(plaintext_1, sKey);
			for(int n=j*64;n<(j+1)*64;n++) {
				ciphertext_2[n]=ciphertext_4[n%64];
			}	
		}
		return ciphertext_2;
	}
	
	public byte[] Decrypt_2(byte[] ciphertext, byte[] sKey,int len) {//明文长度是64的倍数
		byte[] Aplaintext = new byte[len];//解密出的明文长度是64的倍数
		for(int j=0;j<(len/64);j++) {
			byte ciphertext_1[]=new byte[64];
			for(int n=j*64;n<(j+1)*64;n++) {
				ciphertext_1[n%64]=ciphertext[n];
			}
			byte Aplaintext_2[]=this.decrypt(ciphertext_1, sKey,64);
			for(int n=j*64;n<(j+1)*64;n++) {
				Aplaintext[n]=Aplaintext_2[n%64];
			}	
		}
		return Aplaintext;
	}
	
    public byte[] Encrypt_3(byte[] plaintext, byte[] sKey) { //明文长度大于64，且不是64的倍数
    	int len=plaintext.length;
		int a=(len/64)*64;//整数部分
		int b=len-a;//余数部分
		int c=64*(len/64+1);//密文长度
		byte[] plaintext_1=new byte[a];//第一段明文
		byte[] plaintext_2=new byte[b];//第二段明文
		byte[] ciphertext_3 = new byte[c];//密文长度大于64，且不是64的倍数
		
		for(int j=0;j<a;j++) {
			plaintext_1[j]=plaintext[j];}//第一段明文
		byte[] ciphertext_1=Encrypt_2(plaintext_1,sKey);//加密前64整单位的明文
		for(int j=a;j<len;j++) {
			plaintext_2[j%64]=plaintext[j];}//第二段明文
		byte[] ciphertext_2=Encrypt_1(plaintext_2,sKey);//加密最后的不满64位明文
		for(int j=0;j<a;j++) {
			ciphertext_3[j]=ciphertext_1[j];}//加入第一段明文
	    for(int j=a;j<(64*(len/64+1));j++) {
	    	ciphertext_3[j]=ciphertext_2[j%64];}//加入第二段明文
		return ciphertext_3;
	}
    
	public byte[] Decrypt_3(byte[] ciphertext, byte[] sKey,int len) {//明文长度大于64，且不是64的倍数
		//len代表明文长度
		int a=(len/64)*64;//整数部分
		int b=len-a;//余数部分
		int c=64*(len/64+1);//密文长度
		byte[] ciphertext_1 = new byte[a];//密文整数部分
		byte[] ciphertext_2 = new byte[64];//密文最后部分
		byte[] Aplaintext_3 = new byte[len];//解密后的明文
		for(int j=0;j<a;j++) {
			ciphertext_1[j]=ciphertext[j];}//密文整数部分
		byte[] Aplaintext_1=Decrypt_2(ciphertext_1,sKey,a);//解密前64整单位的明文
		for(int j=a;j<c;j++) {
			ciphertext_2[j%64]=ciphertext[j];}
		byte[] Aplaintext_2=Decrypt_1(ciphertext_2,sKey,b);//解密最后的不满64位明文
		for(int j=0;j<a;j++) {
			Aplaintext_3[j]=Aplaintext_1[j];}
	    for(int j=a;j<len;j++) {
	    	Aplaintext_3[j]=Aplaintext_2[j%64];}
		return Aplaintext_3;
	}
    
	public static void main(String[] args) {
		DESUtil des=new DESUtil();
		String Input="hello world";
		System.out.println("输入字符串Input:"+Input);
		// 转换为二进制字符串
		String binaryStr = des.toBinary(Input);
		System.out.println(binaryStr);
		int Len=binaryStr.length();
		byte plaintext[]=new byte[Len];
		for(int i=0;i<Len;i++) {
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
		System.out.printf("加密前密钥是：");
		Print(sKey);
		int len=plaintext.length;
		byte Aplaintext[];
		byte ciphertext[];
		if(len<64)
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
	    System.out.printf("加密后密文是：");
	    Print(ciphertext);
	    System.out.printf("解密后密文是：");
	    Print(Aplaintext);	    
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
	 	System.out.printf("解密后的明文是："+newStr);
	 	
		}

		
	

}
