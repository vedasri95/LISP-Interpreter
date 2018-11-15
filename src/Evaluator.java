/*
 * Author : Vedasri Uppala
 */

import java.util.ArrayList;
import java.util.HashMap;


public class Evaluator {

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
	
	public static String errorMessage = "";
	public int errorFlag=0;
	
	HashMap<String, ArrayList<SExp>> aList = new HashMap<String,ArrayList<SExp>>();
	static HashMap<String, ArrayList<SExp>>	dList = new HashMap<String, ArrayList<SExp>>();
	public Evaluator() {
		
	}
	
	//system defined functions
	public SExp car(SExp exp) {
		if(exp == null) {
			errorMessage = "INVALID arguments to car";
			return null;
		}
		if(exp.nodeType != 3) {
			errorMessage = "CAR called on atom";
			return null;
		}
		return exp.left;
	}
	public SExp cdr(SExp exp) {
		if(exp == null) {
			errorMessage = "INVALID arguments to cdr";
			return null;
		}
		if(exp.nodeType != 3) {
			errorMessage = "CDR called on atom";
			return null;
		}
		return exp.right;
	}
	public SExp cons(SExp exp1, SExp exp2){
		SExp node = new SExp();
		node.isNIL = false;
		node.left = exp1;
		node.right = exp2;
		node.nodeType = 3;
		return node;
	}
	public SExp isAtom(SExp atom) {
		if(atom == null) {
			errorMessage = "INVALID arguments to ATOM";
			return null;
		}
		if(atom.left == null && atom.right == null)
			return new SExp("T");
		else
			return new SExp();
	}
	public SExp eq(SExp atom1, SExp atom2){	
		//System.out.println("hhhh");
		//System.out.println(isAtom(atom1).nodeType + "veda" + isAtom(atom2).nodeType);
		if(isAtom(atom1).nodeType==4 || isAtom(atom2).nodeType == 4) {
			errorFlag=1;
			errorMessage = "Arguments to eq must be atoms";
			return null;
		}		
		if(atom1.nodeType == atom2.nodeType) {
			if(atom1.nodeType == 1) {
				if(atom1.value == atom2.value) return new SExp("T");
				else return new SExp();
			}
			else {
				if(atom1.name.equals(atom2.name))	return new SExp("T");
				else	return new SExp();
			}
		}
		return new SExp();
	}
	public SExp isNIL(SExp atom) {
		if(isAtom(atom).getIsNil()) {
			errorFlag=1;
			errorMessage = "Arguments to eq must be atoms";
			return new SExp();
		}
		else
			if(atom.getIsNil()==true)
				return new SExp("T");
			else
				return new SExp();
	}
	
	public int length(SExp exp){
		if(exp.getIsNil()) {
			return 0;
		}
		if(isAtom(exp).name.equals("T")) {
			return 1;
		}
		else
			return 1+length(cdr(exp));
		
	}
	
	//creating bindings on alist
	public int addPairs(SExp paramList, SExp argList) {	
		if(argList==null && paramList==null) {
			return 1;
		}
		else if(argList==null || paramList==null) {
			return -1;
		}
		
		if(argList.getIsNil() && paramList.getIsNil())
			return 1;
		else if(argList.getIsNil() || paramList.getIsNil()) {
			return -1;
		}
		SExp final_e = car(argList);
		if(!aList.containsKey(car(paramList).name)) {
			ArrayList<SExp> aux= new ArrayList<SExp>();
			aux.add(final_e);
			aList.put(car(paramList).name, aux);
		}
		else {
			ArrayList<SExp> aux = aList.get(car(paramList).name);
			aux.add(final_e);
			aList.put(car(paramList).name, aux);
		}
		addPairs(cdr(paramList),cdr(argList));
		return 1;
	}
	
	//remove bindings on aList
	public void removePairs(SExp paramList) {
		if(paramList.getIsNil())
			return;
		ArrayList<SExp> aux = aList.get(car(paramList).name);
		aux.remove(aux.size()-1);
		aList.put(car(paramList).name, aux);
		removePairs(cdr(paramList));
		}
	
