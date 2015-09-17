package edu.buffalo.cse.irf14.analysis;

public class Dates extends TokenFilter{

	public Dates(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return null;
	}

	public TokenFilter dateFilter(TokenStream stream) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
