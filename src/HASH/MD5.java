package HASH;

public class MD5 {
	//�洢С��
    long []groups = null;
    //�洢���
    String resultMessage="";
	
    //�ĸ��Ĵ����ĳ�ʼ����IV,����С�˴洢
    static final long A=0x67452301L;
    static final long B=0xefcdab89L;
    static final long C=0x98badcfeL;
    static final long D=0x10325476L;
    
    //java��֧���޷��ŵĻ�������(unsigned),����ѡ��long��������
    private long [] result={A,B,C,D};
    
    static final long T[][] = {
    		{0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee,
    		0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
    		0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be,
    		0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821},
    		
    		{0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa,
    		0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
    		0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed,
    		0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a},
    		
    		{0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c,
    		0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
    		0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05,
    		0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665},
    		
    		{0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
    		0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
    		0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1,
    		0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391}};
    //��ʾX[k]�еĵ�kȡֵ���������ʹ����Ϣ�����е���
    static final int k[][] = {
    		{0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15},
    		{1, 6,11, 0, 5,10,15, 4, 9,14, 3, 8,13, 2, 7,12},
    		{5, 8,11,14, 1, 4, 7,10,13, 0, 3, 6, 9,12,15, 2},
    		{0, 7,14, 5,12, 3,10, 1, 8,15, 6,13, 4,11, 2, 9}};
    
    //���ε����в��õ���ѭ����λ��sֵ
    static final int S[][] = {
    		{7,12,17,22},
    		{5,9,14,20},
    		{4,11,16,23},
    		{6,10,15,21}};
    
    //4��ѭ����ʹ�õ����ɺ���(�ֺ���)g
    private static long g(int i, long b, long c, long d) {
    	switch (i) {
		case 0:
			return (b & c) | ((~b) & d);
		case 1:
			return (b & d) | (c & (~d));
		case 2:
			return b ^ c ^ d;
		case 3:
			return c ^ (b | (~d));
		default:
			return 0;
		}
    }
    //��ʼʹ��MD5����
    public String start(String message){
    	//ת��Ϊ�ֽ�����
        byte [] inputBytes=message.getBytes();
        //6A 61 6E 6b 69 6e 67
        //��ȡ�ֽ�����ĳ���
        int byteLen = inputBytes.length;
      //�õ�Kֵ����bit����λ��message���ȣ�
        long K = (long)(byteLen<<3);
        //����С��(512bit)(64byte)�ĸ���
        int groupCount = byteLen/64;
        
        //�ֿ�
        for(int i = 0;i < groupCount;i++){
        	//ÿ��ȡ512bit
            //����һ������
            H(divide(inputBytes, i*64));
        }
        
        //���
        int rest = byteLen % 64;
        //��������һ������
        byte [] paddingBytes=new byte[64];
        //ԭ����β������
        for(int i=0;i<rest;i++)
        	paddingBytes[i]=inputBytes[byteLen-rest+i];
        //��С��448bit������������100...0�����Kֵ�ĵ�64λ
        //��ʱֻ������һ������
        if(rest <= 56){
            //���100...0
            if(rest<56){
            	//���10000000
            	paddingBytes[rest]=(byte)(1<<7);
            	//���00000000
                for(int i=1;i<56-rest;i++)
                	paddingBytes[rest+i]=0;
            }
            //���Kֵ��64λ
            for(int i=0;i<8;i++){
            	paddingBytes[56+i]=(byte)(K&0xFFL);
                K=K>>8;
            }
            //�������
            H(divide(paddingBytes,0));
        //������448bit������������100...0�����Kֵ�ĵ�64λ
        //��ʱ��������������
        }else{
            //���10000000
            paddingBytes[rest]=(byte)(1<<7);
            //���00000000
            for(int i=rest+1;i<64;i++)
            	paddingBytes[i]=0;
            //�����һ��β������
            H(divide(paddingBytes,0));
            
            //���00000000
            for(int i=0;i<56;i++)
            	paddingBytes[i]=0;

            //����64λ
            for(int i=0;i<8;i++){
            	//����ܹؼ���ʹ��С�˷�ʽ����Byte�����ȴ洢len�ĵ�λ���ݣ�Ȼ������len
            	paddingBytes[56+i]=(byte)(K&0xFFL);
                K=K>>8;
            }
            //����ڶ���β������
            H(divide(paddingBytes,0));
        }
        //��Hashֵת����ʮ�����Ƶ��ַ���
        //С�˷�ʽ!
        for(int i=0;i<4;i++){
        	resultMessage += Long.toHexString(result[i] & 0xFF) +
            		Long.toHexString((result[i] & 0xFF00) >> 8) +
            		Long.toHexString((result[i] & 0xFF0000) >> 16) +
            		Long.toHexString((result[i] & 0xFF000000) >> 24);
  
        }
        return resultMessage;
    }
    
    //��inputBytes��index��ʼȡ512λ����Ϊ�µ�512bit�ķ���
    private static long[] divide(byte[] inputBytes,int start){
    	//�洢һ��������,����512bit,������ÿ����32bit������4�ֽڣ�Ϊ����������λ��Ӱ�죬����ʹ��long
        long [] group=new long[16];
        for(int i=0;i<16;i++){
        	//ÿ��32bit��4���ֽ�ƴ�Ӷ���
        	//С�˵Ĵ�byte���鵽bit�ָ�����
            group[i]=byte2unsign(inputBytes[4*i+start])|
                (byte2unsign(inputBytes[4*i+1+start]))<<8|
                (byte2unsign(inputBytes[4*i+2+start]))<<16|
                (byte2unsign(inputBytes[4*i+3+start]))<<24;
        }
        return group;
    }
    
    //��ʵbyte�൱��һ���ֽڵ��з������������ﲻ��Ҫ����λ�����԰ѷ���λȥ��
    public static long byte2unsign(byte b){
        return b < 0 ? b & 0x7F + 128 : b;
     }
    
    // groups[] ��ÿһ������512λ��64�ֽڣ�
    // MD5ѹ������
    private void H(long[] groups) {
    	//���������Ĵ���������
        long a = result[0], b = result[1], c = result[2], d = result[3];
        //����ѭ��
        for(int n = 0; n < 4; n++) {
        	//16�ֵ���
        	for(int i = 0; i < 16; i++) {
            	result[0] += (g(n, result[1], result[2], result[3])&0xFFFFFFFFL) + groups[k[n][i]] + T[n][i];
                result[0] = result[1] + ((result[0]&0xFFFFFFFFL)<< S[n][i % 4] | ((result[0]&0xFFFFFFFFL) >>> (32 - S[n][i % 4])));
                //ѭ���ֻ�
                long temp = result[3];
                result[3] = result[2];
                result[2] = result[1];
                result[1] = result[0];
                result[0] = temp;
            }
        }
        //����֮ǰ����Ľ��
        result[0] += a;
        result[1] += b;
        result[2] += c;
        result[3] += d;
        //��ֹ���
        for(int n = 0; n < 4 ; n++) {
        	result[n] &=0xFFFFFFFFL;
        }
    }
    
    public static void main(String []args){
        MD5 md=new MD5();
        String message = "helloMD5";
        System.out.println("MD5-Algorithm:\n\nOrigin Message: " + message);
        System.out.println("Result Message: " + md.start(message));
        System.out.println("Result Message(UpperCase): " + md.resultMessage.toUpperCase());
        //F0F99260B5A02508C71F6D81C15E9A44
        //3ED9E5F6855DBCDBCD95AC6C4FE0C0A5
    }
}
