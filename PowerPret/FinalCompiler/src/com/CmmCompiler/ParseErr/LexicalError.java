package com.CmmCompiler.ParseErr;

import java.io.BufferedReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.*;


import com.CmmCompiler.interpreter.Token;
import com.CmmCompiler.interpreter.TokenMgrError;
import com.CmmCompiler.interpreter.tempToken;

/**
*
* @author  PowerPret
* @function GetLexicalError
*/


public class LexicalError {
	
	private boolean isSuccess = true;
	private String source;
	private boolean isNotation = false;// Handle Comment "/* */"
	private String notations = "";
	private ArrayList<tempToken> tokens = new ArrayList<tempToken>();
	private String errorMsg = "";

	private LexicalError(String source) {
		this.source = source;
	}

	public static LexicalError getInstance(String source) {
		LexicalError lexererr = new LexicalError(source);
		return lexererr;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	private boolean isDigit(char ch) {
		if (ch >= '0' && ch <= '9')
			return true;
		else
			return false;
	}

	private boolean isLetter(char ch) {
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
			return true;
		else
			return false;
	}

	private boolean isKey(String str) {
		if (str.equals("if") || str.equals("else") || str.equals("while")
				|| str.equals("read") || str.equals("write")
				|| str.equals("int") || str.equals("real") || str.equals("double"))

			return true;
		else
			return false;
	}

	public String lexer4Line(String str, int line) {

		tempToken token;

		// Store Output Result
		String buf = "";

		// get the position of tokens from the Biginning of Line 
		int begin = 0;

		// get the position of tokens from the End of Line 
		int end = 0;
		int i = 0;
		int length = str.length();

		// Record the present state of Lexical Analysis
		int state = 0;

		// Record the position of Error Occur
		int err_col = 0;

		for (i = 0; i < length; i++) {
			err_col++;
			char ch = str.charAt(i);
			if (!isNotation)
				if (ch == '\\' || ch == '~' || ch == '`' || ch == '|'
						|| ch == '��' || ch == '^' || ch == '?' || ch == '&'
						|| ch == '^' || ch == '%' || ch == '$' || ch == '#'
						|| ch == '@' || ch == '!') {
					buf += "    Error: line=" + line + " column=" + err_col
							+ "  (" + ch + " )�ǲ���ʶ�����  \n";

					errorMsg += "    Error: line=" + line + " column=" + err_col
							+ "  (" + ch + " )�ǲ���ʶ�����  \n";
					isSuccess = false;
					state = 0;
				} else {
					switch (state) {
					// S
					case 0:
						// Print if it is Separator and Set state to "0"
						if (ch == ';' || ch == '{' || ch == '}' || ch == '('
								|| ch == ')' || ch == '[' || ch == ']'
								|| ch == ',') {
							buf += "    info:�ָ���  " + ch + "\n";
							state = 0;
							String temp = "";
							temp += ch;
							token = new tempToken(line, i, temp, "seperator");
							tokens.add(token);
						} else if (ch == '_') {
							state = 9;
						} else if (ch == '+')
							state = 1;
						else if (ch == '-')
							state = 2;
						else if (ch == '*')
							state = 3;
						else if (ch == '/')
							state = 4;
						else if (ch == '=')
							state = 5;
						else if (ch == '<')
							state = 6;
						else if (isLetter(ch)) {
							if (i == (length - 1)) {
								buf += "    info:��ʶ��  " + ch + "\n";
								String temp = "";
								temp += ch;
								token = new tempToken(line, i, temp, "identifier");
								tokens.add(token);
								break;
							}
							begin = i;
							state = 7;
						} else if (isDigit(ch)) {
							if (i == (length - 1)) {
								buf += "    info:����  " + ch + "\n";
								String temp = "";
								temp += ch;
								token = new tempToken(line, i, temp, "number");
								tokens.add(token);
								break;
							}

							begin = i;
							state = 8;
						} else if (ch == '.') {
						
							buf += "    Error: line=" + line + " column="
									+ err_col + "(.) ����ʹ�ô���\n";
							isSuccess = false;
							errorMsg += "    Error: line=" + line + " column="
									+ err_col + "(.) ����ʹ�ô���\n";
							state = 0;

						}
						break;
					// For +
					case 1:
						buf += "    info:�����  +\n";
						token = new tempToken(line, i - 1, "+", "operator");
						tokens.add(token);
						i--;
						err_col--;
						state = 0;
						break;
					// For -
					case 2:
						buf += "    info:�����  -\n";
						token = new tempToken(line, i - 1, "-", "operator");
						tokens.add(token);
						i--;
						err_col--;
						state = 0;
						break;
					// For *
					case 3:
						// �����ǰ�ַ���/�����п�����Ҫ���ע���ַ���
						if (ch == '/') {
							buf += "    Error: line=" + line + " column="
									+ err_col + "  �����ʹ�ô���(*)\n";
							errorMsg += "    Error: line=" + line + " column="
									+ err_col + "  �����ʹ�ô���(*)\n";
							isSuccess = false;
						} else {
							buf += "    info:����� *\n";
							token = new tempToken(line, i - 1, "*", "operator");
							tokens.add(token);
							i--;
							err_col--;

						}
						state = 0;
						break;
					// For /
					case 4:
						if (ch == '/') {
							buf += "    info:ע��  //\n";
							String notation = str.substring(i + 1, length);
							buf += "    info:ע������ '" + notation + "'\n";
							i = length;
							state = 0;
						} else if (ch == '*') {
							buf += "    info:ע��  /*\n";
							isNotation = true;
						} else {
							buf += "    info:����� /\n";
							token = new tempToken(line, i - 1, "/", "operator");
							tokens.add(token);
							i--;
							err_col--;
							state = 0;
						}

						break;
					// For =
					case 5:
						if (ch == '=') {
							buf += "    info:����� ==\n";
							token = new tempToken(line, i - 1, "==", "operator");
							tokens.add(token);
						} else {
							buf += "    info:��ֵ����� = \n";
							token = new tempToken(line, i - 1, "=", "operator");
							tokens.add(token);
							i--;
							err_col--;
						}
						state = 0;
						break;
					// For <
					case 6:
						if (ch == '>') {
							buf += "    info:����� <>\n";
							token = new tempToken(line, i - 1, "<>", "operator");
							tokens.add(token);
						} else {
							buf += "    info:����� <\n";
							token = new tempToken(line, i - 1, "<", "operator");
							tokens.add(token);
							i--;
							err_col--;
						}
						state = 0;
						break;

					// For letter
					case 7:
						/*
						 * �����﷨��������Ҫ�������ʶ��ע�͵� if(ch=='['||ch==']'){ if(ch=='['
						 * && flag==false){ flag=true;state=7;} else if(ch==']'
						 * && flag==true && i!=(length-1)){ flag=false;state=7;}
						 * else if(ch==']' && flag==true && i==(length-1)){
						 * flag=false; end=i+1; String id=str.substring(begin,
						 * end); buf+="    info:��ʶ�������飩  "+id+"\n"; token=new
						 * Token(line,begin,id,"array"); tokens.add(token);
						 * state=0; }else{
						 * buf+="    Error:line"+line+" column="+
						 * err_col+"  ʹ�ô���([])" ; state=9; } }else
						 */
						if (ch == '_') {
							if (i != str.length() - 1) {
								char nextChar = str.charAt(i + 1);
								if (isLetter(nextChar) || isDigit(nextChar))
									state = 7;
								else {
									buf += "    Error: line=" + line
											+ " column=" + err_col
											+ "  ��ʾ��������(_)����\n";
									errorMsg += "    Error: line=" + line
											+ " column=" + err_col
											+ "  ��ʾ��������(_)����\n";
									isSuccess = false;
									state = 0;
								}
							}
							else {
								buf += "    Error: line=" + line
										+ " column=" + err_col
										+ "  ��ʾ��������(_)����\n";
								errorMsg += "    Error: line=" + line
										+ " column=" + err_col
										+ "  ��ʾ��������(_)����\n";
								isSuccess = false;
								state = 0;
							}

						} else if (isLetter(ch) || isDigit(ch)) {
							if (i == (length - 1)) {
								end = i + 1;
								String id = str.substring(begin, end);
								if (isKey(id)) {
									buf += "    info:�ؼ���  " + id + "\n";
									token = new tempToken(line, begin, id,"keyword");
									tokens.add(token);
								} else {
									/*
									 * if(id.contains("[")){
									 * buf+="    info:��ʶ�������飩  "+id+"\n";
									 * token=new Token(line,begin,id,"array");
									 * 
									 * }else {
									 */
									buf += "    info:��ʶ��  " + id + "\n";
									token = new tempToken(line, begin, id,"identifier");

									// }
									tokens.add(token);
								}
								break;
							}
							state = 7;
						} else {
							end = i;
							String id = str.substring(begin, end);
							if (isKey(id)) {
								buf += "    info:�ؼ���  " + id + "\n";
								token = new tempToken(line, begin, id, "keyword");
								tokens.add(token);
							} else {
								/*
								 * if(id.contains("[")){
								 * buf+="    info:��ʶ�������飩  "+id+"\n"; token=new
								 * Token(line,begin,id,"array");
								 * 
								 * }else {
								 */
								buf += "    info:��ʶ��  " + id + "\n";
								token = new tempToken(line, begin, id, "identifier");

								// }
								tokens.add(token);
							}
							i--;
							err_col--;
							state = 0;
						}
						break;
					// For digit
					case 8:

						if (isDigit(ch)
								|| (ch == '.' && !str.substring(begin, i)
										.contains("."))) {
							if (i == (length - 1)) {
								end = i + 1;
								String number = str.substring(begin, end);
								buf += "    info:���� " + number + "\n";
								token = new tempToken(line, begin, number, "number");
								tokens.add(token);
								break;
							}
							state = 8;

						}
						// Several "." in Num
						else if (ch == '.'
								&& str.substring(begin, i).contains(".")) {
							buf += "    Error: line=" + line + " column="
									+ (err_col - 1) + "(.) ���ָ�ʽ����\n";
							isSuccess = false;
							errorMsg += "    Error: line=" + line + " column="
									+ (err_col - 1) + "(.) ���ָ�ʽ����\n";
							state = 0;
						} else {
							if (isLetter(ch)) {
								buf += "    Error: line=" + line + " column="
										+ (err_col - 1) + " ���ָ�ʽ������߱�־������\n";
								isSuccess = false;
								errorMsg += "    Error: line=" + line
										+ " column=" + (err_col - 1)
										+ " ���ָ�ʽ������߱�־������\n";
							} else {
								end = i;
								String number = str.substring(begin, end);
								buf += "    info:���� " + number + "\n";
								token = new tempToken(line, begin, number, "number");
								tokens.add(token);
								i--;
								err_col--;
							}
							state = 0;
						}
						break;
					// Handle Identifier Start with "_"
					case 9:
						String id = "_";
						if (ch != ' ' && ch != ';' && ch != '{' && ch != '}'
								&& ch != '(' && ch != ')' && ch != '['
								&& ch != ']') {
							id += "" + ch;
							state = 9;
							if (i == (length - 1)) {
								buf += "    Error: line=" + line + " column="
										+ err_col + "  ��ʾ��������(_)��ʼ";
								errorMsg += "    Error: line=" + line
										+ " column=" + err_col
										+ "  ��ʾ��������(_)��ʼ";
								isSuccess = false;
								state = 0;
							}
						}

						else {

							buf += "    Error: line=" + line + " column="
									+ err_col + "  ��ʾ��������(_)��ʼ\n";
							errorMsg += "    Error: line=" + line + " column="
									+ err_col + "  ��ʾ��������(_)��ʼ\n";
							isSuccess = false;
							state = 0;
							i--;
							err_col--;
						}
						break;
					}
				}
			else {

				if (ch == '*') {
					state = 3;
				} else if (ch == '/' && state == 3) {
					if (notations != "")
						buf += "    info:ע������    " + notations + "\n";
					buf += "    info:ע�ͽ���  */\n";
					state = 0;
					isNotation = false;
					notations = "";
				} else {
					state = 0;
					notations += ch;
				}

			}

		}
		return buf;
	}

	public String lexerAnalysis() {
		String buf = "";

		StringReader content = new StringReader(source);

		BufferedReader br = new BufferedReader(content);

		String strTemp = "";

		int line = 1;

		while (strTemp != null) {
			try {
				strTemp = br.readLine();
				if (strTemp != null && !strTemp.equals("")) {
					if (!isNotation) {
						buf += line + "�� " + strTemp + "\n";
					}
					buf += lexer4Line(strTemp, line);
				}
				if (isNotation)
					notations += "\n" + "             ";
				line++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return buf;
	}

	public ArrayList<tempToken> getTokens() {
		lexerAnalysis();
		return tokens;
	}

	public tempToken getToken(int i) {
		return (tempToken) tokens.get(i);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
    

