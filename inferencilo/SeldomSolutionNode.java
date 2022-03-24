/**
 * SeldomSolutionNode
 *
 * Use to filter rare predicates. Pass only if Global.maxErrors >= 2.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class SeldomSolutionNode extends SolutionNode {

   Seldom goal;
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
   public SeldomSolutionNode(Seldom goal, KnowledgeBase kb,
                             SubstitutionSet parentSolution, SolutionNode parentNode) {
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
      if (noBackTracking()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;
      SubstitutionSet ss = getParentSolution();
      Unifiable result = goal.evaluate(ss);
      if (result == null) return null;   // failure
      return ss;
   }
}
