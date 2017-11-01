import java.util.*;

public class ParserAST {

	protected MicroScalaLexer lexer;
	protected Token token;

	public ParserAST () throws java.io.IOException {
		lexer = new MicroScalaLexer(System.in);
		getToken();
	}

	private void getToken () throws java.io.IOException {
		token = lexer.nextToken();
	}

	public Program program () throws java.io.IOException {

		String funcID = null;
		boolean maindef;
		Statement stmt = null;

		//compilation unit
		if (token.symbol() != TokenClass.OBJECT)
			ErrorMessage.print(lexer.position(), "object EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.ID)
			ErrorMessage.print(lexer.position(), "ID EXPECTED");
		getToken ();
		if (token.symbol() != TokenClass.LEFTBRACE)
			ErrorMessage.print(lexer.position(), "{ EXPECTED");
		getToken();
		maindef = false;
		while ((token.symbol() == TokenClass.DEF 
				|| token.symbol() == TokenClass.VAR) && !maindef) {

			if (token.symbol() == TokenClass.DEF) {
				getToken();
				if (token.symbol() == TokenClass.ID) {
					funcID = token.lexeme();
					getToken();
					stmt = funcDef(funcID);
					System.out.println("Abstract Syntax Tree for " + funcID);
					System.out.println("---------------------------------");
					System.out.println();
					System.out.println(stmt);
					stmt = null;
				}
				else if (token.symbol() == TokenClass.MAIN) {
					getToken();
					stmt = mainDef();
					maindef = true;
				}
				else
					ErrorMessage.print(lexer.position(), "ID EXPECTED");
			}
			else
				varDef();
		}

		//end main and compilation unit
		if (!maindef)
			ErrorMessage.print(lexer.position(), "main EXPECTED");
		if (token.symbol() != TokenClass.RIGHTBRACE)
			ErrorMessage.print(lexer.position(), "} EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.EOF)
			ErrorMessage.print(lexer.position(), "EOF EXPECTED");
		return stmt;
	}

	public Statement mainDef () throws java.io.IOException {
		Statement stmt1, stmt2;
		if (token.symbol() != TokenClass.LEFTPAREN)
			ErrorMessage.print(lexer.position(), "( EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.ARGS)
			ErrorMessage.print(lexer.position(), "args EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.COLON)
			ErrorMessage.print(lexer.position(), ": EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.ARRAY)
			ErrorMessage.print(lexer.position(), "Array EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.LEFTBRACKET)
			ErrorMessage.print(lexer.position(), "[ EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.STRING)
			ErrorMessage.print(lexer.position(), "String EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.RIGHTBRACKET)
			ErrorMessage.print(lexer.position(), "] EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.RIGHTPAREN)
			ErrorMessage.print(lexer.position(), ") EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.LEFTBRACE)
			ErrorMessage.print(lexer.position(), "{ EXPECTED");
		getToken();
		while (token.symbol() == TokenClass.VAR)
			varDef();
		stmt1 = statement();
		while (token.symbol() != TokenClass.RIGHTBRACE){
			if(stmt1 == null) 
				stmt1 = statement();
			else {
				stmt2 = statement();
				if (stmt2 != null)
					stmt1 = new Statement(stmt1, stmt2);
			}
		}
		getToken();
		return stmt1;
	}

	public Statement funcDef (String funcID) throws java.io.IOException {

		Statement stmt = null, stmt1 = null, stmt2 = null;
		Expression exp;

		if (token.symbol() != TokenClass.LEFTPAREN)
			ErrorMessage.print(lexer.position(), "( EXPECTED");
		
		do {
			getToken();
			if(token.symbol() == TokenClass.ID) {
				getToken();
				if (token.symbol() != TokenClass.COLON)
					ErrorMessage . print(lexer.position(), ": EXPECTED");
				getToken();
				type();
			}
		} while(token.symbol() == TokenClass.COMMA);

		if (token . symbol() != TokenClass . RIGHTPAREN)
			ErrorMessage . print (lexer . position (), ") EXPECTED");
		getToken();
		if (token . symbol() != TokenClass . COLON)
			ErrorMessage . print (lexer . position (), ": EXPECTED");
		getToken();
		type();
		if (token . symbol() != TokenClass . ASSIGN)
			ErrorMessage . print (lexer . position (), "= EXPECTED");
		getToken();
		if (token . symbol() != TokenClass . LEFTBRACE)
			ErrorMessage . print (lexer . position (), "{ EXPECTED");
		getToken();
		while (token.symbol() == TokenClass.VAR)
			varDef();
		while (token.symbol() != TokenClass.RETURN) {
			if(stmt1 == null) 
				stmt1 = statement();
			else {
				stmt2 = statement();
				if (stmt2 != null)
					stmt1 = new Statement(stmt1, stmt2);
			}
		}
		getToken();
		exp = listExpr();
		ReturnStmt retS = new ReturnStmt(exp);
		stmt = new Statement(stmt1, retS);
		if (token . symbol() != TokenClass . SEMICOLON)
			ErrorMessage . print (lexer . position (), "; EXPECTED");
		getToken();
		if (token . symbol() != TokenClass . RIGHTBRACE)
			ErrorMessage . print (lexer . position (), "} EXPECTED");
		getToken();
		return stmt;
	}

	public void varDef () throws java.io.IOException {
		if (token.symbol() != TokenClass.VAR)
			ErrorMessage.print(lexer.position(), "var EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.ID)
			ErrorMessage.print(lexer.position(), "id EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.COLON)
			ErrorMessage.print(lexer.position(), ": EXPECTED");
		getToken();
		type();
		if (token.symbol() != TokenClass.ASSIGN)
			ErrorMessage.print(lexer.position(), "= EXPECTED");
		getToken();
		literal();
		if (token.symbol() != TokenClass.SEMICOLON)
			ErrorMessage.print(lexer.position(), "; EXPECTED");
		getToken();
	}

	public void type () throws java.io.IOException {

		if (token.symbol() == TokenClass.INT)
			getToken();
		else if(token.symbol() == TokenClass.LIST) {
			getToken();
			if (token . symbol () != TokenClass . LEFTBRACKET)
				ErrorMessage . print (lexer . position(), "[ EXPECTED");
			getToken();
			if (token . symbol () != TokenClass . INT)
				ErrorMessage . print (lexer . position(), "Int EXPECTED");
			getToken();
			if (token . symbol () != TokenClass . RIGHTBRACKET)
				ErrorMessage . print (lexer . position(), "] EXPECTED");
			getToken();
		}
		else
			ErrorMessage.print(lexer.position(), "TYPE EXPECTED");
	}

	public Statement statement () throws java.io.IOException {
		Expression exp;
		Statement stmt = null, stmt1, stmt2 = null;
		String id;
		VariableRef var;

		switch (token . symbol ()) {

			case IF:
				getToken();
				if (token . symbol () != TokenClass . LEFTPAREN)
					ErrorMessage . print (lexer . position (), "( EXPECTED");
				getToken();
				exp = expr();
				if (token . symbol () != TokenClass . RIGHTPAREN)
					ErrorMessage . print (lexer . position (), ") EXPECTED");
				getToken();
				stmt1 = statement();
				if (token . symbol () == TokenClass . ELSE) {
					getToken();
					stmt2 = statement();
				}
				stmt = new Conditional (exp, stmt1, stmt2);
				break;

			case WHILE:
				getToken();
				if (token . symbol () != TokenClass . LEFTPAREN)
					ErrorMessage . print (lexer . position (), "( EXPECTED");
				getToken();
				exp = expr();
				if (token . symbol () != TokenClass . RIGHTPAREN)
					ErrorMessage . print (lexer . position (), ") EXPECTED");
				getToken();
				stmt1 = statement();
				stmt = new Loop(exp, stmt1);
				break;

			case ID:
				id = token.lexeme();
				getToken();
				var = new VariableRef(id);
				if (token . symbol () != TokenClass . ASSIGN)
					ErrorMessage . print (lexer . position (), "= EXPECTED");
				getToken();
				exp = listExpr();
				stmt = new Assignment(var, exp);
				if (token . symbol () != TokenClass . SEMICOLON)
					ErrorMessage . print (lexer . position (), "; EXPECTED");
				getToken();
				break;

			case PRINTLN:
				getToken();
				if (token . symbol () != TokenClass . LEFTPAREN)
					ErrorMessage . print (lexer . position (), "( EXPECTED");
				getToken();
				exp = listExpr();
				stmt = new Println(exp);
				if (token . symbol () != TokenClass . RIGHTPAREN)
					ErrorMessage . print (lexer . position (), ") EXPECTED");
				getToken();
				if (token . symbol () != TokenClass  . SEMICOLON)
					ErrorMessage . print (lexer . position (), "; EXPECTED");
				getToken();
				break;

			case LEFTBRACE:
				getToken();
				stmt1 = statement();
				while (token.symbol() != TokenClass.RIGHTBRACE) {
					if (stmt1 == null)
						stmt = statement();
					else {
						stmt2 = statement();
						if (stmt2 == null)
							stmt = stmt1;
						else
							stmt = new Statement(stmt1, stmt2);
					}
				}
				getToken();
				break;

			default:
				ErrorMessage . print (lexer . position(), "UNRECOGNIZABLE SYMBOL");
		}
		return stmt;
	}

	public Expression expr () throws java.io.IOException {
		Expression exp;
		exp = andExpr();
		while (token . symbol () == TokenClass . OR) {
			getToken();
			exp = new Oper("||", exp, andExpr());
		}
		return exp;
	}

	public Expression andExpr() throws java.io.IOException {
		Expression andExp;
		andExp = relExpr();
		while (token . symbol () == TokenClass . AND) {
			getToken();
			andExp = new Oper("&&", andExp, relExpr());
		}
		return andExp;
	}

	public Expression relExpr() throws java.io.IOException {
		Expression relExp;
		boolean n = false;
		String op;
		if (token . symbol () == TokenClass . NOT){
			getToken();
			n = true;
		}

		relExp = listExpr();
		if (token . symbol () == TokenClass . RELOP){
			op = token.lexeme();
			getToken();
			relExp = new Oper(op, relExp, listExpr());
		}
		if (n)
			return new NotExp(relExp);
		else
			return relExp;
	}

	public Expression listExpr() throws java.io.IOException {
		Expression cons;
		cons = addExpr();
		if (token . symbol () == TokenClass . CONS){
			getToken();
			cons = new Oper("::", cons, listExpr());
		}
		return cons;
	}

	public Expression addExpr() throws java.io.IOException {
		Expression addExp;
		String op;
		addExp = mulExpr();
		while (token . symbol () == TokenClass . ADDOP) {
			op = token.lexeme();
			getToken();
			addExp = new Oper(op, addExp, mulExpr());
		}
		return addExp;
	}

	public Expression mulExpr() throws java.io.IOException {
		Expression mulExp;
		String op;
		mulExp = prefExpr();
		while (token . symbol () == TokenClass . MULTOP) {
			op = token.lexeme();
			getToken();
			mulExp = new Oper(op, mulExp, prefExpr());
		}
		return mulExp;
	}

	public Expression prefExpr() throws java.io.IOException {
		Expression prefExp;
		String method;
		if (token . symbol () == TokenClass . ADDOP)
			getToken();
		prefExp = simpExpr();
		while (token . symbol () == TokenClass . PERIOD) {
			getToken();
			if (token . symbol() != TokenClass . LISTOP)
				ErrorMessage . print (lexer . position(), "LISTOP EXPECTED");
			method = token.lexeme();
			prefExp = new ListMethod(prefExp, method);
			getToken();
		}
		return prefExp;
	}

	public Expression simpExpr() throws java.io.IOException {
		Expression exp = null;
		String id;

		switch (token . symbol()) {

			case LEFTPAREN:
				getToken();
				exp = expr();
				if (token . symbol () != TokenClass . RIGHTPAREN)
					ErrorMessage . print (lexer . position (), ") EXPECTED");
				getToken();
				break;

			case ID:
				id = token.lexeme();
				exp = new VariableRef(id);
				getToken();
				if (token . symbol() == TokenClass . LEFTPAREN) {
					getToken();
					if (token.symbol() != TokenClass.RIGHTPAREN) {
						exp = listExpr();
						while (token . symbol() == TokenClass . COMMA) {
							getToken();
							exp = new Expression(exp, listExpr());
						}
						if (token . symbol() != TokenClass . RIGHTPAREN)
							ErrorMessage . print (lexer . position (), ") EXPECTED");
						exp = new FunCall(id, exp);
					}
					getToken();
				}
				break;

			default:
				exp = literal();
		}
		return exp;
	}

	public Expression literal() throws java.io.IOException {

		Expression val = null;

		if (token.symbol() == TokenClass.INTEGER){
			val = new IntValue(token.lexeme());
			getToken();
		}
		else if (token.symbol() == TokenClass.NIL){
			val = new Nil();
			getToken();
		}
		else
			ErrorMessage.print("LITERAL EXPECTED");

		return val;
	}
}




















