package edu.buffalo.cse562;

import java.util.HashMap;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.Limit;

public class LimitOperator extends Operators {
	
	Limit lim;

	public Limit getLim() {
		return lim;
	}

	public void setLim(Limit lim) {
		this.lim = lim;
	}

	public LimitOperator(Limit limit, HashMap<String, CreateTable> schema) {
		// TODO Auto-generated constructor stub
		super(schema);
		lim= limit;
	}

}
