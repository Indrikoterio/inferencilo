/**
 * TestBuiltInPredicate
 *
 * Inferencilo allows programmers to easily implement their own
 * predicates and functions, by subclassing BuiltInPredicate and
 * PFunction, respectively.
 *
 * This program tests Hyphenate, which extends BuiltInPredicate.
 *
 * In order to test this functionality, the following rules will
 * be written to the knowledgebase:
 *
 *   join_all($In, $Out, $InErr, $OutErr) :- hyphenate($In, $H, $T, $InErr, $Err2),
 *                                              join_all([$H | $T], $Out, $Err2, $OutErr).
 *   join_all([$H], $H, $X, $X).
 *
 *   bip_test($Out, $OutErr) :- join_all([sister, in, law], $Out, [first error], $OutErr).
 *
 * Useful reference: https://swish.swi-prolog.org/
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestBuiltInPredicate {

   public static void main(String[] args) {

      Variable X = Variable.inst("$X");
      Variable Y = Variable.inst("$Y");

      Variable H = Variable.inst("$H");
      Variable T = Variable.inst("$T");

      Variable In = Variable.inst("$In");
      Variable In_err = Variable.inst("$InErr");
      Variable Out = Variable.inst("$Out");
      Variable Out_err = Variable.inst("$OutErr");
      Variable Err2 = Variable.inst("$Err2");

      Constant bip_test = new Constant("bip_test");
      Constant join_all = new Constant("join_all");

      KnowledgeBase kb = new KnowledgeBase(

         /****************************************************
             join_all($In, $Out, $InErr, $OutErr) :-
                             hyphenate($In, $H, $T, $InErr, $Err2),
                             join_all([$H, $T], $Out, $Err2, $OutErr).
             join_all([$H], $H, $X, $X).
          ***************************************************/

         new Rule(
            new Complex(join_all, In, Out, In_err, Out_err),
            new And(
               new Hyphenate(In, H, T, In_err, Err2),
               new Complex(join_all, new PList(true, H, T), Out, Err2, Out_err)
            )
         ),

         new Rule(
            new Complex(join_all, new PList(false, H), H, X, X)
         ),

         new Rule(
            new Complex(bip_test, Out, Out_err),
            new And(
               new Unify(In, PList.parse("[sister, in, law]")),
               new Unify(In_err, PList.parse("[first error]")),
               new Complex(join_all, In, Out, In_err, Out_err)
            )
         )

      );

      Complex goal, result;
      SubstitutionSet solution;
      SolutionNode root;
      int count;

      try {

         System.out.print("Test Built-In Predicate, 5 arguments: ");
         goal = new Complex(bip_test, X, Y);

         String expected = "sister-in-law [another error, another error, first error]";

         root = goal.getSolver(kb, new SubstitutionSet(), null);
         solution = root.nextSolution();

         result = (Complex)goal.replaceVariables(solution);
         String r = result.getTerm(1) + " " + result.getTerm(2);
         if (r.equals(expected)) {
            System.out.print("✓");
         }
         else {
            System.out.println("Unexpected: " + r);
         }

         System.out.println("");

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

} // TestBuiltInPredicate
