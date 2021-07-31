/**
 * TestFilter
 *
 * Test the include and exclude predicates.
 *
 * First, the facts:
 *
 * male(Sheldon).
 * male(Leonard).
 * male(Raj).
 * male(Howard).
 * female(Penny).
 * female(Bernadette).
 * female(Amy).
 * friends([Leonard, Raj, Penny, Sheldon, Bernadette, Amy, Howard]).
 *
 * The filter test:
 *
 * list_wimmin($W) :- friends($F), include(female, $F, $W).
 * list_nerds($N)  :- friends($F), exclude(female, $F, $N).
 * 
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestFilter {

   public static void main(String[] args) {

      KnowledgeBase kb = new KnowledgeBase(
         new Rule("male(Sheldon)"),
         new Rule("male(Leonard)"),
         new Rule("male(Raj)"),
         new Rule("male(Howard)"),
         new Rule("female(Penny)"),
         new Rule("female(Bernadette)"),
         new Rule("female(Amy)"),
         new Rule("friends([Leonard, Raj, Penny, Sheldon, Bernadette, Amy, Howard])."),
         new Rule("list_wimmin($W) :- friends($F), include(female, $F, $W)."),
         new Rule("list_nerds($N) :- friends($F), exclude(female, $F, $N).")
      );

      System.out.print("Test Include and Exclude predicates: ");

      try {
         Complex goal = new Complex("list_wimmin($W)");
         String[] expected = { "[Penny, Bernadette, Amy]" };
         Solutions.verifyAll(goal, kb, expected, 1);

         goal = new Complex("list_nerds($N)");
         String[] expected2 = { "[Leonard, Raj, Sheldon, Howard]" };
         Solutions.verifyAll(goal, kb, expected2, 1);
      } catch (TimeOverrunException tox) {}
   }

} // TestFilter