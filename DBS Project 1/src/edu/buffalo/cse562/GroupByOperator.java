//package edu.buffalo.cse562;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.Function;
//import net.sf.jsqlparser.expression.LeafValue;
//import net.sf.jsqlparser.expression.LongValue;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.statement.create.table.CreateTable;
//import net.sf.jsqlparser.statement.select.SelectExpressionItem;
//
//public class GroupByOperator extends Operators {
//	
//	List<Expression> selectClause;
//	List<Expression> groupingClause;
//	List<Expression> list = null;
//	Boolean flag = false;
////	private String final_output = null;
//	String temp = null;
//	String current_key=null;
//	int count_output = 0;
//	int count_column=0;
////	private HashMap<String, String> groupby_output = new HashMap<String, String>();
//	private HashMap<String, ArrayList<String>> groupby_output_temp = new HashMap<String, ArrayList<String>>();
//	ArrayList<String> col_check = new ArrayList<String>();
//	ArrayList<String> check = new ArrayList<String>();
//	List<String> expression_list =null;
//	HashMap<String, String> dataType  = null;
//
//	private HashMap<String, LeafValue> output = new HashMap<String, LeafValue>();
//
//	public GroupByOperator(List<Expression> groupByColumnReferences, HashMap<String, CreateTable> schema, HashMap<String, String> dataType) {
//		// TODO Auto-generated constructor stub
//		super(schema);
//		groupingClause  = groupByColumnReferences;
//		this.dataType = dataType;
//	}
//	public List<Expression> getList() {
//		return groupingClause;
//	}
//
//	public void setList(List<Expression> list) {
//		this.groupingClause = list;
//	}
//	public void group(HashMap<Column, String> tuple, ProjectOperator p) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	public Boolean classify(HashMap<Column, String> data,  Operators op) {
//		count_output = 0;
//		temp = null;
//		current_key=null;
//		count_column=0;
//		expression_list = new ArrayList<String>();
//		List<Expression> exp = ((ProjectOperator) op).getList();
//		selectClause= exp;
//		for (int i = 0; i < exp.size(); i++) {
//			Function f = null;
//			
//			
//			if (((SelectExpressionItem) exp.get(i)).getExpression() instanceof Function) {
//				f = (Function) ((SelectExpressionItem) exp.get(i)).getExpression();
//				f.setName(f.getName().toUpperCase());
//				flag = true;
////				String val = f.toString();
////				expression_list.add(val);
//				String s = ((Function) ((SelectExpressionItem) exp.get(i)).getExpression()).getName();
//				if (s.equalsIgnoreCase("SUM")) {
//					sum(data, exp, f);
//				}
//				else if (s.equalsIgnoreCase("COUNT")) {
//				 count(data, exp, f);
//				 }
//				else if (s.equalsIgnoreCase("AVG")) {
//				 average(data, exp, f);
//				 }
//				expression_list.add(((SelectExpressionItem) exp.get(i)).getExpression().toString());
//			} else if (((SelectExpressionItem) exp.get(i)).getExpression() instanceof Column) {
//				columns(data, op, f, ((SelectExpressionItem) exp.get(i)).getExpression());
//			}
//
//		}
//		ArrayList <String> temp_check = new ArrayList<String>();
//		temp_check.add("0");
//		ArrayList<String> value = new ArrayList<String>();
//		for(int i=0;i<check.size();i++)
//		{
//			String s =(check.get(i));
//			
//			if(s.startsWith("AVG") || s.startsWith("avg"))
//			{
//				String temp_sum = output.get(check.get(i)).toString();
//
//				String val = temp_sum + "|" + count_output;
//				value.add(val);
//			}
//			else 
//			{
//				value.add(output.get(check.get(i)).toString());	
//			}
//			
//		}
//		
//		if(groupby_output_temp.containsKey(current_key))
//		{
//			ArrayList<String> combined_value = new ArrayList<String>();
//			if(groupby_output_temp.get(current_key).equals(temp_check))
//			{
//				groupby_output_temp.put(current_key, value);	
//			}
//			else
//			{
//				for (int i = 0; i < value.size(); i++) {
//					
//					if(check.get(i).contains("AVG") || check.get(i).contains("avg"))
//					{
//						String s_saved = groupby_output_temp.get(current_key).get(i);
//						String str_saved[] = s_saved.split("\\|");
//						String s_current = value.get(i);
//						String str_current[] = s_current.split("\\|");
//						Double temp_sum  = Double.parseDouble(str_saved[0]) + (Double.parseDouble(str_current[0]));
//						Double temp_count = Double.parseDouble(str_saved[1]) + (Double.parseDouble(str_current[1]));
//						String s = temp_sum + "|" + temp_count;
//						combined_value.add(s);
//					}
//					else
//					{
//						Double temp = (Double.parseDouble(value.get(i)) + Double.parseDouble(groupby_output_temp.get(current_key).get(0)));
//						combined_value.add(temp.toString());
//					}
//					
//				}
//				groupby_output_temp.put(current_key, combined_value);
//		}
//		}
//		else
//		{
//			//do nothing
//		}
//		
//		return flag;
//	}
//	private void columns(HashMap<Column, String> data, Operators op, Function f, Expression expr) {
//		// TODO Auto-generated method stub
//		count_column++;
//		ScanOperator sc = new ScanOperator(dataType);
//
//		LeafValue leafval = sc.accept(expr, data);
//		String final_output;
//		if(leafval.toString().startsWith("'") && leafval.toString().endsWith("'"))
//		{
//			final_output=(String) leafval.toString().subSequence(1, leafval.toString().length()-1);
//		}
//		else
//		{
//			final_output=leafval.toString();
//		}
//		
//
//		
//		String tmp = ((Column) expr).getColumnName();
//		ArrayList <String> temp_string = new ArrayList<String>();
//		temp_string.add("0");
//		if (col_check.contains(tmp)) {
//			// do nothing
//		} else {
//			col_check.add(tmp);
//		}
//
//		if(temp == null)
//		{
//		temp =final_output;
//		}
//		else 
//		{
//			temp=temp + "|" + final_output;
//		}
//		current_key=temp;
//
//		if (groupingClause.size() == count_column) {
//			if (groupby_output_temp.containsKey(temp)) {
//
//			} else {
//
//				groupby_output_temp.put(temp, temp_string);
//			}
//		}
//
//	}
//
//	private void average(HashMap<Column, String> data,
//			List<Expression> exp, Function f) {
//		// TODO Auto-generated method stub
//		Expression expr = ((Expression) f.getParameters().getExpressions()
//				.get(0));
//		ScanOperator sc = new ScanOperator(dataType);
//
//		LeafValue leafval = sc.accept(expr, data);
//		String tmp = "AVG(" + expr + ")";
//
//		if (check.contains(tmp)) {
//			// do nothing
//		} else {
//			check.add(tmp);
//		}
//		
//			output.put(tmp, leafval);
//		
//		if(count_output==0)
//		{
//		count_output++;
//		}
//			
//	}
//	private void count(HashMap<Column, String> data,
//			List<Expression> exp, Function f) {
//		// TODO Auto-generated method stub
//		
//		LeafValue leafval=null;
//		Expression expr = null;
//		String val = null;
//		if(f.isAllColumns())
//		{
//			val  = "(*)";
//			leafval = new LongValue(1);
//		}
//		else
//		{
//			expr  = ((Expression) f.getParameters().getExpressions().get(0));
//			val  = f.getParameters().toString();
//			ScanOperator sc = new ScanOperator(dataType);
//			leafval = sc.accept(expr, data);
//		}
//		
//		String tmp = "";
//		
//		if(leafval != null)
//		{
//			if(count_output==0)
//				{
//				count_output++;
//				}
//		}
//		
//		if(val.contains("|"))
//		{
//			
//		}
//		else
//		{
//			tmp = "COUNT" + val ;
////			String tmp = "COUNT_" + val;
//			if (check.contains(tmp)) {
//				// do nothing
//			} else {
//				check.add(tmp);
//			}
//			
//			LeafValue l = new LongValue(count_output);
//			output.put(tmp, l);
//		}		
//		
//	}
//	private void sum(HashMap<Column, String> data,
//			List<Expression> exp, Function f) {
//		// TODO Auto-generated method stub
//
//		Expression expr = ((Expression) f.getParameters().getExpressions()
//				.get(0));
//		ScanOperator sc = new ScanOperator(dataType);
//
//		LeafValue leafval = sc.accept(expr, data);
//
//		String tmp = "SUM(" + expr + ")";
//		if (check.contains(tmp)) {
//			// do nothing
//		} else {
//			check.add(tmp);
//		}
//		
//		output.put(tmp, leafval);
//		
//	}
//	
//	
//	public ArrayList<String> getResult() {
//
//		TreeMap<String, ArrayList<String>> op = new TreeMap<String, ArrayList<String>>();
//		
//		for (Map.Entry<String, ArrayList<String>> map : groupby_output_temp
//				.entrySet()) {
//			ArrayList<String> temp_list = new ArrayList<String>();
//			for (int j = 0; j < expression_list.size(); j++) {
//				if (expression_list.get(j).contains("AVG") || expression_list.get(j).contains("avg")) {
//					
//					int index = check.indexOf((expression_list.get(j)));
////					int index1=check.indexOf("COUNT" + map.getKey());
////					LeafValue l_temp1 = map.getValue().co;
////					LeafValue l_temp1 = new DoubleValue(map.getValue().get(index1));
//					String val = map.getValue().get(index);
//					String temp[] = val.split("\\|");
//					Double temp_sum = Double.parseDouble(temp[0]);
//					Double temp_count = Double.parseDouble(temp[1]);
////					Double d_avg = Double
////							.parseDouble(map.getValue().get(index));
////					Double d_count = null;
////					try {
////						d_count = l_temp1.toDouble();
////					} catch (InvalidLeaf e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
////					}
//
////					Double avg_res = (d_avg / d_count);
//					Double avg_res = temp_sum/temp_count;
//					temp_list.add(avg_res.toString());
////					op.put(map.getKey(), temp_list);
//				} 
////				else if (expression_list.get(j).contains("COUNT")) {
////					int index = check.indexOf("COUNT" + map.getKey());
//////					temp_list.add(groupby_output.get("COUNT" + map.getKey()).toString());
////					temp_list.add(map.getValue().get(index));
////					
////				}
//				else {
//				
//					int index = check.indexOf((expression_list.get(j)));
//
//					temp_list.add(map.getValue().get(index));
////					op.put(map.getKey(), temp_list);
//				}
//
//			}
//			op.put(map.getKey(), temp_list);
//		}
//		//Edit
//		ArrayList<String> output_old = new ArrayList<String>();
//		for(Map.Entry<String, ArrayList<String>> m : op.entrySet())
//		{
////			String[] colValues = m.getKey().split("\\|");
//			String output = m.getKey() ;
//			for(String s : m.getValue())
//			{
//				output = output + "|" + s;
//			}
//			output_old.add(output);
//			
//		}
//		
//		ArrayList<String> groupbyitems = new ArrayList<String>();
//		ArrayList<String> selectitems = new ArrayList<String>();
//		
//		TreeMap<String, ArrayList<String>> op_new = new TreeMap<String, ArrayList<String>>();
//		ArrayList<String> output_new = new ArrayList<String>();
//		boolean flag=true;
//		for (int i = 0; i < groupingClause.size(); i++) {
//			
//			Column col =((Column) groupingClause.get(i));
//			
////			if(col.getTable().getName()==null)
////			{
////				for(Map.Entry<String, CreateTable> mp : getSchema().entrySet())
////				col.getTable().setName(mp.getKey());
////			}
////			else
////			{
////			col.getTable().setName((col.getTable().getName().toUpperCase()));
////			}
//			
//			groupbyitems.add(col.getWholeColumnName());
//		}
//		for (int i = 0; i < expression_list.size(); i++) {
//			groupbyitems.add(expression_list.get(i));
//		}
//		for (int i = 0; i < selectClause.size(); i++) {
//			if (((SelectExpressionItem) selectClause.get(i)).getExpression() instanceof Function)
//			{
//				
//				selectitems.add(((SelectExpressionItem) selectClause.get(i)).getExpression().toString());
//			}
//			if (((SelectExpressionItem) selectClause.get(i)).getExpression() instanceof Column)
//			{
//				
//				selectitems.add(((SelectExpressionItem) selectClause.get(i)).getExpression().toString());	
//			}
//			
//		}
//		if (groupbyitems.equals(selectitems))
//		{
//			flag=false;
//		}
//		if(flag)
//		{
//			
//			for(Map.Entry<String,ArrayList<String>> mp : op.entrySet())
//			{
//				String[] split_key=mp.getKey().split("\\|");
//				ArrayList<String> split_value=mp.getValue();
//				ArrayList<String> resultant_list = new ArrayList<String>();
//				for(int i=0;i<split_key.length;i++)
//				{
//					resultant_list.add(split_key[i]);
//				}
//				resultant_list.addAll(split_value);
//				String result=null;
//				for(int i=0;i<groupbyitems.size();i++)
//				{
//					int index=groupbyitems.indexOf(selectitems.get(i));
//					if(result==null)
//					{
//						result=resultant_list.get(index);
//					}
//					else
//					{
//						result = result + "|" + resultant_list.get(index);
//					}
//				}
//				output_new.add(result);
//				
//			}
//			
//		}
//		if(flag)
//		{
//			return output_new;
//		}
//		else
//		{
//		return output_old;
//		}
////		return output_old;
//	}	}
//
//
//
//
package edu.buffalo.cse562;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class GroupByOperator extends Operators {
	
	List<Expression> selectClause;
//	ArrayList<String> selectitemorder = new ArrayList<String>();
	List<Expression> groupingClause;
	List<Expression> list = null;
	Boolean flag = false;
//	private String final_output = null;
	String temp = null;
	String current_key=null;
	int count_output = 0;
	int count_column=0;
//	private HashMap<String, String> groupby_output = new HashMap<String, String>();
	private HashMap<String, ArrayList<String>> groupby_output_temp = new HashMap<String, ArrayList<String>>();
	ArrayList<String> col_check = new ArrayList<String>();
	ArrayList<String> check = new ArrayList<String>();
	List<String> expression_list =null;
	HashMap<String, String> dataType  = null;

	private HashMap<String, LeafValue> output = new HashMap<String, LeafValue>();

	public GroupByOperator(List<Expression> groupByColumnReferences, HashMap<String, CreateTable> schema, HashMap<String, String> dataType) {
		// TODO Auto-generated constructor stub
		super(schema);
		groupingClause  = groupByColumnReferences;
		this.dataType = dataType;
	}
	public List<Expression> getList() {
		return groupingClause;
	}

	public void setList(List<Expression> list) {
		this.groupingClause = list;
	}
	public void group(HashMap<Column, String> tuple, ProjectOperator p) {
		// TODO Auto-generated method stub
		
	}
	
	public Boolean classify(HashMap<Column, String> data,  Operators op) {
		count_output = 0;
		temp = null;
		current_key=null;
		count_column=0;
		expression_list = new ArrayList<String>();
		List<Expression> proj_exp = ((ProjectOperator) op).getList();
		List<Expression> exp = new ArrayList<Expression>();
		exp.addAll(groupingClause);
		for(int i=0;i<proj_exp.size();i++)
		{
			if (((SelectExpressionItem) proj_exp.get(i)).getExpression() instanceof Function) {
				exp.add(((SelectExpressionItem) proj_exp.get(i)).getExpression());
			}
		}
//		List<Expression> exp = ((ProjectOperator) op).getList();
		selectClause= proj_exp;
		for (int i = 0; i < exp.size(); i++) {
			Function f = null;
			
			
			if ((exp.get(i)) instanceof Function) {
				f = (Function) (exp.get(i));
				f.setName(f.getName().toUpperCase());
				flag = true;
//				String val = f.toString();
//				expression_list.add(val);
//				if(selectitemorder.contains(((SelectExpressionItem) exp.get(i)).getExpression().toString()))
//				{
//					
//				}else
//				{
//				selectitemorder.add(((SelectExpressionItem) exp.get(i)).getExpression().toString());
//				}
				String s = ((Function) (exp.get(i))).getName();
				if (s.equals("SUM")) {
					sum(data, exp, f);
				}
				else if (s.equals("COUNT")) {
				 count(data, exp, f);
				 }
				else if (s.equals("AVG")) {
				 average(data, exp, f);
				 }
				expression_list.add( exp.get(i).toString());
			} else if ((exp.get(i)) instanceof Column) {
//				if(selectitemorder.contains(((SelectExpressionItem) exp.get(i)).getExpression().toString()))
//				{
//					
//				}
//				else
//				{
//				selectitemorder.add(((SelectExpressionItem) exp.get(i)).getExpression().toString());
//				}
				columns(data, op, f, (exp.get(i)));
			}

		}
		ArrayList <String> temp_check = new ArrayList<String>();
		temp_check.add("0");
		ArrayList<String> value = new ArrayList<String>();
		for(int i=0;i<check.size();i++)
		{
			String s =(check.get(i));
			
			if(s.startsWith("AVG"))
			{
				String temp_sum = output.get(check.get(i)).toString();

				String val = temp_sum + "|" + count_output;
				value.add(val);
			}
			else 
			{
				value.add(output.get(check.get(i)).toString());	
			}
			
		}
		
		if(groupby_output_temp.containsKey(current_key))
		{
			ArrayList<String> combined_value = new ArrayList<String>();
			if(groupby_output_temp.get(current_key).equals(temp_check))
			{
				groupby_output_temp.put(current_key, value);	
			}
			else
			{
				for (int i = 0; i < value.size(); i++) {
					
					if(check.get(i).contains("AVG"))
					{
						String s_saved = groupby_output_temp.get(current_key).get(i);
						String str_saved[] = s_saved.split("\\|");
						String s_current = value.get(i);
						String str_current[] = s_current.split("\\|");
						Double temp_sum  = Double.parseDouble(str_saved[0]) + (Double.parseDouble(str_current[0]));
						Double temp_count = Double.parseDouble(str_saved[1]) + (Double.parseDouble(str_current[1]));
						String s = temp_sum + "|" + temp_count;
						combined_value.add(s);
					}
					else
					{
						Double temp = (Double.parseDouble(value.get(i)) + Double.parseDouble(groupby_output_temp.get(current_key).get(i)));
						combined_value.add(temp.toString());
					}
					
				}
				groupby_output_temp.put(current_key, combined_value);
		}
		}
		else
		{
			//do nothing
		}
		
		return flag;
	}
	private void columns(HashMap<Column, String> data, Operators op, Function f, Expression expr) {
		// TODO Auto-generated method stub
		count_column++;
		ScanOperator sc = new ScanOperator(dataType);

		LeafValue leafval = sc.accept(expr, data);
		String final_output = "";
//		if(leafval.toString().startsWith("'") && leafval.toString().endsWith("'"))
		if(leafval.toString().startsWith("'"))
		{
			if(leafval.toString().endsWith("'"))
			{
			final_output=(String) leafval.toString().subSequence(1, leafval.toString().length()-1);
			}
			else
			{
				System.out.println("Error");
			}
		}
		else
		{
			final_output=leafval.toString();
		}
		

		
		String tmp = ((Column) expr).getWholeColumnName();
		ArrayList <String> temp_string = new ArrayList<String>();
		temp_string.add("0");
		if (col_check.contains(tmp)) {
			// do nothing
		} else {
			col_check.add(tmp);
		}

		if(temp == null)
		{
		temp =final_output;
		}
		else 
		{
			temp=temp + "|" + final_output;
		}
		current_key=temp;

		if (groupingClause.size() == count_column) {
			if (groupby_output_temp.containsKey(temp)) {

			} else {

				groupby_output_temp.put(temp, temp_string);
			}
		}

	}

	private void average(HashMap<Column, String> data,
			List<Expression> exp, Function f) {
		// TODO Auto-generated method stub
		Expression expr = ((Expression) f.getParameters().getExpressions()
				.get(0));
		ScanOperator sc = new ScanOperator(dataType);

		LeafValue leafval = sc.accept(expr, data);
		String tmp = "AVG(" + expr + ")";

		if (check.contains(tmp)) {
			// do nothing
		} else {
			check.add(tmp);
		}
		
			output.put(tmp, leafval);
		
		if(count_output==0)
		{
		count_output++;
		}
			
	}
	private void count(HashMap<Column, String> data,
			List<Expression> exp, Function f) {
		// TODO Auto-generated method stub
		
		LeafValue leafval=null;
		Expression expr = null;
		String val = null;
		if(f.isAllColumns())
		{
			val  = "(*)";
			leafval = new LongValue(1);
		}
		else
		{
			expr  = ((Expression) f.getParameters().getExpressions().get(0));
			val  = f.getParameters().toString();
			ScanOperator sc = new ScanOperator(dataType);
			leafval = sc.accept(expr, data);
		}
		
		String tmp = "";
		
		if(leafval != null)
		{
			if(count_output==0)
				{
				count_output++;
				}
		}
		
		if(val.contains("|"))
		{
			
		}
		else
		{
			tmp = "COUNT" + val ;
//			String tmp = "COUNT_" + val;
			if (check.contains(tmp)) {
				// do nothing
			} else {
				check.add(tmp);
			}
			
			LeafValue l = new LongValue(count_output);
			output.put(tmp, l);
		}		
		
	}
	private void sum(HashMap<Column, String> data,
			List<Expression> exp, Function f) {
		// TODO Auto-generated method stub

		Expression expr = ((Expression) f.getParameters().getExpressions()
				.get(0));
		ScanOperator sc = new ScanOperator(dataType);

		LeafValue leafval = sc.accept(expr, data);

		String tmp = "SUM(" + expr + ")";
		if (check.contains(tmp)) {
			// do nothing
		} else {
			check.add(tmp);
		}
		
		output.put(tmp, leafval);
		
	}
	
	
	public ArrayList<String> getResult() {

		TreeMap<String, ArrayList<String>> op = new TreeMap<String, ArrayList<String>>();
		
		for (Map.Entry<String, ArrayList<String>> map : groupby_output_temp
				.entrySet()) {
			ArrayList<String> temp_list = new ArrayList<String>();
			for (int j = 0; j < expression_list.size(); j++) {
				if (expression_list.get(j).contains("AVG") || expression_list.get(j).contains("avg")) {
					
					int index = check.indexOf((expression_list.get(j)));
//					int index1=check.indexOf("COUNT" + map.getKey());
//					LeafValue l_temp1 = map.getValue().co;
//					LeafValue l_temp1 = new DoubleValue(map.getValue().get(index1));
					String val = map.getValue().get(index);
					String temp[] = val.split("\\|");
					Double temp_sum = Double.parseDouble(temp[0]);
					Double temp_count = Double.parseDouble(temp[1]);
//					Double d_avg = Double
//							.parseDouble(map.getValue().get(index));
//					Double d_count = null;
//					try {
//						d_count = l_temp1.toDouble();
//					} catch (InvalidLeaf e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

//					Double avg_res = (d_avg / d_count);
					Double avg_res = temp_sum/temp_count;
					temp_list.add(avg_res.toString());
//					op.put(map.getKey(), temp_list);
				} 
//				else if (expression_list.get(j).contains("COUNT")) {
//					int index = check.indexOf("COUNT" + map.getKey());
////					temp_list.add(groupby_output.get("COUNT" + map.getKey()).toString());
//					temp_list.add(map.getValue().get(index));
//					
//				}
				else {
				
					int index = check.indexOf((expression_list.get(j)));

					temp_list.add(map.getValue().get(index));
//					op.put(map.getKey(), temp_list);
				}

			}
			op.put(map.getKey(), temp_list);
		}
		//Edit
		ArrayList<String> output_old = new ArrayList<String>();
		for(Map.Entry<String, ArrayList<String>> m : op.entrySet())
		{
//			String[] colValues = m.getKey().split("\\|");
			String output = m.getKey() ;
			for(String s : m.getValue())
			{
				output = output + "|" + s;
			}
			output_old.add(output);
			
		}
		
		
		
		ArrayList<String> groupbyitems = new ArrayList<String>();
		ArrayList<String> selectitems = new ArrayList<String>();
//		
//		TreeMap<String, ArrayList<String>> op_new = new TreeMap<String, ArrayList<String>>();
		ArrayList<String> output_new = new ArrayList<String>();
		boolean flag=true;
		for (int i = 0; i < groupingClause.size(); i++) {
			
			Column col =((Column) groupingClause.get(i));
			
			if(col.getTable().getName()==null)
			{
				for(Map.Entry<String, CreateTable> mp : getSchema().entrySet())
				col.getTable().setName(mp.getKey());
			}
			else
			{
			col.getTable().setName((col.getTable().getName().toUpperCase()));
			}
			
			groupbyitems.add(col.getWholeColumnName());
		}
		for (int i = 0; i < expression_list.size(); i++) {
			groupbyitems.add(expression_list.get(i));
		}
		for (int i = 0; i < selectClause.size(); i++) {
//			if (selectClause.get(i) instanceof Function)
//			{
				
				selectitems.add(((SelectExpressionItem) selectClause.get(i)).getExpression().toString());
//			}
//			if (selectClause.get(i) instanceof Column)
//			{
//				
//				selectitems.add(selectClause.get(i).toString());	
//			}
			
		}
//		boolean flag_operate=false;
//		if(((SelectExpressionItem) selectClause.get(selectClause.size()-1)).getExpression() instanceof Function)
//		{
//			
//		}
//		else
//		{
//			flag_operate=true;
//		}
//		
//		System.out.println(1);
		if (groupbyitems.equals(selectitems))
		{
			flag=false;
		}
		if(flag)
		{
			
			for(Map.Entry<String,ArrayList<String>> mp : op.entrySet())
			{
				String[] split_key=mp.getKey().split("\\|");
				ArrayList<String> split_value=mp.getValue();
				ArrayList<String> resultant_list = new ArrayList<String>();
				for(int i=0;i<split_key.length;i++)
				{
					resultant_list.add(split_key[i]);
				}
				resultant_list.addAll(split_value);
				String result=null;
				for(int i=0;i<groupbyitems.size();i++)
				{
					int index=groupbyitems.indexOf(selectitems.get(i));
					if(result==null)
					{
						result=resultant_list.get(index);
					}
					else
					{
						result = result + "|" + resultant_list.get(index);
					}
				}
				output_new.add(result);
				
			}
			
		}
		if(flag)
		{
			return output_new;
		}
		else
		{
		return output_old;
		}
//		return output_old;
	}	}



