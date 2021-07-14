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

      Variable D1 = Variable.inst("$D1");
      Variable D2 = Variable.inst("$D2");
      Variable D3 = Variable.inst("$D3");
      Variable D4 = Variable.inst("$D4");
      Variable D5 = Variable.inst("$D5");
      Variable Out = Variable.inst("$Out");

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
         Complex goal = new Complex("would_you_like($X)");
         String[] expected = { "coffee, tea or juice" };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) {}
   }

} // TestJoin
