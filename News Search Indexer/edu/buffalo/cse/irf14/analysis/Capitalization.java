package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;

public class Capitalization extends TokenFilter {
	
	TokenStream ts = null;
	
	Boolean tokenFiltering = false;

	public Capitalization(TokenStream stream) {
		// TODO Auto-generated constructor stub
		super(stream);
		
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

	public TokenFilter capitalFilter(TokenStream stream) {
		// TODO Auto-generated method stub
		
		tokenFiltering = true;
		
		ArrayList<String> filteredTokenString = new ArrayList<String>();
//		TokenStream filteredStream = null;
//		Capitalization capital = null;
		TokenFilter tf = null;
		
		Boolean adjacentStringFlag = false;
		String adjacentString = "";
		
		Boolean adjacentAllCapsStringFlag = false;
		String adjacentAllCapsString = "";
		
		String tempTokenString = "";
		String tempTokenCapsString ="";
		
		int firstWord = 0;
		
		while(stream.hasNext())
		{
			Token tok = stream.next();
			String tokenString = tok.toString();
			
			firstWord++;
			if(tokenString.equalsIgnoreCase("I"))
			{
				tokenString.toUpperCase();
			}
			else
			{
				if(tokenString.equals(tokenString.toLowerCase()))
				{
					tempTokenString = adjacentString;
					tempTokenCapsString = adjacentAllCapsString;
					adjacentString = "";
					adjacentAllCapsString = "";
					adjacentStringFlag = false;
					adjacentAllCapsStringFlag = false;
				}
				else
				{
					if(tokenString.equals(tokenString.toUpperCase()))
					{
						if(adjacentAllCapsStringFlag)
						{
							tempTokenString = adjacentString;
							adjacentString = "";
							
							adjacentStringFlag = false;
							adjacentAllCapsString = adjacentAllCapsString + " " + tokenString;
						}
						
						else
						{
							tempTokenString = adjacentString;
							
							adjacentString = "";
							
							adjacentStringFlag = false;
							adjacentAllCapsStringFlag = true;
							adjacentAllCapsString = tokenString;
						}
					}
					else
					{
						if(Character.isUpperCase(tokenString.charAt(0)))
						{
							if(tokenString.substring(1).equals(tokenString.substring(1).toLowerCase()))
							{
								if(firstWord != 1)
								{
									if(adjacentStringFlag)
									{
										
										tempTokenCapsString = adjacentAllCapsString;
										adjacentAllCapsString = "";
										adjacentAllCapsStringFlag = false;
										adjacentString = adjacentString + " " + tokenString;
									}
									
									else
									{
										
										tempTokenCapsString = adjacentAllCapsString;
										adjacentAllCapsString = "";
										adjacentStringFlag = true;
										adjacentAllCapsStringFlag = false;
										adjacentString = tokenString;
									}
								}
								else
								{
									tempTokenString = adjacentString;
									tempTokenCapsString = adjacentAllCapsString;
									adjacentString = "";
									adjacentAllCapsString = "";
									adjacentStringFlag = false;
									adjacentAllCapsStringFlag = false;
									tokenString = tokenString.toLowerCase();
								}
							}
							
							else
							{
								tempTokenString = adjacentString;
								tempTokenCapsString = adjacentAllCapsString;
								adjacentString = "";
								adjacentAllCapsString = "";
								adjacentStringFlag = false;
								adjacentAllCapsStringFlag = false;
	//							tokenString = tokenString.toLowerCase();//To confirm if this is camelcase.
							}
						}
						else
						{
							tempTokenString = adjacentString;
							tempTokenCapsString = adjacentAllCapsString;
							adjacentString = "";
							adjacentAllCapsString = "";
							adjacentStringFlag = false;
							adjacentAllCapsStringFlag = false;
						}
					}
				}
				
				if(tokenString.endsWith("."))
				{
					firstWord = 0;
				}
				
			}
			
//			tokenString = tempTokenString;
			
			if(!adjacentAllCapsStringFlag)
			{
				if(tempTokenCapsString.equals(""))
				{
					if(!adjacentStringFlag)
					{
						if(tempTokenString.equals(""))
						{
							filteredTokenString.add(tokenString);
						}
						
						else
						{
							filteredTokenString.add(tempTokenString);
							filteredTokenString.add(tokenString);
							tempTokenString = "";
						}
					}
				}
				
				else
				{
					filteredTokenString.add(tempTokenCapsString);
					filteredTokenString.add(tokenString);
					tempTokenCapsString = "";
				}
			}			
		}
		
		if(adjacentAllCapsStringFlag)
		{
			filteredTokenString.add(adjacentAllCapsString);
		}
		
		else if (adjacentStringFlag)
		{
			filteredTokenString.add(adjacentString);
		}
		
		ts = new TokenStream(filteredTokenString);
		tf = new Capitalization(ts);
		
		tokenFiltering = false;
		
		return tf;
	}

}
