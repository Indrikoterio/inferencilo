/**
 * And
 *
 * Defines logical 'And' operator.
 *
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class And extends Operator implements Goal {

   public And(Goal... operands) {
      super(operands);
   }

   public And(ArrayList<Goal> operands) {
      super(operands);
   }

   public SolutionNode getSolver(KnowledgeBase knowledge,
                                         SubstitutionSet parentSolution,
                                         SolutionNode parentNode) {
      return new AndSolutionNode(this, knowledge, parentSolution, parentNode);

   }

   /**
    * getCopy
    *
    * @return copy of this operator
    */
   public Operator getCopy() {
      ArrayList<Goal> al = new ArrayList<Goal>(operands);
      return new And(al);
   };

   public String toString() {
      return " AND " + operandString();
   }

}
