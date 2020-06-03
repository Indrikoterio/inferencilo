/**
 * Unify
 *
 * Defines the logical Unify operator: X = Y.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Unify extends Operator implements Goal {

   private Unifiable term1, term2;

   /**
    * constructor
    *
    * This class holds references to two unifiable terms.
    *
    * @param unifiable term
    * @param unifiable term
    */
   public Unify(Unifiable term1, Unifiable term2) {
      this.term1 = term1;
      this.term2 = term2;
   }


   /**
    * constructor
    *
    * The string parameter is parsed. For example, the input
    * "$X = $Y" is used to construct two variables: $X and $Y.
    *
    * @param terms  ($X = $Y)
    */
   public Unify(String terms) {
      String[] arguments = terms.split("=");
      if (arguments.length == 2) {
         term1 = Make.term(arguments[0]);
         term2 = Make.term(arguments[1]);
      }
   }

   Unifiable getTerm1() { return term1; }
   Unifiable getTerm2() { return term2; }

   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    * This method satisfies the goal interface.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                         SubstitutionSet parentSolution,
                                         SolutionNode parentNode) {
      return new UnifySolutionNode(this, knowledge, parentSolution, parentNode);
   }

   /**
    * standardizeVariablesApart()
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable newTerm1 = (Unifiable)term1.standardizeVariablesApart(newVars);
      Unifiable newTerm2 = (Unifiable)term2.standardizeVariablesApart(newVars);
      return new Unify(newTerm1, newTerm2);
   }

   /**
    * getCopy
    *
    * @return copy of this operator
    */
   public Operator getCopy() {
      return new Unify(term1, term2);
   }

   public String toString() { return "" + term1 + " = " + term2; }

}
