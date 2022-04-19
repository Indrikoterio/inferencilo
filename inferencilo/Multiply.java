/**
 * Multiply
 *
 * Multiply arguments together.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.HashMap;

public class Multiply extends PFunction {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public Multiply(Unifiable... parameters) {
      super("MULTIPLY", parameters);
   }

   /**
    * constructor
    *
    * @param  parameters as string
    */
   public Multiply(String parameters) {
      super("MULTIPLY", parameters);
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

      if (params.length < 2) throw new TooFewArgumentsException("in Multiply.");

      double product = 1.0;

      // All parameters must be bound.
      for (Unifiable param : params) {
         Constant c = ss.castConstant(param);
         if (c == null) throw new UnboundArgumentException("in Multiply.");
         product = product * Double.parseDouble(c.toString());
      }

      return new Constant("" + product);

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
      return new Multiply(newArguments);
   } // standardizeVariablesApart

}  // Multiply
