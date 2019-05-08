/**
 * CheckErrorSolutionNode
 *
 * Solution Node for the CheckError built-in predicate.
 *
 * Prolog model:
 *    check_error(InErrors, ErrorMessage, OutErrors)
 *
 * CheckError adds a new error message to the input error list,
 * if the maximum number of errors (Global.maxErrors) has not
 * been reached. Fail otherwise.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class CheckErrorSolutionNode extends SolutionNode {

   CheckError goal;
   boolean moreSolutions = true;

   /**
    * constructor
    *
    * @param  unifiable goal
    * @param  knowlegde base
    * @param  parent solution set
    * @param  parent solution node
    * @param  solution node
    */
   public CheckErrorSolutionNode(CheckError goal,
                                 KnowledgeBase kb,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      this.goal = goal;
   }

   /**
    * nextSolution
    *
    * Call evaluate() to check if an error message can be added.
    * If yes, evaluate will return a new error list. Unify it with
    * the outgoing argument and return the substitution set.
    * Fail otherwise.
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {
      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;
      Unifiable newErrorList = goal.evaluate(getParentSolution());
      if (newErrorList == null) return null;   // failure
      Unifiable out = goal.getTerm(2);
      SubstitutionSet solution = newErrorList.unify(out, getParentSolution());
      return solution;
   }
}
