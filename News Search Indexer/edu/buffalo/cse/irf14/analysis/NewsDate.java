package edu.buffalo.cse.irf14.analysis;

public class NewsDate implements Analyzer{
	
TokenStream ts = null;
	
	Token tok = null;
	
	Boolean tokenFiltering = false;
	
	TokenFilterFactory factory = TokenFilterFactory.getInstance();
	TokenFilter filter = null;
	
	Analyzer an = null;

	public NewsDate(TokenFilter filter) {
		super();
		this.filter = filter;
	}

	public NewsDate() {
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
	
	public Analyzer newsdateTokenFilter(TokenStream stream)
	{
		filter = factory.getFilterByType(TokenFilterType.DATE, stream);
		
		an = new NewsDate(filter);
		
		return an;
	}
}
