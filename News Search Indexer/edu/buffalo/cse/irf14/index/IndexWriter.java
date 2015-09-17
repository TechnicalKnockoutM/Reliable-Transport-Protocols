/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.analysis.*;

/**
 * @author nikhillo Class responsible for writing indexes to disk
 */
public class IndexWriter {
	/**
	 * Default constructor
	 * 
	 * @param indexDir
	 *            : The root directory to be sued for indexing
	 */
	private String indexDir;
	int sum_doc_len = 0;
	
	private TreeMap<Integer, Integer> docLength = new TreeMap<Integer, Integer>();
	private TreeMap<String, Double> AvgDocLength = new TreeMap<String, Double>();

	private TreeMap<Integer, Integer> termIndexScore = new TreeMap<Integer, Integer>();
	private Map<Integer, Double> termIndexNormalizedScore = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> termWithScore = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> termWithScore1 = new TreeMap<Integer, Double>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexTerm = new TreeMap<Integer, TreeMap<Integer, Double>>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexTerm1 = new TreeMap<Integer, TreeMap<Integer, Double>>();

	private TreeMap<Integer, Integer> AuthorIndexScore = new TreeMap<Integer, Integer>();
	private Map<Integer, Double> AuthorIndexNormalizedScore = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> AuthorWithScore = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> AuthorWithScore1 = new TreeMap<Integer, Double>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexAuthor = new TreeMap<Integer, TreeMap<Integer, Double>>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexAuthor1 = new TreeMap<Integer, TreeMap<Integer, Double>>();

	private TreeMap<Integer, Integer> CategoryIndexScore = new TreeMap<Integer, Integer>();
	private Map<Integer, Double> CategoryIndexNormalizedScore = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> CategoryWithScore = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> CategoryWithScore1 = new TreeMap<Integer, Double>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexCategory = new TreeMap<Integer, TreeMap<Integer, Double>>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexCategory1 = new TreeMap<Integer, TreeMap<Integer, Double>>();

	private TreeMap<Integer, Integer> PlaceIndexScore = new TreeMap<Integer, Integer>();
	private Map<Integer, Double> PlaceIndexNormalizedScore = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> PlaceWithScore = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> PlaceWithScore1 = new TreeMap<Integer, Double>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexPlace = new TreeMap<Integer, TreeMap<Integer, Double>>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexPlace1 = new TreeMap<Integer, TreeMap<Integer, Double>>();

	private TreeMap<String, Integer> docDict = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> termDict = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> authorDict = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> catDict = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> placeDict = new TreeMap<String, Integer>();

	private TreeMap<Integer, Integer> termIndexScoreokapi = new TreeMap<Integer, Integer>();
	private Map<Integer, Double> termIndexNormalizedScoreokapi = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> termWithScoreokapi = new TreeMap<Integer, Double>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexTermokapi = new TreeMap<Integer, TreeMap<Integer, Double>>();

	private TreeMap<Integer, Integer> AuthorIndexScoreokapi = new TreeMap<Integer, Integer>();
	private Map<Integer, Double> AuthorIndexNormalizedScoreokapi = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> AuthorWithScoreokapi = new TreeMap<Integer, Double>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexAuthorokapi = new TreeMap<Integer, TreeMap<Integer, Double>>();

	private TreeMap<Integer, Integer> CategoryIndexScoreokapi = new TreeMap<Integer, Integer>();
	private Map<Integer, Double> CategoryIndexNormalizedScoreokapi = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> CategoryWithScoreokapi = new TreeMap<Integer, Double>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexCategoryokapi = new TreeMap<Integer, TreeMap<Integer, Double>>();

	private TreeMap<Integer, Integer> PlaceIndexScoreokapi = new TreeMap<Integer, Integer>();
	private Map<Integer, Double> PlaceIndexNormalizedScoreokapi = new TreeMap<Integer, Double>();
	private TreeMap<Integer, Double> PlaceWithScoreokapi = new TreeMap<Integer, Double>();
	private TreeMap<Integer, TreeMap<Integer, Double>> FwdIndexPlaceokapi = new TreeMap<Integer, TreeMap<Integer, Double>>();

	private TreeMap<Integer, TreeMap<Integer, Integer>> authorIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	private TreeMap<Integer, TreeMap<Integer, Integer>> termIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	private TreeMap<Integer, TreeMap<Integer, Integer>> catIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	private TreeMap<Integer, TreeMap<Integer, Integer>> placeIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();

	public IndexWriter(String indexDir) {
		// TODO : YOU MUST IMPLEMENT THIS
		this.indexDir = indexDir;
	}

	public TreeMap<String, Integer> getCatDict() {
		return catDict;
	}

	public void setCatDict(TreeMap<String, Integer> catDict) {
		this.catDict = catDict;
	}

	public TreeMap<String, Integer> getPlaceDict() {
		return placeDict;
	}

