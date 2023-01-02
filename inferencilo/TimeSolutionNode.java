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
   }

   /**
    * nextSolution
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackTracking()) { return null; }
      if (getParentSolution() == null) return null;

      SubstitutionSet solution;

      long start = System.nanoTime();
      solution   = solutionNode.nextSolution();
      long end   = System.nanoTime();

      long microseconds = (long)((end - start) / 1000.0);
      double milliseconds = (double)microseconds / 1000.0;
      System.out.println(String.format("\n-- %.2f milliseconds.",
                         milliseconds));

      return solution;
   }

}  // TimeSolutionNode
