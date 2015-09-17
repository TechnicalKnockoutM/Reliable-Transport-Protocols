package edu.buffalo.cse.irf14.analysis;

import java.util.*;

public class SpecialChars extends TokenFilter {

	TokenStream ts = null;
	String[] SpecialcharString = { "@", "!", "#", "$", "%", "&", "*" };
	Boolean tokenFiltering = false;

	public SpecialChars(TokenStream stream) {
		super(stream);
		ts = stream;
		// TODO Auto-generated constructor stub
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

	public TokenFilter SpecialcharstringFilter(TokenStream stream) {
		ArrayList<String> filteredTokenString = new ArrayList<String>();

		TokenFilter tf = null;

		while (stream.hasNext()) {
			Token tok = stream.next();
			String tokenString = tok.toString();
			if (tokenString.matches(".*\\d+[-]\\d+")) {
				tokenString = tokenString.replaceAll("#", "");
			}
			// if(tokenString.matches(".*^a-z+\\d+"))
			// {
			// tokenString=tokenString.replaceAll("#","");
			// }
			if (!tokenString.matches(".*\\d+[-]\\d+")
					&& !tokenString.matches("[-].*\\d")) {
				tokenString = tokenString.replaceAll(
						"[-\\\\@[$&+,:;=?#|'<>^*~_/()%!]+]", "");
			}

			if (!tokenString.equals(null) && !(tokenString.equals("")))
				filteredTokenString.add(tokenString);
		}

		ts = new TokenStream(filteredTokenString);

		tf = new SpecialChars(ts);

		tokenFiltering = false;

		return tf;

	}

}