	public SExp findQuotient(SExp argList) {
		if(length(argList) != 2) {
			errorMessage = "QUOTIENT EXPECTS TWO ARGUMENTS";
			return null;
		}
		if(car(argList).nodeType != 1 && car(argList).nodeType != 2) {
			errorMessage = "Function QUOTIENT has illegal arguments";
			return null;
		}
		else if(car(cdr(argList)).nodeType != 1 && car(cdr(argList)).nodeType != 2) {
			errorMessage = "Function QUOTIENT has illegal arguments";
			return null;
		}
		else if(car(argList).nodeType == 1 && car(argList).value == 0) {
			errorMessage = "Division by 0 is not defined";
			return null;
		}
		else {
			
			int val1,val2;
			
			if(aList.containsKey(car(argList).name)) {
				ArrayList<SExp> aux = aList.get(car(argList).name);
				val1 = aux.get(aux.size()-1).value;
			}
			else
				val1 = car(argList).value;
			
			if(aList.containsKey(car(cdr(argList)).name)) {
				ArrayList<SExp> aux = aList.get(car(cdr(argList)).name);
				val2 = aux.get(aux.size()-1).value;
			}
			else
				val2 = car(cdr(argList)).value;
			
			int val = val1/val2;
			return new SExp(val);
		}
		
	}

	public SExp times(SExp argList) {
		if(length(argList) != 2) {
			errorMessage = "TIMES EXPECTS TWO ARGUMENTS";
			return null;
		}
		if(car(argList).nodeType != 1 && car(argList).nodeType != 2) {
			errorMessage = "Function TIMES has illegal arguments";
			return null;
		}
		else if(car(cdr(argList)).nodeType != 1 && car(cdr(argList)).nodeType != 2) {
			errorMessage = "Function TIMES has illegal arguments";
			return null;
		}
		
		else if(length(argList) != 2) {
			errorMessage = "Illegal number of arguments to TIMES";
			return null;
		}
		else {
			
			int val1,val2;
			
			if(aList.containsKey(car(argList).name)) {
				ArrayList<SExp> aux = aList.get(car(argList).name);
				val1 = aux.get(aux.size()-1).value;
			}
			else
				val1 = car(argList).value;
			
			if(aList.containsKey(car(cdr(argList)).name)) {
				ArrayList<SExp> aux = aList.get(car(cdr(argList)).name);
				val2 = aux.get(aux.size()-1).value;
			}
			else
				val2 = car(cdr(argList)).value;
			
			int val = val1 * val2;
			return new SExp(val);
			
		}
		
	}
	
	public SExp remainder(SExp argList) {
		if(length(argList) != 2) {
			errorMessage = "REMAINDER EXPECTS TWO ARGUMENTS";
			return null;
		}
		
		if(car(argList).nodeType != 1 && car(argList).nodeType != 2) {
			errorMessage = "Function REMAINDER has illegal arguments";
			return null;
		}
		else if(car(cdr(argList)).nodeType != 1 && car(cdr(argList)).nodeType != 2) {
			errorMessage = "Function REMAINDER has illegal arguments";
			return null;
		}
		
		else if(length(argList) != 2) {
			errorMessage = "Illegal number of arguments to REMAINDER";
			return null;
		}
		else if(car(argList).nodeType == 1 && car(argList).value == 0) {
			errorMessage = "Division by 0 is not defined";
			return null;
		}
		else {
			int val1,val2;
			
			if(aList.containsKey(car(argList).name)) {
				ArrayList<SExp> aux = aList.get(car(argList).name);
				val1 = aux.get(aux.size()-1).value;
			}
			else
				val1 = car(argList).value;
			
			if(aList.containsKey(car(cdr(argList)).name)) {
				ArrayList<SExp> aux = aList.get(car(cdr(argList)).name);
				val2 = aux.get(aux.size()-1).value;
			}
			else
				val2 = car(cdr(argList)).value;
			
			int val = val1 % val2;
			return new SExp(val);
		}
		
	}

