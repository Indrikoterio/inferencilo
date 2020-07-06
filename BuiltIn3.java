/**
 * BuiltIn3
 *
 * This class is used to test Built-in Predicate functionality.
 * The BuiltIn3 predicate takes 3 arguments:
 *
 *    builtin3($InList, $Head, $Tail)
 *
 * This is equivalent to: $InList = [$Head | $Tail].
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class BuiltIn3 extends BuiltInPredicate {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public BuiltIn3(Unifiable... args) {
      super("BuiltIn3", args);
   }

   /**
    * evaluate
    *
    * Evaluate the arguments.
    *
    * @param   parent solution
    * @return  new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet ss) {

      PList inList = ss.castPList(arguments[0]);
      if (inList == null) return null;

      Unifiable newHead = ss.castConstant(inList.getHead());
      if (newHead == null) return null;

      Unifiable newTail = inList.getTail();

      // Unify new head and new tail with output terms.
      SubstitutionSet solution = newHead.unify(arguments[1], ss);
      if (newTail != null) solution = newTail.unify(arguments[2], solution);

      return solution;
   } // evaluate

} // BuiltIn3
