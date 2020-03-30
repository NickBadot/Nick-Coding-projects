package HannibalLexer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/*
 * Author: Nicolas Smith 11463982
 * Task:
 * Lexer class that reads input from a file and outputs tokens and their values.
 * Handles token types: id, string, int, Lpar, Rpar, comma and error
 * Subtasks:
 * Symbol Table 		: done
 * Finite State machine : done
 * Output tokens		: done
 * Integer limits		: done
 * Escape characters	: done
 * Stop at Finito		: done
 * Attribute handling   : done
 * Store id's			: done
 */

public class HannibalLexer {
	int finHash;				//hash for final word 'Finito' used to stop loop
	char d;						//probably gonna need this at some point
	Scanner charScanner;		//probably want to store the info from the file
	char[] fileArray;			//rather than reading it from the file every time
	String out;					//will need some output				
	int attribute;				//each token has an attribute
	int STATE;					//defines States of our FSA
	MyHashTable table;			//to store symbols
	
	//constuctor
	public HannibalLexer(){
		table= new MyHashTable();
		finHash=table.addSymbol("Finito");
		out="";
		STATE=0;
		try {
			charScanner = new Scanner(new File("test"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(charScanner.hasNext()){
			out+=charScanner.nextLine();
		}
		
		fileArray = out.toCharArray();		//turn long string into charArray
		out="";
	}
	
	//prints out a token, called when a final accepting state is reached by the FSA
	void returnToken(String token){
		//we deal with id, int and string separately as we need to do something with attributes
		if(token=="id"){ 
			attribute=table.addSymbol(out);
			out=token+' '+attribute+' '+out;
		} //we only want to add symbols to our symbol table
		else if(token=="String"){
			out=token+' '+out;
		}
		else if(token=="int"){
			int t=0;
			//loop deals with ints and ensures no int longer than the limit goes through
			for(int i=0; i<out.length(); i++){
				if(t>=11335577){
					t=-1;
					break;
				}
				if(t==0){
					t+= Character.getNumericValue(out.charAt(i));
				}
				else {
					t*=10;
					t+= Character.getNumericValue(out.charAt(i));
				}
			}
			out=token+" "+t;
		}
		else {
			attribute=0;
			out=token+" "+attribute;
		}
		System.out.println(out);
		out="";//this method should clear the output string..otherwise bad things will happen
	}
	
	/*
	 * This method reads in characters and puts them through 
	 * a finite state automata that determines what type of token is being constructed.
	 * Prepare for if statements galore. (used to check which State to transition to
	 * States:
	 * 0=Start
	 * 1=LPar
	 * 2=RPar
	 * 3=Comma
	 * 4=id
	 * 5=int
	 * 6=string
	 * 7=error
	 */
	void finiteStateAutomata(){
		Boolean specialChar=false;							//detects escape characters in String
		int i=0;
		while(attribute!=finHash && i<fileArray.length){
			d=fileArray[i];
			//System.out.println("Received: "+d);
			i++;
			switch (STATE) {
			//start (returns to start if ' ', '\t' or '\n' is found
			case 0:
				if(d==' ' || d=='\n' || d=='\t') { STATE=0;break; }
				out+=d;
				if(d=='{' || d=='[' || d=='(') { STATE=1;break; }
				if(d=='}' || d==']' || d==')') { STATE=2;break; }
				if(d==',') { STATE=3;break; }
				if((d>= 'a' && d <= 'z')|| (d>= 'a' && d <= 'z')) { STATE=4;break; }
				if(d>= '0' && d <= '9') {STATE=5; break; }
				if(d=='"'){ STATE=6; break; }
				else {STATE=7; break;}
			//LPar
			case 1:
				returnToken("LPar");
				if(d==' ' || d=='\n' || d=='\t') { STATE=0;break; }
				out+=d;
				if(d=='{' || d=='[' || d=='(') { STATE=1;break; }
				if(d=='}' || d==']' || d==')') { STATE=2;break; }
				if(d==',') { STATE=3;break; }
				if((d>= 'a' && d <= 'z')|| (d>= 'A' && d <= 'Z')) { STATE=4;break; }
				if(d>= '0' && d <= '9') {STATE=5; break; }
				if(d=='"'){ STATE=6; break; }
				else {STATE=7; break;}
			//RPar	
			case 2:
				returnToken("RPar");
				if(d==' ' || d=='\n' || d=='\t') { STATE=0;break; }
				out+=d;
				if(d=='{' || d=='[' || d=='(') { STATE=1;break; }
				if(d=='}' || d==']' || d==')') { STATE=2;break; }
				if(d==',') { STATE=3;break; }
				if((d>= 'a' && d <= 'z')|| (d>= 'A' && d <= 'Z')) { STATE=4;break; }
				if(d>= '0' && d <= '9') {STATE=5; break; }
				if(d=='"'){ STATE=6; break; }
				else {STATE=7; break;}
			//Comma	
			case 3:
				returnToken("Comma");
				if(d==' ' || d=='\n' || d=='\t') { STATE=0;break; }
				out+=d;
				if(d=='{' || d=='[' || d=='(') { STATE=1;break; }
				if(d=='}' || d==']' || d==')') { STATE=2;break; }
				if(d==',') { STATE=3;break; }
				if((d>= 'a' && d <= 'z')|| (d>= 'A' && d <= 'Z')) { STATE=4;break; }
				if(d>= '0' && d <= '9') {STATE=5; break; }
				if(d=='"'){ STATE=6; break; }
				else {STATE=7; break;}
		    //id
			case 4:
				if((d>= 'a' && d <= 'z')|| (d>= 'A' && d <= 'Z')|| (d>= '0' && d <= '9')) {out+=d; break; }
				returnToken("id");  //if d!=digit or char then end of identifier 
				if(d==' ' || d=='\n' || d=='\t') { STATE=0; break;}
				out+=d;
				if(d=='{' || d=='[' || d=='(') { STATE=1; break; }
				if(d=='}' || d==']' || d==')') { STATE=2; break; }
				if(d==',') { STATE=3;  break; }
				if(d=='"'){ STATE=6; break; }
				else {STATE=7;  break;}
			//int	
			case 5:
				if(d>= '0' && d <= '9') { out+=d; break; }
				returnToken("int");		//if next char is not a number we return the int token
				if(d==' ' || d=='\n' || d=='\t') { STATE=0;  break;}
				out+=d;
				if(d=='{' || d=='[' || d=='(') { STATE=1;  break; }
				if(d=='}' || d==']' || d==')') { STATE=2;  break; }
				if(d==',') { STATE=3;  break; }
				if((d>= 'a' && d <= 'z')|| (d>= 'A' && d <= 'Z')) {STATE=4;  break; }
				if(d=='"'){ STATE=6;  break; }
				else {STATE=7;  break;}
			//string	
			case 6:
				//use a boolean to see if we have to do anything speciial due to a '/'' character
				if(d=='/' && !specialChar){ specialChar=true; break;} 
				out+=d;
				if(specialChar){ specialChar=false; break; }
				if(d=='"'){  returnToken("String"); STATE=0; break; }
				if(i==fileArray.length) { returnToken("Error"); break; }
				else { break; }
			//error	
			case 7:
				returnToken("error");
				if(d==' ' || d=='\n' || d=='\t') { STATE=0;  break;}
				out+=d;
				if(d=='{' || d=='[' || d=='(') { STATE=1;  break; }
				if(d=='}' || d==']' || d==')') { STATE=2;  break; }
				if(d==',') { STATE=3;  break; }
				if((d>= 'a' && d <= 'z')|| (d>= 'A' && d <= 'Z')) {STATE=4;  break; }
				if(d>= '0' && d <= '9') {STATE=5; break; }
				if(d=='"'){ STATE=6;  break; }
				else {STATE=7; break;}
			//if any character that cannot be handled occurs we default to error	
			default: STATE=7;
			}
		}
	}	
	
	
	
	
}
