/**
 * TestCount
 *
 * Test the built-in predicate count().
 * This predicate returns the count of items in a list.
 *
 * For example:
 *
 *   test_count($Count) :- count([], $Count).          # $C is 0
 *   test_count($Count) :- count([a, b, c], $Count).   # $C is 3
 *   test_count($Count) :- count([a | $_], $Count).    # $C is 2
 *
 * Note: In this inference engine, numbers are Constant-s.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestCount {

   public static void main(String[] args) {

      KnowledgeBase kb;

      Constant red = new Constant("red");
      Constant green = new Constant("green");
      Constant blue = new Constant("blue");

      PList p1 = new PList(false, green, blue);
      PList p2 = new PList(false, red, p1);

      Variable c = Variable.instance("$Count");

      kb = new KnowledgeBase(
         new Rule(new Complex("test_count($Count)"),
            new And( new Count("[], $Count") )
         ),
         new Rule(new Complex("test_count($Count)"),
            new And( new Count("[a, b, c], $Count") )
         ),
         new Rule(new Complex("test_count($Count)"),
            new And( new Count("[a | $_], $Count") )
         ),
         new Rule(new Complex("test_count($Count)"),
            new And( new Count(p2, c) )
         )
      );

      System.out.print("Test Count: ");

      try {
         Complex goal = new Complex("test_count($X)");
         String[] expected = { "0", "3", "2", "3" };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) {}

   }

} // TestCount
