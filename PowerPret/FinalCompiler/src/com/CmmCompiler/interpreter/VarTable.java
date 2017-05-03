package com.CmmCompiler.interpreter;

/**
*
* @author  PowerPret
* @function Table for Recording the Variables
*/

import java.util.*;

public class VarTable {
	private HashMap<String, VarRecord> theTable;
	protected VarTable prev;

	public VarTable(){
		theTable = new HashMap<String, VarRecord>();
		prev = null;
	}
	public VarTable(VarTable p) {
		theTable = new HashMap<String, VarRecord>();
		prev = p;
	}

	public boolean isExist(String name) {
		return this.theTable.containsKey(name);
	}

	public void addVar(VarRecord var) {
		theTable.put(var.getName(), var);
	}

	public void delVar(String name) {
		theTable.remove(name);
	}

	public int size() {
		return theTable.size();
	}

	public VarRecord getVar(String name) {
		for(VarTable v = this;v!=null;v=v.prev){
			VarRecord found = (VarRecord)(v.theTable.get(name));
		    if(found != null)return found;
		}
		return null;
	}
	
	public void updateVar(tempNote newVar){
		VarRecord var = this.getVar(newVar.getName());
		if(var.isArray()){
			if(newVar.getType().equals("integer")){
				var.setArrayElement(newVar.getIndex(), newVar.getIntValue());
			} else{
				var.setArrayElement(newVar.getIndex(), newVar.getDoubleValue());
			}
		} else{
			if(newVar.getType().equals("integer")){
				var.setValue(newVar.getIntValue());
			} else{
				var.setValue(newVar.getDoubleValue());
			}
		}
		this.theTable.put(var.getName(), var);
	}
	
	// Change to String
	public String toString(){
		String str="";
		Set<String> keys = theTable.keySet();
		for(String key:keys){
			str+=theTable.get(key).toString();
			str+="\n";
		}
		return str;
	}
}

