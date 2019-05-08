/**
 * CheckTimeSolutionNode
 *
 * Solution Node for the CheckTime predicate.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class CheckTimeSolutionNode extends SolutionNode {

   CheckTime goal;
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
   public CheckTimeSolutionNode(CheckTime goal,
                                KnowledgeBase kb,
                                SubstitutionSet parentSolution,
                                SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      this.goal = goal;
   }


   /**
    * nextSolution
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;

      if (goal.timeOK()) return getParentSolution();
      return null;
   }

}
