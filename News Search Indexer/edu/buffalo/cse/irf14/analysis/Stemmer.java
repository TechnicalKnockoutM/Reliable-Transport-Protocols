package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;

public class Stemmer  extends TokenFilter {

	TokenStream ts = null;
	Boolean tokenFiltering = false;
	Stemmer1 s= new Stemmer1();
	public Stemmer(TokenStream stream) {
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
	
	public TokenFilter StemmerFilter(TokenStream stream)
	{
		ArrayList<String> filteredTokenString = new ArrayList<String>();
//		TokenStream filteredStream = null;

		TokenFilter tf = null;
		
		while(stream.hasNext())
		{
			Token tok = stream.next();
			String tokenString = tok.toString();
			String newtokenstring;
			char[] charArray = tokenString.toCharArray();
			for (int c = 0; c < charArray.length; c++) s.add(charArray[c]);
			s.stem();
			newtokenstring=s.toString();
			filteredTokenString.add(newtokenstring);
			
		}
		
		ts = new TokenStream(filteredTokenString);
		
		tf = new Accent(ts);
		
		tokenFiltering = false;
		
		return tf;

		
		
	}
	

}
