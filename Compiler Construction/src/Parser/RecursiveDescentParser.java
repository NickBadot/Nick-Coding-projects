package Parser;

import java.util.Vector;

/*
 * Author: Nicolas Smith 11463982
 * A Reursive Descent Parser that uses a parse() function
 * to see if an input of tokens matches the rules of a given grammar
 */


public class RecursiveDescentParser {
	Grammar grammar;
	ParserTokenizer input;
	int[]tokens;
	int nonterminal;
	int SYM;
	int correctChoices,wrongChoices;
	
	
	public RecursiveDescentParser(){
		grammar= new Grammar(1);
	}
	
	
	
	//passes string in sentential form, NOT a string of tokens
	String ParseString(String s){
		input=new ParserTokenizer(s);
		tokens= input.justTokenStream();
		correctChoices=0;
		wrongChoices=0;
		int [] res=Parse(-1,0);
		if(res[0]==1){
			s+=" : SUCCESS";
		}
		else if(res[0]==-1) s+=" : FAILURE";		
		return s;
	}
	
	
	//this method takes in tokens in the form  <token> <value>
	//and adds onlt the <token> portion
			int[] parseInputTokens(int[] a){
				for(int num: a){
					System.out.print(num+" ");
				}
				tokens=a.clone();
				correctChoices=0;
				wrongChoices=0;
				return this.Parse(-1,0);
			}
		
	
	//Function takes in a nonterminal and a starting position to parse tokens and see iif grammar 
	//rules are upheld
	int[] Parse(int NT, int startPos){
		int[] result= new int[2];
		int nextPos=startPos;
		Vector<Production> prod=grammar.getProductions(NT);
		//loop over productions whose LHS is NT 
		next:for(int i=0; i< prod.size(); i++){
			Production P=prod.elementAt(i);
			nextPos=startPos;
		
			//access RHS of P with array pro
			int[] pro= P.RHS();

			for(int j=0; j<pro.length; j++){
				SYM=pro[j];
//				System.out.println(SYM);
				if(SYM>-1){		//if SYM is a terminal it has value 0 or greater 
					if(tokens[nextPos]==SYM || SYM==12){	//if it matches input stream
						if(SYM!=12)
							nextPos++;
					}
					else {
						wrongChoices++;
						continue next;		//otherwise we break the loop
					}
				}
				else {
					int[] res=Parse(SYM,nextPos);
					if(res[0]==1){		//if Parser returns SUCCESS 
						nextPos=res[1];
					}
					else {
						wrongChoices++;
						continue next;			//otherwise we break the loop
					}
				}
				
			}
//			System.out.println("matching RHS found");
			correctChoices++;
			result[0]=1;
			result[1]=nextPos;
			return result;		//this will only be reached if the above loop never breaks	
			
		}
		wrongChoices++;
		result[0]=-1;
		System.out.println(tokens[nextPos]);
		return result;		//this is only reached if SUCCESS is never reached

	}
	
}
