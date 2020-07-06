/**
 * TooFewOperandsException
 *
 * Throw when there are too many operands.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class TooFewOperandsException extends Exception {

   public TooFewOperandsException(String message) {
      super("Too few operands: " + message);
   }

}  // TooFewOperandsException
