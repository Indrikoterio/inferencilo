/**
 * TestFunctor
 *
 * Test the Functor function.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestFunctor {

   public static void main(String[] args) {   // Set up the knowledge base.

      Variable x   = new Variable("$X");
      Variable y   = new Variable("$Y");
      Constant get = new Constant("get");
      Complex  term = new Complex("mouse(mammal, rodent)");

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(get, y),
            new And(
               new Unify(
                  x, new Functor(term)
               ),
               new Unify(x, y)
            )
         )
      );


      System.out.print("Test Functor: ");

      try {
         Complex goal = new Complex(get, x);
         String[] expected = {"mouse"};
         Solutions.verifyAll(goal, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }

   }
}


