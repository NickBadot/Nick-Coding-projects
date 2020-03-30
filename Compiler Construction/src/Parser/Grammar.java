package Parser;

import java.util.Vector;

/*
 * GRAMMAR REPRESENTATION
 * Author: Nick Smith 11463982
 * Class used to represent a grammmar, supports the given 
 * grammar and the grammar in LL() form, this is input 
 * manually not computed 
 * Terminals:
 *0 = eof 
 *1 = id 
 *2 = num 
 *3 = + 
 *4 = -
 *5 = * 
 *6 = / 
 *7 = ( 
 *8 = ) 
 *9 = [ 
 *10 = ] 
 *11 = , 
 *12 = epsilon
 *Nonterminals:
 *-1 = S
 *-2 = Expr
 *-3 = E'
 *-4= Term
 *-5= T'
 *-6=Factor
 *-7=FnCall
 *-8=Aref
 *-9=Arguments
 *-10=Indices
 *-12= F'
 *-13= A'
 *-14= I'
 */
		 

public class Grammar {
	Vector<Production> productions=new Vector<Production>();
	//Create all necessary productions in constructor
	//Implement modal construction where a value can be passed to construct modified grammars
	public Grammar(){
		productions.add(new Production(-1,-2, 0));		//S::=Expr eof
		productions.add(new Production(-2,-4, -3));		//Expr::=TermE'
		productions.add(new Production(-3,3,-4,-3));	//E'::= +TermE' 
		productions.add(new Production(-3,4,-4, -3));	//E'::= -TermE'
		productions.add(new Production(-3,12));       	//E'::=Epsilon
		productions.add(new Production(-4,-6, -5));		//Term::=FactorT'
		productions.add(new Production(-5,5,-6, -5));	//T'::= *FactorT'
		productions.add(new Production(-5,6,-6, -5));	//T'::= /FactorT'
		productions.add(new Production(-5,12));			//T':= Epsilon
		productions.add(new Production(-6, -7));		//Factor::= Fncall
		productions.add(new Production(-6, -8));		//Factor::= Aref
		productions.add(new Production(-6,1));			//Factor::= id
		productions.add(new Production(-6, 2));			//Factor::= num
		productions.add(new Production(-6,7,-2,8));		//Factor::= (Expr)
		productions.add(new Production(-7,1,7,-9,8));   //Fncall::=id(Arguments)
		productions.add(new Production(-8,1,9,-10,10));	//Aref::=id[Indices]
		productions.add(new Production(-9, -2));		//Arguments::=Exp
		productions.add(new Production(-9, -2,11,-9));  //Arguments==Exp, Arguments
		productions.add(new Production(-10, -2));		//Indices::=Expr
		productions.add(new Production(-10, -2,11,-10));//Indices::=Expr,Indices	
	}
	
	
	public Grammar(int mode){
		if(mode!=1){
			System.out.println("Not valid mode\n");
		}
		else{
			productions.add(new Production(-1,-2, 0));		//S::=Expr eof
			productions.add(new Production(-2,-4, -3));		//Expr::=TermE'
			productions.add(new Production(-3,3,-4,-3));	//E'::= +TermE' 
			productions.add(new Production(-3,4,-4, -3));	//E'::= -TermE'
			productions.add(new Production(-3,12));       //E'::=Epsilon
			productions.add(new Production(-4,-6, -5));		//Term::=TermT'
			productions.add(new Production(-5,5,-6, -5));	//T'::= *TermT'
			productions.add(new Production(-5,6,-6, -5));	//T'::= /TermT'
			productions.add(new Production(-5,12));			//T':= Epsilon
			productions.add(new Production(-6,1,-12));		//Factor::= idF'
			productions.add(new Production(-6, 2));			//Factor::= num
			productions.add(new Production(-6,7,-2,8));		//Factor::= (Expr)
			productions.add(new Production(-12,7,-9,8));	//F'::= (arguments)
			productions.add(new Production(-12,9,-10,10));	//F'::= [indices]
			productions.add(new Production(-12,12));		//F'::= Epsilon
			productions.add(new Production(-9,-2, -13));	//Arguments::=ExprA'
			productions.add(new Production(-13, 11,-9));	//A'::=,Arguments
			productions.add(new Production(-13, 12));		//A'::= Epsilon
			productions.add(new Production(-10, -2, -14));	//Indices::=ExprI'
			productions.add(new Production(-14,11,-10 ));	//I'::=, Indices
			productions.add(new Production(-14,12));		//I'::=Epsilon
	
		}
	}
	
	//String reproduction of all productions in grammar
	public String toString(){
		String s = "";
		for(int i=0; i<productions.size(); i++){
			s+=productions.elementAt(i).toString()+'\n';			
		}
		return s;
	}
	
	//returns vector of productions starting with given nonterminal on LHS
	Vector<Production> getProductions(int nonterminal){
		Vector<Production> prod= new Vector<Production>();
		for(int i=0; i<productions.size(); i++){
			if(productions.elementAt(i).LHS()==nonterminal){
				prod.add(productions.elementAt(i));
			}
		}
		return prod;
	}
	
	//checks if a non-terminal can be rewritten as epsilon
	Boolean checkEpsilon(int nonterminal){
		Vector<Production> prod= this.getProductions(nonterminal);
		for(int i=0; i<prod.size(); i++){
			if(prod.elementAt(i).hasEpsilon()){
				return true;
			}
		}
		return false;
	}
	
	//could be useful to get FOLLOW
	Vector<Production> productionsContaining(int k){
		Vector<Production> p= new Vector<Production>();
		for(int i=0; i<productions.size(); i++){
			if(productions.elementAt(i).contains(k)){
				p.add(productions.elementAt(i));
			}
		}
		return p;
	}
	
}
