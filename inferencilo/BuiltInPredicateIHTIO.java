/**
 * BuiltInPredicateIHTIO
 *
 * This class is a base class for built-in predicates which have the format:
 *
 *   do_something(InList, OutHead, OutTail, InErrors, OutErrors)
 *
 * IHTIO stands for In, Head, Tail, In, Out.
 *
 * A subclass must implement the method evaluate().
 *
 * During execution, the solution node (BIPSolutionNodeIHTIO) calls
 * evaluate(), which does some work on the InList, generating a new
 * Head and Tail. The solution node then unifies the resulting Head
 * and Tail with the output arguments (OutHead, OutTail) of the predicate.
 *
 * Refer to comments in BIPSolutionNodeIHTIO for further clarifications.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public abstract class BuiltInPredicateIHTIO extends BuiltInPredicate implements Unifiable, Goal {

   /**
    * constructor
    *
    * @param  predicate name
    * @param  unifiable arguments
    */
   public BuiltInPredicateIHTIO(String predicateName, Unifiable... arguments) {
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
      return new BIPSolutionNodeIHTIO(this, knowledge, parentSolution, parentNode);
   }


   /**
    * evaluate
    *
    * The unique work of the built-in predicate is done by this method.
    *
    * @param   substitution set of parent
    * @return  unifiable term or null
    */
   public abstract Unifiable evaluate(SubstitutionSet ss);


   /**
    * getHead
    *
    * Gets new Head term generated by evaluate().
    *
    * @return  new head tearm
    */
   public abstract Unifiable getHead();

   /**
    * getTail
    *
    * Gets new Tail term generated by evaluate().
    *
    * @return  new tail term
    */
   public abstract Unifiable getTail();


   /**
    * getErrors
    *
    * Gets errors generated by evaluate().
    *
    * @return  new list of errors
    */
   public abstract Unifiable getErrors();

}
