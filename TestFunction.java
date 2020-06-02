/**
 * TestFunction
 *
 * Test functions and built-in predicates.
 *
 * Useful reference: https://swish.swi-prolog.org/
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestFunction {

   public static void main(String[] args) {

      Variable w = new Variable("$W");
      Variable x = new Variable("$X");
      Variable y = new Variable("$Y");
      Variable z = new Variable("$Z");

      Variable h = new Variable("$H");
      Variable t = new Variable("$T");
      Variable t2 = new Variable("$T2");

      Variable in = new Variable("$In");
      Variable in_err = new Variable("$InErr");
      Variable out = new Variable("$Out");
      Variable out_err = new Variable("$OutErr");
      Variable err2 = new Variable("$Err2");

      Constant function_test = new Constant("function_test");
      Constant bip_test = new Constant("bip_test");
      Constant bip3_test = new Constant("bip3_test");
      Constant bip4_test = new Constant("bip4_test");
      Constant bip5_test = new Constant("bip5_test");
      Constant bip5_rule = new Constant("bip5_rule");
      Constant test_functor_is = new Constant("test_functor_is");
      Constant simple_test  = new Constant("simple_test");
      Constant simple_test2 = new Constant("simple_test2");

      Complex  term = new Complex("symptom(cold, sneezing)");

      KnowledgeBase kb = new KnowledgeBase(

         new Rule(new Complex("function_test($L)"),
            new And(
               /* new Unify( VarCache.get("$X"), new Constant("This is OK. This works.") ), */
               new Unify("$X = A"),
               new Unify("$Y = B"),
               new Unify(
                  new ChaChaCha(VarCache.get("$X"), VarCache.get("$Y")),
                  VarCache.get("$M")
               ),
               /* new Unify( VarCache.get("$L"), VarCache.get("$M") ) */
               new Unify("$L = $M")
            )
         ),

         new Rule(new Complex("join_test($X)"),
            new And(
               new Unify(
                  new Join(Make.term("article(the)"), Make.term("noun(city)")),
                  VarCache.get("$X")
               )
            )
         ),

         /*
             built_in_pred(Y) :- X = 'sept mille dance', bip(X, Y).
          */
         new Rule(
            new Complex(bip_test, y),
            new And(
               new Unify(x, new Constant("sept mille dance")),
               new BuiltIn2(x, y)
            )
         ),

         /*
             bip3_test(H, T) :- X = [1, 2, 3, 4], bip3(X, H, T).
          */
         new Rule(
            new Complex(bip3_test, h, t),
            new And(
               new Unify(x, PList.make("[1, 2, 3, 4]")),
               new BuiltIn3(x, h, t)
            )
         ),

         /*
             bip4_test(Out, OutErr) :- In = [first], InErr = [first error],
                                          bip4(In, Out, InErr, OutErr).
          */
         new Rule(
            new Complex(bip4_test, x, y),
            new And(
               new Unify(w, PList.make("[first]")),
               new Unify(z, PList.make("[first error]")),
               new BuiltIn4(w, x, z, y)
            )
         ),


         /****************************************************
             bip5_rule(In, [H | T2], InErr, OutErr) :-
                                       bip5_predicate(In, H, T, InErr, Err2),
                                       bip5_rule(T, T2, Err2, OutErr).

             bip5_rule([H | T], [H | T2], InErr, OutErr) :-
                                       bip5_rule(T, T2, InErr, OutErr).

             bip5_rule([], [], x, x).
          ***************************************************/

         new Rule(
            new Complex(bip5_test, out, out_err),
            new And(
               new Unify(in, PList.make("[built, in, Hello]")),
               new Unify(in_err, PList.make("[first error]")),
               new Complex(bip5_rule, in, out, in_err, out_err)
            )
         ),

         new Rule(
            new Complex(bip5_rule, in, new PList(true, h, t2), in_err, out_err),
            new And(
               new BuiltIn5(in, h, t, in_err, err2),
               new Complex(bip5_rule, t, t2, err2, out_err)
            )
         ),

         new Rule(
            new Complex(bip5_rule, new PList(true, h, t),
                                   new PList(true, h, t2),
                                   in_err, out_err),
            new And(
               new Complex(bip5_rule, t, t2, in_err, out_err)
            )
         ),

         new Rule(
            new Complex(bip5_rule, PList.empty, PList.empty, x, x)
         ),

         new Rule(new Complex(test_functor_is, y),
            new And(
               new Unify(x, term),
               new FunctorIs(new Constant("symptom"), x),
               new Unify(y, new Constant("Success! #1"))
            )
         ),

         new Rule(new Complex(test_functor_is, y),
            new And(
               new Unify(x, term),
               new FunctorIsNot(new Constant("symptoms"), x),
               new Unify(y, new Constant("Success! #2"))
            )
         ),

         new Rule(new Complex(test_functor_is, y),
            new And(
               new Unify(x, term),
               new FunctorStartsWith(new Constant("symp"), x),
               new Unify(y, new Constant("Success! #3"))
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
      goal = new Complex(function_test, w);
      String[] expected = {"A + B + Cha Cha Cha!"};
      Solutions.verifyAll(goal, kb, expected, 1);

      System.out.print("Test Join: ");
      goal = new Complex("join_test($X)");
      String[] expected2 = {"noun phrase(the city)"};
      Solutions.verifyAll(goal, kb, expected2, 1);

      System.out.print("Test Built-In Predicate: ");
      goal = new Complex(bip_test, z);
      String[] expected3 = {"sept mille dance"};
      Solutions.verifyAll(goal, kb, expected3, 1);

      System.out.print("Test Built-In Predicate, 3 arguments: ");
      goal = new Complex(bip3_test, h, t);
      String[] expected4 = {"[2, 3, 4]"};
      Solutions.verifyAll(goal, kb, expected4, 2);

      System.out.print("Test Built-In Predicate, 4 arguments: ");
      goal = new Complex(bip4_test, x, y);
      result = solveIt(goal, kb);
      System.out.println(result.getTerm(1) + " " + result.getTerm(2));

      System.out.print("Test Built-In Predicate, 5 arguments:\n");
      goal = new Complex(bip5_test, x, y);
      //result = solveIt(goal, kb);
      //System.out.println(result.getTerm(1) + " " + result.getTerm(2));

      root = goal.getSolver(kb, new SubstitutionSet(), null);
      solution = root.nextSolution();
      count = 0;
      while (solution != null) {
         result = (Complex)goal.replaceVariables(solution);
         System.out.println(result.getTerm(1) + " " + result.getTerm(2));
         solution = root.nextSolution();
         count++; if (count > 30) break;  // for safety
      }


      /*
      System.out.print("Test FunctorIs/2:\n");
      goal = new Complex(test_functor_is, w);
      root = goal.getSolver(kb, new SubstitutionSet(), null);
      solution = root.nextSolution();
      count = 0;
      while (solution != null) {
         result = (Complex)goal.replaceVariables(solution);
         System.out.println(result.getTerm(1));
         solution = root.nextSolution();
         count++; if (count > 5) break;  // for safety
      }
      */

      System.out.print("Test FunctorIs/2: ");
      goal = new Complex(test_functor_is, w);
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

}
