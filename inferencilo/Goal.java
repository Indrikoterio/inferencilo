/**
 * Goal
 *
 * Base of all goals. A goal is something which provides a solver.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

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
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode);
}
