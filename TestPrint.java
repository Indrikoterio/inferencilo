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

      Variable X = Variable.instance("$X");
      Variable Y = Variable.instance("$Y");
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

      Rule rule = new Rule("print_test :- $X = World, $Y = Cleve, " +
                           "print(\\nHello %s\\, my name is %s., $X, $Y), nl.");
      kb.addRule(rule);

      System.out.println("Test Print");

      try {
         // Define goal and root of search space.
         Complex goal = new Complex("print_test");
         String[] expected = {"print_test", "print_test"};
         Solutions.verifyAll(goal, kb, expected, 0);
      } catch (TimeOverrunException tox) { }
   }

}  // TestPrint
