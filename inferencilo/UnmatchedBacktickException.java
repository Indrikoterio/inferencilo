/**
 * UnmatchedBacktickException
 *
 * Backticks are used to delimit strings. Eg:
 * `Hello, World.`
 *
 * They must match.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class UnmatchedBacktickException extends RuntimeException {

   /**
    * constructor
    *
    * @param  message
    */
   public UnmatchedBacktickException(String message) {
      super("Unmatched Backtick: " + message);
   }

}  // UnmatchedBacktickException
