/**
 * TooFewArgumentsException
 *
 * Throw when there are too few arguments.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class TooFewArgumentsException extends RuntimeException {

   public TooFewArgumentsException(String message) {
      super("Too few arguments " + message);
   }

}  // TooFewArgumentsException
