package edu.buffalo.cse562;

import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class SelectOperator extends Operators {
	
	private List<Expression> where;
	HashMap<String, String> dataType = null;

	public SelectOperator(List<Expression> whr, HashMap<String, CreateTable> schema, HashMap<String, String> dataType) {
		// TODO Auto-generated constructor stub
		super(schema);
		this.setWhere(whr);
		this.dataType = dataType;
	}


	public List<Expression> getWhere() {
		return where;
	}

	public void setWhere(List<Expression> whr) {
		this.where = whr;
	}


	public LeafValue accept(Operators op, HashMap<String, CreateTable> tables, HashMap<Column, String> tuple) {
		// TODO Auto-generated method stub
		ScanOperator sc = new ScanOperator(dataType);
		LeafValue leafVal = null;
		for(Expression exp: where)
		{
			leafVal = sc.accept(exp, tuple);
			
			if(leafVal.toString().equalsIgnoreCase("true") || leafVal.toString().equalsIgnoreCase("'true'"))
			{
//				do nothing
			}
			
			else
			{
				break;
			}
		}
		
		return leafVal;
	}
	
	

}
