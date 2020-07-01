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
      Complex  animal  = new Complex("mouse(mammal, rodent)");

      // Instantiate 'get' rule.
      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(get, Y),
            new And(
               new Functor(animal, X),
               new Unify(X, Y)
            )
         )
      );

      // Test to see if the inference engine can parse the functor predicate.
      Rule rule = new Rule("get($Y) :- $X = cat(mammal, carnivore), functor($X, $Y).");
      kb.addRule(rule);

      System.out.print("Test Functor: ");

      Complex goal = new Complex(get, X);

      try {
         String[] expected = {"mouse", "cat"};
         Solutions.verifyAll(goal, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }


      //----------------------------------------------
      // Check to make sure we can get the arity also.
      // check_arity(X, Y) := functor(diamonds(forever, a girl's...), X, Y).

      Complex mineral = new Complex("diamonds(forever, a girl's best friend)");
      Constant check_arity = new Constant("check_arity");

      // Create check_arity fact.
      kb.addRule(
         new Rule(
            new Complex(check_arity, X, Y),      // head
            new And(new Functor(mineral, X, Y))  // body
         )
      );

      goal = new Complex(check_arity, X, Y);

      try {
         SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
         SubstitutionSet solution = root.nextSolution();
         if (solution != null) {
            Complex result = (Complex)goal.replaceVariables(solution);
            String functor = "" + result.getTerm(1);
            String arity = "" + result.getTerm(2);
            if (functor.equals("diamonds") && arity.equals("2")) {
               System.out.println("✓");
               return;
            }
         }
         System.out.println("TestFunctor: ------------- ERROR");
      }
      catch (TimeOverrunException tox) { }

   } // main

} // TestFunctor


