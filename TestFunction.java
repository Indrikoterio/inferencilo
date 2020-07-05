/**
 * TestFunction
 *
 * Test functions and built-in predicates.
 *
 * Useful reference: https://swish.swi-prolog.org/
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestFunction {

   public static void main(String[] args) {

      Variable W = Variable.instance("$W");
      Variable X = Variable.instance("$X");
      Variable Y = Variable.instance("$Y");
      Variable Z = Variable.instance("$Z");

      Variable H = Variable.instance("$H");
      Variable T = Variable.instance("$T");
      Variable T2 = Variable.instance("$T2");

      Variable In = Variable.instance("$In");
      Variable In_err = Variable.instance("$InErr");
      Variable Out = Variable.instance("$Out");
      Variable Out_err = Variable.instance("$OutErr");
      Variable Err2 = Variable.instance("$Err2");

      Constant function_test = new Constant("function_test");
      Constant bip_test  = new Constant("bip_test");
      Constant bip3_test = new Constant("bip3_test");
      Constant bip4_test = new Constant("bip4_test");
      Constant bip5_test = new Constant("bip5_test");
      Constant bip5_rule = new Constant("bip5_rule");
      Constant test_functor = new Constant("test_functor");
      Constant simple_test  = new Constant("simple_test");
      Constant simple_test2 = new Constant("simple_test2");

      Complex  term = new Complex("symptom(cold, sneezing)");

      KnowledgeBase kb = new KnowledgeBase(

         new Rule(new Complex("function_test($L)"),
            new And(
               /* new Unify( Variable.instance("$X"), new Constant("This is OK. This works.") ), */
               new Unify("$X = A"),
               new Unify("$Y = B"),
               new Unify(
                  new ChaChaCha(Variable.instance("$X"), Variable.instance("$Y")),
                  Variable.instance("$M")
               ),
               /* new Unify( Variable.instance("$L"), Variable.instance("$M") ) */
               new Unify("$L = $M")
            )
         ),

         /*
             built_in_pred(Y) :- X = 'sept mille dance', bip(X, Y).
          */
         new Rule(
            new Complex(bip_test, Y),
            new And(
               new Unify(X, new Constant("sept mille dance")),
               new BuiltIn2(X, Y)
            )
         ),

         /*
             bip3_test(H, T) :- X = [1, 2, 3, 4], bip3(X, H, T).
          */
         new Rule(
            new Complex(bip3_test, H, T),
            new And(
               new Unify(X, PList.make("[1, 2, 3, 4]")),
               new BuiltIn3(X, H, T)
            )
         ),

         /*
             bip4_test(Out, OutErr) :- In = [first], InErr = [first error],
                                          bip4(In, Out, InErr, OutErr).
          */
         new Rule(
            new Complex(bip4_test, X, Y),
            new And(
               new Unify(W, PList.make("[first]")),
               new Unify(Z, PList.make("[first error]")),
               new BuiltIn4(W, X, Z, Y)
            )
         ),


         /****************************************************
             bip5_rule(In, [H | T2], InErr, OutErr) :-
                                       bip5_predicate(In, H, T, InErr, Err2),
                                       bip5_rule(T, T2, Err2, OutErr).

             bip5_rule([H | T], [H | T2], InErr, OutErr) :-
                                       bip5_rule(T, T2, InErr, OutErr).

             bip5_rule([], [], X, X).
          ***************************************************/

         new Rule(
            new Complex(bip5_test, Out, Out_err),
            new And(
               new Unify(In, PList.make("[built, in, Hello]")),
               new Unify(In_err, PList.make("[first error]")),
               new Complex(bip5_rule, In, Out, In_err, Out_err)
            )
         ),

         new Rule(
            new Complex(bip5_rule, In, new PList(true, H, T2), In_err, Out_err),
            new And(
               new BuiltIn5(In, H, T, In_err, Err2),
               new Complex(bip5_rule, T, T2, Err2, Out_err)
            )
         ),

         new Rule(
            new Complex(bip5_rule, new PList(true, H, T),
                                   new PList(true, H, T2),
                                   In_err, Out_err),
            new And(
               new Complex(bip5_rule, T, T2, In_err, Out_err)
            )
         ),

         new Rule(
            new Complex(bip5_rule, PList.empty, PList.empty, X, X)
         ),

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
               new Not(
                  new Functor(X, new Constant("symptoms"))
               ),
               new Unify(Y, new Constant("Success! #2"))
            )
         ),

         new Rule(new Complex(test_functor, Y),
            new And(
               new Unify(X, term),
               new Functor(X, new Constant("symp*")),
               new Unify(Y, new Constant("Success! #3"))
            )
         ),

         new Rule(new Complex(simple_test))

      );

      Complex goal, result;
      SubstitutionSet solution;
      SolutionNode root;
      int count;

      System.out.print("Test Function: ");

      try {
         goal = new Complex(function_test, W);
         String[] expected = {"A + B + Cha Cha Cha!"};
         Solutions.verifyAll(goal, kb, expected, 1);

         System.out.print("Test Built-In Predicate: ");
         goal = new Complex(bip_test, Z);
         String[] expected2 = {"sept mille dance"};
         Solutions.verifyAll(goal, kb, expected2, 1);

         System.out.print("Test Built-In Predicate, 3 arguments: ");
         goal = new Complex(bip3_test, H, T);
         String[] expected3 = {"[2, 3, 4]"};
         Solutions.verifyAll(goal, kb, expected3, 2);

         System.out.print("Test Built-In Predicate, 4 arguments: ");
         goal = new Complex(bip4_test, X, Y);
         result = solveIt(goal, kb);
         System.out.println(result.getTerm(1) + " " + result.getTerm(2));

         System.out.print("Test Built-In Predicate, 5 arguments:\n");
         goal = new Complex(bip5_test, X, Y);

         root = goal.getSolver(kb, new SubstitutionSet(), null);
         solution = root.nextSolution();
         count = 0;
         while (solution != null) {
            result = (Complex)goal.replaceVariables(solution);
            System.out.println(result.getTerm(1) + " " + result.getTerm(2));
            solution = root.nextSolution();
            count++; if (count > 30) break;  // for safety
         }

         System.out.print("Test Functor/2: ");
         goal = new Complex(test_functor, W);
         String[] expected5 = {"Success! #1", "Success! #2", "Success! #3"};
         Solutions.verifyAll(goal, kb, expected5, 1);

      } catch (TimeOverrunException tox) { }
   }


   /*
    * solveIt
    *
    * @param   goal
    * @param   knowledge base
    * @return  result
    */
   private static Complex solveIt(Complex goal, KnowledgeBase kb) {
      SubstitutionSet solution = null;
      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
      try {
         solution = root.nextSolution();
      } catch (TimeOverrunException tox) { return null; }
      return (Complex)goal.replaceVariables(solution);
   }

} // TestFunction
