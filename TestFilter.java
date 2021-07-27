/**
 * TestFilter
 *
 * Test filter predicate. First, the facts:
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
 * list_wimmin($W) :- friends($F), filter($F, female, $W).")
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
         new Rule("list_wimmin($W) :- friends($F), filter($F, female, $W).")
      );

      System.out.print("Test Filter function: ");

      try {
         Complex goal = new Complex("list_wimmin($F)");
         String[] expected = { "[Penny, Bernadette, Amy]" };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) {}
   }

} // TestFilter
