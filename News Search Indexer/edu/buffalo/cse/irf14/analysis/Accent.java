package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.text.Normalizer;


public class Accent extends TokenFilter {
	
	
	//Reference start:http://stackoverflow.com/questions/15190656/easy-way-to-remove-utf-8-accents-from-a-string
	public static String stripAccents(String s) 
	{
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return s;
	    
	}
	//Reference end
	
	TokenStream ts = null;
	
	Boolean tokenFiltering = false;

	public Accent(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
		
		ts = stream;
	}

	@Override
	public boolean increment() throws TokenizerException {
		
		// TODO Auto-generated method stub	
		
		return tokenFiltering;
		
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub	
		
		return ts;
	}
	
	public TokenFilter accentFilter(TokenStream stream)
	{
		ArrayList<String> filteredTokenString = new ArrayList<String>();
//		TokenStream filteredStream = null;
//		Capitalization capital = null;
		TokenFilter tf = null;
		
		tokenFiltering = true;
		
		while(stream.hasNext())
		{
			Token tok = stream.next();
			String tokenString = tok.toString();
			
			
			
			String newtokenString=stripAccents(tokenString);
			
			if (!tokenString.equals(null) && !(tokenString.equals("")))
			filteredTokenString.add(newtokenString);
			
		}
		
		ts = new TokenStream(filteredTokenString);
		
		tf = new Accent(ts);
		
		tokenFiltering = false;
		
		return tf;
	}
	
	

}
