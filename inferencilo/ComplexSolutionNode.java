/**
 * ComplexSolutionNode
 *
 * Solution node for Complex terms (= compound terms).
 *
 * This node has a child node, which is the next subgoal. The method
 * nextSolution() will check to see if the child has a solution.
 * If it does, this solution (substitution set) is returned.
 *
 * Otherwise, nextSolution() fetches rules/facts from the knowledge
 * base, and tries to unify the head of these rules with the goal.
 * If a matching fact is found, and then the solution is returned.
 * (Note, a fact is a rule without a body.)
 *
 * Otherwise, the body node of the rule becomes the child node, and
 * the algorithm tries to find a solution (substitution set) for the
 * child. It will return the child solution or null for failure.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class ComplexSolutionNode extends SolutionNode {

   private SolutionNode child = null;

   /**
    * constructor
    *
    * @param  goal
    * @param  knowledge base
    * @param  parent solution (substitution set)
    * @param  parent solution node
    */
   public ComplexSolutionNode(Complex goal,
                              KnowledgeBase knowledge,
                              SubstitutionSet parentSolution,
                              SolutionNode parentNode) {

      super(goal, knowledge, parentSolution, null);
   }


   /**
    * nextSolution
    *
    *
    * @throws TimeOverrunException
    */
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
            Goal body = rule.getBody();
            if (body == null) return solution;
            child = body.getSolver(getKnowledgeBase(), solution, this);
            SubstitutionSet childSolution = child.nextSolution();
            if (childSolution != null) return childSolution;
         }
      }
      return null;
   }

   /**
    * getChild
    *
    * @return child
    */
   public SolutionNode getChild() {
      return child;
   }

} // ComplexSolutionNode
