/**
 * TestAppend
 *
 * Test the Append predicate.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestAppend {

   public static void main(String[] args) {   // Set up the knowledge base.

      Variable x   = new Variable("$X");
      Variable y   = new Variable("$Y");
      Variable out = new Variable("$Out");
      Constant test_append = new Constant("test_append");
      Constant orange = new Constant("orange");

      /*
          test_append(Out) :- X = red, Y = [green, blue, purple], append(X, orange, null, Y, Out).
       */

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(test_append, out),
            new And(
               new Unify(x, new Constant("red")),
               new Unify(y, PList.make("[green, blue, purple]")),
               new Append(x, orange, y, out)
            )
         )
      );


      System.out.print("Test Append: ");

      try {
         Complex goal = new Complex(test_append, x);
         String[] expected = {"[red, orange, green, blue, purple]"};
         Solutions.verifyAll(goal, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }

   }
}


