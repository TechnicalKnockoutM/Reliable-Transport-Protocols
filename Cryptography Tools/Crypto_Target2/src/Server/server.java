package Server;

import ElGamal.*;

import java.math.BigInteger;
import java.net.*;
import java.util.*;
import java.io.*;

/*References
 * http://en.wikipedia.org/wiki/SHA-1
 */

public class server extends Thread {
	private ServerSocket serverSocket;
	HashMap<String, String> credentials = new HashMap<String, String>();
	HashMap<String, String> credentials_target1 = new HashMap<String, String>();
	HashMap<String, String> credentials_target2 = new HashMap<String, String>();
	int attempts = 0;

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
				ElGamal eg = new ElGamal();
				String c1 = in.readUTF();
				String c2 = in.readUTF();
				String a1 = in.readUTF();
				String p1 = in.readUTF();
				BigInteger b1 = new BigInteger(c1);
				BigInteger b2 = new BigInteger(c2);
				BigInteger a = new BigInteger(a1);
				BigInteger p = new BigInteger(p1);
				BigInteger message = eg.decrypt(b1, b2, a, p);
				System.out.println("The message is " + message);

			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

		}
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