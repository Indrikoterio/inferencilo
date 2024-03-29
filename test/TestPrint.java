/**
 * TestPrint
 *
 * Tests the built-in Print function.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestPrint {

   public static void main(String[] args) {

      LogicVar X = new LogicVar("$X");
      LogicVar Y = new LogicVar("$Y");
      Constant P = new Constant("Persian");

      // Set up the knowledge base.
      KnowledgeBase kb = new KnowledgeBase(
         new Rule(
            new Complex("print_test"),
            new And(
               new Unify("$X = king"),
               new Unify("$Y = [Cyrus, , Cambysis, Darius]"),
               new Print(P, X, Y),
               new NewLine()
            )
         )
      );

      Rule rule = new Rule("print_test2 :- $X = World, $Y = Cleve, " +
                           "print(`Hello %s, my name is %s.`, $X, $Y), nl.");
      kb.addRule(rule);

      System.out.println("Test Print - should print:");
      System.out.println("Persian, king, [Cyrus, Cambysis, Darius]");

      try {
         // Define query and root of search space.
         Complex query = Make.query("print_test");
         String[] expected = {"print_test"};
         Solutions.verifyAll(query, kb, expected, 0);
      } catch (TimeOverrunException tox) { }

      System.out.println("Test Print 2 - should print:");
      System.out.println("Hello World, my name is Cleve.");

      try {
         Complex query = Make.query("print_test2");
         String[] expected = {"print_test2"};
         Solutions.verifyAll(query, kb, expected, 0);
      } catch (TimeOverrunException tox) { }
   }

}  // TestPrint
