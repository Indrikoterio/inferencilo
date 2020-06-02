/**
 * TestUnify
 *
 * Test the unify predicate ($X = $Y).
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestUnify {

   public static void main(String[] args) {         // Set up the knowledge base.

      Constant lawyer = new Constant("lawyer");
      Constant up = new Constant("up");
      Constant down = new Constant("down");

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("job(lawyer)")),
         new Rule(new Complex("job(teacher)")),
         new Rule(new Complex("job(programmer)")),
         new Rule(new Complex("job(janitor)")),
         new Rule(
            new Complex("unify_goal($X, $Y, $Z)"),
            new And(
               new Unify(lawyer, lawyer),  // lawyer = lawyer
               new Unify(
                  new Complex("job(programmer, $Z)"),
                  new Complex("job($Y, janitor)")
               ),
               new Unify(Var.get("$W"), Var.get("$X")),
               new Complex("job($W)")
            )
         ),

         new Rule(
            new Complex("second_goal($Y)"),
            new And(
               new Unify(Var.get("$X"), up),
               new Unify(Var.get("$Y"), down),
               new Unify(Var.get("$X"), Var.get("$Y"))
            )
         )

      );

      System.out.print("Test Unify: ");

      /*
      Prolog equivalent of unify_goal rule:
          unify_goal(X, Y, Z) :- lawyer = lawyer,
                                 job(programmer, Z) = job(Y, janitor),
                                 W = X,
                                 job(W).

      Prolog equivalent of second_goal rule:
           second_goal(Y) :- X = up, Y = down, X = Y.
      This goal must fail.
      */

      try {
         Complex goal;
         goal = new Complex("unify_goal($X, $Y, $Z)");
         String[] expected = {"unify_goal(lawyer, programmer, janitor)",
                              "unify_goal(teacher, programmer, janitor)",
                              "unify_goal(programmer, programmer, janitor)",
                              "unify_goal(janitor, programmer, janitor)"};
         Solutions.verifyAll(goal, kb, expected, 0);
      } catch (TimeOverrunException tox) { }

      System.out.print("Test Unify failure: ");

      try {
         Complex goal;
         goal = new Complex("second_goal($Y)");
         String[] expected = {};
         Solutions.verifyAll(goal, kb, expected, 0);
      } catch (TimeOverrunException tox) { }


   }
}


