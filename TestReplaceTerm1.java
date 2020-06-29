/**
 * TestReplaceTerm1
 *
 * Test the ReplaceTerm1 built-in predicate.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestReplaceTerm1 {

   public static void main(String[] args) {   // Set up the knowledge base.

      Variable x   = Variable.instance("$X");
      Variable y   = Variable.instance("$Y");
      Variable z   = Variable.instance("$Z");
      Variable out = Variable.instance("$Out");

      Constant test = new Constant("test");

      /*
          test(Out) :- X = while, Y = [we, dream], Z = clause(qqq), replace_term1(Z, Out, X, Y)
       */

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(test, out), new And(   new Unify(x, new Constant("while")),
                                                     new Unify(y, PList.make("[we, dream]")),
                                                     new Unify(z, new Complex("clause(qqq)")),
                                                     new ReplaceTerm1(z, out, x, y)
                                                  )

                 )
      );

      try {
         System.out.print("Test ReplaceTerm1: ");
         Complex goal = new Complex(test, x);
         String[] expected = {"clause(while we dream)"};
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) { }

      /*
      System.out.print("Test ReplaceTerm1: ");
      Complex goal = new Complex(test, x);
      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      Complex result;
      int count = 0;
      while (solution != null) {
         result = (Complex)goal.replaceVariables(solution);
         System.out.println(result.getTerm(1));
         solution = root.nextSolution();
         count++; if (count > 10) break;  // for safety
      }
      */

   }
}


