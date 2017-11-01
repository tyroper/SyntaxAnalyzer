public class MScalaAST {

  public static void main (String args []) throws java.io.IOException {

    System . out . println ("Source Program");
    System . out . println ("--------------");
    System . out . println ();

    ParserAST mScala = new ParserAST ();
    Program program = mScala . program ();

    System . out . println ();
    System.out.println("Abstract Syntax Tree Main");
    System.out.println("-------------------------");
    System.out.println();
    System.out.println(program);
  }

}