	public SExp less(SExp argList) {
		if(length(argList) != 2) {
			errorMessage = "LESS EXPECTS TWO ARGUMENTS";
			return null;
		}
			
		if(car(argList).nodeType != 1 && car(argList).nodeType != 2) {
			errorMessage = "Function LESS has illegal arguments";
			return null;
		}
		else if(car(cdr(argList)).nodeType != 1 && car(cdr(argList)).nodeType != 2) {
			errorMessage = "Function LESS has illegal arguments";
			return null;
		}
		
		else if(length(argList) != 2) {
			errorMessage = "Illegal number of arguments to LESS";
			return null;
		}
		
		else {
			int val1,val2;
			
			if(aList.containsKey(car(argList).name)) {
				ArrayList<SExp> aux = aList.get(car(argList).name);
				val1 = aux.get(aux.size()-1).value;
			}
			else
				val1 = car(argList).value;
			
			if(aList.containsKey(car(cdr(argList)).name)) {
				ArrayList<SExp> aux = aList.get(car(cdr(argList)).name);
				val2 = aux.get(aux.size()-1).value;
			}
			else
				val2 = car(cdr(argList)).value;
			
			if(val1 < val2)
				return new SExp("T");
			else
				return new SExp();
		}
		
		
	}
	
	
	public SExp greater(SExp argList) {
		
		
		if(car(argList).nodeType != 1 && car(argList).nodeType != 2) {
			errorMessage = "Function GREATER has illegal arguments";
			return null;
		}
		else if(car(cdr(argList)).nodeType != 1 && car(cdr(argList)).nodeType != 2) {
			errorMessage = "Function GREATER has illegal arguments";
			return null;
		}
		
		else if(length(argList) != 2) {
			errorMessage = "Illegal number of arguments to GREATER";
			return null;
		}
		else {
			int val1,val2;
			
			if(aList.containsKey(car(argList).name)) {
				ArrayList<SExp> aux = aList.get(car(argList).name);
				val1 = aux.get(aux.size()-1).value;
			}
			else
				val1 = car(argList).value;
			
			if(aList.containsKey(car(cdr(argList)).name)) {
				ArrayList<SExp> aux = aList.get(car(cdr(argList)).name);
				val2 = aux.get(aux.size()-1).value;
			}
			else
				val2 = car(cdr(argList)).value;
			
			if(val1 > val2)
				return new SExp("T");
			else
				return new SExp();
		}
		
		
	}
	//apply function definition
	
