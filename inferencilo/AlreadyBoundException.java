/**
 * AlreadyBoundException
 *
 * SubstitutionSet error: Tried to bind a bound variable.
 *
 * @version 1.0
 * @author Klivo
 */

package inferencilo;

public class AlreadyBoundException extends Exception {

   public AlreadyBoundException() {
      super("Tried to bind a bound variable. Don't do that.");
   }

}  // AlreadyBoundException
