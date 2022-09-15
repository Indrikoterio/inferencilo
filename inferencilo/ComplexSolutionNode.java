/**
 * ComplexSolutionNode
 *
 * Solution node for complex terms (= compound terms).
 *
 * This node has a child node, which is the next subgoal. The method
 * nextSolution() will check to see if the child has a solution.
 * If it does, this solution (substitution set) is returned.
 *
 * Otherwise, nextSolution() fetches rules/facts from the knowledge
 * base, and tries to unify the head of these rules and facts with
 * the goal. If a matching fact is found, the solution is returned.
 * (Note, a fact is a rule without a body.)
 *
 * Otherwise, the body node of the rule becomes the child node, and
 * the algorithm tries to find a solution (substitution set) for the
 * child. It will return the child solution or null for failure.
 *
 * @author  Cleve (Klivo) Lendon
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

      // If the goal is a rule or a fact (not an operator), count the number.
      // For example, if the database has:
      // grandfather($Grand, $Child) :- father($Grand, $X), father($X, $Child).
      // grandfather($Grand, $Child) :- father($Grand, $X), mother($X, $Child).
      // ...then the count of grandfather is 2;

      int count = knowledge.getRuleCount(goal);

      // Note: Sometimes it is perfectly OK for a goal to fail
      // because a rule or fact is not found in the knowledgebase.
      // In other cases, the rule or fact is missing because the
      // programmer has misspelled it. Report missing rules.
      // This error message can be commented out in production.
      //if (count == 0) {
      //   Complex c = (Complex)goal;
      //   System.err.println("Missing rule: " + c.key());
      //}
      setRuleCount(count);
   }


   /**
    * nextSolution
    *
    *
    * @throws TimeOverrunException
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackTracking()) { return null; }

      SubstitutionSet solution;

      if (child != null) {
         solution = child.nextSolution();
         if (solution != null) return solution;
      }

      child = null;
      Rule rule;

      while (hasNextRule() == true) {

         // The fallbackId saves the nextId, in case the next rule fails.
         // Restoring this id to nextId will keep the substitution set small.
         int fallbackId = Variable.getNextId();

         rule = nextRule();
         Complex head = rule.getHead();
         solution = head.unify((Unifiable)goal, getParentSolution());

         if (solution == null) {  // If it fails, restore Id.
            Variable.setNextId(fallbackId);
         } else { // Success.
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
