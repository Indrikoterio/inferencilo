/**
 * InvalidVariableException
 *
 * Invalid variable. (ID == 0)
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidVariableException extends RuntimeException {

   /**
    * constructor
    *
    * @param  message
    */
   public InvalidVariableException(String message) {
      super("Invalid variable: " + message);
   }

}  // InvalidVariableException
