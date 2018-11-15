
/*
 * Author : Vedasri Uppala
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Interpreter {
	static int currentToken=0;
	static String errorM;
	public static String printTree(SExp root) {
		StringBuffer expression = new StringBuffer();
		if(root == null) 
			System.out.println("noooo");
		if(root.getIsNil()) return "NIL";
		else if(root.nodeType==1) 
			return expression.append(root.value).toString();
		else if(root.nodeType==2) 
			return expression.append(root.name).toString();
		else {
			expression.append("(");
			expression.append(printTree(root.left));
			expression.append(".");
			expression.append(printTree(root.right));
			expression.append(")");
		}
		return expression.toString();
	}

	public static SExp makeTree(String[] tokens) {
		SExp root =null;
		while(tokens[currentToken].equals("")) {
			currentToken++;
		}
		if( tokens[currentToken].equals(")")|| tokens[currentToken].equals(".")) {
			System.out.println("Error");
			return root;
		}
		if(tokens[currentToken].equals("(")) {
			currentToken++;
			if(tokens[currentToken].equals(")")) {
				currentToken++;
				return new SExp();
			}
			SExp leftChild=makeTree(tokens);			
			SExp rightChild=null;
			if(tokens[currentToken].equals(".")) {
				currentToken++;
				rightChild=makeTree(tokens);
				if(!tokens[currentToken].equals(")")){
					errorM = "Expected Parenthesis";
					return null;
				}
				currentToken++;
			}
			else {
				while(tokens[currentToken].equals("")) currentToken++;
				rightChild=makeTreeHelper(tokens);
				
				if(rightChild==null)	return null;
			}
			
			root = new SExp(leftChild,rightChild);
			return root;
		}
		if(tokens[currentToken].matches("^[+-]?\\d+$")) {
			root =new SExp(Integer.parseInt(tokens[currentToken]));
			currentToken++;
			return root;
		}
		if(tokens[currentToken].equals("NIL")) {
			root =  new SExp();
			currentToken++;
			return root;
		}
		if(tokens[currentToken].matches("[A-Za-z][A-Za-z0-9_]*")) {
			if(SExp.idPointers.containsKey(tokens[currentToken])) {
				root =SExp.idPointers.get(tokens[currentToken]);
			}
			else {
				root =new SExp(tokens[currentToken]);
				SExp.idPointers.put(tokens[currentToken], root);
			}
			currentToken++;
			return root;
		}
		return root;
	}
	public static SExp makeTreeHelper(String[] tokens) {
		SExp root = null;
		if(tokens[currentToken].equals(")")) {
			currentToken++;
			root =new SExp();
			return root;
		}
		else {
			SExp leftChild=makeTree(tokens);
			if(leftChild == null)
				return null;
			SExp rightChild=null;
			if(tokens[currentToken].equals(".")) {
				errorM = "Invalid structure for list";
				return null;
				//currentToken++;
				//rightChild=makeTree(tokens);
				//if(rightChild==null) return null;
			}
			else {
				while(tokens[currentToken]=="") currentToken++;
				rightChild=makeTreeHelper(tokens);
			}

			root = new SExp(leftChild,rightChild);
			return root;
		}
	}		
	
	
	
	public static String checkGrammar(String[] tokens) {
		
		int open = 0;
		
		String errorMessage = "";
		for(int i=0; i<tokens.length; i++){
			String token = tokens[i];
			if(token.matches("^[+-]?\\d+$")) {
				if(i==0 && tokens.length != 1) {
					errorMessage = "Invalid SExpression";
					return errorMessage;
				}
				continue;
			}
			else if(token.matches("[A-Za-z][A-Za-z0-9_]*")) {
				//if(token.length() > 10) {
					//errorMessage = "Variable and Function names cannot have length > 10";
					//return errorMessage;
				//}
				continue;
			}
			
			else if(token.equals("(")) {
				open++;
			}
			
			else if(token.equals(")")) {
				
				if(open <= 0) {
					errorMessage = "Incorrect number of parenthesis";
					return errorMessage;
				}
				open--;
			}
			
			else if(token.equals(".")) {
				if(open <= 0) {
					errorMessage = "parenthesis missing";
					return errorMessage;
				}
				
				if(i==tokens.length-1) {
					errorMessage = "Invalid token after dot";
					return errorMessage;
				}
				
				String next = tokens[i+1];
				if(next.equals(")") || next.equals(".")) {
					errorMessage = "Invalid token after dot";
					return errorMessage;
				}
				 next = tokens[i-1];
				if(next.equals("(")) {
					errorMessage = "Invalid token before dot";
					return errorMessage;
				}
				
			}
			
			else if(token.equals("")) {
				continue;
			}
			
			else {
				errorMessage = "Unexpected token "+ token;
				return errorMessage;
			}
		}
		if(open != 0) {
			errorMessage = "Unmatched parenthesis";
		}
		return errorMessage;
	}
	
	
	public static void main(String[] args) throws IOException{
		
		String buffer = "";
		String currentLine ="";		
		
		BufferedReader readData = new BufferedReader(new InputStreamReader(System.in));
		int flag=0;
		while(true) {
		
			if(flag == 1)
				break;
			//System.out.println("Enter the lisp expression and a '$' in the following line");
			buffer = "";
			while(true) {
				
				currentLine=readData.readLine();
				if(currentLine.trim().equals("$")) {
					break;
				}
				if(currentLine.equals("$$")) {			
					flag=1;
					break;				
				}
				buffer = buffer + " " + currentLine;
			}
			
			
			
			String trimmedBuffer =buffer.trim();
			String tokenList = trimmedBuffer.replaceAll("(\\.\\s+)|(\\s+\\.)","\\.");
			tokenList = tokenList.replaceAll("(\\(\\s+)","\\(");
			tokenList = tokenList.replaceAll("(\\s+\\))","\\)");		
			String[] tokens = tokenList.split("(\\s+)|(\\t+)|(?<=\\()|(?=\\()|(?<=\\))|(?=\\))|(?<=\\.)|(?=\\.)");
			
			/*if(tokens.length == 1) {
				if(!tokens[0].matches("^[+-]?\\d+$") || !tokens[0].matches("[A-Za-z][A-Za-z0-9_]*")) {
					System.out.println("Invalid Sexp");
					continue;
				}
			}*/
			currentToken=0;
				String errorMessage = checkGrammar(tokens);
				if(errorMessage.equals("")) {
					SExp root = makeTree(tokens);
					if(root == null) {
						System.out.println(errorM);
					}
					else {
					String expr = printTree(root);
					if(expr.length() < tokens.length)
						System.out.println("Invalid S-expression");
					else
						System.out.println(printTree(root));
						//System.out.println(root.nodeType+"nodetype");
					Evaluator evaluator = new Evaluator();
					SExp result = evaluator.eval(root);
					if(result == null)
						System.out.println("ERROR ->" + Evaluator.errorMessage);
					else
						System.out.println(printTree(result));
					}
				}
				else
					System.out.println(errorMessage);

		}
		return;
	}	
}
