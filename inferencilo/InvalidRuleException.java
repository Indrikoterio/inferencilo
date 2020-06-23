/**
 * InvalidRuleException
 *
 * This exception is thrown when the program tries to parse an
 * invalid rule, such as:
 *   grandfather($X, $Y) :- parent($X, $Z), :- parent($Z, $Y).
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidRuleException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidRuleException(String str) {
      super(str);
   }

}  // InvalidRuleException
