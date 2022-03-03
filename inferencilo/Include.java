/**
 * Include
 *
 * Include filters terms from an input list, according to the
 * filter predicate. It returns a new list. Eg.
 *
 * ..., include(filter_predicate_name, $InList, $OutList),...
 *
 * The first argument, a predicate name, must be a Constant,
 * which corresponds to a fact/rule with an arity of 1.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.HashMap;

public class Include extends FilterBase {

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Include(Unifiable... arguments) {
      super("Include", arguments);
   } // Include


   /**
    * constructor
    *
    * @param  arguments as string
    */
   public Include(String strArgs) {
      super("Include", strArgs);
   } // Include

   /*
    * passOrDiscard
    *
    * Does the given pass the filter test?
    *
    * @param  value
    * @param  knowledge base
    * @throws TimeOverrunException
    * @return true if value passes
    *
    */
   boolean passOrDiscard(Unifiable value, KnowledgeBase kb)
                         throws TimeOverrunException {
      Complex goal = new Complex(filter + "(" + value + ").");
      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      if (solution != null) return true;
      else return false;
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
      return new Include(newArguments);
   } // standardizeVariablesApart

}  // Include
