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
 *   test_count($Count) :- count([red | [green, blue]], $Count).    # $C is 3
 *
 *   test_count($Count) :- $TailVar = [one, two, three],
 *                         $NewList = [red, green, blue | $TailVar],
 *                         count($NewList, $Count).    # $C is 6
 *
 *   test_count($Count) :- $TailVar = [],
 *                         $NewList = [red, green, blue | $TailVar],
 *                         count($NewList, $Count).    # $C is 3
 *
 * Note: In this inference engine, numbers are Constants.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestCount {

   public static void main(String[] args) {

      KnowledgeBase kb;

      Constant red   = new Constant("red");
      Constant green = new Constant("green");
      Constant blue  = new Constant("blue");
      Constant one   = new Constant("one");
      Constant two   = new Constant("two");
      Constant three = new Constant("three");

      SLinkedList p1 = new SLinkedList(false, green, blue);
      SLinkedList p2 = new SLinkedList(false, red, p1);

      LogicVar c = new LogicVar("$Count");

      LogicVar tailVar = new LogicVar("$TailVar");
      SLinkedList p3 = new SLinkedList(false, one, two, three);
      SLinkedList p4 = new SLinkedList(true, red, green, blue, tailVar);
      LogicVar newList = new LogicVar("$NewList");

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
         ),
         new Rule(new Complex("test_count($Count)"),
            new And(
               new Unify(p3, tailVar),
               new Unify(newList, p4),
               new Count(newList, c)
            )
         ),
         new Rule(new Complex("test_count($Count)"),
            new And(
               new Unify(tailVar, SLinkedList.empty),
               new Unify(newList, p4),
               new Count(newList, c)
            )
         )
      );

      System.out.print("Test Count: ");

      try {
         Complex query = Make.query("test_count($X)");
         String[] expected = { "0", "3", "2", "3", "6", "3" };
         Solutions.verifyAll(query, kb, expected, 1);
      } catch (TimeOverrunException tox) {}

   }

} // TestCount
