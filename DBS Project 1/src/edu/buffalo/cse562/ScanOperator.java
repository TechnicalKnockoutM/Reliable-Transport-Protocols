package edu.buffalo.cse562;


import java.sql.SQLException;
import java.util.*;
import java.lang.String;












import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.expression.*;


public class ScanOperator extends Eval {

//	private HashMap<String, CreateTable> tables;
	private HashMap<String, String> dataTypesMap = new HashMap<String, String>();
	private HashMap<Column, String> data;
	LeafValue leafvalue = null;
	
	public ScanOperator( HashMap<String, String> dataType) {
		// TODO Auto-generated constructor stub
//		this.tables = tables;
		this.data = null;
		this.dataTypesMap = dataType;
	}

	public LeafValue accept(Operators o, HashMap<Column, String> data) {
		LeafValue l = null;
		this.data = data;
		try {
			for(Expression exp : ((SelectOperator) o).getWhere())
			{
				l= eval(exp);
			}
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		return eval(e);
		return l;
	}
	
	public LeafValue accept(Expression exp, HashMap<Column, String> data) {
		LeafValue l = null;
		this.data = null;
		this.data = data;
		try {
			 l= eval(exp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		return eval(e);
		return l;
	}

	@Override
	public LeafValue eval(Column arg0) throws SQLException {
		// TODO Auto-generated method stub

//		String columnName = arg0.getColumnName();
		String tableName = "";
		LeafValue l = null;
//		if(arg0.getTable().getName() == null)
//		{
//			if(tables.size() == 1)
//			{
//				for(Map.Entry<String, CreateTable> schema : tables.entrySet())
//				{
//					tableName = schema.getKey();
//				}
//			}
//			else
//			{
////				try {
////					throw new Exception("TableName is null for column value: " + arg0.getColumnName());
////				} catch (Exception e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//			}
//		}
//		else
//		{
//			tableName = arg0.getTable().getName().toUpperCase();
//		}
//		
//		arg0.getTable().setName(tableName);
//		CreateTable ct = new CreateTable();
		
//		for (Map.Entry<String, CreateTable> m : tables.entrySet()) {
//			
//			ct = m.getValue();
//			List<ColumnDefinition> cd = ct.getColumnDefinitions();
//			for(int i=0; i<cd.size(); i++)
//			{
//				if(cd.get(i).getColumnName().equals(columnName))
//				{
//					tableName = m.getKey();
//					break;
//				}
//			}
//		}	
//		arg0.getTable().setName(tableName);
		l = readtuple(arg0);
		return l;

	}

	@SuppressWarnings("unchecked")
	private LeafValue readtuple(Column arg0) {
		// TODO Auto-generated method stub
		String dataType = dataTypesMap.get(arg0.getWholeColumnName());
		String value = "";
//		String dataType = colDef.indexOf(arg0.getColumnName()
		
//		Column valueColumn = new Column(arg0.getTable(), arg0.getColumnName());

//		for(int i=0; i<colDef.size(); i++)
//		{
//			dataType.add(colDef.get(i).getColDataType().toString());
//			colName.add(colDef.get(i).getColumnName());
//		}
		
		//Organizes data in tuple according to sequence of schema
		
		for (Map.Entry<Column, String> m : data.entrySet()) {
//			count++;
			if (m.getKey().getWholeColumnName().equalsIgnoreCase(arg0.getWholeColumnName())) {
//				System.out.println(m.getKey() + m.getValue());
					value = m.getValue();
					break;
				}
			}
//		for (int i = 0; i < al.size(); ++i, j++) {
//		String s = dataType.get(index).toLowerCase();
//		String s = "";
		if(dataType.contains("varchar"))
		{
			dataType="varchar";
		}
		else if(dataType.contains("char"))
		{
			dataType="char";
		}
		
			switch (dataType) {

			case "string":
				String str = " " + value + " ";
				leafvalue = new StringValue(str);
				break;
			case "char":
				String str1 = " " + value + " ";
				leafvalue = new StringValue(str1);
				break;
			case "varchar":
				String str2 = " " + value + " ";
				leafvalue = new StringValue(str2);
				break;
			case "int":
				leafvalue = new LongValue(value);
				break;
			case "float":
				leafvalue = new DoubleValue(value);
				break;
			case "decimal":
				leafvalue = new DoubleValue(value);
				break;
			 case "boolean":
//			 leafvalue = new BooleanValue(al.get(index));
			 break;

			case "date":
				String date = " " + value + " ";
				leafvalue = new DateValue(date);
//				leafvalue = new DateValue(" DATE(" + leafvalue.toString() + ") ");
				break;
			default:
				break;
			}
//		}

		return leafvalue;

	}

//	@SuppressWarnings("unchecked")
//	public LeafValue readtuple(String ColName, String TableName) {
//		ArrayList<String> al = new ArrayList<String>();
//		List<ColumnDefinition> colDef = new ArrayList<ColumnDefinition>();
//		List<String> dataType = new ArrayList<String>();
//		List<String> colName = new ArrayList<String>();
//		int index = 0;
////		for (Map.Entry<Column, String> m : data.entrySet()) {
////
////			al.add(m.getValue()); //Stores values of columns in the tuple
////		}
////		leafvalue = new LeafValue[al.size()];
////		for (Map.Entry<String, CreateTable> m : tables.entrySet()) {
////			if (m.getKey().equalsIgnoreCase(TableName)) {
////				colDef = m.getValue().getColumnDefinitions(); //gets column definition from createTable
////				break;
////			}
////		}
//		
//		colDef = tables.get(TableName).getColumnDefinitions();
//		
//		
////		Column valueColumn = new Column(tables.get(key))
//
//		for(int i=0; i<colDef.size(); i++)
//		{
//			dataType.add(colDef.get(i).getColDataType().toString());
//			colName.add(colDef.get(i).getColumnName());
//		}
//		
//		//Organizes data in tuple according to sequence of schema
//		
//		for (int i = 0; i < colName.size(); i++) {
//		for (Map.Entry<Column, String> m : data.entrySet()) {
//			String tmp_col = m.getKey().getColumnName().toString();
//			
//				
//
//				if (colName.get(i).equals(tmp_col)) {
//					al.add(data.get(m.getKey()));
//					break;
//				}
//			}
//		}
//		index = colName.indexOf(ColName);
////		index = (index - 2) / 2;
//
////		int j = 1;
////		for (int i = 0; i < al.size(); ++i, j++) {
//		String s = dataType.get(index).toLowerCase();
//		if(s.contains("char"))
//		{
//			s="char";
//		}
//		else if(s.contains("varchar"))
//		{
//			s="varchar";
//		}
//		
//			switch (s) {
//
//			case "string":
//				String str = " " + al.get(index) + " ";
//				leafvalue = new StringValue(str);
//				break;
//			case "char":
//				String str1 = " " + al.get(index) + " ";
//				leafvalue = new StringValue(str1);
//				break;
//			case "varchar":
//				String str2 = " " + al.get(index) + " ";
//				leafvalue = new StringValue(str2);
//				break;
//			case "int":
//				leafvalue = new LongValue(al.get(index));
//				break;
//			case "float":
//				leafvalue = new DoubleValue(al.get(index));
//				break;
//			case "decimal":
//				leafvalue = new DoubleValue(al.get(index));
//				break;
//			 case "boolean":
////			 leafvalue = new BooleanValue(al.get(index));
//			 break;
//
//			case "date":
//				String date = " " + al.get(index) + " ";
//				leafvalue = new DateValue(date);
////				leafvalue = new DateValue(" DATE(" + leafvalue.toString() + ") ");
//				break;
//			default:
//				break;
//			}
////		}
//
//		return leafvalue;
//
//	}

}