/**
 * Compare
 *
 * This is a base class for built-in predicates which compare terms,
 * such as FunctorIs/2.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public abstract class Compare extends BuiltInPredicate implements Unifiable, Goal {

   /**
    * constructor
    *
    * @param  predicate name
    * @param  unifiable arguments
    */
   public Compare(String predicateName, Unifiable... arguments) {
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
      return new CompareSolutionNode(this, knowledge, parentSolution, parentNode);
   }


   /**
    * standardizeVariablesApart()
    *
    * Refer to class Expression for full comments.
    *
    * Each subclass must return its own class. This is a base class,
    * so this method is abstract.
    */
   public abstract Expression
          standardizeVariablesApart(Hashtable<Variable, Variable> newVars);

   /**
    * evaluate
    *
    * The unique work of the built-in predicate is done in the subclass.
    *
    * @param   solution set
    * @return  new unifiable
    */
   public abstract Unifiable evaluate(SubstitutionSet ss);


   /*
    * getGroundComplex
    *
    * This is a utility for the predicates FunctorIs and FunctorIsNot.
    * If the given term is a complex term, return it. If it is a variable,
    * return the complex term which it is ultimately bound to.
    *
    * @param   unifiable term
    * @param   substitution set
    * @return  complex term or null
    */
   Complex getGroundComplex(Unifiable u, SubstitutionSet ss) {
      if (u instanceof Variable) {
         Variable v = (Variable)u;
         if (ss.isGround(v)) {
            u = ss.getGroundTerm(v);
         }
         else return null;
      }
      return castComplex(u, ss);
   }

}
