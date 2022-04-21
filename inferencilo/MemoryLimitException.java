/**
 * MemoryLimitException
 *
 * Everytime a variable is unified, the inference engine creates
 * a copy of the substitution set. The size of the substitution
 * set is equal to the highest variable id.
 *
 * When the highest variable id is in the thousands, there are
 * a lot of large substitution sets, occupying a lot of memory.
 * There may be an endless loop in the algorithm.
 *
 * This exception is thrown when the variable ID becomes too high.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class MemoryLimitException extends RuntimeException {

   public MemoryLimitException(String message) {
      super("Memory Limit. " + message);
   }

}  // MemoryLimitException
