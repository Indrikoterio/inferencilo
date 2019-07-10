/**
 * IncMaxErrorsSolutionNode
 *
 * Solution Node for the IncMaxErrors built-in predicate.
 *
 * Prolog model: inc_max_errors()
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class IncMaxErrorsSolutionNode extends SolutionNode {

   IncMaxErrors goal;
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
   public IncMaxErrorsSolutionNode(IncMaxErrors goal,
                                   KnowledgeBase kb,
                                   SubstitutionSet parentSolution,
                                   SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      this.goal = goal;
   }

   /**
    * nextSolution
    *
    * This predicate has no arguments and never fails.
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {
      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;
      SubstitutionSet ss = getParentSolution();
      goal.evaluate(ss);
      return ss;
   }
}
