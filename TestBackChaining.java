/**
 * TestBackChaining
 *
 * Tests the inference engine.
 * Note: A constant can begin with a capital letter.
 *       A variable must be prefixed with '$'.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestBackChaining {

   public static void main(String[] args) {   // Set up the knowledge base.

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("myfact")),
         new Rule("parent(Bill, Audrey)"),
         new Rule("parent(Maria, Bill)"),
         new Rule(new Complex("parent(Tony, Maria)")),
         new Rule(new Complex("parent(Charles, Tony)")),
         new Rule(
            new Complex("ancestor($X, $Y)"),
            new And(
               new Complex("parent($X, $Y)")
            )
         ),
         new Rule(
            new Complex("ancestor($X, $Y)"),
            new And(new Complex("parent($X, $Z)"),
                    new Complex("ancestor($Z, $Y)"))
         )
      );

      System.out.print("Test Backchaining: ");

      try {
         Complex goal = new Complex("ancestor(Charles, $Y)");
         String[] expected = {"ancestor(Charles, Tony)",
                              "ancestor(Charles, Maria)",
                              "ancestor(Charles, Bill)",
                              "ancestor(Charles, Audrey)"};
         Solutions.verifyAll(goal, kb, expected, 0);
      } catch (TimeOverrunException tox) { }

   }
}


