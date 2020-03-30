package Parser;

import java.util.Stack;

public class PredictiveParser {
	ParserElementGenerator edgar;
	Production[][] parseTable;
	Stack<Integer> s;
	int[] tokens;
	int choices;
	
	public PredictiveParser(int [] t){
		edgar= new ParserElementGenerator();
		edgar.PrepareFollowHashTable(-1);
		edgar.buildParsingTable();
		parseTable= edgar.getTable();
		tokens=t;
	}
	
	//this method takes in tokens in the form  <token> <value>
	//and adds onlt the <token> portion
	Boolean parseInputTokens(int[] a){
		tokens=a.clone();
		choices=0;
		return this.Parse();
	}
	
	
	Boolean Parse(){
		s=new Stack<Integer>();
		int[] right;
		//push $ onto stack
		s.push(20);
		//push S onto stack
		s.push(-1);
		//let a=start of token stream
		int i=0, a= tokens[0];	
		//let T be the top stack symbol
		int top=s.peek();
		//while t ne $
		while(top!=20){
			if(top==a){
				s.pop();
				i++;
				if(i<tokens.length)a=tokens[i];
				top=s.peek();
			}
			else if(top==12){
				s.pop();
				top=s.peek();
			}
			//if top is a terminal and doesnt match next
			//iinput token we know input is wrong
			else if(top>-1 && top<12){
				System.out.println("tokn end: "+top);
				return false;
			}
			//if there is  no table entry for (t,a)
			//it is also wrong
			else if(parseTable[-top-1][a]==null){
				System.out.println("table end");
				return false;
			}
			else {
				choices++;
				s.pop();
				right=parseTable[-top-1][a].RHS();
				for(int i1=1; i1<right.length+1; i1++){
					s.push(right[right.length-i1]);
				}
				top=s.peek();
		}
						
		}
		//if loop reaches end without errors then SUCCESS
		return true;
	}
	

}
