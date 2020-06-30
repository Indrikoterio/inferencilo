/**
 * Or
 *
 * Defines a logical 'Or' operator.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Or extends Operator implements Goal {

   /**
    * constructor
    *
    * @param  list operands
    */
   public Or(Goal... operands) {
      super(operands);
   }

   /**
    * constructor
    *
    * @param  list operands
    */
   public Or(List<Goal> operands) {
      super(operands);
   }

   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    * This method satisfies the goal interface.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new OrSolutionNode(this, knowledge,
                                parentSolution, parentNode);
   }

   /**
    * getCopy
    *
    * @return copy of this operator
    */
   public Operator getCopy() {
      ArrayList<Goal> operands = new ArrayList<Goal>(getOperands());
      return new Or(operands);
   };

   /**
    * toString
    *
    * For debugging purposes.
    *
    * @return string representation
    */
   public String toString() {
      return " OR " + operandString();
   }

}
