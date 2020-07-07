/**
 * TooManyArgumentsException
 *
 * Throw when there are too many arguments.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class TooManyArgumentsException extends RuntimeException {

   public TooManyArgumentsException(String message) {
      super("Too many arguments " + message);
   }

}  // TooManyArgumentsException
