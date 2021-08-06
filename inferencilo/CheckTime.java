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

import java.util.HashMap;

public class CheckTime implements Goal {

   public String predicateName = "Check Time";
   public Unifiable[] arguments;

   /**
    * constructor
    *
    * No arguments.
    */
   public CheckTime() { }


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
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      return this;
   }


   /**
    * timeOK
    *
    * Check elapsed time. If it is less than the maximum time,
    * return true. Otherwise false.
    *
    * @return  t/f
    */
   public boolean timeOK() throws TimeOverrunException {

      long nano = System.nanoTime();
      long elapsed = (long)((nano - Global.startTime) / 1000000.0);
      if (elapsed <= Global.maxTime) return true;
      else {
         throw new TimeOverrunException(elapsed);
      }
      //return false;

   }  // timeOK()

}
