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
    */
   public UnmatchedBracketsException() {
      super("Unmatched Brackets.");
   }

}  // UnmatchedBracketsException
