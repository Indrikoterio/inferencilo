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
 * ?- father($X, Daniel)
 *
 *--------------------------
 * A second and third test.
 *
 * friend(Penny).
 * friend(Sheldon).
 * friend(Leonard).
 * invite($X) :- friend($X), not($X = Sheldon).
 * invite2($X) :- friend($X), not($X = Leonard).
 *
 * ?- invite($X)
 * ?- invite2($X)
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
         new Rule(new Complex("friend(Penny)")),
         new Rule(new Complex("friend(Sheldon)")),
         new Rule(new Complex("friend(Leonard)")),
         new Rule(
            new Complex("invite($X)"),
            new And(
               new Complex("friend($X)"),
               new Not(new Unify(new LogicVar("$X"), new Constant("Sheldon")))
            )
         ),

         //---------------------------------------
         // Third test. Can this be parsed?
         new Rule(
            new Complex("invite2($X)"),
            new And(
               new Complex("friend($X)"),
               Make.subgoal("not($X = Leonard)")
            )
         )
      );

      System.out.print("Test Not: ");

      try {
         Complex query = Make.query("father($X, Daniel)");
         String[] expected = { "Richard" };
         Solutions.verifyAll(query, kb, expected, 1);
      } catch (TimeOverrunException tox) { }

      try {
         Complex query = Make.query("invite($X)");
         String[] expected = { "Penny", "Leonard" };
         Solutions.verifyAll(query, kb, expected, 1);
      } catch (TimeOverrunException tox) { }

      try {
         Complex query = Make.query("invite2($X)");
         String[] expected = { "Penny", "Sheldon" };
         Solutions.verifyAll(query, kb, expected, 1);
      } catch (TimeOverrunException tox) { }
   }

} // TestNot


