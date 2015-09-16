package Client;

import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class client {
	public static BigInteger c1;
	public static BigInteger c2;

	public static void main(String[] args) {
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);

		Scanner inp = new Scanner(System.in);
		System.out.println("Enter Message : ");
		String message = inp.nextLine();
		try {
			System.out.println("Connecting to " + serverName + " on port "
					+ port);
			Socket client = new Socket(serverName, port);
			System.out.println("Just connected to "
					+ client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			DataInputStream in = new DataInputStream(client.getInputStream());

			String b_recv = in.readUTF();
			String k_recv = in.readUTF();
			String g_recv = in.readUTF();
			String p_recv = in.readUTF();

			BigInteger b = new BigInteger(b_recv);
			BigInteger k = new BigInteger(k_recv);
			BigInteger g = new BigInteger(g_recv);
			BigInteger p = new BigInteger(p_recv);

			System.out.println("b :  " + b);
			System.out.println("k :  " + k);
			System.out.println("g :  " + g);
			System.out.println("p :  " + p);

			encrypt(message, b, k, g, p);
			BigInteger b1 = c1;
			String c1 = b1.toString();
			BigInteger b2 = c2;
			String c2 = b2.toString();
			out.writeUTF(c1);
			out.writeUTF(c2);
			client.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public static void encrypt(String s, BigInteger b, BigInteger k,
			BigInteger g, BigInteger p) {

		BigInteger m = new BigInteger(s);
		c1 = g.modPow(k, p);
		c2 = b.modPow(k, p);
		c2 = c2.multiply(m);
		c2 = c2.mod(p);
		// System.out.println("The corresponding cipher texts are c1 = " + c1
		// + " c2 = " + c2);

	}

}
