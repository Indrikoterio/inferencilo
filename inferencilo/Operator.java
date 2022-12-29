/**
 * Operator
 *
 * Base class for And, Or, Not operators, etc.
 * Defines methods for accessing the operands.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

abstract class Operator implements Goal {

   private List<Goal> operands;

   /**
    * constructor
    *
    * @param list of operands
    */
   public Operator(Goal... operands) {
      this.operands = new ArrayList<Goal>(Arrays.asList(operands));
   }

   /**
    * constructor
    *
    * @param list of operands
    */
   public Operator(List<Goal> operands) {
      this.operands = operands;
   }

   /**
    * getCopy
    *
    * @return copy of operator
    */
   abstract public Operator getCopy();

   /**
    * getOperand
    *
    * Gets a specific operand.
    *
    * @param  index of operand
    * @return operand (Goal)
    */
   public Goal getOperand(int i) {
      return operands.get(i);
   }

   /**
    * getOperands
    *
    * @return all operands
    */
   public List<Goal> getOperands() {
      return operands;
   }

   /**
    * getFirstOperand
    *
    * Gets the first operand (head operand).
    *
    * @return operand (Goal)
    */
   public Goal getFirstOperand() {
      return operands.get(0);
   }

   /**
    * getOperatorTail
    *
    * Gets a copy of the operator, containing the current
    * operands except the first. In other words, the tail.
    *
    * @return new operator
    */
   public Operator getOperatorTail() {
      Operator newOperator = getCopy();
      newOperator.removeHead();
      return newOperator;
   }

   /*
    * removeHead
    *
    * This method assists getOperatorTail().
    * getOperatorTail() first produces a complete copy of the current
    * operator (including all operands), then it calls this method
    * to remove the head operand, thus leaving the tail.
    */
    private void removeHead() { operands.remove(0); }

   /**
    * isEmpty
    *
    * Returns true if there are no operands.
    *
    * @return  t/f
    */
   public boolean isEmpty() {
      return operands.isEmpty();
   }

   /**
    * replaceVariables
    *
    * Refer to Expression for full comments.
    *
    * @param   substitution set
    * @return  new expression, without variables
    */
   public Expression replaceVariables(SubstitutionSet s) {
      ArrayList<Goal> newOperands = new ArrayList<Goal>();
      for (int i = 0; i < operands.size(); i++)
         newOperands.add((Goal) getOperand(i).replaceVariables(s));
      Operator copy = getCopy();
      copy.operands = newOperands;
      return copy;
   }

   /**
    * standardizeVariablesApart
    *
    * See Expression for details.
    *
    * @param   list of previously standardized variables
    * @return  expression with standardized variables
    */
   public Expression standardizeVariablesApart(HashMap<String, LogicVar> newVars) {
      ArrayList<Goal> newOperands = new ArrayList<Goal>();
      for (int i = 0; i < operands.size(); i++) {
         // recursive
         newOperands.add((Goal)getOperand(i).standardizeVariablesApart(newVars));
      }
      Operator copy = getCopy();
      copy.operands = newOperands;
      return copy;
   }

   /**
    * operandString
    *
    * Creates a string for debugging purposes
    *
    * @return operand string
    */
   public String operandString() {
      String result = "";
      for (int i = 0; i < operands.size(); i++) {
         result = result + getOperand(i).toString();
         if (i < operands.size() - 1) result += ", ";
      }
      return result;
   }

} // Operator
