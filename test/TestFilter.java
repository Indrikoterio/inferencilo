/**
 * TestFilter
 *
 * Test the include() and exclude() predicates. Eg.
 *
 * $People = [male(Sheldon), male(Leonard), male(Raj), male(Howard),
 *            female(Penny), female(Bernadette), female(Amy)]
 * list_wimmin($W) :- include(female($_), $People, $W).
 * list_nerds($N)  :- exclude(female($_), $People, $N).
 * 
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestFilter {

   public static void main(String[] args) {

      Variable W = new Variable("$W");
      Variable N = new Variable("$N");

      Complex c1 = new Complex("male(Sheldon)");
      Complex c2 = new Complex("male(Leonard)");
      Complex c3 = new Complex("male(Raj)");
      Complex c4 = new Complex("male(Howard)");
      Complex c5 = new Complex("female(Penny)");
      Complex c6 = new Complex("female(Bernadette)");
      Complex c7 = new Complex("female(Amy)");
      PList people = new PList(false, c1, c2, c3, c4, c5, c6, c7);

      Complex list_wimmin = new Complex("list_wimmin($W)");
      Complex list_nerds  = new Complex("list_nerds($N)");
      Complex filter      = new Complex("female($_)");

      Include inc = new Include(filter, people, W);
      Exclude ex  = new Exclude(filter, people, N);

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(list_wimmin, inc),
         new Rule(list_nerds, ex)
      );

      System.out.println("Test Include and Exclude predicates: ");

      try {
         Complex goal = Make.goal("list_wimmin($W)");
         String[] expected = { "[female(Penny), female(Bernadette), female(Amy)]" };
         Solutions.verifyAll(goal, kb, expected, 1);

         goal = Make.goal("list_nerds($N)");
         String[] expected2 = { "[male(Sheldon), male(Leonard), male(Raj), male(Howard)]" };
         Solutions.verifyAll(goal, kb, expected2, 1);
      } catch (TimeOverrunException tox) {}
   }

} // TestFilter
