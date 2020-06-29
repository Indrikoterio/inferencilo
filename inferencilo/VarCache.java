/**
 * VarCache
 *
 * This class is a cache of logic Variables.
 *
 * Why is this necessary? Consider this rule:
 *
 * grandfather($X, $Y) :- father($X, $Z), father($Z, $Y).
 *
 * The complex terms for 'father' can be implemented in Java as:
 *    new Complex("father($X, $Z)")
 * and
 *    new Complex("father($Z, $Y)")
 * The class Complex will create the variable $Z in the first
 * instantiation, and save it in VarCache. The second instantiation
 * must use the same Variable. It gets this from VarCache.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class VarCache {

   private static HashMap<String, Variable> variables = new HashMap<>();

   /**
    * get  (= set)
    *
    * Gets a logic Variable from the 'variables' cache. If the Variable
    * is not found in the cache, create it, store it and return it.
    *
    * This function is equivalent to set().
    *
    * @param  variable name
    * @return Variable object.
    */
   public static Variable get(String name) {
      Variable v = variables.get(name);
      if (v == null) {
         v = new Variable(name);
         variables.put(name, v);
      }
      return v;
   } // get


   /**
    * showEntries
    *
    * This method is for debugging purposes.
    * It outputs all entries in the variable cache.
    */
   public static void showEntries() {
      System.out.println("---------- Variable Cache ----------");
      for (String key : variables.keySet()) {
         Variable v = variables.get(key);
         System.out.println("" + v);
      }
      System.out.println("------------------------------------");
   }

}  // VarCache
