/**
 * UnboundArgumentException
 *
 * Throw this exception when an argument which should be
 * bound is not.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class UnboundArgumentException extends RuntimeException {

   /**
    * constructor
    *
    * @param  message
    */
   public UnboundArgumentException(String message) {
      super("Unbound Argument: " + message);
   }

}  // UnboundArgumentException
