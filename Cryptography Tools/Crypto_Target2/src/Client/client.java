package Client;

import ElGamal.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class client {
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

			ElGamal eg = new ElGamal();
			eg.init();
			eg.encrypt(message);
			BigInteger b1 = ElGamal.c1;
			String c1 = b1.toString();
			BigInteger b2 = ElGamal.c2;
			String c2 = b2.toString();
			BigInteger a1 = ElGamal.a;
			String a = a1.toString();
			BigInteger p1 = ElGamal.p;
			String p = p1.toString();
			out.writeUTF(c1);
			out.writeUTF(c2);
			out.writeUTF(a);
			out.writeUTF(p);
			client.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}
}
