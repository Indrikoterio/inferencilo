/**
 * BuiltIn2
 *
 * This class is used to test Built-in Predicate functionality.
 *
 * BuiltIn2 has two arguments, an input argument and an output argument.
 *
 *     buildin2($In, $Out)
 *
 * It simple binds the input to the output. This is equivalent to:
 *
 *     $In = $Out
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class BuiltIn2 extends BuiltInPredicate {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public BuiltIn2(Unifiable... args) {
      super("BuiltIn2", args);
   }


   /**
    * evaluate
    *
    * The unique work of the built-in predicate is done in this method.
    *
    * This test class is extremely simple. It simply unifies the input
    * argument with the output argument.
    *
    * @param   parent solution
    * @return  new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet ss) {
      if (arguments.length < 2) return null;
      if (arguments[0] == null) return null;
      return arguments[0].unify(arguments[1], ss);
   } // evaluate

} // BuiltIn2
