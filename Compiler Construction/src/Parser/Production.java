package Parser;


import java.util.Arrays;
import java.util.List;
/*
 * Author: Nicolas Smith 11463982
 * Represents productions in a given grammar
 */

public class Production {
	List<Integer> args;
	
	//first argument is always left nonterminal
	public Production(Integer ... nonterminals){
		args= Arrays.asList(nonterminals);
	}
	
	public String toString(){
		String s="";
		s+=args.get(0)+"::= ";
		for(int j=1; j<args.size(); j++){
			s+=args.get(j)+" ";
		}
		return s;
	}
	
	//check if production contains epsilon
	Boolean hasEpsilon(){
		if(args.contains(-11)) return true;
		else return false;
	}
	
	Boolean contains(int k){
		if(args.contains(k)) return true;
		else return false;
	}
	
	int length(){
		return args.size();
	}
	
	//return LHS of production
	int LHS(){
		return args.get(0);
	}
	
	//returns all RHS elements
	int[] RHS(){
		int[] array=new int[args.size()-1];
		for(int i=1; i<args.size(); i++){
			array[i-1]=args.get(i);
		}
		return array;
	}
	
	//checks if production contains terminal
	Boolean hasTerminal(){
		for(int i=1; i<args.size(); i++){
			if(args.get(i)>=0) return true;
		}
		return false;
	}
}
