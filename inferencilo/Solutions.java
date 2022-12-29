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
    * Find a solution for the given query.
    *
    * @param  query
    * @param  kb - Knowledge Base
    * @return solution as strings
    * @throws TimeOverrunException
    */
   public static String solve(Complex query, KnowledgeBase kb)
                              throws TimeOverrunException {
      SolutionNode root = query.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      if (solution != null) {
         Complex result = (Complex)query.replaceVariables(solution);
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
    * @param   query (Complex)
    * @param   solution (SubstitutionSet solution)
    * @return  solution string
    */
   public static String toString(Complex query, SubstitutionSet solution) {
      if (solution != null) {
         int arity = query.arity();
         StringBuilder sb = new StringBuilder();
         Complex result = (Complex)query.replaceVariables(solution);
         boolean first = true;
         for (int i = 0; i < arity; i++) {
            Unifiable term = query.getTerm(i + 1);
            if (term instanceof LogicVar) {
               if (!first) { sb.append(", "); }
               sb.append(((LogicVar)term).name() + " = " +
                              result.getTerm(i + 1).toString());
               first = false;
            }
         }
         String out = sb.toString();
         if (out.length() == 0) return "True ";
         return out;
      }
      return "No";
   } // toString


   /**
    * solveAll
    *
    * Try to find all solutions for the given query.
    *
    * @param  query
    * @param  kb  - Knowledge Base
    * @return solution as list of strings
    * @throws TimeOverrunException
    */
   public static ArrayList<String> solveAll(Complex query, KnowledgeBase kb)
                                   throws TimeOverrunException{
      ArrayList<String> solutions = new ArrayList<String>();
      SolutionNode root = query.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      while (solution != null) {
         Complex result = (Complex)query.replaceVariables(solution);
         solutions.add(result.toString());
         solution = root.nextSolution();
      }
      return solutions;
   }

   /**
    * verifyAll
    *
    * Finds all solutions for a given query and verifies that the solutions
    * are as expected. Results are printed out.
    *
    * About the argument index:
    * It may be useful to look at only one of the arguments of the result.
    * For example, if the original query were mother(Liza, $X), and the
    * resulting complex term were mother(Liza, Jocelyn), then we would only
    * really need to look at the second argument, Jocelyn. This can be
    * extracted as so: result.getTerm(2). If an index of 0 is given, the
    * expected result will be compared to the entire result term.
    *
    * @param  query
    * @param  kb  - Knowledge Base
    * @param  array of expected results
    * @param  argument index
    * @throws TimeOverrunException
    */
   public static void verifyAll(Complex query, KnowledgeBase kb,
                                String[] expected, int index)
                                throws TimeOverrunException {

      SolutionNode root = query.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      int count = 0;

      // Check for goals without solutions.
      if (solution == null && expected.length == 0) {
         System.out.println("✓");
         return;
      }

      while (solution != null) {

         Complex result = (Complex)query.replaceVariables(solution);

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
            System.err.print("verifyAll:  Unexpected!! query: " + query);
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


