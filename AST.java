abstract class Program { }

class Statement extends Program {

	protected Statement stmt1, stmt2;

	public Statement() { }

	public Statement(Statement stmt1, Statement stmt2) {
		this.stmt1 = stmt1;
		this.stmt2 = stmt2;
	}

	public String toString() {
		if (stmt1 == null)
			return "()";
		else if (stmt2 == null)
			return "(" + stmt1 + ")";
		else
			return "(: " + stmt1 + " " +stmt2 + ")";
	}
}

class Assignment extends Statement { 

	protected VariableRef lhs;
	protected Expression rhs;

	public Assignment() { }

	public Assignment(VariableRef lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public String toString() {
		return "(= " + lhs + " " + rhs + ")";
	}
}

class Conditional extends Statement {

	protected Expression test;
	protected Statement elseStmt, thenStmt;

	public Conditional() { }

	public Conditional(Expression test, Statement thenStmt, Statement elseStmt) {
		this.test = test;
		this.thenStmt = thenStmt;
		this.elseStmt = elseStmt;
	}

	public String toString() {
		return "(if " + test + " " + thenStmt + " " + elseStmt + ")";
	}
}

class Println extends Statement {
	protected Expression exp;

	public Println() { }

	public Println(Expression exp) {
		this.exp = exp;
	} 

	public String toString() {
		return "(println " + exp + ")";
	}
}

class Loop extends Statement { 

	protected Expression test;
	protected Statement body;

	public Loop() { }

	public Loop (Expression test, Statement body) {
		this.test = test;
		this.body = body;
	}

	public String toString() {
		return "(while " + test + " " + body + ")";
	}
}

class ReturnStmt extends Statement {
	protected Expression retExp;

	public ReturnStmt() { }

	public ReturnStmt(Expression retExp) {
		this.retExp = retExp;
	}

	public String toString() {
		return "(return " + retExp + ")";
	}
}

abstract class Expressions { }

class Expression extends Expressions {

	protected Expression exp1, exp2;

	public Expression () { }

	public Expression(Expression exp1, Expression exp2) {
		this.exp1 = exp1;
		this.exp2 = exp2;
	}

	public String toString() {
		if (exp1 == null)
			return "";
		else if (exp2 == null)
			return "" + exp1 + "";
		else
			return  "" + exp1 + ", " + exp2 + "";
	}
}

class VariableRef extends Expression {

	protected String id;

	public VariableRef() { }

	public VariableRef(String id) {
		this.id = id;
	}

	public String toString() {
		return "(id " + id + ")";
	}
}

class ListMethod extends Expression {

	protected Expression id;
	protected String method;

	public ListMethod() { }

	public ListMethod(Expression id, String method) {
		this.id = id;
		this.method = method;
	}

	public String toString () {
		return "(" + method + " " + id + ")";
	}
}


abstract class Value extends Expression { }

class IntValue extends Value {

	protected String intValue;

	public IntValue() { }

	public IntValue(String intValue) {
		this.intValue = intValue;
	}

	public String toString() {
		return "(intValue " + intValue + ")";
	}
}

class Nil extends Value {
	protected String n;

	public Nil() { }

	public String toString () {
		return "Nil";
	}
}

class Oper extends Expression {

	protected String op;
	protected Expression term1, term2;

	public Oper() { }

	public Oper(String op, Expression term1, Expression term2) {
		this.op = op;
		this.term1 = term1;
		this.term2 = term2;
	}

	public String toString() {
		return "(" + op + " " + term1 + " " + term2 + ")";
	}
}

class NotExp extends Expression {

	protected Expression term;

	public NotExp() { } 

	public NotExp(Expression term) {
		this.term = term;
	}

	public String toString () {
		return "(! " + term + ")";
	}
}

class FunCall extends Expression {

	protected String id;
	protected Expression exp;

	public FunCall() { }

	public FunCall(String id, Expression exp) {
		this.id = id;
		this.exp = exp;
	}

	public String toString() {
		return "(apply " + id + "[" + exp + "])";
	}
}

