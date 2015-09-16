/**
 * 
 */
package edu.buffalo.cse562;

import java.util.ArrayList;

import net.sf.jsqlparser.expression.Expression;

/**
 * @author Susana D'sa
 *
 */
public class OperatorLists {
	
	private static OperatorLists opListObj = null;
	private static ArrayList<String> relationOpList = new ArrayList<String>();
	private static ArrayList<RelationOperator> relationOperatorList = new ArrayList<RelationOperator>();
	private static ArrayList<Expression> joinConditionList = new ArrayList<Expression>();
	
	private OperatorLists()
	{
		
	}
	
	public static OperatorLists getInstance()
	{
		if(opListObj == null)
		{
			opListObj = new OperatorLists();
		}
		
		return opListObj;
	}

	public static ArrayList<String> getRelationOpList() {
		return relationOpList;
	}

	public static void setRelationOpList(ArrayList<String> relationOpList) {
		OperatorLists.relationOpList = relationOpList;
	}

	public static ArrayList<Expression> getJoinConditionList() {
		return joinConditionList;
	}

	public static void setJoinConditionList(ArrayList<Expression> joinConditionList) {
		OperatorLists.joinConditionList = joinConditionList;
	}

	public static ArrayList<RelationOperator> getRelationOperatorList() {
		return relationOperatorList;
	}

	public static void setRelationOperatorList(ArrayList<RelationOperator> relationOperatorList) {
		OperatorLists.relationOperatorList = relationOperatorList;
	}
	
	
}
