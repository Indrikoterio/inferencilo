/**
 * UnmatchedParenthesesException
 *
 * Parentheses must match; ()) is wrong.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class UnmatchedParenthesesException extends RuntimeException {

   /**
    * constructor
    *
    * @param  message
    */
   public UnmatchedParenthesesException(String message) {
      super("Unmatched Parentheses: " + message);
   }

}  // UnmatchedParenthesesException
