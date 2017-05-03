package com.CmmCompiler.interpreter;

/**
*
* @author  PowerPret
* @function Store the Temp Tokens
*/

public class tempToken {
	private int row;
    private int column;
    private String token;
    private String type;//seperator,number,identifier,keyword,operator,
    
	public tempToken(int row, int column, String token,String type) {
		super();
		this.row = row;
		this.column = column;
		this.token = token;
		this.type=type;
		
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
	
}
