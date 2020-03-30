package Parser;

import java.util.StringTokenizer;

//import Practical1.MyHashTable;
//the input is a series of numbers in pairs, so all that you actually need to do with the tokenstream
//is read those numbers from a file; put every odd number, which represents the token type in the first column
//and every even number, which represents the attribute and is frequently 0, into the second column
//you will see that his input example on Moodle is a series of numbers like this
//I commented out the hash table as you're not suppossed to use it cause all the attribute values are given
//in the input

public class ParserTokenizer {
	int n=100,ln=0;
	Integer[][] tokenStream;
	//MyHashTable hTable=new MyHashTable();
	
	public ParserTokenizer(){
		tokenStream=new Integer[n][2];
		for(int i=0; i<n; i++){
			tokenStream[i][0]=99;			//99 denotes empty space
			tokenStream[i][1]=0;			//most tokens have attribute of 0 anyway	
		}
	}
	
	
	public ParserTokenizer(String s){
		tokenStream=new Integer[n][2];
		for(int i=0; i<n; i++){
			tokenStream[i][0]=99;			//99 denotes empty space
			tokenStream[i][1]=0;			//most tokens have attribute of 0 anyway	
		}
		getTokens(s);
	}
	
	void getTokens(String s){
		StringTokenizer tokenizer = new StringTokenizer(s,"+-/*()[], \n ",true);
		int i=0;
		while(tokenizer.hasMoreTokens()){
			String c=tokenizer.nextToken();
			if(c.equals("+")){tokenStream[i][0]=3;}
			else if(c.equals("-")){tokenStream[i][0]=4;}
			else if(c.equals("*")){tokenStream[i][0]=5;}
			else if(c.equals("/")){tokenStream[i][0]=6;}
			else if(c.equals("(")){tokenStream[i][0]=7;}
			else if(c.equals(")")){tokenStream[i][0]=8;}
			else if(c.equals("[")){tokenStream[i][0]=9;}
			else if(c.equals("]")){tokenStream[i][0]=10;}
			else if(c.equals(",")){tokenStream[i][0]=11;}
			else if(c.equals("eof")){tokenStream[i][0]=0;}			//eof=0
			else if(c.matches("-?\\d+(\\.\\d+)?")){ 
				tokenStream[i][0]=2;								//num=2
				tokenStream[i][1]=Integer.parseInt(c);
			}
			else if(c.equals(" ") || c.equals("\n")){ i--; ln--;}
			else {
				tokenStream[i][0]=1;								//id=1
				tokenStream[i][1]=0; //hTable.addSymbol(c);
				//Barbara says: the symbol table for this is fake; you're not supposed to implement that
			}	
			i++;
			ln++;
		}
	}
	
	public String toString(){
		String s="";
		for(int i=0; i<ln; i++){
			s+= tokenStream[i][0] +" "+ tokenStream[i][1] + "  ";
		}
		return s;
	}
	
	//We'll probably want to retrieve just the tokens (without attributes) at some point
	int[] justTokenStream(){
		int[] stream = new int[ln];
		for(int i=0; i<ln; i++)
			stream[i]=tokenStream[i][0];
		return stream;
		
	}
	
	//turns string of integers into array of tokens 
		//in form <token> <value>
		int[] createInputTokens(String s){
			
			int i=1;
			//sam is a good name
			StringTokenizer sam= new StringTokenizer(s," ", false);
			int[] b= new int[sam.countTokens()/2];
			//set first element
			String samschar=sam.nextToken();
			b[0]=Integer.parseInt(samschar);
			while(sam.hasMoreTokens()){
				samschar=sam.nextToken();
				if(sam.hasMoreTokens()){
				samschar=sam.nextToken();
					if(samschar.matches("-?\\d+(\\.\\d+)?")){ 
						b[i]=Integer.parseInt(samschar);
						i++;
					}
					else System.out.println("ERROR WITH INPUT");
				}	
			}
			return b;
		}
		
		
		
	
}
