/**
 * TestAndOr
 *
 * Tests the And and Or operators of the inference engine.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestAndOr {

   public static void main(String[] args) {

      // Set up the knowledge base.
      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("father(George, Frank)")),
         new Rule(new Complex("father(George, Sam)")),
         new Rule(new Complex("mother(Gina, Frank)")),
         new Rule(new Complex("mother(Gina, Sam)")),
         new Rule(new Complex("mother(Maria, Marcus)")),
         new Rule(new Complex("father(Frank, Marcus)")),
         new Rule(
            new Complex("parent($X, $Y)"),
            new Or(
               new Complex("father($X, $Y)"),
               new Complex("mother($X, $Y)")
            )
         ),
         new Rule(
            new Complex("relative($X, $Y)"),
            new Or(
               new Complex("grandfather($X, $Y)"),
               new Complex("father($X, $Y)"),
               new Complex("grandmother($X, $Y)"),  // Is not a rule.
               new Complex("mother($X, $Y)")
            )
         ),
         new Rule(
            new Complex("grandfather($X, $Y)"),
            new And(new Complex("father($X, $Z)"), new Complex("parent($Z, $Y)"))
         )
      );

      System.out.print("Test And/Or: ");

      try {
         // Define goal and root of search space.
         // Must use Make.query() to create queries.
         Complex query = Make.query("relative($X, Marcus)");
         String[] expected = {
            "relative(George, Marcus)",
            "relative(Frank, Marcus)",
            "relative(Maria, Marcus)"
         };
         Solutions.verifyAll(query, kb, expected, 0);
      } catch (TimeOverrunException tox) { }
   }

}  // TestAndOr
