/**
 * InvalidExpressionException
 *
 * This exception is thrown when the program tries to tokenize an invalid
 * expression. For example, the expression $likes(Toby, Rachel) is wrong
 * (Dollar sign before functor name).
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidExpressionException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidExpressionException(String str) {
      super("Invalid expression: " + str);
   }

}  // InvalidExpressionException
