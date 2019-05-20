/**
 * CheckError
 *
 * This predicate is used to register errors found during syntax
 * analysis. It takes an input error list and adds a new error
 * message to the list, if the global maximum number of errors
 * has not been exceeded. Otherwise, the predicate fails, causing
 * the inference engine to search for another solution.
 *
 * Prolog format:   check_error(InErrors, ErrorMessage, OutErrors)
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
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new CheckError(newArguments);
   }


   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new CheckErrorSolutionNode(this, knowledge, parentSolution, parentNode);
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
    * @param   substitution set of parent
    * @return  list of error messages or null
    */
   public Unifiable evaluate(SubstitutionSet ss) {

      if (arguments.length < 3) return null;

      PList errorList = castPList(getTerm(0), ss);
      if (errorList == null) return null;
      if (errorList.count() >= Global.maxErrors) return null;

      // Create a new error list with the new error message.
      Constant newError = castConstant(getTerm(1), ss);
      if (newError == null) return null;

      List<Unifiable> listOfErrors = PList.joinHeadTail(newError, errorList);
      return new PList(false, listOfErrors);
   }
}
