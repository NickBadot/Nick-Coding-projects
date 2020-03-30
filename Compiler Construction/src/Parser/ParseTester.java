package Parser;
/*
 * Author: Nicolas Smith 11463982
 * Used to test the parsers and related classes
 * 1 1 0 0 
2 55 0 0 
1 1 3 0 1 2 5 0 1 3 0 0 
2 3 4 0 2 4 4 0 2 5 0 0 
1 1 9 0 2 3 11 0 2 7 10 0 0 0 
1 1 9 0 1 1 9 0 7 0 2 3 3 0 2 4 8 0 5 0 2 5 11 0 1 2 10 0 11 0 1 3 10 0 0 0 
1 1 7 0 7 0 1 2 9 0 2 1 10 0 5 0 1 2 9 0 2 1 10 0 8 0 3 0 1 2 9 0 2 2 10 0 5 0 1 2 9 0 2 2 10 0 8 0 0 0 
 */



public class ParseTester {
	public static void main (String[] argv){
		ParserTokenizer meredith= new ParserTokenizer();
		int a[]= meredith.createInputTokens("1 1 0 0");
		int b[]= meredith.createInputTokens("2 55 0 0");
		int c[]= meredith.createInputTokens("1 1 3 0 1 2 5 0 1 3 0 0");
		int d[]= meredith.createInputTokens("2 3 4 0 2 4 4 0 2 5 0 0");
		int e[]= meredith.createInputTokens("1 1 9 0 2 3 11 0 2 7 10 0 0 0 ");
		int f[]= meredith.createInputTokens("1 1 9 0 1 1 9 0 7 0 2 3 3 0 2 4 8" +
				" 0 5 0 2 5 11 0 1 2 10 0 11 0 1 3 10 0 0 0 ");
		int g[]=meredith.createInputTokens("1 1 7 0 7 0 1 2 9 0 2 1 10 0 5 0 1 2 9 0 2 1 10 0 8" +
				" 0 3 0 1 2 9 0 2 2 10 0 5 0 1 2 9 0 2 2 10 0 8 0 0 0 ");
		
//		String s="[";
//		for(int i=0; i<a.length; i++){
//			s+=a[i]+",";
//		}
//		s+="]";
//		System.out.println(s);
		RecursiveDescentParser hermes= new RecursiveDescentParser();
		PredictiveParser peggy = new PredictiveParser(a);
		
		System.out.println("a");
		if(hermes.parseInputTokens(a)[0]==1) System.out.println("SUCCESS");
		else System.out.println("FAILURE");
		System.out.println("RDP Correct Choices="+hermes.correctChoices);
		System.out.println("RDP Wrong Choices="+hermes.wrongChoices);
		System.out.println(peggy.parseInputTokens(a)+" "+ peggy.choices);
		
		System.out.println("b");
		if(hermes.parseInputTokens(b)[0]==1) System.out.println("SUCCESS");
		else System.out.println("FAILURE");
		System.out.println("RDP Correct Choices="+hermes.correctChoices);
		System.out.println("RDP Wrong Choices="+hermes.wrongChoices);
		System.out.println(peggy.parseInputTokens(b)+" "+ peggy.choices);
		
		System.out.println("c");
		if(hermes.parseInputTokens(c)[0]==1) System.out.println("SUCCESS");
		else System.out.println("FAILURE");
		System.out.println("RDP Correct Choices="+hermes.correctChoices);
		System.out.println("RDP Wrong Choices="+hermes.wrongChoices);
		System.out.println(peggy.parseInputTokens(c)+" "+ peggy.choices);
		
		System.out.println("d");
		if(hermes.parseInputTokens(d)[0]==1) System.out.println("SUCCESS");
		else System.out.println("FAILURE");
		System.out.println("RDP Correct Choices="+hermes.correctChoices);
		System.out.println("RDP Wrong Choices="+hermes.wrongChoices);
		System.out.println(peggy.parseInputTokens(d)+" "+ peggy.choices);
		
		
		System.out.println("e");
		if(hermes.parseInputTokens(e)[0]==1) System.out.println("SUCCESS");
		else System.out.println("FAILURE");
		System.out.println("RDP Correct Choices="+hermes.correctChoices);
		System.out.println("RDP Wrong Choices="+hermes.wrongChoices);
		System.out.println(peggy.parseInputTokens(e)+" "+ peggy.choices);
		
		
		System.out.println("f");
		if(hermes.parseInputTokens(f)[0]==1) System.out.println("SUCCESS");
		else System.out.println("FAILURE");
		System.out.println("RDP Correct Choices="+hermes.correctChoices);
		System.out.println("RDP Wrong Choices="+hermes.wrongChoices);
		System.out.println(peggy.parseInputTokens(f)+" "+ peggy.choices);
		
		System.out.println("g");
		if(hermes.parseInputTokens(g)[0]==1) System.out.println("SUCCESS");
		else System.out.println("FAILURE");
		System.out.println("RDP Correct Choices="+hermes.correctChoices);
		System.out.println("RDP Wrong Choices="+hermes.wrongChoices);
		System.out.println(peggy.parseInputTokens(g) +" "+ peggy.choices);
		
		ParserElementGenerator enoch= new ParserElementGenerator();
		enoch.PrepareFollowHashTable(-1);
		enoch.buildParsingTable();
		System.out.println(enoch.displayTable());
		
		
	}

}
