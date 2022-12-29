/**
 * NotSolutionNode
 *
 * Note: A Not only works if there are no unbound variables.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class NotSolutionNode extends SolutionNode {

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
   public NotSolutionNode(Not goal, KnowledgeBase kb,
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

      if (noBackTracking()) { return null; }
      if (parentSolution == null) return null;

      if (operand instanceof Complex) {

         Unifiable[] terms = ((Complex)operand).getTerms();

         for (Unifiable term : terms) {
            if (term instanceof LogicVar) {
               if (!parentSolution.isGround((LogicVar)term)) {
                  System.out.println("Not: LogicVar " + term.toString() + " is not grounded.");
               }
            }
         }
      }

      SubstitutionSet solution;

      solution = solutionNode.nextSolution();
      if (solution != null) {
         return null;
      }
      else {
         solution = parentSolution;
         parentSolution = null;  // Is this right? Yes, it seems so.
         return solution;
      }
   }

}  // NotSolutionNode
