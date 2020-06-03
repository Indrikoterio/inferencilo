/**
 * InvalidFunctorException
 *
 * Functor must be a Constant.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidFunctorException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidFunctorException(String str) {
      super("Invalid functor: " + str);
   }

}  // InvalidFunctorException
