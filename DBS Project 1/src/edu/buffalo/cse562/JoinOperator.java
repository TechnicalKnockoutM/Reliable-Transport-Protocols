package edu.buffalo.cse562;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.FromItem;

public class JoinOperator extends Operators {

	private List<FromItem> tables = null;
	private String stringTuple;

	private List<Operators> joinOperators = new ArrayList<Operators>();
	private List<Table> relationTablesList = new ArrayList<Table>();
	private Expression joinCondition = null;
	HashMap<String, String> dataType = null;

	TreeMap<Integer, ArrayList<String>> merge_join_output = new TreeMap<Integer, ArrayList<String>>();
	TreeMap<Integer, ArrayList<String>> merge_join_output_file = new TreeMap<Integer, ArrayList<String>>();

	public String getStringTuple() {
		return stringTuple;
	}

	public void setStringTuple(String stringTuple) {
		this.stringTuple = stringTuple;
	}

	public List<Operators> getJoinOperators() {
		return joinOperators;
	}

	public void setJoinOperators(List<Operators> joinOperators) {
		this.joinOperators = joinOperators;
	}

	public Expression getJoinCondition() {
		return joinCondition;
	}

	public void setJoinCondition(Expression joinCondition) {
		this.joinCondition = joinCondition;
	}

	public List<Table> getRelationTableList() {
		return relationTablesList;
	}

	public void setRelationTableList(List<Table> relationTableList) {
		this.relationTablesList = relationTableList;
	}

	public JoinOperator() {
		// TODO Auto-generated constructor stub
		super(null);
	}

	public JoinOperator(HashMap<String, String> dataType)
	{
		super(null);
		this.dataType = dataType;
	}
	
	public JoinOperator(JoinOperator join) {
		super(null);
		this.joinCondition = join.joinCondition;
		this.joinOperators = join.joinOperators;
		this.relationTablesList = join.relationTablesList;
		this.stringTuple = join.stringTuple;
		this.tables = join.tables;
	}

//	public JoinOperator(HashMap<String, CreateTable> schema) {
//		super(schema);
//	}

	public JoinOperator(List<FromItem> joins,
			HashMap<String, CreateTable> schema) {
		// TODO Auto-generated constructor stub
		super(schema);
		tables = joins;
	}

	public List<FromItem> getTables() {
		return tables;
	}

	public void setTables(List<FromItem> tables) {
		this.tables = tables;
	}

	public void findJoins() {

	}

//	public HashMap<Column, String> join(HashMap<Column, String> tuple1,
//			HashMap<Column, String> tuple2) {
//		HashMap<Column, String> join_output = new HashMap<Column, String>();
//		BinaryExpression b = (BinaryExpression) joinCondition;
//		ScanOperator so = new ScanOperator(getSchema());
//		Expression exp_tuple1 = b.getLeftExpression();
//		Expression exp_tuple2 = b.getRightExpression();
//		LeafValue value_tuple1 = so.accept(exp_tuple1, tuple1);
//		LeafValue value_tuple2 = so.accept(exp_tuple2, tuple2);
//		// TODO Auto-generated method stub
//		if (value_tuple1.equals(value_tuple2)) {
//			for (Map.Entry<Column, String> mp : tuple1.entrySet()) {
//				join_output.put(mp.getKey(), mp.getValue());
//			}
//			for (Map.Entry<Column, String> mp : tuple2.entrySet()) {
//				join_output.put(mp.getKey(), mp.getValue());
//			}
//		}
//		return join_output;
//	}

