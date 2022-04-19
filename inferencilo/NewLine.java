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

import java.util.HashMap;

public class NewLine extends BuiltInPredicate {

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
   public Expression standardizeVariablesApart(HashMap<String, Variable> newVars) {
      return this;
   }

   /**
    * evaluate
    *
    * Prints out a new line.
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {
      System.out.print("\n");
      return parentSolution;
   } // evaluate

}  // NewLine
