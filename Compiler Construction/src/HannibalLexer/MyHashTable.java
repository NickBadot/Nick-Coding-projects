package HannibalLexer;
/*
 * Program to represent a hash table. Uses array o primes to calculate Hash values.
 * Author: Nicolas Smith 11463982
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MyHashTable {
	//charArray stores padded symbols that are elements of our hashtable
	char[] charArray = new char[4096];
	//intArray stores markers for what index of the charArray a symbol starts at
	int[] intArray = new int[512];
	int[] primes = new int[37];
	int primeCount;		//keep position in primes array
	int next; 				// points to next free slot
	int end;				//points to end of last symbol
	int symbolTotalHash;	//counts the total hash of a symbol, reset when symbol is done
	Scanner scanPrime;
	String symbol;
	
//constructor
	public MyHashTable() {
		try {
			scanPrime = new Scanner(new File("prime.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(int i=0; i<37; i++ ){
			primes[i]=scanPrime.nextInt();			
		}
		primeCount=0;
		for (int i = 0; i < 4096; i++)
			charArray[i] = '~';
		for (int i = 0; i < 512; i++)
			intArray[i] = -1;
		next = 0;
		end = -1;
		symbolTotalHash=0;
	}

	//adds symbol
	int addSymbol(String s){
		symbol=s;
		symbolTotalHash=0;
		primeCount=0;
		for(int i = 0; i < symbol.length(); i++)
				addChar(symbol.charAt(i));
		int c=placeSymbol();
		return c;
	}
//Function takes in array index to charArray and sees if current symbol matches stored symbol
	Boolean checkMatch( int start){
		for(int i=0; i<symbol.length(); i++){
			if(symbol.charAt(i)!= charArray[start+i]) return false;
		}
		if(charArray[start+symbol.length()] != '~') return false; //this is to check for prefix error
		//i.e. that the string 'symbol' matches the charAray value perfectly and is not a prefix
		return true;
	}
	
	//this method takes in a single character from a symbol and places it into the charArray
	//then updates the total hash for the symbol
	int addChar(char c) {
		//handle illegal character
		if(c=='~'){
			System.out.println("Illegal syymbol '~' has been discarded");
			return symbolTotalHash;
		}
		charArray[next] = c;
		next++; 						   // increment next char counter
		int nprime = primes[primeCount]; // read in next prime to compute hash
		primeCount++;
		symbolTotalHash += nprime * (int) c;
		return symbolTotalHash;
	}

//after a every character of a symbol is acquired this method computes where in the
//hash table to place it and returns the index of the symbol in the integer array
	int placeSymbol() {
		int ideal_position=symbolTotalHash % 512;
		Boolean positionFits=false;
		int iterations=0;
		//loop ends if symbol is placed or if it goes through the entire array
		while(!positionFits && iterations<512){
			//case 1, slot is free. We add the start index of this symbol to the intArray, then increment counters
			if(intArray[ideal_position]==-1){
				positionFits=true;
				intArray[ideal_position]=++end;
				end+=symbol.length();				//point to the end of the newly inserted symbol
				next++;
			}
			//if slot is not empty we must check if the symbol already exists in this place
			//if so we do not need to add it
			else if (checkMatch(intArray[ideal_position])){
					positionFits=true;
					//reset next counter
					next=end+1;
				}
			else {
				//if we reach the end of the array we must loop back around to the front
				if(ideal_position==512) ideal_position=0;
				else ideal_position++;
				iterations++;
			}
			
		}
		if(iterations==512) System.out.print("Overflow Error Detected");
		if(positionFits)return ideal_position;
		else return -1;
	}
	//string represention of table
	String tableToString(){
		String temp="",s="";
		char c;
		for(int i=0; i<512; i++){
			if(intArray[i]!=-1){
				int j=intArray[i];			//retrive index of first character
				c=charArray[j];
				s+=i+" "+j+':';
				while(c!='~'){
					temp=temp+c;
					c=charArray[++j];					
				}
				s+=temp+'\n';
				temp="";
			}
		}
		return s;
	}

}
