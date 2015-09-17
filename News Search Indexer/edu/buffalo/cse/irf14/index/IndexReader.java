/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo Class that emulates reading data back from a written index
 */
public class IndexReader {

	String indexDir;
	IndexType type;
	public TreeMap<String, Double> avgDocLength = new TreeMap<String, Double>();
	public TreeMap<Integer, Integer> DocLength = new TreeMap<Integer, Integer>();

	public TreeMap<String, Integer> docDict = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> termDict = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> authorDict = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> catDict = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> placeDict = new TreeMap<String, Integer>();

	private TreeMap<Integer, TreeMap<Integer, Integer>> authorIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	private TreeMap<Integer, TreeMap<Integer, Integer>> termIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	private TreeMap<Integer, TreeMap<Integer, Integer>> catIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	private TreeMap<Integer, TreeMap<Integer, Integer>> placeIndex = new TreeMap<Integer, TreeMap<Integer, Integer>>();

	public TreeMap<Integer, TreeMap<Integer, Double>> fwdAuthorIndex = new TreeMap<Integer, TreeMap<Integer, Double>>();
	public TreeMap<Integer, TreeMap<Integer, Double>> fwdTermIndex = new TreeMap<Integer, TreeMap<Integer, Double>>();
	public TreeMap<Integer, TreeMap<Integer, Double>> fwdCatIndex = new TreeMap<Integer, TreeMap<Integer, Double>>();
	public TreeMap<Integer, TreeMap<Integer, Double>> fwdPlaceIndex = new TreeMap<Integer, TreeMap<Integer, Double>>();

	public TreeMap<Integer, TreeMap<Integer, Double>> fwdAuthorIndexokapi = new TreeMap<Integer, TreeMap<Integer, Double>>();
	public TreeMap<Integer, TreeMap<Integer, Double>> fwdTermIndexokapi = new TreeMap<Integer, TreeMap<Integer, Double>>();
	public TreeMap<Integer, TreeMap<Integer, Double>> fwdCatIndexokapi = new TreeMap<Integer, TreeMap<Integer, Double>>();
	public TreeMap<Integer, TreeMap<Integer, Double>> fwdPlaceIndexokapi = new TreeMap<Integer, TreeMap<Integer, Double>>();

