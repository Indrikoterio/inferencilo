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
 * Another cut test.
 *
 * cut_rule :- !, print("Test Cut: This text should print."), fail.
 * cut_rule :- print("*** This should NOT print. ***").
 * cut_rule("Two").
 * my_test("One") :- cut_rule.
 * my_test($X) :- cut_rule($X).
 *
 * The query is ?- my_test($X).
 *
 * The above prints out: "Test Cut: This text should print."
 * It also returns an answer: $X = "Two"
 *
 * Note: cut_rule/0 and cut_rule/1 are completely different rules.
 *
 * Another test.
 *
 * get_value($X) :- $X = 1.
 * get_value($X) :- $X = 2.
 * another_test($X) :- get_value($X), !, $X == 2.
 *
 * When the inference engine is queried with 'another_test(X)',
 * it should returns no solutions.
 *
 * @author  Klivo
 * @version 1.1
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
         new Rule("my_test($X) :- cut_rule($X)."),

         // Another important test.
         new Rule("get_value($X) :- $X = 1."),
         new Rule("get_value($X) :- $X = 2."),
         new Rule("another_test($X) :- get_value($X), !, $X == 2.")

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

         ArrayList<String> solutions = new ArrayList<String>();
         query = Make.query("another_test($X)");
         SolutionNode root = query.getSolver(kb, new SubstitutionSet(), null);
         SubstitutionSet solution = root.nextSolution();
         while (solution != null) {
            Complex result = (Complex)query.replaceVariables(solution);
            solutions.add(result.toString());
            solution = root.nextSolution();
         }

         int count = solutions.size();
         if (count == 0) { System.out.println("Test Cut: ✓"); }
         else {
            System.out.println("Test Cut: ERROR! Expected 0 results. Got " +
                                count + ".");
         }

      } catch (TimeOverrunException tox) { }

   }
} // TestCut
