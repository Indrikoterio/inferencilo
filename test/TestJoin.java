/**
 * TestJoin
 *
 * Test the built-in function join().
 * This function joins constants, representing a list words
 * and punctuation, to form a single constant.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestJoin {

   public static void main(String[] args) {

      Variable D1 = new Variable("$D1");
      Variable D2 = new Variable("$D2");
      Variable D3 = new Variable("$D3");
      Variable D4 = new Variable("$D4");
      Variable D5 = new Variable("$D5");
      Variable Out = new Variable("$Out");

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("would_you_like($Out)"),
            new And(
                   new Unify(D1, new Constant("coffee")),
                   new Unify(D2, new Constant(",")),
                   new Unify(D3, new Constant("tea")),
                   new Unify(D4, new Constant("or")),
                   new Unify(D5, new Constant("juice")),
                   new Unify(Out, new Join(D1, D2, D3, D4, D5))
                )
         )
      );

      System.out.print("Test Join: ");

      try {
         Complex query = Make.query("would_you_like($X)");
         String[] expected = { "coffee, tea or juice" };
         Solutions.verifyAll(query, kb, expected, 1);
      } catch (TimeOverrunException tox) {}
   }

} // TestJoin
