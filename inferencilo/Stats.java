/**
 * Stats
 *
 * This predicate is used for debugging purposes.
 * At present it prints out the size of caches.
 * It ignores arguments.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Stats extends BuiltInPredicate {

   private static final String NAME = "STATS";

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Stats(Unifiable... arguments) {
      super(NAME, arguments);
   }

   /**
    * evaluate
    *
    * Prints out some information, about the Constant and
    * Variable caches.
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {

      System.out.println("Constant cache size: " + Constant.cacheSize());
      System.out.println("Variable cache size: " + Variable.cacheSize());
      return parentSolution;

   } // evaluate

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Stats(newArguments);
   } // standardizeVariablesApart

}  // Stats
