package edu.buffalo.cse562;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.expression.LeafValue.InvalidLeaf;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class ProjectOperator extends Operators {
	List<Expression> list = null;
	LeafValue sum_output;
	double count_output = 0;
	double avg_output = 0;
	double avg_sum_output = 0;
	double avg_count_output = 0;
	double min_output, max_output;
	Boolean flag = false;
	private String final_output = null;
	private String final_col_output = null;
	ArrayList<String> check = new ArrayList<String>();
	ArrayList<String> col_check = new ArrayList<String>();
	HashMap<String, String> dataType = null;

	private HashMap<String, LeafValue> output = new HashMap<String, LeafValue>();
	private HashMap<String, String> column = new HashMap<String, String>();

	public ProjectOperator(List<Expression> selectItems,
			HashMap<String, CreateTable> schema, HashMap<String, String> dataType) {
		// TODO Auto-generated constructor stub
		super(schema);
		list = selectItems;
		this.dataType = dataType;
	}

	public List<Expression> getList() {
		return list;
	}

	public void setList(List<Expression> list) {
		this.list = list;
	}

	public Boolean classify(HashMap<Column, String> data) {
		List<Expression> exp = list;
		for (int i = 0; i < exp.size(); i++) {
			Function f = null;
			if (((SelectExpressionItem) exp.get(i)).getExpression() instanceof Function) {
				f = (Function) ((SelectExpressionItem) exp.get(i)).getExpression();
				flag = true;

				String s = ((Function) ((SelectExpressionItem) exp.get(i)).getExpression()).getName();
				if (s.equalsIgnoreCase("SUM")) {
					sum(data, exp, f);
				}
//				 if (s.equals("MIN")) {
//				 max(data, exp, f);
//				 }
//				 if (s.equals("MAX")) {
//				 min(data, exp, f);
//				 }
				 if (s.equalsIgnoreCase("COUNT")) {
				 count(data, exp, f);
				 }
				 if (s.equalsIgnoreCase("AVG")) {
				 average(data, exp, f);
				 }
			}
		else if (((SelectExpressionItem) exp.get(i)).getExpression() instanceof Column) {
				columns(data, dataType, f, ((SelectExpressionItem) exp.get(i)).getExpression());
			}

		}
		return flag;
	}

	

	private void columns(HashMap<Column, String> data, HashMap<String, String> dataType, Function f, Expression expr) {
		// TODO Auto-generated method stub
//		List<SelectExpressionItem> exp = ((ProjectOperator) op).getList();
//		ProjectOperator po = new ProjectOperator(exp, getSchema());
//		HashMap<String, CreateTable> sch = new HashMap<String, CreateTable>();
//		sch = po.getSchema();

		ScanOperator sc = new ScanOperator(dataType);

		LeafValue leafval = sc.accept(expr, data);
		if((leafval.toString().startsWith("'") && leafval.toString().endsWith("'")) || (leafval.toString().startsWith(" ") && leafval.toString().endsWith(" ")))
		{
			final_output=(String) leafval.toString().subSequence(1, leafval.toString().length()-1);
		}
		else
		{
			final_output=leafval.toString();
		}
		

		
		String tmp = ((Column) expr).getColumnName();
		
		if (col_check.contains(tmp)) {
			// do nothing
		} else {
			col_check.add(tmp);
		}

		column.put(tmp, final_output);

	}

	@SuppressWarnings("unchecked")
	private void average(HashMap<Column, String> data, List<Expression> exp,
			Function f) {
		// TODO Auto-generated method stub
		String val = f.getParameters().toString();
		CreateTable ct = null;
		String datatype = null;
		for (Map.Entry<Column, String> fields : data.entrySet()) {

			if ((getSchema().containsKey(fields.getKey().getTable().toString()))) {
				ct = getSchema().get(fields.getKey().getTable().toString());
				List<ColumnDefinition> l = ct.getColumnDefinitions();
				for (int i = 0; i < l.size(); i++) {
					if (l.get(i)
							.getColumnName()
							.equalsIgnoreCase((fields.getKey().getColumnName()))) {
						datatype = l.get(i).getColDataType().toString();
						break;
					}
				}
			}

		}

		// String sum_value=leafval.toString();
		String tmp = f.getName() + "_" + val;
		if (check.contains(tmp)) {
			// do nothing
		} else {
			check.add(tmp);
		}
		
		sum(data,exp, f);
		count(data,exp, f);
		LeafValue sum = null;
		LeafValue count = null;
		for(Map.Entry<String, LeafValue> mp : output.entrySet())
		{
			if(mp.getKey().contains("SUM_" + val))
			{
				sum=mp.getValue();
			}
			if(mp.getKey().contains("COUNT_" + val))
			{
				count=mp.getValue();
			}
		}
		
		if (datatype.equalsIgnoreCase("INT")) {
			
			try {
				Long l1 = sum.toLong();
				Long l2 = count.toLong();
				LeafValue l = new LongValue(l1 / l2);
				output.put(tmp, l);
				
			} catch (InvalidLeaf e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (datatype.equalsIgnoreCase("DECIMAL")) {
			
			try {
				Double l1 = sum.toDouble();
				Double l2 = count.toDouble();
				LeafValue l = new DoubleValue(l1 / l2);
				output.put(tmp, l);
			} catch (InvalidLeaf e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
			}

	private void count(HashMap<Column, String> data, List<Expression> exp,
 Function f) {
		// TODO Auto-generated method stub

		String val = f.getParameters().toString();
//		CreateTable ct = null;
//		String datatype = null;

		Expression expr = ((Expression) f.getParameters().getExpressions()
				.get(0));
		ScanOperator sc = new ScanOperator(dataType);

		LeafValue leafval = sc.accept(expr, data);
		String tmp = f.getName() + "_" + val;
		if (check.contains(tmp)) {
			// do nothing
		} else {
			check.add(tmp);
		}
		if (leafval != null) {
			if (output.containsKey(tmp)) {
				LeafValue l = output.get(tmp);
				int x = Integer.parseInt(l.toString());
				x++;
				l = new LongValue(x);
				output.put(tmp, l);

			}

			else {
				int x = 1;
				LeafValue l = new LongValue(x);
				output.put(tmp, l);
			}
		}
	}

	/*private void min(HashMap<Column, String> data, List<SelectExpressionItem> exp,
			Function f) {
		// TODO Auto-generated method stub
		String val = f.getParameters().toString();
		CreateTable ct = null;
		String datatype = null;
		for (Map.Entry<Column, String> fields : data.entrySet()) {

			if ((getSchema().containsKey(fields.getKey().getTable()))) {
				ct = getSchema().get(fields.getKey().getTable());
				List<ColumnDefinition> l = ct.getColumnDefinitions();
				for (int i = 0; i < l.size(); i++) {
					if (l.contains(fields.getKey().getColumnName())) {
						datatype = l.get(i).getColDataType().toString();
					}
				}
			}
			if (fields.getKey().getColumnName().equals(val)) {
				if (Integer.parseInt(fields.getValue()) < min_output)
					min_output = Integer.parseInt(fields.getValue());
			}
		}
		String tmp = f.getName() + "_" + val;

		if (check.contains(tmp)) {
			// do nothing
		} else {
			check.add(tmp);
		}
		if (output.containsKey(val)) {
			String tmp1 = output.get(tmp);
			tmp1 += min_output;
			output.put(tmp, tmp1);
		} else {

			if (datatype.equalsIgnoreCase("INT")) {
				Double d = new Double(sum_output);
				output.put(tmp, Integer.toString(d.intValue()));
			} else if (datatype.equalsIgnoreCase("DECIMAL")) {
				output.put(tmp, String.valueOf(sum_output));
			}

		}
	}

/*private void max(HashMap<Column, String> data, List<SelectExpressionItem> exp,
			Function f) {
		// TODO Auto-generated method stub
		String val = f.getParameters().toString();
		CreateTable ct = null;
		String datatype = null;
		for (Map.Entry<Column, String> fields : data.entrySet()) {

			if ((getSchema().containsKey(fields.getKey().getTable()))) {
				ct = getSchema().get(fields.getKey().getTable());
				List<ColumnDefinition> l = ct.getColumnDefinitions();
				for (int i = 0; i < l.size(); i++) {
					if (l.contains(fields.getKey().getColumnName())) {
						datatype = l.get(i).getColDataType().toString();
					}
				}
			}
			if (fields.getKey().getColumnName().equals(val)) {
				if (Integer.parseInt(fields.getValue()) > max_output)
					max_output = Integer.parseInt(fields.getValue());
			}
		}
		String tmp = f.getName() + "_" + val;
		if (check.contains(tmp)) {
			// do nothing
		} else {
			check.add(tmp);
		}
		if (output.containsKey(val)) {
			String tmp1 = output.get(tmp);
			tmp1 += max_output;
			output.put(tmp, tmp1);
		} else {

			if (datatype.equalsIgnoreCase("INT")) {
				Double d = new Double(sum_output);
				output.put(tmp, Integer.toString(d.intValue()));
			} else if (datatype.equalsIgnoreCase("DECIMAL")) {
				output.put(tmp, String.valueOf(sum_output));
			}

		}
	}
*/
	private void sum(HashMap<Column, String> data,
			List<Expression> exp, Function f) {
		// TODO Auto-generated method stub

		String val = f.getParameters().toString();
//		CreateTable ct = null;
		String datatype = "DECIMAL";
//		for (Map.Entry<Column, String> fields : data.entrySet()) {
//
//			if ((getSchema().containsKey(fields.getKey().getTable().toString()))) {
//				ct = getSchema().get(fields.getKey().getTable().toString());
//				List<ColumnDefinition> l = ct.getColumnDefinitions();
//				for (int i = 0; i < l.size(); i++) {
//					if (l.get(i)
//							.getColumnName()
//							.equalsIgnoreCase((fields.getKey().getColumnName()))) {
//						datatype = l.get(i).getColDataType().toString();
//						break;
//					}
//				}
//			}
//
//		}
		
//		String tableName = ((Column)f.getParameters().getExpressions().get(0)).getTable().toString();
//		
//		if(getSchema().containsKey(tableName))
//		{
//			ct = getSchema().get(tableName);
//			List<ColumnDefinition> l = ct.getColumnDefinitions();
//			for (int i = 0; i < l.size(); i++) {
//				if (l.get(i).getColumnName().toString().equalsIgnoreCase(val)) {
//					datatype = l.get(i).getColDataType().toString();
//					break;
//				}
//			}
//		}
		
		Expression expr = ((Expression) f.getParameters().getExpressions()
				.get(0));
		ScanOperator sc = new ScanOperator(dataType);

		LeafValue leafval = sc.accept(expr, data);
		// String sum_value=leafval.toString();
		String tmp = f.getName() + "_" + val;
		if (check.contains(tmp)) {
			// do nothing
		} else {
			check.add(tmp);
		}

		if (output.containsKey(tmp)) {
			if (datatype.equalsIgnoreCase("INT")) {
				LeafValue tmp1 = output.get(tmp);
				try {
					Long l1 = tmp1.toLong();
					Long l2 = leafval.toLong();
					LeafValue l = new LongValue(l1 + l2);
					output.put(tmp, l);
				} catch (InvalidLeaf e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (datatype.equalsIgnoreCase("DECIMAL")) {
				LeafValue tmp1 = output.get(tmp);
				try {
					Double l1 = tmp1.toDouble();
					Double l2 = leafval.toDouble();
					LeafValue l = new DoubleValue(l1 + l2);
					output.put(tmp, l);
				} catch (InvalidLeaf e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} else {
			output.put(tmp, leafval);
		}

	}

	public String getResult() {

		final_col_output = null;
		
		if (flag) {

			for (int i = 0; i < check.size(); i++) {
				if (output.containsKey(check.get(i))) {
					if (final_output == null) {
						final_output = output.get(check.get(i)).toString();
					} else {
						final_output = final_output + "|"
								+ output.get(check.get(i)).toString();
					}
				}
			}
			return final_output;
		} else {
			for (int i = 0; i < col_check.size(); i++) {
				if (column.containsKey(col_check.get(i))) {
					if (final_col_output == null) {
						final_col_output = column.get(col_check.get(i));
					} else {
						final_col_output = final_col_output + "|"
								+ column.get(col_check.get(i));
					}
				}
			}

		}
		return final_col_output;
	}

}





//package edu.buffalo.cse562;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import net.sf.jsqlparser.expression.DoubleValue;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.Function;
//import net.sf.jsqlparser.expression.LeafValue;
//import net.sf.jsqlparser.expression.LeafValue.InvalidLeaf;
//import net.sf.jsqlparser.expression.LongValue;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
//import net.sf.jsqlparser.statement.create.table.CreateTable;
//import net.sf.jsqlparser.statement.select.SelectExpressionItem;
//
//public class ProjectOperator extends Operators {
//	List<SelectExpressionItem> list = null;
//	LeafValue sum_output;
//	double count_output = 0;
//	double avg_output = 0;
//	double avg_sum_output = 0;
//	double avg_count_output = 0;
//	double min_output, max_output;
//	Boolean flag = false;
//	private String final_output = null;
//	private String final_col_output = null;
//	ArrayList<String> check = new ArrayList<String>();
//	ArrayList<String> col_check = new ArrayList<String>();
//
//	private HashMap<String, LeafValue> output = new HashMap<String, LeafValue>();
//	private HashMap<String, String> column = new HashMap<String, String>();
//
//	public ProjectOperator(List<SelectExpressionItem> selectItems,
//			HashMap<String, CreateTable> schema) {
//		// TODO Auto-generated constructor stub
//		super(schema);
//		list = selectItems;
//	}
//
//	public List<SelectExpressionItem> getList() {
//		return list;
//	}
//
//	public void setList(List<SelectExpressionItem> list) {
//		this.list = list;
//	}
//
//	public Boolean classify(HashMap<Column, String> data,  Operators op) {
//		List<SelectExpressionItem> exp = ((ProjectOperator) op).getList();
//		for (int i = 0; i < exp.size(); i++) {
//			Function f = null;
//			if (exp.get(i).getExpression() instanceof Function) {
//				f = (Function) exp.get(i).getExpression();
//				flag = true;
//
//				String s = ((Function) exp.get(i).getExpression()).getName();
//				if (s.equals("SUM")) {
//					sum(data, exp, f);
//				}
////				 if (s.equals("MIN")) {
////				 max(data, exp, f);
////				 }
////				 if (s.equals("MAX")) {
////				 min(data, exp, f);
////				 }
//				 if (s.equals("COUNT")) {
//				 count(data, exp, f);
//				 }
//				 if (s.equals("AVG")) {
//				 average(data, exp, f);
//				 }
//			}
//		else if (exp.get(i).getExpression() instanceof Column) {
//				columns(data, op, f, exp.get(i).getExpression());
//			}
//
//		}
//		return flag;
//	}
//
//	
//
//	private void columns(HashMap<Column, String> data, Operators op, Function f, Expression expr) {
//		// TODO Auto-generated method stub
//		List<SelectExpressionItem> exp = ((ProjectOperator) op).getList();
//		ProjectOperator po = new ProjectOperator(exp, getSchema());
//		HashMap<String, CreateTable> sch = new HashMap<String, CreateTable>();
//		sch = po.getSchema();
//
//		ScanOperator sc = new ScanOperator(getSchema());
//
//		LeafValue leafval = sc.accept(expr, data);
//		if((leafval.toString().startsWith("'") && leafval.toString().endsWith("'")) || (leafval.toString().startsWith(" ") && leafval.toString().endsWith(" ")))
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
//		
//		if (col_check.contains(tmp)) {
//			// do nothing
//		} else {
//			col_check.add(tmp);
//		}
//
//		column.put(tmp, final_output);
//
//	}
//
//	private void average(HashMap<Column, String> data, List<SelectExpressionItem> exp,
//			Function f) {
//		// TODO Auto-generated method stub
//		String val = f.getParameters().toString();
//		CreateTable ct = null;
//		String datatype = null;
//		for (Map.Entry<Column, String> fields : data.entrySet()) {
//
//			if ((getSchema().containsKey(fields.getKey().getTable().toString()))) {
//				ct = getSchema().get(fields.getKey().getTable().toString());
//				List<ColumnDefinition> l = ct.getColumnDefinitions();
//				for (int i = 0; i < l.size(); i++) {
//					if (l.get(i)
//							.getColumnName()
//							.equalsIgnoreCase((fields.getKey().getColumnName()))) {
//						datatype = l.get(i).getColDataType().toString();
//						break;
//					}
//				}
//			}
//
//		}
//
//		// String sum_value=leafval.toString();
//		String tmp = f.getName() + "_" + val;
//		if (check.contains(tmp)) {
//			// do nothing
//		} else {
//			check.add(tmp);
//		}
//		
//		sum(data,exp, f);
//		count(data,exp, f);
//		LeafValue sum = null;
//		LeafValue count = null;
//		for(Map.Entry<String, LeafValue> mp : output.entrySet())
//		{
//			if(mp.getKey().contains("SUM_" + val))
//			{
//				sum=mp.getValue();
//			}
//			if(mp.getKey().contains("COUNT_" + val))
//			{
//				count=mp.getValue();
//			}
//		}
//		
//		if (datatype.equalsIgnoreCase("INT")) {
//			
//			try {
//				Long l1 = sum.toLong();
//				Long l2 = count.toLong();
//				LeafValue l = new LongValue(l1 / l2);
//				output.put(tmp, l);
//				
//			} catch (InvalidLeaf e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		} else if (datatype.equalsIgnoreCase("DECIMAL")) {
//			
//			try {
//				Double l1 = sum.toDouble();
//				Double l2 = count.toDouble();
//				LeafValue l = new DoubleValue(l1 / l2);
//				output.put(tmp, l);
//			} catch (InvalidLeaf e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		
//			}
//
//	private void count(HashMap<Column, String> data, List<SelectExpressionItem> exp,
// Function f) {
//		// TODO Auto-generated method stub
//
//		String val = f.getParameters().toString();
//		CreateTable ct = null;
//		String datatype = null;
//
//		Expression expr = ((Expression) f.getParameters().getExpressions()
//				.get(0));
//		ScanOperator sc = new ScanOperator(getSchema());
//
//		LeafValue leafval = sc.accept(expr, data);
//		String tmp = f.getName() + "_" + val;
//		if (check.contains(tmp)) {
//			// do nothing
//		} else {
//			check.add(tmp);
//		}
//		if (leafval != null) {
//			if (output.containsKey(tmp)) {
//				LeafValue l = output.get(tmp);
//				int x = Integer.parseInt(l.toString());
//				x++;
//				l = new LongValue(x);
//				output.put(tmp, l);
//
//			}
//
//			else {
//				int x = 1;
//				LeafValue l = new LongValue(x);
//				output.put(tmp, l);
//			}
//		}
//	}
//
//	/*private void min(HashMap<Column, String> data, List<SelectExpressionItem> exp,
//			Function f) {
//		// TODO Auto-generated method stub
//		String val = f.getParameters().toString();
//		CreateTable ct = null;
//		String datatype = null;
//		for (Map.Entry<Column, String> fields : data.entrySet()) {
//
//			if ((getSchema().containsKey(fields.getKey().getTable()))) {
//				ct = getSchema().get(fields.getKey().getTable());
//				List<ColumnDefinition> l = ct.getColumnDefinitions();
//				for (int i = 0; i < l.size(); i++) {
//					if (l.contains(fields.getKey().getColumnName())) {
//						datatype = l.get(i).getColDataType().toString();
//					}
//				}
//			}
//			if (fields.getKey().getColumnName().equals(val)) {
//				if (Integer.parseInt(fields.getValue()) < min_output)
//					min_output = Integer.parseInt(fields.getValue());
//			}
//		}
//		String tmp = f.getName() + "_" + val;
//
//		if (check.contains(tmp)) {
//			// do nothing
//		} else {
//			check.add(tmp);
//		}
//		if (output.containsKey(val)) {
//			String tmp1 = output.get(tmp);
//			tmp1 += min_output;
//			output.put(tmp, tmp1);
//		} else {
//
//			if (datatype.equalsIgnoreCase("INT")) {
//				Double d = new Double(sum_output);
//				output.put(tmp, Integer.toString(d.intValue()));
//			} else if (datatype.equalsIgnoreCase("DECIMAL")) {
//				output.put(tmp, String.valueOf(sum_output));
//			}
//
//		}
//	}
//
///*private void max(HashMap<Column, String> data, List<SelectExpressionItem> exp,
//			Function f) {
//		// TODO Auto-generated method stub
//		String val = f.getParameters().toString();
//		CreateTable ct = null;
//		String datatype = null;
//		for (Map.Entry<Column, String> fields : data.entrySet()) {
//
//			if ((getSchema().containsKey(fields.getKey().getTable()))) {
//				ct = getSchema().get(fields.getKey().getTable());
//				List<ColumnDefinition> l = ct.getColumnDefinitions();
//				for (int i = 0; i < l.size(); i++) {
//					if (l.contains(fields.getKey().getColumnName())) {
//						datatype = l.get(i).getColDataType().toString();
//					}
//				}
//			}
//			if (fields.getKey().getColumnName().equals(val)) {
//				if (Integer.parseInt(fields.getValue()) > max_output)
//					max_output = Integer.parseInt(fields.getValue());
//			}
//		}
//		String tmp = f.getName() + "_" + val;
//		if (check.contains(tmp)) {
//			// do nothing
//		} else {
//			check.add(tmp);
//		}
//		if (output.containsKey(val)) {
//			String tmp1 = output.get(tmp);
//			tmp1 += max_output;
//			output.put(tmp, tmp1);
//		} else {
//
//			if (datatype.equalsIgnoreCase("INT")) {
//				Double d = new Double(sum_output);
//				output.put(tmp, Integer.toString(d.intValue()));
//			} else if (datatype.equalsIgnoreCase("DECIMAL")) {
//				output.put(tmp, String.valueOf(sum_output));
//			}
//
//		}
//	}
//*/
//	private void sum(HashMap<Column, String> data,
//			List<SelectExpressionItem> exp, Function f) {
//		// TODO Auto-generated method stub
//
//		String val = f.getParameters().toString();
//		CreateTable ct = null;
//		String datatype = "DECIMAL";
////		for (Map.Entry<Column, String> fields : data.entrySet()) {
////
////			if ((getSchema().containsKey(fields.getKey().getTable().toString()))) {
////				ct = getSchema().get(fields.getKey().getTable().toString());
////				List<ColumnDefinition> l = ct.getColumnDefinitions();
////				for (int i = 0; i < l.size(); i++) {
////					if (l.get(i)
////							.getColumnName()
////							.equalsIgnoreCase((fields.getKey().getColumnName()))) {
////						datatype = l.get(i).getColDataType().toString();
////						break;
////					}
////				}
////			}
////
////		}
//		
////		String tableName = ((Column)f.getParameters().getExpressions().get(0)).getTable().toString();
////		
////		if(getSchema().containsKey(tableName))
////		{
////			ct = getSchema().get(tableName);
////			List<ColumnDefinition> l = ct.getColumnDefinitions();
////			for (int i = 0; i < l.size(); i++) {
////				if (l.get(i).getColumnName().toString().equalsIgnoreCase(val)) {
////					datatype = l.get(i).getColDataType().toString();
////					break;
////				}
////			}
////		}
//		
//		Expression expr = ((Expression) f.getParameters().getExpressions()
//				.get(0));
//		ScanOperator sc = new ScanOperator(getSchema());
//
//		LeafValue leafval = sc.accept(expr, data);
//		// String sum_value=leafval.toString();
//		String tmp = f.getName() + "_" + val;
//		if (check.contains(tmp)) {
//			// do nothing
//		} else {
//			check.add(tmp);
//		}
//
//		if (output.containsKey(tmp)) {
//			if (datatype.equalsIgnoreCase("INT")) {
//				LeafValue tmp1 = output.get(tmp);
//				try {
//					Long l1 = tmp1.toLong();
//					Long l2 = leafval.toLong();
//					LeafValue l = new LongValue(l1 + l2);
//					output.put(tmp, l);
//				} catch (InvalidLeaf e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			} else if (datatype.equalsIgnoreCase("DECIMAL")) {
//				LeafValue tmp1 = output.get(tmp);
//				try {
//					Double l1 = tmp1.toDouble();
//					Double l2 = leafval.toDouble();
//					LeafValue l = new DoubleValue(l1 + l2);
//					output.put(tmp, l);
//				} catch (InvalidLeaf e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//
//		} else {
//			output.put(tmp, leafval);
//		}
//
//	}
//
//	public String getResult() {
//
//		final_col_output = null;
//		
//		if (flag) {
//
//			for (int i = 0; i < check.size(); i++) {
//				if (output.containsKey(check.get(i))) {
//					if (final_output == null) {
//						final_output = output.get(check.get(i)).toString();
//					} else {
//						final_output = final_output + "|"
//								+ output.get(check.get(i)).toString();
//					}
//				}
//			}
//			return final_output;
//		} else {
//			for (int i = 0; i < col_check.size(); i++) {
//				if (column.containsKey(col_check.get(i))) {
//					if (final_col_output == null) {
//						final_col_output = column.get(col_check.get(i));
//					} else {
//						final_col_output = final_col_output + "|"
//								+ column.get(col_check.get(i));
//					}
//				}
//			}
//
//		}
//		return final_col_output;
//	}
//
//}