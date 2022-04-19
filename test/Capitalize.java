/**
 * Capitalize
 *
 * This function capitalizes a word or name.
 *
 * It accepts one argument, a Constant or Variable bound to a Constant,
 * and returns a capitalized Constant. (tokyo becomes Tokyo.)
 *
 * Note: Unlike a predicate, which returns a substitution set (or null),
 * a function returns a Unifiable term. Therefore, it must be used with
 * unification (=). Eg.
 *
 *    ..., $CapName = capitalize($Name),...
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

import java.util.HashMap;

public class Capitalize extends PFunction {

   /**
    * constructor
    *
    * @param  unifiable arguments
    * @throws  TooFewArgumentsException, TooManyArgumentsException
    */
   public Capitalize(Unifiable... args) {
      super("Capitalize", args);
      if (arguments.length < 1) throw new TooFewArgumentsException("- Capitalize");
      if (arguments.length > 1) throw new TooManyArgumentsException("- Capitalize");
   }

   /**
    * constructor
    *
    * @param  arguments as string
    * @throws  TooFewArgumentsException, TooManyArgumentsException
    */
   public Capitalize(String str) {
      super("Capitalize", str);
      if (arguments.length < 1) throw new TooFewArgumentsException("- Capitalize");
      if (arguments.length > 1) throw new TooManyArgumentsException("- Capitalize");
   }

   /**
    * evaluate the arguments
    *
    * @param   Substitution Set
    * @param   Unifiable parameters
    * @return  Constant
    */
   public Unifiable evaluate(SubstitutionSet ss, Unifiable... params) {

      Constant c = ss.castConstant(arguments[0]);
      if (c == null) return new Constant("?!");
      if (c.toString().length() == 0) return new Constant("");

      String first = c.toString().substring(0, 1).toUpperCase();
      String theRest = c.toString().substring(1);

      return new Constant(first + theRest);

   } // evaluate()

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Capitalize(newArguments);
   } // standardizeVariablesApart

}  // Capitalize