	public SExp apply(SExp funcName, SExp argList) {
		if(isAtom(funcName).getIsNil()) {
			errorFlag = 1;
			errorMessage = "Expected function";
			return null;
		}
		String fname = funcName.name;
		//System.out.println("nameee" + fname);
		
		if(fname.equals("CAR")) {
			if(length(argList)!=1) {
				errorMessage = "CAR expects single argument";
				return null;
			}
			return car(car(argList));
		}
		else if(fname.equals("CDR")) {
			if(length(argList)!=1) {
				errorMessage = "CDR expects single argument";
				return null;
			}
			return cdr(car(argList));
		}
		else if(fname.equals("INT")) {
			if(length(argList) != 1) {
				errorMessage = "INT expects a single argument";
				return null;
			}
			if(car(argList).nodeType == 1) {
				return new SExp("T");
			}
			else {
				return new SExp();
			}
		}
		
		else if(fname.equals("LESS")) {
			SExp e = less(argList);
			if(e == null)	return null;
			else return e;
		}
		
		else if(fname.equals("GREATER")) {
			SExp e = greater(argList);
			if(e == null)	return null;
			else return e;
		}
		else if(fname.equals("TIMES")) {
			SExp e = times(argList);
			if(e == null)	return null;
			else return e;
		}
		else if(fname.equals("REMAINDER")) {
			SExp e = remainder(argList);
			if(e == null)	return null;
			else return e;
		}
		
		
		
		else if(fname.equals("QUOTIENT")) {
			SExp e = findQuotient(argList);
			if(e == null)	return null;
			else return e;
		}
		
		else if(fname.equals("CONS")) {
			if(length(argList) != 2) {
				errorMessage = "CONS expects two arguments";
				return null;
			}
			return cons(car(argList), car(cdr(argList)));
		}
		else if(fname.equals("ATOM")) {
			
			if(length(argList) != 1) {
				errorMessage = "ATOM expects single arguments";
				return null;
			}
			return isAtom(car(argList));
		}
		else if(fname.equals("NULL")) {
			if(length(argList) != 1) {
				errorMessage = "NULL expects single argument";
				return null;
			}
			return isNIL(car(argList));
		}
		else if(fname.equals("EQ")) {
			if(length(argList) != 2) {
				errorMessage = "EQ expects two arguments";
				return null;
			}
			SExp result =  eq(car(argList), car(cdr(argList)));
			return result;
		}
		else if(fname.equals("PLUS")) {
			
			int val1,val2;
			if(length(argList) != 2) {
				errorMessage = "PLUS expects two arguments";
				return null;
			}
			
			if(car(argList).nodeType != 1 && car(argList).nodeType != 2) {

				errorMessage = "Function PLUS has illegal arguments";
				return null;
			}
			
			if(car(cdr(argList)).nodeType != 1 && car(cdr(argList)).nodeType != 2) {

				errorMessage = "Function PLUS has illegal arguments";
				return null;
			}
			if(aList.containsKey(car(argList).name)) {
				ArrayList<SExp> aux = aList.get(car(argList).name);
				val1 = aux.get(aux.size()-1).value;
			}
			else
				val1 = car(argList).value;
			
			if(aList.containsKey(car(cdr(argList)).name)) {
				ArrayList<SExp> aux = aList.get(car(cdr(argList)).name);
				val2 = aux.get(aux.size()-1).value;
			}
			else
				val2 = car(cdr(argList)).value;
			return new SExp(val1+val2);
		}
		
		else if(fname.equals("MINUS")) {
			
			int val1,val2;	
			if(length(argList) != 2) {
				errorMessage = "MINUS expects two arguments";
				return null;
			}
			
			if(car(argList).nodeType != 1 && car(argList).nodeType != 2) {
				errorMessage = "Function MINUS has illegal arguments";
				return null;
			}
			
			if(car(cdr(argList)).nodeType != 1 && car(cdr(argList)).nodeType != 2) {
				errorMessage = "Function MINUS has illegal arguments";
				return null;
			}
			
			if(aList.containsKey(car(argList).name)) {
				ArrayList<SExp> aux = aList.get(car(argList).name);
				val1 = aux.get(aux.size()-1).value;		
			}
			else
				val1 = car(argList).value;
			
			if(aList.containsKey(car(cdr(argList)).name)) {
				ArrayList<SExp> aux = aList.get(car(cdr(argList)).name);
				val2 = aux.get(aux.size()-1).value;
			}
			else
				val2 = car(cdr(argList)).value;
			return new SExp(val1-val2);
		}
		
		else {
			if(!dList.containsKey(fname)) {
				errorMessage = "No function defined with the name "+fname;
				return null;
			}
			ArrayList<SExp> temp = dList.get(fname);		//( (a b) (plus a b))
			SExp f = temp.get(temp.size()-1);
			if(length(car(f)) != length(argList)) {
				errorMessage = "Unexpected number of arguments to function "+fname;
				return null;
			}
			int result = addPairs(car(f), argList);
			if(result == -1) {
				errorMessage = "Unexpected number of arguments to function "+fname;
				return null;
			}
			SExp res = eval(car(cdr(f)));
			removePairs(car(f));
			return res;
		}		
	}
	
	
	//evlis function definition
	
	int flag;
	public SExp evlis(SExp exp) {
		if(exp.getIsNil())
			return exp;
		else {
			flag=0;
			if(car(exp)==null) {
				errorMessage = "Invalid Arguments to function";
				return null;
			}
			SExp e1 = eval(car(exp));
			
			if(e1 == null)	return null;		
			SExp e2 = evlis(cdr(exp));
			if(e2 == null) return null;
			return cons(e1,e2);
		}
	}
	
