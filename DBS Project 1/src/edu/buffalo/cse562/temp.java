//package edu.buffalo.cse562;
//
//import java.io.*;
//import java.sql.Timestamp;
//import java.util.*;
//
//import javax.swing.text.TabableView;
//import javax.swing.tree.DefaultMutableTreeNode;
//
//import net.sf.jsqlparser.expression.BinaryExpression;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.Function;
//import net.sf.jsqlparser.expression.LeafValue;
//import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
//import net.sf.jsqlparser.parser.CCJSqlParser;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.schema.Table;
//import net.sf.jsqlparser.statement.*;
//import net.sf.jsqlparser.statement.create.table.*;
//import net.sf.jsqlparser.statement.select.*;
//
//public class Main {
//
//	@SuppressWarnings({ "unchecked", "rawtypes", "null" })
//	public static void main(String[] args) {
//		
//		java.util.Date date= new java.util.Date();
//		System.out.println(new Timestamp(date.getTime()));
//
//		File datadir = null;
//		File swapdir = null;
//		ArrayList<File> sqlFiles = new ArrayList<File>();
//		
//		HashMap<String, CreateTable> tables = new HashMap<String, CreateTable>();
//		HashMap<String, CreateTable> inputTables = new HashMap<String, CreateTable>();
//		CreateTable ct = null;
//		boolean aliasingExists = false;
////		OperatorLists opListSingletonObj = OperatorLists.getInstance();
//		
//		RATree node = new RATree();
//
////		int currentLevel = -1;
//
//		for (int j = 0; j < args.length; j++) 
//		{
//			if (args[j].equals("--data")) {
//				datadir = new File(args[j + 1]);
//				j++;
//			} 
//			else if (args[j].equals("--swap")) {
//				swapdir = new File(args[j + 1]);
//				j++;
//			} 
//			else {
//				sqlFiles.add(new File(args[j]));
//			}
//		}
//
//		for (File sql : sqlFiles) {
//			try {
//				FileReader stream = new FileReader(sql);
//				CCJSqlParser parser = new CCJSqlParser(stream);
//				Statement stmnt;
//
////				DefaultMutableTreeNode node = null;
//				
//
//				while ((stmnt = parser.Statement()) != null) {
////					System.out.println(stmnt);
//					if (stmnt instanceof CreateTable) {
////						System.out.println("stmnt instanceof CreateTable");
//						ct = (CreateTable) stmnt;
//						inputTables.put(ct.getTable().getName().toUpperCase(), ct);
////						System.out.println(ct.getTable().getName().toUpperCase());
//					} else if (stmnt instanceof Select) {
////						System.out.println("stmnt instanceof Select");
//						PlainSelect select = (PlainSelect) ((Select) stmnt)
//								.getSelectBody();
//
//						if (!(select.getFromItem() == null)) {
//							if (select.getFromItem() instanceof Table) {
//								
//								Table tab = ((Table) select.getFromItem());								
//								String tableName = tab.getName().toUpperCase();
//								String tableAlias = null;
//								if(tab.getAlias() == null)
//								{
//									//do nothing
//								}
//								else
//								{
//									tableAlias = tab.getAlias().toUpperCase();
//								}
//								
//								if(inputTables.containsKey(tableName))
//								{
////									System.out.println("Table added to schema: " + ((Table) select.getFromItem()).getName().toUpperCase());
//									tables.put(tableName, inputTables.get(tableName));
//								}
//								else
//								{
//									//do nothing
////									System.out.println("Table was NOT added to schema");
//								}
//								
//								
//								RelationOperator r = new RelationOperator(tab, tables);
//
//								node.AddNode(r);
////								OperatorLists.getRelationOpList().add(select.getFromItem().toString());
//								OperatorLists.getRelationOperatorList().add(r);
//								
//								if(tableAlias == null)
//								{
//									OperatorLists.getRelationOpList().add(tableName);
//								}
//								else
//								{
//									CreateTable crtTabToReplace = tables.get(tableName);
//									crtTabToReplace.getTable().setName(tableAlias);
//									tables.remove(tableName);
//									tables.put(tableAlias, crtTabToReplace);
//									OperatorLists.getRelationOpList().add(tableAlias);
//									aliasingExists = true;
//								}
//
//							} else if (select.getFromItem() instanceof SubSelect) {
//								//to implement later
//							}
//						}
//
//						if (!(select.getJoins() == null)) {					
//							
//							
//							for(Object f : select.getJoins())
//							{
//								Table tab = ((Table) ((Join) f).getRightItem());
//								String tableName = tab.getName().toUpperCase();
//								String tableAlias = null;
//								
//								if(tab.getAlias() == null)
//								{
//									//do nothing
//								}
//								else
//								{
//									tableAlias = tab.getAlias().toUpperCase();
//								}
//								
//								if(inputTables.containsKey(tableName))
//								{
////									System.out.println("Table added to schema: " + ((Table) ((Join) f).getRightItem()).getName().toUpperCase());
//									tables.put(tableName, inputTables.get(tableName));
//								}
//								else
//								{
//									//do nothing
////									System.out.println("Table was NOT added to schema");
//								}
//								RelationOperator r = new RelationOperator(tab, tables);
////								OperatorLists.getRelationOpList().add((((Join) f).getRightItem()).toString());
//								OperatorLists.getRelationOperatorList().add(r);
//								node.AddNodeForJoin(r);
//								
//								if(tableAlias == null)
//								{
//									OperatorLists.getRelationOpList().add(tableName);
//								}
//								else
//								{
//									CreateTable crtTabToReplace = tables.get(tableName);
//									crtTabToReplace.getTable().setName(tableAlias);
//									tables.remove(tableName);
//									tables.put(tableAlias, crtTabToReplace);
//									OperatorLists.getRelationOpList().add(tableAlias);
//								}
//								
////								System.out.println(1);
//							}
////							JoinOperator j = new JoinOperator(
////									select.getJoins(), tables);
//
////							node.AddNode(j);
//							
//						}
//
//						if (!(select.getWhere() == null)) {
//							List<Expression> otherWhereClauses = new ArrayList<Expression>(); 
//							List<Expression> whereClause = splitAndClauses(select.getWhere());
//							
//							for(Expression e : whereClause)
//							{
//								Expression leftExp = ((BinaryExpression) e).getLeftExpression();
//								Expression rightExp = ((BinaryExpression) e).getRightExpression();
//								//check if its a join condition
//								if(rightExp instanceof Column)
//								{
//									Table leftTab = ((Column) ((BinaryExpression) e).getLeftExpression()).getTable();
//									String leftTabName = leftTab.getName().toUpperCase();
//									leftTab.setName(leftTabName);
//									Table rightTab = ((Column) ((BinaryExpression) e).getRightExpression()).getTable();
//									String rightTabName = rightTab.getName().toUpperCase();
//									rightTab.setName(rightTabName);
//									// verify if its a join condition
//									if(rightTab == leftTab)
//									{
//										int index = OperatorLists.getRelationOpList().indexOf(leftTabName);
////										Table t = ((Column) ((BinaryExpression) e).getLeftExpression()).getTable();
////										t.setName(t.getName().toUpperCase());
////										((Column) ((BinaryExpression) e).getLeftExpression()).setTable(t);
//										
////										t = ((Column) ((BinaryExpression) e).getRightExpression()).getTable();
////										t.setName(t.getName().toUpperCase());
////										((Column) ((BinaryExpression) e).getRightExpression()).setTable(t);
//										
//										OperatorLists.getRelationOperatorList().get(index).getRowCondition().add(e);
//									}
//									else
//									{
////										System.out.println(((Column) ((BinaryExpression) e).getLeftExpression()).getTable());
//										int oneIndex = OperatorLists.getRelationOpList().indexOf(leftTabName);
//										int otherIndex = OperatorLists.getRelationOpList().indexOf(rightTabName);
//										
////										String tableName = ((Column) ((BinaryExpression) e).getLeftExpression()).getTable().getName().toUpperCase();
////										((Column) ((BinaryExpression) e).getLeftExpression()).getTable().setName(tableName);
////										
////										tableName = ((Column) ((BinaryExpression) e).getRightExpression()).getTable().getName().toUpperCase();
////										((Column) ((BinaryExpression) e).getRightExpression()).getTable().setName(tableName);
//										
//										if(oneIndex > otherIndex)
//										{
//											while(OperatorLists.getJoinConditionList().size() < oneIndex)
//											{
//												OperatorLists.getJoinConditionList().add(null);
//											}
//											if(oneIndex < OperatorLists.getJoinConditionList().size())
//											{
//												if(OperatorLists.getJoinConditionList().get(oneIndex) == null)
//												{
//													OperatorLists.getJoinConditionList().remove(oneIndex);
//												}
//												else
//												{
//													otherWhereClauses.add(e);
//												}
//											}
//											else
//											{
//												OperatorLists.getJoinConditionList().add(oneIndex, e);
//											}										
//												
//										}
//										else
//										{
//											while(OperatorLists.getJoinConditionList().size() < otherIndex)
//											{
//												OperatorLists.getJoinConditionList().add(null);
//											}
//											if(otherIndex < OperatorLists.getJoinConditionList().size())
//											{
//												if(OperatorLists.getJoinConditionList().get(otherIndex) == null)
//												{
//													OperatorLists.getJoinConditionList().remove(otherIndex);
//												}
//												else
//												{
//													otherWhereClauses.add(e);
//												}
//											}
//											else
//											{
//												OperatorLists.getJoinConditionList().add(otherIndex, e);
//											}
//											
//										}
//									}
//								}
//								
//								else
//								{
//									int index;
//									
//									Table leftTab = ((Column) ((BinaryExpression) e).getLeftExpression()).getTable();
//									String leftTabName = null;
//									if(leftTab.getName() == null)
//									{
//										//do nothing
//									}
//									else
//									{
//										leftTabName = leftTab.getName().toUpperCase();
//										leftTab.setName(leftTabName);
//									}
//									
//									if(leftTabName == null)
//									{
//										index =-1;
//									}
//									else
//									{
//										index = OperatorLists.getRelationOpList().indexOf(leftTabName);
//									}
//									
//									
//									if(index == -1 && OperatorLists.getRelationOperatorList().size() == 1)
//									{
//										String tableName = OperatorLists.getRelationOperatorList().get(0).getTable().getName().toUpperCase();
//										leftTab.setName(tableName);
//										OperatorLists.getRelationOperatorList().get(0).getRowCondition().add(e);
//										
//									}
//									else if (OperatorLists.getRelationOperatorList().size() == 1)
//									{
//										throw new Exception("Table name returned null in Binary Expression");
//									}
//									else
//									{
//										String tableName = "";
//										if(!aliasingExists)
//										{
//											tableName = OperatorLists.getRelationOperatorList().get(index).getTable().getName().toUpperCase();
//										}
//										else
//										{
//											tableName = OperatorLists.getRelationOperatorList().get(index).getTable().getAlias().toUpperCase();
//										}
//										
//										leftTab.setName(tableName);
//										OperatorLists.getRelationOperatorList().get(index).getRowCondition().add(e);
//									}
//								}
//								
//							}
//							
//							if(otherWhereClauses.size() > 0)
//							{
//								SelectOperator s = new SelectOperator((List<Expression>) otherWhereClauses, tables);
//								
//								node.AddNode(s);
//							}
//							else
//							{
//								//do nothing
//							}
//							
////							SelectOperator s = new SelectOperator(
////									select.getWhere(), tables);
////							
////							node.AddNode(s);
//						}
//
//						if (!(select.getGroupByColumnReferences() == null)) {
//							List<Expression> groupByCol = select.getGroupByColumnReferences();
//							for(Expression exp: groupByCol)
//							{
//								if(exp instanceof Column)
//								{
//									Table tab = ((Column) exp).getTable();
//									String tableName = tab.getName().toUpperCase();
//									tab.setName(tableName);
//								}
//							}
//							GroupByOperator g = new GroupByOperator(groupByCol, tables);
//							node.AddNode(g);
//						}
//
//						if (!(select.getSelectItems() == null)) {
//							// System.out.println(select.getSelectItems());
//							List<Expression> projectCol = select.getSelectItems();
////							for(Expression exp: projectCol)
////							{
////								if(exp.getExpression() instanceof Column)
////								{
////									Table tab = ((Column) exp.getExpression()).getTable();
////									String tableName = tab.getName().toUpperCase();
////									tab.setName(tableName);
////								}
////							}
//							
//							for(int i =0; i < projectCol.size(); i++)
//							{
//								Expression exp = ((SelectExpressionItem) projectCol.get(i)).getExpression();
//								if(exp instanceof Function)
//								{
//									//do nothing
//								}
//								else if(exp instanceof Column)
//								{
//									Table tab = ((Column) exp).getTable();
//									String tableName = tab.getName().toUpperCase();
//									tab.setName(tableName);
//								}
//							}
//							ProjectOperator p = new ProjectOperator(projectCol, tables);
//							node.AddNode(p);
//						}
//
//						if (!(select.getHaving() == null)) {
//							// code here
//							HavingOperator h = new HavingOperator(
//									select.getHaving(), tables);
//							node.AddNode(h);
//						}
//
//						if (!(select.getOrderByElements() == null)) {
//							// code here
//							OrderByOperator o = new OrderByOperator(
//									select.getOrderByElements(), tables);
//							node.AddNode(o);
//						}
//
//						if (!(select.getDistinct() == null)) {
//							// code here
//							DistinctOperator d = new DistinctOperator(
//									select.getDistinct(), tables);
//							node.AddNode(d);
//						}
//
//						if (!(select.getLimit() == null)) {
//							// code here
//							LimitOperator l = new LimitOperator(
//									select.getLimit(), tables);
//							node.AddNode(l);
//						}
//
////						 System.out.println(select);
////						System.gc();
//					}
//				}
//			} //end of Try
//				catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		
////		System.out.println("RA Tree created");
//		// Access RATree here
////		ArrayList<Operators> operatorList = new ArrayList<Operators>();
//		Enumeration en = ((DefaultMutableTreeNode) node.getNode())
//				.postorderEnumeration();
////		System.out.println("Node is not null");
//		int currentJoin = 0; //location in arrayList for Join condition
//
//		if (en.hasMoreElements()) {
//			DefaultMutableTreeNode child = (DefaultMutableTreeNode) en
//					.nextElement();
//			
//			if(child.getUserObject() instanceof RelationOperator)
//			{
////				System.out.println("Relation Operator obtained");
//				RelationOperator r = (RelationOperator) child.getUserObject();
//				String tableName = r.getTable().getName().toUpperCase();
//				
//				String filepath = datadir.getAbsolutePath()
//						+ File.separator + tableName + ".dat";
//				
//				if(aliasingExists)
//				{
//					tableName =  r.getTable().getAlias().toUpperCase();
//				}
//				else
//				{
//					//do nothing
//				}
//				
////				String filepath = "D:\\MS - CSE\\Spring 15\\CSE 562 DBS\\Project\\Files\\tpch_2_17_0\\tpch_2_17_0\\ref_data\\1\\customer.tbl.1";
//				
//				ArrayList<String> tableJoin = null;
//				Column colJoinLeft = null;
//				Column colJoinRight = null;
//				JoinOperator join = null;
//				ArrayList<String> tableNamesList = new ArrayList<String>();
//				tableNamesList.add(tableName);
//				String getResults = "";
//				GroupByOperator g = null;
//				ProjectOperator p = null;
//				ArrayList<HashMap<Column, String>> tupleSet = new ArrayList<HashMap<Column,String>>();
//				boolean endOfExecutionFlag = false;
//				ArrayList<String> projectedList = new ArrayList<String>();
//				
//				while(child.getParent() != null)
//				{
////					System.out.println("Parent is not null");
//					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) child.getParent();
//					if(parent.getUserObject() instanceof JoinOperator)
//					{							
////						System.out.println("Join detected");
////						DefaultMutableTreeNode parent = (DefaultMutableTreeNode) child.getParent();
//						DefaultMutableTreeNode sibling = (DefaultMutableTreeNode) en.nextElement();
//						parent = (DefaultMutableTreeNode) en.nextElement();
//																			
//						Expression joinExpression = OperatorLists.getJoinConditionList().get(currentJoin + 1);
//						Column colLeft = null;
//						Column colRight = null;
//						
//						//External Sort Start
//						
//						ExternalSort sort = new ExternalSort();
//						
//						TreeMap<Integer, ArrayList<String>> tableLeft = null;
//						
//						if(currentJoin == 0)
//						{
//							if(tableName.equalsIgnoreCase(((Column) ((BinaryExpression) joinExpression).getLeftExpression()).getTable().toString()))
//							{
//								colLeft = ((Column) ((BinaryExpression) joinExpression).getLeftExpression());
//								colRight = ((Column) ((BinaryExpression) joinExpression).getRightExpression());
//							}
//							else if(tableName.equalsIgnoreCase(((Column) ((BinaryExpression) joinExpression).getRightExpression()).getTable().toString()))
//							{
//								colLeft = ((Column) ((BinaryExpression) joinExpression).getRightExpression());
//								colRight = ((Column) ((BinaryExpression) joinExpression).getLeftExpression());
//							}
//							else
//							{
//								//do something else for now
//							}
//							
//							tableLeft = sort.externalSort_unlimited(filepath, tableName, tables, colLeft, r.getRowCondition());
////							System.out.println("First sort");
//						} //end of if(currentJoin == 0)
//						else
//						{
//							colLeft = ((Column) ((BinaryExpression) joinExpression).getLeftExpression());
//							colLeft.getTable().setName(colLeft.getTable().toString().toUpperCase());
//							
//							colRight = ((Column) ((BinaryExpression) joinExpression).getRightExpression());
//							colRight.getTable().setName(colRight.getTable().toString().toUpperCase());
//							
//							if(colJoinLeft == colLeft
//									|| colJoinLeft == colRight
//									|| colJoinRight == colRight 
//									|| colJoinRight == colLeft)
//							{
//								tableLeft = join.getMerge_join_output();
//							}
//							else
//							{
//								if(tableNamesList.contains(((Column) ((BinaryExpression) joinExpression).getLeftExpression()).getTable().toString()))
//								{
////									colLeft = ((Column) ((BinaryExpression) joinExpression).getLeftExpression());
////									colRight = ((Column) ((BinaryExpression) joinExpression).getRightExpression());
//									//already assigned
////									System.out.println((1));
//								}
//								else if(tableNamesList.contains(((Column) ((BinaryExpression) joinExpression).getRightExpression()).getTable().toString()))
//								{
////									colLeft = ((Column) ((BinaryExpression) joinExpression).getRightExpression());
////									colRight = ((Column) ((BinaryExpression) joinExpression).getLeftExpression());
//									Column colTemp = colLeft;
//									colLeft = colRight;
//									colRight = colTemp;
//								}
//								else
//								{
//									//do something else for now
//								}
//								tableLeft = sort.externalSort_unlimited(tableJoin, tableNamesList, tables, colLeft);
////								System.out.println("Join table sorted");
//							}
//						}// end of if(currentJoin != 0)												
//																		
//						RelationOperator s = (RelationOperator) sibling.getUserObject();
//						String tableNameS = s.getTable().getName().toUpperCase();
//						
////						if(tableNameS.equalsIgnoreCase("NATION"))
////						{
////							System.out.println(1);
////						}
//						
//						String filepathS = datadir.getAbsolutePath()
//								+ File.separator + tableNameS + ".dat";
//						
//						if(aliasingExists)
//						{
//							tableNameS =  s.getTable().getAlias().toUpperCase();
//						}
//						else
//						{
//							//do nothing
//						}
//						
//						tableNamesList.add(tableNameS);
//						
////						String filepathS = "D:\\MS - CSE\\Spring 15\\CSE 562 DBS\\Project\\Files\\tpch_2_17_0\\tpch_2_17_0\\ref_data\\1\\orders.tbl.1";
//						TreeMap<Integer, ArrayList<String>> tableRight = sort.externalSort_unlimited(filepathS, tableNameS, tables, colRight, s.getRowCondition());
////						System.out.println("Right table sorted");
//						//External Sort End
//						
//						//Merge Join Start
//						
//						JoinOperator j = (JoinOperator) parent.getUserObject();
//						tableJoin = j.merge_join(tableLeft, tableRight);
//						
////						System.out.println("Tables joined");
//						
//						//Merge Join End
//						
//						currentJoin++;
//						child = parent;
//						colJoinLeft = colLeft;
//						colJoinRight = colRight;
//						join = j;
//						
//					}//end of if(parent.getUserObject() instanceof JoinOperator)
//					else
//					{
//						child = (DefaultMutableTreeNode) en.nextElement();
//						
//						if(child.getUserObject() instanceof SelectOperator)
//						{
//							SelectOperator s = (SelectOperator) child.getUserObject();
//							
////							System.out.println("Some where conditions were not pushed down");
//							
//							HashMap<Column, String> tuple = null;
//							
//							if(tableJoin != null)
//							{
////								System.out.println("Join detected therefore select has to split data");
//								//create tuple here
//								for(String tupleString: tableJoin)
//								{
//									tuple = new HashMap<Column, String>();
//									String colValues[] = tupleString.split("\\|");
//									
//									int columnPosition = 0;
//									for (int j = 0; j < tableNamesList.size(); j++) {
//
//										if (tables.containsKey(tableNamesList.get(j))) {
//											ct = tables.get(tableNamesList.get(j));														
//											List<ColumnDefinition> colDefList = ct.getColumnDefinitions();
//											
//											for (int k = 0; k < colDefList.size(); k++) {
//												Column c = new Column(ct.getTable(), colDefList.get(k).getColumnName());
//
//												tuple.put(c, colValues[columnPosition + k]);
////												System.out
////														.println(c+colValues[columnPosition + k]);
//											}
//											columnPosition += colDefList.size();
//										}
//
//									}// tuple created for one one whole row
//									
//									tupleSet.add(tuple);
//								}
//							}//end of if(tableJoin != null)
//							else
//							{
////								System.out.println("No join detected therefore select has to split data");
//								//create tuple here
//								FileReader fr = null;
//								BufferedReader br = null;
//								try {
//									fr = new FileReader(filepath);
//									br = new BufferedReader(fr);
//								} catch (FileNotFoundException e) {
//									// TODO Auto-generated catch block
//									System.out.println("Error in File Reader");
//									e.printStackTrace();
//								}
//								
//								String line;
//								
//								try {
//									while ((line = br.readLine()) != null) 
//									{
//										boolean flag = false;
//										if(!(line.equals("")))
//										{
//											tuple = new HashMap<Column, String>();
//											String colValues[] = line.split("\\|");
//											if (tables.containsKey(tableName)) 
//											{
//												ct = tables.get(tableName);
//												List<ColumnDefinition> colDef = ct.getColumnDefinitions();
//
//												for (int j = 0; j < colDef.size(); j++) 
//												{
//													Column c = new Column(
//															ct.getTable(), colDef.get(j).getColumnName());
//													tuple.put(c, colValues[j]);
//												}
//												
//												LeafValue leafval = null;
//												ScanOperator so = new ScanOperator(tables);
//												if(r.getRowCondition()==null)
//												{
//													
//												}
//												else{
//													for (Expression exp : r.getRowCondition()) 
//													{
//														leafval = so.accept(exp, tuple);
//														if((leafval.toString().equalsIgnoreCase("'true'"))||(leafval.toString().equalsIgnoreCase("true")))
//														{
//															//do nothing
//														}
//														else
//														{
//															flag=true;
//															break;
//														}
//													}
//												}
//												
//												if(!flag)
//												{
//													tupleSet.add(tuple);
//												}													
//											}
//										}//end of if(!(line.equals("")))
//									}
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
////									System.out.println("Error while reading from Buffer");
//									e.printStackTrace();
//								} //end of while ((line = br.readLine()) != null)
//							}// end of if(tableJoin == null)
//							
//							ArrayList<HashMap<Column, String>> tupleTempSet = new ArrayList<HashMap<Column,String>>();
//							
////							System.out.println("Verifying tuple to pass to next level");
//
//							for(HashMap<Column, String> tupleEntry: tupleSet)
//							{
//								LeafValue flag = s.accept(s, tables, tuple);
//								
//								if(flag.toString().equalsIgnoreCase("true") || flag.toString().equalsIgnoreCase("'true'"))
//								{
//									//do nothing
//								}
//								else
//								{
//									tupleTempSet.add(tupleEntry);
//								}
//							}
//							
//							tupleSet = null;
//							tupleSet = tupleTempSet;
//							
////							if(tupleSet.size() > 0)
////							{
////								System.out.println("New tupleSet created after Select Operator");
////							}
////							else
////							{
////								System.out.println("No tuples passed the Select criteria");
////							}
//						}
//						
//						else if(child.getUserObject() instanceof GroupByOperator)
//						{
//							g = (GroupByOperator) child.getUserObject();
//							
////							System.out.println("Group by detected");
//							
//							child = (DefaultMutableTreeNode) en.nextElement();
//							if(child.getUserObject() instanceof ProjectOperator)
//							{
//								p = (ProjectOperator) child.getUserObject();
//								
//								HashMap<Column, String> tuple = null;
////								if(tupleSet.size() != 0)
////								{
////									//do nothing
////									
////									System.out.println("TupleSet exists");
////								}
////								else
////								{
//									if(tableJoin != null)
//									{
////										System.out.println("Tuple to be created from Join table");
//										//create tuple here
//										for(String tupleString: tableJoin)
//										{
//											tuple = new HashMap<Column, String>();
//											String colValues[] = tupleString.split("\\|");
//											
//											int columnPosition = 0;
//											for (int j = 0; j < tableNamesList.size(); j++) {
//	
//												if (tables.containsKey(tableNamesList.get(j))) {
//													ct = tables.get(tableNamesList.get(j));														
//													List<ColumnDefinition> colDefList = ct.getColumnDefinitions();
//													
//													for (int k = 0; k < colDefList.size(); k++) {
//														Column c = new Column(ct.getTable(), colDefList.get(k).getColumnName());
//	
//														tuple.put(c, colValues[columnPosition + k]);
//													}
//													columnPosition += colDefList.size();
//												}
//											}// tuple created for one one whole row
//											
//											tupleSet.add(tuple);
//										}
//										
////										if(tupleSet.size() != 0)
////										{
////											System.out.println("Tuples created successfully from Join table");
////										}
////										else
////										{
////											System.out.println("Tuples were not created from Join table");
////										}
////										System.out.println("TupleSet created for GroupBy from tableJoin");
//									}//end of if(tableJoin != null)
//									else
//									{
////										System.out.println("Tuple is created from data file");
//										//create tuple here
//										FileReader fr = null;
//										BufferedReader br = null;
//										try {
//											fr = new FileReader(filepath);
//											br = new BufferedReader(fr);
//										} catch (FileNotFoundException e) {
//											// TODO Auto-generated catch block
////											System.out.println("Error in File Reader");
//											e.printStackTrace();
//										}
//										
//										String line;
////										int count = 0;
////										int lineNull = 0;
//										List<ColumnDefinition> colDef = null;
//										
//										if (tables.containsKey(tableName)) 
//										{
////											if(count ==0)
////											{
////												System.out
////														.println("Schema contains the given tablename");
////												count++;
////											}
//											ct = tables.get(tableName);
//											colDef = ct.getColumnDefinitions();
//										}
//										else
//										{
////											System.out
////													.println("Schema doesn't contain the given tableName: " + tableName);
//										}
//										
//										try {
//											while ((line = br.readLine()) != null) 
//											{
//												boolean flag = false;
//												if(!(line.equals("")))
//												{
////													if(lineNull == 0)
////													{
////														lineNull++;
////													}
//													tuple = new HashMap<Column, String>();
//													String colValues[] = line.split("\\|");
////													if (tables.containsKey(tableName)) 
////													{
////														if(count ==0)
////														{
////															System.out
////																	.println("Schema contains the given tablename");
////															count++;
////														}
////														ct = tables.get(tableName);
////														List<ColumnDefinition> colDef = ct.getColumnDefinitions();
////	
//														for (int j = 0; j < colDef.size(); j++) 
//														{
//															Column c = new Column(
//																	ct.getTable(), colDef.get(j).getColumnName());
//	//													c.setColumnName(colDef.get(j)
//	//															.getColumnName());
//	//													c.setTable(ct.getTable());
//															tuple.put(c, colValues[j]);
//														}
//														
//														LeafValue leafval = null;
//														ScanOperator so = new ScanOperator(tables);
//														if(r.getRowCondition()==null)
//														{
//															//do nothing
//														}
//														else{
//															for (Expression exp : r.getRowCondition()) 
//															{
//																leafval = so.accept(exp, tuple);
//																if((leafval.toString().equalsIgnoreCase("'true'"))||(leafval.toString().equalsIgnoreCase("true")))
//																{
//																	//do nothing
//																}
//																else
//																{
//																	flag=true;
//																	break;
//																}
//															}
//														}
//														
//														if(!flag)
//														{
//															tupleSet.add(tuple);
//														}													
//												}//end of if(!(line.equals("")))
//												else
//												{
////													if(lineNull == 0)
////													{
////														System.out
////																.println("Line blank detected initially");
////													}
//												}
//											}//end of while readline
////											if(tupleSet.size() != 0)
////											{
////												System.out.println("Tuples created successfully from data file in Groupby");
////											}
////											else
////											{
////												System.out.println("Tuples were NOT created from data file in Groupby");
////											}
//										} catch (IOException e) {
//											// TODO Auto-generated catch block
//											System.out.println("Error while reading from Buffer");
//											e.printStackTrace();
//										} //end of while ((line = br.readLine()) != null)
//									}// end of if(tableJoin == null)
////									System.out.println("TupleSet created for GroupBy from file");
////								}//end of if(tupleSet == null)
//								
//								
//								for(HashMap<Column, String> tupleEntry: tupleSet)
//								{
//									g.classify(tupleEntry, p);
//								}
//								
////								if(tupleSet.size() > 0)
////								{
////									System.out.println("Tuples are grouped");
////								}
////								else
////								{
////									System.out.println("No tuples were present to GroupBy");
////								}
////								System.out.println("Data modified per GroupBy");
//								getResults = "GROUPBY";
//
//							}//end of if(child.getUserObject() instanceof ProjectOperator)
//							else
//							{
//								//do nothing for now
////								System.out.println("Project Operator was not detected in the tree. Hence Group By was not executed");
//							}
//						}//end of if(child.getUserObject() instanceof GroupByOperator)
//						
//						else if(child.getUserObject() instanceof ProjectOperator)
//						{
//							p = (ProjectOperator) child.getUserObject();
//							
//							HashMap<Column, String> tuple = null;
//							
//							if(tableJoin != null)
//							{
//								
//								//create tuple here
//								for(String tupleString: tableJoin)
//								{
//									tuple = new HashMap<Column, String>();
//									String colValues[] = tupleString.split("\\|");
//									
//									int columnPosition = 0;
//									for (int j = 0; j < tableNamesList.size(); j++) {
//
//										if (tables.containsKey(tableNamesList.get(j))) {
//											ct = tables.get(tableNamesList.get(j));														
//											List<ColumnDefinition> colDefList = ct.getColumnDefinitions();
//											
//											for (int k = 0; k < colDefList.size(); k++) {
//												Column c = new Column(ct.getTable(), colDefList.get(k).getColumnName());
//
//												tuple.put(c, colValues[columnPosition + k]);
////												System.out
////														.println(c+colValues[columnPosition + k]);
//											}
//											columnPosition += colDefList.size();
//										}
////									columnPosition++;
//									
////									for (int k = 0; k < colValues.length; k++) {
////									
////									for (int j = 0; j < tableNamesList.size(); j++) {
////
////										if (tables.containsKey(tableNamesList.get(j))) {
////											ct = tables.get(tableNamesList.get(j));														
////											List<ColumnDefinition> colDefList = ct.getColumnDefinitions();
////
////											
////												Column c = new Column(ct.getTable(), colDefList.get(k).getColumnName());
////
////												tuple.put(c, colValues[k]);
////											}
////
////										}
//
//									}// tuple created for one one whole row
//									
//									tupleSet.add(tuple);
//								}
//							}//end of if(tableJoin != null)
//							else
//							{
//								//create tuple here
//								FileReader fr = null;
//								BufferedReader br = null;
//								try {
//									fr = new FileReader(filepath);
//									br = new BufferedReader(fr);
//								} catch (FileNotFoundException e) {
//									// TODO Auto-generated catch block
//									System.out.println("Error in File Reader");
//									e.printStackTrace();
//								}
//								
//								String line;
//								
//								try {
//									while ((line = br.readLine()) != null) 
//									{
//										boolean flag = false;
//										if(!(line.equals("")))
//										{
//											tuple = new HashMap<Column, String>();
//											String colValues[] = line.split("\\|");
//											if (tables.containsKey(tableName)) 
//											{
//												ct = tables.get(tableName);
//												List<ColumnDefinition> colDef = ct.getColumnDefinitions();
//
//												for (int j = 0; j < colDef.size(); j++) 
//												{
//													Column c = new Column(
//															ct.getTable(), colDef.get(j).getColumnName());
////												c.setColumnName(colDef.get(j)
////														.getColumnName());
////												c.setTable(ct.getTable());
//													tuple.put(c, colValues[j]);
//												}
//												
//												LeafValue leafval = null;
//												ScanOperator so = new ScanOperator(tables);
//												if(r.getRowCondition()==null)
//												{
//													
//												}
//												else{
//													for (Expression exp : r.getRowCondition()) 
//													{
//														leafval = so.accept(exp, tuple);
//														if((leafval.toString().equalsIgnoreCase("'true'"))||(leafval.toString().equalsIgnoreCase("true")))
//														{
//															//do nothing
//														}
//														else
//														{
//															flag=true;
//															break;
//														}
//													}
//												}
//												
//												if(!flag)
//												{
//													tupleSet.add(tuple);
//												}													
//											}
//										}//end of if(!(line.equals("")))
//									}
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									System.out.println("Error while reading from Buffer");
//									e.printStackTrace();
//								} //end of while ((line = br.readLine()) != null)
//							}// end of if(tableJoin == null)
//
//							
//							for(HashMap<Column, String> tupleEntry: tupleSet)
//							{
//								boolean flag = p.classify(tupleEntry, p);
//								
//								if(flag)
//								{
//									getResults = "PROJECT";
//								}
//								else
//								{
//									if(endOfExecutionFlag || getResults.equals("PROJECTEDLIST"))
//									{
//										//do nothing
//									}
//									else
//									{
//										child = (DefaultMutableTreeNode) en.nextElement();
//									}
//																		
//									if(child.getParent() == null)
//									{
//										System.out.println(p.getResult());
//										endOfExecutionFlag = true;
//									}
//									else
//									{
//										projectedList.add(p.getResult());
//										getResults = "PROJECTEDLIST";
//									}
//								}
//							}//end of for(HashMap<Column, String> tupleEntry: tupleSet)
//
//						}// end of if(child.getUserObject() instanceof ProjectOperator)
//						else if (child.getUserObject() instanceof OrderByOperator)
//						{
////							System.out.println("OrderBy starts here");
//							OrderByOperator o = (OrderByOperator) child.getUserObject();
//							List<Expression> colOrder = p.getList();
//							List<?> orderByList = o.getOrderOfElements();
//							ArrayList<Integer> colIndex = new ArrayList<Integer>();
//							
//							for(Object e: orderByList)
//							{
//								colIndex.add(colOrder.indexOf(e));
//							}
//							
//							if(getResults.equals("GROUPBY"))
//							{
////								System.out.println("Ordering results from GroupBy");
//								ArrayList<String> gb_result = g.getResult();
//								
////								ArrayList<String> gbOutput = new ArrayList<String>();
//								if(child.getParent() == null)
//								{
////									System.out.println("Will be posting ordered results from GroupBy");
////									Collections.reverse(gb_result);
////									for(Map.Entry<String, ArrayList<String>> m : gb_result.entrySet())
////									{
////										String[] colValues = m.getKey().split("\\|");
////										String output = m.getKey() ;
//									
////									if(gb_result.size() == 0)
////									{
////										System.out.println("No results to post gb_result is empty");
////									}
////									else
////									{
////										System.out.println("Results follow this line");
////									}
//										for(String s : gb_result)
//										{
//											System.out.println(s);
//										}
//										
////									}
//									endOfExecutionFlag = true;
//								}
//								else
//								{
////									System.out.println("Results will be displayed after execution of the next operator");
////									getResults = "";
//									
////									if(g.getList() == o.getOrderOfElements())
////									{
////										//do nothing
////									}
////									else
////									{									
////										for(Expression exp: o.getOrderOfElements())
////										{
////											int index = p.getList().indexOf(exp);
////										}
//										
////										Map<String, ArrayList<String>> sortedMap = 
////											    new TreeMap<String, ArrayList<String>>(Collections.reverseOrder());
////										sortedMap = gb_result;
////										gbOutput.add("dedbgjdf");
////									}
//									
//								}
//							}//end of if(getResults.equals("GROUPBY"))
//							else if(getResults.equals("PROJECT"))
//							{
//								//modify this to handle order by
////								if(child.getParent() == null)
////								{
////									System.out.println(p.getResult());
////									endOfExecutionFlag = true;
////								}
////								else
////								{
////									//do nothing for now
////								}
//							}//end of if(getResults.equals("PROJECT"))
//							else if(getResults.equals("PROJECTEDLIST"))
//							{
////								for(String projectStr: projectedList)
////								{
////									String[] colValues = projectStr.split("\\|");
//									//for now assume the tuple is recreated for OrderBy
//									
//									//implement this later
////								}
//							}
//							else
//							{
//								//do nothing for now
////								System.out.println("Reached orderBy without GroupBy");
//							}
//						}//end of if (child.getUserObject() instanceof OrderByOperator)
//						else if (child.getUserObject() instanceof LimitOperator)
//						{
////							System.out.println("Limit Operator detected");
//							LimitOperator l = (LimitOperator) child.getUserObject();
//							long limitOuptut = (long) l.getLim().getRowCount();
//							
//							if(getResults.equals("GROUPBY"))
//							{
////								System.out.println("Limiting results from GroupBy");
//								ArrayList<String> gb_result = g.getResult();
////								ArrayList<String> gbOutput = new ArrayList<String>();
//								if(child.getParent() == null)
//								{
////									System.out.println("Posting Limited results from GroupBy");
////									for(Map.Entry<String, ArrayList<String>> m : gb_result.entrySet())
////									{
////										String[] colValues = m.getKey().split("\\|");
////										String output = m.getKey() ;
//									
////									if(gb_result.size() > 0)
////									{
////										System.out.println("Results follow this line");
////									}
////									else
////									{
////										System.out.println("No rows returned by Group By");
////									}
//										for(String s : gb_result)
//										{
//											System.out.println(s);
//											
//											--limitOuptut;
//											if(limitOuptut == 0)
//											{
//												break;
//											}
//										}
//										
////									}
//									endOfExecutionFlag = true;								
//								}
//								else
//								{
//									//do nothing for now
////									System.out.println("Don't know why the code reached here");
//								}
//							}//end of if(getResults.equals("GROUPBY"))
//							else if(getResults.equals("PROJECT"))
//							{
////								System.out.println("Posting limited results from Project");
//								//modify this to handle order by
//								if(child.getParent() == null)
//								{
//									System.out.println(p.getResult());
//									endOfExecutionFlag = true;
//								}
//								else
//								{
//									//do nothing for now
//								}
//							}//end of if(getResults.equals("PROJECT"))
//							else if(getResults.equals("PROJECTEDLIST"))
//							{
////								System.out.println("Posting limited results from Projected List");
//								for(String projectStr: projectedList)
//								{
//									System.out.println(projectStr);
//									--limitOuptut;
//									
//									if(limitOuptut == 0)
//									{
//										break;
//									}
//								}
//								endOfExecutionFlag = true;
//							}
//							else
//							{
//								//do nothing for now
////								System.out.println("No GroupBy was detected in the code");
//							}
//						}
//						else
//						{
//							//do nothing for now
//							
////							System.out.println("Don't know why the code reached here");
//						}
//					}//end of if(!(parent.getUserObject() instanceof JoinOperator))
//					
//				}//end of while(child.getParent() != null)
//				
//				if(endOfExecutionFlag)
//				{
//					//do nothing
////					System.out.println("Everything should have been displayed by now");
//				}
//				else
//				{
////					System.out.println("End of execution posting results now");
//					if(getResults.equals("GROUPBY"))
//					{
////						System.out.println("End of execution posting results from GroupBy");
//						ArrayList<String> gb_result = g.getResult();
//						
////						if(gb_result.size() == 0)
////						{
////							System.out.println("Nothing to display as gb_results is empty");
////						}
////						else
////						{
////							System.out.println("Results follow this line");
////						}
//////						if(child.getParent() == null)
////						{
////							for(Map.Entry<String, ArrayList<String>> m : gb_result.entrySet())
////							{
//////								String[] colValues = m.getKey().split("\\|");
////								String output = m.getKey() ;
//								for(String s : gb_result)
//								{
//									System.out.println(s);
//								}								
////							}
////							endOfExecutionFlag = true;
////						}
////						else
////						{
////							//do nothing for now
////						}
//					}
//					else if(getResults.equals("PROJECT"))
//					{
//						
//					}
//					else if(getResults.equals("PROJECTEDLIST"))
//					{
////						System.out.println("End of execution posting results from projected list");
//						for(String projectStr: projectedList)
//						{
//							System.out.println(projectStr);
//						}
//					}
//				}
//			}//end of if(child.getUserObject() instanceof RelationOperator)
//		} //end of if (en.hasMoreElements()) 
//
////		timeNow = System.currentTimeMillis();
////		System.out.println(new Date(timeNow));
//
////		System.out.println();
////		long endTime = System.nanoTime() - timeNow;
////		System.out.println(endTime);
//		
//		date= new java.util.Date();
//		System.out.println(new Timestamp(date.getTime()));
//		
//		
//		System.exit(0);
//	}
//
//	private static List<Expression> splitAndClauses(Expression where) {
//		// TODO Auto-generated method stub
//		
//		List<Expression> whereList = new ArrayList<Expression>();
//		
//		if(where instanceof AndExpression)
//		{
//			whereList.addAll((Collection<? extends Expression>) splitAndClauses(((AndExpression) where).getLeftExpression()));
//			
//			whereList.addAll((Collection<? extends Expression>) splitAndClauses(((AndExpression) where).getRightExpression()));
//		}
//		else
//		{
//			whereList.add(where);
//		}
//		
//		return whereList;
//	}
//
//}