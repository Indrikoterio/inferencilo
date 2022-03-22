/**
 * Hyphenate
 *
 * This class is used to test Built-in Predicate functionality.
 * Hyphenate takes 5 arguments:
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

public class Hyphenate extends BuiltInPredicate {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public Hyphenate(Unifiable... args) {

      super("Hyphenate", args);
      if (args.length < 5) throw new TooFewArgumentsException("- Hyphenate");
      if (args.length > 5) throw new TooManyArgumentsException("- Hyphenate");

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

      Constant err = new Constant("another error");
      Unifiable newErrorList = new PList(false, err, inErrors);  // Add an error.

      Unifiable[] terms = inList.flatten(2, ss);  // Get two terms
      if (terms == null) return null;
      if (terms.length < 3) return null;

      Unifiable term1 = ss.castConstant(terms[0]);
      Unifiable term2 = ss.castConstant(terms[1]);

      String str = term1.toString() + "-" + term2;
      Unifiable newHead = new Constant(str);

      Unifiable newTail = terms[2];

      SubstitutionSet solution = newErrorList.unify(arguments[4], ss);
      if (solution == null) return null;
      solution = newHead.unify(arguments[1], solution);
      if (solution == null) return null;
      if (newTail != null) solution = newTail.unify(arguments[2], solution);

      return solution;

   } // evaluate

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Hyphenate(newArguments);
   } // standardizeVariablesApart

} // Hyphenate
