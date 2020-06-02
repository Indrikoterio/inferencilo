/**
 * Var  (A cache of logic Variables)
 *
 * This class is a hashmap of logic Variables. Variables are keyed by
 * the variable name, eg. $X, $Y.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Var {

   private static HashMap<String, Variable> variables = new HashMap<>();

   /**
    * get
    *
    * Gets a logic Variable from the 'variables' hashmap. If the Variable is
    * not found in the hash, create it, store it and return it.
    *
    * This function is equivalent to set().
    *
    * @param variable as string
    * @return  Variable object.
    */
   public static Variable get(String str) {
      Variable v = variables.get(str);
      if (v == null) {
         v = new Variable(str);
         variables.put(str, v);
      }
      return v;
   }

}
