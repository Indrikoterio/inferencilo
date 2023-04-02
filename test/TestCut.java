/**
 * TestCut
 *
 * Testing Cut predicate (!).
 *
 * handicapped(John).
 * handicapped(Mary).
 * has_small_children(Mary).
 * is_elderly(Diane)
 * is_elderly(John)
 * priority_seating($Name, $YN) :- handicapped($Name), $YN = Yes, !.
 * priority_seating($Name, $YN) :- has_small_children($Name), $YN = Yes, !.
 * priority_seating($Name, $YN) :- is_elderly($Name), $YN = Yes, !.
 * priority_seating($Name, No).
 *
 *----------------------------------------
 * Another cut test. In standard Prolog:
 *
   cut_rule :- !, print("Test Cut: This text should print."), fail.
   cut_rule :- print("*** This should NOT print. ***").
   cut_rule("Two").
   my_test("One") :- cut_rule.
   my_test(X) :- cut_rule(X).
 *
 * The query is ?- my_test(X)
 *
 * Result is:
   "Test Cut: This text should print."
   X = "Two"
 *
 * Note: cut_rule/0 and cut_rule/1 are completely different rules.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestCut {

   public static void main(String[] args) {    // Set up the knowledge base.

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("handicapped(John)")),
         new Rule(new Complex("handicapped(Mary)")),
         new Rule(new Complex("has_small_children(Mary)")),
         new Rule(new Complex("is_elderly(Diane)")),
         new Rule(new Complex("is_elderly(John)")),
         new Rule(
            new Complex("priority_seating($Name, $YN)"),
            new And(
               new Complex("handicapped($Name)"),
               new Unify("$YN = Yes"),
               new Cut()
            )
         ),
         new Rule(
            new Complex("priority_seating($Name, $YN)"),
            new And(
               new Complex("has_small_children($Name)"),
               new Unify("$YN = Yes"),
               new Cut()
            )
         ),
         new Rule(
            new Complex("priority_seating($Name, $YN)"),
            new And(
               new Complex("is_elderly($Name)"),
               new Unify("$YN = Yes"),
               new Cut()
            )
         ),
         new Rule(
            new Complex("priority_seating($Name, No)")
         ),

         new Rule("cut_rule :- !, print(Test Cut: This text should print.), fail."),
         new Rule("cut_rule :- print(*** This should NOT print. ***)."),
         new Rule("cut_rule(Two)."),
         new Rule("my_test(One) :- cut_rule()."),
         new Rule("my_test($X) :- cut_rule($X).")

      );

      // Define goal and root of search space.

      System.out.print("Test Cut: ");

      Complex query;
      try {
         query = Make.query("priority_seating(John, $X)");
         String[] expected = {"Yes"};
         Solutions.verifyAll(query, kb, expected, 2);

         query = Make.query("my_test($X)");
         String[] expected2 = { "Two" };
         Solutions.verifyAll(query, kb, expected2, 1);
      } catch (TimeOverrunException tox) { }

   }
}
