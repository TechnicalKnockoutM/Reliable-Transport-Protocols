package Client;


import java.net.*;
import java.util.Scanner;
import java.io.*;

public class client
{
   public static void main(String [] args)
   {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
//      Console console = System.console();
//      //read user name, using java.util.Formatter syntax :
//      String username = console.readLine("User Name? ");
//
//      //read the password, without echoing the output 
//      char[] password = console.readPassword("Password? ");

      Scanner inp = new Scanner(System.in);
      System.out.println("Enter Username : ");
      String username = inp.nextLine();
      System.out.println("Enter Password : ");
      String password = inp.nextLine();
     
      try
      {
         System.out.println("Connecting to " + serverName
                             + " on port " + port);
         Socket client = new Socket(serverName, port);
         System.out.println("Just connected to "
                      + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out =
                       new DataOutputStream(outToServer);

//         out.writeUTF("Hello from "
//                      + client.getLocalSocketAddress());
//         
        
         
         InputStream inFromServer = client.getInputStream();
         DataInputStream in =
                        new DataInputStream(inFromServer);
         String encrypted_username;
     	String encrypted_password;
 // Target 1
         try {
			encrypted_username = AES.encrypt(username);
			encrypted_password = AES.encrypt(password);
			out.writeUTF(encrypted_username);
	         out.writeUTF(encrypted_password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         while(in!=null){
       	 String read_string = in.readUTF();
       	 try {
			read_string = AES.decrypt(read_string);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if(read_string.equals("Wrong password entered.Please try again!!"))
        {
//       	 System.out.println("Server says " + in.readUTF());
       	 System.out.println("Wrong password entered.Please try again!!");
       	 
       	try {
       		System.out.println("Enter username : ");
          	 username = inp.nextLine();
       		encrypted_username = AES.encrypt(username);
			 System.out.println("Enter Password : ");
	            password = inp.nextLine();
	            encrypted_password = AES.encrypt(password);
	            out.writeUTF(encrypted_username);
	            out.writeUTF(encrypted_password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           

        }
        else if(read_string.equals("Username does not exists"))
        {
       	 System.out.println("Username does not exists");
       	try {
       		System.out.println("Enter username : ");
          	 username = inp.nextLine();
       		encrypted_username = AES.encrypt(username);
			 System.out.println("Enter Password : ");
	            password = inp.nextLine();
	            encrypted_password = AES.encrypt(password);
	            out.writeUTF(encrypted_username);
	            out.writeUTF(encrypted_password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        else if(read_string.equals("Exceeded number of attempts"))
       		 {
       	 System.out.println("Exceeded number of attempts");
       	 try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
       	 
       	try {
       		System.out.println("Enter username : ");
          	 username = inp.nextLine();
       		encrypted_username = AES.encrypt(username);
			 System.out.println("Enter Password : ");
	            password = inp.nextLine();
	            encrypted_password = AES.encrypt(password);
	            out.writeUTF(encrypted_username);
	            out.writeUTF(encrypted_password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       		 }
        else if(read_string.equals("Welcome"))
        {
       	 System.out.println("Welcome " + username);
       	 outToServer.flush();
       	 
       	 client.close();
       	 break;
//       	 System.exit(0);
        }
//        System.out.println("Server says " + in.readUTF());
 	}        
         
/*  Target 0
 
//         out.writeUTF(username);
//         out.writeUTF(password);
          while(in!=null){
        	 String read_string = in.readUTF();
         if(read_string.equals("Wrong password entered.Please try again!!"))
         {
//        	 System.out.println("Server says " + in.readUTF());
        	 System.out.println("Wrong password entered.Please try again!!");
        	 System.out.println("Enter username : ");
        	 username = inp.nextLine();
             System.out.println("Enter Password : ");
             password = inp.nextLine();
             out.writeUTF(username);
             out.writeUTF(password);

         }
         else if(read_string.equals("Username does not exists"))
         {
        	 System.out.println("Username does not exists");
        	 System.out.println("Enter username : ");
        	 username = inp.nextLine();
             System.out.println("Enter Password : ");
             password = inp.nextLine();
             out.writeUTF(username);
             out.writeUTF(password);

         }
         else if(read_string.equals("Exceeded number of attempts"))
        		 {
        	 System.out.println("Exceeded number of attempts");
        	 try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	 
        	 System.out.println("Enter username : ");
        	 username = inp.nextLine();
             System.out.println("Enter Password : ");
             password = inp.nextLine();
             out.writeUTF(username);
             out.writeUTF(password);

        		 }
         else if(read_string.equals("Welcome"))
         {
        	 System.out.println("Welcome " + username);
        	 outToServer.flush();
        	 
        	 client.close();
        	 break;
//        	 System.exit(0);
         }
//         System.out.println("Server says " + in.readUTF());
  	}
  */       
      
      } 
      catch(IOException e)
      {
         e.printStackTrace();
      }
      }
   }
   
