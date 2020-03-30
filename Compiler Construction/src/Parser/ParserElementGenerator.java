
/*
 * Author: Nicolas Smith 11463982
 * Used to generate key components used withing parsers,
 * namely methods to produce FIRST and FOLLOW and to
 * build a parsing table with the results. 
 */
package Parser;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class ParserElementGenerator {
	Grammar g;
	Hashtable<Integer, Set<Integer>> followHashTable;
	private Production[][] parsingTable;
	public ParserElementGenerator() {
		g = new Grammar(1);
		followHashTable = new Hashtable<Integer, Set<Integer>>();
	}

	// computes first of single symbol, MUCH easier than only having one
	// function for an array
	Vector<Integer> first(int alpha) {
		Vector<Integer> first = new Vector<Integer>();
		// if alpha is noneterminal
		if (alpha > -1) {
			first.add(alpha);
		} else {
			if (g.checkEpsilon(alpha))
				first.add(-11);
			Vector<Production> P = g.getProductions(alpha);
			// loop through productions
			for (int i = 0; i < P.size(); i++) {
				// get the RHS of each production
				int[] v = P.elementAt(i).RHS();
				// check elements of RHS and add until we find one without
				// epsilon
				Boolean eps = true;
				int l = 0;
				// go until the production cannot be rewritten as epsilon or til
				// the end
				while (eps == true && l < v.length) {
					Vector<Integer> nextfirst = this.first(v[l]);
					if (!nextfirst.contains(-11)) {
						eps = false;
					} else
						nextfirst.remove(-11);
					// add all remaining elements of the next first set to
					// first(alpha)
					for (int k = 0; k < nextfirst.size(); k++) {
						first.add(nextfirst.elementAt(k));
					}
					l++;
				}
			}
		}
		return first;
	}

	// computes first set of an array, goes through all elements until one has
	// no epsilon in
	// their first set
	Vector<Integer> First(int[] beta) {
		Vector<Integer> first = new Vector<Integer>();
		Boolean noEpsilon = false;
		int i = 0;
		while (noEpsilon == false && i < beta.length) {
			Vector<Integer> nextfirst = first(beta[i]);
			if (!nextfirst.contains(12)) {
				noEpsilon = true;
			} else {
				nextfirst.removeElement(12);
			}
			for (int k = 0; k < nextfirst.size(); k++) {
				first.add(nextfirst.elementAt(k));
			}
			i++;
		}
		if (noEpsilon == false) {
			first.add(12);
		}

		return first;
	}

	// get follow set from hashtable
	Set<Integer> Follow(int nonterminal) {
		return followHashTable.get(nonterminal);
	}

	
	//method creates a hashtabe with Nonterminals as keys and
	//the follow set as values
	void PrepareFollowHashTable(int s) {
		//inherittable is used near the end to apply the last rule
		Hashtable<Integer, Set<Integer>> inheritHashTable = new Hashtable<Integer, Set<Integer>>();
		Set<Integer> v = new HashSet<Integer>(); // input vector
		Vector<Integer> u = new Vector<Integer>(); // looping vector
		v.add(20); //let $=20
		followHashTable.put(s,v);
		
		int sym;
		for (Production P : g.productions) {
			//System.out.println(P);
			// for each symbol in RHS of P (start at 1 cos args[0]=LHS)
			for (int i = 1; i < P.length(); i++) {
				sym = P.args.get(i);
				if (sym < 0) { // if sym is NT
					int[] b = new int[P.length() - i - 1]; // from SYM to nd of
															// RHS
					for (int j = i + 1; j < P.length(); j++) {
						b[b.length - (P.length() - j)] = P.args.get(j); // messy
					}

					u = this.First(b);
					for (Integer token : u) {
						// if token = epsilon
						if (token == 12) {
							// put Sym in inherit with LHS as key,
							// check if entry already exists first!
							if (inheritHashTable.containsKey(P.LHS())) {
								inheritHashTable.get(P.LHS()).add(sym);
								//System.out.println("inherit:"+inheritHashTable.get(P.LHS()));
							}
							// else create an entry
							else {
								v=new HashSet<Integer>();
								v.add(sym);
								//System.out.println(P.LHS() + " create inherit: "+ v);
								inheritHashTable.put(P.LHS(), v);
							}
						}
						// else add token to follow(sym)
						else {
							if (followHashTable.containsKey(sym)) {
								followHashTable.get(sym).add(token);
								//System.out.println("follow:"+followHashTable.get(sym));
							} else {
								v=new HashSet<Integer>();
								v.add(token);
								//System.out.println(sym + " created with: "+ v);
								followHashTable.put(sym, v);
							}
						}
					}
				}
			}
		}
		
		
		// second loop
		Boolean idle = false;
		while (!idle) {
			idle = true;
			// loop over keys of inherithashtable
			Set<Integer> k = inheritHashTable.keySet();
			for (Integer nt1 : k) {
				v = (Set<Integer>) inheritHashTable.get(nt1);
				// for every element associated with this key
				for (Integer nt2 : v) {
					// for every terminal in follow(key)

					for (Integer terminal : followHashTable.get(nt1)) {
						// if terminal in follow(key) is not in follow(token)
						if(!followHashTable.containsKey(nt2)){
							followHashTable.put(nt2, new HashSet<Integer>());
						}
						if (!followHashTable.get(nt2).contains(terminal)) {
							v = followHashTable.get(nt2);
							v.add(terminal);
							followHashTable.put(nt2, v);
							idle = false;
						}
					}
				}
			}
		}
	}

	void buildParsingTable() {
		int lcol;
		Boolean hasEpsilon;
	// There are 14 Nonterminals and 13 Terminal tokens (counting $)
		parsingTable = new Production[14][13]; 
	//for A:=alpha add (A,a) where a < First(alpha)
		for (Production p: g.productions) {
			hasEpsilon=false;
			lcol= p.LHS();
			for(int r: this.First(p.RHS())){
				if(r==12){
					hasEpsilon=true;
				}
				parsingTable[-lcol-1][r]=p;
			}
		//if First(alpha) contains epsilon we add Follow(A)
			if(hasEpsilon){
				for(int r: this.Follow(lcol)){
					//check for Sentinel
					if(r==20){parsingTable[-lcol-1][12]=p;}
					else parsingTable[-lcol-1][r]=p;
				}
			}
			
			// if a=$ and a is in FOLLOW(A) and e is in FIRST(alpha)
		}
	}
	
	String displayTable(){
		String s="";
		for(int i=0; i<13; i++){
			s+="   "+i+"   ";
		}
		s+="\n";
		for(int i=-1; i>-14; i--){
			s+=i+" ";
			for(int j=0; j<13; j++){
				s+=" "+parsingTable[-i][j];
			}
			s+="\n";
		}
		return s;
	}
	
	Production[][] getTable(){
		return parsingTable;
	}

}
