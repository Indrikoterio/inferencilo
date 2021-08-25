/**
 * SolutionNode
 *
 * Represents a node in a 'proof tree'.
 *
 * Complex terms and operators (And, Or, Unify, etc.) implement a
 * method called getSolver(), which returns a SolutionNode specific
 * to the term or operator.
 *
 * The method nextSolution() starts the search for a solution.
 * When a solution is found, the search stops. Each node preserves
 * its state (goal, ruleNumber, etc.). Calling nextSolution() again
 * will continue the search from where it left off.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public abstract class SolutionNode {

   // These allow continuation.
   private KnowledgeBase knowledge;
   private SubstitutionSet parentSolution;
   private SolutionNode parentNode;
   private int ruleNumber = 0;
   private boolean noBackChaining = false;

   private Rule   currentRule = null;
   Goal    goal = null;     // goal being solved

   private int count;  // counts number of rules/facts

   /**
    * constructor
    *
    * @param  goal
    * @param  knowledge base
    * @param  parentSolution (substitution set)
    * @param  parentNode
    */
   public SolutionNode(Goal goal,
                       KnowledgeBase knowledge,
                       SubstitutionSet parentSolution,
                       SolutionNode parentNode) {
      this.goal = goal;
      this.knowledge = knowledge;

      // If the goal is a rule or a fact (not an operator), count the number.
      // For example, if the database has:
      // grandfather($Grand, $Child) :- father($Grand, $X), father($X, $Child).
      // grandfather($Grand, $Child) :- father($Grand, $X), mother($X, $Child).
      // ...then the count of grandfather is 2;

      // Note: Sometimes it is perfectly OK for a goal to fail
      // because a rule or fact is not found in the knowledgebase.
      // In other cases, the rule or fact is missing because the
      // programmer has misspelled it. Report missing rules.
      // This error message can be commented out in production.

      if (goal instanceof Complex) {
         count = knowledge.getRuleCount(goal);
         /*  For debugging.
         if (count == 0) {
            Complex c = (Complex)goal;
            System.err.println("Missing rule: " + c.key());
         }
         */
      }

      this.parentSolution = parentSolution;
      this.parentNode     = parentNode;
   }

   /**
    * nextSolution
    *
    * Abstract method to initiate or continue the search for a solution.
    *
    * @return substitution set
    * @throws TimeOverrunException
    */
   public abstract SubstitutionSet nextSolution() throws TimeOverrunException;

   /**
    * nextRule
    *
    * Fetches next rule from the database, with standardized variables.
    * Returns null (failure) if the goal has been exhausted.
    *
    * @return rule object
    */
   public Rule nextRule() {
      if (hasNextRule()) {
         currentRule = knowledge.getRuleStandardizedApart(goal, ruleNumber++);
      }
      else {
         currentRule = null;
      }
      return currentRule;
   }

   /*
    * hasNextRule()
    *
    * Returns true if the knowledge base contains untried rules
    * for this node's goal. False otherwise.
    *
    * @return t/f
    */
   boolean hasNextRule() {
      if (noBackChaining) return false;
      return ruleNumber < count;
   }

   /*
    * getParentSolution
    *
    * Returns parent's solution (substitution set).
    *
    * @return substitution set
    */
   SubstitutionSet getParentSolution() {
      return parentSolution;
   }

   /*
    * getKnowledgeBase
    *
    * @return knowledge base
    */
   KnowledgeBase getKnowledgeBase() {
      return knowledge;
   }

   /**
    * getParentNode
    *
    * @return parent solution node
    */
   public SolutionNode getParentNode() { return parentNode; }

   /**
    * setNoBackChaining
    *
    * Disables back chaining. This is used for the cut operator: !.
    */
   public void setNoBackChaining() {
      noBackChaining = true;
   }

   /**
    * noBackChaining
    *
    * Tests whether back chaining is allowed or not.
    *
    * @return t/f
    */
   public boolean noBackChaining() {
      return noBackChaining;
   }

}  // SolutionNode
