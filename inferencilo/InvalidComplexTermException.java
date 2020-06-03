/**
 * InvalidComplexTermException
 *
 * This exception is thrown when the program attempts to make
 * a complex term from an invalid string representation. For Example,
 * "mother(elizabeth, mimi)" is valid, but "mother[elizabeth, mimi)"
 * will throw this exception.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidComplexTermException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidComplexTermException(String str) {
      super("Invalid complex term: " + str);
   }

}  // InvalidComplexTermException
