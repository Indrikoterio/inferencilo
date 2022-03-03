/**
 * Seldom
 *
 * This predicate is used as a filter for rare patterns.
 * An analysis is usually run 3 times, with Global.maxErrors equal to 0, 1, then 2.
 * This predicate will only pass when maxErrors >= 2. Thus ensuring that
 * other patterns are tried first.
 *
 * Prolog format: seldom
 * Java format:   new Seldom()
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Seldom extends BuiltInPredicate {

   Constant anything = new Constant("anything");

   /**
    * constructor
    */
   public Seldom() {
      super("seldom");
   }


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      return this;
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
      return new SeldomSolutionNode(this, knowledge, parentSolution, parentNode);
   }


   /**
    * evaluate
    *
    * @param   substitution set of parent
    * @return  a string or null
    */
   public Unifiable evaluate(SubstitutionSet ss) {
      if (Global.maxErrors < 2) return null;
      return anything;
   }

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Seldom(newArguments);
   } // standardizeVariablesApart

} // Seldom
