/**
 * BuiltIn5
 *
 * This class is used to test Built-in Predicate functionality.
 * BuiltIn5 takes 5 arguments:
 *
 *    word list  - in
 *    new head word  - out
 *    tail of list   - out
 *    error list - in
 *    new error list - out
 *
 * This predicate takes the first two words of the word list,
 * and joins them with a hyphen. The new word is unified with
 * (bound to) 'new head word'. The remainder of the word list
 * is bound to the tail argument.
 *
 * The predicate also creates an error message: 'an error'. This
 * message is added to the error list, then unified with the
 * 'new error list'.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class BuiltIn5 extends BuiltInPredicate {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public BuiltIn5(Unifiable... args) {

      super("BuiltIn5", args);
      if (args.length < 5) throw new TooFewArgumentsException("- BuiltIn5");
      if (args.length > 5) throw new TooManyArgumentsException("- BuiltIn5");

   } // constructor


   /**
    * evaluate
    *
    * Evaluate the arguments.
    *
    * @param  parent solution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet ss) {

      PList inList = ss.castPList(arguments[0]);
      if (inList == null) return null;

      PList inErrors = ss.castPList(arguments[3]);
      if (inErrors == null) return null;

      Constant err = new Constant("another error message");
      Unifiable newErrorList = new PList(false, err, inErrors);  // Add an error.

      Unifiable[] terms = inList.flatten(2, ss);  // Get two terms
      if (terms == null) return null;
      if (terms.length < 3) return null;

      String str = terms[0].toString() + "-" + terms[1];
      Unifiable newHead = new Constant(str);

      Unifiable newTail = terms[2];

      SubstitutionSet solution = newErrorList.unify(arguments[4], ss);
      solution = newHead.unify(arguments[1], solution);
      if (newTail != null) solution = newTail.unify(arguments[2], solution);

      return solution;

   } // evaluate

} // BuiltIn5
