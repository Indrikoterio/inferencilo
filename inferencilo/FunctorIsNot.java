/**
 * FunctorIsNot/2
 *
 * This predicate is the evil twin of FunctorIs. It succeeds if
 * the functor of the given Complex term is not equal to the given
 * Constant. For example:
 *
 *  FunctorIsNot(style, genre(jazz))  <-- succeeds
 *  FunctorIsNot(genre, genre(jazz))  <-- fails
 *
 * The first argument is the constant. The second is the complex
 * term. Additional arguments are ignored.
 *
 * @author   Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class FunctorIsNot extends Compare {

   /**
    * constructor
    *
    * The first argument is a constant. The second is a complex
    * term to be tested. Additional arguments are ignored.
    *
    * @param  arguments (unifiable)
    */
   public FunctorIsNot(Unifiable... arguments) {
      super("functor_is_not", arguments);
   }


   /**
    * evaluate - Does the constant match the functor?
    *
    * @param   substitution set
    * @return  new unifiable term
    */
   public Unifiable evaluate(SubstitutionSet ss) {
      if (arguments.length < 2) return null;
      Constant c1 = castConstant(arguments[0], ss);
      if (c1 == null) return null;
      Complex c2 = getGroundComplex(arguments[1], ss);
      if (c2 == null) return null;
      String str  = "" + c1;
      String str2 = "" + c2.getTerm(0);
      if (!str.equals(str2)) return c1;
      return null;
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
      return new FunctorIsNot(newArguments);
   }

}
