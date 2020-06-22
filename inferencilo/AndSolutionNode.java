/**
 * AndSolutionNode
 *
 * Solution node for the And operator.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class AndSolutionNode extends SolutionNode {

   private SolutionNode headSolutionNode = null;
   private SolutionNode tailSolutionNode = null;
   private Operator operatorTail = null;

   /**
    * constructor
    *
    * @param  goal
    * @param  knowledge base
    * @param  parent solution (substitution set)
    * @param  parent solution node
    */
   public AndSolutionNode(And goal, KnowledgeBase kb,
                          SubstitutionSet parentSolution,
                          SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);

      // The first operand could be anything, perhaps a complex term.
      headSolutionNode = goal.getFirstOperand().getSolver(kb, parentSolution, this);
      // The operator tail is the same as the original 'And' minus the first goal.
      operatorTail = goal.getOperatorTail();
   }

   /*
    * getHeadSolutionNode
    *
    * @return solution node of head
    */
   protected SolutionNode getHeadSolutionNode() {
      return headSolutionNode;
   }

   /*
    * getTailSolutionNode
    *
    * @return solution node of tail
    */
   protected SolutionNode getTailSolutionNode() {
      return tailSolutionNode;
   }

   /**
    * nextSolution
    *
    * Recursively call next solution on all subgoals.
    *
    * @return  new substitution set (solution)
    * @throws  TimeOverrunException
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackChaining()) { return null; }

      SubstitutionSet solution;

      if (tailSolutionNode != null) {
         solution = tailSolutionNode.nextSolution();
         if (solution != null) return solution;
      }

      solution = headSolutionNode.nextSolution();
      while (solution != null) {

         if (operatorTail.isEmpty()) return solution;
         else {
            // tailSolutionNode has to be a new AndSolutionNode.
            tailSolutionNode = operatorTail.getSolver(getKnowledgeBase(),
                                                      solution, this);
            SubstitutionSet tailSolution = tailSolutionNode.nextSolution();
            if (tailSolution != null) return tailSolution;
         }
         solution = headSolutionNode.nextSolution();
      }
      return null;
   }

} // AndSolutionNode
