package edu.buffalo.cse562;

//import java.util.ArrayList;
//import java.util.List;

import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.*;

//import net.sf.jsqlparser.schema.Table;

public class RATree extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree tree;
//	private List<Table> tablesList = null;
//	private List<Table> prevTablesList = null;
	private DefaultMutableTreeNode node = null;
	private DefaultMutableTreeNode prevNode = null;
	
//	public List<Table> getTablesList() {
//		return tablesList;
//	}
//
//	public void setTablesList(List<Table> tablesList) {
//		this.tablesList = tablesList;
//	}

	OperatorLists opListSingletonObj = OperatorLists.getInstance();

	public RATree() {

	}
	
	public DefaultMutableTreeNode getNode() {
		return node;
	}

	public void setNode (DefaultMutableTreeNode node) {
		this.node = node;
	}

	public void AddNode(Operators op) {
		node = new DefaultMutableTreeNode(op);

		if(prevNode != null)
		{
			node.add(prevNode);
		}
		else
		{
			//do nothing
		}
		
		tree = new JTree(node);
		add(tree);
		
		prevNode = node;
		
//		return node;
	}
	

	public void AddNodeForJoin(RelationOperator r, HashMap<String, String> dataType) {
		// TODO Auto-generated method stub
		
		
		JoinOperator j = new JoinOperator(dataType);
//		j.setRelationTableList(tablesList);
		
		node = new DefaultMutableTreeNode(j);
		node.add(prevNode);
		
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode((Operators) r);

		node.add(childNode);
		
		tree = new JTree(node);
		add(tree);
		
		prevNode = node;
//		prevTablesList = tablesList;
		
//		return node;
	}
	
	public RATree createTree(DefaultMutableTreeNode node)
	{
		tree = new JTree(node);
		add(tree);
		
		return this;
	}
}
