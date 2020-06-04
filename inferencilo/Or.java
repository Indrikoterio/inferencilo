/**
 * Or
 *
 * Defines logical 'Or' operator.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Or extends Operator implements Goal {

   public Or(Goal... operands) {
      super(operands);
   }

   public Or(ArrayList<Goal> operands) {
      super(operands);
   }

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