	/**
	 * Default constructor
	 * 
	 * @param indexDir
	 *            : The root directory from which the index is to be read. This
	 *            will be exactly the same directory as passed on IndexWriter.
	 *            In case you make subdirectories etc., you will have to handle
	 *            it accordingly.
	 * @param type
	 *            The {@link IndexType} to read from
	 */
	@SuppressWarnings("unchecked")
	public IndexReader(String indexDir, IndexType type) {
		// TODO
		this.indexDir = indexDir;
		this.type = type;

		try {
			File docDictfile = new File(this.indexDir + File.separator
					+ "docDict.txt");
			FileInputStream fIS = new FileInputStream(docDictfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			docDict = (TreeMap<String, Integer>) oIS.readObject();
//			System.out.println(docDict.containsValue(7037));
//			System.out.println(docDict.get("0001934"));
//			System.out.println(docDict.get("0000219"));
//			System.out.println(docDict.get("0000231"));
			
			oIS.close();
		}

		catch (Exception e) {

		}

		try {
			File authDictfile = new File(this.indexDir + File.separator
					+ "authorDict.txt");
			FileInputStream fIS = new FileInputStream(authDictfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			authorDict = (TreeMap<String, Integer>) oIS.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File authIndexfile = new File(this.indexDir + File.separator
					+ "authorIndex.txt");
			FileInputStream fIS = new FileInputStream(authIndexfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			authorIndex = (TreeMap<Integer, TreeMap<Integer, Integer>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File catDictfile = new File(this.indexDir + File.separator
					+ "categoryDict.txt");
			FileInputStream fIS = new FileInputStream(catDictfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			catDict = (TreeMap<String, Integer>) oIS.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File catIndexfile = new File(this.indexDir + File.separator
					+ "categoryIndex.txt");
			FileInputStream fIS = new FileInputStream(catIndexfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			catIndex = (TreeMap<Integer, TreeMap<Integer, Integer>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File termDictfile = new File(this.indexDir + File.separator
					+ "termDict.txt");
			FileInputStream fIS = new FileInputStream(termDictfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			termDict = (TreeMap<String, Integer>) oIS.readObject();
//			System.out.println(termDict.get("tapioca"));
			oIS.close();
		} catch (Exception e) {

		}
		
		try {
			File termIndexfile = new File(this.indexDir + File.separator
					+ "termIndex.txt");
			FileInputStream fIS = new FileInputStream(termIndexfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			termIndex = (TreeMap<Integer, TreeMap<Integer, Integer>>) oIS
					.readObject();
//			System.out.println(termIndex.get(termDict.get("tapioca")));
			oIS.close();
		} catch (Exception e) {

		}

		
		try {
			File placeDictfile = new File(this.indexDir + File.separator
					+ "placeDict.txt");
			FileInputStream fIS = new FileInputStream(placeDictfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			placeDict = (TreeMap<String, Integer>) oIS.readObject();
			
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File placeIndexfile = new File(this.indexDir + File.separator
					+ "placeIndex.txt");
			FileInputStream fIS = new FileInputStream(placeIndexfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			placeIndex = (TreeMap<Integer, TreeMap<Integer, Integer>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}



		try {
			File fwdTermIndexfile = new File(this.indexDir + File.separator
					+ "fwdTermIndex.txt");
			FileInputStream fIS = new FileInputStream(fwdTermIndexfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			fwdTermIndex = (TreeMap<Integer, TreeMap<Integer, Double>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}
		
//		System.out.println("FwdIndexTerm size in reader = " + fwdTermIndex.get(684).get(9007));

		try {
			File fwdCatIndexfile = new File(this.indexDir + File.separator
					+ "fwdCategoryIndex.txt");
			FileInputStream fIS = new FileInputStream(fwdCatIndexfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			fwdCatIndex = (TreeMap<Integer, TreeMap<Integer, Double>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File fwdAuthorIndexfile = new File(this.indexDir + File.separator
					+ "fwdAuthorIndex.txt");
			FileInputStream fIS = new FileInputStream(fwdAuthorIndexfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			fwdAuthorIndex = (TreeMap<Integer, TreeMap<Integer, Double>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File fwdPlaceIndexfile = new File(this.indexDir + File.separator
					+ "fwdPlaceIndex.txt");
			FileInputStream fIS = new FileInputStream(fwdPlaceIndexfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			fwdPlaceIndex = (TreeMap<Integer, TreeMap<Integer, Double>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}
    
		//	Okapi file reader
		
		try {
			File fwdTermIndexokapifile = new File(this.indexDir + File.separator
					+ "fwdTermIndexokapi.txt");
			FileInputStream fIS = new FileInputStream(fwdTermIndexokapifile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			fwdTermIndexokapi = (TreeMap<Integer, TreeMap<Integer, Double>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File fwdCatIndexokapifile = new File(this.indexDir + File.separator
					+ "fwdCategoryIndexokapi.txt");
			FileInputStream fIS = new FileInputStream(fwdCatIndexokapifile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			fwdCatIndexokapi = (TreeMap<Integer, TreeMap<Integer, Double>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File fwdAuthorIndexokapifile = new File(this.indexDir + File.separator
					+ "fwdAuthorIndexokapi.txt");
			FileInputStream fIS = new FileInputStream(fwdAuthorIndexokapifile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			fwdAuthorIndexokapi = (TreeMap<Integer, TreeMap<Integer, Double>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File fwdPlaceIndexokapifile = new File(this.indexDir + File.separator
					+ "fwdPlaceIndexokapi.txt");
			FileInputStream fIS = new FileInputStream(fwdPlaceIndexokapifile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			fwdPlaceIndexokapi = (TreeMap<Integer, TreeMap<Integer, Double>>) oIS
					.readObject();
			oIS.close();
		} catch (Exception e) {

		}

		try {
			File avgDocLenfile = new File(this.indexDir + File.separator
					+ "avgDocLen.txt");
			FileInputStream fIS = new FileInputStream(avgDocLenfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			avgDocLength = (TreeMap<String, Double>) oIS.readObject();
			oIS.close();

		} catch (Exception e) {

		}

		try {
			File DocLenfile = new File(this.indexDir + File.separator
					+ "docLen.txt");
			FileInputStream fIS = new FileInputStream(DocLenfile);
			ObjectInputStream oIS = new ObjectInputStream(fIS);
			DocLength = (TreeMap<Integer, Integer>) oIS.readObject();
			oIS.close();

		} catch (Exception e) {

		}

//		for (Map.Entry<String, Double> score : avgDocLength.entrySet()) {
//			System.out.println("Index Reader op is" + score.getValue());
//		}

	}

	public IndexReader() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get total number of terms from the "key" dictionary associated with this
	 * index. A postings list is always created against the "key" dictionary
	 * 
	 * @return The total number of terms
	 */
	public int getTotalKeyTerms() {
		// TODO : YOU MUST IMPLEMENT THIS

		int totalKeyTerms;

		switch (type) {
		case AUTHOR:
			totalKeyTerms = authorDict.size();
			break;

		case PLACE:
			totalKeyTerms = placeDict.size();
			break;

		case CATEGORY:
			totalKeyTerms = catDict.size();
			break;

		case TERM:
			totalKeyTerms = termDict.size();
			break;

		default:
			totalKeyTerms = 0;
			break;
		}

		return totalKeyTerms;
	}

	/**
	 * Get total number of terms from the "value" dictionary associated with
	 * this index. A postings list is always created with the "value" dictionary
	 * 
	 * @return The total number of terms
	 */
	public int getTotalValueTerms() {
		// TODO: YOU MUST IMPLEMENT THIS

		int totalValueTerms = docDict.size();
		return totalValueTerms;
	}

	/**
	 * Method to get the postings for a given term. You can assume that the raw
	 * string that is used to query would be passed through the same Analyzer as
	 * the original field would have been.
	 * 
	 * @param term
	 *            : The "analyzed" term to get postings for
	 * @return A Map containing the corresponding fileid as the key and the
	 *         number of occurrences as values if the given term was found, null
	 *         otherwise.
	 */
	public Map<String, Integer> getPostings(String term) {
		// TODO:YOU MUST IMPLEMENT THIS

		Map<String, Integer> postings = new TreeMap<String, Integer>();
		TreeMap<Integer, Integer> postingsIndex = null;

		Tokenizer tok = null;
		TokenStream ts = null;
		Token tkn = null;
		AnalyzerFactory anF = null;
		Analyzer an = null;

		String filteredTerm;
		Integer key;
		Integer value;
		String strKey = null;
		// int intValue;

		switch (type) {
		case AUTHOR:

			tok = new Tokenizer("{}");
			try {
				if (term != null) {
					ts = tok.consume(term);

					anF = new AnalyzerFactory();
					an = anF.getAnalyzerForField(FieldNames.AUTHOR, ts);

					if (an != null) {
						ts = an.getStream();
						ts.reset();

						while (ts.hasNext()) {
							tkn = ts.next();
							filteredTerm = tkn.toString();
							if (authorDict.get(filteredTerm) != null) {
								int authID = authorDict.get(filteredTerm);// gives
																			// ID
																			// for
																			// searching
																			// in
																			// index
								postingsIndex = authorIndex.get(authID);// gives
																		// postings
																		// list

								for (Map.Entry<Integer, Integer> docFreq : postingsIndex
										.entrySet()) {
									key = docFreq.getKey(); // gives docID

									for (Map.Entry<String, Integer> dict : docDict
											.entrySet()) {
										if (dict.getValue().equals(key)) {
											strKey = dict.getKey();
										}
									}
									value = docFreq.getValue();

									postings.put(strKey, value);
								}
							} else
								postings = null;
						}
					}
				}
			} catch (Exception e) {

			}

			break;

		case PLACE:
			tok = new Tokenizer(",");
			try {
				if (term != null) {
					ts = tok.consume(term);

					anF = new AnalyzerFactory();
					an = anF.getAnalyzerForField(FieldNames.PLACE, ts);

					if (an != null) {
						ts = an.getStream();
						ts.reset();

						while (ts.hasNext()) {
							tkn = ts.next();
							filteredTerm = tkn.toString();
							if (placeDict.get(filteredTerm) != null) {
								int placeID = placeDict.get(filteredTerm);// gives
																			// ID
																			// for
																			// searching
																			// in
																			// index
								postingsIndex = placeIndex.get(placeID);// gives
																		// postings
																		// list

								for (Map.Entry<Integer, Integer> docFreq : postingsIndex
										.entrySet()) {
									key = docFreq.getKey(); // gives docID

									for (Map.Entry<String, Integer> dict : docDict
											.entrySet()) {
										if (dict.getValue().equals(key)) {
											strKey = dict.getKey();
										}
									}
									value = docFreq.getValue();

									postings.put(strKey, value);
								}
							}

							else
								postings = null;
						}
					}
				}
			} catch (Exception e) {

			}

			break;
		case CATEGORY:
			tok = new Tokenizer();
			try {
				if (term != null) {
					ts = tok.consume(term);

					anF = new AnalyzerFactory();
					an = anF.getAnalyzerForField(FieldNames.CATEGORY, ts);

					if (an != null) {
						ts = an.getStream();
						ts.reset();

						while (ts.hasNext()) {
							tkn = ts.next();
							filteredTerm = tkn.toString();
							if (catDict.get(filteredTerm) != null) {
								int catID = catDict.get(filteredTerm);// gives
																		// ID
																		// for
																		// searching
																		// in
																		// index
								postingsIndex = catIndex.get(catID);// gives
																	// postings
																	// list

								for (Map.Entry<Integer, Integer> docFreq : postingsIndex
										.entrySet()) {
									key = docFreq.getKey(); // gives docID

									for (Map.Entry<String, Integer> dict : docDict
											.entrySet()) {
										if (dict.getValue().equals(key)) {
											strKey = dict.getKey();
										}
									}
									value = docFreq.getValue();

									postings.put(strKey, value);
								}
							}

							else
								postings = null;
						}
					}
				}
			} catch (Exception e) {

			}

			break;
		case TERM:
			tok = new Tokenizer();
			try {
				if (term != null) {
					ts = tok.consume(term);

					anF = new AnalyzerFactory();
					an = anF.getAnalyzerForField(FieldNames.TITLE, ts);

					if (an != null) {
						ts = an.getStream();
						ts.reset();

						while (ts.hasNext()) {
							tkn = ts.next();
							filteredTerm = tkn.toString();

							if (termDict.get(filteredTerm) != null) {
								int termID = termDict.get(filteredTerm);// gives
																		// ID
																		// for
																		// searching
																		// in
																		// index
								postingsIndex = termIndex.get(termID);// gives
																		// postings
																		// list

								for (Map.Entry<Integer, Integer> docFreq : postingsIndex
										.entrySet()) {
									key = docFreq.getKey(); // gives docID

									for (Map.Entry<String, Integer> dict : docDict
											.entrySet()) {
										if (dict.getValue().equals(key)) {
											strKey = dict.getKey();
										}
									}
									value = docFreq.getValue();

									postings.put(strKey, value);
								}
							} else
								postings = null;
						}

						an = anF.getAnalyzerForField(FieldNames.CONTENT, ts);

						if (an != null) {
							ts = an.getStream();
							ts.reset();

							while (ts.hasNext()) {
								tkn = ts.next();
								filteredTerm = tkn.toString();
								if (termDict.get(filteredTerm) != null) {
									int termID = termDict.get(filteredTerm);// gives
																			// ID
																			// for
																			// searching
																			// in
																			// index
									postingsIndex = termIndex.get(termID);// gives
																			// postings
																			// list

									for (Map.Entry<Integer, Integer> docFreq : postingsIndex
											.entrySet()) {
										key = docFreq.getKey(); // gives docID

										for (Map.Entry<String, Integer> dict : docDict
												.entrySet()) {
											if (dict.getValue().equals(key)) {
												strKey = dict.getKey();
											}
										}
										value = docFreq.getValue();

										if (postings.get(strKey) != null) {
											// Do nothing
											// int val = postings.get(strKey);
											// postings.put(strKey, value+val);
										} else {
											postings.put(strKey, value);
										}
									}
								}

								else
									postings = null;
							}

						}
					}
				}
			} catch (Exception e) {

			}

			break;
		default:
			postings = null;
			break;
		}

		return postings;
	}

	/**
	 * Method to get the top k terms from the index in terms of the total number
	 * of occurrences.
	 * 
	 * @param k
	 *            : The number of terms to fetch
	 * @return : An ordered list of results. Must be <=k fr valid k values null
	 *         for invalid k values
	 */
	public List<String> getTopK(int k) {
		// TODO YOU MUST IMPLEMENT THIS
		List<String> maxfreq_author = new ArrayList<String>();
		List<Integer> max_freq = new ArrayList<Integer>();
		List<Integer> max_freq_authorid = new ArrayList<Integer>();
		TreeMap<Integer, Integer> term_freq_dict = new TreeMap<Integer, Integer>();

		if (k < 1) {
			maxfreq_author = null;
		}

		else {
			switch (type) {
			case TERM: {

				if (k <= termDict.size()) {
					for (Map.Entry<Integer, TreeMap<Integer, Integer>> fields : termIndex
							.entrySet())

					{
						int value1 = 0;
						int key = fields.getKey();
						TreeMap<Integer, Integer> value = fields.getValue();

						for (Map.Entry<Integer, Integer> fields1 : value
								.entrySet()) {

							value1 += fields1.getValue();

						}

						term_freq_dict.put(key, value1);
						max_freq.add(value1);

					}
					Collections.sort(max_freq);
					Collections.reverse(max_freq);

					for (int i = 0; i < k; i++)

					{

						for (Map.Entry<Integer, Integer> int_fields : term_freq_dict
								.entrySet()) {

							if (max_freq.get(i).equals(int_fields.getValue())) {
								max_freq_authorid.add(int_fields.getKey());
							}
						}
					}

					for (int i = 0; i < k; i++) {
						for (Map.Entry<String, Integer> fields : termDict
								.entrySet()) {
							if ((max_freq_authorid).get(i).equals(
									fields.getValue())) {
								maxfreq_author.add(fields.getKey());
							}
						}
					}
				}

				else
					maxfreq_author = null;
				break;

			}

			case AUTHOR: {
				if (k <= authorDict.size()) {
					for (Map.Entry<Integer, TreeMap<Integer, Integer>> fields : authorIndex
							.entrySet())

					{
						int freq_author;
						int key = fields.getKey();
						TreeMap<Integer, Integer> value = fields.getValue();
						freq_author = value.size();
						term_freq_dict.put(key, freq_author);
						max_freq.add(freq_author);

					}
					Collections.sort(max_freq);
					Collections.reverse(max_freq);

					for (Map.Entry<Integer, Integer> int_fields : term_freq_dict
							.entrySet())

					{

						for (int i = 0; i < k; i++) {

							if (max_freq.get(i).equals(int_fields.getValue())) {
								max_freq_authorid.add(int_fields.getKey());
							}

						}

					}

					for (Map.Entry<String, Integer> fields : authorDict
							.entrySet()) {
						for (int i = 0; i < k; i++) {
							if ((max_freq_authorid).get(i).equals(
									fields.getValue())) {
								maxfreq_author.add(fields.getKey());
							}
						}
					}
				} else
					maxfreq_author = null;
				break;
			}

			case CATEGORY: {
				if (k <= catDict.size()) {
					for (Map.Entry<Integer, TreeMap<Integer, Integer>> fields : catIndex
							.entrySet())

					{
						int freq_author;
						int key = fields.getKey();
						TreeMap<Integer, Integer> value = fields.getValue();
						freq_author = value.size();
						term_freq_dict.put(key, freq_author);
						max_freq.add(freq_author);

					}
					Collections.sort(max_freq);
					Collections.reverse(max_freq);

					for (Map.Entry<Integer, Integer> int_fields : term_freq_dict
							.entrySet())

					{

						for (int i = 0; i < k; i++) {

							if (max_freq.get(i).equals(int_fields.getValue())) {
								max_freq_authorid.add(int_fields.getKey());
							}

						}

					}

					for (Map.Entry<String, Integer> fields : catDict.entrySet()) {
						for (int i = 0; i < k; i++) {
							if ((max_freq_authorid).get(i).equals(
									fields.getValue())) {
								maxfreq_author.add(fields.getKey());
							}
						}
					}
				} else
					maxfreq_author = null;
				break;
			}

			case PLACE: {
				if (k <= placeDict.size()) {
					for (Map.Entry<Integer, TreeMap<Integer, Integer>> fields : placeIndex
							.entrySet())

					{
						int freq_author;
						int key = fields.getKey();
						TreeMap<Integer, Integer> value = fields.getValue();
						freq_author = value.size();
						term_freq_dict.put(key, freq_author);
						max_freq.add(freq_author);

					}
					Collections.sort(max_freq);
					Collections.reverse(max_freq);

					for (Map.Entry<Integer, Integer> int_fields : term_freq_dict
							.entrySet())

					{

						for (int i = 0; i < k; i++) {

							if (max_freq.get(i).equals(int_fields.getValue())) {
								max_freq_authorid.add(int_fields.getKey());
							}
						}
					}

					for (Map.Entry<String, Integer> fields : placeDict
							.entrySet()) {
						for (int i = 0; i < k; i++) {
							if ((max_freq_authorid).get(i).equals(
									fields.getValue())) {
								maxfreq_author.add(fields.getKey());
							}
						}
					}
				} else
					maxfreq_author = null;
				break;
			}

			default:
				break;

			}
		}
		return maxfreq_author;
	}

	/**
	 * Method to implement a simple boolean AND query on the given index
	 * 
	 * @param terms
	 *            The ordered set of terms to AND, similar to getPostings() the
	 *            terms would be passed through the necessary Analyzer.
	 * @return A Map (if all terms are found) containing FileId as the key and
	 *         number of occurrences as the value, the number of occurrences
	 *         would be the sum of occurrences for each participating term.
	 *         return null if the given term list returns no results BONUS ONLY
	 */
	public Map<String, Integer> query(String... terms) {
		// TODO : BONUS ONLY

		return null;
	}

	public TreeMap<Integer, TreeMap<Integer, Double>> GetFwdIndex(IndexType type) {

		switch (type) {
		case AUTHOR:
			return fwdAuthorIndex;

		case PLACE:
			return fwdPlaceIndex;

		case CATEGORY:
			return fwdCatIndex;

		case TERM:
			return fwdTermIndex;
		}
		return null;
	}

	public TreeMap<Integer, TreeMap<Integer, Double>> GetFwdIndexokapi(IndexType type) {

		switch (type) {
		case AUTHOR:
			return fwdAuthorIndexokapi;

		case PLACE:
			return fwdPlaceIndexokapi;

		case CATEGORY:
			return fwdCatIndexokapi;

		case TERM:
			return fwdTermIndexokapi;
		}
		return null;
	}

	public TreeMap<String, Integer> GetDict(IndexType type) {

		switch (type) {
		case AUTHOR:
			return authorDict;

		case PLACE:
			return placeDict;

		case CATEGORY:
			return catDict;

		case TERM:
			return termDict;

		default:
			return docDict;
		}
	}

	public TreeMap<Integer, TreeMap<Integer, Integer>> GetIndex(IndexType type) {

		switch (type) {
		case AUTHOR:
			return authorIndex;

		case PLACE:
			return placeIndex;

		case CATEGORY:
			return catIndex;

		case TERM:
			return termIndex;
		}
		return null;
	}
}
