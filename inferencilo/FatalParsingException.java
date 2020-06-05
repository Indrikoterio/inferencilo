/**
 * FatalParsingException
 *
 * This exception is thrown when the Tokenizer tries to
 * parse a goal (such as 'mother($X, $Y); father($X, $Y)')
 * and encounters a problem.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class FatalParsingException extends RuntimeException {

   /**
    * constructor
    *
    * @param  string which caused exception
    */
   public FatalParsingException(String str) {
      super("Fatal Parsing Exception: " + str);
   }

}  // FatalParsingException
