/**
 * TestJoin - Test the built-in function join().
 *
 * This function joins constants, representing a list words
 * and punctuation, to form a single constant.
 *
 * In the following source example,
 *
 *   $D1 = coffee, $D2 = "," , $D3 = tea,
 *   $D4 = or, $D5 = juice, $D6 = ?,
 *   $X = join($D1, $D2, $D3, $D4, $D5, $D6).
 *
 * $X is bound to the Atom "coffee, tea or juice?".
 *
 * A built-in function is different from a built-in predicate, in
 * that a built-in function returns a value which must be unified
 * with something in order to be useful. All the arguments of
 * a function must be constants or grounded variables. If not,
 * the function fails.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.1
 */

import inferencilo.*;

public class TestJoin {

   public static void main(String[] args) {

      LogicVar D1 = new LogicVar("$D1");
      LogicVar D2 = new LogicVar("$D2");
      LogicVar D3 = new LogicVar("$D3");
      LogicVar D4 = new LogicVar("$D4");
      LogicVar D5 = new LogicVar("$D5");
      LogicVar D6 = new LogicVar("$D6");
      LogicVar Out = new LogicVar("$Out");

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("would_you_like($Out)"),
            new And(
                   new Unify(D1, new Constant("coffee")),
                   new Unify(D2, new Constant(",")),
                   new Unify(D3, new Constant("tea")),
                   new Unify(D4, new Constant("or")),
                   new Unify(D5, new Constant("juice")),
                   new Unify(D6, new Constant("?")),
                   new Unify(Out, new Join(D1, D2, D3, D4, D5, D6))
                )
         )
      );

      System.out.print("Test Join: ");

      try {
         Complex query = Make.query("would_you_like($X)");
         String[] expected = { "coffee, tea or juice?" };
         Solutions.verifyAll(query, kb, expected, 1);
      } catch (TimeOverrunException tox) {}

      //--------------------------------------------------
      // New test. Let the parser create a join function.

      kb = new KnowledgeBase();
      String str = "would_you_like($Out) :- $D1 = coffee, $D2 = \\,, " +
                   "$D3 = tea, $D4 = or, $D5 = juice, $D6 = ?, " +
                   "$Out = join($D1, $D2, $D3, $D4, $D5, $D6).";

      Rule rule = new Rule(str);
      kb.addRule(rule);

      try {
         Complex query = Make.query("would_you_like($X)");
         String[] expected = { "coffee, tea or juice?" };
         Solutions.verifyAll(query, kb, expected, 1);
      } catch (TimeOverrunException tox) {}

   } // main

} // TestJoin
