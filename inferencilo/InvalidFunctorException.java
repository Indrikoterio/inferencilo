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

   public InvalidFunctorException() {
      super("Invalid functor: Functor must be a Constant.");
   }

}  // InvalidFunctorException
