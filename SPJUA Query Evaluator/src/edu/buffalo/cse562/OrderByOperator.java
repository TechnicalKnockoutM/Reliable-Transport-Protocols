//
//package edu.buffalo.cse562;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.Function;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.statement.create.table.CreateTable;
//import net.sf.jsqlparser.statement.select.FromItem;
//import net.sf.jsqlparser.statement.select.OrderByElement;
//import net.sf.jsqlparser.statement.select.SelectExpressionItem;
//
//public class OrderByOperator extends Operators {
//
//	List<OrderByElement> orderOfElements;
//
//	public List<OrderByElement> getOrderOfElements() {
//		return orderOfElements;
//	}
//
//	public void setOrderOfElements(List<OrderByElement> orderOfElements) {
//		this.orderOfElements = orderOfElements;
//	}
//
//	public OrderByOperator(HashMap<String, CreateTable> schema) {
//		super(schema);
//		// TODO Auto-generated constructor stub
//	}
//
//	public OrderByOperator(List<OrderByElement> orderByElements,
//			HashMap<String, CreateTable> schema) {
//		// TODO Auto-generated constructor stub
//		super(schema);
//		orderOfElements = orderByElements;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public ArrayList<String> order(List<Expression> project_list,
//			List<Expression> groupby_list, ArrayList<String> tupleset) {
//		ArrayList<String> orderbyElements = new ArrayList<String>();
//		ArrayList<String> groupbyElements = new ArrayList<String>();
//		ArrayList<String> projectListElements = new ArrayList<String>();
//		HashMap<String, String> datatype = new HashMap<String, String>();
//
//		for (int i = 0; i < project_list.size(); i++) {
//			// if (((SelectExpressionItem) project_list.get(i)).getExpression()
//			// instanceof Function)
//
//			// if (((SelectExpressionItem) project_list.get(i)).getExpression()
//			// instanceof Column) {
//
//			projectListElements
//					.add(((SelectExpressionItem) project_list.get(i))
//							.getExpression().toString());
//			// } else {
//
//			// projectListElements.add(((SelectExpressionItem) project_list
//			// .get(i)).getExpression().toString());
//			// }
//
//		}
//		for (int i = 0; i < orderOfElements.size(); i++) {
//			if (projectListElements.contains(((Column) orderOfElements.get(i)
//					.getExpression()).getWholeColumnName())) {
//
//				orderbyElements.add(((Column) orderOfElements.get(i)
//						.getExpression()).getWholeColumnName());
//				datatype.put(((Column) orderOfElements.get(i).getExpression())
//						.getWholeColumnName(), "String");
//
//			} else {
//				// Check if order by has aliases..handle this
//				for (int l = 0; l < project_list.size(); l++) {
//					String alias_name = null;
//					if (((SelectExpressionItem) project_list.get(l))
//							.getExpression() instanceof Column) {
//					} else {
//						alias_name = ((SelectExpressionItem) project_list
//								.get(l)).getAlias();
//						projectListElements.remove(l);
//						projectListElements.add(l, alias_name);
//
//					}
//					if (alias_name == null) {
//
//					} else {
//						if (alias_name.equals(((Column) orderOfElements.get(i)
//								.getExpression()).getWholeColumnName())) {
//							orderbyElements.add(alias_name);
//							datatype.put(alias_name, "Double");
//						}
//					}
//				}
//
//			}
//
//		}
//
//		for (int i = 0; i < groupby_list.size(); i++) {
//
//			Column col = ((Column) groupby_list.get(i));
//			groupbyElements.add(col.getWholeColumnName());
//		}
//
//		if (groupbyElements.containsAll(orderbyElements)) {
//			// if groupby list is equal to order by list
//			ArrayList<String> order = new ArrayList<String>();
//			ArrayList<String> output = new ArrayList<String>();
//			String orderofsorting;
//			boolean asc_flag = false;
//			boolean desc_flag = false;
//			for (int i = 0; i < orderOfElements.size(); i++) {
//				if (orderOfElements.get(i).isAsc()) {
//					order.add("asc");
//					asc_flag = true;
//				} else {
//					order.add("desc");
//					desc_flag = true;
//				}
//			}
//
//			if (desc_flag && asc_flag) {
//				// case when one is ac and other is desc
//
//				// case when one is ac and other is desc
//				TreeMap<String, ArrayList<String>> sorted_list = new TreeMap<String, ArrayList<String>>();
//				Map<String, ArrayList<String>> newMap = null;
//				// for (int j = orderOfElements.size(); j > 0; j--) {
//				if (orderOfElements.size() == 1) {
//					// Only one order by element
//					String orderofcol = null;
//					if (orderOfElements.get(orderOfElements.size() - 1).isAsc()) {
//						orderofcol = "asc";
//					} else {
//						orderofcol = "desc";
//					}
//					for (int i = 0; i < tupleset.size(); i++) {
//						String cols[] = tupleset.get(i).split("//|");
//						int index = projectListElements.indexOf(orderbyElements
//								.get(orderOfElements.size() - 1));
//						if (sorted_list.containsKey(cols[index])) {
//							ArrayList<String> temp = sorted_list
//									.get(cols[index]);
//							temp.add(tupleset.get(i));
//							sorted_list.put(cols[index], temp);
//						} else {
//							ArrayList<String> temp = new ArrayList<String>();
//							temp.add(tupleset.get(i));
//							sorted_list.put(cols[index], temp);
//						}
//
//					}
//
//					if (orderofcol.equals("asc")) {
//						// do nothing. tree map already sorted
//						for (Map.Entry<String, ArrayList<String>> mp : sorted_list
//								.entrySet()) {
//							// String key = mp.getKey();
//							for (String value : mp.getValue()) {
//								output.add(value);
//							}
//						}
//					} else {
//
//						@SuppressWarnings("rawtypes")
//						Map<String, ArrayList<String>> newMap1 = new TreeMap(
//								Collections.reverseOrder());
//						newMap1.putAll(sorted_list);
//						// sorted_list.clear();
//						// sorted_list.putAll(newMap);
//						for (Map.Entry<String, ArrayList<String>> mp : newMap1
//								.entrySet()) {
//							// String key = mp.getKey();
//							for (String value : mp.getValue()) {
//								output.add(value);
//							}
//						}
//
//						// sorted_list1.descendingMap();
//					}
//
//				} else {
//
//					String orderofcol = null;
//					if (orderOfElements.get((orderOfElements.size() - 1))
//							.isAsc()) {
//						orderofcol = "asc";
//					} else {
//						orderofcol = "desc";
//					}
//					for (int i = 0; i < tupleset.size(); i++) {
//						String cols[] = tupleset.get(i).split("\\|");
//						int index = projectListElements.indexOf(orderbyElements
//								.get((orderOfElements.size() - 1)));
//						if (sorted_list.containsKey(cols[index])) {
//							ArrayList<String> temp = sorted_list
//									.get(cols[index]);
//							temp.add(tupleset.get(i));
//							sorted_list.put(cols[index], temp);
//						} else {
//							ArrayList<String> temp = new ArrayList<String>();
//							temp.add(tupleset.get(i));
//							sorted_list.put(cols[index], temp);
//						}
//
//					}
//					TreeMap<String, ArrayList<String>> sorted_list1 = new TreeMap<String, ArrayList<String>>();
//					if (orderofcol.equals("asc")) {
//						// do nothing. tree map already sorted
//						for (Map.Entry<String, ArrayList<String>> mp : sorted_list
//								.entrySet()) {
//							ArrayList<String> val = mp.getValue();
//							for (int k = 0; k < val.size(); k++) {
//								String cols[] = val.get(k).split("\\|");
//								int index = projectListElements
//										.indexOf(orderbyElements.get(0));
//								if (sorted_list1.containsKey(cols[index])) {
//									ArrayList<String> temp = sorted_list1
//											.get(cols[index]);
//									temp.add(val.get(k));
//									sorted_list1.put(cols[index], temp);
//								} else {
//									ArrayList<String> temp = new ArrayList<String>();
//									temp.add(val.get(k));
//									sorted_list1.put(cols[index], temp);
//								}
//							}
//						}
//
//					} else {
//
//						// @SuppressWarnings("rawtypes")
//						newMap = new TreeMap(Collections.reverseOrder());
//						newMap.putAll(sorted_list);
//
//						for (Map.Entry<String, ArrayList<String>> mp : newMap
//								.entrySet()) {
//							ArrayList<String> val = mp.getValue();
//							for (int k = 0; k < val.size(); k++) {
//								String cols[] = val.get(k).split("\\|");
//								int index = projectListElements
//										.indexOf(orderbyElements.get(0));
//								if (sorted_list1.containsKey(cols[index])) {
//									ArrayList<String> temp = sorted_list1
//											.get(cols[index]);
//									temp.add(val.get(k));
//									sorted_list1.put(cols[index], temp);
//								} else {
//									ArrayList<String> temp = new ArrayList<String>();
//									temp.add(val.get(k));
//									sorted_list1.put(cols[index], temp);
//								}
//							}
//						}
//
//					}
//
//					if (orderOfElements.get(0).isAsc()) {
//						orderofcol = "asc";
//
//					} else {
//						orderofcol = "desc";
//
//					}
//					if (orderofcol.equals("asc")) {
//						// do nothing. tree map already sorted
//						for (Map.Entry<String, ArrayList<String>> mp : sorted_list1
//								.entrySet()) {
//							// String key = mp.getKey();
//							for (String value : mp.getValue()) {
//								output.add(value);
//							}
//						}
//					} else {
//
//						@SuppressWarnings("rawtypes")
//						Map<String, ArrayList<String>> newMap1 = new TreeMap(
//								Collections.reverseOrder());
//						newMap1.putAll(sorted_list1);
//						// sorted_list.clear();
//						// sorted_list.putAll(newMap);
//						for (Map.Entry<String, ArrayList<String>> mp : newMap1
//								.entrySet()) {
//							// String key = mp.getKey();
//							for (String value : mp.getValue()) {
//								output.add(value);
//							}
//						}
//
//						// sorted_list1.descendingMap();
//					}
//
//				}
//
//				// }
//
//			} else {
//				orderofsorting = order.get(0);
//				if (orderofsorting.equals("asc")) {
//					// order of sorting is asc
//					// System.out.println(1);
//
//				} else {
//					// order of sorting is desc
//					Collections.reverse(tupleset);
//
//				}
//
//			}
//			if (desc_flag && asc_flag)
//				return output;
//			else
//				return tupleset;
//		} else {
//			// if groupby list is NOT equal to order by list
//			ArrayList<String> output = new ArrayList<String>();
//			ArrayList<String> order = new ArrayList<String>();
//			String orderofsorting;
//			boolean asc_flag = false;
//			boolean desc_flag = false;
//			for (int i = 0; i < orderOfElements.size(); i++) {
//				if (orderOfElements.get(i).isAsc()) {
//					order.add("asc");
//					asc_flag = true;
//				} else {
//					order.add("desc");
//					desc_flag = true;
//				}
//			}
//			if (desc_flag && asc_flag) {
//				// case when one is ac and other is desc
//
//				// case when one is ac and other is desc
//				TreeMap<String, ArrayList<String>> sorted_list_string = new TreeMap<String, ArrayList<String>>();
//				TreeMap<Double, ArrayList<String>> sorted_list_double = new TreeMap<Double, ArrayList<String>>();
//				TreeMap<String, ArrayList<String>> sorted_list1_string = new TreeMap<String, ArrayList<String>>();
//				TreeMap<Double, ArrayList<String>> sorted_list1_double = new TreeMap<Double, ArrayList<String>>();
//				Map<String, ArrayList<String>> newMap_string = null;
//				Map<Double, ArrayList<String>> newMap_double = null;
//
//				// for (int j = orderOfElements.size(); j > 0; j--) {
//				if (orderOfElements.size() == 1) {
//					// Only one order by element
//					String datatype_flag;
//					if (datatype.get(orderbyElements.get(0)).equals("String")) {
//
//						datatype_flag = "String";
//					} else {
//						
//						for(int i=0; i < orderOfElements.size(); i++)
//						{
//							System.out.println("orderOfElements.get(i)" + orderOfElements.size());
//							System.out.println("orderbyElements.get(i)" + orderbyElements.size());
//							System.out.println("datatype.get(i)" + datatype.size());
//						}
//
//
//						datatype_flag = "Double";
//					}
//					String orderofcol = null;
//					if (orderOfElements.get(orderOfElements.size() - 1).isAsc()) {
//						orderofcol = "asc";
//					} else {
//						orderofcol = "desc";
//					}
//					if (datatype_flag.equals("String")) {
//						// if datatype is string
//						TreeMap<String, ArrayList<String>> sorted_list = new TreeMap<String, ArrayList<String>>();
//						for (int i = 0; i < tupleset.size(); i++) {
//							String cols[] = tupleset.get(i).split("//|");
//							int index = projectListElements
//									.indexOf(orderbyElements
//											.get(orderOfElements.size() - 1));
//							if (sorted_list.containsKey(cols[index])) {
//								ArrayList<String> temp = sorted_list
//										.get(cols[index]);
//								temp.add(tupleset.get(i));
//								sorted_list.put(cols[index], temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(tupleset.get(i));
//								sorted_list.put(cols[index], temp);
//							}
//
//						}
//						sorted_list_string.putAll(sorted_list);
//					} else {
//						// if datatype is double
//						TreeMap<Double, ArrayList<String>> sorted_list = new TreeMap<Double, ArrayList<String>>();
//						for (int i = 0; i < tupleset.size(); i++) {
//							String cols[] = tupleset.get(i).split("//|");
//							int index = projectListElements
//									.indexOf(orderbyElements
//											.get(orderOfElements.size() - 1));
//							if (sorted_list.containsKey(Double
//									.parseDouble(cols[index]))) {
//								ArrayList<String> temp = sorted_list.get(Double
//										.parseDouble(cols[index]));
//								temp.add(tupleset.get(i));
//								sorted_list.put(
//										Double.parseDouble(cols[index]), temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(tupleset.get(i));
//								sorted_list.put(
//										Double.parseDouble(cols[index]), temp);
//							}
//
//						}
//						sorted_list_double.putAll(sorted_list);
//					}
//
//					if (orderofcol.equals("asc")) {
//						// do nothing. tree map already sorted
//						if (datatype_flag.equals("String")) {
//							for (Map.Entry<String, ArrayList<String>> mp : sorted_list_string
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//						} else {
//
//							for (Map.Entry<Double, ArrayList<String>> mp : sorted_list_double
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						}
//
//					} else {
//						// Desc order
//
//						if (datatype_flag.equals("String")) {
//							Map<String, ArrayList<String>> newMap1 = new TreeMap(
//									Collections.reverseOrder());
//							newMap1.putAll(sorted_list_string);
//							// sorted_list.clear();
//							// sorted_list.putAll(newMap);
//							for (Map.Entry<String, ArrayList<String>> mp : newMap1
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						} else {
//
//							Map<Double, ArrayList<String>> newMap1 = new TreeMap(
//									Collections.reverseOrder());
//							newMap1.putAll(sorted_list_double);
//							// sorted_list.clear();
//							// sorted_list.putAll(newMap);
//							for (Map.Entry<Double, ArrayList<String>> mp : newMap1
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						}
//
//						// sorted_list1.descendingMap();
//					}
//
//				} else {
//
//					
//					for(int i=0; i < orderOfElements.size(); i++)
//					{
//						System.out.println("orderOfElements.get(i)" + orderOfElements.size());
//						System.out.println("orderbyElements.get(i)" + orderbyElements.size());
//						System.out.println("datatype.get(i)" + datatype.size());
//					}
//					
//					String datatype_flag;
//					if (datatype.get(
//							orderbyElements.get((orderOfElements.size() - 1)))
//							.equals("String")) {
//
//						datatype_flag = "String";
//					} else {
//
//						datatype_flag = "Double";
//					}
//					String orderofcol = null;
//					if (orderOfElements.get((orderOfElements.size() - 1))
//							.isAsc()) {
//						orderofcol = "asc";
//					} else {
//						orderofcol = "desc";
//					}
//
//					if (datatype_flag.equals("String")) {
//						TreeMap<String, ArrayList<String>> sorted_list = new TreeMap<String, ArrayList<String>>();
//						for (int i = 0; i < tupleset.size(); i++) {
//							String cols[] = tupleset.get(i).split("\\|");
//							int index = projectListElements
//									.indexOf(orderbyElements
//											.get((orderOfElements.size() - 1)));
//							if (sorted_list.containsKey(cols[index])) {
//								ArrayList<String> temp = sorted_list
//										.get(cols[index]);
//								temp.add(tupleset.get(i));
//								sorted_list.put(cols[index], temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(tupleset.get(i));
//								sorted_list.put(cols[index], temp);
//							}
//
//						}
//						sorted_list_string.putAll(sorted_list);
//					} else {
//
//						TreeMap<Double, ArrayList<String>> sorted_list = new TreeMap<Double, ArrayList<String>>();
//						for (int i = 0; i < tupleset.size(); i++) {
//							String cols[] = tupleset.get(i).split("//|");
//							int index = projectListElements
//									.indexOf(orderbyElements
//											.get(orderOfElements.size() - 1));
//							if (sorted_list.containsKey(Double
//									.parseDouble(cols[index]))) {
//								ArrayList<String> temp = sorted_list.get(Double
//										.parseDouble(cols[index]));
//								temp.add(tupleset.get(i));
//								sorted_list.put(
//										Double.parseDouble(cols[index]), temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(tupleset.get(i));
//								sorted_list.put(
//										Double.parseDouble(cols[index]), temp);
//							}
//
//						}
//						sorted_list_double.putAll(sorted_list);
//
//					}
//					if (datatype.get(orderbyElements.get(0)).equals("String")) {
//
//						datatype_flag = "String";
//					} else {
//
//						datatype_flag = "Double";
//					}
//					// if (orderOfElements.get(0).isAsc()) {
//					// orderofcol = "asc";
//					//
//					//
//					// } else {
//					// orderofcol = "desc";
//					//
//					// }
//
//					if (datatype_flag.equals("String")) {
//						// Datatype is string
//						// Check if previous orderby element is string or double
//						TreeMap<String, ArrayList<String>> sorted_list1 = new TreeMap<String, ArrayList<String>>();
//						if (orderofcol.equals("asc")) {
//							// do nothing. tree map already sorted
//							for (Map.Entry<String, ArrayList<String>> mp : sorted_list_string
//									.entrySet()) {
//								ArrayList<String> val = mp.getValue();
//								for (int k = 0; k < val.size(); k++) {
//									String cols[] = val.get(k).split("\\|");
//									int index = projectListElements
//											.indexOf(orderbyElements.get(0));
//									if (sorted_list1.containsKey(cols[index])) {
//										ArrayList<String> temp = sorted_list1
//												.get(cols[index]);
//										temp.add(val.get(k));
//										sorted_list1.put(cols[index], temp);
//									} else {
//										ArrayList<String> temp = new ArrayList<String>();
//										temp.add(val.get(k));
//										sorted_list1.put(cols[index], temp);
//									}
//								}
//							}
//							sorted_list1_string.putAll(sorted_list1);
//
//						} else {
//							// order is desc
//							// @SuppressWarnings("rawtypes")
//							newMap_string = new TreeMap(
//									Collections.reverseOrder());
//							newMap_string.putAll(sorted_list_string);
//
//							for (Map.Entry<String, ArrayList<String>> mp : newMap_string
//									.entrySet()) {
//								ArrayList<String> val = mp.getValue();
//								for (int k = 0; k < val.size(); k++) {
//									String cols[] = val.get(k).split("\\|");
//									int index = projectListElements
//											.indexOf(orderbyElements.get(0));
//									if (sorted_list1.containsKey(cols[index])) {
//										ArrayList<String> temp = sorted_list1
//												.get(cols[index]);
//										temp.add(val.get(k));
//										sorted_list1.put(cols[index], temp);
//									} else {
//										ArrayList<String> temp = new ArrayList<String>();
//										temp.add(val.get(k));
//										sorted_list1.put(cols[index], temp);
//									}
//								}
//							}
//							// Collections.reverse(sorted_list1);
//
//							sorted_list1_string.putAll(sorted_list1);
//						}
//
//					} else {
//						// Datatype is Double
//						TreeMap<Double, ArrayList<String>> sorted_list1 = new TreeMap<Double, ArrayList<String>>();
//						if (orderofcol.equals("asc")) {
//							// do nothing. tree map already sorted
//							for (Map.Entry<String, ArrayList<String>> mp : sorted_list_string// for
//																								// time
//																								// being
//									.entrySet()) {
//								ArrayList<String> val = mp.getValue();
//								for (int k = 0; k < val.size(); k++) {
//									String cols[] = val.get(k).split("\\|");
//									int index = projectListElements
//											.indexOf(orderbyElements.get(0));
//									if (sorted_list1.containsKey(Double
//											.parseDouble(cols[index]))) {
//										ArrayList<String> temp = sorted_list1
//												.get(Double
//														.parseDouble(cols[index]));
//										temp.add(val.get(k));
//										sorted_list1
//												.put(Double
//														.parseDouble(cols[index]),
//														temp);
//									} else {
//										ArrayList<String> temp = new ArrayList<String>();
//										temp.add(val.get(k));
//										sorted_list1
//												.put(Double
//														.parseDouble(cols[index]),
//														temp);
//									}
//								}
//							}
//							sorted_list1_double.putAll(sorted_list1);
//
//						} else {
//							// Order is desc
//							// @SuppressWarnings("rawtypes")
//							newMap_string = new TreeMap(
//									Collections.reverseOrder());// for time
//																// being
//							newMap_string.putAll(sorted_list_string);
//
//							for (Map.Entry<String, ArrayList<String>> mp : newMap_string
//									.entrySet()) {
//								ArrayList<String> val = mp.getValue();
//								for (int k = 0; k < val.size(); k++) {
//									String cols[] = val.get(k).split("\\|");
//									int index = projectListElements
//											.indexOf(orderbyElements.get(0));
//									if (sorted_list1.containsKey(Double
//											.parseDouble(cols[index]))) {
//										ArrayList<String> temp = sorted_list1
//												.get(Double
//														.parseDouble(cols[index]));
//										temp.add(val.get(k));
//										sorted_list1
//												.put(Double
//														.parseDouble(cols[index]),
//														temp);
//									} else {
//										ArrayList<String> temp = new ArrayList<String>();
//										temp.add(val.get(k));
//										sorted_list1
//												.put(Double
//														.parseDouble(cols[index]),
//														temp);
//									}
//								}
//							}
//							// Collections.reverse(sorted_list1);
//							sorted_list1_double.putAll(sorted_list1);
//
//						}
//
//					}
//					if (orderOfElements.get(0).isAsc()) {
//						orderofcol = "asc";
//
//					} else {
//						orderofcol = "desc";
//
//					}
//					if (orderofcol.equals("asc")) {
//						// do nothing. tree map already sorted
//						if (datatype_flag.equals("String")) {
//							for (Map.Entry<String, ArrayList<String>> mp : sorted_list1_string
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//						} else {
//
//							for (Map.Entry<Double, ArrayList<String>> mp : sorted_list1_double
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						}
//
//					} else {
//
//						if (datatype_flag.equals("String")) {
//							Map<String, ArrayList<String>> newMap1 = new TreeMap(
//									Collections.reverseOrder());
//							newMap1.putAll(sorted_list1_string);
//							// sorted_list.clear();
//							// sorted_list.putAll(newMap);
//							for (Map.Entry<String, ArrayList<String>> mp : newMap1
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//						} else {
//
//							Map<Double, ArrayList<String>> newMap1 = new TreeMap(
//									Collections.reverseOrder());
//							newMap1.putAll(sorted_list1_double);
//							// sorted_list.clear();
//							// sorted_list.putAll(newMap);
//							for (Map.Entry<Double, ArrayList<String>> mp : newMap1
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						}
//
//						// sorted_list1.descendingMap();
//					}
//
//				}
//
//				// }
//
//			} else {
//				// case when one is ac and other is desc
//
//				// case when one is ac and other is desc
//				TreeMap<String, ArrayList<String>> sorted_list_string = new TreeMap<String, ArrayList<String>>();
//				TreeMap<Double, ArrayList<String>> sorted_list_double = new TreeMap<Double, ArrayList<String>>();
//				TreeMap<String, ArrayList<String>> sorted_list1_string = new TreeMap<String, ArrayList<String>>();
//				TreeMap<Double, ArrayList<String>> sorted_list1_double = new TreeMap<Double, ArrayList<String>>();
//				Map<String, ArrayList<String>> newMap_string = null;
//				Map<Double, ArrayList<String>> newMap_double = null;
//
//				// for (int j = orderOfElements.size(); j > 0; j--) {
//				if (orderOfElements.size() == 1) {
//					// Only one order by element
//					String datatype_flag;
//					if (datatype.get(orderbyElements.get(0)).equals("String")) {
//
//						datatype_flag = "String";
//					} else {
//
//						datatype_flag = "Double";
//					}
//					String orderofcol = null;
//					// if (orderOfElements.get(orderOfElements.size()).isAsc())
//					// {
//					// orderofcol = "asc";
//					// } else {
//					// orderofcol = "desc";
//					// }
//					orderofcol = order.get(0);
//					if (datatype_flag.equals("String")) {
//						// if datatype is string
//						TreeMap<String, ArrayList<String>> sorted_list = new TreeMap<String, ArrayList<String>>();
//						for (int i = 0; i < tupleset.size(); i++) {
//							String cols[] = tupleset.get(i).split("//|");
//							int index = projectListElements
//									.indexOf(orderbyElements
//											.get(orderOfElements.size() - 1));
//							if (sorted_list.containsKey(cols[index])) {
//								ArrayList<String> temp = sorted_list
//										.get(cols[index]);
//								temp.add(tupleset.get(i));
//								sorted_list.put(cols[index], temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(tupleset.get(i));
//								sorted_list.put(cols[index], temp);
//							}
//
//						}
//						sorted_list_string.putAll(sorted_list);
//					} else {
//						// if datatype is double
//						TreeMap<Double, ArrayList<String>> sorted_list = new TreeMap<Double, ArrayList<String>>();
//						for (int i = 0; i < tupleset.size(); i++) {
//							String cols[] = tupleset.get(i).split("//|");
//							int index = projectListElements
//									.indexOf(orderbyElements
//											.get(orderOfElements.size() - 1));
//							if (sorted_list.containsKey(Double
//									.parseDouble(cols[index]))) {
//								ArrayList<String> temp = sorted_list.get(Double
//										.parseDouble(cols[index]));
//								temp.add(tupleset.get(i));
//								sorted_list.put(
//										Double.parseDouble(cols[index]), temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(tupleset.get(i));
//								sorted_list.put(
//										Double.parseDouble(cols[index]), temp);
//							}
//
//						}
//						sorted_list_double.putAll(sorted_list);
//					}
//
//					if (orderofcol.equals("asc")) {
//						// do nothing. tree map already sorted
//						if (datatype_flag.equals("String")) {
//							for (Map.Entry<String, ArrayList<String>> mp : sorted_list_string
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//						} else {
//
//							for (Map.Entry<Double, ArrayList<String>> mp : sorted_list_double
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						}
//
//					} else {
//						// Desc order
//
//						if (datatype_flag.equals("String")) {
//							Map<String, ArrayList<String>> newMap1 = new TreeMap(
//									Collections.reverseOrder());
//							newMap1.putAll(sorted_list_string);
//							// sorted_list.clear();
//							// sorted_list.putAll(newMap);
//							for (Map.Entry<String, ArrayList<String>> mp : newMap1
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						} else {
//
//							Map<Double, ArrayList<String>> newMap1 = new TreeMap(
//									Collections.reverseOrder());
//							newMap1.putAll(sorted_list_double);
//							// sorted_list.clear();
//							// sorted_list.putAll(newMap);
//							for (Map.Entry<Double, ArrayList<String>> mp : newMap1
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						}
//
//						// sorted_list1.descendingMap();
//					}
//
//				} else {
//
//					String datatype_flag;
//					if (datatype.get(
//							orderbyElements.get((orderOfElements.size() - 1)))
//							.equals("String")) {
//
//						datatype_flag = "String";
//					} else {
//
//						datatype_flag = "Double";
//					}
//					String orderofcol = null;
//					// if
//					// (orderOfElements.get((orderOfElements.size()-1)).isAsc())
//					// {
//					// orderofcol = "asc";
//					// } else {
//					// orderofcol = "desc";
//					// }
//					orderofcol = order.get(0);
//					if (datatype_flag.equals("String")) {
//						TreeMap<String, ArrayList<String>> sorted_list = new TreeMap<String, ArrayList<String>>();
//						for (int i = 0; i < tupleset.size(); i++) {
//							String cols[] = tupleset.get(i).split("\\|");
//							int index = projectListElements
//									.indexOf(orderbyElements
//											.get((orderOfElements.size() - 1)));
//							if (sorted_list.containsKey(cols[index])) {
//								ArrayList<String> temp = sorted_list
//										.get(cols[index]);
//								temp.add(tupleset.get(i));
//								sorted_list.put(cols[index], temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(tupleset.get(i));
//								sorted_list.put(cols[index], temp);
//							}
//
//						}
//						sorted_list_string.putAll(sorted_list);
//					} else {
//
//						TreeMap<Double, ArrayList<String>> sorted_list = new TreeMap<Double, ArrayList<String>>();
//						for (int i = 0; i < tupleset.size(); i++) {
//							String cols[] = tupleset.get(i).split("//|");
//							int index = projectListElements
//									.indexOf(orderbyElements
//											.get(orderOfElements.size() - 1));
//							if (sorted_list.containsKey(Double
//									.parseDouble(cols[index]))) {
//								ArrayList<String> temp = sorted_list.get(Double
//										.parseDouble(cols[index]));
//								temp.add(tupleset.get(i));
//								sorted_list.put(
//										Double.parseDouble(cols[index]), temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(tupleset.get(i));
//								sorted_list.put(
//										Double.parseDouble(cols[index]), temp);
//							}
//
//						}
//						sorted_list_double.putAll(sorted_list);
//
//					}
//					if (datatype.get(orderbyElements.get(0)).equals("String")) {
//
//						datatype_flag = "String";
//					} else {
//
//						datatype_flag = "Double";
//					}
//					// if (orderOfElements.get(0).isAsc()) {
//					// orderofcol = "asc";
//					//
//					//
//					// } else {
//					// orderofcol = "desc";
//					//
//					// }
//
//					if (datatype_flag.equals("String")) {
//						// Datatype is string
//						// Check if previous orderby element is string or double
//						TreeMap<String, ArrayList<String>> sorted_list1 = new TreeMap<String, ArrayList<String>>();
//						if (orderofcol.equals("asc")) {
//							// do nothing. tree map already sorted
//							for (Map.Entry<String, ArrayList<String>> mp : sorted_list_string
//									.entrySet()) {
//								ArrayList<String> val = mp.getValue();
//								for (int k = 0; k < val.size(); k++) {
//									String cols[] = val.get(k).split("\\|");
//									int index = projectListElements
//											.indexOf(orderbyElements.get(0));
//									if (sorted_list1.containsKey(cols[index])) {
//										ArrayList<String> temp = sorted_list1
//												.get(cols[index]);
//										temp.add(val.get(k));
//										sorted_list1.put(cols[index], temp);
//									} else {
//										ArrayList<String> temp = new ArrayList<String>();
//										temp.add(val.get(k));
//										sorted_list1.put(cols[index], temp);
//									}
//								}
//							}
//							sorted_list1_string.putAll(sorted_list1);
//
//						} else {
//							// order is desc
//							// @SuppressWarnings("rawtypes")
//							newMap_string = new TreeMap(
//									Collections.reverseOrder());
//							newMap_string.putAll(sorted_list_string);
//
//							for (Map.Entry<String, ArrayList<String>> mp : newMap_string
//									.entrySet()) {
//								ArrayList<String> val = mp.getValue();
//								for (int k = 0; k < val.size(); k++) {
//									String cols[] = val.get(k).split("\\|");
//									int index = projectListElements
//											.indexOf(orderbyElements.get(0));
//									if (sorted_list1.containsKey(cols[index])) {
//										ArrayList<String> temp = sorted_list1
//												.get(cols[index]);
//										temp.add(val.get(k));
//										sorted_list1.put(cols[index], temp);
//									} else {
//										ArrayList<String> temp = new ArrayList<String>();
//										temp.add(val.get(k));
//										sorted_list1.put(cols[index], temp);
//									}
//								}
//							}
//							// Collections.reverse(sorted_list1);
//
//							sorted_list1_string.putAll(sorted_list1);
//						}
//
//					} else {
//						// Datatype is Double
//						TreeMap<Double, ArrayList<String>> sorted_list1 = new TreeMap<Double, ArrayList<String>>();
//						if (orderofcol.equals("asc")) {
//							// do nothing. tree map already sorted
//							for (Map.Entry<String, ArrayList<String>> mp : sorted_list_string// for
//																								// time
//																								// being
//									.entrySet()) {
//								ArrayList<String> val = mp.getValue();
//								for (int k = 0; k < val.size(); k++) {
//									String cols[] = val.get(k).split("\\|");
//									int index = projectListElements
//											.indexOf(orderbyElements.get(0));
//									if (sorted_list1.containsKey(Double
//											.parseDouble(cols[index]))) {
//										ArrayList<String> temp = sorted_list1
//												.get(Double
//														.parseDouble(cols[index]));
//										temp.add(val.get(k));
//										sorted_list1
//												.put(Double
//														.parseDouble(cols[index]),
//														temp);
//									} else {
//										ArrayList<String> temp = new ArrayList<String>();
//										temp.add(val.get(k));
//										sorted_list1
//												.put(Double
//														.parseDouble(cols[index]),
//														temp);
//									}
//								}
//							}
//							sorted_list1_double.putAll(sorted_list1);
//
//						} else {
//							// Order is desc
//							// @SuppressWarnings("rawtypes")
//							newMap_string = new TreeMap(
//									Collections.reverseOrder());// for time
//																// being
//							newMap_string.putAll(sorted_list_string);
//
//							for (Map.Entry<String, ArrayList<String>> mp : newMap_string
//									.entrySet()) {
//								ArrayList<String> val = mp.getValue();
//								for (int k = 0; k < val.size(); k++) {
//									String cols[] = val.get(k).split("\\|");
//									int index = projectListElements
//											.indexOf(orderbyElements.get(0));
//									if (sorted_list1.containsKey(Double
//											.parseDouble(cols[index]))) {
//										ArrayList<String> temp = sorted_list1
//												.get(Double
//														.parseDouble(cols[index]));
//										temp.add(val.get(k));
//										sorted_list1
//												.put(Double
//														.parseDouble(cols[index]),
//														temp);
//									} else {
//										ArrayList<String> temp = new ArrayList<String>();
//										temp.add(val.get(k));
//										sorted_list1
//												.put(Double
//														.parseDouble(cols[index]),
//														temp);
//									}
//								}
//							}
//							// Collections.reverse(sorted_list1);
//							sorted_list1_double.putAll(sorted_list1);
//
//						}
//
//					}
//					// if (orderOfElements.get(0).isAsc()) {
//					// orderofcol = "asc";
//					//
//					//
//					// } else {
//					// orderofcol = "desc";
//					//
//					// }
//					if (orderofcol.equals("asc")) {
//						// do nothing. tree map already sorted
//						if (datatype_flag.equals("String")) {
//							for (Map.Entry<String, ArrayList<String>> mp : sorted_list1_string
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//						} else {
//
//							for (Map.Entry<Double, ArrayList<String>> mp : sorted_list1_double
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						}
//
//					} else {
//
//						if (datatype_flag.equals("String")) {
//							Map<String, ArrayList<String>> newMap1 = new TreeMap(
//									Collections.reverseOrder());
//							newMap1.putAll(sorted_list1_string);
//							// sorted_list.clear();
//							// sorted_list.putAll(newMap);
//							for (Map.Entry<String, ArrayList<String>> mp : newMap1
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//						} else {
//
//							Map<Double, ArrayList<String>> newMap1 = new TreeMap(
//									Collections.reverseOrder());
//							newMap1.putAll(sorted_list1_double);
//							// sorted_list.clear();
//							// sorted_list.putAll(newMap);
//							for (Map.Entry<Double, ArrayList<String>> mp : newMap1
//									.entrySet()) {
//								// String key = mp.getKey();
//								for (String value : mp.getValue()) {
//									output.add(value);
//								}
//							}
//
//						}
//
//						// sorted_list1.descendingMap();
//					}
//
//				}
//
//				// }
//
//			}
//			return output;
//		}
//		// return null;
//	}
//
//	public void setTableNames() {
//		// TODO Auto-generated method stub
//		for (int i = 0; i < orderOfElements.size(); i++) {
//			if (orderOfElements.get(i).getExpression() instanceof Column) {
//				Column col = ((Column) orderOfElements.get(i).getExpression());
//				if (col.getTable().getName() == null) {
//					if (getSchema().size() == 1) {
//						for (Map.Entry<String, CreateTable> mp : getSchema()
//								.entrySet())
//							col.getTable().setName(mp.getKey());
//					} else {
//						// do nothing since it is an alias
//					}
//				} else {
//					col.getTable().setName(
//							(col.getTable().getName().toUpperCase()));
//				}
//			}
//		}
//	}
//}

