/**
 * FunctorStartsWith/2
 *
 * This predicate is almost the same as functor_is/2 .
 * Some complex terms are very similar. For example, adverbo()
 * and adverboj(). Instead of calling functor_is twice, the
 * the program can call this built-in predicate once. It might
 * save some time.
 *
 *  FunctorStartsWith(adverbo, adverboj(tre bone...))  <-- succeeds
 *  FunctorStartsWith(adverbo, adjektivo(bona...))  <-- fails
 *
 * The first argument is a constant. The second is a complex term.
 * Additional arguments are ignored.
 *
 * @author   Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class FunctorStartsWith extends Compare {

   /**
    * constructor
    *
    * The first argument is a constant. The second is a complex
    * term to be tested. Additional arguments are ignored.
    *
    * @param  arguments (unifiable)
    */
   public FunctorStartsWith(Unifiable... arguments) {
      super("functor_is", arguments);
   }

   /**
    * evaluate - Does the constant match the functor?
    *
    * @param   substitution set
    * @return  new unifiable term
    */
   public Unifiable evaluate(SubstitutionSet ss) {
      if (arguments.length < 2) return null;
      Constant c1 = castConstant(arguments[0]);
      if (c1 == null) return null;
      Complex c2 = getGroundComplex(arguments[1], ss);
      if (c2 == null) return null;
      String str  = "" + c1;
      String str2 = "" + c2.getTerm(0);
      if (str2.startsWith(str)) return c1;
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
      return new FunctorStartsWith(newArguments);
   }

}
