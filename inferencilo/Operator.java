/**
 * Operator
 *
 * Base class for 'and', 'or', 'not', etc.
 * Defines methods for accessing the operands of operators.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public abstract class Operator implements Goal {

   protected ArrayList<Goal> operands;

   /**
    * constructor
    *
    * @param operands
    */
   public Operator(Goal... operands) {
      this.operands = new ArrayList<Goal>(Arrays.asList(operands));
   }

   /**
    * constructor
    *
    * @param arraylist of operands
    */
   public Operator(ArrayList<Goal> operands) {
      this.operands = operands;
   }

   /**
    * getCopy
    *
    * @return copy of operator
    */
   abstract public Operator getCopy();


   /**
    * setOperands
    *
    * @param  array list of operands
    */
   public void setOperands(ArrayList<Goal> operands) {
      this.operands = operands;
   }

   /**
    * getOperands
    *
    * @return  array list of operands
    */
   public ArrayList<Goal> getOperands() {
      return operands;
   }

   /**
    * operandCount
    *
    * @return number of operands
    */
   public int operandCount() {
      return operands.size();
   }

   /**
    * getOperand
    *
    * For getting a specific operand.
    *
    * @param  index of operand
    * @return operand (Goal)
    */
   public Goal getOperand(int i) {
      return operands.get(i);
   }

   /**
    * getFirstOperand
    *
    * For getting the first operand (= head operand).
    *
    * @return operand (Goal)
    */
   public Goal getFirstOperand() {
      return operands.get(0);
   }

   /**
    * getOperatorTail
    *
    * Gets a new operator which contains the current operands
    * except the first. In other words, the tail.
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
    protected void removeHead() { operands.remove(0); }


   /**
    * isEmpty - True if no operands.
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
      for (int i = 0; i < operandCount(); i++)
         newOperands.add((Goal) getOperand(i).replaceVariables(s));
      Operator copy = getCopy();
      copy.setOperands(newOperands);
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
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      ArrayList<Goal> newOperands = new ArrayList<Goal>();
      for (int i = 0; i < operandCount(); i++) {
         // recursive
         newOperands.add((Goal)getOperand(i).standardizeVariablesApart(newVars));
      }
      Operator copy = getCopy();
      copy.setOperands(newOperands);
      return copy;
   }

   /**
    * operandString - for debugging purposes
    *
    * @param operand (String)
    */
   public String operandString() {
      String result = "";
      for (int i = 0; i < operands.size(); i++) {
         result = result + getOperand(i).toString();
         if (i < operandCount() - 1) result += ", ";
      }
      return result;
   }

}
