/**
 * TestFunctor
 *
 * Test the Functor function.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestFunctor {

   public static void main(String[] args) {   // Set up the knowledge base.

      Variable X   = Variable.instance("$X");
      Variable Y   = Variable.instance("$Y");
      Constant get = new Constant("get");
      Complex  term = new Complex("mouse(mammal, rodent)");
      Complex  term2 = new Complex("cat(mammal, carnivore)");

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(get, Y),
            new And(
               new Unify(X, new Functor(term)),
               new Unify(X, Y)
            )
         )
      );

      // Test parsing.
      Rule rule = new Rule("get($Y) :- $X = cat(mammal, carnivore), $Y = functor($X).");
      kb.addRule(rule);

      System.out.print("Test Functor: ");

      try {
         Complex goal = new Complex(get, X);
         String[] expected = {"mouse", "cat"};
         Solutions.verifyAll(goal, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }

   } // main

} // TestFunctor


