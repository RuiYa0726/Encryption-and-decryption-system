package RSA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RSA {
	/**
	 * �����ĸ����Լ����㷨
	 * @param k
	 * @param n
	 * @return
	 */
	static boolean isPrime(int k, long n) {
	    List<Long> a = new ArrayList<Long>();
	    int t = n - 2 > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) (n - 2);
	    do {
	        long l = (long) (new Random().nextInt(t - 2) + 2);
	        if (-1 == a.indexOf(l)) 
	            a.add(l);
	    } while (a.size() < k);
	    for (int i = 0; i < k; i++)  
	        if (! Miller(n, a.get(i))) 
	            return false;
	    return true;
	}
	static boolean Miller(long n, long a) {
	    long m = n - 1;
	    int t = 0;
	    while (m % 2 == 0) {
	        m /= 2;
	        t++;
	    }
	    long b = modExp(a, m, n);
	    if (b == 1 || b == n - 1) 
	        return true;
	    for (int j = 1; j < t; j++) {
	        b = b * b % n;
	        if (b == n - 1) 
	            return true;
	    }
	    return false;
	}
	
	/**
	 * ģ������
	 * @param b
	 * @param m
	 * @return  b^-1(mod m)
	 */
	static long modInv(long b, long m) {
	    if (b >= m) b %= m;
	    return exGcd(b, m)[1] < 0 ? exGcd(b, m)[1] + m : exGcd(b, m)[1];
	}
	/**
	 * ��չŷ������㷨
	 * <p>(a,b)=ax+by
	 * @param a
	 * @param b
	 * @return  ����һ��long����result��result[0]=x��result[1]=y��result[2]=(a,b)
	 */
	static long[] exGcd(long a, long b) {
	    if (a < b) {
	        long temp = a;
	        a = b;
	        b = temp;
	    }
	    long[] result = new long[3];
	    if (b == 0) {
	        result[0] = 1;
	        result[1] = 0;
	        result[2] = a;
	        return result;
	    }
	    long[] temp = exGcd(b, a % b);
	    result[0] = temp[1];
	    result[1] = temp[0] - a / b * temp[1];
	    result[2] = temp[2];
	    return result;
	}
	
	/**
	 * ģָ����
	 * @param b
	 * @param n
	 * @param m
	 * @return   b^n(mod m)
	 */
	static long modExp(long b, long n, long m) {
	    long result = 1;
	    b = b % m;
	    do {
	        if ((n & 1) == 1) 
	            result=result*b%m;
	        b = b * b % m;
	        n = n >> 1;
	    } while (n != 0);
	    return result;
	}
	
	/**
	 * ����
	 * <p> C=M^e(mod n)
	 * @param M
	 * @param n
	 * @param e
	 * @return
	 */
	public static BigInteger encrypt(BigInteger M, BigInteger n, BigInteger e) {
	    return M.modPow(e, n);
	}
	
	/**
	 * ����
	 * <p> M=C^d(mod n)
	 * @param C
	 * @param n
	 * @param d
	 * @return
	 */
	public static BigInteger decrypt(BigInteger C, BigInteger n, BigInteger d) {
	    return C.modPow(d, n);
	}
	
	public static void main(String[] args) {
	//p��q��Ϊ100-200λ������֮��	
	String input="b503be7137293906649e0ae436e29819ea2d06abf31e10091a7383349de84c5b";
	BigInteger m = new BigInteger(input,16);//���ַ���ת����10���ƴ�����
	BigInteger p = BigInteger.probablePrime(new Random().nextInt(100) + 200, new Random());//probablePrime(int a,random b)������������������a�����ȣ�b�����������Դ
	BigInteger q = BigInteger.probablePrime(new Random().nextInt(100) + 200, new Random());//new Random().nextInt(100)�������һ�����ֵΪ100�������֮���ټ���200�����������Ķ�����λ��λ��200-300֮��
	BigInteger n = p.multiply(q);
	BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));//ŷ��������BigInteger.ONE����������1
	BigInteger e;
	do {
	    e = new BigInteger(new Random().nextInt(phi_n.bitLength() - 1) + 1, new Random());
	} while (e.compareTo(phi_n) != -1 || e.gcd(phi_n).intValue() != 1);
	BigInteger d = e.modPow(new BigInteger("-1"), phi_n);//modPowģ������
	BigInteger c=encrypt(m,n,e);
	BigInteger outm=decrypt(c,n,d);
	System.out.print("����m��"+m.toString(16)+"\n");
	System.out.print("p:"+p.toString(16)+"\n");//BigInteger��16�������
	System.out.print("q:"+q.toString(16)+"\n");
	System.out.print("n:"+n.toString(16)+"\n");
	System.out.print("ŷ������(n):"+phi_n.toString(16)+"\n");
	System.out.print("e:"+e.toString(16)+"\n");
	System.out.print("d:"+d.toString(16)+"\n");
	System.out.print("���ܵ�����c:"+c.toString(16)+"\n");
	System.out.print("���ܵ�����outm:"+outm.toString(16)+"\n");
	}
}
