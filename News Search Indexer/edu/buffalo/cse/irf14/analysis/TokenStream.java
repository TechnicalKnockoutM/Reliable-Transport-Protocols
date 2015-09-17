/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author nikhillo
 * Class that represents a stream of Tokens. All {@link Analyzer} and
 * {@link TokenFilter} instances operate on this to implement their
 * behavior
 */
public class TokenStream implements Iterator<Token>{
	
	
	private ArrayList<String> tokArray;
	private ArrayList<String> tokArray1= new ArrayList<String>();
	boolean nextElement=false;
//	private int currentsize=tokArray.size();
//	private static int currentindex=0;
	private int currentindex;//Modified by Susana
	int indexvalue;
	int indexstate;
	Token tk;//Added by Susana
	
	/**
	 * Method that checks if there is any Token left in the stream
	 * with regards to the current pointer.
	 * DOES NOT ADVANCE THE POINTER
	 * @return true if at least one Token exists, false otherwise
	 */
	
	public TokenStream(ArrayList <String> tokArray)
	{
	this.tokArray=tokArray;	
	currentindex=-1;
	}
	
	
	@Override
	public boolean hasNext() {
		// TODO YOU MUST IMPLEMENT THIS
//		if( currentindex < tokArray.size() && tokArray.get(currentindex) != null){
		if( currentindex < tokArray.size()-1){//Modified by Susana
			nextElement=true;
			return true;
		}
		else
		return false;
	}

	/**
	 * Method to return the next Token in the stream. If a previous
	 * hasNext() call returned true, this method must return a non-null
	 * Token.
	 * If for any reason, it is called at the end of the stream, when all
	 * tokens have already been iterated, return null
	 */
	@Override
	public Token next() {
		// TODO YOU MUST IMPLEMENT THIS
		
		if(hasNext()){
//			indexvalue=currentindex++; 
			indexvalue=++currentindex;//Modified by Susana
		
		String tokenString = tokArray.get(indexvalue);
//		Token tk = new Token(tokenString);
		tk=new Token(tokenString);//Modified by Susana
//		indexvalue++;
//		++currentindex;//Modified by Susana
		
		return tk;	
		}
		else
			tk = null;//Added by Susana
//			return null;
			return tk;//Modified by Susana
		
	}
	
	/**
	 * Method to remove the current Token from the stream.
	 * Note that "current" token refers to the Token just returned
	 * by the next method. 
	 * Must thus be NO-OP when at the beginning of the stream or at the end
	 */
	@Override
	public void remove() {
		// TODO YOU MUST IMPLEMENT THIS
//		if(currentindex > 0)//Added by Susana
//		--currentindex;
//		if(nextElement && hasNext()){
		if(nextElement){ //Modified by Susana
			nextElement=false;
//			tokArray.remove(indexvalue);
			tokArray.remove(currentindex);//Modified by Susana
			--currentindex;//Added by Susana
		}
	}
	
	/**
	 * Method to reset the stream to bring the iterator back to the beginning
	 * of the stream. Unless the stream has no tokens, hasNext() after calling
	 * reset() must always return true.
	 */
	public void reset() {
		//TODO : YOU MUST IMPLEMENT THIS
//		TokenStream.currentindex=0;
		
		currentindex=-1;//Modified by Susana
		
	}
	
	/**
	 * Method to append the given TokenStream to the end of the current stream
	 * The append must always occur at the end irrespective of where the iterator
	 * currently stands. After appending, the iterator position must be unchanged
	 * Of course this means if the iterator was at the end of the stream and a 
	 * new stream was appended, the iterator hasn't moved but that is no longer
	 * the end of the stream.
	 * @param stream : The stream to be appended
	 * @throws NullPointerException 
	 */
	public void append(TokenStream stream) throws NullPointerException {
		//TODO : YOU MUST IMPLEMENT THIS
	try{
		
		if(stream != null)		//Added by Susana
		{
			indexstate=currentindex;
			stream.reset();
			
			while((stream.hasNext()))
			{
				tk=stream.next();
				String newStream=tk.toString();
				tokArray1.add(newStream);
			}
			
			tokArray.addAll(tokArray1);
			currentindex=indexstate;
		}
		
	}
	catch(NullPointerException e)
	{
		throw e;
	}	
	
	
	}
	
	
	/**
	 * Method to get the current Token from the stream without iteration.
	 * The only difference between this method and {@link TokenStream#next()} is that
	 * the latter moves the stream forward, this one does not.
	 * Calling this method multiple times would not alter the return value of {@link TokenStream#hasNext()}
	 * @return The current {@link Token} if one exists, null if end of stream
	 * has been reached or the current Token was removed
	 */
	public Token getCurrent() {

		//TODO: YOU MUST IMPLEMENT THIS
		
//		if(currentindex < 0)
//			return null;
//		
//		String tokenString = tokArray.get(indexvalue);
//		Token tk=new Token(tokenString);
		//Added by Susana
		
		boolean tokenFlag = false;
		if(tk != null)
		{
			for(String tok : tokArray)
			{
				if(tok.equals(tk.toString()))
				{
					tokenFlag = true;
					break;
				}
			}
			
			if(!tokenFlag)
			{
				tk = null;
			}
		}
		return tk;
	}
	
}
