package L3Visitor;

import java.util.Vector;

public class L3Visitor implements EG2Visitor {
	Vector<Vector<String>> instructions;
	Vector<String> identifiers;
	int loc;  //stores register number
	boolean ints, bools;
	public L3Visitor(){
		instructions= new Vector<Vector<String>>();
		identifiers= new Vector<String>();
		loc=0;
		//for future implementation of boolean operators
		ints=false; bools=false;
	}

	//check for and retrieve locations of identifiers
	public boolean contains (String s){
		return identifiers.contains(s);
	}
	public int  retrieveIndex(String id) {
		return identifiers.indexOf(id);
	}
	
	public Object visit(SimpleNode node, Object data) {
		data = node.childrenAccept(this, data);
		System.out.println("visited: " + node);
		return data;
	}

	public Object visit(ASTStart node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	public Object visit(ASTdecls node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}
	
	public Object visit(ASTd node, Object data) {
		if(node.value!=null) {
			data = node.childrenAccept(this, data);
			return data;}
		else return null; //if d::=epsilon
	}

	//boolean can let us know what type an assigned variable will be
	public Object visit(ASTdecl node, Object data) {
		data = node.childrenAccept(this, data);
		if(node.value.equals("int")) ints=true; bools= false;
		if(node.value.equals("bools")) ints=false; bools= true;
		return data;
	}

	//nodes with assignemnts save the location as their value
	public Object visit(ASTvars node, Object data) {
		
		Vector<String> loadinst = new Vector<String>(4);
	
		loadinst.add("load"); 
		loadinst.add("t"+loc);
		loadinst.add((String) node.value);
		loadinst.add("");
		instructions.add(loadinst);
		identifiers.add(loc, (String)node.value);
		
		node.jjtSetValue("t"+loc++);
		
		data = node.childrenAccept(this, data);
		System.out.println(loadinst + " added to instructions");
		return data;
	}

	//cn have loadinstruction or nothing
	public Object visit(ASTv node, Object data) {
		//System.out.println(node);
		if(node.value!=null){
			Vector<String> loadinst = new Vector<String>(4);
			loadinst.add("load"); 
			loadinst.add("t"+loc);
			loadinst.add((String) node.value);
			loadinst.add("");
			instructions.add(loadinst);
			identifiers.add(loc, (String)node.value);
			
			node.jjtSetValue("t"+loc++);
			System.out.println(loadinst + " added to instructions");
			data = node.childrenAccept(this, data);
			return data;
		}
		else return null;
	}

	
	public Object visit(ASTstmts node, Object data) {
		System.out.println(node);
		data = node.childrenAccept(this, data);
		return data;
	}
	
	public Object visit(ASTst node, Object data) {
		if(node.value.equals(1)){
			data = node.childrenAccept(this, data);
			return data; }
		else return null;
	}

	
	public Object visit(ASTstmt node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	
	public Object visit(ASTassn node, Object data) {
		System.out.println("visited: "+node);
		data = node.childrenAccept(this, data);
		Vector<String> loadinst = new Vector<String>(4);
		//if(node.jjtGetNumChildren()>0) {

			loadinst.add("load"); 
			loadinst.add("t"+ retrieveIndex((String)node.value));
			loadinst.add((String) node.value);

			SimpleNode n= (SimpleNode) node.jjtGetChild(0);
			loadinst.add((String) n.value);
			instructions.add(loadinst);
			identifiers.add(loc, (String)node.value);

			System.out.println(loadinst + " added to instructions");
			node.jjtSetValue("t"+loc++);
		//}
		
		return data;
	}

	
	public Object visit(ASTarith node, Object data) {
		data = node.childrenAccept(this, data);
		System.out.println("visited: "+node);
		return data;
	}

	//deal with addition here
	public Object visit(ASTar node, Object data) {
		System.out.println(node);
		if(node.value!=null){		
			System.out.println(node);
			Vector<String> addins = new Vector<String>(4);
			addins.add("add");
			addins.add("t"+loc);			
			SimpleNode n= (SimpleNode) node.jjtGetChild(0);
			SimpleNode m= (SimpleNode) node.jjtGetChild(1);
			addins.add((String) n.value);
			addins.add((String) m.value);
			System.out.println(node);
			instructions.add(addins);
			identifiers.add(loc, (String)node.value);
			System.out.println(addins + " added to instructions");

			node.jjtSetValue("t"+loc++);
			data = node.childrenAccept(this, data);
			return data; }
		else return null;
	}

	//term
	public Object visit(ASTterm node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	//tr
	public Object visit(ASTtr node, Object data) {
		if(node.value!=null){
			Vector<String> mul = new Vector<String>(4);
			mul.add("multiply");
			mul.add("t"+loc);
			SimpleNode n= (SimpleNode) node.jjtGetChild(0);
			SimpleNode m= (SimpleNode) node.jjtGetChild(1);
			mul.add((String) n.value);
			mul.add((String) m.value);
			instructions.add(mul);
			
			data = node.childrenAccept(this, data);
			System.out.println(mul + " added to instructions");
			return data;
		}
		else return null;
	}

	//factor::= f | -f
	public Object visit(ASTfactor node, Object data) {
		if(node.value!=null){
			Vector<String> neg = new Vector<String>(4);
			neg.add("negate");
			SimpleNode n = (SimpleNode) node.jjtGetChild(0);
			neg.add("t"+loc);
			neg.add((String) n.value);
			neg.add("");
			
			instructions.add(neg);
			System.out.println(neg + " added to instructions");
		}
		
		data = node.childrenAccept(this, data);
		return data;
	}

	//f'::+ id | num | arith
	public Object visit(ASTf node, Object data) {
		String s1= (String)node.value;
		String s2= s1.substring(0, 1);
		String s3 = s1.substring(1,s1.length());
		if(node.value!=null){
			//f'::=id
			if(s2.equals("i")){
				if(contains(s3))
					node.jjtSetValue("t"+retrieveIndex(s3));
				else{
					Vector<String> loadinst = new Vector<String>(4);
					loadinst.add("load");
					loadinst.add("t"+loc);
					loadinst.add(s3);
					loadinst.add("");
					node.jjtSetValue("t"+loc++);
					instructions.add(loadinst);
					System.out.println(loadinst + " added to instructions");
				}
			}
			//f':==num
			else {
				Vector<String> loadinst = new Vector<String>(4);
				loadinst.add("loadliteral");
				loadinst.add("t"+loc);
				loadinst.add(s3);
				loadinst.add("");
				node.jjtSetValue("t"+loc++);
				instructions.add(loadinst);
				System.out.println(loadinst + " added to instructions");
			}
		}
		//otherwse f:== (arith), must keep going down tree
		data = node.childrenAccept(this, data);
		return data;
	}

	//did not have time to evaluate booleans, set them all to true
	public Object visit(ASTcond node, Object data) {
		data = node.childrenAccept(this, data);
		Vector<String> bool = new Vector<String>(4);
		bool.add("boolean");
		bool.add("t"+loc);
		bool.add("1");
		bool.add(" ");
		instructions.add(bool);
		System.out.println(bool+ "  added to iinstructions");
		node.jjtSetValue("t"+loc++);
		return data;
	}

	@Override
	public Object visit(ASTlogical node, Object data) {
		data = node.childrenAccept(this, data);
		return data;

	}

	@Override
	public Object visit(ASTcomp node, Object data) {
		data = node.childrenAccept(this, data);
		return data;

	}

}
