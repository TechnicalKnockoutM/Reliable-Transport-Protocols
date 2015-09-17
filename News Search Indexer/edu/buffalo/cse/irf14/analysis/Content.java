package edu.buffalo.cse.irf14.analysis;

public class Content implements Analyzer {

	TokenStream ts = null;

	Token tok = null;

	Boolean tokenFiltering = false;

	TokenFilterFactory factory = TokenFilterFactory.getInstance();
	TokenFilter filter = null;

	Analyzer an = null;

	public Content(TokenFilter filter) {
		super();
		this.filter = filter;
	}

	public Content() {
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

	public Analyzer contentTokenFilter(TokenStream stream) {

//		filter = factory.getFilterByType(TokenFilterType.DATE, stream);
//
//		if (filter.getStream().next().toString() == "YYYY MM DD HH mm ss") {
//			// Do nothing
//		}
//
//		else {
			
//			filter = factory.getFilterByType(TokenFilterType.STOPWORD, filter.getStream());
			filter = factory.getFilterByType(TokenFilterType.CAPITALIZATION1, stream);
			
			if (filter.getStream() != null) {
				
				filter = factory.getFilterByType(TokenFilterType.NUMERIC, filter.getStream());

				if (filter.getStream() != null) {
					
					filter = factory.getFilterByType(TokenFilterType.SPECIALCHARS, filter.getStream());

					if (filter.getStream() != null) {
						
						filter = factory.getFilterByType(TokenFilterType.SYMBOL, filter.getStream());

						if (filter.getStream() != null) {
							
							filter = factory.getFilterByType(TokenFilterType.ACCENT, filter.getStream());

							if (filter.getStream() != null) {
								
								filter = factory.getFilterByType(TokenFilterType.STOPWORD,filter.getStream());
								
								if (filter.getStream() != null) {

								filter = factory.getFilterByType(TokenFilterType.STEMMER, filter.getStream());
								}
							}
						}
					}
				}
			}
//		}

		an = new Content(filter);

		return an;
	}
}