	public ArrayList<String> merge_join(
			TreeMap<Integer, ArrayList<String>> tuple1,
			TreeMap<Integer, ArrayList<String>> tuple2) {

		ArrayList<String> al = new ArrayList<String>();
		int key1 = 0;
		int key2 = 0;

		// for (Map.Entry<Integer, ArrayList<String>> mp1 : tuple1.entrySet()) {
		while (!(tuple1.isEmpty())) {
			key1 = tuple1.firstKey();

			if (key1 >= key2) {
				if (tuple2.isEmpty()) {

					break;

				} else {
					// for (Map.Entry<Integer, ArrayList<String>> mp2 :
					// tuple2.entrySet()) {
					while (!(tuple2.isEmpty())) {
						key2 = tuple2.firstKey();

						if (key1 == key2) {
							for (String mp1_value : tuple1.get(key1)) {
								for (String mp2_value : tuple2.get(key2)) {
									if (mp1_value.endsWith("|")) {
										if (merge_join_output.containsKey(key1)) {
											ArrayList<String> temp_al1 = merge_join_output
													.get(key1);
											temp_al1.add(mp1_value + mp2_value);
											merge_join_output.put(key1,
													temp_al1);
											al.add(mp1_value + mp2_value);
										}
										else
										{
											ArrayList<String> temp_al1 = new ArrayList<String>();
											temp_al1.add(mp1_value + mp2_value);
											merge_join_output.put(key1,
													temp_al1);
											al.add(mp1_value + mp2_value);
										}
									} else {
										if (merge_join_output.containsKey(key1)) {
											ArrayList<String> temp_al1 = merge_join_output
													.get(key1);
											temp_al1.add(mp1_value + "|"
													+ mp2_value);
											merge_join_output.put(key1,
													temp_al1);
											al.add(mp1_value + "|"
													+ mp2_value);
										}
										else
										{
											ArrayList<String> temp_al1 = new ArrayList<String>();
											temp_al1.add(mp1_value + "|"
													+ mp2_value);
											merge_join_output.put(key1, temp_al1);
											al.add(mp1_value + "|" + mp2_value);
										}
										
									}
								}
							}
							tuple1.remove(key1);
							tuple2.remove(key2);
							break;
						}

						else if (key1 < key2) {
							tuple1.remove(key1);
							break;
						}

						else if (key2 < key1) {
							tuple2.remove(key2);
						}
					}
				}
			}//end of if (key1 >= key2)
			else
			{
				tuple1.remove(key1);
			}

		}
		return al;

	}

