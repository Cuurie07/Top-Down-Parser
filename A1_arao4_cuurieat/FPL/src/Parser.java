public class Parser {
	public static char a[] = new char[10];
	public static int in=1;
	public static char storeob;
	public static int n;
	public static int no;
	public static int codeptr = 0;
    public static String[] code = new String[100];
	
	public static void main(String[] args)
	{
		System.out.println("Enter an Expression, end with semi-colon!\n");
		Lexer.lex();
		new Program();
		Code.output();
	}
}


class Program extends Parser// Program -> Decls Stmts end
{
	Decls d;
	Stmts s;
	
	public Program() // Program -> Decls Stmts end
	{
		d=new Decls();
		Lexer.lex();
		s= new Stmts();
		
		if (Lexer.nextToken==21)
		{
	
		Code.gen(Code.print_return());	
		}
		
	}
}



class Decls // Decls -> int Idlist ';'
{
	Idlist Idlist;
	
	public Decls()
	{
		//already lexed INT   now lex i and ;
		
		Idlist = new Idlist();
	
		}
		
	}
	


class Idlist extends Parser//Idlist ->id [',' Idlist]
{
	char op;
	int i;
	Idlist i2;
	public Idlist()
	{

		i=Lexer.intValue;  // Int
		Lexer.lex();  // variable x
        a[in]=Lexer.ident;
		in++;
		Lexer.lex();
	
		while(Lexer.nextToken==Token.COMMA)
			 i2=new Idlist();
	}
	
	
}


class Stmts extends Parser// Stmts ->Stmt [Stmts]
{
	Stmt s;
	Stmts s1;
	Stmts s2;
	Stmts s11;
	Stmts s3,s12;
	int j;
	
	public Stmts()
	{
		s=new Stmt();
		if (Lexer.nextToken!=Token.KEY_END)
		{
		Lexer.lex();
		
		for(j=1;j<a.length;j++)
		{
			if(a[j]==storeob)
			{
				no=j;
				break;
			}
		}
		Code.gen(Code.intcodestore(no));
		
		while(Lexer.nextToken==Token.ID)
		{
			s1=new Stmts();
			if(Lexer.nextToken==Token.KEY_END)
				break;
			
		}
		}
		if(Lexer.nextToken==Token.KEY_IF)
			 s3=new Stmts();

		if(Lexer.nextToken==Token.KEY_FOR)
			 s12=new Stmts();
	
		}
		
	}

class Stmt extends Parser// Stmt -> Assign ';' | Cmpd | Cond | Loop
{
	Assign a;
	Cond c;
	Cmpd com;
	Loop l;
	public Stmt()
	{
		if(Lexer.nextToken==Token.KEY_FOR)
		{
		// GO TO FOR
				l=new Loop();
		}
		
		if(Lexer.nextToken==Token.KEY_IF)
		{
		// GO TO Cond
				c=new Cond();
		}
		if(Lexer.nextToken==Token.LEFT_BRACE)
		{
			com=new Cmpd();
		}
		if(Lexer.nextToken!=Token.KEY_END)
		{	
		if(Lexer.nextToken==Token.ID)
		{   
		    storeob=Lexer.ident;
	
			Lexer.lex();
		}
		if(Lexer.nextToken!=Token.KEY_ELSE)
		{	
		if(Lexer.nextToken!=Token.SEMICOLON)	
		{
		a=new Assign();
		}
		}
		
		}
		
	}
	
}


class Assign  // Assign -> id '=' Expr
{
	Expr e;

	public Assign()
	{
		
		int j=0; // count for istore 
		Lexer.lex(); // integer value
		e=new Expr();
		
	}
	
}


class Cmpd extends Parser// Cmpd -> '{' Stmts '}'
{
	Stmts s;
	
	public Cmpd()
	{
	
		Lexer.lex();
		if(Lexer.nextToken==Token.ID)
		{
			storeob=Lexer.ident;
	
		}
		Lexer.lex();
		s=new Stmts();
		Lexer.lex();//else
	
	}
	
}

