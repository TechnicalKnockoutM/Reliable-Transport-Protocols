package edu.buffalo.cse.irf14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.omg.CORBA.IRObject;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;
import edu.buffalo.cse.irf14.index.IndexWriter;
import edu.buffalo.cse.irf14.query.Query;
import edu.buffalo.cse.irf14.query.QueryParser;

/**
 * Main class to run the searcher. As before implement all TODO methods unless
 * marked for bonus
 * 
 * @author nikhillo
 *
 */
public class SearchRunner {
	public enum ScoringModel {
		TFIDF, OKAPI
	}

	private String indexDir;
	private String corpusDir;
	private PrintStream stream;
	static TreeMap<String, TreeMap<String, ArrayList<Integer>>> termPosIndex = new TreeMap<String, TreeMap<String, ArrayList<Integer>>>();
	// String userQuery = "{ Term:hello OR Term:world }";

	TreeMap<String, Integer> docDict = new TreeMap<String, Integer>();
	TreeMap<String, Integer> termDict = new TreeMap<String, Integer>();
	TreeMap<String, Integer> authorDict = new TreeMap<String, Integer>();
	TreeMap<String, Integer> catDict = new TreeMap<String, Integer>();
	TreeMap<String, Integer> placeDict = new TreeMap<String, Integer>();

	TreeMap<Integer, TreeMap<Integer, Integer>> authorIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	TreeMap<Integer, TreeMap<Integer, Integer>> termIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	TreeMap<Integer, TreeMap<Integer, Integer>> catIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	TreeMap<Integer, TreeMap<Integer, Integer>> placeIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();

	TreeMap<Integer, TreeMap<Integer, Double>> fwdAuthorIndex = new TreeMap<Integer, TreeMap<Integer, Double>>();
	TreeMap<Integer, TreeMap<Integer, Double>> fwdTermIndex = new TreeMap<Integer, TreeMap<Integer, Double>>();
	TreeMap<Integer, TreeMap<Integer, Double>> fwdCatIndex = new TreeMap<Integer, TreeMap<Integer, Double>>();
	TreeMap<Integer, TreeMap<Integer, Double>> fwdPlaceIndex = new TreeMap<Integer, TreeMap<Integer, Double>>();

	TreeMap<Integer, TreeMap<Integer, Double>> fwdAuthorIndexOkapi = new TreeMap<Integer, TreeMap<Integer, Double>>();
	TreeMap<Integer, TreeMap<Integer, Double>> fwdTermIndexOkapi = new TreeMap<Integer, TreeMap<Integer, Double>>();
	TreeMap<Integer, TreeMap<Integer, Double>> fwdCatIndexOkapi = new TreeMap<Integer, TreeMap<Integer, Double>>();
	TreeMap<Integer, TreeMap<Integer, Double>> fwdPlaceIndexOkapi = new TreeMap<Integer, TreeMap<Integer, Double>>();

	/**
	 * Default (and only public) constructor
	 * 
	 * @param indexDir
	 *            : The directory where the index resides
	 * @param corpusDir
	 *            : Directory where the (flattened) corpus resides
	 * @param mode
	 *            : Mode, one of Q or E
	 * @param stream
	 *            : Stream to write output to
	 */
	public SearchRunner(String indexDir, String corpusDir, char mode,
			PrintStream stream) {
		// TODO: IMPLEMENT THIS METHOD
		this.indexDir = indexDir;
		this.corpusDir = corpusDir;
		this.stream = stream;
		FileReader fr = null;
		File ipDirectory = new File(corpusDir);
		String[] catDirectories = ipDirectory.list();

		IndexReader ir = new IndexReader(indexDir, null);

		docDict = ir.GetDict(IndexType.OTHER);
		termDict = ir.GetDict(IndexType.TERM);
		authorDict = ir.GetDict(IndexType.AUTHOR);
		catDict = ir.GetDict(IndexType.CATEGORY);
		placeDict = ir.GetDict(IndexType.PLACE);

		authorIndex = ir.GetIndex(IndexType.AUTHOR);
		termIndex = ir.GetIndex(IndexType.TERM);
		catIndex = ir.GetIndex(IndexType.CATEGORY);
		placeIndex = ir.GetIndex(IndexType.PLACE);

		fwdAuthorIndex = ir.GetFwdIndex(IndexType.AUTHOR);
		fwdTermIndex = ir.GetFwdIndex(IndexType.TERM);
		fwdCatIndex = ir.GetFwdIndex(IndexType.CATEGORY);
		fwdPlaceIndex = ir.GetFwdIndex(IndexType.PLACE);

		fwdAuthorIndexOkapi = ir.GetFwdIndexokapi(IndexType.AUTHOR);
		fwdTermIndexOkapi = ir.GetFwdIndexokapi(IndexType.TERM);
		fwdCatIndexOkapi = ir.GetFwdIndexokapi(IndexType.CATEGORY);
		fwdPlaceIndexOkapi = ir.GetFwdIndexokapi(IndexType.PLACE);
	}

	public Map<Integer, Double> tfidfValue(Map<Integer, Integer> map) {

		ArrayList<Double> tfidf_value = new ArrayList<Double>();
		TreeMap<Integer, Double> doc_score = new TreeMap<Integer, Double>();
		TreeMap<Integer, Double> final_doc_score = new TreeMap<Integer, Double>();
		IndexReader ir = new IndexReader();
		int docid;
		double total_num_terms;
		double total_num_docs = ir.docDict.size();
		double total_num_docs_term = map.size();

		for (Map.Entry<Integer, Integer> termFreq : map.entrySet()) {
			docid = termFreq.getKey();
			total_num_terms = termFreq.getValue();
			double tf = (double) Math.log10(1.0 + total_num_terms);
			// double idf = (double) Math.log10(total_num_docs
			// / (1.0 + (total_num_docs_term)));
			// tfidf_value.add(tf * idf);
			tfidf_value.add(tf);
			Collections.sort(tfidf_value);
			Collections.reverse(tfidf_value);
			// doc_score.put(docid, tf * idf);
			doc_score.put(docid, tf);

			// if (tfidf_value.size() >= 10) {
			// for (int i = 0; i < 10; i++) {
			//
			// for (Map.Entry<Integer, Double> score : doc_score
			// .entrySet()) {
			// if (tfidf_value.get(i) == score.getValue()) {
			// final_doc_score.put(score.getKey(),
			// score.getValue());
			// }
			// }
			// }
			// } else {
			// for (int i = 0; i < tfidf_value.size(); i++) {
			//
			// for (Map.Entry<Integer, Double> score : doc_score
			// .entrySet()) {
			// if (tfidf_value.get(i) == score.getValue()) {
			// final_doc_score.put(score.getKey(),
			// score.getValue());
			// }
			// }
			// }
			// }
		}
		return doc_score;// final_doc_score;
	}

	public Map<Integer, Double> okapiValue(Map<Integer, Integer> map,
			double k1, double b, double k3) {

		ArrayList<Double> okapi_value = new ArrayList<Double>();
		TreeMap<Integer, Double> doc_score = new TreeMap<Integer, Double>();
		TreeMap<Integer, Double> final_doc_score = new TreeMap<Integer, Double>();
		TreeMap<String, Double> Average_Doc_Length = new TreeMap<String, Double>();
		TreeMap<Integer, Integer> Doc_Length = new TreeMap<Integer, Integer>();
		IndexReader ir = new IndexReader(indexDir, IndexType.AUTHOR);
		IndexReader ir1 = new IndexReader();
		IndexWriter iw = new IndexWriter();
		int docid;
		double total_num_terms;
		double len_of_doc = 0;
		double avg_len_doc = 0;
		double total_num_docs = ir.docDict.size();
		double total_num_docs_term = map.size();
		double final_score = 0.0;

		Doc_Length = ir1.DocLength;
		// for (Map.Entry<Integer, Integer> dl : Doc_Length
		// .entrySet()) {
		// len_of_doc=dl.getValue();
		// }
		// avgDocLength=iw.averageDocLength();
		Average_Doc_Length = ir1.avgDocLength;

		for (Map.Entry<String, Double> adl : Average_Doc_Length.entrySet()) {
			avg_len_doc = adl.getValue();
		}

		for (Map.Entry<Integer, Integer> termFreq : map.entrySet()) {
			docid = termFreq.getKey();
			total_num_terms = termFreq.getValue();
			len_of_doc = Doc_Length.get(docid);

			double idf = (double) Math
					.log10(total_num_docs - total_num_docs_term + 0.5
							/ (0.5 + (total_num_docs_term)));

			double rest_formula = ((total_num_terms) * (k1 + 1))
					/ (total_num_terms + k1
							* (1 - b + b * (len_of_doc / avg_len_doc)));

			double k3_formula = ((k3 + 1) * (total_num_terms))
					/ (k3 + (total_num_terms));

			if (k3 == 0) {
				final_score = idf * rest_formula;
			} else {
				final_score = idf * rest_formula * k3_formula;
			}
			okapi_value.add(final_score);

			Collections.sort(okapi_value);
			Collections.reverse(okapi_value);
			doc_score.put(docid, final_score);

			if (okapi_value.size() >= 10) {
				for (int i = 0; i < 10; i++) {

					for (Map.Entry<Integer, Double> score : doc_score
							.entrySet()) {
						if (okapi_value.get(i) == score.getValue()) {
							final_doc_score.put(score.getKey(),
									score.getValue());
						}
					}
				}
			} else {
				for (int i = 0; i < okapi_value.size(); i++) {

					for (Map.Entry<Integer, Double> score : doc_score
							.entrySet()) {
						if (okapi_value.get(i) == score.getValue()) {
							final_doc_score.put(score.getKey(),
									score.getValue());
						}
					}
				}
			}
		}
		return final_doc_score;
	}

