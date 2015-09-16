package edu.buffalo.cse562;

import java.util.HashMap;

import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.Distinct;

public class DistinctOperator extends Operators {
	
	Distinct dis;

	public DistinctOperator(Distinct distinct, HashMap<String, CreateTable> schema) {
		// TODO Auto-generated constructor stub
		super(schema);
		dis = distinct;
	}

}
