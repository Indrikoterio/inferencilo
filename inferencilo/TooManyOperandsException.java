/**
 * TooManyOperandsException
 *
 * The operator Not() only accepts one operand.
 *
 * @author Klivo
 * @version 1.0
 */

package inferencilo;

public class TooManyOperandsException extends Exception {

   public TooManyOperandsException() {
      super("'Not' operator only accepts one operand.");
   }

}  // TooManyOperandsException
