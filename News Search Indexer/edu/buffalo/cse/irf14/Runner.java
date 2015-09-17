/**
 * 
 */
package edu.buffalo.cse.irf14;

import java.io.File;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.util.*;

import edu.buffalo.cse.irf14.SearchRunner.ScoringModel;
import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.document.ParserException;
import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;
import edu.buffalo.cse.irf14.index.IndexWriter;
import edu.buffalo.cse.irf14.index.IndexerException;
import edu.buffalo.cse.irf14.query.Query;
import edu.buffalo.cse.irf14.query.QueryParser;

/**
 * @author nikhillo
 *
 */
public class Runner {

	/**
	 * 
	 */
	public Runner() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ipDir = args[0];
		String indexDir = args[1];
		// more? idk!

		File ipDirectory = new File(ipDir);
		String[] catDirectories = ipDirectory.list();

		String[] files;
		File dir;
		Document d = null;
		IndexWriter writer = new IndexWriter(indexDir);

//		try {
//			for (String cat : catDirectories) {
//				dir = new File(ipDir + File.separator + cat);
//				files = dir.list();
//
//				if (files == null)
//					continue;
//
//				for (String f : files) {
//					try {
//						d = Parser.parse(dir.getAbsolutePath() + File.separator
//								+ f);
//						writer.addDocument(d);
//					} catch (ParserException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//
//			}
//
//			writer.close();
		
			File f = new File("D:\\Java Workspace\\IR Proj1\\File\\InputFile.txt");
			PrintStream so = new PrintStream(System.out);
			SearchRunner sr = new SearchRunner("E:\\UB\\IR CSE535\\Projects\\news_training\\INDEX", "E:\\Corpus", 'Q', so);
			sr.query("ADOBE", ScoringModel.OKAPI);
			sr.query(f);
//		} catch (IndexerException e) {
////			 TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
			}
}
