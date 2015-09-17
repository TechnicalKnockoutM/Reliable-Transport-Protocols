package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;

public class Symbol extends TokenFilter {
	
	TokenStream ts = null;
	
	Boolean tokenFiltering = false;
	
	public Symbol(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
		
		ts = stream;
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

	public TokenFilter symbolFilter(TokenStream stream) {
		// TODO Auto-generated method stub
		
		tokenFiltering = true;
		
		String endContractions[] = {"'ve", "n't", "'re" ,"'d", "'ll", "'em", "n't've", "'d've"};
		String contractionsReplacement[] = {" have"," not"," are", " would", " will", "them", " not have", " would have"};
		
		String words[] = {"won't", "shan't", "I'm", "can't", "ma'am", "o'clock","y'all"};
		String replacementWords[] ={"will not", "shall not", "I am", "cannot", "madam", "of the clock", "you all"};
		
		ArrayList<String> filteredTokenString = new ArrayList<String>();
		//TokenStream filteredStream = null;
		TokenFilter tf = null; 
		
		Boolean breakFlag = false;
		Boolean hyphenRemoveFlag = false;
		
		int count =0;
		
		while(stream.hasNext())
		{
			Token tok = stream.next();
			String tokenString = tok.toString();
			
			while(!breakFlag)
			{
				if(tokenString.endsWith("?"))
				{
					tokenString = tokenString.substring(0, tokenString.length()-1);
				}
				
				else if(tokenString.endsWith("!"))
				{
					tokenString = tokenString.substring(0, tokenString.length()-1);
				}
				
				else if (tokenString.endsWith("."))
				{
					tokenString = tokenString.substring(0, tokenString.length()-1);
				}
				
				else
					breakFlag = true;;
			}
					
			if(tokenString.endsWith("'s"))
			{
				tokenString = tokenString.substring(0, tokenString.length()-2);
			}
			
			for(String str : words)
			{
				if(tokenString.equalsIgnoreCase(str))
				{
					tokenString = replacementWords[count];
				}
				
				count++;
			}
			
			count = 0;
			
			for(String str : endContractions)
			{
				if(tokenString.endsWith(str))
				{
					tokenString = tokenString.substring(0, tokenString.indexOf(str)) + contractionsReplacement[count];
				}
				
				count++;
			}
			
			if(tokenString.contains("'"))
			{
				tokenString = tokenString.replaceAll("'", "");
			}
			
			while(!hyphenRemoveFlag)
			{
				if(tokenString.startsWith("-"))
				{
					tokenString = tokenString.substring(1, tokenString.length());
				}
				
				else if(tokenString.endsWith("-"))
				{
					tokenString = tokenString.substring(0, tokenString.length()-1);
				}
				
				else
					hyphenRemoveFlag = true;
			}
			
			if(!tokenString.matches(".*\\d+.*"))
			{
				if(tokenString.contains("-"))
				{
					tokenString = tokenString.replaceAll("-", " ");
				}
			}
			
			if (!tokenString.equals(null) && !(tokenString.equals("")))
			{
				filteredTokenString.add(tokenString);
			}
			
			breakFlag = false;
			hyphenRemoveFlag = false;
			
			}
		
		ts = new TokenStream(filteredTokenString);
		
		tf = new Accent(ts);
		
		tokenFiltering = false;
		
		return tf;
	}
}
