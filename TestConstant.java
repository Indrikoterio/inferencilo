/**
 * TestConstant
 *
 * This class tests the Constant class.
 * Constant stores a string value; it is instantiated as: new Constant("red").
 *
 * @author  Klivo
 * @version 1.0
 */

import inferencilo.*;

public class TestConstant {

   /**
    * main
    *
    */
   public static void main(String[] args) {

      // Set up the knowledge base.
      Variable X   = new Variable("$X");
      Variable Out = new Variable("$Out");

      Constant test_constant = new Constant("test_constant");
      Constant red = new Constant("red");
      Constant one = new Constant("1");
      Constant two = new Constant("2");
      Constant oneFloat = new Constant("1.0000");

      /*
        Prolog equivalent:
                test_constant(Out) :- Out = red.
                test_constant(Out) :- Out = 1.0, 1 = 1.0.
                test_constant(ok) :- 1 = 1.0.
       */

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(test_constant, Out), new And(new Unify(Out, new Constant("red")))),
         // The next rule has no solution.
         new Rule(new Complex(test_constant, new Constant("bad")), new And(new Unify(one, two))),
         new Rule(new Complex(test_constant, new Constant("ok")), new And(new Unify(one, oneFloat)))
      );

      try {
         System.out.print("Test Constant: ");
         Complex goal = new Complex(test_constant, X);
         String[] expected = {"red", "ok"};
         Solutions.verifyAll(goal, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }
   }
}  // TestConstant


