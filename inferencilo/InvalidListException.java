/**
 * InvalidListException
 *
 * This exception is thrown when the program attempts to make
 * a Prolog list from an invalid string representation.
 * For example, "[a, b, c]" is valid, but "[a, b, c" will cause
 * an exception.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidListException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidListException(String str) {
      super("Invalid list: " + str);
   }

}  // InvalidListException
