/**
 * TestConstant
 *
 * This class tests the Constant class.
 * The Constant class stores a string value, which may represent a number.
 * It is instantiated as:
 *
 *    new Constant("red")
 * or
 *    Constant.inst("red")
 *
 * The second method is more efficient, because it stores the newly created
 * Constant in a hash table. Unification can be tested with a simple comparison
 * (con1 == con2), instead of comparing strings.
 *
 * @author  Cleve (Klivo) Lendon
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

      Constant test_constant    = new Constant("test_constant");
      Constant test_constant_bt = new Constant("test_constant_bt");
      Constant red = new Constant("red");
      Constant one = new Constant("1");
      Constant two = new Constant("2");
      Constant oneFloat = new Constant("1.0000");

      /*
        Prolog equivalent:
                test_constant(Out) :- Out = red.
                test_constant(bad) :- 1 = 2.
                test_constant(ok) :- 1 = 1.0000.
       */

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(test_constant, Out), new And(new Unify(Out, new Constant("red")))),
         // The next rule has no solution.
         new Rule(new Complex(test_constant, new Constant("bad")), new And(new Unify(one, two))),
         new Rule(new Complex(test_constant, new Constant("ok")), new And(new Unify(one, oneFloat))),
         new Rule("test_constant_bt($X) :- $X = `Let's see, if this works.`.")
      );

      try {
         System.out.print("Test Constant: ");
         // Must use Make.query() to create goals,
         // even though goals are Complex terms.
         Complex query = Make.query(test_constant, X);
         String[] expected = {"red", "ok"};
         Solutions.verifyAll(query, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }

      try {
         System.out.print("Test Constant (backticks): ");
         Complex query = Make.query(test_constant_bt, X);
         String[] expected = {"Let's see, if this works."};
         Solutions.verifyAll(query, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }

      // Test Constant cache:
      Constant c1 = Constant.inst("pronoun");
      Constant c2 = Constant.inst("pronoun");
      if (c1 == c2) System.out.println("Constant Cache is OK. ✓");
      else System.out.println("Constant Cache NOT OK!");

   }
}  // TestConstant
