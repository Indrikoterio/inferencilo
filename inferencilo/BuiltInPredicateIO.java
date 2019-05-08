/**
 * BuiltInPredicateIO
 *
 * This class is a base class for built-in predicates with the form:
 *
 *      do_something($InList, $OutList)
 *
 * The IO stands for In, Out. The first argument is an input word list;
 + the second is the output.
 *
 * A subclass must implement the method evaluate().
 *
 * During execution, the solution node (BIPSolutionNodeIO) calls
 * evaluate(), which does some work on the InList. The solution
 * node unifies the modified InList with the OutList.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public abstract class BuiltInPredicateIO extends BuiltInPredicate implements Unifiable, Goal {

   /**
    * constructor
    *
    * @param  predicate name
    * @param  unifiable arguments
    */
   public BuiltInPredicateIO(String predicateName, Unifiable... arguments) {
      super(predicateName, arguments);
   }

   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new BIPSolutionNodeIO(this, knowledge, parentSolution, parentNode);
   }

   public String toString() { return predicateName + "(...)"; }

}
