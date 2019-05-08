/**
 * TestUnify
 *
 * Test the Prolog unify predicate ($X = $Y).
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestUnify {

   public static void main(String[] args) {         // Set up the knowledge base.

      Var.set("$Y");
      Constant lawyer = new Constant("lawyer");

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("job(lawyer)")),
         new Rule(new Complex("job(teacher)")),
         new Rule(new Complex("job(programmer)")),
         new Rule(new Complex("job(janitor)")),
         new Rule(new Complex("the_list(lawyer, teacher, programmer)")),
         new Rule(
            new Complex("unify_goal($X, $W, $Z)"),
            new And(
               new Unify(lawyer, lawyer),  // lawyer = lawyer
               new Unify(
                  new Complex("job(programmer, $Z)"),
                  new Complex("job($W, janitor)")
               ),
               new Unify(Var.get("$Y"), Var.get("$X")),
               new Complex("job($Y)")
            )
         )
      );


      System.out.print("Test Unify: ");

      try {
         Complex goal;
         goal = new Complex("unify_goal($X, $W, $Z)");
         String[] expected = {"unify_goal(lawyer, programmer, janitor)",
                                    "unify_goal(teacher, programmer, janitor)",
                                    "unify_goal(programmer, programmer, janitor)",
                                    "unify_goal(janitor, programmer, janitor)"};
         Solutions.verifyAll(goal, kb, expected, 0);
      } catch (TimeOverrunException tox) { }

   }
}


