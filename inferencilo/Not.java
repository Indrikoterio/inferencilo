/**
 * Not
 *
 * In Prolog, the operator Not means 'currently not provable'.
 * This operator only accepts one operand. Eg. not(father(jonathan, _))
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */


package inferencilo;

import java.util.*;

public class Not extends Operator implements Goal {

   /**
    * constructor
    *
    * @param  array of goals
    */
   public Not(Goal... operands) {
      super(operands);
      if (operands.length != 1) throw new TooManyOperandsException();
   }

   /**
    * constructor
    *
    * @param  arraylist of goals
    */
   public Not(ArrayList<Goal> operands) {
      super(operands);
      if (operands.size() != 1) throw new TooManyOperandsException();
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
      ArrayList<Goal> al = new ArrayList<Goal>();
      al.add(operands.get(0));
      return new Not(al);
   };

   public String toString() { return " NOT " + operandString(); }

}
