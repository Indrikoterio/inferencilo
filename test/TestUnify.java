/**
 * TestUnify
 *
 * Test the unify predicate ($X = $Y).
 *
 * @author  Cleve (Klivo) Lendon
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
            new Complex("unify_test($X, $Y, $Z)"),
            new And(
               new Unify(lawyer, lawyer),  // lawyer = lawyer
               new Unify(
                  new Complex("job(programmer, $Z)"),
                  new Complex("job($Y, janitor)")
               ),
               new Unify(new LogicVar("$W"), new LogicVar("$X")),
               new Complex("job($W)")
            )
         ),

         new Rule(
            new Complex("second_test($Y)"),
            new And(
               new Unify(new LogicVar("$X"), up),
               new Unify(new LogicVar("$Y"), down),
               new Unify(new LogicVar("$X"), new LogicVar("$Y"))
            )
         )

      );

      System.out.print("Test Unify success: ");

      /*
      Prolog equivalent of unify_test rule:
          unify_test(X, Y, Z) :- lawyer = lawyer,
                                 job(programmer, Z) = job(Y, janitor),
                                 W = X,
                                 job(W).

      Prolog equivalent of second_test rule:
           second_test(Y) :- X = up, Y = down, X = Y.
      This test must fail.
      */

      try {
         Complex query;
         // Must use Make.query() to create queries, to ensure
         // that variables are standardized.
         query = Make.query("unify_test($X, $Y, $Z)");
         String[] expected = {"unify_test(lawyer, programmer, janitor)",
                              "unify_test(teacher, programmer, janitor)",
                              "unify_test(programmer, programmer, janitor)",
                              "unify_test(janitor, programmer, janitor)"};
         Solutions.verifyAll(query, kb, expected, 0);
      } catch (TimeOverrunException tox) { }

      System.out.print("Test Unify failure: ");

      try {
         Complex query;
         query = Make.query("second_test($Y)");
         String[] expected = {};
         Solutions.verifyAll(query, kb, expected, 0);
      } catch (TimeOverrunException tox) { }

   }
} // TestUnify


