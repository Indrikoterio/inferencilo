/**
 * TestAppend
 *
 * Test the Append predicate. The append predicate is used to join
 * terms into a list. For example:
 *
 * $X = raspberry, append(cherry, [strawberry, blueberry], $X, $Out).
 *
 * The last term of append() is an output term. For the above, $Out
 * should unify with: [cherry, strawberry, blueberry, raspberry]
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestAppend {

   public static void main(String[] args) {   // Set up the knowledge base.

      LogicVar X   = new LogicVar("$X");
      LogicVar Y   = new LogicVar("$Y");
      LogicVar Out = new LogicVar("$Out");
      Constant test_append = new Constant("test_append");
      Constant orange = new Constant("orange");

      /*
        Prolog format:
        test_append(Out) :- X = red, Y = [green, blue, purple],
                            append(X, orange, null, Y, Out).
       */

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(test_append, Out),
            new And(
               new Unify(X, new Constant("red")),
               new Unify(Y, SLinkedList.parse("[green, blue, purple]")),
               new Append(X, orange, Y, Out)
            )
         )
      );

      System.out.print("Test Append: ");

      try {
         Complex query = Make.query(test_append, X);
         String[] expected = {"[red, orange, green, blue, purple]"};
         Solutions.verifyAll(query, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }

      // Now let's parse the Append function.
      Rule rule = new Rule("test_append2($Out) :- $X = raspberry, " +
              "append(cherry, [strawberry, blueberry], $X, $Out).");
      kb.addRule(rule);

      try {
         Complex query = Make.query("test_append2($Z)");
         String[] expected = {"[cherry, strawberry, blueberry, raspberry]"};
         Solutions.verifyAll(query, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }

   }
}  // TestAppend


