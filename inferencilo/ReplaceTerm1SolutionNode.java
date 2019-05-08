/**
 * ReplaceTerm1SolutionNode
 *
 * Solution Node for the built-in predicate ReplaceTerm1.
 * Unify the resulting Complex term with the output argument.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class ReplaceTerm1SolutionNode extends SolutionNode {

   ReplaceTerm1 goal;
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
   public ReplaceTerm1SolutionNode(ReplaceTerm1 goal,
                                   KnowledgeBase kb,
                                   SubstitutionSet parentSolution,
                                   SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      this.goal = goal;
   }


   /**
    * nextSolution
    *
    * Call evaluate() to do some work on the input argument(s),
    * then unify the result(s) with the output arguments.
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() {

      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;

      Complex c = (Complex)goal.evaluate(getParentSolution());
      if (c == null) return null;

      Unifiable outTerm = goal.getTerm(1);
      SubstitutionSet ss = outTerm.unify(c, getParentSolution());
      return ss;
   }

}
