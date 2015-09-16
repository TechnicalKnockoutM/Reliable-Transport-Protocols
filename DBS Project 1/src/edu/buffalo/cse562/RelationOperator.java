package edu.buffalo.cse562;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class RelationOperator extends Operators {
	
	private Table table;
	
	private ArrayList<Expression> rowCondition = new ArrayList<Expression>();
	private ArrayList<Expression> orCondition = new ArrayList<Expression>();
	public ArrayList<Expression> getOrCondition() {
		return orCondition;
	}

	public void setOrCondition(ArrayList<Expression> orCondition) {
		this.orCondition = orCondition;
	}

	OperatorLists opListSingletonObj = OperatorLists.getInstance();
	
	public RelationOperator(Table tbl, HashMap<String, CreateTable> schema)
	{
		super(schema);
		table = tbl;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
	
	public ArrayList<Expression> getRowCondition() {
		return rowCondition;
	}

	public void setRowCondition(ArrayList<Expression> rowCondition) {
		this.rowCondition = rowCondition;
	}
	
	public void ReadFile(String filepath) {
//		try {
//
//			BufferedReader bufferReader;
//			FileReader inputFile = null;
//			LeafValue[] leafvalue;
//
//			inputFile = new FileReader(filepath);
//
//			bufferReader = new BufferedReader(inputFile);
//
//			String line;
//			while ((line = bufferReader.readLine()) != null) {
//				System.out.println(line);
//				// al.add(line);
//				int k = 0;
//				String cols[] = line.split("\\|");
//				List<String> l = new ArrayList<String>();
//				// int[] ret_int = new int[cols.length];
//				leafvalue = new LeafValue[cols.length];
//			}
//
//			// System.out.println(leafvalue[0]);
//			// break;
//			// }
//
//			// Close the buffer reader
//			bufferReader.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			System.out.println("Error while reading file line by line:"
//					+ e.getMessage());
//		}
//
//		// System.out.println("");
	}

//	public boolean check_tuple(HashMap<Column, String> tuple) {
//		boolean pass_tuple = true;
//		RelationOperator ro = new RelationOperator(table, getSchema());
//		ScanOperator so = new ScanOperator(getSchema());
//		Expression exp = (Expression) ro.getRowCondition();
//		LeafValue lv = so.accept(exp, tuple);
//		if (lv.toString().equalsIgnoreCase("false"))
//		pass_tuple = false;
//
//		return pass_tuple;
//		}


}