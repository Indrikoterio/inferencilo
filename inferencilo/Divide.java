/**
 * Divide
 *
 * Function to divide numbers. (All numbers are double float.)
 * For example:
 *
 *     $X = divide(12.0, 4.0, 2)
 *
 * $X binds to 1.5.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.HashMap;

public class Divide extends PFunction {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public Divide(Unifiable... parameters) {
      super("DIVIDE", parameters);
   }

   /**
    * constructor
    *
    * @param  parameters as string
    */
   public Divide(String parameters) {
      super("DIVIDE", parameters);
   }

   /**
    * evaluate the parameters
    *
    * @param   Substitution Set
    * @param   Unifiable parameters
    * @return  Constant
    * @throws  UnboundArgumentException, NumberFormatException, TooFewArgumentsException
    */
   public Unifiable evaluate(SubstitutionSet ss, Unifiable... params)
                       throws UnboundArgumentException,
                              NumberFormatException,
                              TooFewArgumentsException {

      if (params.length < 2) throw new TooFewArgumentsException("in Divide.");

      // Get the dividend.
      Constant c = ss.castConstant(params[0]);

      // All parameters must be bound.
      if (c == null) throw new UnboundArgumentException("in Divide.");
      double result = Double.parseDouble(c.toString());

      // There are one or more divisors.
      for (int i = 1; i < params.length; i++) {
         c = ss.castConstant(params[i]);
         if (c == null) throw new UnboundArgumentException("in Divide.");
         result /= Double.parseDouble(c.toString());
      }

      return new Constant("" + result);

   } // evaluate()

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, LogicVar> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Divide(newArguments);
   } // standardizeVariablesApart

}  // Divide
