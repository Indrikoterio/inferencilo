/**
 * TestCut
 *
 * Testing Prolog's Cut (!).
 *
 * good_job(X) :- job(X), !, pay(X, high).
 * good_job(programmer).
 *
 * getta_good_job(X) :- good_job(X).
 *
 * job(lawyer).
 * job(teacher).
 * job(programmer).
 * job(janitor).
 *
 * pay(lawyer, high).
 * pay(teacher, high).
 * pay(programmer, low).
 * pay(janitor, low).
 *
 *----------------------------------------
 * Another test.
 *
   cut_rule :- !, print("Print this."), fail.
   cut_rule :- print("This should not print."), nl.
   cut_rule("Two").
   my_goal("One") :- cut_rule.
   my_goal(X) :- cut_rule(X).
 *
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestCut {

   public static void main(String[] args) {    // Set up the knowledge base.

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("job(lawyer)")),
         new Rule(new Complex("job(teacher)")),
         new Rule(new Complex("job(programmer)")),
         new Rule(new Complex("job(janitor)")),
         new Rule(new Complex("pay(lawyer, high)")),
         new Rule(new Complex("pay(teacher, high)")),
         new Rule(new Complex("pay(programmer, low)")),
         new Rule(new Complex("pay(janitor, low)")),
         new Rule(
            new Complex("good_job($X)"),
            new And(
               new Cut(),
               new Complex("job($X)"),
               new Complex("pay($X, high)")
            )
         ),
         new Rule(
            new Complex("good_job(programmer)")
         ),
         new Rule(
            new Complex("getta_good_job($X)"),
            new Complex("good_job($X)")
         ),
         new Rule("cut_rule :- !, print(Print this.\\n), fail."),
         new Rule("cut_rule :- print(This should not print.\\n)."),
         new Rule("cut_rule(Two)."),
         new Rule("my_goal(One) :- cut_rule()."),
         new Rule("my_goal($X) :- cut_rule($X).")
      );

      // Define goal and root of search space.

      System.out.print("Test Cut: ");

      try {
         Complex goal = new Complex("getta_good_job($X)");
         String[] expected = {"lawyer"};
         Solutions.verifyAll(goal, kb, expected, 1);

         goal = new Complex("my_goal($X)");
         String[] expected2 = { "One", "Two" };
         Solutions.verifyAll(goal, kb, expected2, 1);
      } catch (TimeOverrunException tox) { }

   }
}


