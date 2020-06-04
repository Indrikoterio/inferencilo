/**
 * Not
 *
 * In Prolog, the Not operator means 'currently not provable'.
 * Many Prolog implementations use \+ for this operator.
 * 'Not' only allows one operand. Eg. not(father(jonathan, _))
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */


package inferencilo;

import java.util.*;

public class Not extends Operator implements Goal {

   private String errorMessage = "Not operator takes only 1 operand.";

    /**
     * constructor
     *
     * @param  list of operands
     */
   public Not(Goal... operands) {
      super(operands);
      if (operands.length != 1) throw new InvalidOperandException(errorMessage);
   }

    /**
     * constructor
     *
     * @param  list of operands
     */
   public Not(ArrayList<Goal> operands) {
      super(operands);
      if (operands.size() != 1) throw new InvalidOperandException(errorMessage);
   }

   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new NotSolutionNode(this, knowledge,
                                 parentSolution, parentNode);
   }


   /**
    * getCopy
    *
    * @return copy of this operator
    */
    public Operator getCopy() {
       return new Not(getFirstOperand());
    };


   /**
    * toString
    *
    * For debugging purposes.
    *
    * @return printable string
    */
   public String toString() { return " NOT " + operandString(); }


   /**
    * getOperand
    *
    * The Not operator only allows one operand.
    *
    * @return operand
    */
   public Goal getOperand() { return getFirstOperand(); }

} // Not
