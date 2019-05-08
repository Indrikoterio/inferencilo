/**
 * Anon
 *
 * For anonymous variables. Anonymous variables unify with any term.
 * They indicate that the term is irrelevant.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Anon implements Unifiable {

   private Anon() {};  // Don't allow instantiation. Use the static attribute.

   public String toString() { return ""; }

   // Attribute is public for convenience.
   public static Anon anon = new Anon();

   /**
    * unify
    *
    * No substitution necessary. This 'variable' is ignored.
    *
    * @param term to unify with
    * @param substitution set
    * @return new substitution set
    */
   public SubstitutionSet unify(Unifiable term, SubstitutionSet ss) {
      return ss;
   }

   /**
    * replaceVariables
    *
    * Refer to Expression for full comments.
    *
    * @param   substitution set
    * @return  new expression, without variables
    */
   public Expression replaceVariables(SubstitutionSet s) {
      return this;
   }

   /**
    * standardizeVariablesApart()
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      return this;
   }

}