	public String merge_join(String File1, String File2) {

		TreeMap<Integer, String> merge_join_output_limited = new TreeMap<Integer, String>();
		String FileName = null;
		long exclude = 10000000;
		File f = new File(File1);
//		FileReader fr = new FileReader(File1);
		Long N = f.length();
		Long M = (Runtime.getRuntime().maxMemory() - exclude) / 2;
		int slices = (int) Math.ceil((double) N / M);

		try {
			FileReader fr1 = new FileReader(File1);
			BufferedReader br1 = new BufferedReader(fr1);
			FileReader fr2 = new FileReader(File2);
			BufferedReader br2 = new BufferedReader(fr2);
			String t;
			FileName = "E:\\UB\\external-sorted.txt";
			FileWriter fw = new FileWriter("E:\\UB\\external-sorted.txt");
			PrintWriter pw = new PrintWriter(fw);
			TreeMap<Integer, ArrayList<String>> tuple1 = new TreeMap<Integer, ArrayList<String>>();
			TreeMap<Integer, ArrayList<String>> tuple2 = new TreeMap<Integer, ArrayList<String>>();
			for (int i = 0; i < slices; i++) {
				while (Runtime.getRuntime().freeMemory() > 25000000
						&& Runtime.getRuntime().freeMemory() <= 90000000
						&& ((t = br1.readLine()) != null)) {
					String[] split_string = t.split("\\+\\|\\+");
					String key = split_string[0];
					String value = split_string[1];
					if (tuple1.containsKey(Integer.parseInt(key))) {
						ArrayList<String> temp = tuple1.get(key);
						temp.add(value);
						tuple1.put(Integer.parseInt(key), temp);
					} else {
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(value);
						tuple1.put(Integer.parseInt(key), temp);
					}

				}
				br1.close();
				while (Runtime.getRuntime().freeMemory() > 10000000
						&& Runtime.getRuntime().freeMemory() <= 55000000
						&& ((t = br2.readLine()) != null)) 
						{
//					while(((t = br2.readLine()) != null)){
					String[] split_string = t.split("\\+\\|\\+");
					String key = split_string[0];
					String value = split_string[1];
					if (tuple2.containsKey(Integer.parseInt(key))) {
						ArrayList<String> temp = tuple2.get(Integer.parseInt(key));
						temp.add(value);
						tuple2.put(Integer.parseInt(key), temp);
					} else {
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(value);
						tuple2.put(Integer.parseInt(key), temp);
					}
//					}
				}
				br2.close();
				int key1 = 0;
				int key2 = 0;
//				ArrayList<String> al = new ArrayList<String>();
//				while (!(sorted_file1.isEmpty())) {
//					key1 = sorted_file1.firstKey();
//
//					if (key1 >= key2) {
//						if (sorted_file2.isEmpty()) {
//
//							break;
//
//						} else {
//							// for (Map.Entry<Integer, ArrayList<String>> mp2 :
//							// tuple2.entrySet()) {
//							while (!(sorted_file2.isEmpty())) {
//								key2 = sorted_file2.firstKey();
//
//								if (key1 == key2) {
//									for (String mp1_value : sorted_file1
//											.get(key1)) {
//										for (String mp2_value : sorted_file2
//												.get(key2)) {
//											if (mp1_value.endsWith("|")) {
//												merge_join_output_limited.put(
//														key1, mp1_value
//																+ mp2_value);
//
//											} else {
//												merge_join_output_limited.put(
//														key1, mp1_value + "|"
//																+ mp2_value);
//
//											}
//										}
//									}
//
//									sorted_file2.remove(key2);
//									break;
//								}
//
//								else if (key1 < key2) {
//									sorted_file1.remove(key1);
//									break;
//								}
//
//								else if (key2 < key1) {
//									sorted_file2.remove(key2);
//								}
//							}
//						}
//					}
//
//				}
				while(!(tuple1.isEmpty()))
				{
					key1 = tuple1.firstKey();

					if (key1 >= key2) {
						if (tuple2.isEmpty()) {

							break;

						} else {
							// for (Map.Entry<Integer, ArrayList<String>> mp2 :
							// tuple2.entrySet()) {
							while (!(tuple2.isEmpty())) {
								key2 = tuple2.firstKey();

								if (key1 == key2) {
									for (String mp1_value : tuple1.get(key1)) {
										for (String mp2_value : tuple2.get(key2)) {
											if (mp1_value.endsWith("|")) {
												if (merge_join_output_file.containsKey(key1)) {
													ArrayList<String> temp_al1 = merge_join_output_file
															.get(key1);
													temp_al1.add(mp1_value + mp2_value);
													merge_join_output_file.put(key1,
															temp_al1);
//													al.add(mp1_value + mp2_value);
												}
												else
												{
													ArrayList<String> temp_al1 = new ArrayList<String>();
													temp_al1.add(mp1_value + mp2_value);
													merge_join_output_file.put(key1,
															temp_al1);
//													al.add(mp1_value + mp2_value);
												}
											} else {
												if (merge_join_output_file.containsKey(key1)) {
													ArrayList<String> temp_al1 = merge_join_output_file
															.get(key1);
													temp_al1.add(mp1_value + "|"
															+ mp2_value);
													merge_join_output_file.put(key1,
															temp_al1);
//													al.add(mp1_value + "|"
//															+ mp2_value);
												}
												else
												{
													ArrayList<String> temp_al1 = new ArrayList<String>();
													temp_al1.add(mp1_value + "|"
															+ mp2_value);
													merge_join_output_file.put(key1, temp_al1);
//													al.add(mp1_value + "|" + mp2_value);
												}
												
											}
										}
									}
									tuple1.remove(key1);
									tuple2.remove(key2);
									break;
								}

								else if (key1 < key2) {
									tuple1.remove(key1);
									break;
								}

								else if (key2 < key1) {
									tuple2.remove(key2);
								}
							}
						}
					}//end of if (key1 >= key2)
					else
					{
						tuple1.remove(key1);
					}

				}
			
				

				for (Map.Entry<Integer, ArrayList<String>> mp : merge_join_output_file
						.entrySet()) {
					ArrayList<String> temp = mp.getValue();
					for(String tmp : temp)
					{
					pw.println(mp.getKey() + "+|+" + tmp);
					}
				}
				merge_join_output_file.clear();
			}

			pw.flush();
			pw.close();
			fw.close();
			System.gc();
		}

		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return FileName;

	}
	public TreeMap<Integer, ArrayList<HashMap<Column,String>>> GraceHybridJoin(
			TreeMap<Integer, ArrayList<HashMap<Column,String>>> table, String fileName,ArrayList<Column> colDef, Column col,Column join_col,
			ArrayList<Expression> rowCondition,ArrayList<Expression> orCondition) {
//		for(Map.Entry<Integer, ArrayList<HashMap<Column,String>>> mp : table.entrySet())
//		{
//			System.out.println(mp.getKey() + "->" + mp.getValue());
//		}
		TreeMap<Integer, ArrayList<HashMap<Column,String>>> sorted_file = new TreeMap<Integer, ArrayList<HashMap<Column,String>>>();
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			String t = "";
//			HashMap<Column, String> tuple = new HashMap<Column, String>();
//			List<ColumnDefinition> lt = null;
//			Table tab = null;
//			
//			tab = col.getTable();
//			tab.setName(tab.getName().toUpperCase());
//			
//			if (tables.containsKey(tableName.toUpperCase())) {
//				ct = tables.get(tableName.toUpperCase());
//				lt = ct.getColumnDefinitions();
////				tab = ct.getTable();
////				tab.setName(tab.getName().toUpperCase());
//			}

			while (t != null) {

				while ((t = br.readLine()) != null) {
					if(t.equals(""))
					{
//						System.out.println(1);
					}
					else
					{
						HashMap<Column, String> tuple = new HashMap<Column, String>();
						String cols[] = t.split("\\|");
						boolean flag = false;
						boolean or_flag=true;
	
	
							for (int k = 0; k < colDef.size(); k++) {

								Column c = colDef.get(k);
//								if(c.getWholeColumnName().equals(col.getWholeColumnName()))
//								{
//									col = c;
//								}
//								else
//								{
//									//do nothing
//								}
//	

								tuple.put(c, cols[k]);
							}
							cols = null;
							ScanOperator so = new ScanOperator(dataType);
														
							LeafValue leafval = null;
							LeafValue leafval_or = null;
							
							if(orCondition.size() <= 0)
							{
								
							}
							else{
//								int i=0;
							for (Expression exp : orCondition) {
//								String or_datatype = datatype_or.get(i);
//								i++;
//								ScanOperator so = new ScanOperator(dataType);
								leafval_or = so.accept(exp, tuple);
								if(leafval_or.toString().equals("TRUE"))
								{
									or_flag=true;
									break;
								}
								else
								{
									or_flag=false;
									
								}
							}
							}

							if(rowCondition.size() <= 0)
							{
								
							}
							else{
//								int i=0;
								for (Expression exp : rowCondition) {
//									String row_datatype = datatype_row.get(i);
//									i++;
//									ScanOperator so = new ScanOperator(dataType);
									leafval = so.accept(exp, tuple);
								if(leafval.toString().equalsIgnoreCase("true"))
								{
									
								}
								else
								{
									flag=true;
									break;
								}
							}
							}
							ArrayList<HashMap<Column,String>> hash_results = new ArrayList<HashMap<Column,String>>();
						if (!flag) {
							if (or_flag) {
								String value = tuple.get(col);
								
								
								if(table.containsKey(Integer.parseInt(value)))
								{
//									System.out.println("Left table: "+table.get(Integer.parseInt(value)));
//									System.out.println();
									ArrayList<HashMap<Column,String>> temp = table.get(Integer.parseInt(value));
									for(int i=0;i<temp.size();i++)
									{
//										System.out.println("Right table Tuple: "+tuple);
//										System.out.println();
//										HashMap<Column,String> temp_hash2= temp.get(i);
										HashMap<Column,String> temp_hash = new HashMap<Column,String>(temp.get(i));
//										temp_hash = temp.get(i);
										temp_hash.putAll(tuple);
//										System.out.println(temp);
//										System.out.println("Joined table Tuple: "+temp_hash);
//										System.out.println();
										if(join_col==null)
										{
											int key = 1;
											if(sorted_file.containsKey(key)){
												ArrayList<HashMap<Column,String>> temp1 = new ArrayList<HashMap<Column,String>>(sorted_file.get(key));
//												ArrayList<HashMap<Column,String>> 
//												temp1 = sorted_file.get(key);
												temp1.add(temp_hash);
//												System.out.println("Addition to list: "+temp1);
//												System.out.println();
												sorted_file.put(key,temp1);
											}
											else
											{
												ArrayList<HashMap<Column,String>> temp1 = new ArrayList<HashMap<Column,String>>();
												temp1.add(temp_hash);
//												System.out.println("Addition to list: "+temp1);
//												System.out.println();
												sorted_file.put(key,temp1);	
											}
											
										}
										else
										{
											String key= temp_hash.get(join_col);
											if(sorted_file.containsKey(Integer.parseInt(key))){
												ArrayList<HashMap<Column,String>> temp1= new ArrayList<HashMap<Column,String>>(sorted_file.get(Integer.parseInt(key)));
//												ArrayList<HashMap<Column,String>> temp1 = sorted_file.get(Integer.parseInt(key));
												temp1.add(temp_hash);
//												System.out.println("Addition to list: "+temp1);
//												System.out.println();
												sorted_file.put(Integer.parseInt(key),temp1);
											}
											else
											{
												ArrayList<HashMap<Column,String>> temp1 = new ArrayList<HashMap<Column,String>>();
												temp1.add(temp_hash);
//												System.out.println("Addition to list: "+temp1);
//												System.out.println();
												sorted_file.put(Integer.parseInt(key),temp1);	
											}	
										}
										
										
									}
								}
								
								
							} else {
								// do nothing
							}
						}else
							{
								//do nothing
							}
					}
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		for(Map.Entry<Integer, ArrayList<HashMap<Column,String>>> mp : sorted_file.entrySet())
//		{
//			System.out.println(mp.getKey() + "->" + mp.getValue());
//		}
		return sorted_file;
		


	}
	public TreeMap<Integer, ArrayList<String>> getMerge_join_output() {
		return merge_join_output;
	}

	public void setMerge_join_output(
			TreeMap<Integer, ArrayList<String>> merge_join_output) {
		this.merge_join_output = merge_join_output;
	}
}