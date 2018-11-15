/*
 * Author : Vedasri Uppala
 */

import java.util.HashMap;

public class SExp {
		int nodeType=0;
		int value=0;
		boolean isNIL=false;
		String name="";
		SExp left,right;
		static HashMap<String,SExp> idPointers=new HashMap<String,SExp>();
		public SExp() {
			isNIL=true;
			nodeType = 4;
		}
		public SExp(SExp leftChild, SExp rightChild) {
			left = leftChild;
			right = rightChild;
			nodeType = 3;
		}
		public SExp(int value) {
			nodeType = 1;
			this.value = value;
			left = null;
			right = null;
		}
		public SExp(String nameval) {
			name=nameval;
			nodeType=2;
			left=null;
			right=null;
		}
		public boolean getIsNil() {
			return isNIL;
		}
	}

