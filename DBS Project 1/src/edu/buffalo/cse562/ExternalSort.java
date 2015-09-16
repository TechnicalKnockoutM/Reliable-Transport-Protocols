package edu.buffalo.cse562;

import java.io.*;
import java.util.*;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class ExternalSort {
	int count_global=0;
	HashMap<String, String> dataType = new HashMap<String, String>();

	public ExternalSort(HashMap<String, String> dataType) {
		// TODO Auto-generated constructor stub
		this.dataType = dataType;
	}

	@SuppressWarnings("unchecked")
	public void externalSort(String fileName, String tableName,
			HashMap<String, CreateTable> tables, Column col,ArrayList<Expression> rowCondition,ArrayList<Expression> orCondition) {
		CreateTable ct = null;
		File f = new File(fileName);
		long N = f.length();
		long exclude = 10000000;
		long M = Runtime.getRuntime().maxMemory() - exclude;// 100000;
		String tfile = "temp-file-";
//		String[] buffer = new String[(int) (M < N ? M : N)];
		String[] buffer =  new String[(int) (10000000)];
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			int slices = (int) Math.ceil((double) N / M);

//			String t = "";
//			HashMap<Column, String> tuple = new HashMap<Column, String>();
			List<ColumnDefinition> lt_new = null;
			Table tab = null;
			
			tab = col.getTable();
			tab.setName(tab.getName().toUpperCase());
			
			if (tables.containsKey(tableName.toUpperCase())) {
				ct = tables.get(tableName.toUpperCase());
				lt_new = ct.getColumnDefinitions();
//				tab = ct.getTable();
//				tab.setName(tab.getName().toUpperCase());
			}
			int i, j;
			i = 0;
			j = -1;
			for (i = 0; i < slices; i++) {
				TreeMap<Integer, ArrayList<String>> sorted_file = new TreeMap<Integer, ArrayList<String>>();
				String t = "";
				HashMap<Column, String> tuple = new HashMap<Column, String>();
				
				while (t != null) {
//					System.out.println(Runtime.getRuntime().freeMemory());
//					System.out.println(Runtime.getRuntime().maxMemory());
					if (Runtime.getRuntime().freeMemory() < 10000000)
						throw new RuntimeException("Can't free enough memory.");

					while (Runtime.getRuntime().freeMemory() >= 10000000
							&& Runtime.getRuntime().freeMemory() < Runtime
									.getRuntime().maxMemory()
							&& ((t = br.readLine()) != null)) {
						
						if (!(t.equals(""))) {
							String cols[] = t.split("\\|");
							boolean flag = false;
							boolean or_flag=true;
							if (tables.containsKey(tableName)) {
								ct = tables.get(tableName);
								List<ColumnDefinition> lt = ct
										.getColumnDefinitions();

								for (int k = 0; k < lt.size(); k++) {
									Column c = new Column(ct.getTable(), lt
											.get(k).getColumnName());
									
									if(c.getWholeColumnName().equals(col.getWholeColumnName()))
									{
										col = c;
									}
									else
									{
										//do nothing
									}
									
//									c.setColumnName(lt.get(k).getColumnName());
//									c.setTable(ct.getTable());
									tuple.put(c, cols[k]);
								}

							}
							j++;
							buffer[j] = t;
//							String value = tuple.get(col);
//							if (sorted_file.containsKey(value)) {
//								ArrayList<String> temp = sorted_file.get(value);
//								temp.add(buffer[j]);
//								sorted_file.put(Integer.parseInt(value), temp);
//							} else {
//								ArrayList<String> temp = new ArrayList<String>();
//								temp.add(buffer[j]);
//								sorted_file.put(Integer.parseInt(value), temp);
//							}
							
							LeafValue leafval = null;
							ScanOperator so = new ScanOperator(dataType);
							LeafValue leafval_or = null;
							
							if(orCondition.size() <= 0)
							{
								
							}
							else{
							for (Expression exp : orCondition) {
								leafval_or = so.accept(exp, tuple);
								if((leafval_or.toString().equalsIgnoreCase("'true'"))||(leafval_or.toString().equalsIgnoreCase("true")))
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
							for (Expression exp : rowCondition) {
								leafval = so.accept(exp, tuple);
								if((leafval.toString().equalsIgnoreCase("'true'"))||(leafval.toString().equalsIgnoreCase("true")))
								{
									
								}
								else
								{
									flag=true;
									break;
								}
							}
							}
	
						if (!flag) {
							if (or_flag) {
								String value = tuple.get(col);
								if (sorted_file.containsKey(Integer
										.parseInt(value))) {
									ArrayList<String> temp = sorted_file
											.get(Integer.parseInt(value));
									temp.add(t);
									sorted_file.put(Integer.parseInt(value),
											temp);
								} else {
									ArrayList<String> temp = new ArrayList<String>();
									temp.add(t);
									sorted_file.put(Integer.parseInt(value),
											temp);
								}
							} else {
								// do nothing
							}
						}else
							{
								//do nothing
							}

							// Runtime.getRuntime().gc();
							System.gc();
						}
					}
					t = null;
				}

				// Write the sorted numbers to temp file
				FileWriter fw = new FileWriter(tfile + Integer.toString(count_global) + Integer.toString(i)
						+ ".txt");
				PrintWriter pw = new PrintWriter(fw);
				for (Map.Entry<Integer, ArrayList<String>> mp : sorted_file
						.entrySet()) {
					for (String s : mp.getValue()) {
						pw.println(mp.getKey() + "+|+" + s);
					}
				}
				sorted_file.clear();
				pw.flush();
				br.close();
				pw.close();
				fw.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		count_global++;
		
	}

	@SuppressWarnings("unchecked")
	public TreeMap<Integer, ArrayList<String>> externalSort_unlimited(
			String fileName, String tableName,
			HashMap<String, CreateTable> tables, Column col,
			ArrayList<Expression> rowCondition,ArrayList<Expression> orCondition) {
		TreeMap<Integer, ArrayList<String>> sorted_file = new TreeMap<Integer, ArrayList<String>>();
		CreateTable ct = null;
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			String t = "";
//			HashMap<Column, String> tuple = new HashMap<Column, String>();
			List<ColumnDefinition> lt = null;
			Table tab = null;
			
			tab = col.getTable();
			tab.setName(tab.getName().toUpperCase());
			
			if (tables.containsKey(tableName.toUpperCase())) {
				ct = tables.get(tableName.toUpperCase());
				lt = ct.getColumnDefinitions();
//				tab = ct.getTable();
//				tab.setName(tab.getName().toUpperCase());
			}

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
	
	//					if (tables.containsKey(tableName.toUpperCase())) {
	//						ct = tables.get(tableName.toUpperCase());
	//						@SuppressWarnings("unchecked")
	//						List<ColumnDefinition> lt = ct.getColumnDefinitions();
	
							for (int k = 0; k < lt.size(); k++) {
	//							Table tab = ct.getTable();
	//							tab.setName(tab.getName().toUpperCase());
								Column c = new Column(tab, lt.get(k)
										.getColumnName());
								if(c.getWholeColumnName().equals(col.getWholeColumnName()))
								{
									col = c;
								}
								else
								{
									//do nothing
								}
	
	//							c.setColumnName(lt.get(k).getColumnName());
	//							c.setTable(ct.getTable());
								tuple.put(c, cols[k]);
							}
	
	//					}
							

														
							LeafValue leafval = null;
							ScanOperator so = new ScanOperator(dataType);
							LeafValue leafval_or = null;
							
							if(orCondition.size() <= 0)
							{
								
							}
							else{
							for (Expression exp : orCondition) {
								leafval_or = so.accept(exp, tuple);
								if((leafval_or.toString().equalsIgnoreCase("'true'"))||(leafval_or.toString().equalsIgnoreCase("true")))
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
							for (Expression exp : rowCondition) {
								leafval = so.accept(exp, tuple);
								if((leafval.toString().equalsIgnoreCase("'true'"))||(leafval.toString().equalsIgnoreCase("true")))
								{
									
								}
								else
								{
									flag=true;
									break;
								}
							}
							}
	
						if (!flag) {
							if (or_flag) {
								String value = tuple.get(col);
								if (sorted_file.containsKey(Integer
										.parseInt(value))) {
									ArrayList<String> temp = sorted_file
											.get(Integer.parseInt(value));
									temp.add(t);
									sorted_file.put(Integer.parseInt(value),
											temp);
								} else {
									ArrayList<String> temp = new ArrayList<String>();
									temp.add(t);
									sorted_file.put(Integer.parseInt(value),
											temp);
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
		return sorted_file;
	}

	@SuppressWarnings("unchecked")
	public TreeMap<Integer, ArrayList<String>> externalSort_unlimited(
			ArrayList<String> dataToSortList, ArrayList<String> tableNames,
			HashMap<String, CreateTable> tables, Column col) {

		TreeMap<Integer, ArrayList<String>> sorted_file = new TreeMap<Integer, ArrayList<String>>();

		for (int i = 0; i < dataToSortList.size(); i++) {
			CreateTable ct = null;

			String t = dataToSortList.get(i);
			String cols[] = t.split("\\|");
			HashMap<Column, String> tuple = new HashMap<Column, String>();
			int columnPosition = 0;
			for (int j = 0; j < tableNames.size(); j++) {

				if (tables.containsKey(tableNames.get(j))) {
					ct = tables.get(tableNames.get(j));
					ct.getTable().setName(tableNames.get(j));
					
					List<ColumnDefinition> lt = ct.getColumnDefinitions();

					for (int k = 0; k < lt.size(); k++) {
						Column c = new Column(ct.getTable(), lt.get(k)
								.getColumnName());

						if (c.getWholeColumnName().equals(
								col.getWholeColumnName())) {
							col = c;
						} else {
							// do nothing
						}

						tuple.put(c, cols[columnPosition + k]);
					}
					columnPosition += lt.size();

				}

			}// tuple created for one one whole row

			String value = tuple.get(col);
			if (sorted_file.containsKey(Integer.parseInt(value))) {
				ArrayList<String> temp = sorted_file.get(Integer.parseInt(value));
				temp.add(t);
				sorted_file.put(Integer.parseInt(value), temp);
			} else {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(t);
				sorted_file.put(Integer.parseInt(value), temp);
			}

		}

		return sorted_file;

	}
	
	public TreeMap<Integer, ArrayList<HashMap<Column,String>>> GraceHybridSort(
			String fileName,ArrayList<Column> colDef, Column col,
			ArrayList<Expression> rowCondition,ArrayList<Expression> orCondition) {
		
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

//								Column c = colDef.get(k);
//								if(c.getWholeColumnName().equals(col.getWholeColumnName()))
//								{
//									col = c;
//								}
//								else
//								{
//									//do nothing
//								}
	

								tuple.put(colDef.get(k), cols[k]);
							}
				

														
							LeafValue leafval = null;
							LeafValue leafval_or = null;
							
							ScanOperator so = new ScanOperator(dataType);
							
							if(orCondition.size() <= 0)
							{
								
							}
							else{
								int i=0;
							for (Expression exp : orCondition) {
//								String or_datatype = datatype_or.get(i);
//								i++;
//								ScanOperator so = new ScanOperator(dataType);
								leafval_or = so.accept(exp, tuple);
								if((leafval_or.toString().equalsIgnoreCase("'true'"))||(leafval_or.toString().equalsIgnoreCase("true")))
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
								int i=0;
								for (Expression exp : rowCondition) {
//									String row_datatype = datatype_row.get(i);
//									i++;
//									ScanOperator so = new ScanOperator(dataType);
									leafval = so.accept(exp, tuple);
								if((leafval.toString().equalsIgnoreCase("'true'"))||(leafval.toString().equalsIgnoreCase("true")))
								{
									
								}
								else
								{
									flag=true;
									break;
								}
							}
							}
	
						if (!flag) {
							if (or_flag) {
								String value = tuple.get(col);
								if (sorted_file.containsKey(Integer
										.parseInt(value))) {
									ArrayList<HashMap<Column,String>> temp = new ArrayList<HashMap<Column,String>>(sorted_file.get(Integer.parseInt(value)));
									temp.add(tuple);
									sorted_file.put(Integer.parseInt(value),
											temp);
//									System.out.println("temp:" + temp);
//									System.out.println("t:" + t);
								} else {
									ArrayList<HashMap<Column,String>> temp = new ArrayList<HashMap<Column,String>>();
									temp.add(tuple);
									sorted_file.put(Integer.parseInt(value),
											temp);
//									System.out.println("temp:" + temp);
//									System.out.println("t:" + t);
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
		return sorted_file;
		


	}

}