package edu.buffalo.cse562;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class OrderByOperator extends Operators {

	List<OrderByElement> orderOfElements;

	public List<OrderByElement> getOrderOfElements() {
		return orderOfElements;
	}

	public void setOrderOfElements(List<OrderByElement> orderOfElements) {
		this.orderOfElements = orderOfElements;
	}

	public OrderByOperator(HashMap<String, CreateTable> schema) {
		super(schema);
		// TODO Auto-generated constructor stub
	}

	public OrderByOperator(List<OrderByElement> orderByElements,
			HashMap<String, CreateTable> schema) {
		// TODO Auto-generated constructor stub
		super(schema);
		orderOfElements = orderByElements;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<String> order(List<Expression> project_list,
			List<Expression> groupby_list, ArrayList<String> tupleset) {
		ArrayList<String> orderbyElements = new ArrayList<String>();
		ArrayList<String> groupbyElements = new ArrayList<String>();
		ArrayList<String> projectListElements = new ArrayList<String>();
		HashMap<String, String> datatype = new HashMap<String, String>();

		for (int i = 0; i < project_list.size(); i++) {

			projectListElements
					.add(((SelectExpressionItem) project_list.get(i))
							.getExpression().toString());

		}
		for (int i = 0; i < orderOfElements.size(); i++) {
			if (projectListElements.contains(((Column) orderOfElements.get(i)
					.getExpression()).getWholeColumnName())) {

				orderbyElements.add(((Column) orderOfElements.get(i)
						.getExpression()).getWholeColumnName());
				datatype.put(((Column) orderOfElements.get(i).getExpression())
						.getWholeColumnName(), "String");

			} else {
				// Check if order by has aliases..handle this
				for (int l = 0; l < project_list.size(); l++) {
					String alias_name = null;
					if (((SelectExpressionItem) project_list.get(l))
							.getExpression() instanceof Column) {
					} else {
						alias_name = ((SelectExpressionItem) project_list
								.get(l)).getAlias();
						projectListElements.remove(l);
						projectListElements.add(l, alias_name);

					}
					if (alias_name == null) {

					} else {
						if (alias_name.equals(((Column) orderOfElements.get(i)
								.getExpression()).getWholeColumnName())) {
							orderbyElements.add(alias_name);
							datatype.put(alias_name, "Double");
						}
					}
				}

			}

		}
		// New code
		TreeMap<String, ArrayList<String>> sorted_list_string = new TreeMap<String, ArrayList<String>>();
		TreeMap<Double, ArrayList<String>> sorted_list_double = new TreeMap<Double, ArrayList<String>>();
		ArrayList<String> temp_tupleset = new ArrayList<String>();
		ArrayList<String> output = new ArrayList<String>();
		temp_tupleset.addAll(tupleset);
		String datatype_col = null;
		for (int i = orderbyElements.size() - 1; i >= 0; i--) {
			datatype_col = datatype.get(orderbyElements.get(i));
			if (datatype_col.equals("String")) {

				for (int j = 0; j < temp_tupleset.size(); j++) {
					String cols[] = temp_tupleset.get(j).split("\\|");
					int index = projectListElements.indexOf(orderbyElements
							.get(i));
					if (sorted_list_string.containsKey(cols[index])) {
						ArrayList<String> temp = sorted_list_string
								.get(cols[index]);
						temp.add(temp_tupleset.get(j));
						sorted_list_string.put(cols[index], temp);
					} else {
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(temp_tupleset.get(j));
						sorted_list_string.put(cols[index], temp);
					}
				}
				output.clear();
				for (Map.Entry<String, ArrayList<String>> mp : sorted_list_string
						.entrySet()) {
					output.addAll(mp.getValue());
				}
			} else {
				// datatype is Double

				for (int j = 0; j < temp_tupleset.size(); j++) {
					String cols[] = temp_tupleset.get(j).split("\\|");
					int index = projectListElements.indexOf(orderbyElements
							.get(i));
					if (sorted_list_double.containsKey(Double
							.parseDouble(cols[index]))) {
						ArrayList<String> temp = sorted_list_double
								.get(Double.parseDouble(cols[index]));
						temp.add(temp_tupleset.get(j));
						sorted_list_double.put(Double.parseDouble(cols[index]),
								temp);
					} else {
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(temp_tupleset.get(j));
						sorted_list_double.put(Double.parseDouble(cols[index]),
								temp);
					}
				}

				output.clear();
				for (Map.Entry<Double, ArrayList<String>> mp : sorted_list_double
						.entrySet()) {
					output.addAll(mp.getValue());
				}

			}

			if (orderOfElements.get(i).isAsc()) {

				temp_tupleset.clear();
				temp_tupleset.addAll(output);
				// return output;
			} else {
				output.clear();
				if (datatype_col.equals("String")) {
					Map<String, ArrayList<String>> newMap1 = new TreeMap(
							Collections.reverseOrder());
					newMap1.putAll(sorted_list_string);
					// sorted_list.clear();
					// sorted_list.putAll(newMap);
					for (Map.Entry<String, ArrayList<String>> mp : newMap1
							.entrySet()) {
						// String key = mp.getKey();
						for (String value : mp.getValue()) {
							output.add(value);
						}
					}
					temp_tupleset.clear();
					temp_tupleset.addAll(output);
				} else {
					Map<Double, ArrayList<String>> newMap1 = new TreeMap(
							Collections.reverseOrder());
					newMap1.putAll(sorted_list_double);
					// sorted_list.clear();
					// sorted_list.putAll(newMap);
					for (Map.Entry<Double, ArrayList<String>> mp : newMap1
							.entrySet()) {
						// String key = mp.getKey();
						for (String value : mp.getValue()) {
							output.add(value);
						}
					}
					temp_tupleset.clear();
					temp_tupleset.addAll(output);
				}

			}
			//
			sorted_list_double.clear();
			sorted_list_string.clear();
		}
		return temp_tupleset;

	}

	public void setTableNames() {
		// TODO Auto-generated method stub
		for (int i = 0; i < orderOfElements.size(); i++) {
			if (orderOfElements.get(i).getExpression() instanceof Column) {
				Column col = ((Column) orderOfElements.get(i).getExpression());
				if (col.getTable().getName() == null) {
					if (getSchema().size() == 1) {
						for (Map.Entry<String, CreateTable> mp : getSchema()
								.entrySet())
							col.getTable().setName(mp.getKey());
					} else {
						// do nothing since it is an alias
					}
				} else {
					col.getTable().setName(
							(col.getTable().getName().toUpperCase()));
				}
			}
		}
	}
}
