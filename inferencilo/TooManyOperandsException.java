/**
 * TooManyOperandsException
 *
 * Throw when there are too many operands.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class TooManyOperandsException extends RuntimeException {

   public TooManyOperandsException(String message) {
      super("Too many operands: " + message);
   }

}  // TooManyOperandsException
