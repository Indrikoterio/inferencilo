/**
 * Fail
 *
 * Defines the logical 'Fail' operator.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class Fail extends Operator implements Goal {

   public Fail() { }

   public String toString() {
      return "FAIL";
   }

   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new FailSolutionNode(this, knowledge, parentSolution, parentNode);
   }

   // getCopy, to avoid cloning.
   public Operator getCopy() {
      return new Fail();
   };

}
