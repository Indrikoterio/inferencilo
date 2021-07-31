/**
 * TestBuiltInPredicate
 *
 * Inferencilo allows programmers to easily implement their own
 * predicates and functions, by subclassing BuiltInPredicate and
 * PFunction, respectively.
 *
 * This program tests BuiltIn5, which extends BuiltInPredicate.
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
      Variable T2 = Variable.inst("$T2");

      Variable In = Variable.inst("$In");
      Variable In_err = Variable.inst("$InErr");
      Variable Out = Variable.inst("$Out");
      Variable Out_err = Variable.inst("$OutErr");
      Variable Err2 = Variable.inst("$Err2");

      Constant bip5_test = new Constant("bip5_test");
      Constant bip5_rule = new Constant("bip5_rule");

      KnowledgeBase kb = new KnowledgeBase(

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
               new Unify(In, PList.parse("[sister, in, law]")),
               new Unify(In_err, PList.parse("[first error]")),
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
         )
      );

      Complex goal, result;
      SubstitutionSet solution;
      SolutionNode root;
      int count;

      try {

         System.out.print("Test Built-In Predicate, 5 arguments: ");
         goal = new Complex(bip5_test, X, Y);

         String[] expectedBIP = {
            "[sister-in, law] [another error message, first error]",
            "[sister, in-law] [another error message, first error]",
            "[sister, in, law] [first error]"
         };

         root = goal.getSolver(kb, new SubstitutionSet(), null);
         solution = root.nextSolution();
         count = 0;
         while (solution != null) {
            result = (Complex)goal.replaceVariables(solution);
            String r = result.getTerm(1) + " " + result.getTerm(2);
            if (r.equals(expectedBIP[count])) {
               System.out.print("✓");
            }
            else {
               System.out.println("Unexpected: " + r);
            }
            solution = root.nextSolution();
            count++; if (count > 30) break;  // for safety
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
