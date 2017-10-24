public class MicroScalaSyn {

  public static void main (String args []) throws java.io.IOException {

    System . out . println ("Source Program");
    System . out . println ("--------------");
    System . out . println ();

    SyntaxAnalyzer mScala = new SyntaxAnalyzer ();
    mScala . program ();

    System . out . println ();
    System . out . println ("PARSE SUCCESSFUL");
  }

}
