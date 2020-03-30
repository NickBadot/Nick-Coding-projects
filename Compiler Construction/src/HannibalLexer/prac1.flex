

%%
%standalone
%class HannibalFlexer
%init{ 
		MyHashTable table=new MyHashTable();
		int finHash=table.addSymbol("Finito");
		int hash;
		
%init}

%{
	String s;
}%




%eof{
%eof}


letter = [A-Za-z]
digit = [0-9]
alphanum= {letter} | {digit}
identifier= {letter}{alphanum}*
int	= {digit}*
lpar=[\{\(]
rpar=[\}\)]
whitespace = [ \n\t\r\v]
invalid = [^A-Za-z0-9\{\(\}\) \n\t\r\v\",]


%x STR

%%



\,				{System.out.println("Comma"); }
{lpar}			{System.out.println("LPar"); }
{rpar}			{System.out.println("RPar"); }

{int}			{	
					int t=0;
					String out=yytext();
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
						System.out.println("Int: "+t);
					}
				}
				
{identifier}	{ 
						hash=table.addSymbol(yytext());
						System.out.println("id: "+id+" "+yytext() );
				}

{invalid}		{ 		System.out.println("ERROR"); }		
				
{whitespace}     {}


\"		{ 
			s=""; 
			yybegin(STR);
		}

<STR>\" {yybegin(YYINITIAL); 
	System.out.println("String: "+s);
}

<STR>[^\"\\] {s+= yytext().charAt(i);}
"\""                         { s.append( '"' ); }













