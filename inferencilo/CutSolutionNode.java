/**
 * CutSolutionNode
 *
 * Solution Node for Cut operator. Sets 'noBackTracking' flag
 * for all ancestors.
 *
 : @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class CutSolutionNode extends SolutionNode {

   private SubstitutionSet parentSolution;

   public CutSolutionNode(Cut goal, KnowledgeBase kb,
                          SubstitutionSet parentSolution,
                          SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      this.parentSolution = parentSolution;
   }

   public SubstitutionSet nextSolution() throws TimeOverrunException {
      if (noBackTracking()) { return null; }
      setNoBackTracking();
      SolutionNode parent = getParentNode();
      // Set noBackTracking on all ancestors.
      while (parent != null) {
         //System.out.println("......" + parent);
         parent.setNoBackTracking();
         parent = parent.getParentNode();
      }
      return parentSolution;
   }

}
