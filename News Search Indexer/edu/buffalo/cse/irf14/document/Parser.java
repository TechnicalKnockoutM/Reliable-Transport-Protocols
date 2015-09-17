/**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.io.*;
import java.util.ArrayList;

/**
 * @author nikhillo
 * Class that parses a given file into a Document
 */
public class Parser {
	/**
	 * Static method to parse the given file into the Document object
	 * @param filename : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException In case any error occurs during parsing
	 */
	public static Document parse(String filename) throws ParserException {
		
		
		if(filename==null || filename=="")
			throw new ParserException("Blank file or null file");
		// TODO YOU MUST IMPLEMENT THIS
		String[] FileId = new String[1];
		String CategoryPath;
		String[] Category = new String[1];
		String[] Title = new String[1];
		String[] Author;
		ArrayList<String> AuthorAL = new ArrayList<String>();
		String[] AuthorOrg = new String[1];
		String[] Place = new String[1];
		String[] NewsDate = new String[1];
		String[] Content = new String[1];
		
		Content[0] = "";
		
		try{
			
		String[] months = {"JAN ", "FEB ", "MAR ", "APR ", "MAY ", "JUN ", "JUL ", "AUG ", "SEP ", "OCT ", "NOV ", "DEC ", "JANUARY ", "FEBRUARY ", "MARCH ", "APRIL ", "JUNE ", "JULY ", "AUGUST ", "SEPTEMBER ", "OCTOBER ", "NOVEMBER ", "DECEMBER "};
			
		
		
		if(filename.lastIndexOf(File.separator) < 0)
				{
					throw new FileNotFoundException("Invalid File or File not found");
				}
		
		FileReader fr = new FileReader(filename);
		
			FileId[0] = filename.substring(filename.lastIndexOf(File.separator)+1);
			CategoryPath = filename.substring(0, filename.lastIndexOf(File.separator));
			Category[0] = CategoryPath.substring(CategoryPath.lastIndexOf(File.separator)+1);
					
			
			BufferedReader br = new BufferedReader(fr);
			String currentLine;
			Boolean titleFlag = false;
			Boolean titleFlagMulLine = false;
			//Boolean startOfFileFlag = false;
			Boolean placeFieldCapFlag = false;

			
			while((currentLine = br.readLine()) != null)
			{
				if(currentLine.length() > 0)
				{	
					if(currentLine.trim().startsWith("<AUTHOR>"))//add code for multiple authors
					{
						int authorPosition = 0;
						int authorEndPosition ;
						String replacementString = "";
						
						
						if(currentLine.trim().indexOf("By")>0)
						{
							authorPosition = currentLine.trim().indexOf("By");
							replacementString = "By";
						}
						
						else if(currentLine.trim().indexOf("BY")>0)
						{
							authorPosition = currentLine.trim().indexOf("BY");
							replacementString = "BY";
						}
						
						else if(currentLine.trim().indexOf("by")>0)
						{
							authorPosition = currentLine.trim().indexOf("by");
							replacementString = "by";
						}
						
						authorEndPosition = currentLine.trim().indexOf("</AUTHOR>");
						
						if(currentLine.indexOf(",")>0)
						{
							String tempAuthorString = currentLine.trim().substring(authorPosition, currentLine.indexOf(",")).trim();
							AuthorAL.add(tempAuthorString.replace(replacementString,"").trim());
							tempAuthorString = currentLine.trim().substring(currentLine.indexOf(","), authorEndPosition).trim();
							
							AuthorOrg[0] = tempAuthorString.replace(", ", "").trim();
						}
						
						else
						{
							String tempAuthorString = currentLine.trim().substring(authorPosition, authorEndPosition).trim();
							AuthorAL.add(tempAuthorString.replace(replacementString,"").trim());
						}
					}
					

					else if (!placeFieldCapFlag)
					{
						String tempString = currentLine.trim();
						
						for(String s : months)
						{
							if(tempString.toUpperCase().contains(s) && tempString.contains(" -") && tempString.contains(", "))
							{
								
								Place[0] = currentLine.trim().substring(0, currentLine.trim().toUpperCase().indexOf(s)-2);
								int startPosition = currentLine.trim().toUpperCase().indexOf(s);
								int endPosition = currentLine.trim().indexOf(" -");
								//String tempCurrentLine = currentLine.trim().substring(startPosition);
								
								if(startPosition < endPosition)
								{
									NewsDate[0] = currentLine.trim().substring(startPosition, endPosition);
									
									if(currentLine.trim().indexOf(" - ") > 0)
									{
										Content[0] = Content[0].concat(currentLine.trim().substring(currentLine.trim().indexOf(" - ") + 2));
									}
									placeFieldCapFlag = true;
									break;
								}
							}
						}
						
						if(!placeFieldCapFlag && (!titleFlag || titleFlagMulLine))
						{
							if(titleFlagMulLine)
							{
								Title[0] = Title[0] + " " + currentLine;

							}
							
							else
							{
								Title[0] = currentLine;
								titleFlag = true;

								titleFlagMulLine = true;
							}
						}
					}
					
					else if(placeFieldCapFlag || titleFlag )
					{
						Content[0] = Content[0].concat(currentLine).concat(" ") ;

					}				
				}
				
				else
				{
					titleFlagMulLine = false;
				}
			}
			
			br.close();
		}

		catch(FileNotFoundException f)
		{
			throw new ParserException("Invalid file name");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		Document doc = new Document();
		
		doc.setField(FieldNames.FILEID, FileId);
		doc.setField(FieldNames.CATEGORY, Category);
		doc.setField(FieldNames.TITLE, Title);
		doc.setField(FieldNames.PLACE, Place);
		doc.setField(FieldNames.NEWSDATE, NewsDate);
		doc.setField(FieldNames.CONTENT, Content);
		

		if(AuthorAL.size() > 0)
		{
			Author = new String[AuthorAL.size()];
			for(int i=0; i < AuthorAL.size(); i++)
			{
				Author[i] = AuthorAL.get(i);
			}
			
			doc.setField(FieldNames.AUTHOR, Author);
			doc.setField(FieldNames.AUTHORORG, AuthorOrg);
		}
		
			return doc;

	}
}
