package com.CmmCompiler.interpreter;

/**
*
* @author  PowerPret
* @function Record the Temp State
*/

public class tempNote {
	private String name;
	private int index=-1;
	private int Ivalue;
	private double Dvalue;
	private String type;

	public tempNote(int value) {
		super();
		this.Ivalue = value;
		this.type = "integer";
	}
	public tempNote(double value){
		super();
		this.Dvalue = value;
		this.type = "double";
	}

	public int getIntValue() {
		return Ivalue;
	}
	public double getDoubleValue(){
		return Dvalue;
	}

	public void setValue(int value) {
		if(this.type.equals("double")){
			this.type="integer";
		}
		this.Ivalue = value;
	}
	
	public void setValue(double value){
		if(this.type.equals("integer")){
			this.type="double";
		}
		this.Dvalue = value;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
}
