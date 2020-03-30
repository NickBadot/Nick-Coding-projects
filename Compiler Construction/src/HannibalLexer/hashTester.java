package HannibalLexer;
//tester program for Hash Table. Nicolas Smith 11463982
public class hashTester {
	public static void main(String [] argv){
		MyHashTable table= new MyHashTable();
		String out;
		int a,b,c,d,e;
		a=table.addSymbol("Tom");
		b=table.addSymbol("Dick");
		c=table.addSymbol("Jum");
		d=table.addSymbol("Harry");
		e=table.addSymbol("Tom");
		System.out.println("a="+a+" b="+b+" c="+c+" d="+d+" e="+e);
		out=table.tableToString();
		System.out.println(out);
		
		System.exit(0);
	}

}
