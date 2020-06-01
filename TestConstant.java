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

      /* Prolog equivalent: test_constant(Out) :- Out = red. */

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(test_constant, Out), new And( new Unify(Out, new Constant("red"))))
      );

      try {
         System.out.print("Test Constant: ");
         Complex goal = new Complex(test_constant, X);
         String[] expected = {"red"};
         Solutions.verifyAll(goal, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }
   }
}  // TestConstant


