/**
 * Add
 *
 * Add two numbers together.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class Add extends PFunction {

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
         sum = sum + Double.parseDouble("" + c);
      }

      return new Constant("" + sum);

   } // evaluate()

}  // Add
