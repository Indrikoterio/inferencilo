/**
 * SolutionNode
 *
 * @author  Klivo
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
   Goal   goal = null;     // goal being solved
   String predicateName;   // Use as key to kb, eg.  mother/2

   /**
    * constructor
    *
    * @param  goal
    * @param  knowledge baswe
    * @param  parentSolution (substitution set)
    * @param  parentNode
    */
   public SolutionNode(Goal goal,
                            KnowledgeBase knowledge,
                            SubstitutionSet parentSolution,
                            SolutionNode parentNode) {
      this.goal = goal;
      predicateName = "" + goal;
      this.knowledge = knowledge;
      this.parentSolution = parentSolution;
      this.parentNode     = parentNode;
   }

   public abstract SubstitutionSet nextSolution() throws TimeOverrunException;

   public void reset(SubstitutionSet newParentSolution) {
      parentSolution = newParentSolution;
      ruleNumber = 0;
   }


   /*
    * nextRule
    *
    * Fetches next rule from the database.
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

   protected boolean hasNextRule() {
      return ruleNumber < knowledge.getRuleCount(goal);
   }

   protected SubstitutionSet getParentSolution() {
      return parentSolution;
   }

   protected KnowledgeBase getKnowledgeBase() {
      return knowledge;
   }

   public Rule getCurrentRule() {
      return currentRule;
   }

   public Goal getGoal() {
      return goal;
   }

   public SolutionNode getParentNode() {
      return parentNode;
   }

   public void setNoBackChaining() {
      noBackChaining = true;
   }

   public boolean noBackChaining() {
      return noBackChaining;
   }


}
