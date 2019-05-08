/**
 * CompareSolutionNode
 *
 * This solution node is for built in predicates which
 * compare two terms, such as FunctorIs(),.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class CompareSolutionNode extends SolutionNode {

   Compare goal;
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
   public CompareSolutionNode(Compare goal, KnowledgeBase kb,
                              SubstitutionSet parentSolution,
                              SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      this.goal = goal;
   }

   public SubstitutionSet nextSolution() throws TimeOverrunException {
      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;
      Unifiable newTerm = goal.evaluate(getParentSolution());
      if (newTerm == null) return null;
      return getParentSolution();
   }

}
