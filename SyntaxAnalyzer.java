public class SyntaxAnalyzer {

	protected MicroScalaLexer lexer;	// lexical analyzer
	protected Token token;	// current token

	public SyntaxAnalyzer() throws java.io.IOException {
		lexer = new MicroScalaLexer (System.in);
		getToken();
	}

	private void getToken() throws java.io.IOException {
		token = lexer.nextToken();
	}

	public void program() throws java.io.IOException {
		if (token.symbol() != TokenClass.OBJECT)
			ErrorMessage.print(lexer.position(), "object EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.ID)
			ErrorMessage.print(lexer.position(), "ID EXPECTED");
		getToken ();
		if (token.symbol() != TokenClass.LEFTBRACE)
			ErrorMessage.print(lexer.position(), "{ EXPECTED");
		getToken ();
		definitions();
		if (token.symbol() != TokenClass.MAIN)
			ErrorMessage.print(lexer.position(), "main EXPECTED");
		getToken();
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
			ErrorMessage.print(lexer.position(), "{ EXPECCTED");
		getToken();
		varDef();
		statements();
		if (token.symbol() != TokenClass.RIGHTBRACE)
			ErrorMessage.print(lexer.position(), "} EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.RIGHTBRACE)
			ErrorMessage.print(lexer.position(), "} EXPECTED");
		getToken();
		if (token.symbol() != TokenClass.EOF)
			ErrorMessage.print(lexer.position(), "END OF PROGRAM EXPECTED");
	}

	public void definitions () throws java.io.IOException {
		while (token . symbol () == TokenClass . DEF
		  || token . symbol () == TokenClass . VAR) {
			definition ();
		}
	}

	public void definition () throws java.io.IOException {
		switch (token . symbol ()) {
			case DEF:
				getToken();
				if (token . symbol () == TokenClass . ID){
					getToken();
					if (token . symbol() != TokenClass . LEFTPAREN)
						ErrorMessage . print (lexer . position (), "( EXPECTED");
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
					if (token . symbol() == TokenClass . VAR)
						varDef();
					statements();
					if (token . symbol() != TokenClass . RETURN)
						ErrorMessage . print (lexer . position (), "return EXPECTED");
					getToken();
					listExpr();
					if (token . symbol() != TokenClass . SEMICOLON)
						ErrorMessage . print (lexer . position (), "; EXPECTED");
					getToken();
					if (token . symbol() != TokenClass . RIGHTBRACE)
						ErrorMessage . print (lexer . position (), "} EXPECTED");
					getToken();
				}
				break;

			case VAR:
				varDef();
				break;

			default:
				ErrorMessage . print (lexer . position (), "UNRECOGNIZED TOKEN");
		}
	}

	public void varDef () throws java.io.IOException {

		while (token. symbol () == TokenClass.VAR) {
			getToken();
			if (token . symbol () != TokenClass . ID)
				ErrorMessage . print (lexer . position (), "ID EXPECTED");
			getToken();
			if (token . symbol () != TokenClass . COLON)
				ErrorMessage . print (lexer . position (), ": EXPECTED");
			getToken();
			type();
			if (token . symbol () != TokenClass . ASSIGN)
				ErrorMessage . print (lexer . position (), "= EXPECTED");
			getToken();
			if (token.symbol () != TokenClass . INTEGER 
					&& token.symbol () != TokenClass. NIL)
				ErrorMessage . print (lexer . position (), "LITERAL EXPECTED");
			getToken();
			if (token . symbol () != TokenClass . SEMICOLON)
				ErrorMessage . print (lexer . position (), "; EXPECTED");
			getToken();
		}
	}

	public void type () throws java.io.IOException {

		switch (token . symbol()) {

			case INT:
				getToken();
				break;

			case LIST:
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
				break;

			default:
				ErrorMessage . print (lexer . position(), "Int EXPECTED");
		}
	}

	public void statements () throws java.io.IOException {

		while (token . symbol () == TokenClass . IF
					|| token . symbol () == TokenClass . WHILE
					|| token . symbol () == TokenClass . ID
					|| token . symbol () == TokenClass . PRINTLN
					|| token . symbol () == TokenClass . LEFTBRACE) {

			statement();
		}
	}

	public void statement () throws java.io.IOException {

		switch (token . symbol ()) {

			case IF:
				getToken();
				if (token . symbol () != TokenClass . LEFTPAREN)
					ErrorMessage . print (lexer . position (), "( EXPECTED");
				getToken();
				expr();
				if (token . symbol () != TokenClass . RIGHTPAREN)
					ErrorMessage . print (lexer . position (), ") EXPECTED");
				getToken();
				statement();
				if (token . symbol () == TokenClass . ELSE) {
					getToken();
					statement();
				}
				break;

			case WHILE:
				getToken();
				if (token . symbol () != TokenClass . LEFTPAREN)
					ErrorMessage . print (lexer . position (), "( EXPECTED");
				getToken();
				expr();
				if (token . symbol () != TokenClass . RIGHTPAREN)
					ErrorMessage . print (lexer . position (), ") EXPECTED");
				getToken();
				statement();
				break;

			case ID:
				getToken();
				if (token . symbol () != TokenClass . ASSIGN)
					ErrorMessage . print (lexer . position (), "= EXPECTED");
				getToken();
				listExpr();
				if (token . symbol () != TokenClass . SEMICOLON)
					ErrorMessage . print (lexer . position (), "; EXPECTED");
				getToken();
				break;

			case PRINTLN:
				getToken();
				if (token . symbol () != TokenClass . LEFTPAREN)
					ErrorMessage . print (lexer . position (), "( EXPECTED");
				getToken();
				listExpr();
				if (token . symbol () != TokenClass . RIGHTPAREN)
					ErrorMessage . print (lexer . position (), ") EXPECTED");
				getToken();
				if (token . symbol () != TokenClass  . SEMICOLON)
					ErrorMessage . print (lexer . position (), "; EXPECTED");
				getToken();
				break;

			case LEFTBRACE:
				getToken();
				do
					statement();
				while (token . symbol () != TokenClass . RIGHTBRACE);
				getToken();
				break;

			default:
				ErrorMessage . print (lexer . position(), "UNRECOGNIZABLE SYMBOL");
		}
	}

	public void expr () throws java.io.IOException {

		andExpr();
		while (token . symbol () == TokenClass . OR) {
			getToken();
			andExpr();
		}
	}

	public void andExpr() throws java.io.IOException {

		relExpr();
		while (token . symbol () == TokenClass . AND) {
			getToken();
			relExpr();
		}
	}

	public void relExpr() throws java.io.IOException {


		if (token . symbol () == TokenClass . NOT)
			getToken();
		listExpr();
		if (token . symbol () == TokenClass . RELOP){
			getToken();
			listExpr();
		}
	}

	public void listExpr() throws java.io.IOException {

		addExpr();
		if (token . symbol () == TokenClass . CONS){
			getToken();
			listExpr();
		}
	}

	public void addExpr() throws java.io.IOException {

		mulExpr();
		while (token . symbol () == TokenClass . ADDOP) {
			getToken();
			mulExpr();
		}
	}

	public void mulExpr() throws java.io.IOException {

		prefExpr();
		while (token . symbol () == TokenClass . MULTOP) {
			getToken();
			prefExpr();
		}
	}

	public void prefExpr() throws java.io.IOException {

		if (token . symbol () == TokenClass . ADDOP)
			getToken();
		simpExpr();
		while (token . symbol () == TokenClass . PERIOD) {
			getToken();
			if (token . symbol() != TokenClass . LISTOP)
				ErrorMessage . print (lexer . position(), "LISTOP EXPECTED");
			getToken();
		}
	}

	public void simpExpr() throws java.io.IOException {

		switch (token . symbol()) {

			case INTEGER:
			case NIL:
				getToken();
				break;

			case LEFTPAREN:
				getToken();
				expr();
				if (token . symbol () != TokenClass . RIGHTPAREN)
					ErrorMessage . print (lexer . position (), ") EXPECTED");
				getToken();
				break;

			case ID:
				getToken();
				if (token . symbol() == TokenClass . LEFTPAREN) {
					getToken();
					listExpr();
					while (token . symbol() == TokenClass . COMMA) {
						getToken();
						listExpr();
					}
					if (token . symbol() != TokenClass . RIGHTPAREN)
						ErrorMessage . print (lexer . position (), ") EXPECTED");
					getToken();
				}
				break;

			default:
				ErrorMessage . print (lexer . position (), "UNRECOGNIZABLE SYMBOL");
		}
	}
}

