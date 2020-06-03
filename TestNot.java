/**
 * TestNot
 *
 * Testing the Not operator.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestNot {

   public static void main(String[] args) {   // Set up the knowledge base.

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("parent(Sarah, Daniel)")),
         new Rule(new Complex("parent(Richard, Daniel)")),
         //new Rule(new Complex("parent(Steve, Daniel)")),
         new Rule(new Complex("female(Sarah)")),
         new Rule(
            new Complex("mother($X, $Y)"),
            new And(
               new Complex("female($X)"),
               new Complex("parent($X, $Y)")
            )
         ),
         new Rule(
            new Complex("father($X, $Y)"),
            new And(
               new Complex("parent($X, $Y)"),
               new Not(new Complex("female($X)")) //,
               //new Fail()
            )
         )
      );

      System.out.print("Test Not: ");

      try {
         Complex goal = new Complex("father($X, Daniel)");
         String[] expected = { "Richard" };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) { }
   }

} // TestNot


