/**
 * Subtract
 *
 * Functor to subtract numbers.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.List;
import java.util.ArrayList;

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

      List<Double> numbers = new ArrayList<Double>();
      if (params.length < 2) throw new TooFewArgumentsException("in Substract.");

      // All parameters must be bound.
      for (Unifiable param : params) {
         Constant c = ss.castConstant(param);
         if (c == null) throw new UnboundArgumentException("in Subtract.");
         numbers.add(Double.parseDouble("" + c));
      }

      double result = numbers.get(0);
      int size = numbers.size();
      for (int i = 1; i < size; i++) {
         result -= numbers.get(i);
      }

      return new Constant("" + result);

   } // evaluate()

}  // Subtract
