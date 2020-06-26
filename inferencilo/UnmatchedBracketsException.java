/**
 * UnmatchedBracketsException
 *
 * Brackets must match; []] is wrong.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class UnmatchedBracketsException extends RuntimeException {

   /**
    * constructor
    *
    * @param  message
    */
   public UnmatchedBracketsException(String message) {
      super("Unmatched Brackets: " + message);
   }

}  // UnmatchedBracketsException
