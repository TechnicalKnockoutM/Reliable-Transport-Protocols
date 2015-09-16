package edu.buffalo.cse562;

import java.util.HashMap;

import net.sf.jsqlparser.statement.create.table.CreateTable;

public abstract class Operators {
	
	private HashMap<String, CreateTable> schema;

	public HashMap<String, CreateTable> getSchema() {
		return schema;
	}

	public void setSchema(HashMap<String, CreateTable> schema) {
		this.schema = schema;
	}

	public Operators(HashMap<String, CreateTable> schema)
	{
		this.schema = schema;
	}
}
