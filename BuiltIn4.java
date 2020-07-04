/**
 * BuiltIn4
 *
 * This class is used to test Built-in Predicate functionality.
 * BuiltIn4 has 4 arguments:
 *
 *    word list - in
 *    new word list - out
 *    error list - in
 *    new error list - out
 *
 * The input word list is ignored. A new word list is created
 * [transformed list] and bound to the second argument.
 *
 * A new error message is created: "second error". This is
 * appended to the input error list, to create a new error list.
 * The new error list is bound to the fourth argument.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class BuiltIn4 extends BuiltInPredicate {

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public BuiltIn4(Unifiable... args) {
      super("BuiltIn4", args);
   }

   /**
    * evaluate
    *
    * Evaluate the arguments. See comments at top of file.
    *
    * @param   parent solution
    * @return  new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet ss) {

      Constant err = new Constant("second error");  // Make a new error
      Unifiable newErrorList = new PList(false, err, arguments[2]);
      Unifiable wordListOut = PList.make("[transformed list]");

      SubstitutionSet solution = wordListOut.unify(arguments[1], ss);
      solution = newErrorList.unify(arguments[3], solution);

      return solution;
   } // evaluate

} // BuiltIn4
