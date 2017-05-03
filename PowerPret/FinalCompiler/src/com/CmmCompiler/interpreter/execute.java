package com.CmmCompiler.interpreter;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

//import com.cmmsemantic.Node;
//import com.cmmsemantic.tempNote;

/**
*
* @author  PowerPret
* @function Main Execution of Tree
*/


public class execute {

	boolean breakflag = false ;
	//int flag = 0;
	// boolean continueflag =true ;
	// boolean returnflag = false;
	//
	SimpleNode thisNode;

	public String Console="";
	//public static PrintStream Model_Console;// Use Model Console to output result
	//
	public VarTable varTable;

	public execute(SimpleNode n) throws FileNotFoundException {
		this.thisNode = n;
		varTable = new VarTable();
		varTable= null;
		//Model_Console = new PrintStream(new FileOutputStream("Output.txt"));
	}

	// Handle all the outer statements
	public boolean walk() {
		String pname = this.thisNode.toString();
		if (pname.equals("Start")) {
			varTable= new VarTable(varTable);
			int num = thisNode.jjtGetNumChildren();
			for (int i = 0; i < num; i++) {
				if (!StmtHandler(thisNode.jjtGetChild(i))) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	
	//Statement Handler
	private boolean StmtHandler(Node n) {

		String ename = n.jjtGetChild(0).toString();

		if (ename.equals("varDecl")) {
			if (!(this.varDeclHandler(n.jjtGetChild(0)))) {
				System.out.println("Variables Declairation Error");
				return false;
			}
		} else if (ename.equals("IfStmt")) {
			if (!(this.IfStmtHandler(n.jjtGetChild(0)))) {
				System.out.println("If Statement Error");
				return false;
			}
		}  else if (ename.equals("SwitchStmt")) {
			if (!(this.SwitchStmtHandler(n.jjtGetChild(0)))) {
				System.out.println("Switch Statement Error");
				return false;
			}
		}  else if (ename.equals("WhileStmt")) {
			if (!(this.WhileStmtHandler(n.jjtGetChild(0)))) {
				System.out.println("While Statement Error");
				return false;
			}
		} else if (ename.equals("BreakStmt")) {
			if (!(this.BreakStmtHandler(n.jjtGetChild(0)))) {
				System.out.println("Break Statement Error");
				return false;
			}
		} else if (ename.equals("AssignStmt")) {
			if (!(this.AssignStmtHandler(n.jjtGetChild(0)))) {
				System.out.println("Assignment Statement Error");
				return false;
			}
		} else if (ename.equals("StmtBlock")) {
			if (!(this.StmtBlockHandler(n.jjtGetChild(0)))) {
				System.out.println("StmtBlock Statement Error");
				return false;
			}
		} else if (ename.equals("ReadStmt")) {
			if (!(this.ReadStmtHandler(n.jjtGetChild(0)))) {
				System.out.println("ReadStmt Statement Error");
				return false;
			}
		} else if (ename.equals("WriteStmt")) {
			if (!(this.WriteStmtHandler(n.jjtGetChild(0)))) {
				System.out.println("WriteStmt Statement Error");
				return false;
			}
		} else {
			return false;
		}

		return true;
	}
	
	//Switch
	private boolean SwitchStmtHandler(Node n) {
		// TODO Auto-generated method stub
		String pname = n.toString();
		tempNote note;
		if (pname.equals("SwitchStmt")) {
			int num = n.jjtGetNumChildren();
			if(num<1){
				return false;
			}
			tempNote note1;
			tempNote note2;
			note1 = this.ExpressionHandler(n.jjtGetChild(0));
			if(note1==null){
				return false;
			}
			for(int i=1;i<=((num-1)/2)*2;i+=2){
				note2 = this.ExpressionHandler(n.jjtGetChild(i));
				if(note2==null){
					return false;
				}
				if(!note1.getType().equals(note2.getType())){
					return false;
				}
				if(note1.getType().equals("integer")){
					if(note1.getIntValue()==note2.getIntValue()){
						return StmtHandler(n.jjtGetChild(i+1));
					} 
				} else{
					if(note1.getDoubleValue()==note2.getDoubleValue()){
						return StmtHandler(n.jjtGetChild(i+1));
					} 
				}
			}
			if(num%2==0){
				return StmtHandler(n.jjtGetChild(num-1));
			}
		} else{
			return false;
		}
		
		return false;
	}

	
	//Write
	private boolean WriteStmtHandler(Node n) {
		String pname = n.toString();
		tempNote note;
		if (pname.equals("WriteStmt")) {
			note = this.ExpressionHandler(n.jjtGetChild(0));
			if(note==null){
				return false;
			}
			if(note.getType().equals("integer")){
				Console+=note.getIntValue()+"\n";
				//Model_Console.print(note.getIntValue()+"\n");
			} else{
				Console+=note.getDoubleValue()+"\n";
				//Model_Console.print(note.getDoubleValue()+"\n");
			}
			return true;
		} else return false;
	}

	
	//Read
	private boolean ReadStmtHandler(Node n) {
		// TODO Auto-generated method stub
		String pname = n.toString();
		tempNote note;
		if (pname.equals("ReadStmt")) {
			note = this.ValueHandler(n.jjtGetChild(0));
			if(note==null){
				return false;
			}
			String str = JOptionPane.showInputDialog("");
		//	Scanner input = new Scanner(System.in);
			if(note.getType().equals("integer")){
				note.setValue(Integer.parseInt(str));
			} else{
				note.setValue(Double.parseDouble(str));
			}
			varTable.updateVar(note);
			return true;
		} else return false;
	}
	
	
	//Statement Block
	private boolean StmtBlockHandler(Node n) {
		// TODO Auto-generated method stub
		String pname = n.toString();
		VarTable saved = new VarTable();
		if (pname.equals("StmtBlock")) {
			saved = varTable;
			varTable = new VarTable(varTable);
			int num = n.jjtGetNumChildren();
			for (int i = 0; i < num; i++) {
				if (!StmtHandler(n.jjtGetChild(i))) {
					return false;
				}
				if(breakflag)break;
			}
			varTable = saved;
		} else {
			return false;
		}
		return true;
	}

	//Assignment Statement
	private boolean AssignStmtHandler(Node n) {
		// TODO Auto-generated method stub
		//VarRecord var;
		tempNote note1;
		tempNote note2;
		String pname = n.toString();
		if (pname.equals("AssignStmt")) {
			int num = n.jjtGetNumChildren();
			if (num != 2) {
				return false;
			}
			note1 = ValueHandler(n.jjtGetChild(0));
			if (note1 == null) {
				return false;
			}
			note2 = ExpressionHandler(n.jjtGetChild(1));
			if (note2 == null) {
				return false;
			}
			if(note1.getType().equals("double")&&note2.getType().equals("integer")){
				note2=new tempNote(new Integer(note2.getIntValue()).doubleValue());
			}

			if (note1.getIndex() == -1) {
				note2.setName(note1.getName());
			} else {
				note2.setName(note1.getName());
				note2.setIndex(note1.getIndex());
			}
			this.varTable.updateVar(note2);
		} else {
			return false;
		}
		return true;
	}

	
	//Expression
	private tempNote ExpressionHandler(Node n) {
		// TODO Auto-generated method stub
		String pname = n.toString();
		tempNote note1;
		tempNote note2;
		String type;
		SimpleNode nn = (SimpleNode) n;
		if (pname.equals("Expression")) {
			int cnum = nn.jjtGetNumChildren();
			int vnum = nn.jjtValueLength();
			if (cnum != vnum + 1) {
				return null;
			}
			note1 = TermHandler(nn.jjtGetChild(0));
			for (int i = 1; i < cnum; i++) {
				note2 = TermHandler(nn.jjtGetChild(i));
				if (note2 == null) {
					return null;
				}
				type = nn.jjtGetValue(i - 1).toString();
				if (!note1.getType().equals(note2.getType())) {
					return null;
				} else if (note1.getType().equals("integer")) {
					switch (type.charAt(0)) {
					case '+':
						note1.setValue(note1.getIntValue()
								+ note2.getIntValue());
						break;
					case '-':
						note1.setValue(note1.getIntValue()
								- note2.getIntValue());
						break;
					}
				} else {
					switch (type.charAt(0)) {
					case '+':
						note1.setValue(note1.getDoubleValue()
								+ note2.getDoubleValue());
						break;
					case '-':
						note1.setValue(note1.getDoubleValue()
								- note2.getDoubleValue());
						break;

					}
				}
			}
			return note1;
		} else {
			return null;
		}
	}
	
	
    //Term
	private tempNote TermHandler(Node n) {
		// TODO Auto-generated method stub
		String pname = n.toString();
		tempNote note1;
		tempNote note2;
		String type;
		SimpleNode nn = (SimpleNode) n;
		if (pname.equals("Term")) {
			int cnum = nn.jjtGetNumChildren();
			int vnum = nn.jjtValueLength();
			if (cnum != vnum + 1) {
				return null;
			}
			note1 = ElementHandler(nn.jjtGetChild(0));
			for (int i = 1; i < cnum; i++) {
				note2 = ElementHandler(nn.jjtGetChild(i));
				if (note2 == null) {
					return null;
				}
				type = nn.jjtGetValue(i - 1).toString();
				if (!note1.getType().equals(note2.getType())) {
					return null;
				} else if (note1.getType().equals("integer")) {
					switch (type.charAt(0)) {
					case '*':
						note1.setValue(note1.getIntValue()
								* note2.getIntValue());
						break;
					case '/':
						note1.setValue(note1.getIntValue()
								/ note2.getIntValue());
						break;
					case '%':
						note1.setValue(note1.getIntValue()
								% note2.getIntValue());
					}
				} else {
					switch (type.charAt(0)) {
					case '*':
						note1.setValue(note1.getDoubleValue()
								* note2.getDoubleValue());
						break;
					case '/':
						note1.setValue(note1.getDoubleValue()
								/ note2.getDoubleValue());
						break;
					case '%':
						return null;
					}
				}
			}
			return note1;
		} else {
			return null;
		}
	}

	//Element
	private tempNote ElementHandler(Node n) {
		// TODO Auto-generated method stub
		String pname = n.toString();
		tempNote note;
		String type;
		SimpleNode nn = (SimpleNode) n;
		if (pname.equals("Element")) {
			int num = nn.jjtGetNumChildren();
			if(nn.jjtGetValue(0).toString().equals("minus")){
				if(num!=1){
					return null;
				}
				note = ElementHandler(nn.jjtGetChild(0));
				type = note.getType();
			    if(type.equals("integer")){
					note.setValue(-note.getIntValue());
			    } else{
					note.setValue(-note.getDoubleValue());
			    }
			} else if(nn.jjtGetValue(0).toString().equals("expression")){
				if(num!=1){
					return null;
				}
				note = ExpressionHandler(nn.jjtGetChild(0));
			} else if(nn.jjtGetValue(0).toString().equals("value")){
				if(num!=1){
					return null;
				}
				note = ValueHandler(nn.jjtGetChild(0));
			} else if(nn.jjtGetValue(0).toString().equals("integer")){
				if(num!=0){
					return null;
				}
				note = new tempNote(Integer.parseInt(nn.jjtGetValue(1).toString()));
			} else if(nn.jjtGetValue(0).toString().equals("double")){
				if(num!=0){
					return null;
				}
				note = new tempNote(Double.parseDouble(nn.jjtGetValue(1).toString()));
			} else{
				return null;
			}
			return note;
		} else{
			return null;
		}
	}

	//Value
	private tempNote ValueHandler(Node n) {
		// TODO Auto-generated method stub
		SimpleNode nn = (SimpleNode) n;
		VarRecord var;
		tempNote note;
		String name = nn.jjtGetValue(0).toString();
		if (!this.varTable.isExist(name)&&this.varTable.getVar(name)==null) {
			return null;
		}
		var = this.varTable.getVar(name);
		int num = n.jjtGetNumChildren();
		int index;
		if (num == 1) {
			index = ExpressionHandler(nn.jjtGetChild(0)).getIntValue();
			if (var.getType().equals("integer")) {
				note = new tempNote(var.getIntArrayElement(index));
				note.setName(name);
				note.setIndex(index);
			} else {
				note = new tempNote(var.getDoubleArrayElement(index));
				note.setName(name);
				note.setIndex(index);
			}
		} else if (num == 0 && nn.jjtValueLength() == 2) {
			index = Integer.parseInt(nn.jjtGetValue(1).toString());
			if (var.getType().equals("integer")) {
				note = new tempNote(var.getIntArrayElement(index));
				note.setName(name);
				note.setIndex(index);
			} else {
				note = new tempNote(var.getDoubleArrayElement(index));
				note.setName(name);
				note.setIndex(index);
			}
		} else if (num == 0 && nn.jjtValueLength() == 1) {
			if (var.getType().equals("integer")) {
				note = new tempNote(var.getIntValue());
				note.setName(name);
			} else {
				note = new tempNote(var.getDoubleValue());
				note.setName(name);
			}
		} else {
			return null;
		}

		return note;
	}

	//Break
	private boolean BreakStmtHandler(Node n) {
		// TODO Auto-generated method stub
		String pname = n.toString();
		SimpleNode nn =(SimpleNode)n.jjtGetParent().jjtGetParent();
		if(pname.equals("BreakStmt")){
			if((nn.jjtGetParent().jjtGetParent().toString().equals("WhileStmt")&&nn.toString().equals("StmtBlock"))||(n.jjtGetParent().jjtGetParent().toString().equals("IfStmt")&&nn.jjtGetParent().jjtGetParent().jjtGetParent().jjtGetParent().toString().equals("WhileStmt"))){
				this.breakflag = true;
			}
			return true;
		}else return false;
	}

	
	//While
	private boolean WhileStmtHandler(Node n) {
		// TODO Auto-generated method stub
		SimpleNode nn = (SimpleNode) n;
		String pname = n.toString();
		//this.breakflag=true;
		if(pname.equals("WhileStmt")){
			while(CriterionHandler(nn.jjtGetChild(0))){
				if(!StmtHandler(nn.jjtGetChild(1))){
					return false;
				}
				if(breakflag)break;
			}
			if(breakflag)breakflag = false;
			return true;
		} else{
			return false;
		}
	}
	
	//If
	private boolean IfStmtHandler(Node n) {
		// TODO Auto-generated method stub
		SimpleNode nn = (SimpleNode) n;
		String pname = n.toString();
		if(pname.equals("IfStmt")){
			if(CriterionHandler(nn.jjtGetChild(0))){
				if(!StmtHandler(nn.jjtGetChild(1))){
					return false;
				}
			} else if(nn.jjtGetNumChildren()==3){
				if(!StmtHandler(nn.jjtGetChild(2))){
					return false;
				}
			}
			return true;
		} else{
			return false;
		}
	}

	//Criterion
	private boolean CriterionHandler(Node n) {
		// TODO Auto-generated method stub
		tempNote note1;
		tempNote note2;
		SimpleNode nn = (SimpleNode)n;
		String pname = n.toString();
		//String type = nn.jjtGetValue(0).toString();
		if(pname.equals("Criterion")){
			int num = n.jjtGetNumChildren();
			if(num==1){
				note1 = ExpressionHandler(nn.jjtGetChild(0));
				if(note1.getType().equals("integer")){
					return note1.getIntValue() != 0;
				}else return note1.getDoubleValue() !=0;
			}
			if(num!=2){
				return false;
			}
			note1 = ExpressionHandler(nn.jjtGetChild(0));
			if(num == 2){
				String type = nn.jjtGetValue(0).toString();
			    note2 = ExpressionHandler(nn.jjtGetChild(1));
			/*if(!note1.getType().equals(note2.getType())){
				return false;
			}*/
			if(note1.getType().equals("integer")){
				if(type.equals(">")){
					return note1.getIntValue()>note2.getIntValue();
				} else if(type.equals("<")){
					return note1.getIntValue()<note2.getIntValue();
				} else if(type.equals("==")){
					return note1.getIntValue()==note2.getIntValue();
				} else if(type.equals("!=")){
					return note1.getIntValue()!=note2.getIntValue();
				} else if(type.equals(">=")){
					return note1.getIntValue()>=note2.getIntValue();
				} else if(type.equals("<=")){
					return note1.getIntValue()<=note2.getIntValue();
				} else return false;
			} else{
				if(type.equals(">")){
					return note1.getDoubleValue()>note2.getDoubleValue();
				} else if(type.equals("<")){
					return note1.getDoubleValue()<note2.getDoubleValue();
				} else if(type.equals("==")){
					return note1.getDoubleValue()==note2.getDoubleValue();
				} else if(type.equals("!=")){
					return note1.getDoubleValue()!=note2.getDoubleValue();
				} else if(type.equals(">=")){
					return note1.getDoubleValue()>=note2.getDoubleValue();
				} else if(type.equals("<=")){
					return note1.getDoubleValue()<=note2.getDoubleValue();
				} else return false;
			}
			}else return false;
		} else{
			return false;
		}
	}
	
	
	//Variables Declartion
	private boolean varDeclHandler(Node n) {
		// TODO Auto-generated method stub
		VarRecord var;
		String pname = n.toString();
		ArrayList names;
		tempNote note;
		if (pname.equals("varDecl")) {
			int num = n.jjtGetNumChildren();
			int number = n.jjtGetChild(1).jjtGetNumChildren();
			if (num != 2) {
				return false;
			}
			names = VarListHandler(n.jjtGetChild(1));
			for (int i = 0; i < names.size(); i++) {
				String name = names.get(i).toString();
				if (varTable.isExist(name)) {
					return false;
				}
				var = TypeHandler(n.jjtGetChild(0));
				var.setName(name);
                if(number!=0){
					note = ExpressionHandler(n.jjtGetChild(1).jjtGetChild(i));
	                if(note.getType().equals("integer"))	{
	                	var.setValue(note.getIntValue());
	                }else var.setValue(note.getDoubleValue());
				}
				varTable.addVar(var);
			}
		} else {
			return false;
		}
		return true;
	}

	//Variables List
	private ArrayList VarListHandler(Node n) {
		// TODO Auto-generated method stub
		SimpleNode nn = (SimpleNode) n;
		return nn.value;
	}

	
	//Type
	private VarRecord TypeHandler(Node n) {
		// TODO Auto-generated method stub
		VarRecord var;
		SimpleNode nn = (SimpleNode)n;
		int num = nn.jjtValueLength();
		if (num == 1) {
			String name = nn.jjtGetValue(0).toString();
			var = new VarRecord("newVar", name);
		} else {
			String name = nn.jjtGetValue(0).toString();
			String length = nn.jjtGetValue(1).toString();
			var = new VarRecord("newVar", name);
			var.setLength(Integer.parseInt(length));
		}
		return var;
	}

}