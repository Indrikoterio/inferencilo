/**
 * Fail
 *
 * Defines logical 'Fail' operator.
 *
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Fail extends AbstractOperator implements Goal {

   public Fail() { }

   public String toString() {
      return " Fail ";
   }

   public SolutionNode getSolver(KnowledgeBase knowledge,
                                         SubstitutionSet parentSolution,
                                         SolutionNode parentNode) {
      return new FailSolutionNode(this, knowledge, parentSolution, parentNode);
   }

   // getCopy, to avoid cloning.
   public AbstractOperator getCopy() {
      return new Fail();
   };

}