	//evcon function definition
	
	public SExp evcon(SExp be) {
		//System.out.println("beeee");
		if(be.getIsNil()) {
			errorMessage = "All booleans evaluate to NIL in COND";
			return null;
		}
		else {
			if(length(car(be)) != 2) {
				errorMessage = "All clauses of COND must be a list of size 2";
				return null;
			}
			SExp expr = eval(car(car(be)));
			if(expr == null)	return null;
			else if(!eval(car(car(be))).getIsNil())
				return eval(car(cdr(car(be))));
			else
				return evcon(cdr(be));
		}
	}
	
	public boolean checkTypes(SExp e) {
		if(e.getIsNil())	return true;
		if(isAtom(e).name.equals("T")) {
			if(e.nodeType == 2)
				return true;
		}
		if(car(e).nodeType == 2)
			return checkTypes(cdr(e));
		else return false;
	}
	
	
	//eval function definition
	
	public SExp eval(SExp parsedExp){
		
		SExp expr = isAtom(parsedExp);
		if(expr==null) {
			return null;
		}
		//System.out.println("inside eval" + printTree(parsedExp) );
		if(!isAtom(parsedExp).getIsNil()) {
			//System.out.println("hee" + "inside eval" + printTree(parsedExp) + "nt" + parsedExp.nodeType);
			if(parsedExp.nodeType == 1 || parsedExp.name.equals("T") || parsedExp.getIsNil()) {
				
				return parsedExp;
			}
			else if(aList.containsKey(parsedExp.name)) {
				ArrayList<SExp> aux = aList.get(parsedExp.name);
				
				return aux.get(aux.size()-1);			//write a while loop here to keep iterating until number is seen as binding
			}
			else {
				errorFlag = 1;
				errorMessage = "Unbound Variable " + parsedExp.name;
				return null;
			}
				
		}
		
		else if(!isAtom(car(parsedExp)).getIsNil()) {
			if(car(parsedExp).name.equals("QUOTE")) {
				if(length(cdr(parsedExp)) != 1) {
					errorMessage = "UNEXPECTED ARGUMENTS TO QUOTE";
					return null;
				}
				return car(cdr(parsedExp));
			}
			else if(car(parsedExp).name.equals("DEFUN")){
				if(length(cdr(parsedExp)) != 3) {
					errorMessage = "Invalid Structure for DEFUN";
					return null;
				}
				//System.out.println("PLSS" + car(cdr(cdr(parsedExp))).nodeType);
				
				if(car(cdr(cdr(parsedExp))).nodeType == 1 || car(cdr(cdr(parsedExp))).nodeType == 2) {
					errorMessage = "DEFUN should have list of parameters";
					return null;
				}
				if(checkTypes(car(cdr(cdr(parsedExp)))) == false) {
					errorMessage = "DEFUN'S PARAMETER LIST SHOULD HAVE SYMBOLS";
					return null;
				}
				
				if(dList.containsKey(car(cdr(parsedExp)).name)) {
					//System.out.println("hereeee");
					
					ArrayList<SExp> temp = dList.get(car(cdr(parsedExp)).name);
					
					temp.add(cdr(cdr(parsedExp)));
					
					dList.put(car(cdr(parsedExp)).name, temp);
					
				}
				else {
					ArrayList<SExp> temp = new ArrayList<SExp>();
					temp.add(cdr(cdr(parsedExp)));
					dList.put(car(cdr(parsedExp)).name, temp);
				}
				return (car(cdr(parsedExp)));
			}
			else if(car(parsedExp).name.equals("COND")) {
				return evcon(cdr(parsedExp));
			}
			else {
				
				SExp e = evlis(cdr(parsedExp));
				if(e==null) {
					
					return null;
				}
					
				return apply(car(parsedExp), e);
			}
		}
		else {
			errorFlag = 1;
			errorMessage = "Cannot Evaluate " + printTree(parsedExp);
			return null;
		}		
	}
	
}

