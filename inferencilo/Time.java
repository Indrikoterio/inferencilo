/**
 * Time
 *
 * This operator can be used to measure the execution time of goals.
 * Example:
 *
 *    time(qsort()).
 *
 * It will print out a message giving the execution time in milliseconds.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Time extends Operator implements Goal {

   private String errorMessage = "Usage: time(goal)";

    /**
     * constructor
     *
     * @param  list of operands
     */
   public Time(Goal... operands) {
      super(operands);
      if (operands.length < 1) throw new TooFewArgumentsException(errorMessage);
      if (operands.length > 1) throw new TooManyArgumentsException(errorMessage);
   }

    /**
     * constructor
     *
     * @param  list of operands
     */
   public Time(List<Goal> operands) {
      super(operands);
      int size = operands.size();
      if (size < 1) throw new TooFewArgumentsException(errorMessage);
      if (size > 1) throw new TooManyArgumentsException(errorMessage);
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
      return new TimeSolutionNode(this, knowledge,
                                 parentSolution, parentNode);
   }


   /**
    * getCopy
    *
    * @return copy of this operator
    */
    public Operator getCopy() {
       return new Time(getFirstOperand());
    };


   /**
    * toString
    *
    * For debugging purposes.
    *
    * @return printable string
    */
   public String toString() { return " TIME " + operandString(); }


   /**
    * getOperand
    *
    * The Not operator only allows one operand.
    *
    * @return operand
    */
   public Goal getOperand() { return getFirstOperand(); }

} // Time
