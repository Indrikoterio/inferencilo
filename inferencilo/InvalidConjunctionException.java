/**
 * InvalidConjunctionException
 *
 * Conjunction means 'logical And'. In Prolog, and here, 'logical and' is
 * represented by a comma. This exception is thrown when the program tries
 * to convert an invalid string to an And operator. For example,
 * "noun(manatees), verb(swim)" is valid, but "noun(manatees),,verb(swim)"
 * would throw this exception, 
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class InvalidConjunctionException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public InvalidConjunctionException(String str) {
      super("Invalid Conjunction (And): " + str);
   }

}  // InvalidConjunctionException