class Cond extends Parser // if '(' Rexp ')' Stmt [else Stmt]
{
	Rexp r;
	Stmt s;
	Stmt s1;
	int n;
	
	public Cond()
	{
			Lexer.lex();
			Lexer.lex();
			r=new Rexp();
			Lexer.lex();
			s=new Stmt();
			if(Lexer.nextToken==Token.SEMICOLON)
			{
				for(n=1;n<a.length;n++)
				{
					if(a[n]==storeob)
					{
						no=n;
						break;
	 				}
				}
				Code.gen(Code.intcodestore(no));
			}

			if(Lexer.nextToken==Token.KEY_ELSE)
			{
				Lexer.lex();
				s1=new Stmt();
				
			}
			
		if(Lexer.nextToken!=Token.KEY_END)
		{
			Lexer.lex();
		while(Lexer.nextToken==Token.KEY_ELSE)
		{
		
			Lexer.lex();
			s1=new Stmt();
		}
		}
			}
	
}

class Rexp // Rexp -> Expr ('<' | '>' | '==' | '!=' ) Expr 
{
	Expr e;
	Expr e2;
	char op;
	
	
	public Rexp()
	{
		e=new Expr();
		if (Lexer.nextToken == Token.GREATER_OP || Lexer.nextToken == Token.LESSER_OP || Lexer.nextToken == Token.EQ_OP || Lexer.nextToken == Token.NOT_EQ ) 
		{
			op = Lexer.nextChar;
			Lexer.lex();
			e2 = new Expr();
			Code.gen(Code.cmcode(op));
				 
		}

	}
	
}

class Loop extends Parser// Loop -> for '(' [Assign] ';' [Rexp] ';' [Assign] ')' Stmt 
{
	Assign a1,a2;
	Rexp r1,r2;
	Stmt s;
	int n;
	char store;
	
	
			public Loop()
			{
				// 	LEXER SHUD ALREADY HAVE 'FOR' KEYWORD
				Lexer.lex(); // '('
				Lexer.lex();
				if(Lexer.nextToken==Token.ID)
				{
					storeob=Lexer.ident;
					Lexer.lex();
					a1=new Assign ();
					for(n=1;n<a.length;n++)
					{
						if(a[n]==storeob)
						{
							no=n;
							break;
						}
					}
					Code.gen(Code.intcodestore(no));
					
				}
				
				//LEXER SHOULD ALREADY HAVE ';'
				Lexer.lex();
				if(Lexer.nextToken==Token.ID)
				{
					r1=new Rexp();
					
				}
				
				//ALREADY LEXED TO ';'
				Lexer.lex();
				
				if(Lexer.nextToken==Token.ID)
				{   
					Code.gen("s");
					store=Lexer.ident;
					Lexer.lex();
					a2=new Assign();
				
				}
				for(n=1;n<a.length;n++)
				{
					if(a[n]==store)
					{
						no=n;
						break;
					}
				}
				
				Code.gen(Code.intcodestore(no));
				
				Lexer.lex();  //  Token
					
				s=new Stmt();
				
				
				
			}
	
}

class Expr  
{ // Expr -> Term (+ | -) Expr | Term
	Term t;
	Expr e;
	char op;

	public Expr() 
	{
		
		t = new Term();
		if (Lexer.nextToken == Token.ADD_OP || Lexer.nextToken == Token.SUB_OP) 
		{
			op = Lexer.nextChar;
			Lexer.lex();
			e = new Expr();
		Code.gen(Code.opcode(op));
					
		}
	}
}

class Term   { // Term -> Factor (* | /) Term | Factor
	Factor f;
	Term t;
	char op;
 
	public Term() {
		f = new Factor();
		if (Lexer.nextToken == Token.MULT_OP || Lexer.nextToken == Token.DIV_OP) {
			op = Lexer.nextChar;
			Lexer.lex();
			t = new Term();
			Code.gen(Code.opcode(op));
			}
			}
}

