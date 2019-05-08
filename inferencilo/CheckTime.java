/**
 * CheckTime
 *
 * Check to make sure that maximum execution time has not been exceeded.
 * This built-in predicate makes use of a couple global variables:
 *
 *    Global.startTime   (in nanoseconds)
 *    Global.maxTime     (in milliseconds)
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class CheckTime implements Unifiable, Goal {

   public String predicateName = "Check Time";
   public Unifiable[] arguments;

   /**
    * constructor
    *
    * The CheckTime built in predicate should produce an error message if
    * execution time is exceeded. Therefore it needs two arguments, InErrors
    * (list) and OutErrors (list). Extra arguments are ignored.
    *
    * @param  InErrors
    * @param  OutErrors
    */
   public CheckTime(Unifiable... arguments) {
      this.arguments = arguments;
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
      return new CheckTimeSolutionNode(this, knowledge, parentSolution, parentNode);
   }


   /**
    * replaceVariables
    *
    * Refer to Expression for full comments.
    * Nothing to do here.
    *
    * @param   substitution set
    * @return  this
    */
   public Expression replaceVariables(SubstitutionSet ss) {
      return this;
   }  // replaceVariables


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    * Nothing to do here.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      return this;
   }


   /**
    * unify
    *
    * Performs unification with another term.
    * For built-in predicates, this method is not used.
    *
    * @param  expression
    * @param  substitution set
    * @return new substitution set
    */
   public SubstitutionSet unify(Unifiable uni, SubstitutionSet ss) {
      return ss;  // Nothing to do here.
   }


   /**
    * timeOK
    *
    * Check ellapsed time. If it is less than the maximum time,
    * return true. Otherwise false.
    *
    * @return  t/f
    */
   public boolean timeOK() throws TimeOverrunException {

      long nano = System.nanoTime();
      long ellapsed = (long)((nano - Global.startTime) / 1000000.0);
      if (ellapsed <= Global.maxTime) return true;
      else {
         throw new TimeOverrunException(ellapsed);
      }
      //return false;

   }  // evaluate()

}
