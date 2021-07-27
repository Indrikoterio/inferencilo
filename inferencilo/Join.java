/**
 * Join
 *
 * This function joins constants to form one new constant.
 * It's used to join words and punctuation.
 *
 * Words are separated by a space, but punctuation is attached
 * directly to the previous word. For example:
 *
 *   $D1 = coffee, $D2 = \, , $D3 = tea, $D4 = or, $D5 = juice,
 *   $X = join($D1, $D2, $D3, $D4, $D5).
 *
 * $X becomes the constant: coffee, tea or juice
 *
 * Note: All terms must be bound. If not, an exception is thrown.
 *
 * @author  Klivo
 * @version 1.0, 2021
 */

package inferencilo;

import java.util.*;

public class Join extends PFunction {

   /**
    * constructor
    *
    * @param  list of unifiable terms
    */
   public Join(Unifiable... parameters) {
      super("JOIN", parameters);
      if (parameters.length < 2) throw new TooFewArgumentsException("- join(...)");
   }

   /**
    * constructor
    *
    * @param  parameters as string
    */
   public Join(String parameters) {
      super("JOIN", parameters);
      List<String> strTerms = Make.splitTerms(parameters, ',');
      if (strTerms.size() < 2)
         throw new TooFewArgumentsException(
               "join() needs at least 2 arguments: " + parameters);
   }


   /**
    * evaluate
    *
    * @param   substitution set
    * @param   parameters (arguments)
    * @return  new Constant
    */
   public Unifiable evaluate(SubstitutionSet ss, Unifiable... params)
                       throws UnboundArgumentException,
                              NumberFormatException,
                              TooFewArgumentsException {

      StringBuilder sb = new StringBuilder("");

      int count = 0;
      for (Unifiable term : params) {
         Constant con = ss.castConstant(term);
         // All parameters must be bound.
         if (con == null) throw new UnboundArgumentException("in Join.");
         String str = con.toString();
         if (count > 0) {
            if (str.length() == 1 &&
                  (str.equals(",") ||
                   str.equals(".") ||
                   str.equals("?") ||
                   str.equals("!"))
                ) {
               sb.append(str);
            }
            else {
               sb.append(" ");
               sb.append(str);
            }
         }
         else {
            sb.append(str);
         }
         count++;
      }

      return new Constant(sb.toString());
   } // evaluate

} // Join
