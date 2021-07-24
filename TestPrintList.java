/**
 * TestPrintList
 *
 * Tests the built-in Print List predicate.
 * The PrintList predicate is useful for debugging a theory.
 *
 *    $List1 = [pronoun(They, subject, third, plural),
 *              verb(envy, present, base),
 *              pronoun(us, object, first, plural)],
 *    print_list($List1).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestPrintList {

   public static void main(String[] args) {

      // Set up the knowledge base.
      KnowledgeBase kb = new KnowledgeBase(
         new Rule(
            new Complex("print_list_test"),
            new And(
               new Unify("$X1 = pronoun(They, subject, third, plural)"),
               new Unify("$X2 = verb(envy, present, base)"),
               new Unify("$X3 = pronoun(us, object, first, plural)"),
               new Unify("$List1 = [$X1, $X2, $X3]"),
               new PrintList("$List1")
            )
         )
      );

      System.out.println("Test PrintList");

      try {
         // Define goal and root of search space.
         Complex goal = new Complex("print_list_test");
         String[] expected = {"print_list_test"};
         Solutions.verifyAll(goal, kb, expected, 0);
      } catch (TimeOverrunException tox) { }
   }

}  // TestPrintList