	public void setPlaceDict(TreeMap<String, Integer> placeDict) {
		this.placeDict = placeDict;
	}

	public TreeMap<Integer, TreeMap<Integer, Integer>> getAuthorIndex() {
		return authorIndex;
	}

	public void setAuthorIndex(
			TreeMap<Integer, TreeMap<Integer, Integer>> authorIndex) {
		this.authorIndex = authorIndex;
	}

	public TreeMap<Integer, TreeMap<Integer, Integer>> getTermIndex() {
		return termIndex;
	}

	public void setTermIndex(
			TreeMap<Integer, TreeMap<Integer, Integer>> termIndex) {
		this.termIndex = termIndex;
	}

	public TreeMap<Integer, TreeMap<Integer, Integer>> getCatIndex() {
		return catIndex;
	}

	public void setCatIndex(TreeMap<Integer, TreeMap<Integer, Integer>> catIndex) {
		this.catIndex = catIndex;
	}

	public TreeMap<Integer, TreeMap<Integer, Integer>> getPlaceIndex() {
		return placeIndex;
	}

	public void setPlaceIndex(
			TreeMap<Integer, TreeMap<Integer, Integer>> placeIndex) {
		this.placeIndex = placeIndex;
	}

	public IndexWriter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Method to add the given Document to the index This method should take
	 * care of reading the filed values, passing them through corresponding
	 * analyzers and then indexing the results for each indexable field within
	 * the document.
	 * 
	 * @param d
	 *            : The Document to be added
	 * @throws IndexerException
	 *             : In case any error occurs
	 */
	public void addDocument(Document d) throws IndexerException {
		// TODO : YOU MUST IMPLEMENT THIS

		int doc_len = 0;
		AnalyzerFactory factory = AnalyzerFactory.getInstance();
		Analyzer an = null;

		// Tokenizer tok = new Tokenizer();
		TokenStream ts = null;
		Token tkn = null;

		String[] docFile = d.getField(FieldNames.FILEID);

		String doc = docFile[0];
		
//		if(doc.equals("0004003"))
//		{
//			System.out.println("0004003");
//		}

		int docID = docDict.size() + 1;
		docDict.put(doc, docID);

		try {
			if (d.getField(FieldNames.CATEGORY) != null) {
				Tokenizer tok = new Tokenizer();
				for (String toTokenize : d.getField(FieldNames.CATEGORY)) {
					tok.consume(toTokenize);

					an = factory.getAnalyzerForField(FieldNames.CATEGORY,
							tok.consume(toTokenize));

					ts = an.getStream();
					ts.reset();

					while (ts.hasNext()) {
						tkn = ts.next();
						String tknstr = tkn.toString();

						int catID;
						TreeMap<Integer, Integer> postingsList = new TreeMap<Integer, Integer>();

						if (catDict.get(tknstr) != null) { // Contains author
							doc_len++;
							catID = catDict.get(tknstr);

							if (catIndex.get(catID) != null) // Contains entry
							// for author in
							// the map
							{
								postingsList = catIndex.get(catID);

								if (postingsList.get(docID) != null) { // Contains
									// the
									// map
									// in
									// postings
									// list
									int freq = postingsList.get(docID);
									postingsList.put(docID, freq + 1);
									catIndex.put(catID, postingsList);
								}

								else {
									postingsList.put(docID, 1);
									catIndex.put(catID, postingsList);
								}
							}

							else {
								postingsList.put(docID, 1);
								catIndex.put(catID, postingsList);
							}
						}

						else// When entry for author is not present in map
						{
							catDict.put(tknstr, catDict.size() + 1);
							postingsList.put(docID, 1);

							catID = catDict.get(tknstr);
							catIndex.put(catID, postingsList);
						}
					}
				}
			}

			if (d.getField(FieldNames.TITLE) != null) {
				Tokenizer tok = new Tokenizer();
				// Tokenizer tok = new Tokenizer();
				for (String toTokenize : d.getField(FieldNames.TITLE)) {
					if (toTokenize != null) {
						an = factory.getAnalyzerForField(FieldNames.TITLE,
								tok.consume(toTokenize));

						if (an != null) {
							ts = an.getStream();
							ts.reset();

							while (ts.hasNext()) {
								tkn = ts.next();
								String tknstr = tkn.toString();

								int titleID;
								TreeMap<Integer, Integer> postingsList = new TreeMap<Integer, Integer>();

								if (termDict.get(tknstr) != null) { // Contains
																	// author
									doc_len++;
									titleID = termDict.get(tknstr);

									if (termIndex.get(titleID) != null) // Contains
																		// entry
																		// for
																		// author
																		// in
																		// the
																		// map
									{
										postingsList = termIndex.get(titleID);

										if (postingsList.get(docID) != null) {
											int freq = postingsList.get(docID);
											postingsList.put(docID, freq + 1);
											termIndex
													.put(titleID, postingsList);
										}

										else {
											postingsList.put(docID, 1);
											termIndex
													.put(titleID, postingsList);
										}
									}

									else // When entry for author is not present
											// in map
									{
										postingsList.put(docID, 1);
										termIndex.put(titleID, postingsList);
									}
								}

								else {
									termDict.put(tknstr, termDict.size() + 1);
									postingsList.put(docID, 1);

									titleID = termDict.get(tknstr);
									termIndex.put(titleID, postingsList);
								}
							}
						}
					}
				}
			}

			if (d.getField(FieldNames.AUTHOR) != null) {
				Tokenizer tok = new Tokenizer("{}");
				// Tokenizer tok = new Tokenizer();
				for (String toTokenize : d.getField(FieldNames.AUTHOR)) {

					an = factory.getAnalyzerForField(FieldNames.AUTHOR,
							tok.consume(toTokenize));

					ts = an.getStream();
					ts.reset();

					while (ts.hasNext()) {
						tkn = ts.next();
						String tknstr = tkn.toString();

						int authID;
						TreeMap<Integer, Integer> postingsList = new TreeMap<Integer, Integer>();

						if (authorDict.get(tknstr) != null) { // Contains author
							doc_len++;
							authID = authorDict.get(tknstr);

							if (authorIndex.get(authID) != null) // Contains
																	// entry for
																	// author in
																	// the map
							{
								postingsList = authorIndex.get(authID);

								if (postingsList.get(docID) != null) {
									int freq = postingsList.get(docID);
									postingsList.put(docID, freq + 1);
									authorIndex.put(authID, postingsList);
								}

								else {
									postingsList.put(docID, 1);
									authorIndex.put(authID, postingsList);
								}
							}

							else // When entry for author is not present in map
							{
								postingsList.put(docID, 1);
								authorIndex.put(authID, postingsList);
							}
						}

						else {
							authorDict.put(tknstr, authorDict.size() + 1);
							postingsList.put(docID, 1);

							authID = authorDict.get(tknstr);
							authorIndex.put(authID, postingsList);
						}
					}
				}
			}

			if (d.getField(FieldNames.AUTHORORG) != null) {
				Tokenizer tok = new Tokenizer();
				// Tokenizer tok = new Tokenizer();
				for (String toTokenize : d.getField(FieldNames.AUTHORORG)) {

					if (toTokenize != null) {
						an = factory.getAnalyzerForField(FieldNames.AUTHORORG,
								tok.consume(toTokenize));

						ts = an.getStream();
						ts.reset();

						while (ts.hasNext()) {
							tkn = ts.next();
							String tknstr = tkn.toString();

							int authOrgID;
							TreeMap<Integer, Integer> postingsList = new TreeMap<Integer, Integer>();

							if (authorDict.get(tknstr) != null) { // Contains
																	// author
								doc_len++;
								authOrgID = authorDict.get(tknstr);

								if (authorIndex.get(authOrgID) != null) // Contains
																		// entry
																		// for
																		// author
																		// in
																		// the
																		// map
								{
									postingsList = authorIndex.get(authOrgID);

									if (postingsList.get(docID) != null) {
										int freq = postingsList.get(docID);
										postingsList.put(docID, freq + 1);
										authorIndex
												.put(authOrgID, postingsList);
									}

									else {
										postingsList.put(docID, 1);
										authorIndex
												.put(authOrgID, postingsList);
									}
								}

								else // When entry for author is not present in
										// map
								{
									postingsList.put(docID, 1);
									authorIndex.put(authOrgID, postingsList);
								}
							}

							else {
								authorDict.put(tknstr, authorDict.size() + 1);
								postingsList.put(docID, 1);

								authOrgID = authorDict.get(tknstr);
								authorIndex.put(authOrgID, postingsList);
							}
						}
					}
				}
			}

			if ((d.getField(FieldNames.PLACE) != null)
					&& !(d.getField(FieldNames.PLACE).equals(""))) {
				Tokenizer tok = new Tokenizer(",");
				for (String toTokenize : d.getField(FieldNames.PLACE)) {
					if (toTokenize != null) {
						tok.consume(toTokenize);

						an = factory.getAnalyzerForField(FieldNames.PLACE,
								tok.consume(toTokenize));

						ts = an.getStream();
						ts.reset();

						while (ts.hasNext()) {
							tkn = ts.next();
							String tknstr = tkn.toString();

							int placeID;
							TreeMap<Integer, Integer> postingsList = new TreeMap<Integer, Integer>();

							if (placeDict.get(tknstr) != null) { // Contains
																	// author
								doc_len++;
								placeID = placeDict.get(tknstr);

								if (placeIndex.get(placeID) != null) // Contains
																		// entry
																		// for
																		// author
																		// in
																		// the
																		// map
								{
									postingsList = placeIndex.get(placeID);

									if (postingsList.get(docID) != null) { // Contains
																			// the
																			// map
																			// in
																			// postings
																			// list
										int freq = postingsList.get(docID);
										postingsList.put(docID, freq + 1);
										placeIndex.put(placeID, postingsList);
									}

									else {
										postingsList.put(docID, 1);
										placeIndex.put(placeID, postingsList);
									}
								}

								else {
									postingsList.put(docID, 1);
									placeIndex.put(placeID, postingsList);
								}
							}

							else// When entry for author is not present in map
							{
								placeDict.put(tknstr, placeDict.size() + 1);
								postingsList.put(docID, 1);

								placeID = placeDict.get(tknstr);
								placeIndex.put(placeID, postingsList);
							}
						}
					}

				}
			}

			if (d.getField(FieldNames.CONTENT) != null) {
				Tokenizer tok = new Tokenizer();
				for (String toTokenize : d.getField(FieldNames.CONTENT)) {
					if (toTokenize != null && (!toTokenize.equals(""))) {
						tok.consume(toTokenize);

						an = factory.getAnalyzerForField(FieldNames.CONTENT,
								tok.consume(toTokenize));

						if (an != null) {
							ts = an.getStream();
							ts.reset();

							while (ts.hasNext()) {
								tkn = ts.next();
								String tknstr = tkn.toString();

								int termID;
								TreeMap<Integer, Integer> postingsList = new TreeMap<Integer, Integer>();

								if (tknstr.equals(null))
									;

								else {
									if (termDict.get(tknstr) != null) { // Contains
										// author
										doc_len++;
										termID = termDict.get(tknstr);

										if (termIndex.get(termID) != null) // Contains
										// entry for
										// author in
										// the map
										{
											postingsList = termIndex
													.get(termID);

											if (postingsList.get(docID) != null) { // Contains
												// the
												// map
												// in
												// postings
												// list
												int freq = postingsList
														.get(docID);
												postingsList.put(docID,
														freq + 1);
												termIndex.put(termID,
														postingsList);
											}

											else {
												postingsList.put(docID, 1);
												termIndex.put(termID,
														postingsList);
											}
										}

										else {
											postingsList.put(docID, 1);
											termIndex.put(termID, postingsList);
										}
									}

									else// When entry for author is not present
										// in map
									{
										termDict.put(tknstr,
												termDict.size() + 1);
										postingsList.put(docID, 1);

										termID = termDict.get(tknstr);
										termIndex.put(termID, postingsList);
									}
								}
							}

						}
					}
				}
			}
			sum_doc_len += doc_len;
			docLength.put(docID, doc_len);

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Map<Integer, Double> tfidfValue(Map<Integer, Integer> map) {

		ArrayList<Double> tfidf_value = new ArrayList<Double>();

		TreeMap<Integer, Double> doc_score = new TreeMap<Integer, Double>();
		TreeMap<Integer, Double> final_doc_score = new TreeMap<Integer, Double>();
		// IndexReader ir = new IndexReader();
		int docid;
		double total_num_terms;
		double total_num_docs = docDict.size(); // changed
		double total_num_docs_term = map.size();

		for (Map.Entry<Integer, Integer> termFreq : map.entrySet()) {
			docid = termFreq.getKey();
			total_num_terms = termFreq.getValue();
			double tf = (double) Math.log10(1.0 + total_num_terms);
//			double tf = (double) total_num_terms;
			double idf = (double) Math.log10((total_num_docs)
					/ (1.0 + (total_num_docs_term)));
			tfidf_value.add(tf * idf);
			// Collections.sort(tfidf_value);
			// Collections.reverse(tfidf_value);
			doc_score.put(docid, tf * idf);
//			doc_score.put(docid, tf);
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
		return doc_score; // final_doc_score;
	}

	public Map<Integer, Double> okapiValue(Map<Integer, Integer> map,
			double k1, double b) {

		ArrayList<Double> okapi_value = new ArrayList<Double>();
		TreeMap<Integer, Double> doc_score = new TreeMap<Integer, Double>();
		TreeMap<Integer, Double> final_doc_score = new TreeMap<Integer, Double>();
		TreeMap<String, Double> Average_Doc_Length = new TreeMap<String, Double>();
		TreeMap<Integer, Integer> Doc_Length = new TreeMap<Integer, Integer>();
		// IndexReader ir = new IndexReader(indexDir,IndexType.AUTHOR);
		// IndexReader ir1 = new IndexReader();
		// IndexWriter iw= new IndexWriter();
		int docid;
		double total_num_terms;
		double len_of_doc = 0;
		double avg_len_doc = 0;
		double total_num_docs = docDict.size();// changed
		double total_num_docs_term = map.size();
		double final_score = 0.0;

		Average_Doc_Length = averageDocLength();// Changed
		Doc_Length = docLength;
		// for (Map.Entry<Integer, Integer> dl : Doc_Length
		// .entrySet()) {
		// len_of_doc=dl.getValue();
		// }
		// avgDocLength=iw.averageDocLength();
		// Average_Doc_Length=ir1.avgDocLength;

		for (Map.Entry<String, Double> adl : Average_Doc_Length.entrySet()) {
			avg_len_doc = adl.getValue();
		}

		for (Map.Entry<Integer, Integer> termFreq : map.entrySet()) {
			docid = termFreq.getKey();
			total_num_terms = termFreq.getValue();
			len_of_doc = Doc_Length.get(docid);

			double idf = (double) Math
					.log10((total_num_docs - total_num_docs_term) / total_num_docs_term);

			double rest_formula = ((total_num_terms) * (k1 + 1))
					/ (total_num_terms + k1
							* ((1 - b) + (b * (len_of_doc / avg_len_doc))));

//			double k3_formula = ((k3 + 1) * (total_num_terms))
//					/ (k3 + (total_num_terms));

			
				final_score = idf * rest_formula;
			
			okapi_value.add(final_score);

			doc_score.put(docid, final_score);

//			if (okapi_value.size() >= 10) {
//				for (int i = 0; i < 10; i++) {
//
//					for (Map.Entry<Integer, Double> score : doc_score
//							.entrySet()) {
//						if (okapi_value.get(i) == score.getValue()) {
//							final_doc_score.put(score.getKey(),
//									score.getValue());
//						}
//					}
//				}
//			} else {
//				for (int i = 0; i < okapi_value.size(); i++) {
//
//					for (Map.Entry<Integer, Double> score : doc_score
//							.entrySet()) {
//						if (okapi_value.get(i) == score.getValue()) {
//							final_doc_score.put(score.getKey(),
//									score.getValue());
//						}
//					}
//				}
//			}
		}
		return doc_score;
	}

	// Function to calculate average document length
	public TreeMap<String, Double> averageDocLength() {
		double avg_doc_len = sum_doc_len / docLength.size();
		AvgDocLength.put("Average Document Length", avg_doc_len);
		return AvgDocLength;

	}

	/**
	 * Method that indicates that all open resources must be closed and cleaned
	 * and that the entire indexing operation has been completed.
	 * 
	 * @throws IndexerException
	 *             : In case any error occurs
	 */
	public void close() throws IndexerException {
		// TODO

		// Tfidf and normalization for termIndex

		for (Entry<Integer, TreeMap<Integer, Integer>> ti : termIndex
				.entrySet()) {
			termIndexScore = ti.getValue(); // Postings list of a term
			int key = ti.getKey();
			
//			if(key == 9007)
//			{
//				System.out.println("axcvahg");
//			}
			termIndexNormalizedScore = tfidfValue(termIndexScore); // Returns
																	// a map
																	// with
																	// docid
																	// and
																	// score
			for (Entry<Integer, Double> tins : termIndexNormalizedScore
					.entrySet()) {
				if (FwdIndexTerm.get(tins.getKey()) != null) {
//					if(key == 9007)
//						{
//						System.out.println(FwdIndexTerm.get(684));
//						}
					
					termWithScore = FwdIndexTerm.get(tins.getKey());
				} else {
					termWithScore = new TreeMap<Integer, Double>();
				}
				termWithScore.put(key, tins.getValue());
				
//				if(key == 9007)
//				{
//				System.out.println(termWithScore.get(9007));
//				}
				FwdIndexTerm.put(tins.getKey(), termWithScore);
				
//				if(key == 9007)
//				{
//				System.out.println(FwdIndexTerm.get(684));
//				}

			}
		}
		
//		System.out.println("FwdIndexTerm size in writer = " + FwdIndexTerm.get(684));

		// Normalization
		
//		TreeMap<String, Long> file_size1 = new TreeMap<String, Long>();
//		file_size1 = Parser.file_size;
		
		
		for (Entry<Integer, TreeMap<Integer, Double>> fit : FwdIndexTerm
		.entrySet()) {
	Double normScore = 0.0;
	Double finalnormScore = 0.0;
	int key = fit.getKey();
	Map<Integer, Double> value = new TreeMap<Integer, Double>();
	value = fit.getValue();
	for (Entry<Integer, Double> tis : value.entrySet()) {
		normScore += Math.pow(tis.getValue(), 2); // Squaring and adding
													// each tfidf score
	}
	finalnormScore = Math.sqrt(normScore); // taking square root of
	// addition of square of
	// tfidf value
	for (Entry<Integer, Double> tis : value.entrySet()) {
		if (FwdIndexTerm1.get(key) != null) {
			termWithScore1 = FwdIndexTerm1.get(key);
		} else {
			termWithScore1 = new TreeMap<Integer, Double>();
		}

		termWithScore1.put(tis.getKey(),
				(tis.getValue() / finalnormScore));
		FwdIndexTerm1.put(key, termWithScore1);
	}
		}


		// Tfidf and normalization for AuthorIndex

		for (Entry<Integer, TreeMap<Integer, Integer>> ti : authorIndex
				.entrySet()) {
			AuthorIndexScore = ti.getValue(); // Postings list of a term
			int key = ti.getKey();
			AuthorIndexNormalizedScore = tfidfValue(AuthorIndexScore); // Returns
																		// a map
																		// with
																		// docid
																		// and
																		// score
			for (Entry<Integer, Double> tins : AuthorIndexNormalizedScore
					.entrySet()) {
				if (FwdIndexAuthor.get(tins.getKey()) != null) {
					AuthorWithScore = FwdIndexAuthor.get(tins.getKey());
				} else {
					AuthorWithScore = new TreeMap<Integer, Double>();
				}
				AuthorWithScore.put(key, tins.getValue());
				FwdIndexAuthor.put(tins.getKey(), AuthorWithScore);

			}
		}

//		// Normalization
		for (Entry<Integer, TreeMap<Integer, Double>> fit : FwdIndexAuthor
				.entrySet()) {
			Double normScore = 0.0;
			Double finalnormScore = 0.0;
			int key = fit.getKey();
			Map<Integer, Double> value = new TreeMap<Integer, Double>();
			value = fit.getValue();
			for (Entry<Integer, Double> tis : value.entrySet()) {
				normScore += Math.pow(tis.getValue(), 2); // Squaring and adding
															// each tfidf score
			}
			finalnormScore = Math.sqrt(normScore); // taking square root of
			// addition of square of
			// tfidf value
			for (Entry<Integer, Double> tis : value.entrySet()) {
				if (FwdIndexAuthor1.get(key) != null) {
					AuthorWithScore1 = FwdIndexAuthor1.get(key);
				} else {
					AuthorWithScore1.clear();
				}

				AuthorWithScore1.put(tis.getKey(),
						(tis.getValue() / finalnormScore));
				FwdIndexAuthor1.put(key, AuthorWithScore1);
			}
		}

		// Tfidf and normalization for CategoryIndex

		for (Entry<Integer, TreeMap<Integer, Integer>> ti : catIndex.entrySet()) {
			CategoryIndexScore = ti.getValue(); // Postings list of a term
			int key = ti.getKey();
			CategoryIndexNormalizedScore = tfidfValue(CategoryIndexScore); // Returns
			// a map
			// with
			// docid
			// and
			// score
			for (Entry<Integer, Double> tins : CategoryIndexNormalizedScore
					.entrySet()) {
				if (FwdIndexCategory.get(tins.getKey()) != null) {
					CategoryWithScore = FwdIndexCategory.get(tins.getKey());
				} else {
					CategoryWithScore = new TreeMap<Integer, Double>();
				}
				CategoryWithScore.put(key, tins.getValue());
				FwdIndexCategory.put(tins.getKey(), CategoryWithScore);

			}
		}

//		// Normalization
		for (Entry<Integer, TreeMap<Integer, Double>> fit : FwdIndexCategory
				.entrySet()) {
			Double normScore = 0.0;
			Double finalnormScore = 0.0;
			int key = fit.getKey();
			Map<Integer, Double> value = new TreeMap<Integer, Double>();
			value = fit.getValue();
			for (Entry<Integer, Double> tis : value.entrySet()) {
				normScore += Math.pow(tis.getValue(), 2); // Squaring and adding
															// each tfidf score
			}
			finalnormScore = Math.sqrt(normScore); // taking square root of
			// addition of square of
			// tfidf value
			for (Entry<Integer, Double> tis : value.entrySet()) {
				if (FwdIndexCategory1.get(key) != null) {
					CategoryWithScore1 = FwdIndexCategory1.get(key);
				} else {
					CategoryWithScore1.clear();
				}
				CategoryWithScore1.put(tis.getKey(),
						(tis.getValue() / finalnormScore));
				FwdIndexCategory1.put(key, CategoryWithScore1);
			}
		}

		// Tfidf and normalization for PlaceIndex

		for (Entry<Integer, TreeMap<Integer, Integer>> ti : placeIndex
				.entrySet()) {
			PlaceIndexScore = ti.getValue(); // Postings list of a term
			int key = ti.getKey();
			PlaceIndexNormalizedScore = tfidfValue(PlaceIndexScore); // Returns
																		// a map
																		// with
																		// docid
																		// and
																		// score
			for (Entry<Integer, Double> tins : PlaceIndexNormalizedScore
					.entrySet()) {
				if (FwdIndexPlace.get(tins.getKey()) != null) {
					PlaceWithScore = FwdIndexPlace.get(tins.getKey());
				} else {
					PlaceWithScore = new TreeMap<Integer, Double>();
				}
				PlaceWithScore.put(key, tins.getValue());
				FwdIndexPlace.put(tins.getKey(), PlaceWithScore);

			}
		}

//		// Normalization
		for (Entry<Integer, TreeMap<Integer, Double>> fit : FwdIndexPlace
				.entrySet()) {
			Double normScore = 0.0;
			Double finalnormScore = 0.0;
			int key = fit.getKey();
			Map<Integer, Double> value = new TreeMap<Integer, Double>();
			value = fit.getValue();
			for (Entry<Integer, Double> tis : value.entrySet()) {
				normScore += Math.pow(tis.getValue(), 2); // Squaring and adding
															// each tfidf score
			}
			finalnormScore = Math.sqrt(normScore); // taking square root of
			// addition of square of
			// tfidf value
			for (Entry<Integer, Double> tis : value.entrySet()) {
				if (FwdIndexPlace1.get(key) != null) {
					PlaceWithScore1 = FwdIndexPlace1.get(key);
				} else {
					PlaceWithScore1.clear();
				}
				PlaceWithScore1.put(tis.getKey(),
						(tis.getValue() / finalnormScore));
				FwdIndexPlace1.put(key, PlaceWithScore1);
			}
		}

		// Okapi for termIndex

		for (Entry<Integer, TreeMap<Integer, Integer>> ti : termIndex
				.entrySet()) {
			termIndexScoreokapi = ti.getValue(); // Postings list of a term
			int key = ti.getKey();
			termIndexNormalizedScoreokapi = okapiValue(termIndexScoreokapi,
					1, 1); // Returns
			// a map
			// with
			// docid
			// and
			// score
			for (Entry<Integer, Double> tins : termIndexNormalizedScoreokapi
					.entrySet()) {
				if (FwdIndexTermokapi.get(tins.getKey()) != null) {
					termWithScoreokapi = FwdIndexTermokapi.get(tins.getKey());
				} else {
					termWithScoreokapi = new TreeMap<Integer, Double>();
				}
				termWithScoreokapi.put(key, tins.getValue());
				FwdIndexTermokapi.put(tins.getKey(), termWithScoreokapi);

			}
		}

		// Okapi for AuthorIndex

		for (Entry<Integer, TreeMap<Integer, Integer>> ti : authorIndex
				.entrySet()) {
			AuthorIndexScoreokapi = ti.getValue(); // Postings list of a term
			int key = ti.getKey();
			AuthorIndexNormalizedScoreokapi = okapiValue(AuthorIndexScoreokapi,
					1, 1); // Returns
			// a map
			// with
			// docid
			// and
			// score
			for (Entry<Integer, Double> tins : AuthorIndexNormalizedScoreokapi
					.entrySet()) {
				if (FwdIndexAuthorokapi.get(tins.getKey()) != null) {
					AuthorWithScoreokapi = FwdIndexAuthorokapi.get(tins.getKey());
				} else {
					AuthorWithScoreokapi  = new TreeMap<Integer, Double>();
				}
				AuthorWithScoreokapi.put(key, tins.getValue());
				FwdIndexAuthorokapi.put(tins.getKey(), AuthorWithScoreokapi);

			}
		}

		// Okapi for CategoryIndex

		for (Entry<Integer, TreeMap<Integer, Integer>> ti : catIndex.entrySet()) {
			CategoryIndexScoreokapi = ti.getValue(); // Postings list of a term
			int key = ti.getKey();
			CategoryIndexNormalizedScoreokapi = okapiValue(
					CategoryIndexScoreokapi, 1.50, 0.75); // Returns
			// a map
			// with
			// docid
			// and
			// score
			for (Entry<Integer, Double> tins : CategoryIndexNormalizedScoreokapi
					.entrySet()) {
				if (FwdIndexCategoryokapi.get(tins.getKey()) != null) {
					CategoryWithScoreokapi = FwdIndexCategoryokapi.get(tins.getKey());
				} else {
					CategoryWithScoreokapi = new TreeMap<Integer, Double>();
				}
				CategoryWithScoreokapi.put(key, tins.getValue());
				FwdIndexCategoryokapi
						.put(tins.getKey(), CategoryWithScoreokapi);

			}
		}

		// Okapi for PlaceIndex

		for (Entry<Integer, TreeMap<Integer, Integer>> ti : placeIndex
				.entrySet()) {
			PlaceIndexScoreokapi = ti.getValue(); // Postings list of a term
			int key = ti.getKey();
			PlaceIndexNormalizedScoreokapi = okapiValue(PlaceIndexScoreokapi,
					1.50, 0.75); // Returns
			// a map
			// with
			// docid
			// and
			// score
			for (Entry<Integer, Double> tins : PlaceIndexNormalizedScoreokapi
					.entrySet()) {
				if (FwdIndexPlaceokapi.get(tins.getKey()) != null) {
					PlaceWithScoreokapi = FwdIndexPlaceokapi.get(tins.getKey());
				} else {
					PlaceWithScoreokapi  = new TreeMap<Integer, Double>();
				}
				PlaceWithScoreokapi.put(key, tins.getValue());
				FwdIndexPlaceokapi.put(tins.getKey(), PlaceWithScoreokapi);

			}
		}

		// Writing Docs
		TreeMap<String, Double> avg_doc_len1 = new TreeMap<String, Double>();
		avg_doc_len1 = averageDocLength();
//		System.out.println("Average document length is" + avg_doc_len1);
		File avgDocLenfile = new File(indexDir + File.separator
				+ "avgDocLen.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(avgDocLenfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(avg_doc_len1);
			oOS.close();
		} catch (Exception e) {

		}

		File docLenfile = new File(indexDir + File.separator + "docLen.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(docLenfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(docLength);
			oOS.close();
		} catch (Exception e) {

		}

		File docDictfile = new File(indexDir + File.separator + "docDict.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(docDictfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(docDict);
			oOS.close();
		} catch (Exception e) {

		}

		File catDictfile = new File(indexDir + File.separator
				+ "categoryDict.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(catDictfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(catDict);
			oOS.close();
		} catch (Exception e) {

		}

		File catIndexfile = new File(indexDir + File.separator
				+ "categoryIndex.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(catIndexfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(catIndex);
			oOS.close();
		} catch (Exception e) {

		}

		File authorDictfile = new File(indexDir + File.separator
				+ "authorDict.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(authorDictfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(authorDict);
			oOS.close();
		} catch (Exception e) {

		}

		File authorIndexfile = new File(indexDir + File.separator
				+ "authorIndex.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(authorIndexfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(authorIndex);
			oOS.close();
		} catch (Exception e) {

		}

		File placeDictfile = new File(indexDir + File.separator
				+ "placeDict.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(placeDictfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(placeDict);
			oOS.close();
		} catch (Exception e) {

		}

		File placeIndexfile = new File(indexDir + File.separator
				+ "placeIndex.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(placeIndexfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(placeIndex);
			oOS.close();
		} catch (Exception e) {

		}

		File termDictfile = new File(indexDir + File.separator + "termDict.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(termDictfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(termDict);
			oOS.close();
		} catch (Exception e) {

		}

		File termIndexfile = new File(indexDir + File.separator
				+ "termIndex.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(termIndexfile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(termIndex);
			oOS.close();
		} catch (Exception e) {

		}

//		File fwdTermIndexFile = new File(indexDir + File.separator
//				+ "fwdTermIndex.txt");
//		try {
//			FileOutputStream fOS = new FileOutputStream(fwdTermIndexFile);
//			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
//			oOS.writeObject(FwdIndexTerm);
//			oOS.close();
//		} catch (Exception e) {
//
//		}
		File fwdTermIndexFile = new File(indexDir + File.separator
				+ "fwdTermIndex.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(fwdTermIndexFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(FwdIndexTerm1);
			oOS.close();
		} catch (Exception e) {

		}

		File fwdAuthorIndexFile = new File(indexDir + File.separator
				+ "fwdAuthorIndex.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(fwdAuthorIndexFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(FwdIndexAuthor1);
			oOS.close();
		} catch (Exception e) {

		}

		File fwdPlaceIndexFile = new File(indexDir + File.separator
				+ "fwdPlaceIndex.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(fwdPlaceIndexFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(FwdIndexPlace1);
			oOS.close();
		} catch (Exception e) {

		}

		File fwdCategoryIndexFile = new File(indexDir + File.separator
				+ "fwdCategoryIndex.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(fwdCategoryIndexFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(FwdIndexCategory1);
			oOS.close();
		} catch (Exception e) {

		}

		File fwdTermIndexokapiFile = new File(indexDir + File.separator
				+ "fwdTermIndexokapi.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(fwdTermIndexokapiFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(FwdIndexTermokapi);
			oOS.close();
		} catch (Exception e) {

		}

		File fwdAuthorIndexokapiFile = new File(indexDir + File.separator
				+ "fwdAuthorIndexokapi.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(fwdAuthorIndexokapiFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(FwdIndexAuthorokapi);
			oOS.close();
		} catch (Exception e) {

		}

		File fwdPlaceIndexokapiFile = new File(indexDir + File.separator
				+ "fwdPlaceIndexokapi.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(fwdPlaceIndexokapiFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(FwdIndexPlaceokapi);
			oOS.close();
		} catch (Exception e) {

		}

		File fwdCategoryIndexokapiFile = new File(indexDir + File.separator
				+ "fwdCategoryIndexokapi.txt");
		try {
			FileOutputStream fOS = new FileOutputStream(
					fwdCategoryIndexokapiFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			oOS.writeObject(FwdIndexCategoryokapi);
			oOS.close();
		} catch (Exception e) {

		}

	}
}
