/**
 * InvalidArgumentException
 *
 * This exception is thrown when one of the arguments to
 * a predicate is invalid. For example, the second argument
 * of filter/3 must be a Constant.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidArgumentException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidArgumentException(String str) {
      super("Invalid argument: " + str);
   }

}  // InvalidArgumentException
