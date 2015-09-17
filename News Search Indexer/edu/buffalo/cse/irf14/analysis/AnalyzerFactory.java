/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * This factory class is responsible for instantiating "chained" {@link Analyzer} instances
 */
public class AnalyzerFactory {
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
	public static AnalyzerFactory getInstance() {
		//TODO: YOU NEED TO IMPLEMENT THIS METHOD
		return new AnalyzerFactory();
	}
	
	/**
	 * Returns a fully constructed and chained {@link Analyzer} instance
	 * for a given {@link FieldNames} field
	 * Note again that the singleton factory instance allows you to reuse
	 * {@link TokenFilter} instances if need be
	 * @param name: The {@link FieldNames} for which the {@link Analyzer}
	 * is requested
	 * @param TokenStream : Stream for which the Analyzer is requested
	 * @return The built {@link Analyzer} instance for an indexable {@link FieldNames}
	 * null otherwise
	 */
	public Analyzer getAnalyzerForField(FieldNames name, TokenStream stream) {
		//TODO : YOU NEED TO IMPLEMENT THIS METHOD
		
		Analyzer an = null;
		
		try
		{
			switch(name)
			{
				case FILEID:
					FileID fileID = new FileID();
					an = fileID.fileTokenFilter(stream);
					break;
					
				case CATEGORY:
					Category category = new Category();
					an = category.categoryTokenFilter(stream);
					break;
					
				case AUTHOR:
					Author auth = new Author();
					an = auth.authorTokenFilter(stream);
					break;
					
				case AUTHORORG:
					AuthorOrg authOrg = new AuthorOrg();
					an = authOrg.authorOrgTokenFilter(stream);
					break;
					
				case CONTENT:
					Content content = new Content();
					an = content.contentTokenFilter(stream);
					break;
					
				case NEWSDATE:
					NewsDate newsdate = new NewsDate();
					an = newsdate.newsdateTokenFilter(stream);
					break;
					
				case PLACE:
					Place place = new Place();
					an = place.placeTokenFilter(stream);
					break;
					
				case TITLE:
					Title title = new Title();
					an = title.titleTokenFilter(stream);
					break;
					
				default:
					break;
			}
		}
		
		catch(Exception e)
		{
			
		}
		return an;
	}
}
