//References : http://www.usna.edu/Users/math/wdj/_files/documents/book/node48.html

package Server;

import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;
import java.util.*;
import java.io.*;


public class server extends Thread {
	private ServerSocket serverSocket;
	Random sc = new SecureRandom();
	public static BigInteger p;
	private static BigInteger a;
	public static BigInteger b;
	public static BigInteger k;
	public static BigInteger g;
	public static BigInteger c1;
	public static BigInteger c2;

	public server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000000);
	}

	public void run() {

		while (true) {
			try {
				System.out.println("Waiting for client on port "
						+ serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
						+ server.getRemoteSocketAddress());

				DataInputStream in = new DataInputStream(
						server.getInputStream());
				DataOutputStream out = new DataOutputStream(
						server.getOutputStream());
				init();
				
				String b_send=b.toString();
				String k_send=k.toString();
				String g_send=g.toString();
				String p_send=p.toString();
				out.writeUTF(b_send);
				out.writeUTF(k_send);
				out.writeUTF(g_send);
				out.writeUTF(p_send);
				String c1_recv = in.readUTF();
				String c2_recv = in.readUTF();
				c1 = new BigInteger(c1_recv);
				c2 = new BigInteger(c2_recv);
				System.out.println("c1 : " + c1);
				System.out.println("c2 : " + c2);
				
				BigInteger message = decrypt(c1,c2);
				System.out.println("The message is " + message);

			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

		}
	}
	public void init() {

		a = new BigInteger("876268429347923409");
		p = BigInteger.probablePrime(64, sc);
		g = new BigInteger("3");
		b = g.modPow(a, p);
		k = new BigInteger(p.bitCount() - 1, sc);
		
	}


	public BigInteger decrypt(BigInteger c1, BigInteger c2) {
		//
		BigInteger temp = c1.modPow(a, p);
		temp = temp.modInverse(p);

		BigInteger recover = temp.multiply(c2);
		recover = recover.mod(p);

		return recover;

	}
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		try {
			Thread t = new server(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}