/**
 * Goal
 *
 * Base interface of all goal objects. Complex terms (eg. symptom(flu, fever))
 * and operators such as And, Or, Unify etc. implement this interface. A goal
 * object provides a solution node (solver).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public interface Goal extends Expression {

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
   SolutionNode getSolver(KnowledgeBase knowledge,
                          SubstitutionSet parentSolution,
                          SolutionNode parentNode);
}
