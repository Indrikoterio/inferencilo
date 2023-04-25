/**
 * TestFunctor
 *
 * Test the Functor built-in predicate.
 *
 * Rules and queries:
 *
 *    get($Y) :- functor(mouse(mammal, rodent), $X), $X = $Y.
 *    get($Y) :- $X = cat(mammal, carnivore), functor($X, $Y).
 *    ?- get($X).
 *
 *-----------------------------------
 *
 *    check_arity($X, $Y) :- functor(diamonds(forever, a girl's best friend), $X, $Y).
 *    ?- check_arity($X, $Y).
 *
 *-----------------------------------
 *
 *    test_functor($Y) :- $X = symptom(cold, sneezing), functor($X, symptom),
 *                        $Y = `Success #1`.
 *    test_functor($Y) :- $X = symptom(cold, sneezing), not(functor($X, symptoms)),
 *                        $Y = `Success #2`.
 *    test_functor($Y) :- $X = symptom(cold, sneezing), functor($X, symp*),
 *                        $Y = `Success #3`.
 *    ?- test_functor($X).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestFunctor {

   public static void main(String[] args) {   // Set up the knowledge base.

      LogicVar X   = new LogicVar("$X");
      LogicVar Y   = new LogicVar("$Y");
      Constant get = new Constant("get");
      Complex  animal  = new Complex("mouse(mammal, rodent)");

      // Instantiate 'get' rule.
      // get($Y) :- functor(mouse(mammal, rodent), $X), $X = $Y.
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

      Complex query = Make.query(get, X);

      try {
         String[] expected = {"mouse", "cat"};
         Solutions.verifyAll(query, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }


      //----------------------------------------------
      // Check to make sure we can get the arity also.
      // check_arity(X, Y) :- functor(diamonds(forever, a girl's best friend), X, Y).

      Complex mineral = new Complex("diamonds(forever, a girl's best friend)");
      Constant check_arity = new Constant("check_arity");

      // Create check_arity fact.
      kb.addRule(
         new Rule(
            new Complex(check_arity, X, Y),      // head
            new And(new Functor(mineral, X, Y))  // body
         )
      );

      query = Make.query(check_arity, X, Y);

      try {
         SolutionNode root = query.getSolver(kb, new SubstitutionSet(), null);
         SubstitutionSet solution = root.nextSolution();
         if (solution != null) {
            Complex result = (Complex)query.replaceVariables(solution);
            String functor = result.getTerm(1).toString();
            String arity = result.getTerm(2).toString();
            if (functor.equals("diamonds") && arity.equals("2")) {
               System.out.println("✓");
               return;
            }
         }
         System.out.println("TestFunctor: ------------- ERROR");
      }
      catch (TimeOverrunException tox) { }

      // A second test.

      Complex  term = new Complex("symptom(cold, sneezing)");
      Constant test_functor = new Constant("test_functor");

      kb = new KnowledgeBase(

         new Rule(new Complex(test_functor, Y),
            new And(
               new Unify(X, term),
               new Functor(X, new Constant("symptom")),
               new Unify(Y, new Constant("Success! #1"))
            )
         ),

         new Rule(new Complex(test_functor, Y),
            new And(
               new Unify(X, term),
               new Not(new Functor(X, new Constant("symptoms"))),
               new Unify(Y, new Constant("Success! #2"))
            )
         ),

         new Rule(new Complex(test_functor, Y),
            new And(
               new Unify(X, term),
               new Functor(X, new Constant("symp*")),
               new Unify(Y, new Constant("Success! #3"))
            )
         )
      );

      try {
         System.out.print("Test Functor/2: ");
         query = Make.query(test_functor, X);
         String[] expected5 = {"Success! #1", "Success! #2", "Success! #3"};
         Solutions.verifyAll(query, kb, expected5, 1);

      } catch (TimeOverrunException tox) { }


   } // main

} // TestFunctor


