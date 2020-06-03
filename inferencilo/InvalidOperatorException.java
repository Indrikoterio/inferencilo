/**
 * InvalidOperatorException
 *
 * This exception is thrown when the program tries to convert an invalid
 * string to an operator (And or Or). For example,
 * "father(jake); father(carl)" is valid, but "father(jake);; father(carl)"
 * would throw this exception, 
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidOperatorException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidOperatorException(String str) {
      super("Invalid operator: " + str);
   }

}  // InvalidOperatorException
