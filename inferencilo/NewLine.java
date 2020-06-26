/**
 * NewLine
 *
 * This built-in function prints out a new line.
 * The parser will recognize "nl".
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.Hashtable;

public class NewLine extends BuiltInPredicate implements Unifiable, Goal {

   private static final String NAME = "NL";

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public NewLine() { super(NAME); }


   /**
    * toString()
    *
    * @return printable name
    */
   public String toString() { return NAME; }


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    *
    * NewLine has no arguments, so there is nothing to do here.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      return this;
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
      return new NewLineSolutionNode(this, knowledge, parentSolution, parentNode);
   }

   /**
    * evaluate
    *
    * Prints out a new line.
    */
   public Unifiable evaluate(SubstitutionSet ss) {
      System.out.print("\n");
      return Anon.anon;   // not needed
   } // evaluate

}  // NewLine
