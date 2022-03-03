/**
 * Subtract
 *
 * Function to subtract numbers.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.HashMap;

public class Subtract extends PFunction {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public Subtract(Unifiable... parameters) {
      super("SUBTRACT", parameters);
   }

   /**
    * constructor
    *
    * @param  parameters as string
    */
   public Subtract(String parameters) {
      super("SUBTRACT", parameters);
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

      if (params.length < 2) throw new TooFewArgumentsException("in Subtract.");

      Constant c = ss.castConstant(params[0]);

      // All parameters must be bound.
      if (c == null) throw new UnboundArgumentException("in Subtract.");
      double result = Double.parseDouble(c.toString());

      for (int i = 1; i < params.length; i++) {
         c = ss.castConstant(params[i]);
         if (c == null) throw new UnboundArgumentException("in Subtract.");
         result -= Double.parseDouble(c.toString());
      }

      return new Constant("" + result);

   } // evaluate()

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Subtract(newArguments);
   } // standardizeVariablesApart

}  // Subtract
