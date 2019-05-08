/**
 * ComplexSolutionNode
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class ComplexSolutionNode extends SolutionNode {

   private SolutionNode child = null;

   public ComplexSolutionNode(Complex goal,
                              KnowledgeBase knowledge,
                              SubstitutionSet parentSolution,
                              SolutionNode parentNode) {

      super(goal, knowledge, parentSolution, null);
   }


   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackChaining()) { return null; }

      SubstitutionSet solution;

      if (child != null) {
         solution = child.nextSolution();
         if (solution != null) return solution;
      }

      child = null;
      Rule rule;

      while (hasNextRule() == true) {
         rule = nextRule();
         Complex head = rule.getHead();
         solution = head.unify((Unifiable)goal, getParentSolution());

         if (solution != null) {
            Goal tail = rule.getBody();
            if (tail == null) return solution;
            child = tail.getSolver(getKnowledgeBase(), solution, this);
            SubstitutionSet childSolution = child.nextSolution();
            if (childSolution != null) return childSolution;
         }
      }
      return null;
   }

   public SolutionNode getChild() {
      return child;
   }
}