class Factor extends Parser { // Factor -> int_lit | id | '(' Expr ')'   
	Expr e;
	int i;
	int n=0;
	int index;
	static int num=0;
	String str1;

	public Factor() {
		switch (Lexer.nextToken) {
		case Token.INT_LIT: // number
			i = Lexer.intValue;
	
			Code.gen(Code.intcode(i));
			Lexer.lex();
			break;
		case Token.LEFT_PAREN: // '('
			Lexer.lex();
			e = new Expr();
			Lexer.lex(); // skip over ')'
			break;
		case Token.ID:
			
			for(n=0; n<a.length ; n++)
           {
        	   if(a[n]==Lexer.ident)
        	   {   
        	   index=n;
        	   break;
        	   }
        	   
           }
			Code.gen(Code.intload(index));
			Lexer.lex();
			break;
						
		default:
			break;
		}
	}
}


class Code extends Parser {
	
	//static String[] code = new String[100];
	//public static int codeptr = 0;
	static int n;
	static int x=0;
	static int add;
	static String[] sub=new String[5];
	static int flag;
	static int g=0;
	static String[] sub2=new String[5];
	
	public static void gen(String s) {
		code[codeptr] = s;
		codeptr++;
	}
	
	
	public static String intcode(int i) {
		if (i > 127) return "sipush " + i;
		if (i > 5) return "bipush " + i; 
		return "iconst_" + i;
	}
	
	public static String opcode(char op) {
		switch(op) {
		case '+' : return "iadd";
		case '-':  return "isub";
		case '*':  return "imul";
		case '/':  return "idiv";
		default: return "";
		}
	}
	public static String cmcode(char cm)
	{
		switch(cm)
		{
			case '>':return "if_icmple";
			case '<':return "if_icmpge";
			case '!':return "if_cmpeq";
			case '=':return "if_cmpneq";
			default:return "";
		}
	}
	
	public static String intcodestore(int i) 
	{
		if (i>3)
			return "istore "+i;
		else
			return "istore_" + i;
		
	}
	public static String intload(int index)
	{
		if(index>3)
			return "iload "+index;
		else
			return "iload_"+index;
		
	}
	public static String print_return() 
	{
		return "return";
	}
	
	
	
	
	public static void output() {
		int j=0; // counter for displaying instruction numbers 
		for (int i=0; i<codeptr; i++)
		{   
			if(code[i]!=null)
		{
			if(code[i]!="s")
			{
				if(code[i]=="return")
				{
					break;
				}
			System.out.println(j +": "+code[i]);
			}
			if(code[i]=="s" && flag==0)
			{   
				flag=1;
				add=i+5;
			for(n=i;n<add;n++)
			{	
				sub[x]=code[n];
				x++;
			}
				i=i+4;
			}
			if(code[i]=="s" && flag==1)
			{
				add=i+5;
				for(n=i;n<add;n++)
				{	
					sub2[g]=code[n];
					g++;
				}
					i=i+4;
			}
			
					
			boolean sival = code[i].contains("sipush");
			if(sival == true)
				j=j+2;
			
			boolean icmpleval = code[i].contains("if_icmple");
			if(icmpleval == true)
				j=j+2;
			
			boolean icmpgeval = code[i].contains("if_icmpge");
			if(icmpgeval == true)
				j=j+2;
			
			boolean bival = code[i].contains("bipush");
			if(bival == true)
				j=j+1;
			
			boolean istoreval = code[i].contains("istore ");
			if(istoreval == true)
				j=j+1;
			
			boolean iloadval = code[i].contains("iload ");
			if(iloadval == true)
				j=j+1;
			
			
		j++;
		}
		
		} // end of for Loop
		
		if(sub2[0]!=null)
		{
		for(int y=1;y<sub2.length;y++)
		{
			
			System.out.println(j+": "+sub2[y]);
			j++;
		}
		}
		if(sub[0]!=null)
		{
		for(int y=1;y<sub.length;y++)
		{
			
			System.out.println(j+": "+sub[y]);
			j++;
		}
		}
		
		System.out.println(j+": return");
	}
					
}


