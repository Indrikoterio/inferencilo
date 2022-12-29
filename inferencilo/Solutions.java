/**
 * Solutions
 *
 * This class has static methods, solve() and solveAll() which search
 * the Prolog knowledge space for solutions.
 *
 * @author Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Solutions {

   public Solutions() {}

   /**
    * solve
    *
    * Find a solution for the given goal.
    *
    * @param  goal
    * @param  kb - Knowledge Base
    * @return solution as strings
    * @throws TimeOverrunException
    */
   public static String solve(Complex goal, KnowledgeBase kb)
                              throws TimeOverrunException {
      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      if (solution != null) {
         Complex result = (Complex)goal.replaceVariables(solution);
         return result.toString();
      }
      else {
         return "No";
      }
   }

   /**
    * toString
    *
    * Returns one solution, as a String.
    * Eg. $X = [a, b, c]
    *
    * @param   goal (Complex)
    * @param   solution (SubstitutionSet solution)
    * @return  solution string
    */
   public static String toString(Complex goal, SubstitutionSet solution) {
      if (solution != null) {
         int arity = goal.arity();
         StringBuilder sb = new StringBuilder();
         Complex result = (Complex)goal.replaceVariables(solution);
         boolean first = true;
         for (int i = 0; i < arity; i++) {
            Unifiable term = goal.getTerm(i + 1);
            if (term instanceof LogicVar) {
               if (!first) { sb.append(", "); }
               sb.append(((LogicVar)term).name() + " = " +
                              result.getTerm(i + 1).toString());
               first = false;
            }
         }
         return sb.toString();
      }
      return "No";
   } // toString


   /**
    * solveAll
    *
    * Try to find all solutions for the given goal.
    *
    * @param  goal
    * @param  kb  - Knowledge Base
    * @return   solution as list of strings
    * @throws TimeOverrunException
    */
   public static ArrayList<String> solveAll(Complex goal, KnowledgeBase kb)
                                   throws TimeOverrunException{
      ArrayList<String> solutions = new ArrayList<String>();
      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      while (solution != null) {
         Complex result = (Complex)goal.replaceVariables(solution);
         solutions.add(result.toString());
         solution = root.nextSolution();
      }
      return solutions;
   }

   /**
    * verifyAll
    *
    * Finds all solutions for a given goal and verifies that the solutions
    * are as expected. Results are printed out.
    *
    * About the argument index: It may be useful to look at only one of the
    * arguments of the result. For example, if the original goal is
    * mother(Liza, $X), and the resulting complex term is mother(Liza, Jocelyn),
    * then we only really need to look at the second argument, Jocelyn.
    * This can be extracted as so: result.getTerm(2). If an index of 0 is
    * given, the expected result will be compared to the entire result term.
    *
    * @param  goal
    * @param  kb  - Knowledge Base
    * @param  array of expected results
    * @param  argument index
    * @throws TimeOverrunException
    */
   public static void verifyAll(Complex goal, KnowledgeBase kb,
                                String[] expected, int index)
                                throws TimeOverrunException {

      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      int count = 0;

      // Check for goals without solutions.
      if (solution == null && expected.length == 0) {
         System.out.println("✓");
         return;
      }

      while (solution != null) {

         Complex result = (Complex)goal.replaceVariables(solution);

         String strResult;

         if (index == 0) {
            strResult = result.toString();
         }
         else {
            if (index >= result.length()) {
               System.err.println("verifyAll: Bad index: " + index);
               return;
            }
            strResult = result.getTerm(index).toString();
         }

         if (count >= expected.length) {
            System.err.println("verifyAll:  Too many solutions.");
            System.out.println(strResult);
            return;
         }

         if (!strResult.equals(expected[count])) {
            System.err.print("verifyAll:  Unexpected!! goal: " + goal);
            System.err.println("  result: " + strResult +
                               "    expected: " + expected[count]);
         }
         else {
            System.out.print("✓");
         }

         solution = root.nextSolution();
         count++;
      }  // while

      if (count != expected.length) {
         System.err.println("verifyAll: Not enough solutions.");
         System.err.println("count = " + count + "  expected = " + expected.length);
      }

      System.err.print("\n");

   }  // verifyAll

}  // Solutions


