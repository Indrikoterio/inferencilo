/**
 * OrSolutionNode
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class OrSolutionNode extends SolutionNode {

   private SolutionNode headSolutionNode = null;
   private SolutionNode tailSolutionNode = null;
   private Operator operatorTail = null;
   private SubstitutionSet parentSolution;

   public OrSolutionNode(Or goal, KnowledgeBase kb,
                         SubstitutionSet parentSolution,
                         SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      // The first operand could be anything, probably Complex.
      headSolutionNode = goal.getFirstOperand().getSolver(kb, parentSolution, this);
      // The operator tail is the same as the original 'Or' minus the first goal.
      operatorTail = goal.getOperatorTail();
      this.parentSolution = parentSolution;
   }

   protected SolutionNode getHeadSolutionNode() {
      return headSolutionNode;
   }

   protected SolutionNode getTailSolutionNode() {
      return tailSolutionNode;
   }

   /**
    * nextSolution
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackChaining()) { return null; }

      SubstitutionSet solution;

      if (tailSolutionNode != null) {
         solution = tailSolutionNode.nextSolution();
         return solution;
      }

      solution = headSolutionNode.nextSolution();
      if (solution != null || operatorTail.isEmpty()) {
         // System.out.println("Or headSolutionNode >>>>>>>>>> " + solution);
         return solution;
      }
      else {
         // tailSolutionNode has to be a new OrSolutionNode.
         tailSolutionNode = operatorTail.getSolver(getKnowledgeBase(),
                                                   parentSolution, this);
         SubstitutionSet tailSolution = tailSolutionNode.nextSolution();
         // System.out.println("Or tailSolution>>>>>>>>>> " + solution);
         return tailSolution;
      }
   }

}
