/**
 * And
 *
 * Defines a logical 'And' operator (Conjunction).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class And extends Operator implements Goal {

   /**
    * constructor
    *
    * @param  operands
    */
   public And(Goal... operands) {
      super(operands);
   }

   /**
    * constructor
    *
    * @param  array of operands
    */
   public And(ArrayList<Goal> operands) {
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
      return new AndSolutionNode(this, knowledge, parentSolution, parentNode);

   }

   /**
    * getCopy
    *
    * @return copy of this operator
    */
   public Operator getCopy() {
      ArrayList<Goal> operands = new ArrayList<Goal>(getOperands());
      return new And(operands);
   };

   /**
    * toString
    *
    * For debugging purposes.
    *
    * @return string representation
    */
   public String toString() {
      return " AND " + operandString();
   }

}
