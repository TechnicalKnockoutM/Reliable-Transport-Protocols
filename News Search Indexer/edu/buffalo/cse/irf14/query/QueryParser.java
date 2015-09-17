/**
 * 
 */
package edu.buffalo.cse.irf14.query;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.query.Query;

/**
 * @author nikhillo Static parser that converts raw text to Query objects
 */
public class QueryParser {
	/**
	 * MEthod to parse the given user query into a Query object
	 * 
	 * @param userQuery
	 *            : The query to parse
	 * @param defaultOperator
	 *            : The default operator to use, one amongst (AND|OR)
	 * @return Query object if successfully parsed, null otherwise
	 */
	public static Query parse(String userQuery, String defaultOperator) {
		// TODO: YOU MUST IMPLEMENT THIS METHOD

		int count = 1;
//		String userQuery = "(black OR blue) AND bruises";
//		String defaultOperator = "";
		String operator;

		String parsedQuery = "{ ";
		// String tempQuery = "";

		String spaceSplit[] = userQuery.split(" ");
		String spaceSplitStr[] = new String[spaceSplit.length + 3];

		for (int i = 0; i < spaceSplit.length + 3; i++) {
			if (i < spaceSplit.length) {
				spaceSplitStr[i] = spaceSplit[i];
			}
		}

		String indexStr = "Term:";

		Stack<String> stReverse = new Stack<String>();
		Stack<String> stNormal = new Stack<String>();
		Stack<String> stTerms = new Stack<String>();
		Stack<String> stTermsRev = new Stack<String>();

		String tempWhileStr = "";
		String tempStr = "";

		boolean firstTerm = false;
		boolean operatorSet = false;
		boolean otherIndex = false;
		boolean isNotFirst = false;
		boolean setBoxBracket = false;
		boolean bracketOpen = false;

		if (defaultOperator.equals("") || defaultOperator == null) {
			operator = "OR ";
		} else {
			operator = defaultOperator;
		}

		for (int i = 0; i < spaceSplitStr.length; i++) {

			if (spaceSplitStr[i] != null) {
				while (spaceSplitStr[i].endsWith(")") && count != 0) {
					if (spaceSplitStr[i].substring(0,
							spaceSplitStr[i].length() - 1).endsWith(")")) {
						spaceSplitStr[i] = spaceSplitStr[i].substring(0,
								spaceSplitStr[i].length() - 1);
						spaceSplitStr[i + count] = ")";
						++count;
					}

					else {
						count = 0;
					}
				}

				count = 1;

				if (!bracketOpen) {
					if (spaceSplitStr[i].startsWith("Author")) {
						indexStr = "Author:";
						spaceSplitStr[i] = spaceSplitStr[i].replaceAll(
								"Author:", "");
					} else if (spaceSplitStr[i].startsWith("Place")) {
						indexStr = "Place:";
						spaceSplitStr[i] = spaceSplitStr[i].replaceAll(
								"Place:", "");
					}

					else if (spaceSplitStr[i].startsWith("Category")) {
						indexStr = "Category:";
						spaceSplitStr[i] = spaceSplitStr[i].replaceAll(
								"Category:", "");
						otherIndex = true;
					}

					else {
						indexStr = "Term:";
					}
				}
				
				if (!stReverse.empty()) {
					if (stReverse.peek().equals("<")) {
						tempStr = stReverse.pop();

						if (spaceSplitStr[i].endsWith(")")) {
							tempStr = tempStr
									+ indexStr
									+ spaceSplitStr[i].substring(0,
											spaceSplitStr[i].length() - 1);
							tempStr = tempStr + stReverse.pop() + " ]";
							setBoxBracket = true;
							bracketOpen = false;
						}

						else {
							tempStr = tempStr + indexStr + spaceSplitStr[i];
							tempStr = tempStr + stReverse.pop();
						}

						if (stReverse.empty()) {
							parsedQuery = parsedQuery + "AND " + tempStr + " ";
						}

						else {
							stReverse.push("AND");
							stReverse.push(tempStr);

							if (setBoxBracket) {
								while (!(stReverse.empty())
										&& !(stReverse.peek().startsWith("("))) {
									stNormal.push(stReverse.pop());
								}

								if (!stReverse.empty()) {
									stReverse.pop();
								}

								while (!stNormal.empty()) {
									parsedQuery = parsedQuery + stNormal.pop()
											+ " ";
								}

								setBoxBracket = false;
							}
						}
					} else if (spaceSplitStr[i].endsWith("\"")
							|| spaceSplitStr[i].endsWith(")")) {

						tempWhileStr = spaceSplitStr[i]
								.substring(spaceSplitStr[i].length() - 1);

						if (tempWhileStr.equals(")")) {
							tempWhileStr = "(";
							bracketOpen = false;
						} else {
							indexStr = "";
						}
						while (!(stReverse.empty())
								&& !(stReverse.peek().startsWith(tempWhileStr))) {
							stNormal.push(stReverse.pop());
						}

						if (!stReverse.empty()) {
							stReverse.pop();
						}

						if (stReverse.empty()) {
							while (!stNormal.empty()) {
								parsedQuery = parsedQuery + stNormal.pop()
										+ " ";
							}

							if (spaceSplitStr[i].equals(")")) {
								spaceSplitStr[i] = spaceSplitStr[i].replace(
										")", " ]");

								parsedQuery = parsedQuery + spaceSplitStr[i]
										+ " ";

							}

							else {
								spaceSplitStr[i] = spaceSplitStr[i].replace(
										")", " ]");

								parsedQuery = parsedQuery + indexStr
										+ spaceSplitStr[i] + " ";

							}
							firstTerm = true;
						}

						else {
							if (!stNormal.empty()) {

								String temp = "";

								while (!stNormal.empty()) {
									temp = temp + stNormal.pop() + " ";

									// stReverse.push(stNormal.pop());
								}

								if (!temp.equals("")) {
									if (!spaceSplitStr[i].equals(")")) {
										temp = temp
												+ indexStr
												+ spaceSplitStr[i].replace(")",
														" ]");

										stReverse.push(temp);
									}

									else {
										temp = temp
												+ spaceSplitStr[i].replace(")",
														" ]");

										stReverse.push(temp);

									}
								}
							}
						}

					}

					else {

						if (spaceSplitStr[i].startsWith("\"")
								|| spaceSplitStr[i].startsWith("(")) {
							spaceSplitStr[i] = spaceSplitStr[i]
									.replace("(", "");
							stReverse.push("(");
							stReverse.push("[ " + indexStr + spaceSplitStr[i]);
							bracketOpen = true;

						} else if (spaceSplitStr[i].contains("(")) {
							spaceSplitStr[i] = spaceSplitStr[i]
									.replace("(", "");
							stReverse.push("(");
							stReverse.push(indexStr + spaceSplitStr[i]);
							bracketOpen = true;

						} else if (spaceSplitStr[i].equalsIgnoreCase("NOT")) {
							stReverse.push(">");
							stReverse.push("<");
						}

						else {
							if (spaceSplitStr[i].equalsIgnoreCase("OR")
									|| spaceSplitStr[i].equalsIgnoreCase("AND")) {
								stReverse.push(spaceSplitStr[i]);
							}

							else {
								stReverse.push(indexStr + spaceSplitStr[i]);
							}
						}
					}
				} else {
					if (spaceSplitStr[i].startsWith("\"")) {
						stReverse.push(indexStr + spaceSplitStr[i]);

					} else if (spaceSplitStr[i].startsWith("(")) {
						spaceSplitStr[i] = spaceSplitStr[i].replace("(", "");
						stReverse.push("(");
						stReverse.push("[ " + indexStr + spaceSplitStr[i]);
						bracketOpen = true;
						// setBoxBracket = true;
					} else if (spaceSplitStr[i].contains("(")) {
						spaceSplitStr[i] = spaceSplitStr[i].replace("(", "");
						stReverse.push("(");
						stReverse.push("[ " + spaceSplitStr[i]);
						// setBoxBracket = true;
					} else if (spaceSplitStr[i].equalsIgnoreCase("NOT")) {
						stReverse.push(">");
						stReverse.push("<");
					} else if (spaceSplitStr[i].equalsIgnoreCase("AND")) {
						operator = "AND ";
						operatorSet = true;
						parsedQuery = parsedQuery + operator;
						operator = "OR ";
					}

					else if (spaceSplitStr[i].equalsIgnoreCase("OR")) {
						operator = "OR ";
						operatorSet = true;
						parsedQuery = parsedQuery + operator;
						operator = "OR ";
					}

					else if (otherIndex && !(indexStr.equals("Author:"))
							&& !(indexStr.equals("Place:"))
							&& !(indexStr.equals("Category:"))
							&& !(spaceSplitStr[i].equals("AND"))
							&& !(spaceSplitStr[i].equals("OR"))) {
						stTermsRev.push(indexStr + spaceSplitStr[i]);
					}

					else if (firstTerm && !(operatorSet)) {
						parsedQuery = parsedQuery + operator + indexStr
								+ spaceSplitStr[i] + " ";
						operator = "OR ";

					}

					else if (!(spaceSplitStr[i].equals("AND"))
							&& !(spaceSplitStr[i].equals("OR"))) {
						parsedQuery = parsedQuery + indexStr + spaceSplitStr[i]
								+ " ";
						firstTerm = true;
						operator = "OR ";
						operatorSet = false;
					}
				}
			}
		}
		while (!(stTermsRev.empty())) {
			stTerms.push(stTermsRev.pop());
		}

		while (!(stTerms.empty())) {
			if (isNotFirst) {
				parsedQuery = parsedQuery + operator + stTerms.pop() + " ";
			}

			else {
				if (stTerms.peek() != null) {
					parsedQuery = parsedQuery + "[ " + stTerms.pop() + " ";
					isNotFirst = true;
				}

				else {

				}
			}
		}

		if (isNotFirst) {
			parsedQuery = parsedQuery + "] ";
		}

		parsedQuery = parsedQuery + "}";
//		System.out.println(parsedQuery);

		Query query = new Query(parsedQuery);
		return query;
	}



}