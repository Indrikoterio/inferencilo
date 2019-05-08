/**
 * Var  (Prolog Variables)
 *
 * This class is a global hash of Prolog Variables.
 *
 * In this inference engine, Prolog variables can
 * be defines as so:
 *
 * Variable X = new Variable("X");
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Var {

   private static HashMap<String, Variable> variables = new HashMap<>();

   /**
    * set
    *
    * If the given variable already exists in the hashmap,
    * just return it. If it doesn't yet exist, create it and return it.
    *
    * @param variable as string, eg: "$X"
    * @return  Variable object.
    */
   public static Variable set(String str) {
      Variable v = variables.get(str);
      if (v != null) return v;
      v = new Variable(str);
      variables.put(str, v);
      return v;
   }

   /**
    * get
    *
    * @param variable as string
    * @return  Variable object.
    * @throws  Illegal Argument X if variable not in hash.
    */
   public static Variable get(String str) {
      Variable v = variables.get(str);
      if (v == null)
         throw new IllegalArgumentException("Var.get(\"" + str + "\"): " +
                                            "not found.");
      return v;
   }

}
