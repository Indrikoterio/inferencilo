/**
 * FailSolutionNode
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class FailSolutionNode extends SolutionNode {

   public FailSolutionNode(Fail goal, KnowledgeBase kb,
                           SubstitutionSet parentSolution,
                           SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
   }

   public SubstitutionSet nextSolution() throws TimeOverrunException {
      return null;  // Always fails.
   }

}
