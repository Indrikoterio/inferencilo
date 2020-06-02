/**
 * AlreadyBoundException
 *
 * SubstitutionSet error: Tried to bind a bound variable.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class AlreadyBoundException extends Exception {

   public AlreadyBoundException() {
      super("Tried to bind a bound variable. Don't do that.");
   }

}  // AlreadyBoundException
