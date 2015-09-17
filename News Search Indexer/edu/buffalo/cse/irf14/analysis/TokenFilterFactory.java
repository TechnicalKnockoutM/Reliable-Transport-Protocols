/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;


/**
 * Factory class for instantiating a given TokenFilter
 * @author nikhillo
 *
 */
public class TokenFilterFactory {
	/**
	 * Static method to return an instance of the factory class.
	 * Usually factory classes are defined as singletons, i.e. 
	 * only one instance of the class exists at any instance.
	 * This is usually achieved by defining a private static instance
	 * that is initialized by the "private" constructor.
	 * On the method being called, you return the static instance.
	 * This allows you to reuse expensive objects that you may create
	 * during instantiation
	 * @return An instance of the factory
	 */
	public static TokenFilterFactory getInstance() {
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		return new TokenFilterFactory();
	}
	
	/**
	 * Returns a fully constructed {@link TokenFilter} instance
	 * for a given {@link TokenFilterType} type
	 * @param type: The {@link TokenFilterType} for which the {@link TokenFilter}
	 * is requested
	 * @param stream: The TokenStream instance to be wrapped
	 * @return The built {@link TokenFilter} instance
	 */
	public TokenFilter getFilterByType(TokenFilterType type, TokenStream stream) {
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		
		TokenFilter tokFilter = null;
		
		try
		{
				switch(type)
				{
				case SYMBOL:
					Symbol symbol = new Symbol(stream);
					tokFilter = symbol.symbolFilter(stream);
					break;
					
				case ACCENT:
					Accent accent = new Accent(stream);
					tokFilter = accent.accentFilter(stream);
					break;
					
				case CAPITALIZATION:
					Capitalization capital = new Capitalization(stream);
					tokFilter = capital.capitalFilter(stream);
					break;
					
				case CAPITALIZATION1:
					Capitalization1 capital1 = new Capitalization1(stream);
					tokFilter = capital1.capitalFilter(stream);
					break;
					
				case SPECIALCHARS:
					SpecialChars specialchar = new SpecialChars(stream);
					tokFilter = specialchar.SpecialcharstringFilter(stream);
					break;
					
				case DATE:
					Dates date = new Dates(stream);
					tokFilter = date.dateFilter(stream);
					break;
					
				case NUMERIC:
					Numbers number = new Numbers(stream);
					tokFilter = number.NumbersFilter(stream);
					break;
					
				case STEMMER:
					Stemmer stem = new Stemmer(stream);
					tokFilter = stem.StemmerFilter(stream);
					break;
					
				case STOPWORD:
					Stopwords stop = new Stopwords(stream);
					tokFilter = stop.StopwordFilter(stream);
					break;
					
				default:
						break;
				}
				
				//tokFilter = new TokenFilter
		}
		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}	
		
		return tokFilter;
	}
}
