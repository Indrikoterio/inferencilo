/**
 * UnifySolutionNode
 *
 * Unify is Prolog '='. Eg. X = giraffe
 *
 * Reference:
 * http://www.dai.ed.ac.uk/groups/ssp/bookpages/quickprolog/node12.html
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class UnifySolutionNode extends SolutionNode {

   Unify uni;
   boolean moreSolutions = true;

   public UnifySolutionNode(Unify goal, KnowledgeBase kb,
                            SubstitutionSet parentSolution,
                            SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      uni = goal;
   }

   public SubstitutionSet nextSolution() {

      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;

      Unifiable term1 = uni.getTerm1();
      Unifiable term2 = uni.getTerm2();

      SubstitutionSet solution = term1.unify(term2, getParentSolution());
      return solution;
   }
}
