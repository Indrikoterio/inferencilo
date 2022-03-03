/**
 * Exclude
 *
 * Exclude is the opposite of Include. It takes a list, and
 * creates a new list of arguments which are excluded by the
 * given filter predicate. Eg.
 *
 * ..., exclude(filter_predicate_name, $InList, $OutList),...
 *
 * The first argument, a predicate name, must be a Constant,
 * which corresponds to a fact/rule with an arity of 1.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.HashMap;

public class Exclude extends FilterBase {

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Exclude(Unifiable... arguments) {
      super("Exclude", arguments);
   } // Exclude


   /**
    * constructor
    *
    * @param  arguments as string
    */
   public Exclude(String strArgs) {
      super("Exclude", strArgs);
   } // Exclude

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
      if (solution == null) return true;
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
      return new Exclude(newArguments);
   } // standardizeVariablesApart

}  // Exclude
