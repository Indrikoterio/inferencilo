/**
 * Cut
 *
 * Defines logical 'Cut' operator.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Cut extends Operator implements Goal {

   public Cut() { }

   public String toString() { return " Cut "; }

   public SolutionNode getSolver(KnowledgeBase knowledge,
                                         SubstitutionSet parentSolution,
                                         SolutionNode parentNode) {
      return new CutSolutionNode(this, knowledge, parentSolution, parentNode);
   }

   // getCopy, to avoid cloning.
   public Operator getCopy() {
      return new Cut();
   };

}
