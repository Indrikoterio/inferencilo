/**
 * BIPSolutionNodeIO
 *
 * Built In Predicate Solution Node.
 *
 * This solution node was designed for predicates which have
 * and input list and an output list:
 *
 *    do_something(InList, OutList)
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class BIPSolutionNodeIO extends SolutionNode {

   BuiltInPredicateIO goal;
   boolean moreSolutions = true;

   /**
    * constructor
    *
    * @param  unifiable goal
    * @param  knowlegde base
    * @param  parent solution set
    * @param  parent solution node
    * @param  solution node
    */
   public BIPSolutionNodeIO(BuiltInPredicateIO goal, KnowledgeBase kb,
                            SubstitutionSet parentSolution,
                            SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      this.goal = goal;
   }

   /**
    * nextSolution
    *
    * Call evaluate() to do some work on the input argument,
    * then unify the result with the output argument.
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {
      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;
      Unifiable term1 = goal.evaluate(getParentSolution());   // Do the logical business.
      if (term1 == null) return null;   // failure
      Unifiable term2 = goal.getTerm(1);
      SubstitutionSet solution = term1.unify(term2, getParentSolution());
      return solution;
   }
}
