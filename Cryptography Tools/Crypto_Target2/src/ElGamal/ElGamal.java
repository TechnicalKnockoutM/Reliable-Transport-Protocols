package ElGamal;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class ElGamal {
	Random sc = new SecureRandom();
	public static BigInteger p;
	public static BigInteger a;
	public static BigInteger b;
	public static BigInteger k;
	public static BigInteger g;
	public static BigInteger c1;
	public static BigInteger c2;

	public ElGamal() {

	}

	public void init() {

		a = new BigInteger("876268429347923409");
		p = BigInteger.probablePrime(64, sc);
		g = new BigInteger("3");
		b = g.modPow(a, p);
		k = new BigInteger(p.bitCount() - 1, sc);
		// System.out.println("secretKey = " + a);
		// System.out.println("p = " + p);
		// System.out.println("g = " + g);
		// System.out.println("b = " + b);
		// System.out.println("k = " + k);
		//
	}

	public void encrypt(String s) {

		BigInteger m = new BigInteger(s);
		c1 = g.modPow(k, p);
		c2 = b.modPow(k, p);
		c2 = c2.multiply(m);
		c2 = c2.mod(p);
		// System.out.println("The corresponding cipher texts are c1 = " + c1
		// + " c2 = " + c2);

	}

	public BigInteger decrypt(BigInteger c1, BigInteger c2, BigInteger a,
			BigInteger p) {
		//
		BigInteger temp = c1.modPow(a, p);
		temp = temp.modInverse(p);

		// Print this out.
		// System.out.println("Here is c1^ -a = " + temp);

		// Now, just multiply this by the second ciphertext
		BigInteger recover = temp.multiply(c2);
		recover = recover.mod(p);

		// And this will give us our original message back!
		// System.out.println("The original message = " + recover);
		return recover;

	}
}
