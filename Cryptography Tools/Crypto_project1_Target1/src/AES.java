import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.*;
public class AES {

//	private static final byte[] keyValue = new byte[] { 'T', 'a', 'r', 'g', 'e', 't', '1' };
	private static final String ALGO = "AES";
    private static final byte[] keyValue = 
        new byte[] { 'A', 'p', 'p', 'C', 'r', 'y', 'p',
't', 'o', 'T', 'a','r', 'g', 'e', 't', '1' };

	@SuppressWarnings("restriction")
	public static String encrypt(String input) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(input.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

	  private static Key generateKey() throws Exception {
	        Key key = new SecretKeySpec(keyValue, "AES");
	        return key;
	}
	  
	  @SuppressWarnings("restriction")
	public static String decrypt(String encryptedData) throws Exception {
	        Key key = generateKey();
		    Cipher c = Cipher.getInstance("AES");
	        c.init(Cipher.DECRYPT_MODE, key);
	        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
	        byte[] decValue = c.doFinal(decordedValue);
	        String decryptedValue = new String(decValue);
	        return decryptedValue;
	    }
	  
	  public static String generateSalt() {
	        SecureRandom random = new SecureRandom();
	        byte bytes[] = new byte[20];
	        random.nextBytes(bytes);
	        String s = new String(bytes);
	        return s;
	    }

	  public static void main(String[] args) throws Exception {

	        String password = "mypassword";
	        String passwordEnc = AES.encrypt(password);
	        String passwordDec = AES.decrypt(passwordEnc);

	        System.out.println("Plain Text : " + password);
	        System.out.println("Encrypted Text : " + passwordEnc);
	        System.out.println("Decrypted Text : " + passwordDec);
	    }
	  
}

