package com.CmmCompiler.interpreter;

/**
*
* @author  PowerPret
* @function Store the Record of Variables
*/

public class VarRecord {
	private String name;
	private String type;
	private int intValue = 0;
	private double doubleValue = 0;
	private boolean isArray = false;
	private int length = 0;
	//private int declRow=0;
	//private int declColumn=0;
	

	private int[] intArray = null;
	private double[] doubleArray = null;

	// ////////////////////////////////////////////
	// Construction Function
	public VarRecord(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	// ///////////////////////////////////////////
	// Judge isArray
	public boolean isArray() {
		return isArray;
	}

	// //////////////////////////////////////////
	// Functions of name,type,get
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	public void setName(String name){
		this.name=name;
	}
	
	public void setType(String type){
		this.type = type;
	}

	// //////////////////////////////////////////
	// Functions of set,get for non-array type elements
	public int getIntValue() {
		return intValue;
	}

	public void setValue(int intValue) {
		this.intValue = intValue;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	// /////////////////////////////////////////
	// Functions of set,get for array type elements
	public int getIntArrayElement(int index) {
		return this.intArray[index];
	}

	public double getDoubleArrayElement(int index) {
		return this.doubleArray[index];
	}

	public void setArrayElement(int index, int num) {
		this.intArray[index] = num;
	}

	public void setArrayElement(int index, double num) {
		this.doubleArray[index] = num;
	}

	// ///////////////////////////////////////////
	//  Functions of set,get for whole array type elements
	public int[] getIntArray() {
		return intArray;
	}

	public void setArray(int[] intArray) {
		this.setLength(intArray.length);
		this.intArray = intArray;
	}

	public double[] getDoubleArray() {
		return this.doubleArray;
	}

	public void setArray(double[] doubleArray) {
		this.setLength(doubleArray.length);
		this.doubleArray = doubleArray;
	}

	// /////////////////////////////////////////
	// Get the length of Array
	public int getLength() {
		return this.length;
	}

	// /////////////////////////////////////////
	// Set the length of Array
	public void setLength(int length) {
		this.isArray = true;
		this.length = length;
		if(this.type.equals("integer")){
			this.intArray = new int[length];
		} else{
			this.doubleArray = new double[length];
		}
	}
	
//	///////////////////////////////////////////////////////////
//	//Set the position of Variables Declaration
//	
//	public int getDeclRow() {
//		return declRow;
//	}
//
//	public int getDeclColumn() {
//		return declColumn;
//	}
//	
//	public void setDeclRow(int declRow) {
//		this.declRow=declRow;
//	}
//
//	public void setDeclColumn(int declColumn) {
//		this.declColumn=declColumn;
//	}

	//Change to String
	public String toString() {
		// TODO Auto-generated method stub
		String str = "";
		if (this.isArray()) {
			str = "New variable entry:\n\tThe Type is:" + this.type
					+ " array\n\tThe Name is:" + this.name
					+ "\n\tThe length is:" + this.length;

		} else {
			str = "New variable entry:\n\tThe Type is:" + this.type
					+ "\n\tThe Name is:" + this.name + "\n\tThe value is:"
					+ (this.intValue + this.doubleValue);
		}
		return str;
	}
}
