/**
 * Join
 *
 * This function joins constants to form one new constant.
 * It's used to join words and punctuation.
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

public class Join extends PFunction {

   /**
    * konstruilo
    *
    * @param  listo de unuigeblaj termoj
    */
   public Join(Unifiable... parameters) {
      super("JOIN", parameters);
   }

   /**
    * constructor
    *
    * @param  parameters as string
    */
   public Join(String parameters) {
      super("JOIN", parameters);
   }


   /**
    * evaluate (komputu)
    *
    * @param   ghis-nuna solvo (SubstitutionSet)
    * @return  nova konstanto
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
         String str = "" + con;
         if (count > 0) {
            if (str.equals(",") || str.equals(".") ||
                str.equals("?") || str.equals("!")) {
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
