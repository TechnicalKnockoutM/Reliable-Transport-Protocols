package Server;

import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
//				Put value for Target 0 method here
				credentials.put("Avinash", "avi");
				credentials.put("Nikhil", "nik");
				credentials.put("Sharath", "sha");
				credentials.put("Susana", "sus");
		
				
//				SHA-1
//			String val = sha1("Avinash");
//				credentials_target1.put("Avinash", val);
//				String val1 = sha1("Nikhil");
//				credentials_target1.put("Nikhil", val1);
//				String val2 = sha1("Sharath");
//				credentials_target1.put("Sharath", val2);
//				String val3 = sha1("Susana");
//				credentials_target1.put("Susana", val3);
//				
				DataInputStream in = new DataInputStream(
						server.getInputStream());
				DataOutputStream out = new DataOutputStream(
						server.getOutputStream());
				while(in!=null){
					if(server.isClosed())
					{
						System.out.println("Socket Closed");
						break;
					}
					else{
				String username = in.readUTF();
				String password = in.readUTF();
				target0(username,password,in,out,server);
					}
//				target1(username,password,in,out,server);
//				System.out.println(username);
//				System.out.println(password);
				}
				// out.writeUTF("Thank you for connecting to "
				// + server.getLocalSocketAddress() + "\nGoodbye!");
				
//				server.close();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
//				catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}

		
		static String sha1(String input) throws NoSuchAlgorithmException {
	        MessageDigest md = MessageDigest.getInstance("SHA-1");
	        byte[] result = md.digest(input.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < result.length; i++) {
	            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }
	         
	        return sb.toString();
	    }
	

	

	private void target0(String username, String password, DataInputStream in,
			DataOutputStream out, Socket server) {
		// TODO Auto-generated method stub
		
		
		if (credentials.containsKey(username)) {
			String pwd = credentials.get(username);
			if (pwd.equals(password)) {
				System.out.println("Welcome " + username);
				
				try {
					out.writeUTF("Welcome");
					server.close();
					
//					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} else {
				try {
					attempts++;
					System.out.println("Attempts " + attempts);
					if(attempts==3)
					{
						//Lock out
						
						try {
							System.out.println("Exceeded number of attempts");
							out.writeUTF("Exceeded number of attempts");
//							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					else
					{
					System.out.println("Wrong password entered.Please try again!!");
					out.writeUTF("Wrong password entered.Please try again!!");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		} else {
			System.out.println("Username does not exists");
			try {
				out.writeUTF("Username does not exists");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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