/**
 * TimeSolutionNode
 *
 * The time operator measures the execution time of a goal.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class TimeSolutionNode extends SolutionNode {

   private SolutionNode solutionNode;
   private Goal operand;
   private SubstitutionSet parentSolution;

   /**
    * constructor
    *
    * @param  goal
    * @param  knowledge base
    * @param  parent solution
    * @param  parent node
    */
   public TimeSolutionNode(Time goal, KnowledgeBase kb,
                           SubstitutionSet parentSolution,
                           SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      operand = goal.getOperand();
      solutionNode = operand.getSolver(kb, parentSolution, this);
      this.parentSolution = parentSolution;
   }

   /**
    * nextSolution
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackChaining()) { return null; }
      if (parentSolution == null) return null;

      if (operand instanceof Complex) {

         Unifiable[] terms = ((Complex)operand).getTerms();

         for (Unifiable term : terms) {
            if (term instanceof Variable) {
               if (!parentSolution.isGround((Variable)term)) {
                  System.out.println("Time: Variable " + term.toString() +
                                     " is not grounded.");
               }
            }
         } // for
      }

      SubstitutionSet solution;

      long start = System.nanoTime();
      solution   = solutionNode.nextSolution();
      long end   = System.nanoTime();

      long microseconds = (long)((end - start) / 1000.0);
      double milliseconds = (double)microseconds / 1000.0;
      System.out.println(String.format("\-- %.2f milliseconds.",
                         milliseconds));

      return solution;
   }

}  // TimeSolutionNode
