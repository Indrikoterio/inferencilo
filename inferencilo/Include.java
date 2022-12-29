/**
 * Include
 *
 * The built-in predicate 'Include' filters terms from an input list, according
 * to a filter term. Its arguments are: filter, input list, output list. Eg.
 *
 * ...$InList = [male(Sheldon), female(Penny), female(Bernadette), male(Leonard)]
 * ...include(male($_), $InList, $OutList),...
 *
 * The filter term is a goal. Items in the input list which are unifiable
 * with the goal will be written to the output list.
 *
 * The output list above will contain only males, [male(Sheldon), male(Leonard)]
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
    * Does the given term pass the filter? In other words,
    * can the given term unify with the filter goal?
    *
    * @param  term
    * @param  substitution set
    * @return true if term passes
    *
    */
   boolean passOrDiscard(Unifiable term, SubstitutionSet ss) {
      SubstitutionSet newSS = filter.unify(term, ss);
      if (newSS != null) return true;
      else return false;
   }

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, LogicVar> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Include(newArguments);
   } // standardizeVariablesApart

}  // Include
