/**
 * CutSolutionNode
 *
 * Solution Node for Cut operator. Sets 'noBackChaining' flag
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
      if (noBackChaining()) { return null; }
      setNoBackChaining();
      SolutionNode parent = getParentNode();
      // Set noBackChaining on all ancestors.
      while (parent != null) {
         //System.out.println("......" + parent);
         parent.setNoBackChaining();
         parent = parent.getParentNode();
      }
      return parentSolution;
   }

}
