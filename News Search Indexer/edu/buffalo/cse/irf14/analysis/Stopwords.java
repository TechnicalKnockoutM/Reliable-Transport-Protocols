package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;

public class Stopwords extends TokenFilter {

	TokenStream ts = null;
	//Reference start UB Learns Course Blog stopwords list provided by Nikhil Londhe
	String[] StopwordsString = { "a", "able", "about", "across", "after",
			"all", "almost", "also", "am", "among", "an", "and", "any", "are",
			"as", "at", "be", "because", "been", "but", "by", "can", "cannot",
			"could", "dear", "did", "do", "does", "either", "else", "ever",
			"every", "for", "from", "get", "got", "had", "has", "have", "he",
			"her", "hers", "him", "his", "how", "however", "i", "if", "in",
			"into", "is", "it", "its", "just", "least", "let", "like",
			"likely", "may", "me", "might", "most", "must", "my", "neither",
			"no", "nor", "not", "of", "off", "often", "on", "only", "or",
			"other", "our", "own", "rather", "said", "say", "says", "she",
			"should", "since", "so", "some", "than", "that", "the", "their",
			"them", "then", "there", "these", "they", "this", "tis", "to",
			"too", "twas", "us", "wants", "was", "we", "were", "what", "when",
			"where", "which", "while", "who", "whom", "why", "will", "with",
			"would", "yet", "you", "your" };
	//Reference end

	Boolean tokenFiltering = false;

	public Stopwords(TokenStream stream) {
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

	public TokenFilter StopwordFilter(TokenStream stream) {
		ArrayList<String> filteredTokenString = new ArrayList<String>();
		// TokenStream filteredStream = null;

		TokenFilter tf = null;

		while (stream.hasNext()) {
			Token tok = stream.next();
			String tokenString = tok.toString();
			int indexForReplacement = 0;

			for (String findString : StopwordsString) {
				if (indexForReplacement == StopwordsString.length)
					indexForReplacement = 0;

				if (tokenString.equals(findString)) {
					tokenString = tokenString.replaceAll(findString, "");
				}

				indexForReplacement++;
			}

			if (!tokenString.equals(null) && !(tokenString.equals("")))
				filteredTokenString.add(tokenString);
		}

		ts = new TokenStream(filteredTokenString);

		tf = new Accent(ts);

		tokenFiltering = false;

		return tf;

	}
}
