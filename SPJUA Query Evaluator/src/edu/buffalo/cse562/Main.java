package edu.buffalo.cse562;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import javax.swing.text.TabableView;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.create.table.*;
import net.sf.jsqlparser.statement.select.*;

public class Main {

	@SuppressWarnings({ "unchecked", "rawtypes", "null" })
	public static void main(String[] args) {

//		java.util.Date date = new java.util.Date();
//		System.out.println(new Timestamp(date.getTime()));

		File datadir = null;
		File swapdir = null;
		ArrayList<File> sqlFiles = new ArrayList<File>();

		HashMap<String, CreateTable> tables = new HashMap<String, CreateTable>();
		HashMap<String, CreateTable> inputTables = new HashMap<String, CreateTable>();
		HashMap<String, String> dataType = new HashMap<String, String>();
		CreateTable ct = null;
		boolean aliasingExists = false;
		// OperatorLists opListSingletonObj = OperatorLists.getInstance();

		RATree node = new RATree();

		// int currentLevel = -1;

		for (int j = 0; j < args.length; j++) {
			if (args[j].equals("--data")) {
				datadir = new File(args[j + 1]);
				j++;
			} else if (args[j].equals("--swap")) {
				swapdir = new File(args[j + 1]);
				j++;
			} else {
				sqlFiles.add(new File(args[j]));
			}
		}

		for (File sql : sqlFiles) {
			try {
				FileReader stream = new FileReader(sql);
				CCJSqlParser parser = new CCJSqlParser(stream);
				Statement stmnt;

				// DefaultMutableTreeNode node = null;

				while ((stmnt = parser.Statement()) != null) {
					// System.out.println(stmnt);
					if (stmnt instanceof CreateTable) {
						// System.out.println("stmnt instanceof CreateTable");
						ct = (CreateTable) stmnt;
						inputTables.put(ct.getTable().getName().toUpperCase(),
								ct);
						// System.out.println(ct.getTable().getName().toUpperCase());
					} else if (stmnt instanceof Select) {
						System.out.println(stmnt);
						// System.out.println("stmnt instanceof Select");
						PlainSelect select = (PlainSelect) ((Select) stmnt)
								.getSelectBody();

						if (!(select.getFromItem() == null)) {
							if (select.getFromItem() instanceof Table) {

								Table tab = ((Table) select.getFromItem());
								String tableName = tab.getName().toUpperCase();
								String tableAlias = null;
								if (tab.getAlias() == null) {
									// do nothing
								} else {
									tableAlias = tab.getAlias().toUpperCase();
								}

								if (inputTables.containsKey(tableName)) {
									// System.out.println("Table added to schema: "
									// + ((Table)
									// select.getFromItem()).getName().toUpperCase());
									tables.put(tableName,
											inputTables.get(tableName));
								} else {
									// do nothing
									// System.out.println("Table was NOT added to schema");
								}

								RelationOperator r = new RelationOperator(tab,
										tables);

								node.AddNode(r);
								// OperatorLists.getRelationOpList().add(select.getFromItem().toString());
								OperatorLists.getRelationOperatorList().add(r);

								if (tableAlias == null) {
									OperatorLists.getRelationOpList().add(
											tableName);
								} else {
									CreateTable crtTabToReplace = tables
											.get(tableName);
									crtTabToReplace.getTable().setName(
											tableAlias);
									tables.remove(tableName);
									tables.put(tableAlias, crtTabToReplace);
									OperatorLists.getRelationOpList().add(
											tableAlias);
									aliasingExists = true;
									tableName = tableAlias;
								}

								CreateTable crtTab = tables.get(tableName);
								List colDef = crtTab.getColumnDefinitions();
								for (int i = 0; i < colDef.size(); i++) {
									String key = tableName
											+ "."
											+ ((ColumnDefinition) colDef.get(i))
													.getColumnName();
									String value = ((ColumnDefinition) colDef
											.get(i)).getColDataType()
											.toString().toLowerCase();
									dataType.put(key, value);
								}

							} else if (select.getFromItem() instanceof SubSelect) {
								// to implement later
							}
						}

						if (!(select.getJoins() == null)) {

							for (Object f : select.getJoins()) {
								Table tab = ((Table) ((Join) f).getRightItem());
								String tableName = tab.getName().toUpperCase();
								String tableAlias = null;

								if (tab.getAlias() == null) {
									// do nothing
								} else {
									tableAlias = tab.getAlias().toUpperCase();
								}

								if (inputTables.containsKey(tableName)) {
									// System.out.println("Table added to schema: "
									// + ((Table) ((Join)
									// f).getRightItem()).getName().toUpperCase());
									tables.put(tableName,
											inputTables.get(tableName));
								} else {
									// do nothing
									// System.out.println("Table was NOT added to schema");
								}
								RelationOperator r = new RelationOperator(tab,
										tables);
								// OperatorLists.getRelationOpList().add((((Join)
								// f).getRightItem()).toString());
								OperatorLists.getRelationOperatorList().add(r);
								node.AddNodeForJoin(r, dataType);

								if (tableAlias == null) {
									OperatorLists.getRelationOpList().add(
											tableName);
								} else {
									CreateTable crtTabToReplace = tables
											.get(tableName);
									crtTabToReplace.getTable().setName(
											tableAlias);
									tables.remove(tableName);
									tables.put(tableAlias, crtTabToReplace);
									OperatorLists.getRelationOpList().add(
											tableAlias);
									tableName = tableAlias;
								}

								CreateTable crtTab = tables.get(tableName);
								List colDef = crtTab.getColumnDefinitions();
								for (int i = 0; i < colDef.size(); i++) {
									String key = tableName
											+ "."
											+ ((ColumnDefinition) colDef.get(i))
													.getColumnName();
									String value = ((ColumnDefinition) colDef
											.get(i)).getColDataType()
											.toString().toLowerCase();
									dataType.put(key, value);
								}
								// System.out.println(1);
							}
							// JoinOperator j = new JoinOperator(
							// select.getJoins(), tables);

							// node.AddNode(j);

						}

						if (!(select.getWhere() == null)) {
							List<Expression> otherWhereClauses = new ArrayList<Expression>();
							List<Expression> whereClause = splitAndClauses(select
									.getWhere());

							for (Expression e : whereClause) {
								if (e instanceof Parenthesis) {
									// System.out.println("or expression");
									List<Expression> orexp = splitAndClausesOrExp(((Parenthesis) e)
											.getExpression());
									for (Expression ex : orexp) {
										Table lefttab = ((Column) ((BinaryExpression) ex)
												.getLeftExpression())
												.getTable();
										String lefttabName = lefttab.getName()
												.toUpperCase();
										lefttab.setName(lefttabName);
										// Table righttab = ((Column)
										// ((BinaryExpression)ex).getRightExpression()).getTable();
										// String righttabName =
										// righttab.getName().toUpperCase();
										// righttab.setName(righttabName);
										int index = OperatorLists
												.getRelationOpList().indexOf(
														lefttabName);
										OperatorLists.getRelationOperatorList()
												.get(index).getOrCondition()
												.add(ex);
									}
								} else {
									Expression leftExp = ((BinaryExpression) e)
											.getLeftExpression();
									Expression rightExp = ((BinaryExpression) e)
											.getRightExpression();
									// check if its a join condition
									if (rightExp instanceof Column) {
										Table leftTab = ((Column) ((BinaryExpression) e)
												.getLeftExpression())
												.getTable();
										String leftTabName = leftTab.getName()
												.toUpperCase();
										leftTab.setName(leftTabName);
										Table rightTab = ((Column) ((BinaryExpression) e)
												.getRightExpression())
												.getTable();
										String rightTabName = rightTab
												.getName().toUpperCase();
										rightTab.setName(rightTabName);
										// verify if its a join condition
										if (rightTab.getName().equalsIgnoreCase(leftTab.getName())) {
											int index = OperatorLists
													.getRelationOpList()
													.indexOf(leftTabName);
											// Table t = ((Column)
											// ((BinaryExpression)
											// e).getLeftExpression()).getTable();
											// t.setName(t.getName().toUpperCase());
											// ((Column) ((BinaryExpression)
											// e).getLeftExpression()).setTable(t);

											// t = ((Column) ((BinaryExpression)
											// e).getRightExpression()).getTable();
											// t.setName(t.getName().toUpperCase());
											// ((Column) ((BinaryExpression)
											// e).getRightExpression()).setTable(t);

											OperatorLists
													.getRelationOperatorList()
													.get(index)
													.getRowCondition().add(e);
										} else {
											// System.out.println(((Column)
											// ((BinaryExpression)
											// e).getLeftExpression()).getTable());
											int oneIndex = OperatorLists
													.getRelationOpList()
													.indexOf(leftTabName);
											int otherIndex = OperatorLists
													.getRelationOpList()
													.indexOf(rightTabName);

											// String tableName = ((Column)
											// ((BinaryExpression)
											// e).getLeftExpression()).getTable().getName().toUpperCase();
											// ((Column) ((BinaryExpression)
											// e).getLeftExpression()).getTable().setName(tableName);
											//
											// tableName = ((Column)
											// ((BinaryExpression)
											// e).getRightExpression()).getTable().getName().toUpperCase();
											// ((Column) ((BinaryExpression)
											// e).getRightExpression()).getTable().setName(tableName);

											if (oneIndex > otherIndex) {
												while (OperatorLists
														.getJoinConditionList()
														.size() < oneIndex) {
													OperatorLists
															.getJoinConditionList()
															.add(null);
												}
												if (oneIndex < OperatorLists
														.getJoinConditionList()
														.size()) {
													if (OperatorLists
															.getJoinConditionList()
															.get(oneIndex) == null) {
														OperatorLists
																.getJoinConditionList()
																.remove(oneIndex);
														OperatorLists
														.getJoinConditionList()
														.add(oneIndex, e);
													} else {
														otherWhereClauses
																.add(e);
													}
												} else {
													OperatorLists
															.getJoinConditionList()
															.add(oneIndex, e);
												}

											} else {
												while (OperatorLists
														.getJoinConditionList()
														.size() < otherIndex) {
													OperatorLists
															.getJoinConditionList()
															.add(null);
												}
												if (otherIndex < OperatorLists
														.getJoinConditionList()
														.size()) {
													if (OperatorLists
															.getJoinConditionList()
															.get(otherIndex) == null) {
														OperatorLists
																.getJoinConditionList()
																.remove(otherIndex);
														OperatorLists
														.getJoinConditionList()
														.add(otherIndex, e);
													} else {
														otherWhereClauses
																.add(e);
													}
												} else {
													OperatorLists
															.getJoinConditionList()
															.add(otherIndex, e);
												}

											}
										}
									}

									else {
										int index;

										Table leftTab = ((Column) ((BinaryExpression) e)
												.getLeftExpression())
												.getTable();
										String leftTabName = null;
										if (leftTab.getName() == null) {
											// do nothing
										} else {
											leftTabName = leftTab.getName()
													.toUpperCase();
											leftTab.setName(leftTabName);
										}

										if (leftTabName == null) {
											index = -1;
										} else {
											index = OperatorLists
													.getRelationOpList()
													.indexOf(leftTabName);
										}

										if (index == -1
												&& OperatorLists
														.getRelationOperatorList()
														.size() == 1) {
											String tableName = OperatorLists
													.getRelationOperatorList()
													.get(0).getTable()
													.getName().toUpperCase();
											leftTab.setName(tableName);
											OperatorLists
													.getRelationOperatorList()
													.get(0).getRowCondition()
													.add(e);

										} else if (OperatorLists
												.getRelationOperatorList()
												.size() == 0) {
											throw new Exception(
													"Table name returned null in Binary Expression");
										} else {
											String tableName = "";
											if (!aliasingExists) {
												tableName = OperatorLists
														.getRelationOperatorList()
														.get(index).getTable()
														.getName()
														.toUpperCase();
											} else {
												tableName = OperatorLists
														.getRelationOperatorList()
														.get(index).getTable()
														.getAlias()
														.toUpperCase();
											}

											leftTab.setName(tableName);
											OperatorLists
													.getRelationOperatorList()
													.get(index)
													.getRowCondition().add(e);
										}
									}
								}// End of added else
							}

							if (otherWhereClauses.size() > 0) {
								SelectOperator s = new SelectOperator(
										(List<Expression>) otherWhereClauses,
										tables, dataType);

								node.AddNode(s);
							} else {
								// do nothing
							}

							// SelectOperator s = new SelectOperator(
							// select.getWhere(), tables);
							//
							// node.AddNode(s);
						}

						if (!(select.getGroupByColumnReferences() == null)) {
							List<Expression> groupByCol = select
									.getGroupByColumnReferences();
							for (Expression exp : groupByCol) {
								if (exp instanceof Column) {
									Table tab = ((Column) exp).getTable();
									if (tab.getName() == null) {
										String tableName = OperatorLists
												.getRelationOperatorList()
												.get(0).getTable().getName()
												.toUpperCase();
										tab.setName(tableName);
									} else {
										String tableName = tab.getName()
												.toUpperCase();
										tab.setName(tableName);
									}
								}
							}
							GroupByOperator g = new GroupByOperator(groupByCol,
									tables, dataType);
							node.AddNode(g);
						}

						if (!(select.getSelectItems() == null)) {
							// System.out.println(select.getSelectItems());
							List<Expression> projectCol = select
									.getSelectItems();
							// for(Expression exp: projectCol)
							// {
							// if(exp.getExpression() instanceof Column)
							// {
							// Table tab = ((Column)
							// exp.getExpression()).getTable();
							// String tableName = tab.getName().toUpperCase();
							// tab.setName(tableName);
							// }
							// }

							for (int i = 0; i < projectCol.size(); i++) {
								Expression exp = ((SelectExpressionItem) projectCol
										.get(i)).getExpression();
								if (exp instanceof Function) {
									// do nothing
									if(((Function) exp).getParameters() == null)
									{
										//do nothing
									}
									else
									{
									List<Expression> f = (List<Expression>) ((Function) exp).getParameters().getExpressions();
									for(int j=0; j<f.size(); j++)
									{
										if(f.get(j) instanceof Column)
										{
											Table tab = ((Column) f.get(j)).getTable();
											if (tab.getName() == null) {
												String tableName = OperatorLists
														.getRelationOperatorList()
														.get(0).getTable().getName()
														.toUpperCase();
												tab.setName(tableName);
											}
											else
											{
												String tableName = tab.getName()
														.toUpperCase();
												tab.setName(tableName);
											}
										}
										else if(f.get(j) instanceof CaseExpression)
										{
											List<Expression> ex = ((CaseExpression) f.get(j)).getWhenClauses();
											for(int k=0; k < ex.size(); k++)
											{
												if(ex.get(k) instanceof WhenClause)
												{
													Expression e = ((WhenClause) ex.get(k)).getWhenExpression();
//													Expression e = ex.get(k).getWhenExpresion();
													if(e instanceof OrExpression)
													{
														Expression left = ((OrExpression) e).getLeftExpression();
														if(left instanceof Column)
														{
															if(left instanceof Column)
															{
																Table tab = ((Column) left).getTable();
																if (tab.getName() == null) {
																	String tableName = OperatorLists
																			.getRelationOperatorList()
																			.get(0).getTable().getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
																else
																{
																	String tableName = tab.getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
															}
														}
														else
														{
															Expression left_left = ((BinaryExpression) ((OrExpression) e).getLeftExpression()).getLeftExpression();
															if(left_left instanceof Column)
															{
																Table tab = ((Column) left_left).getTable();
																if (tab.getName() == null) {
																	String tableName = OperatorLists
																			.getRelationOperatorList()
																			.get(0).getTable().getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
																else
																{
																	String tableName = tab.getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
															}
														}
														
														Expression right = ((OrExpression) e).getRightExpression();
														if(right instanceof Column)
														{
															if(right instanceof Column)
															{
																Table tab = ((Column) right).getTable();
																if (tab.getName() == null) {
																	String tableName = OperatorLists
																			.getRelationOperatorList()
																			.get(0).getTable().getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
																else
																{
																	String tableName = tab.getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
															}
														}
														else
														{
															Expression left_right = ((BinaryExpression) ((OrExpression) e).getRightExpression()).getLeftExpression();
															if(left_right instanceof Column)
															{
																Table tab = ((Column) left_right).getTable();
																if (tab.getName() == null) {
																	String tableName = OperatorLists
																			.getRelationOperatorList()
																			.get(0).getTable().getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
																else
																{
																	String tableName = tab.getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
															}
														}
													}
													
													else
													{
														Expression left = ((AndExpression) e).getLeftExpression();
														if(left instanceof Column)
														{
															if(left instanceof Column)
															{
																Table tab = ((Column) left).getTable();
																if (tab.getName() == null) {
																	String tableName = OperatorLists
																			.getRelationOperatorList()
																			.get(0).getTable().getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
																else
																{
																	String tableName = tab.getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
															}
														}
														else
														{
															Expression left_left = ((BinaryExpression) ((AndExpression) e).getLeftExpression()).getLeftExpression();
															if(left_left instanceof Column)
															{
																Table tab = ((Column) left_left).getTable();
																if (tab.getName() == null) {
																	String tableName = OperatorLists
																			.getRelationOperatorList()
																			.get(0).getTable().getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
																else
																{
																	String tableName = tab.getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
															}
														}
														
														Expression right = ((AndExpression) e).getRightExpression();
														if(right instanceof Column)
														{
															if(right instanceof Column)
															{
																Table tab = ((Column) right).getTable();
																if (tab.getName() == null) {
																	String tableName = OperatorLists
																			.getRelationOperatorList()
																			.get(0).getTable().getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
																else
																{
																	String tableName = tab.getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
															}
														}
														else
														{
															Expression left_right = ((BinaryExpression) ((AndExpression) e).getRightExpression()).getLeftExpression();
															if(left_right instanceof Column)
															{
																Table tab = ((Column) left_right).getTable();
																if (tab.getName() == null) {
																	String tableName = OperatorLists
																			.getRelationOperatorList()
																			.get(0).getTable().getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
																else
																{
																	String tableName = tab.getName()
																			.toUpperCase();
																	tab.setName(tableName);
																}
															}
														}
													
													}
												}
											}
										}
										else
										{
											Expression left = ((BinaryExpression) f.get(j)).getLeftExpression();
											
											if(left instanceof Column)
											{
												Table tab = ((Column) left).getTable();
												if (tab.getName() == null) {
													String tableName = OperatorLists
															.getRelationOperatorList()
															.get(0).getTable().getName()
															.toUpperCase();
													tab.setName(tableName);
												}
												else
												{
													String tableName = tab.getName()
															.toUpperCase();
													tab.setName(tableName);
												}
											}
											else
											{
												Expression left_left = ((BinaryExpression) left).getLeftExpression();
												
												if(left_left instanceof Column)
												{
													Table tab = ((Column) left_left).getTable();
													if (tab.getName() == null) {
														String tableName = OperatorLists
																.getRelationOperatorList()
																.get(0).getTable().getName()
																.toUpperCase();
														tab.setName(tableName);
													}
													else
													{
														String tableName = tab.getName()
																.toUpperCase();
														tab.setName(tableName);
													}
												}
												Expression right_left = ((BinaryExpression) left).getRightExpression();
												
												if(right_left instanceof Column)
												{
													Table tab = ((Column) right_left).getTable();
													if (tab.getName() == null) {
														String tableName = OperatorLists
																.getRelationOperatorList()
																.get(0).getTable().getName()
																.toUpperCase();
														tab.setName(tableName);
													}
													else
													{
														String tableName = tab.getName()
																.toUpperCase();
														tab.setName(tableName);
													}
												}
												else
												{
													Expression left_right_left = null;
													Expression right_right_left = null;
													if(right_left instanceof Parenthesis)
													{
														left_right_left = ((BinaryExpression) ((Parenthesis) right_left).getExpression()).getLeftExpression();
														right_right_left = ((BinaryExpression) ((Parenthesis) right_left).getExpression()).getRightExpression();
													}
													else
													{
														left_right_left = ((BinaryExpression) right_left).getLeftExpression();
														right_right_left = ((BinaryExpression) right_left).getRightExpression();
													}
													
													
													
													if(left_right_left instanceof Column)
													{
														Table tab = ((Column) left_right_left).getTable();
														if (tab.getName() == null) {
															String tableName = OperatorLists
																	.getRelationOperatorList()
																	.get(0).getTable().getName()
																	.toUpperCase();
															tab.setName(tableName);
														}
														else
														{
															String tableName = tab.getName()
																	.toUpperCase();
															tab.setName(tableName);
														}
													}
													
													if(right_right_left instanceof Column)
													{
														Table tab = ((Column) right_right_left).getTable();
														if (tab.getName() == null) {
															String tableName = OperatorLists
																	.getRelationOperatorList()
																	.get(0).getTable().getName()
																	.toUpperCase();
															tab.setName(tableName);
														}
														else
														{
															String tableName = tab.getName()
																	.toUpperCase();
															tab.setName(tableName);
														}
													}
												}
											}
											
											Expression right = ((BinaryExpression) f.get(j)).getRightExpression();
											
											if(right instanceof Column)
											{
												Table tab = ((Column) right).getTable();
												if (tab.getName() == null) {
													String tableName = OperatorLists
															.getRelationOperatorList()
															.get(0).getTable().getName()
															.toUpperCase();
													tab.setName(tableName);
												}
												else
												{
													String tableName = tab.getName()
															.toUpperCase();
													tab.setName(tableName);
												}
											}
											else
											{
												Expression left_right = null;
												Expression right_right = null;
												if(right instanceof Parenthesis)
												{
													left_right = ((BinaryExpression) ((Parenthesis) right).getExpression()).getLeftExpression();
													right_right = ((BinaryExpression) ((Parenthesis) right).getExpression()).getRightExpression();
												}
												else
												{
													left_right = ((BinaryExpression) right).getLeftExpression();
													right_right = ((BinaryExpression) right).getRightExpression();
												}
												
												
												
												if(left_right instanceof Column)
												{
													Table tab = ((Column) left_right).getTable();
													if (tab.getName() == null) {
														String tableName = OperatorLists
																.getRelationOperatorList()
																.get(0).getTable().getName()
																.toUpperCase();
														tab.setName(tableName);
													}
													else
													{
														String tableName = tab.getName()
																.toUpperCase();
														tab.setName(tableName);
													}
												}
												
												if(right_right instanceof Column)
												{
													Table tab = ((Column) right_right).getTable();
													if (tab.getName() == null) {
														String tableName = OperatorLists
																.getRelationOperatorList()
																.get(0).getTable().getName()
																.toUpperCase();
														tab.setName(tableName);
													}
													else
													{
														String tableName = tab.getName()
																.toUpperCase();
														tab.setName(tableName);
													}
												}
											}
											
										}
									}
									}
								} else if (exp instanceof Column) {
									Table tab = ((Column) exp).getTable();
									if (tab.getName() == null) {
										String tableName = OperatorLists
												.getRelationOperatorList()
												.get(0).getTable().getName()
												.toUpperCase();
										tab.setName(tableName);
									} else {
										String tableName = tab.getName()
												.toUpperCase();
										tab.setName(tableName);
									}
								}
							}
							ProjectOperator p = new ProjectOperator(projectCol,
									tables, dataType);
							node.AddNode(p);
						}

						if (!(select.getHaving() == null)) {
							// code here
							HavingOperator h = new HavingOperator(
									select.getHaving(), tables);
							node.AddNode(h);
						}

						if (!(select.getOrderByElements() == null)) {
							// code here
							OrderByOperator o = new OrderByOperator(
									select.getOrderByElements(), tables);
							o.setTableNames();
							node.AddNode(o);
						}

						if (!(select.getDistinct() == null)) {
							// code here
							DistinctOperator d = new DistinctOperator(
									select.getDistinct(), tables);
							node.AddNode(d);
						}

						if (!(select.getLimit() == null)) {
							// code here
							LimitOperator l = new LimitOperator(
									select.getLimit(), tables);
							node.AddNode(l);
						}

						// System.out.println(select);
						// System.gc();
					}
				}
			} // end of Try
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		// System.out.println("RA Tree created");
		// Access RATree here
		// ArrayList<Operators> operatorList = new ArrayList<Operators>();
		
//		for(int i=0; i< OperatorLists.getJoinConditionList().size(); i++)
//		{
//			System.out.println(OperatorLists.getJoinConditionList().get(i));
//		}
		
		Enumeration en = ((DefaultMutableTreeNode) node.getNode())
				.postorderEnumeration();
		// System.out.println("Node is not null");
		int currentJoin = 0; // location in arrayList for Join condition

		if (en.hasMoreElements()) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) en
					.nextElement();

			if (child.getUserObject() instanceof RelationOperator) {
				// System.out.println("Relation Operator obtained");
				RelationOperator r = (RelationOperator) child.getUserObject();
				String tableName = r.getTable().getName().toUpperCase();

				String filepath = datadir.getAbsolutePath() + File.separator
						+ tableName + ".dat";

				if (aliasingExists) {
					tableName = r.getTable().getAlias().toUpperCase();
				} else {
					// do nothing
				}

				// String filepath =
				// "D:\\MS - CSE\\Spring 15\\CSE 562 DBS\\Project\\Files\\tpch_2_17_0\\tpch_2_17_0\\ref_data\\1\\customer.tbl.1";

				TreeMap<Integer, ArrayList<HashMap<Column, String>>> tableJoin = null;
				Column colLeft = null;
				Column colRight = null;
//				JoinOperator join = null;
				ArrayList<String> tableNamesList = new ArrayList<String>();
				tableNamesList.add(tableName);
				String getResults = "";
				GroupByOperator g = null;
				ProjectOperator p = null;
//				ArrayList<HashMap<Column, String>> tupleSet = new ArrayList<HashMap<Column, String>>();
				ArrayList<HashMap<Column, String>> tupleSet = null;
				boolean endOfExecutionFlag = false;
				ArrayList<String> outputList = new ArrayList<String>();
				ArrayList<Column> allColList = new ArrayList<Column>();

				while (child.getParent() != null) {
					// System.out.println("Parent is not null");
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) child
							.getParent();
					if (parent.getUserObject() instanceof JoinOperator) {
						// System.out.println("Join detected");
						// DefaultMutableTreeNode parent =
						// (DefaultMutableTreeNode) child.getParent();
						DefaultMutableTreeNode sibling = (DefaultMutableTreeNode) en.nextElement();
						parent = (DefaultMutableTreeNode) en.nextElement();

						Expression joinExpression = null;

						// External Sort Start

						
						TreeMap<Integer, ArrayList<HashMap<Column, String>>> tableLeft = null;

						if (currentJoin == 0) {
							
							ExternalSort sort = new ExternalSort(dataType);
//							System.out.println("In join"+ currentJoin + ": " + OperatorLists.getJoinConditionList().get(currentJoin + 1));
							joinExpression = OperatorLists.getJoinConditionList().get(currentJoin + 1);
							
							if (tableName.equalsIgnoreCase(((Column) ((BinaryExpression) joinExpression)
											.getLeftExpression()).getTable()
											.toString())) {
								colLeft = ((Column) ((BinaryExpression) joinExpression)
										.getLeftExpression());
								colRight = ((Column) ((BinaryExpression) joinExpression)
										.getRightExpression());
							} else if (tableName
									.equalsIgnoreCase(((Column) ((BinaryExpression) joinExpression)
											.getRightExpression()).getTable()
											.toString())) {
								colLeft = ((Column) ((BinaryExpression) joinExpression)
										.getRightExpression());
								colRight = ((Column) ((BinaryExpression) joinExpression)
										.getLeftExpression());
							} else {
								// do something else for now
							}

							ArrayList<Column> colList = new ArrayList<Column>();
							CreateTable crtTab = tables.get(tableName);
							List<ColumnDefinition> colDef = crtTab.getColumnDefinitions();
							Table tab = new Table(null, tableName);
							for(int i=0; i<colDef.size(); i++)
							{
								Column c = new Column(tab, colDef.get(i).getColumnName());
								colList.add(c);
								if(c.getWholeColumnName().equalsIgnoreCase(colLeft.getWholeColumnName()))
								{
									colLeft = c;
								}
							}
							
							allColList.addAll(colList);
								
							tableLeft = sort.GraceHybridSort(filepath, colList, colLeft, r.getRowCondition(), r.getOrCondition());
//							 System.out.println("First sort completed successfully");
						} // end of if(currentJoin == 0)
						else {
							
							tableLeft = tableJoin;
//							System.out.println("Table returned by join is of size: " + tableJoin.size());
							
						}// end of if(currentJoin != 0)

						RelationOperator s = (RelationOperator) sibling
								.getUserObject();
						String tableNameS = s.getTable().getName()
								.toUpperCase();

						// if(tableNameS.equalsIgnoreCase("NATION"))
						// {
						// System.out.println(1);
						// }

						String filepathS = datadir.getAbsolutePath()
								+ File.separator + tableNameS + ".dat";

						if (aliasingExists) {
							tableNameS = s.getTable().getAlias().toUpperCase();
						} else {
							// do nothing
						}

						tableNamesList.add(tableNameS);
						
						currentJoin++;
						
//						Column joinCol = null;
						Column joinColLeft = null;
						Column joinColRight = null;
						
						if(((DefaultMutableTreeNode) parent.getParent()).getUserObject() instanceof JoinOperator)
						{
							joinExpression = OperatorLists.getJoinConditionList().get(currentJoin + 1);
//							System.out.println("joinExpression: " + joinExpression);
							
							joinColLeft = ((Column) ((BinaryExpression) joinExpression)
									.getLeftExpression());


							joinColRight = ((Column) ((BinaryExpression) joinExpression)
									.getRightExpression());

								if (tableNamesList
										.contains(((Column) ((BinaryExpression) joinExpression)
												.getLeftExpression())
												.getTable().getName())) {
									// already assigned
									// System.out.println((1));
								} else if (tableNamesList
										.contains(((Column) ((BinaryExpression) joinExpression)
												.getRightExpression())
												.getTable().toString())) {

									Column colTemp = joinColLeft;
									joinColLeft = joinColRight;
									joinColRight = colTemp;
								} else {
									// do something else for now
								}
//							}
						}
												
						ArrayList<Column> colList = new ArrayList<Column>();
						CreateTable crtTab = tables.get(tableNameS);
						ArrayList<ColumnDefinition> colDef = (ArrayList<ColumnDefinition>) crtTab.getColumnDefinitions();
						Table tab = new Table(null, tableNameS);
						
						boolean flag = false;
						
						for(int i=0; i<colDef.size(); i++)
						{
							Column c = new Column(tab, colDef.get(i).getColumnName());
							colList.add(c);
							if(c.getWholeColumnName().equalsIgnoreCase(colRight.getWholeColumnName()))
							{
								colRight = c;
							}
							
							if(joinColLeft != null)
							{
								if(c.getWholeColumnName().equalsIgnoreCase(joinColLeft.getWholeColumnName()))
								{
									joinColLeft = c;
									flag = true;
								}
							}
							
						}
						
						allColList.addAll(colList);
						
						if(!flag && joinColLeft != null)
						{
//							colList = null;
							for(Column col : allColList)
							{
								if(col.getWholeColumnName().equalsIgnoreCase(joinColLeft.getWholeColumnName()))
								{
									joinColLeft = col;
									break;
								}
							}
						}
						
//						System.out.println("JoinColLeft: " + joinColLeft);
						JoinOperator j = (JoinOperator) parent.getUserObject();
						tableJoin = j.GraceHybridJoin(tableLeft, filepathS, colList, colRight, joinColLeft, s.getRowCondition(), s.getOrCondition());

						// System.out.println("Tables joined");

						// Merge Join End

//						currentJoin++;
						child = parent;
						colRight = joinColRight;
//						colJoinLeft = colLeft;
//						colJoinRight = colRight;
//						join = j;

					}// end of if(parent.getUserObject() instanceof
						// JoinOperator)
					else {
						child = (DefaultMutableTreeNode) en.nextElement();

						if (child.getUserObject() instanceof SelectOperator) {
							SelectOperator sel = (SelectOperator) child
									.getUserObject();

							// System.out.println("Some where conditions were not pushed down");


							if (tableJoin != null) {
								tupleSet = tableJoin.get(1);
								ScanOperator sc = new ScanOperator(dataType);

									ArrayList<HashMap<Column, String>> tempTupleSet = tupleSet;
									tupleSet = null;
									tupleSet = new ArrayList<HashMap<Column,String>>();
									
									 for(HashMap<Column, String> tupleEntry: tempTupleSet)
									 {
										 LeafValue flag = sc.accept(sel, tupleEntry);
//
										 if (flag.toString()
												 .equals("TRUE")
												 ) {
											 tupleSet.add(tupleEntry);
										
										 }
										 else
										 {
											// do nothing
//												// System.out.println(1);
										 }

								}
							}// end of if(tableJoin != null)
							else {

							}// end of if(tableJoin == null)

						}

						else if (child.getUserObject() instanceof GroupByOperator) {
							g = (GroupByOperator) child.getUserObject();

							// System.out.println("Group by detected");

							child = (DefaultMutableTreeNode) en.nextElement();
							
							if (child.getUserObject() instanceof ProjectOperator) {
								p = (ProjectOperator) child.getUserObject();

//								HashMap<Column, String> tuple = new HashMap<Column, String>();
								 if(tupleSet != null)
								 {
									 //do nothing
								
									 // System.out.println("TupleSet exists");
									 for (HashMap<Column, String> tupleEntry : tupleSet) {
											g.classify(tupleEntry, p);
										}
								 }
								 else
								 {
									if (tableJoin != null) {
										// System.out.println("Tuple to be created from Join table");
										// create tuple here
										tupleSet = tableJoin.get(1);
										for (HashMap<Column, String> tupleEntry : tupleSet) {
											g.classify(tupleEntry, p);
										}
									}// end of if(tableJoin != null)
									else {
										// System.out.println("Tuple is created from data file");
										// create tuple here
										tupleSet = new ArrayList<HashMap<Column,String>>();
										FileReader fr = null;
										BufferedReader br = null;
										try {
											fr = new FileReader(filepath);
											br = new BufferedReader(fr);
										} catch (FileNotFoundException e) {
											// TODO Auto-generated catch block
											// System.out.println("Error in File Reader");
											e.printStackTrace();
										}
	
										String line;
										// int count = 0;
										// int lineNull = 0;
										List<ColumnDefinition> colDef = null;
	
										ArrayList<Column> tupleCols = new ArrayList<>();
	
										if (tables.containsKey(tableName)) {
											// if(count ==0)
											// {
											// System.out
											// .println("Schema contains the given tablename");
											// count++;
											// }
											ct = tables.get(tableName);
											colDef = ct.getColumnDefinitions();
	
											for (int j = 0; j < colDef.size(); j++) {
												Column c = new Column(
														ct.getTable(), colDef
																.get(j)
																.getColumnName());
												tupleCols.add(c);
											}
										} else {
											try {
												throw new Exception(
														"Schema doesn't contain the given tableName: "
																+ tableName);
											} catch (Exception e) {
												// TODO Auto-generated catch block
												System.out
														.println("Schema doesn't contain the given tableName: "
																+ tableName);
												e.printStackTrace();
											}
										}
	
										try {
											while ((line = br.readLine()) != null) {
												boolean flag = false;
												HashMap<Column, String> tuple = new HashMap<Column, String>();
												if (!(line.equals(""))) {
													// if(lineNull == 0)
													// {
													// lineNull++;
													// }
	//												tuple = new HashMap<Column, String>();
													String colValues[] = line
															.split("\\|");
													// if
													// (tables.containsKey(tableName))
													// {
													// if(count ==0)
													// {
													// System.out
													// .println("Schema contains the given tablename");
													// count++;
													// }
													// ct = tables.get(tableName);
													// List<ColumnDefinition> colDef
													// = ct.getColumnDefinitions();
													//
													for (int j = 0; j < colDef
															.size(); j++) {
														// Column c = new Column(
														// ct.getTable(),
														// colDef.get(j).getColumnName());
														// c.setColumnName(colDef.get(j)
														// .getColumnName());
														// c.setTable(ct.getTable());
														tuple.put(tupleCols.get(j),
																colValues[j]);
													}
	
													LeafValue leafval = null;
													ScanOperator so = new ScanOperator(dataType);
													if (r.getRowCondition() == null) {
														// do nothing
													} else {
														for (Expression exp : r
																.getRowCondition()) {
															leafval = so.accept(
																	exp, tuple);
															if ((leafval.toString().equalsIgnoreCase("true"))) {
																// do nothing
															} else {
																flag = true;
																break;
															}
														}
													}
	
													if (!flag) {
														// tupleSet.add(tuple);
														g.classify(tuple, p);
														
													}
												}// end of if(!(line.equals("")))
												else {
													// if(lineNull == 0)
													// {
													// System.out
													// .println("Line blank detected initially");
													// }
												}
												
//												for (HashMap<Column, String> tupleEntry : tupleSet) {
//													g.classify(tupleEntry, p);
//												}
											}// end of while readline
												// if(tupleSet.size() != 0)
											// {
											// System.out.println("Tuples created successfully from data file in Groupby");
											// }
											// else
											// {
											// System.out.println("Tuples were NOT created from data file in Groupby");
											// }
										} catch (IOException e) {
											// TODO Auto-generated catch block
											System.out
													.println("Error while reading from Buffer");
											e.printStackTrace();
										} // end of while ((line = br.readLine()) !=
											// null)
									}// end of if(tableJoin == null)
										// System.out.println("TupleSet created for GroupBy from file");
								 }//end of if(tupleSet == null)

//								for (HashMap<Column, String> tupleEntry : tupleSet) {
//									g.classify(tupleEntry, p);
//								}

								getResults = "GROUPBY";

							}// end of if(child.getUserObject() instanceof
								// ProjectOperator)
							else {
								// do nothing for now
								// System.out.println("Project Operator was not detected in the tree. Hence Group By was not executed");
							}
						}// end of if(child.getUserObject() instanceof
							// GroupByOperator)

						else if (child.getUserObject() instanceof ProjectOperator) {
							p = (ProjectOperator) child.getUserObject();

							HashMap<Column, String> tuple = null;

							if (tableJoin != null) {

							}// end of if(tableJoin != null)
							else {
								// create tuple here
								FileReader fr = null;
								BufferedReader br = null;
								try {
									fr = new FileReader(filepath);
									br = new BufferedReader(fr);
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									System.out.println("Error in File Reader");
									e.printStackTrace();
								}

								String line;

								try {
									
									tupleSet = new ArrayList<HashMap<Column, String>>();
									
									while ((line = br.readLine()) != null) {
										boolean flag = false;
										if (!(line.equals(""))) {
											tuple = new HashMap<Column, String>();
											String colValues[] = line
													.split("\\|");
											if (tables.containsKey(tableName)) {
												ct = tables.get(tableName);
												List<ColumnDefinition> colDef = ct
														.getColumnDefinitions();

												for (int j = 0; j < colDef
														.size(); j++) {
													Column c = new Column(
															ct.getTable(),
															colDef.get(j)
																	.getColumnName());
													// c.setColumnName(colDef.get(j)
													// .getColumnName());
													// c.setTable(ct.getTable());
													tuple.put(c, colValues[j]);
												}

												LeafValue leafval = null;
												ScanOperator so = new ScanOperator(dataType);
												if (r.getRowCondition() == null) {

												} else {
													for (Expression exp : r
															.getRowCondition()) {
														leafval = so.accept(
																exp, tuple);
														if ((leafval.toString()
																.equalsIgnoreCase("'true'"))
																|| (leafval
																		.toString()
																		.equalsIgnoreCase("true"))) {
															// do nothing
														} else {
															flag = true;
															break;
														}
													}
												}

												if (!flag) {
													tupleSet.add(tuple);
												}
											}
										}// end of if(!(line.equals("")))
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									System.out
											.println("Error while reading from Buffer");
									e.printStackTrace();
								} // end of while ((line = br.readLine()) !=
									// null)
							}// end of if(tableJoin == null)

							for (HashMap<Column, String> tupleEntry : tupleSet) {
								boolean flag = p.classify(tupleEntry);

								if (flag) {
									getResults = "PROJECT";
								} else {
									if (endOfExecutionFlag
											|| getResults
													.equals("PROJECTEDLIST")) {
										// do nothing
									} else {
										child = (DefaultMutableTreeNode) en
												.nextElement();
									}

									if (child.getParent() == null) {
										System.out.println(p.getResult());
										endOfExecutionFlag = true;
									} else {
//										projectedList.add(p.getResult());
										getResults = "PROJECTEDLIST";
									}
								}
							}// end of for(HashMap<Column, String> tupleEntry:
								// tupleSet)

						}// end of if(child.getUserObject() instanceof
							// ProjectOperator)
						else if (child.getUserObject() instanceof OrderByOperator) {
							// System.out.println("OrderBy starts here");
							OrderByOperator o = (OrderByOperator) child
									.getUserObject();
							List<Expression> projColOrder = p.getList();
							List<Expression> groupByColOrder = g.getList();
							List<?> orderByList = o.getOrderOfElements();
//							ArrayList<Integer> colIndex = new ArrayList<Integer>();
//
//							for (Object e : orderByList) {
//								colIndex.add(colOrder.indexOf(e));
//							}

							
							if (getResults.equals("GROUPBY")) {
								// System.out.println("Ordering results from GroupBy");
								ArrayList<String> gb_result = g.getResult();

								ArrayList<String> orderByResult = o.order(projColOrder, groupByColOrder, gb_result);
								
								if (child.getParent() == null) {
									for (String str : orderByResult) {
										System.out.println(str);
									}
						
									endOfExecutionFlag = true;
								} else {
									// System.out.println("Results will be displayed after execution of the next operator");
									 getResults = "";
									outputList = orderByResult;
								}
							}// end of if(getResults.equals("GROUPBY"))
							else if (getResults.equals("PROJECT")) {
								// modify this to handle order by
								 if(child.getParent() == null)
								 {
									 System.out.println(p.getResult());
									 endOfExecutionFlag = true;
								 }
								 else
								 {
								 //do nothing for now
								 }
							}// end of if(getResults.equals("PROJECT"))
							else if (getResults.equals("PROJECTEDLIST")) {
								// for(String projectStr: projectedList)
								// {
								// String[] colValues = projectStr.split("\\|");
								// for now assume the tuple is recreated for
								// OrderBy

								// implement this later
								// }
							} else {
								// do nothing for now
								// System.out.println("Reached orderBy without GroupBy");
							}
						}// end of if (child.getUserObject() instanceof
							// OrderByOperator)
						else if (child.getUserObject() instanceof LimitOperator) {
							// System.out.println("Limit Operator detected");
							LimitOperator l = (LimitOperator) child
									.getUserObject();
							long limitOuptut = (long) l.getLim().getRowCount();

							if (getResults.equals("GROUPBY")) {
								// System.out.println("Limiting results from GroupBy");
								ArrayList<String> gb_result = g.getResult();
								// ArrayList<String> gbOutput = new
								// ArrayList<String>();
								if (child.getParent() == null) {
									for (String str : gb_result) {
										System.out.println(str);

										--limitOuptut;
										if (limitOuptut == 0) {
											break;
										}
									}

									// }
									endOfExecutionFlag = true;
								} else {
									// do nothing for now
									// System.out.println("Don't know why the code reached here");
								}
							}// end of if(getResults.equals("GROUPBY"))
							else if (getResults.equals("PROJECT")) {
								// System.out.println("Posting limited results from Project");
								// modify this to handle order by
								if (child.getParent() == null) {
									System.out.println(p.getResult());
									endOfExecutionFlag = true;
								} else {
									// do nothing for now
								}
							}// end of if(getResults.equals("PROJECT"))
							else if (getResults.equals("PROJECTEDLIST")) {
								// System.out.println("Posting limited results from Projected List");
//								for (String projectStr : projectedList) {
//									System.out.println(projectStr);
//									--limitOuptut;
//
//									if (limitOuptut == 0) {
//										break;
//									}
//								}
								endOfExecutionFlag = true;
							} else {
								// do nothing for now
								// System.out.println("No GroupBy was detected in the code");
								if (child.getParent() == null) {
									for (String str : outputList) {
										System.out.println(str);

										--limitOuptut;
										if (limitOuptut == 0) {
											break;
										}
									}

									// }
									endOfExecutionFlag = true;
								} 
							}
						} else {
							// do nothing for now

							// System.out.println("Don't know why the code reached here");
						}
					}// end of if(!(parent.getUserObject() instanceof
						// JoinOperator))

				}// end of while(child.getParent() != null)

				if (endOfExecutionFlag) {
					// do nothing
					// System.out.println("Everything should have been displayed by now");
				} else {
					// System.out.println("End of execution posting results now");
					if (getResults.equals("GROUPBY")) {
						// System.out.println("End of execution posting results from GroupBy");
						ArrayList<String> gb_result = g.getResult();

						for (String s : gb_result) {
							System.out.println(s);
						}
					} else if (getResults.equals("PROJECT")) {
						
						System.out.println(p.getResult());

					} else if (getResults.equals("PROJECTEDLIST")) {
						
					}
					else
					{
						ArrayList<String> orderByResult = outputList;

						for (String s : orderByResult) 
						{
							System.out.println(s);
						}
					}
						
				}
			}// end of if(child.getUserObject() instanceof RelationOperator)
		} // end of if (en.hasMoreElements())

		// timeNow = System.currentTimeMillis();
		// System.out.println(new Date(timeNow));

		// System.out.println();
		// long endTime = System.nanoTime() - timeNow;
		// System.out.println(endTime);

//		date = new java.util.Date();
//		System.out.println(new Timestamp(date.getTime()));

		System.exit(0);
	}

	private static List<Expression> splitAndClauses(Expression where) {
		// TODO Auto-generated method stub

		List<Expression> whereList = new ArrayList<Expression>();

		if (where instanceof AndExpression) {
			whereList
					.addAll((Collection<? extends Expression>) splitAndClauses(((AndExpression) where)
							.getLeftExpression()));

			whereList
					.addAll((Collection<? extends Expression>) splitAndClauses(((AndExpression) where)
							.getRightExpression()));
		} else {
			whereList.add(where);
		}

		return whereList;
	}
	
	private static List<Expression> splitAndClausesOrExp(Expression where) {
		List<Expression> orList = new ArrayList<Expression>();
//			if ((where instanceof Parenthesis)) {

		if (where instanceof OrExpression) {
		orList.addAll((Collection<? extends Expression>) splitAndClausesOrExp(((OrExpression) where).getLeftExpression()));

		orList.addAll((Collection<? extends Expression>) splitAndClausesOrExp(((OrExpression) where)
		.getRightExpression()));
		}
		else
		{
		orList.add(where);
		}
//			}
		return orList;
		}

}