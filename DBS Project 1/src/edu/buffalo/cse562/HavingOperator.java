package edu.buffalo.cse562;

import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class HavingOperator extends Operators {
	
	Expression havingClause;

	public HavingOperator(Expression having, HashMap<String, CreateTable> schema) {
		// TODO Auto-generated constructor stub
		super(schema);
		havingClause = having;
	}

}
