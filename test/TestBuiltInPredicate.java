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

      LogicVar X = new LogicVar("$X");
      LogicVar Y = new LogicVar("$Y");

      LogicVar H = new LogicVar("$H");
      LogicVar T = new LogicVar("$T");

      LogicVar In = new LogicVar("$In");
      LogicVar In_err = new LogicVar("$InErr");
      LogicVar Out = new LogicVar("$Out");
      LogicVar Out_err = new LogicVar("$OutErr");
      LogicVar Err2 = new LogicVar("$Err2");

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
               new Complex(join_all, new SLinkedList(true, H, T), Out, Err2, Out_err)
            )
         ),

         new Rule(
            new Complex(join_all, new SLinkedList(false, H), H, X, X)
         ),

         new Rule(
            new Complex(bip_test, Out, Out_err),
            new And(
               new Unify(In, SLinkedList.parse("[sister, in, law]")),
               new Unify(In_err, SLinkedList.parse("[first error]")),
               new Complex(join_all, In, Out, In_err, Out_err)
            )
         )

      );

      Complex query, result;
      SubstitutionSet solution;
      SolutionNode root;
      int count;

      try {

         System.out.print("Test Built-In Predicate, 5 arguments: ");
         query = Make.query(bip_test, X, Y);

         String expected = "sister-in-law [another error, another error, first error]";

         root = query.getSolver(kb, new SubstitutionSet(), null);
         solution = root.nextSolution();

         result = (Complex)query.replaceVariables(solution);
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

} // TestBuiltInPredicate
