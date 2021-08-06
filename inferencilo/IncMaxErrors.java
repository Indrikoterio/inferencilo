/**
 * IncMaxErrors
 *
 * The global variable Global.maxErrors is used to force the
 * inference machine to search for a better solution when an
 * error is found. In many cases, there is no better solution,
 * so an error should not force further searches. For example:
 * "Paris in the the spring." "The the" is an error, and no amount
 * of searching will provide a correct alternative analysis.
 *
 * For such cases, it is useful to increment maxErrors, so that
 * the analysis of syntax can continue.
 *
 * Prolog format:   inc_max_errors()
 * Java format:  new IncMaxErrors()
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class IncMaxErrors extends BuiltInPredicate {

   final static Constant x = new Constant("x");

   /**
    * constructor
    *
    * @param  InErrors
    * @param  OutErrors
    */
   public IncMaxErrors(Unifiable... args) {
      super("inc_max_errors", args);
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
      return new IncMaxErrorsSolutionNode(this, knowledge, parentSolution, parentNode);
   }


   /**
    * evaluate
    *
    * Increment maxErrors.
    *
    * @param   substitution set of parent
    * @return  x = success
    */
   public Unifiable evaluate(SubstitutionSet ss) {
      Global.maxErrors++;
      return x;
   }
}
