/**
 * BuiltIn2
 *
 * This class is used to test Built In Predicate functionality.
 * BuiltIn2 is a subclass of BuiltInPredicateIO.
 *
 * @author   Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class BuiltIn2 extends BuiltInPredicateIO {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public BuiltIn2(Unifiable... args) {
      super("BuiltIn2", args);
   }


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new BuiltIn2(newArguments);
   }


   /**
    * evaluate
    *
    * The unique work of the built-in predicate is done in this method.
    *
    * This test class is extremely simple. It simply unifies the input
    * argument with the output argument.
    *
    * @return  unifiable term or null
    * @param   substitution set of parent
    */
   public Unifiable evaluate(SubstitutionSet ss) {
      return arguments[0];
   }

}
