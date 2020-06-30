/**
 * Cut
 *
 * Defines logical 'Cut' operator.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class Cut extends Operator implements Goal {

   /**
    * constructor
    */
   public Cut() { }

   /**
    * toString
    */
   public String toString() { return " Cut "; }

   /**
    * getSolver
    *
    * Returns a solution node.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new CutSolutionNode(this, knowledge, parentSolution, parentNode);
   }

   // getCopy, to avoid cloning.
   public Operator getCopy() {
      return new Cut();
   }

} // Cut
