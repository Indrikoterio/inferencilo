/**
 * Unifiable  - Base interface for unifiable expressions.
 *
 * This interface defines a method to 'unify' logical expressions
 * (Constant, Variable, Complex).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public interface Unifiable extends Expression {

   /**
    * unify
    *
    * This method unifies (binds) predicate calculus expressions
    * (Constants, Variables, Complex/Compound terms). A substitution
    * set records the bindings made in the search for a solution.
    *
    * For example, if the source program has: $X = water, then the
    * unify method for Variable will add { $X : 'water' } to the
    * substitution set.
    *
    * @param   unifiable expression
    * @param   substitution set
    * @return  updated substitution set
    */
   public SubstitutionSet unify(Unifiable u, SubstitutionSet ss);

}
