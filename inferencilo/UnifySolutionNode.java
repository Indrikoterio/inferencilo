/**
 * UnifySolutionNode
 *
 * Unify is Prolog '='. Eg. X = giraffe
 *
 * Reference:
 * http://www.dai.ed.ac.uk/groups/ssp/bookpages/quickprolog/node12.html
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class UnifySolutionNode extends SolutionNode {

   private Unify uni;
   private boolean moreSolutions = true;

   /**
    * constructor
    *
    * @param  goal
    * @param  knowledge base
    * @param  parent solution
    * @param  parent node
    */
   public UnifySolutionNode(Unify goal, KnowledgeBase kb,
                            SubstitutionSet parentSolution,
                            SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      uni = goal;
   }

   /**
    * nextSolution
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() {

      if (noBackTracking()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;

      Unifiable term1 = uni.getTerm1();
      Unifiable term2 = uni.getTerm2();

      SubstitutionSet solution = term1.unify(term2, getParentSolution());
      return solution;
   }
}  // UnifySolutionNode
