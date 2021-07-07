/**
 * TestNot
 *
 * Testing the Not operator. (Note: variables begin with $.)
 *
 * parent(Sarah, Daniel).
 * parent(Richard, Daniel).
 * female(Sarah).
 * mother($X, $Y) := female($X), parent($X, $Y).
 * father($X, $Y) := parent($X, $Y), not(female($X)).
 *
 * A second test.
 *
 * friend(Susan).
 * friend(Raj).
 * friend(Carl).
 * invite($X) :- friend($X), not($X = Carl).
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
               new Not(new Complex("female($X)"))
            )
         ),

         //---------------------------------------
         // For second test.
         new Rule(new Complex("friend(Susan)")),
         new Rule(new Complex("friend(Raj)")),
         new Rule(new Complex("friend(Carl)")),
         new Rule(
            new Complex("invite($X)"),
            new And(
               new Complex("friend($X)"),
               new Not(new Unify(Variable.instance("$X"), new Constant("Carl")))
            )
         )

      );

      System.out.print("Test Not: ");

      try {
         Complex goal = new Complex("father($X, Daniel)");
         String[] expected = { "Richard" };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) { }

      try {
         Complex goal = new Complex("invite($X)");
         String[] expected = { "Susan", "Raj" };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) { }
   }

} // TestNot


