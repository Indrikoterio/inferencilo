/**
 * OrSolutionNode
 *
 * Solution node for the Or operator.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class OrSolutionNode extends SolutionNode {

   private SolutionNode headSolutionNode = null;
   private SolutionNode tailSolutionNode = null;
   private Operator operatorTail = null;
   private SubstitutionSet parentSolution;

   /**
    * constructor
    *
    * @param  goal
    * @param  knowledge base
    * @param  parent solution (substitution set)
    * @param  parent solution node
    */
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

   /*
    * getHeadSolutionNode
    *
    * @return solution node of head
    */
   SolutionNode getHeadSolutionNode() {
      return headSolutionNode;
   }

   /*
    * getTailSolutionNode
    *
    * @return solution node of tail
    */
   SolutionNode getTailSolutionNode() {
      return tailSolutionNode;
   }

   /**
    * nextSolution
    *
    * @return new substitution set
    * @throws TimeOverrunException
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackTracking()) { return null; }

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
   } // nextSolution

} // OrSolutionNode
