package edu.buffalo.cse.irf14.query;


/**
 * Class that represents a parsed query
 * 
 * @author nikhillo
 *
 */
public class Query {
	
	String queryStr;

	public Query() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Query(String parsedQuery) {
		// TODO Auto-generated constructor stub

		queryStr = parsedQuery;
	}

	/**
	 * Method to convert given parsed query into string
	 */

	public String toString() {
		// TODO: YOU MUST IMPLEMENT THIS
		
		return queryStr;
	}
}
