/**
 * FailSolutionNode
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class FailSolutionNode extends AbstractSolutionNode {

   public FailSolutionNode(Fail goal, KnowledgeBase kb,
                           SubstitutionSet parentSolution,
                           AbstractSolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
   }

   public SubstitutionSet nextSolution() throws TimeOverrunException {
      return null;  // Always fails.
   }

}