	/**
	 * Method to execute given query in the Q mode
	 * 
	 * @param userQuery
	 *            : Query to be parsed and executed
	 * @param model
	 *            : Scoring Model to use for ranking results
	 */
	public void query(String userQuery, ScoringModel model) {
		// TODO: IMPLEMENT THIS METHOD
		long startTime = System.currentTimeMillis();
		Query q = null;

		q = QueryParser.parse(userQuery, "OR");

		String terms = q.toString();

		// String terms = "{ Term:adobe }";

		String splitTerms[] = terms.split(" ");
		String tempSplitTerms[] = splitTerms;

		ArrayList<Double> scores = new ArrayList<Double>();
		ArrayList<TreeMap<String, Double>> mapAL = new ArrayList<TreeMap<String, Double>>();
		ArrayList<TreeMap<String, TreeMap<Integer, Double>>> finalMapAL = new ArrayList<TreeMap<String, TreeMap<Integer, Double>>>();
		ArrayList<Boolean> opAL = new ArrayList<Boolean>();
		ArrayList<Double> tfidfScoreAL = new ArrayList<Double>();
		ArrayList<Double> okapiScoreAL = new ArrayList<Double>();

		TreeMap<Integer, Integer> postingsList = null;
		TreeMap<Integer, Double> tfidfList = null;
		TreeMap<Integer, Double> okapiList = null;
		Double score = 0.0;
		TreeMap<String, Double> tfidfScore = new TreeMap<String, Double>();
		TreeMap<String, Double> okapiScore = new TreeMap<String, Double>();

		TreeMap<String, TreeMap<Integer, Double>> tfidfFinalScore = new TreeMap<String, TreeMap<Integer, Double>>();
		TreeMap<Integer, Double> tfidfPostingsList = null;
		TreeMap<String, TreeMap<Integer, Double>> okapiFinalScore = new TreeMap<String, TreeMap<Integer, Double>>();
		TreeMap<Integer, Double> Final_tfidf = new TreeMap<Integer, Double>();
		TreeMap<String, Double> Display_tfidf = new TreeMap<String, Double>();

		TreeMap<Integer, Double> queryScore = new TreeMap<Integer, Double>();
		TreeMap<Integer, Double> finalQueryScore = new TreeMap<Integer, Double>();

		TreeMap<String, Double> temp = null;
		TreeMap<String, Double> temp1 = null;
		TreeMap<String, Double> buffered = null;
		TreeMap<String, TreeMap<Integer, Double>> finalBuffered = null;
		String docName = "";
		String tempStr = "";

		boolean operator = false;
		boolean tempOp = false;
		boolean removeEntry = false;
		boolean quotes = false;

		AnalyzerFactory factory = AnalyzerFactory.getInstance();
		Analyzer an = null;
		Tokenizer tok = new Tokenizer("\" \"");
		TokenStream ts = null;
		Token tkn = null;

		switch (model) {
		case TFIDF:
			for (int i = 0; i < splitTerms.length; i++) {

				if (splitTerms[i].startsWith("<")) {
					removeEntry = true;
					splitTerms[i] = splitTerms[i].substring(1,
							splitTerms[i].length() - 2);
				} else {
					removeEntry = false;
				}

				if (splitTerms[i].toLowerCase().startsWith("term:")) {

					String s = splitTerms[i].replaceAll("Term:", "");
					s = splitTerms[i].replaceAll("term:", "");

					if (quotes) {
						try {
							String otherStr = tempStr;
							otherStr = otherStr + " " + s;
							otherStr = otherStr.replaceAll("\"", "");
							an = factory.getAnalyzerForField(
									FieldNames.CONTENT, tok.consume(otherStr));
							quotes = false;
						}

						catch (Exception e) {

						}

					} else {
						try {
							tempStr = "";
							an = factory.getAnalyzerForField(
									FieldNames.CONTENT, tok.consume(s));
						}

						catch (Exception e) {

						}
					}
					ts = an.getStream();
					ts.reset();

					tkn = ts.next();
					String tknstr = tkn.toString();

					int id = termDict.get(tknstr);
					postingsList = termIndex.get(id);

					if (queryScore.get(id) == null) {
						// queryScore.put(id, queryScore.get(id) + 1.0);
						queryScore.put(id, 1.0);
					} else {
						// queryScore.put(id, 1.0);
						queryScore.put(id, queryScore.get(id) + 1.0);
					}

					if (operator) // for AND
					{
						for (Map.Entry<String, TreeMap<Integer, Double>> matchExistence : tfidfFinalScore
								.entrySet()) {
							if (postingsList.containsKey(docDict
									.get(matchExistence.getKey()))) {
								// do nothing
							} else {
								tfidfFinalScore.remove(matchExistence);
							}
						}
					}

					for (Map.Entry<Integer, Integer> doc : postingsList
							.entrySet()) // get
											// Postings
											// list
											// for
											// a
											// term
					{
//						System.out.println(doc.getKey());
						// System.out.println(fwdTermIndex.get(doc.getKey()));
						if (fwdTermIndex.containsKey(doc.getKey())) {
							tfidfList = fwdTermIndex.get(doc.getKey()); // get
																		// tfidf
																		// list
																		// for
																		// a
																		// given
																		// document
							score = tfidfList.get(id); // get tfidf score for a
														// given
														// term in a document

							A: for (Map.Entry<String, Integer> docD : docDict
									.entrySet()) // get
													// Document
													// Name
							{
								// if(docD.getValue() == 6600)
								// {
								// System.out.println(docD.getValue());
								// }

								if (docD.getValue().equals(doc.getKey())) {
									// if (docD.getValue().equals(6600) ){
									docName = docD.getKey();

									break A;
								}

							}

							if (!removeEntry) {
								if (!operator) {// True for AND and false for OR

									if (tfidfScore != null) {
										if (tfidfScore.containsKey((docName))) {
											tfidfScore.put(docName,
													tfidfScore.get(docName)
															+ score);
										}

										else {
											tfidfScore.put(docName, score);
										}
									} else {
										tfidfScore.put(docName, score);
									}

									if (!tfidfFinalScore.equals(null)) {
										if (tfidfFinalScore
												.containsKey((docName))) {

											tfidfPostingsList = tfidfFinalScore
													.get(docName);

											tfidfPostingsList.put(id, score);

											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}

										else {
											tfidfPostingsList = new TreeMap<Integer, Double>();
											tfidfPostingsList.put(id, score);
											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}
									} else {
										tfidfPostingsList.put(id, score);
										tfidfFinalScore.put(docName,
												tfidfPostingsList);
									}

								} else {
									temp.put(docName, score);
									// temp.put(doc.getKey(), score);

									temp1 = tfidfScore;
									tfidfScore = new TreeMap<String, Double>();

									for (Map.Entry<String, Double> newList : temp
											.entrySet()) {
										for (Map.Entry<String, Double> oldList : temp1
												.entrySet()) {
											if (newList.getKey().equals(
													oldList.getKey())) {
												double tempScore = newList
														.getValue()
														+ oldList.getValue();
												tfidfScore.put(
														newList.getKey(),
														tempScore);
											}
										}
									}

									if (tfidfFinalScore != null) {
										if (tfidfFinalScore
												.containsKey((docName))) {

											tfidfPostingsList = tfidfFinalScore
													.get(docName);

											tfidfPostingsList.put(id, score);

											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}

										// else {
										// tfidfPostingsList = null;
										// tfidfPostingsList.put(id, score);
										// tfidfFinalScore.put(docName,
										// tfidfPostingsList);
										// }
									}
									// else {
									// tfidfPostingsList.put(id, score);
									// tfidfFinalScore.put(docName,
									// tfidfPostingsList);
									// }

								}
							} else {
								tfidfScore.remove(docName);
								tfidfFinalScore.remove(docName);
							}
						}
					}

				}

				else if (splitTerms[i].toLowerCase().startsWith("category:")) {
					String s = splitTerms[i].replaceAll("Category:", "");
					s = splitTerms[i].replaceAll("category:", "");
					if (quotes) {
						try {

							tempStr = tempStr + " " + s;
							tempStr = tempStr.replaceAll("\"", "");
							an = factory.getAnalyzerForField(
									FieldNames.CATEGORY, tok.consume(tempStr));
							quotes = false;
						} catch (Exception e) {

						}
					} else {
						try {
							tempStr = "";
							an = factory.getAnalyzerForField(
									FieldNames.CATEGORY, tok.consume(s));
						}

						catch (Exception e) {

						}
					}
					ts = an.getStream();
					ts.reset();

					tkn = ts.next();
					String tknstr = tkn.toString();

					int id = catDict.get(tknstr);
					postingsList = catIndex.get(id);

					if (queryScore.get(id) == null) {
						// queryScore.put(id, queryScore.get(id) + 1.0);
						queryScore.put(id, 1.0);
					} else {
						// queryScore.put(id, 1.0);
						queryScore.put(id, queryScore.get(id) + 1.0);
					}

					if (operator) // for AND
					{
						for (Map.Entry<String, TreeMap<Integer, Double>> matchExistence : tfidfFinalScore
								.entrySet()) {
							if (postingsList.containsKey(docDict
									.get(matchExistence.getKey()))) {
								// do nothing
							} else {
								tfidfFinalScore.remove(matchExistence);
							}
						}
					}

					for (Map.Entry<Integer, Integer> doc : postingsList
							.entrySet()) // get
											// Postings
											// list
											// for
											// a
											// term
					{
//						System.out.println(doc.getKey());
						// System.out.println(fwdTermIndex.get(doc.getKey()));
						if (fwdCatIndex.containsKey(doc.getKey())) {
							tfidfList = fwdCatIndex.get(doc.getKey()); // get
																		// tfidf
																		// list
																		// for
																		// a
																		// given
																		// document
							score = tfidfList.get(id); // get tfidf score for a
														// given
														// term in a document

							A: for (Map.Entry<String, Integer> docD : docDict
									.entrySet()) // get
													// Document
													// Name
							{
								// if(docD.getValue() == 6600)
								// {
								// System.out.println(docD.getValue());
								// }

								if (docD.getValue().equals(doc.getKey())) {
									// if (docD.getValue().equals(6600) ){
									docName = docD.getKey();

									break A;
								}

							}

							if (!removeEntry) {
								if (!operator) {// True for AND and false for OR

									if (tfidfScore != null) {
										if (tfidfScore.containsKey((docName))) {
											tfidfScore.put(docName,
													tfidfScore.get(docName)
															+ score);
										}

										else {
											tfidfScore.put(docName, score);
										}
									} else {
										tfidfScore.put(docName, score);
									}

									if (!tfidfFinalScore.equals(null)) {
										if (tfidfFinalScore
												.containsKey((docName))) {

											tfidfPostingsList = tfidfFinalScore
													.get(docName);

											tfidfPostingsList.put(id, score);

											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}

										else {
											tfidfPostingsList = new TreeMap<Integer, Double>();
											tfidfPostingsList.put(id, score);
											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}
									} else {
										tfidfPostingsList.put(id, score);
										tfidfFinalScore.put(docName,
												tfidfPostingsList);
									}

								} else {
									temp.put(docName, score);
									// temp.put(doc.getKey(), score);

									temp1 = tfidfScore;
									tfidfScore = new TreeMap<String, Double>();

									for (Map.Entry<String, Double> newList : temp
											.entrySet()) {
										for (Map.Entry<String, Double> oldList : temp1
												.entrySet()) {
											if (newList.getKey().equals(
													oldList.getKey())) {
												double tempScore = newList
														.getValue()
														+ oldList.getValue();
												tfidfScore.put(
														newList.getKey(),
														tempScore);
											}
										}
									}

									if (tfidfFinalScore != null) {
										if (tfidfFinalScore
												.containsKey((docName))) {

											tfidfPostingsList = tfidfFinalScore
													.get(docName);

											tfidfPostingsList.put(id, score);

											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}

										// else {
										// tfidfPostingsList = null;
										// tfidfPostingsList.put(id, score);
										// tfidfFinalScore.put(docName,
										// tfidfPostingsList);
										// }
									}
									// else {
									// tfidfPostingsList.put(id, score);
									// tfidfFinalScore.put(docName,
									// tfidfPostingsList);
									// }

								}
							} else {
								tfidfScore.remove(docName);
								tfidfFinalScore.remove(docName);
							}
						}
					}

				}
		 else if (splitTerms[i].toLowerCase().startsWith("author:")) {
					String s = splitTerms[i].replaceAll("Author:", "");
					s = splitTerms[i].replaceAll("author:", "");

					if (quotes) {
						try {

							tempStr = tempStr + " " + s;
							tempStr = tempStr.replaceAll("\"", "");
							an = factory.getAnalyzerForField(FieldNames.AUTHOR,
									tok.consume(tempStr));
							quotes = false;
						}

						catch (Exception e) {

						}

					} else {
						try {
							tempStr = "";
							an = factory.getAnalyzerForField(FieldNames.AUTHOR,
									tok.consume(s));
						}

						catch (Exception e) {

						}
					}
					ts = an.getStream();
					ts.reset();

					tkn = ts.next();
					String tknstr = tkn.toString();

					int id = authorDict.get(tknstr);
					postingsList = authorIndex.get(id);

					if (queryScore.get(id) == null) {
						// queryScore.put(id, queryScore.get(id) + 1.0);
						queryScore.put(id, 1.0);
					} else {
						// queryScore.put(id, 1.0);
						queryScore.put(id, queryScore.get(id) + 1.0);
					}

					if (operator) // for AND
					{
						for (Map.Entry<String, TreeMap<Integer, Double>> matchExistence : tfidfFinalScore
								.entrySet()) {
							if (postingsList.containsKey(docDict
									.get(matchExistence.getKey()))) {
								// do nothing
							} else {
								tfidfFinalScore.remove(matchExistence);
							}
						}
					}

					for (Map.Entry<Integer, Integer> doc : postingsList
							.entrySet()) // get
											// Postings
											// list
											// for
											// a
											// term
					{
//						System.out.println(doc.getKey());
						// System.out.println(fwdTermIndex.get(doc.getKey()));
						if (fwdAuthorIndex.containsKey(doc.getKey())) {
							tfidfList = fwdAuthorIndex.get(doc.getKey()); // get
																		// tfidf
																		// list
																		// for
																		// a
																		// given
																		// document
							score = tfidfList.get(id); // get tfidf score for a
														// given
														// term in a document

							A: for (Map.Entry<String, Integer> docD : docDict
									.entrySet()) // get
													// Document
													// Name
							{
								// if(docD.getValue() == 6600)
								// {
								// System.out.println(docD.getValue());
								// }

								if (docD.getValue().equals(doc.getKey())) {
									// if (docD.getValue().equals(6600) ){
									docName = docD.getKey();

									break A;
								}

							}

							if (!removeEntry) {
								if (!operator) {// True for AND and false for OR

									if (tfidfScore != null) {
										if (tfidfScore.containsKey((docName))) {
											tfidfScore.put(docName,
													tfidfScore.get(docName)
															+ score);
										}

										else {
											tfidfScore.put(docName, score);
										}
									} else {
										tfidfScore.put(docName, score);
									}

									if (!tfidfFinalScore.equals(null)) {
										if (tfidfFinalScore
												.containsKey((docName))) {

											tfidfPostingsList = tfidfFinalScore
													.get(docName);

											tfidfPostingsList.put(id, score);

											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}

										else {
											tfidfPostingsList = new TreeMap<Integer, Double>();
											tfidfPostingsList.put(id, score);
											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}
									} else {
										tfidfPostingsList.put(id, score);
										tfidfFinalScore.put(docName,
												tfidfPostingsList);
									}

								} else {
									temp.put(docName, score);
									// temp.put(doc.getKey(), score);

									temp1 = tfidfScore;
									tfidfScore = new TreeMap<String, Double>();

									for (Map.Entry<String, Double> newList : temp
											.entrySet()) {
										for (Map.Entry<String, Double> oldList : temp1
												.entrySet()) {
											if (newList.getKey().equals(
													oldList.getKey())) {
												double tempScore = newList
														.getValue()
														+ oldList.getValue();
												tfidfScore.put(
														newList.getKey(),
														tempScore);
											}
										}
									}

									if (tfidfFinalScore != null) {
										if (tfidfFinalScore
												.containsKey((docName))) {

											tfidfPostingsList = tfidfFinalScore
													.get(docName);

											tfidfPostingsList.put(id, score);

											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}

										// else {
										// tfidfPostingsList = null;
										// tfidfPostingsList.put(id, score);
										// tfidfFinalScore.put(docName,
										// tfidfPostingsList);
										// }
									}
									// else {
									// tfidfPostingsList.put(id, score);
									// tfidfFinalScore.put(docName,
									// tfidfPostingsList);
									// }

								}
							} else {
								tfidfScore.remove(docName);
								tfidfFinalScore.remove(docName);
							}
						}
					}

				}
				else if (splitTerms[i].toLowerCase().startsWith("place:")) {
					String s = splitTerms[i].replaceAll("Place:", "");
					s = splitTerms[i].replaceAll("place:", "");

					if (quotes) {
						try {

							tempStr = tempStr + " " + s;
							tempStr = tempStr.replaceAll("\"", "");
							an = factory.getAnalyzerForField(FieldNames.PLACE,
									tok.consume(tempStr));
							quotes = false;
						}

						catch (Exception e) {

						}

					} else {
						try {
							tempStr = "";
							an = factory.getAnalyzerForField(FieldNames.PLACE,
									tok.consume(s));
						}

						catch (Exception e) {

						}
					}
					ts = an.getStream();
					ts.reset();

					tkn = ts.next();
					String tknstr = tkn.toString();

					int id = placeDict.get(tknstr);
					postingsList = placeIndex.get(id);

					if (queryScore.get(id) == null) {
						// queryScore.put(id, queryScore.get(id) + 1.0);
						queryScore.put(id, 1.0);
					} else {
						// queryScore.put(id, 1.0);
						queryScore.put(id, queryScore.get(id) + 1.0);
					}

					if (operator) // for AND
					{
						for (Map.Entry<String, TreeMap<Integer, Double>> matchExistence : tfidfFinalScore
								.entrySet()) {
							if (postingsList.containsKey(docDict
									.get(matchExistence.getKey()))) {
								// do nothing
							} else {
								tfidfFinalScore.remove(matchExistence);
							}
						}
					}

					for (Map.Entry<Integer, Integer> doc : postingsList
							.entrySet()) // get
											// Postings
											// list
											// for
											// a
											// term
					{
//						System.out.println(doc.getKey());
						// System.out.println(fwdTermIndex.get(doc.getKey()));
						if (fwdPlaceIndex.containsKey(doc.getKey())) {
							tfidfList = fwdPlaceIndex.get(doc.getKey()); // get
																		// tfidf
																		// list
																		// for
																		// a
																		// given
																		// document
							score = tfidfList.get(id); // get tfidf score for a
														// given
														// term in a document

							A: for (Map.Entry<String, Integer> docD : docDict
									.entrySet()) // get
													// Document
													// Name
							{
								// if(docD.getValue() == 6600)
								// {
								// System.out.println(docD.getValue());
								// }

								if (docD.getValue().equals(doc.getKey())) {
									// if (docD.getValue().equals(6600) ){
									docName = docD.getKey();

									break A;
								}

							}

							if (!removeEntry) {
								if (!operator) {// True for AND and false for OR

									if (tfidfScore != null) {
										if (tfidfScore.containsKey((docName))) {
											tfidfScore.put(docName,
													tfidfScore.get(docName)
															+ score);
										}

										else {
											tfidfScore.put(docName, score);
										}
									} else {
										tfidfScore.put(docName, score);
									}

									if (!tfidfFinalScore.equals(null)) {
										if (tfidfFinalScore
												.containsKey((docName))) {

											tfidfPostingsList = tfidfFinalScore
													.get(docName);

											tfidfPostingsList.put(id, score);

											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}

										else {
											tfidfPostingsList = new TreeMap<Integer, Double>();
											tfidfPostingsList.put(id, score);
											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}
									} else {
										tfidfPostingsList.put(id, score);
										tfidfFinalScore.put(docName,
												tfidfPostingsList);
									}

								} else {
									temp.put(docName, score);
									// temp.put(doc.getKey(), score);

									temp1 = tfidfScore;
									tfidfScore = new TreeMap<String, Double>();

									for (Map.Entry<String, Double> newList : temp
											.entrySet()) {
										for (Map.Entry<String, Double> oldList : temp1
												.entrySet()) {
											if (newList.getKey().equals(
													oldList.getKey())) {
												double tempScore = newList
														.getValue()
														+ oldList.getValue();
												tfidfScore.put(
														newList.getKey(),
														tempScore);
											}
										}
									}

									if (tfidfFinalScore != null) {
										if (tfidfFinalScore
												.containsKey((docName))) {

											tfidfPostingsList = tfidfFinalScore
													.get(docName);

											tfidfPostingsList.put(id, score);

											tfidfFinalScore.put(docName,
													tfidfPostingsList);
										}

										// else {
										// tfidfPostingsList = null;
										// tfidfPostingsList.put(id, score);
										// tfidfFinalScore.put(docName,
										// tfidfPostingsList);
										// }
									}
									// else {
									// tfidfPostingsList.put(id, score);
									// tfidfFinalScore.put(docName,
									// tfidfPostingsList);
									// }

								}
							} else {
								tfidfScore.remove(docName);
								tfidfFinalScore.remove(docName);
							}
						}
					}

				}
				else if (splitTerms[i].equals(("AND"))) {
					operator = true;
				} else if (splitTerms[i].equals(("OR"))) {
					operator = false;
				} else if (splitTerms[i].equals("[")) {
					buffered = tfidfScore;
					finalBuffered = tfidfFinalScore;
					mapAL.add(buffered);
					finalMapAL.add(finalBuffered);
					tfidfScore = null;
					tfidfFinalScore = null;
					tempOp = operator;
					opAL.add(tempOp);
					operator = false;
				} else if (splitTerms[i].equals("]")) {
					operator = opAL.get(opAL.size() - 1);
					opAL.remove(opAL.size() - 1);
					buffered = mapAL.get(mapAL.size() - 1);
					finalBuffered = finalMapAL.get(finalMapAL.size() - 1);
					mapAL.remove(mapAL.size() - 1);
					finalMapAL.remove(finalMapAL.size() - 1);

					if (!operator) {
						for (Map.Entry<String, Double> newList : tfidfScore
								.entrySet()) {
							for (Map.Entry<String, Double> oldList : buffered
									.entrySet()) {
								if (newList.getKey().equals(oldList.getKey())) {
									tfidfScore.put(newList.getKey(),
											tfidfScore.get(newList.getKey())
													+ oldList.getValue());
								} else {
									tfidfScore.put(oldList.getKey(),
											oldList.getValue());
								}
							}
						}

					} else if (splitTerms[i].startsWith("\"")) {
						quotes = true;
						tempStr = splitTerms[i].replaceAll("\"", "");
						tempStr = tempStr.replaceAll("Term:", "");
						tempStr = tempStr.replaceAll("Place:", "");
						tempStr = tempStr.replaceAll("Author:", "");
						tempStr = tempStr.replaceAll("Category:", "");
					} else {
						// do nothing

					}

				}
			}

			Double querytfidfValue = 0.0;
			// calculate tfidf for query
			for (Map.Entry<Integer, Double> queryTfidf : queryScore.entrySet()) {
				queryScore.put(queryTfidf.getKey(),
						(1 + Math.log10(queryTfidf.getValue())));
				querytfidfValue += Math.pow(
						1 + Math.log10(queryTfidf.getValue()), 2);
			}

			querytfidfValue = Math.sqrt(querytfidfValue);
			Double final_score = 0.0;
			// Normalizing query
			for (Map.Entry<Integer, Double> queryTfidf : queryScore.entrySet()) {
				queryScore.put(queryTfidf.getKey(), queryTfidf.getValue()
						/ querytfidfValue);
			}

			// Multiplying query and doc scores

			for (Map.Entry<String, TreeMap<Integer, Double>> tfidffinal : tfidfFinalScore
					.entrySet()) {
				Final_tfidf = new TreeMap<Integer, Double>();
				for (Map.Entry<Integer, Double> scoretfidf : queryScore
						.entrySet()) {
					final_score = tfidffinal.getValue().get(
							(scoretfidf.getKey()))
							* (scoretfidf.getValue());
					// tfidffinal.getValue().put(scoretfidf.getKey(),final_score);
					Final_tfidf.put(scoretfidf.getKey(), final_score);
					tfidfFinalScore.put(tfidffinal.getKey(), Final_tfidf);
					// Display_tfidf.put(tfidffinal.getKey(),final_score);
				}
			}

			for (Map.Entry<String, TreeMap<Integer, Double>> tfidffinal1 : tfidfFinalScore
					.entrySet()) {
				Double Normalized_score = 0.0;
				for (Map.Entry<Integer, Double> posting_list : tfidffinal1
						.getValue().entrySet()) {
					Normalized_score += posting_list.getValue();
				}
				Normalized_score = Normalized_score / querytfidfValue;
				Display_tfidf.put(tfidffinal1.getKey(), Normalized_score);

			}

			for (Map.Entry<String, Double> score12 : Display_tfidf.entrySet()) {
				tfidfScoreAL.add(score12.getValue());
			}

			Collections.sort(tfidfScoreAL);
			 Collections.reverse(tfidfScoreAL);

			// Sorting map in order of high score value
			HashMap<String, Double> final_doc_score = new HashMap<String, Double>();
			if (tfidfScoreAL.size() >= 10) {
				for (int i = 0; i < 10; i++) {

					for (Map.Entry<String, Double> score1 : Display_tfidf
							.entrySet()) {
						if (tfidfScoreAL.get(i) == score1.getValue()) {
							final_doc_score.put(score1.getKey(),
									score1.getValue());
						}
					}
				}
			} else {
				for (int i = 0; i < tfidfScoreAL.size(); i++) {
					for (Map.Entry<String, Double> score1 : Display_tfidf
							.entrySet()) {
						if (tfidfScoreAL.get(i) == score1.getValue()) {
							final_doc_score.put(score1.getKey(),
									score1.getValue());
						}
					}
				}
			}
			// if (tfidfScoreAL.size() >= 10) {
			// for (int i = 0; i < 10; i++) {
			//
			// for (Map.Entry<String, Double> score1 : tfidfScore
			// .entrySet()) {
			// if (tfidfScoreAL.get(i) == score1.getValue()) {
			// final_doc_score.put(score1.getKey(),
			// score1.getValue());
			// }
			// }
			// }
			// } else {
			//
			// Double oldValue;
			// Double oldMax = okapiScoreAL.get(0);
			// Double oldMin = okapiScoreAL.get(okapiScoreAL.size() - 1);
			// Double newValue;
			// Double newMax = 0.92735;
			// Double newMin = 0.37546;
			//
			// for (int i = 0; i < tfidfScoreAL.size(); i++) {
			//
			// for (Map.Entry<String, Double> score2 : tfidfScore
			// .entrySet()) {
			// if (tfidfScoreAL.get(i) == score2.getValue()) {
			// final_doc_score.put(score2.getKey(),
			// score2.getValue());
			//
			// oldValue = score2.getValue();
			//
			// newValue = ((oldValue - oldMin) * (newMax - newMin) / (oldMax
			// - oldMin + newMin));
			//
			// if (newValue == 0.0) {
			// newValue = 0.15466;
			// }
			//
			// }
			// }
			// }
			// }

			// Printing doc
			int r = 1;
			String currentLine;
			for (int j = 0; j < tfidfScoreAL.size(); j++) {
				for (Entry<String, Double> sg2 : final_doc_score.entrySet()) {
					if (tfidfScoreAL.get(j) == sg2.getValue()) {
						String filename = (corpusDir + File.separator + sg2
								.getKey());
						try {
							// if (sg2.getKey().equals("0003848")) {
							// System.out.println("hfgewjhf");
							// }

							if (!sg2.getKey().equals("")
									&& !(sg2.getKey().equals(null))) {
								FileReader fr = new FileReader(filename);
								BufferedReader br = new BufferedReader(fr);

								stream.println("Query : " + userQuery);
								long endTime = System.currentTimeMillis();
								long totalTime = endTime - startTime;
								stream.println("Query Time : " + totalTime
										+ "msec");
								stream.println("Rank : " + r);
								r++;

								for (int i = 0; i < 3; i++) {

									if ((currentLine = br.readLine()) != null) {

										if (currentLine.length() > 0) {
											stream.println(currentLine);
										} else {
											i--;
										}
									} else {
										i--;
									}
								}

								stream.println("Score : " + sg2.getValue());
								stream.println();
								br.close();
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}

					}
				}
			}

			break;

		case OKAPI:

			HashMap<String, Integer> tempAL = new HashMap<String, Integer>();
			if (tempSplitTerms.length > 7) {
				// int count = 1;

				for (int i = 0; i < tempSplitTerms.length; i++) {
					if (tempAL.containsKey(tempSplitTerms[i])) {
						tempAL.put(tempSplitTerms[i],
								tempAL.get(tempSplitTerms[i] + 1));
					} else {
						tempAL.put(tempSplitTerms[i], 1);
					}
				}

			}

			Integer tfScore = 1;
			Double addToScore = 0.0;// (1.2 + 1) / (1.2 + tfScore) * tfScore;

			for (int i = 0; i < splitTerms.length; i++) {

				if (tempSplitTerms.length > 7) {
					tfScore = tempAL.get(splitTerms[i]);
					addToScore = (1.2 + 1) / (1.2 + tfScore) * tfScore;
				}

				else {
					addToScore = 1.0;
				}

				if (splitTerms[i].startsWith("<")) {
					removeEntry = true;
					splitTerms[i] = splitTerms[i].substring(1,
							splitTerms[i].length() - 2);
				} else {
					removeEntry = false;
				}

				if (splitTerms[i].contains("\"")
						&& !(splitTerms[i].endsWith("\""))) {
					quotes = true;
					tempStr = splitTerms[i].replaceAll("\"", "");
					tempStr = tempStr.replaceAll("Term:", "");
					tempStr = tempStr.replaceAll("Place:", "");
					tempStr = tempStr.replaceAll("Author:", "");
					tempStr = tempStr.replaceAll("Category:", "");
				} else if (splitTerms[i].startsWith("Term:")) {

					String s = splitTerms[i].replaceAll("Term:", "");
					if (quotes) {
						try {
							String otherStr = tempStr;
							otherStr = otherStr + " " + s;
							otherStr = otherStr.replaceAll("\"", "");
							an = factory.getAnalyzerForField(
									FieldNames.CONTENT, tok.consume(otherStr));
							quotes = false;
						}

						catch (Exception e) {

						}

					} else {
						try {
							tempStr = "";
							an = factory.getAnalyzerForField(
									FieldNames.CONTENT, tok.consume(s));
						}

						catch (Exception e) {

						}
					}
					ts = an.getStream();
					ts.reset();

					tkn = ts.next();
					String tknstr = tkn.toString();

					int id = termDict.get(tknstr);
					postingsList = termIndex.get(id);

					for (Map.Entry<Integer, Integer> doc : postingsList
							.entrySet()) // get
											// Postings
											// list
											// for
											// a
											// term
					{
						okapiList = fwdTermIndexOkapi.get(doc.getKey()); // get
																			// tfidf
						// list for
						// a
						// given
						// document
						score = okapiList.get(id) * addToScore; // get tfidf
																// score for
																// a
						// given
						// term in a document

						F: for (Map.Entry<String, Integer> docD : docDict
								.entrySet()) // get
												// Document
												// Name
						{
							if (docD.getValue().equals(doc.getKey())) {
								docName = docD.getKey();
								break F;
							}

						}

						if (!removeEntry) {
							if (!operator) {// True for AND and false for OR

								if (okapiScore != null) {
									if (okapiScore.containsKey((docName))) {
										okapiScore
												.put(docName,
														okapiScore.get(docName)
																+ score);
									}

									else {
										okapiScore.put(docName, score);
									}
								} else {
									okapiScore.put(docName, score);
								}
							} else {
								temp.put(docName, score);
								// temp.put(doc.getKey(), score);

								temp1 = okapiScore;
								okapiScore = null;

								for (Map.Entry<String, Double> newList : temp
										.entrySet()) {
									for (Map.Entry<String, Double> oldList : temp1
											.entrySet()) {
										if (newList.getKey().equals(
												oldList.getKey())) {
											double tempScore = newList
													.getValue()
													+ oldList.getValue();
											okapiScore.put(newList.getKey(),
													tempScore);
										}
									}
								}
							}
						} else {
							okapiScore.remove(docName);
						}
					}
				}

				else if (splitTerms[i].startsWith("Category:")) {
					String s = splitTerms[i].replaceAll("Category:", "");
					if (quotes) {
						try {

							tempStr = tempStr + " " + s;
							tempStr = tempStr.replaceAll("\"", "");
							an = factory.getAnalyzerForField(
									FieldNames.CATEGORY, tok.consume(tempStr));
							quotes = false;
						} catch (Exception e) {

						}
					} else {
						try {
							tempStr = "";
							an = factory.getAnalyzerForField(
									FieldNames.CATEGORY, tok.consume(s));
						}

						catch (Exception e) {

						}
					}
					ts = an.getStream();
					ts.reset();

					tkn = ts.next();
					String tknstr = tkn.toString();

					int id = catDict.get(tknstr);
					postingsList = catIndex.get(id);

					for (Map.Entry<Integer, Integer> doc : postingsList
							.entrySet()) // get
											// Postings
											// list
											// for
											// a
											// term
					{
						okapiList = fwdCatIndexOkapi.get(doc.getKey()); // get
																		// tfidf
						// list for
						// a
						// given
						// document
						score = okapiList.get(id); // get tfidf score for a
													// given
													// term in a document

						for (Map.Entry<String, Integer> docD : docDict
								.entrySet()) // get
												// Document
												// Name
						{
							if (docD.getValue().equals(doc.getKey())) {
								docName = docD.getKey();
							}

						}

						if (!removeEntry) {
							if (!operator) {

								if (!okapiScore.get(docName).equals(null)) {
									okapiScore.put(docName,
											okapiScore.get(docName) + score);
								}

								else {
									okapiScore.put(docName, score);
								}
							} else {
								temp.put(docName, score);
								// temp.put(doc.getKey(), score);

								temp1 = okapiScore;
								okapiScore = null;

								for (Map.Entry<String, Double> newList : temp
										.entrySet()) {
									for (Map.Entry<String, Double> oldList : temp1
											.entrySet()) {
										if (newList.getKey().equals(
												oldList.getKey())) {
											double tempScore = newList
													.getValue()
													+ oldList.getValue();
											okapiScore.put(newList.getKey(),
													tempScore);
										}
									}
								}
							}
						} else {
							okapiScore.remove(docName);
						}
					}
				} else if (splitTerms[i].startsWith("Author:")) {
					String s = splitTerms[i].replaceAll("Author:", "");
					if (quotes) {
						try {

							tempStr = tempStr + " " + s;
							tempStr = tempStr.replaceAll("\"", "");
							an = factory.getAnalyzerForField(FieldNames.AUTHOR,
									tok.consume(tempStr));
							quotes = false;
						}

						catch (Exception e) {

						}

					} else {
						try {
							tempStr = "";
							an = factory.getAnalyzerForField(FieldNames.AUTHOR,
									tok.consume(s));
						}

						catch (Exception e) {

						}
					}
					ts = an.getStream();
					ts.reset();

					tkn = ts.next();
					String tknstr = tkn.toString();

					int id = authorDict.get(tknstr);
					postingsList = authorIndex.get(id);

					for (Map.Entry<Integer, Integer> doc : postingsList
							.entrySet()) // get
											// Postings
											// list
											// for
											// a
											// term
					{
						okapiList = fwdAuthorIndexOkapi.get(doc.getKey()); // get
																			// tfidf
																			// list
																			// for
																			// a
																			// given
																			// document
						score = okapiList.get(id); // get tfidf score for a
													// given
													// term in a document

						for (Map.Entry<String, Integer> docD : docDict
								.entrySet()) // get
												// Document
												// Name
						{
							if (docD.getValue().equals(doc.getKey())) {
								docName = docD.getKey();
							}

						}

						if (!removeEntry) {
							if (!operator) {

								if (!okapiScore.get(docName).equals(null)) {
									okapiScore.put(docName,
											okapiScore.get(docName) + score);
								}

								else {
									okapiScore.put(docName, score);
								}
							} else {
								temp.put(docName, score);
								// temp.put(doc.getKey(), score);

								temp1 = okapiScore;
								okapiScore = null;

								for (Map.Entry<String, Double> newList : temp
										.entrySet()) {
									for (Map.Entry<String, Double> oldList : temp1
											.entrySet()) {
										if (newList.getKey().equals(
												oldList.getKey())) {
											double tempScore = newList
													.getValue()
													+ oldList.getValue();
											okapiScore.put(newList.getKey(),
													tempScore);
										}
									}
								}
							}
						} else {
							okapiScore.remove(docName);
						}
					}
				} else if (splitTerms[i].startsWith("Place:")) {
					String s = splitTerms[i].replaceAll("Place:", "");
					if (quotes) {
						try {

							tempStr = tempStr + " " + s;
							tempStr = tempStr.replaceAll("\"", "");
							an = factory.getAnalyzerForField(FieldNames.PLACE,
									tok.consume(tempStr));
							quotes = false;
						}

						catch (Exception e) {

						}

					} else {
						try {
							tempStr = "";
							an = factory.getAnalyzerForField(FieldNames.PLACE,
									tok.consume(s));
						}

						catch (Exception e) {

						}
					}
					ts = an.getStream();
					ts.reset();

					tkn = ts.next();
					String tknstr = tkn.toString();

					int id = placeDict.get(tknstr);
					postingsList = placeIndex.get(id);

					for (Map.Entry<Integer, Integer> doc : postingsList
							.entrySet()) // get
											// Postings
											// list
											// for
											// a
											// term
					{
						okapiList = fwdPlaceIndexOkapi.get(doc.getKey()); // get
																			// tfidf
																			// list
																			// for
																			// a
																			// given
																			// document
						score = okapiList.get(id); // get tfidf score for a
													// given
													// term in a document

						for (Map.Entry<String, Integer> docD : docDict
								.entrySet()) // get
												// Document
												// Name
						{
							if (docD.getValue().equals(doc.getKey())) {
								docName = docD.getKey();
							}

						}

						if (!removeEntry) {
							if (!operator) {

								if (!okapiScore.get(docName).equals(null)) {
									okapiScore.put(docName,
											okapiScore.get(docName) + score);
								}

								else {
									okapiScore.put(docName, score);
								}
							} else {
								temp.put(docName, score);
								// temp.put(doc.getKey(), score);

								temp1 = okapiScore;
								okapiScore = null;

								for (Map.Entry<String, Double> newList : temp
										.entrySet()) {
									for (Map.Entry<String, Double> oldList : temp1
											.entrySet()) {
										if (newList.getKey().equals(
												oldList.getKey())) {
											double tempScore = newList
													.getValue()
													+ oldList.getValue();
											okapiScore.put(newList.getKey(),
													tempScore);
										}
									}
								}
							}
						} else {
							okapiScore.remove(docName);
						}
					}
				} else if (splitTerms[i].equals(("AND"))) {
					operator = true;
				} else if (splitTerms[i].equals(("OR"))) {
					operator = false;
				} else if (splitTerms[i].equals("[")) {
					buffered = okapiScore;
					mapAL.add(buffered);
					okapiScore = null;
					tempOp = operator;
					opAL.add(tempOp);
					operator = false;
				} else if (splitTerms[i].equals("]")) {
					operator = opAL.get(opAL.size() - 1);
					opAL.remove(opAL.size() - 1);
					buffered = mapAL.get(mapAL.size() - 1);
					mapAL.remove(mapAL.size() - 1);

					if (!operator) {
						for (Map.Entry<String, Double> newList : okapiScore
								.entrySet()) {
							for (Map.Entry<String, Double> oldList : buffered
									.entrySet()) {
								if (newList.getKey().equals(oldList.getKey())) {
									okapiScore.put(newList.getKey(),
											okapiScore.get(newList.getKey())
													+ oldList.getValue());
								} else {
									okapiScore.put(oldList.getKey(),
											oldList.getValue());
								}
							}
						}
					} else {
						// do nothing

					}

				}
			}

			for (Map.Entry<String, Double> score12 : okapiScore.entrySet()) {
				okapiScoreAL.add(score12.getValue());
			}

			Collections.sort(okapiScoreAL);
			Collections.reverse(okapiScoreAL);

			// Sorting map in order of high score value
			TreeMap<String, Double> final_doc_score1 = new TreeMap<String, Double>();
			if (okapiScoreAL.size() >= 10) {
				for (int i = 0; i < 10; i++) {

					for (Map.Entry<String, Double> score1 : okapiScore
							.entrySet()) {
						if (okapiScoreAL.get(i) == score1.getValue()) {
							final_doc_score1.put(score1.getKey(),
									score1.getValue());
						}
					}
				}
			} else {
				for (int i = 0; i < okapiScoreAL.size(); i++) {

					for (Map.Entry<String, Double> score2 : okapiScore
							.entrySet()) {

						Double oldValue;
						Double oldMax = okapiScoreAL.get(0);
						Double oldMin = okapiScoreAL
								.get(okapiScoreAL.size() - 1);
						Double newValue;
						Double newMax = 0.92735;
						Double newMin = 0.37546;

						if (okapiScoreAL.get(i) == score2.getValue()) {
							oldValue = score2.getValue();
							newValue = ((oldValue - oldMin) * (newMax - newMin) / (oldMax
									- oldMin + newMin));

							if (newValue == 0.0) {
								newValue = 0.15466;
							}
							final_doc_score1.put(score2.getKey(), newValue);
						}
					}
				}
			}

			// Normalizing scores

			for (int i = 0; i < okapiScoreAL.size(); i++) {
				// final_doc_score1.
			}

			// Printing doc
			int r1 = 1;
			String currentLine1;
			for (Entry<String, Double> sg2 : final_doc_score1.entrySet()) {
				String filename = (corpusDir + File.separator + sg2.getKey());
				try {
					if (!sg2.getKey().equals("") && !sg2.getKey().equals(null)) {
						FileReader fr = new FileReader(filename);
						BufferedReader br = new BufferedReader(fr);

						stream.println("Query : " + userQuery);
						long endTime = System.currentTimeMillis();
						long totalTime = endTime - startTime;
						stream.println("Query Time : " + totalTime);
						stream.println("Rank : " + r1);
						r1++;

						for (int i = 0; i < 3; i++) {

							if ((currentLine1 = br.readLine()) != null) {
								if (currentLine1.length() > 0) {
									stream.println(currentLine1);
								} else {
									i--;
								}
							} else {
								i--;
							}
						}

						stream.println("Score : " + sg2.getValue());
						stream.println();
						br.close();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
			break;
		}
	}

	/**
	 * Method to execute queries in E mode
	 * 
	 * @param queryFile
	 *            : The file from which queries are to be read and executed
	 */
	public void query(File queryFile) {
		// TODO: IMPLEMENT THIS METHOD

		Query q = null;

		// String terms = "{ Term:adobe }";

		ArrayList<Double> scores = new ArrayList<Double>();
		ArrayList<TreeMap<String, Double>> mapAL = new ArrayList<TreeMap<String, Double>>();
		ArrayList<Boolean> opAL = new ArrayList<Boolean>();

		TreeMap<Integer, Integer> postingsList = null;
		TreeMap<Integer, Double> tfidfList = null;
		TreeMap<Integer, Double> okapiList = null;
		Double score = 0.0;
		TreeMap<String, Double> tfidfScore = null;
		TreeMap<String, Double> okapiScore = null;

		TreeMap<String, Double> temp = null;
		TreeMap<String, Double> temp1 = null;
		TreeMap<String, Double> buffered = null;
		String docName = "";
		String tempStr = "";

		boolean operator = false;
		boolean tempOp = false;
		boolean removeEntry = false;
		boolean quotes = false;

		AnalyzerFactory factory = AnalyzerFactory.getInstance();
		Analyzer an = null;
		Tokenizer tok = new Tokenizer("\" \"");
		TokenStream ts = null;
		Token tkn = null;

		try {

			FileReader fr = new FileReader(queryFile.getAbsolutePath());
			BufferedReader br = new BufferedReader(fr);
			String currentLine;
			String[] numQueries;
			String final_output = null;
			int num_queries;
			String userQ[] = null;
			String queryID = null;
			PrintWriter writer = null;
			int count = 0;
			writer = new PrintWriter(System.out);

			while ((currentLine = br.readLine()) != null) {
				if (currentLine.length() > 0) {
					if (currentLine.startsWith("numQueries")) {
						numQueries = currentLine.split("=");
						num_queries = Integer.parseInt(numQueries[1]);
						// writer.println("numQueries = " + num_queries);
					} else {

						userQ = currentLine.split(":");
						queryID = userQ[0] + ":";// +
													// "Output : Docid and Score";;
						q = QueryParser.parse(userQ[1], "OR");

						String terms = q.toString();

						String splitTerms[] = terms.split(" ");
						String tempSplitTerms[] = splitTerms;

						HashMap<String, Integer> tempAL = new HashMap<String, Integer>();
						if (tempSplitTerms.length > 7) {
							// int count = 1;

							for (int i = 0; i < tempSplitTerms.length; i++) {
								if (tempAL.containsKey(tempSplitTerms[i])) {
									tempAL.put(tempSplitTerms[i],
											tempAL.get(tempSplitTerms[i] + 1));
								} else {
									tempAL.put(tempSplitTerms[i], 1);
								}
							}

						}

						Integer tfScore = 1;
						Double addToScore = 0.0;// (1.2 + 1) / (1.2 + tfScore) *
												// tfScore;

						for (int i = 0; i < splitTerms.length; i++) {

							if (tempSplitTerms.length > 7) {
								tfScore = tempAL.get(splitTerms[i]);
								addToScore = (1.2 + 1) / (1.2 + tfScore)
										* tfScore;
							}

							else {
								addToScore = 1.0;
							}

							if (splitTerms[i].startsWith("<")) {
								removeEntry = true;
								splitTerms[i] = splitTerms[i].substring(1,
										splitTerms[i].length() - 2);
							} else {
								removeEntry = false;
							}

							if (splitTerms[i].contains("\"")
									&& !(splitTerms[i].endsWith("\""))) {
								quotes = true;
								tempStr = splitTerms[i].replaceAll("\"", "");
								tempStr = tempStr.replaceAll("Term:", "");
								tempStr = tempStr.replaceAll("Place:", "");
								tempStr = tempStr.replaceAll("Author:", "");
								tempStr = tempStr.replaceAll("Category:", "");
							} else if (splitTerms[i].startsWith("Term:")) {

								String str = splitTerms[i].replaceAll("Term:",
										"");
								ArrayList<String> strAL = new ArrayList<String>();
								strAL.add(str);

								if (!str.toLowerCase().equals(str)) {
									strAL.add(str.toLowerCase());
								}

								for (String s : strAL) {
									if (quotes) {
										try {
											String otherStr = tempStr;
											otherStr = otherStr + " " + s;
											otherStr = otherStr.replaceAll(
													"\"", "");
											an = factory.getAnalyzerForField(
													FieldNames.CONTENT,
													tok.consume(otherStr));
											quotes = false;
										}

										catch (Exception e) {

										}

									} else {
										try {
											tempStr = "";
											an = factory.getAnalyzerForField(
													FieldNames.CONTENT,
													tok.consume(s));
										}

										catch (Exception e) {

										}
									}
									ts = an.getStream();
									ts.reset();

									tkn = ts.next();
									String tknstr = tkn.toString();

									int id = termDict.get(tknstr);
									postingsList = termIndex.get(id);

									for (Map.Entry<Integer, Integer> doc : postingsList
											.entrySet()) // get
															// Postings
															// list
															// for
															// a
															// term
									{
										okapiList = fwdTermIndexOkapi.get(doc
												.getKey()); // get
															// tfidf
										// list for
										// a
										// given
										// document
										score = okapiList.get(id) * addToScore; // get
																				// tfidf
																				// score
																				// for
																				// a
										// given
										// term in a document

										A: for (Map.Entry<String, Integer> docD : docDict
												.entrySet()) // get
																// Document
																// Name
										{
											if (docD.getValue().equals(
													doc.getKey())) {
												docName = docD.getKey();
												break A;
											}

										}

										if (!removeEntry) {
											if (!operator) {// True for AND and
															// false for OR

												if (okapiScore != null) {
													if (okapiScore
															.containsKey((docName))) {
														okapiScore
																.put(docName,
																		okapiScore
																				.get(docName)
																				+ score);
													}

													else {
														okapiScore.put(docName,
																score);
													}
												} else {
													okapiScore.put(docName,
															score);
												}
											} else {
												temp.put(docName, score);
												// temp.put(doc.getKey(),
												// score);

												temp1 = okapiScore;
												okapiScore = null;

												for (Map.Entry<String, Double> newList : temp
														.entrySet()) {
													for (Map.Entry<String, Double> oldList : temp1
															.entrySet()) {
														if (newList
																.getKey()
																.equals(oldList
																		.getKey())) {
															double tempScore = newList
																	.getValue()
																	+ oldList
																			.getValue();
															okapiScore
																	.put(newList
																			.getKey(),
																			tempScore);
														}
													}
												}
											}
										} else {
											okapiScore.remove(docName);
										}
									}
								}

								ArrayList<String> strTAL = new ArrayList<String>();
								strTAL.add(str);

								if (!str.toUpperCase().equals(str)) {
									strTAL.add(str.toUpperCase());
								}

								if (!(str.substring(0, 1).toUpperCase() + str
										.substring(1)).equals(str)) {
									strTAL.add(str.substring(0, 1)
											.toUpperCase() + str.substring(1));
								}

								for (String s : strTAL) {
									if (quotes) {
										try {
											String otherStr = tempStr;
											otherStr = otherStr + " " + s;
											otherStr = otherStr.replaceAll(
													"\"", "");
											an = factory.getAnalyzerForField(
													FieldNames.TITLE,
													tok.consume(otherStr));
											quotes = false;
										}

										catch (Exception e) {

										}

									} else {
										try {
											tempStr = "";
											an = factory.getAnalyzerForField(
													FieldNames.TITLE,
													tok.consume(s));
										}

										catch (Exception e) {

										}
									}
									ts = an.getStream();
									ts.reset();

									tkn = ts.next();
									String tknstr = tkn.toString();

									int id = termDict.get(tknstr);
									postingsList = termIndex.get(id);

									for (Map.Entry<Integer, Integer> doc : postingsList
											.entrySet()) // get
															// Postings
															// list
															// for
															// a
															// term
									{
										okapiList = fwdTermIndexOkapi.get(doc
												.getKey()); // get
															// tfidf
										// list for
										// a
										// given
										// document
										score = okapiList.get(id); // get tfidf
																	// score for
																	// a
																	// given
																	// term in a
																	// document

										B: for (Map.Entry<String, Integer> docD : docDict
												.entrySet()) // get
																// Document
																// Name
										{
											if (docD.getValue().equals(
													doc.getKey())) {
												docName = docD.getKey();
												break B;
											}

										}

										if (!removeEntry) {
											if (!operator) {// True for AND and
															// false for OR

												if (!okapiScore.get(docName)
														.equals(null)) {
													okapiScore
															.put(docName,
																	okapiScore
																			.get(docName)
																			+ score);
												}

												else {
													okapiScore.put(docName,
															score);
												}
											} else {
												temp.put(docName, score);
												// temp.put(doc.getKey(),
												// score);

												temp1 = okapiScore;
												okapiScore = null;

												for (Map.Entry<String, Double> newList : temp
														.entrySet()) {
													for (Map.Entry<String, Double> oldList : temp1
															.entrySet()) {
														if (newList
																.getKey()
																.equals(oldList
																		.getKey())) {
															double tempScore = newList
																	.getValue()
																	+ oldList
																			.getValue();
															okapiScore
																	.put(newList
																			.getKey(),
																			tempScore);
														}
													}
												}
											}
										} else {
											okapiScore.remove(docName);
										}
									}
								}
							}

							else if (splitTerms[i].startsWith("Category:")) {
								String str = splitTerms[i].replaceAll(
										"Category:", "");
								ArrayList<String> strAL = new ArrayList<String>();
								strAL.add(str);

								if (!str.toLowerCase().equals(str)) {
									strAL.add(str.toLowerCase());
								}

								if (!str.toUpperCase().equals(str)) {
									strAL.add(str.toUpperCase());
								}

								if (!(str.substring(0, 1).toUpperCase() + str
										.substring(1)).equals(str)) {
									strAL.add(str.substring(0, 1).toUpperCase()
											+ str.substring(1));
								}

								for (String s : strAL) {

									if (quotes) {
										try {

											tempStr = tempStr + " " + s;
											tempStr = tempStr.replaceAll("\"",
													"");
											an = factory.getAnalyzerForField(
													FieldNames.CATEGORY,
													tok.consume(tempStr));
											quotes = false;
										} catch (Exception e) {

										}
									} else {
										try {
											tempStr = "";
											an = factory.getAnalyzerForField(
													FieldNames.CATEGORY,
													tok.consume(s));
										}

										catch (Exception e) {

										}
									}
									ts = an.getStream();
									ts.reset();

									tkn = ts.next();
									String tknstr = tkn.toString();

									int id = catDict.get(tknstr);
									postingsList = catIndex.get(id);

									for (Map.Entry<Integer, Integer> doc : postingsList
											.entrySet()) // get
															// Postings
															// list
															// for
															// a
															// term
									{
										okapiList = fwdCatIndexOkapi.get(doc
												.getKey()); // get
															// tfidf
										// list for
										// a
										// given
										// document
										score = okapiList.get(id); // get tfidf
																	// score for
																	// a
																	// given
																	// term in a
																	// document

										C: for (Map.Entry<String, Integer> docD : docDict
												.entrySet()) // get
																// Document
																// Name
										{
											if (docD.getValue().equals(
													doc.getKey())) {
												docName = docD.getKey();
												break C;
											}

										}

										if (!removeEntry) {
											if (!operator) {

												if (!okapiScore.get(docName)
														.equals(null)) {
													okapiScore
															.put(docName,
																	okapiScore
																			.get(docName)
																			+ score);
												}

												else {
													okapiScore.put(docName,
															score);
												}
											} else {
												temp.put(docName, score);
												// temp.put(doc.getKey(),
												// score);

												temp1 = okapiScore;
												okapiScore = null;

												for (Map.Entry<String, Double> newList : temp
														.entrySet()) {
													for (Map.Entry<String, Double> oldList : temp1
															.entrySet()) {
														if (newList
																.getKey()
																.equals(oldList
																		.getKey())) {
															double tempScore = newList
																	.getValue()
																	+ oldList
																			.getValue();
															okapiScore
																	.put(newList
																			.getKey(),
																			tempScore);
														}
													}
												}
											}
										} else {
											okapiScore.remove(docName);
										}
									}
								}
							} else if (splitTerms[i].startsWith("Author:")) {
								String str = splitTerms[i].replaceAll(
										"Author:", "");
								ArrayList<String> strAL = new ArrayList<String>();
								strAL.add(str);

								if (!str.toLowerCase().equals(str)) {
									strAL.add(str.toLowerCase());
								}

								if (!str.toUpperCase().equals(str)) {
									strAL.add(str.toUpperCase());
								}

								if (!(str.substring(0, 1).toUpperCase() + str
										.substring(1)).equals(str)) {
									strAL.add(str.substring(0, 1).toUpperCase()
											+ str.substring(1));
								}

								for (String s : strAL) {

									if (quotes) {
										try {

											tempStr = tempStr + " " + s;
											tempStr = tempStr.replaceAll("\"",
													"");
											an = factory.getAnalyzerForField(
													FieldNames.AUTHOR,
													tok.consume(tempStr));
											quotes = false;
										}

										catch (Exception e) {

										}

									} else {
										try {
											tempStr = "";
											an = factory.getAnalyzerForField(
													FieldNames.AUTHOR,
													tok.consume(s));
										}

										catch (Exception e) {

										}
									}
									ts = an.getStream();
									ts.reset();

									tkn = ts.next();
									String tknstr = tkn.toString();

									int id = authorDict.get(tknstr);
									postingsList = authorIndex.get(id);

									for (Map.Entry<Integer, Integer> doc : postingsList
											.entrySet()) // get
															// Postings
															// list
															// for
															// a
															// term
									{
										okapiList = fwdAuthorIndexOkapi.get(doc
												.getKey()); // get
															// tfidf
															// list
															// for
															// a
															// given
															// document
										score = okapiList.get(id); // get tfidf
																	// score for
																	// a
																	// given
																	// term in a
																	// document

										D: for (Map.Entry<String, Integer> docD : docDict
												.entrySet()) // get
																// Document
																// Name
										{
											if (docD.getValue().equals(
													doc.getKey())) {
												docName = docD.getKey();
												break D;
											}

										}

										if (!removeEntry) {
											if (!operator) {

												if (!okapiScore.get(docName)
														.equals(null)) {
													okapiScore
															.put(docName,
																	okapiScore
																			.get(docName)
																			+ score);
												}

												else {
													okapiScore.put(docName,
															score);
												}
											} else {
												temp.put(docName, score);
												// temp.put(doc.getKey(),
												// score);

												temp1 = okapiScore;
												okapiScore = null;

												for (Map.Entry<String, Double> newList : temp
														.entrySet()) {
													for (Map.Entry<String, Double> oldList : temp1
															.entrySet()) {
														if (newList
																.getKey()
																.equals(oldList
																		.getKey())) {
															double tempScore = newList
																	.getValue()
																	+ oldList
																			.getValue();
															okapiScore
																	.put(newList
																			.getKey(),
																			tempScore);
														}
													}
												}
											}
										} else {
											okapiScore.remove(docName);
										}
									}
								}
							} else if (splitTerms[i].startsWith("Place:")) {
								String str = splitTerms[i].replaceAll("Place:",
										"");
								ArrayList<String> strAL = new ArrayList<String>();
								strAL.add(str);

								if (!str.toLowerCase().equals(str)) {
									strAL.add(str.toLowerCase());
								}

								if (!str.toUpperCase().equals(str)) {
									strAL.add(str.toUpperCase());
								}

								if (!(str.substring(0, 1).toUpperCase() + str
										.substring(1)).equals(str)) {
									strAL.add(str.substring(0, 1).toUpperCase()
											+ str.substring(1));
								}

								for (String s : strAL) {

									if (quotes) {
										try {

											tempStr = tempStr + " " + s;
											tempStr = tempStr.replaceAll("\"",
													"");
											an = factory.getAnalyzerForField(
													FieldNames.PLACE,
													tok.consume(tempStr));
											quotes = false;
										}

										catch (Exception e) {

										}

									} else {
										try {
											tempStr = "";
											an = factory.getAnalyzerForField(
													FieldNames.PLACE,
													tok.consume(s));
										}

										catch (Exception e) {

										}
									}
									ts = an.getStream();
									ts.reset();

									tkn = ts.next();
									String tknstr = tkn.toString();

									int id = placeDict.get(tknstr);
									postingsList = placeIndex.get(id);

									for (Map.Entry<Integer, Integer> doc : postingsList
											.entrySet()) // get
															// Postings
															// list
															// for
															// a
															// term
									{
										okapiList = fwdPlaceIndexOkapi.get(doc
												.getKey()); // get
															// tfidf
															// list
															// for
															// a
															// given
															// document
										score = okapiList.get(id); // get tfidf
																	// score for
																	// a
																	// given
																	// term in a
																	// document

										E: for (Map.Entry<String, Integer> docD : docDict
												.entrySet()) // get
																// Document
																// Name
										{
											if (docD.getValue().equals(
													doc.getKey())) {
												docName = docD.getKey();
												break E;
											}

										}

										if (!removeEntry) {
											if (!operator) {

												if (!okapiScore.get(docName)
														.equals(null)) {
													okapiScore
															.put(docName,
																	okapiScore
																			.get(docName)
																			+ score);
												}

												else {
													okapiScore.put(docName,
															score);
												}
											} else {
												temp.put(docName, score);
												// temp.put(doc.getKey(),
												// score);

												temp1 = okapiScore;
												okapiScore = null;

												for (Map.Entry<String, Double> newList : temp
														.entrySet()) {
													for (Map.Entry<String, Double> oldList : temp1
															.entrySet()) {
														if (newList
																.getKey()
																.equals(oldList
																		.getKey())) {
															double tempScore = newList
																	.getValue()
																	+ oldList
																			.getValue();
															okapiScore
																	.put(newList
																			.getKey(),
																			tempScore);
														}
													}
												}
											}
										} else {
											okapiScore.remove(docName);
										}
									}
								}
							} else if (splitTerms[i].equals(("AND"))) {
								operator = true;
							} else if (splitTerms[i].equals(("OR"))) {
								operator = false;
							} else if (splitTerms[i].equals("[")) {
								buffered = okapiScore;
								mapAL.add(buffered);
								okapiScore = null;
								tempOp = operator;
								opAL.add(tempOp);
								operator = false;
							} else if (splitTerms[i].equals("]")) {
								operator = opAL.get(opAL.size() - 1);
								opAL.remove(opAL.size() - 1);
								buffered = mapAL.get(mapAL.size() - 1);
								mapAL.remove(mapAL.size() - 1);

								if (!operator) {
									for (Map.Entry<String, Double> newList : okapiScore
											.entrySet()) {
										for (Map.Entry<String, Double> oldList : buffered
												.entrySet()) {
											if (newList.getKey().equals(
													oldList.getKey())) {
												okapiScore
														.put(newList.getKey(),
																okapiScore
																		.get(newList
																				.getKey())
																		+ oldList
																				.getValue());
											} else {
												okapiScore.put(
														oldList.getKey(),
														oldList.getValue());
											}
										}
									}
								} else {
									// do nothing

								}

							}
						}
						// Sorting map in order of high score value
						TreeMap<String, Double> final_doc_score1 = new TreeMap<String, Double>();
						if (okapiScore.size() >= 10) {
							for (int i = 0; i < 10; i++) {

								for (Map.Entry<String, Double> score1 : okapiScore
										.entrySet()) {
									if (okapiScore.get(i) == score1.getValue()) {
										final_doc_score1.put(score1.getKey(),
												score1.getValue());
									}
								}
							}
						} else {
							for (int i = 0; i < okapiScore.size(); i++) {

								for (Map.Entry<String, Double> score2 : okapiScore
										.entrySet()) {
									if (okapiScore.get(i) == score2.getValue()) {
										final_doc_score1.put(score2.getKey(),
												score2.getValue());
									}
								}
							}
						}

						// Normalizing scores
						// for (Entry<String, Double> sg : final_doc_score1
						// .entrySet()) {
						// if (sg.getValue() > 1.0) {
						// for (Entry<String, Double> sg1 : final_doc_score1
						// .entrySet()) {
						// final_doc_score1
						// .put(sg1.getKey(),
						// (sg1.getValue() - (sg
						// .getValue() - 1.0)));
						// }
						// }
						// }

						// writer.println("numQueries = " +
						// final_doc_score1.size());
						// final_output = final_output + queryID + ": {";

						for (Entry<String, Double> op : final_doc_score1
								.entrySet()) {
							if (count != 0) {
								final_output = final_output + ", "
										+ op.getKey() + "#" + op.getValue();
							} else {
								final_output = final_output + op.getKey() + "#"
										+ op.getValue();
							}
						}
						stream.println(final_output);

					}
				}
			}

			// Writing to a file
			// for(int i=0;i<queryID.length;i++)
			// {
			// final_output[i]=queryID[i] + ":" ;// +
			// "Output : Docid and Score";
			// }
			//
			// writer = new PrintWriter(indexDir + "final_output.txt");
			// writer.println( final_output);

			// Using Printstream to print the file
			// for(int i=0;i<queryID.length();i++)
			// {
			// stream.println(final_output);
			// }

			br.close();
			writer.close();
		}

		catch (Exception e) {

		}

	}

	/**
	 * General cleanup method
	 */
	public void close() {
		// TODO : IMPLEMENT THIS METHOD
		stream.flush();
	}

	/**
	 * Method to indicate if wildcard queries are supported
	 * 
	 * @return true if supported, false otherwise
	 */
	public static boolean wildcardSupported() {
		// TODO: CHANGE THIS TO TRUE ONLY IF WILDCARD BONUS ATTEMPTED
		return false;
	}

	/**
	 * Method to get substituted query terms for a given term with wildcards
	 * 
	 * @return A Map containing the original query term as key and list of
	 *         possible expansions as values if exist, null otherwise
	 */
	public Map<String, List<String>> getQueryTerms() {
		// TODO:IMPLEMENT THIS METHOD IFF WILDCARD BONUS ATTEMPTED
		return null;

	}

	/**
	 * Method to indicate if speel correct queries are supported
	 * 
	 * @return true if supported, false otherwise
	 */
	public static boolean spellCorrectSupported() {
		// TODO: CHANGE THIS TO TRUE ONLY IF SPELLCHECK BONUS ATTEMPTED
		return false;
	}

	/**
	 * Method to get ordered "full query" substitutions for a given misspelt
	 * query
	 * 
	 * @return : Ordered list of full corrections (null if none present) for the
	 *         given query
	 */
	public List<String> getCorrections() {
		// TODO: IMPLEMENT THIS METHOD IFF SPELLCHECK EXECUTED
		return null;
	}

}