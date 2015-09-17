package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;

public class Numbers extends TokenFilter {

	TokenStream ts = null;
	Boolean tokenFiltering = false;
	public Numbers(TokenStream stream) {
		super(stream);
		ts = stream;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		return tokenFiltering;
		// TODO Auto-generated method stub
		
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return ts;
	}
	public TokenFilter NumbersFilter(TokenStream stream)
	{
		ArrayList<String> filteredTokenString = new ArrayList<String>();
		//TokenStream filteredStream = null;

		TokenFilter tf = null;
		
		while(stream.hasNext())
		{
			Token tok = stream.next();
			String tokenString = tok.toString();
//			if(tokenString.contains("[0-9]"))
//			{
//				tokenString=tokenString.replaceAll("[0-9]", "");	
//			}
			
			tokenString=tokenString.replaceAll("\\d+[.,]?\\d+","");
						
			if (!tokenString.equals(null) && !(tokenString.equals("")))
			filteredTokenString.add(tokenString);
		}	
		
		
		ts = new TokenStream(filteredTokenString);
		
		tf = new Accent(ts);
		
		tokenFiltering = false;
		
		return tf;

	}
	


}
