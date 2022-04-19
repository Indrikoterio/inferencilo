/**
 * CheckError
 *
 * This predicate is used to register errors found during syntax
 * analysis. It takes an input error list and adds a new error
 * message to the list, if the global maximum number of errors
 * has not been exceeded. Otherwise, the predicate fails, causing
 * the inference engine to search for another solution.
 *
 * Prolog format:   check_error($InErrors, $ErrorMessage, $OutErrors)
 * Java format:  new CheckError(inErrors, new Constant("Error message."), outErrors)
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class CheckError extends BuiltInPredicate {

   /**
    * constructor
    *
    * @param  InErrors
    * @param  OutErrors
    */
   public CheckError(Unifiable... args) {
      super("CheckError", args);
   }


   /**
    * evaluate
    *
    * Get the input error list, and a count of the number of errors.
    * If the count is less than Global.maxErrors, create a new error
    * list, with the new error message, and return successfully.
    * If the error count is equal or greater than Global.maxErrors,
    * fail (return null).
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {

      if (arguments.length < 3) return null;

      PList errorList = parentSolution.castPList(getTerm(0));
      if (errorList == null) return null;
      if (errorList.count() >= Global.maxErrors) return null;

      // Create a new error list with the new error message.
      Constant newError = parentSolution.castConstant(getTerm(1));
      if (newError == null) return null;

      PList newList = new PList(true, newError, errorList);
      if (newList == null) return null;

      Unifiable outArgument = getTerm(2);
      return newList.unify(outArgument, parentSolution);
   }

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new CheckError(newArguments);
   } // standardizeVariablesApart

} // CheckError
