package edu.buffalo.cse.irf14.analysis;

public class Place implements Analyzer{
	
TokenStream ts = null;
	
	Token tok = null;
	
	Boolean tokenFiltering = false;
	
	TokenFilterFactory factory = TokenFilterFactory.getInstance();
	TokenFilter filter = null;
	
	Analyzer an = null;

	public Place(TokenFilter filter) {
		super();
		this.filter = filter;
	}

	public Place() {
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
		ts = filter.getStream();
		return ts;
	}
	
	public Analyzer placeTokenFilter(TokenStream stream)
	{
		
		filter = factory.getFilterByType(TokenFilterType.CAPITALIZATION, stream);
					
		filter = factory.getFilterByType(TokenFilterType.NUMERIC, filter.getStream());
				
		filter = factory.getFilterByType(TokenFilterType.SPECIALCHARS, filter.getStream());

		filter = factory.getFilterByType(TokenFilterType.SYMBOL, filter.getStream());

		filter = factory.getFilterByType(TokenFilterType.ACCENT, filter.getStream());
	
		filter = factory.getFilterByType(TokenFilterType.STOPWORD, filter.getStream());

		an = new Author(filter);
		
		return an;
	}
}
