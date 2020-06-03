/**
 * InvalidOperandException
 *
 * This exception is thrown when the program tries to create a class
 * with too many operands. For example, the Not operator constructor
 * takes an arraylist with one operand. If the number is other than
 * one, throw this exception.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidOperandException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidOperandException(String str) {
      super("Invalid operands: " + str);
   }

}  // InvalidOperandException
