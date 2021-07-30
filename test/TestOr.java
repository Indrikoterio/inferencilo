/**
 * TestOr
 *
 * Tests the Or operators of the inference engine.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestOr {

   public static void main(String[] args) {

      // Set up the knowledge base.
      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("grammar(Apollonius)")),
         new Rule(new Complex("logic(Aristoteles)")),
         new Rule(new Complex("rhetoric(Isocrates)")),
         new Rule(
            new Complex("teacher($X)"),
            new Or(
               new Complex("grammar($X)"),
               new Complex("logic($X)"),
               new Complex("rhetoric($X)")
            )
         )
      );

      System.out.print("Test Or: ");

      try {
         // Define goal and root of search space.
         Complex goal = new Complex("teacher($X)");
         String[] expected = {
            "teacher(Apollonius)",
            "teacher(Aristoteles)",
            "teacher(Isocrates)"
         };
         Solutions.verifyAll(goal, kb, expected, 0);
      } catch (TimeOverrunException tox) { }
   }

}  // TestOr
