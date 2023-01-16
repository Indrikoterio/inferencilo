/**
 * Add
 *
 * Add two numbers together.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.HashMap;

public class Add extends SFunction {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public Add(Unifiable... parameters) {
      super("ADD", parameters);
   }

   /**
    * constructor
    *
    * @param  parameters as string
    */
   public Add(String parameters) {
      super("ADD", parameters);
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

      if (params.length < 2) throw new TooFewArgumentsException("in Add.");

      double sum = 0.0;

      // All parameters must be bound.
      for (Unifiable param : params) {
         Constant c = ss.castConstant(param);
         if (c == null) throw new UnboundArgumentException("in Add.");
         sum = sum + Double.parseDouble(c.toString());
      }

      return new Constant("" + sum);

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
      return new Add(newArguments);
   } // standardizeVariablesApart

}  // Add
