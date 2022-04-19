/**
 * Variable
 *
 * A logical variable, which can be bound or unbound.
 *
 * Prolog variables are strings which begin with a capital letter,
 * eg. X, Y, Noun. In this inference engine, a Variable begins with
 * a dollar sign and a letter: $X, $Y, $Noun. This was done so that
 * Constants which begin with a capital letter do not have to be put
 * inside quote marks. (Harold, instead of "Harold".)
 *
 * This class has a cache of Variables. Why is this necessary?
 * Consider this rule:
 *
 * grandfather($X, $Y) :- father($X, $Z), father($Z, $Y).
 *
 * The complex terms for 'father' can be implemented in Java as:
 *    new Complex("father($X, $Z)")
 * and
 *    new Complex("father($Z, $Y)")
 *
 * The class Complex will create the variable $Z in the first
 * instantiation, and save it in the cache. The second instantiation
 * must use the same Variable. It gets this from the cache.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Variable implements Unifiable {

   private String name = null;
   private static int nextId = 0;
   private int id;

   /**
    * constructor
    *
    * Note: In this implementation, variable names should
    * start with a $ sign. Eg. $X.
    *
    * @param  name of variable
    */
   public Variable(String name) {
      this.id = 0;
      this.name = name;
   }

   /*
    * copy constructor
    *
    * Creates a Variable with the same name as the one given,
    * but having a different ID.
    *
    * @param variable to copy
    */
   private Variable(Variable v) {
      this.id = nextId++;
      this.name = v.name();
   }

   /**
    * toString
    *
    * Produces a string representation by combining name and ID.
    * For example: $X_237
    *
    * @return  string representation
    */
   public String toString() {
      if (id == 0) return name;
      return name + "_" + id;
   }

   /**
    * name
    *
    * @return  name of variable
    */
   public String name() { return name; }


   /**
    * id
    *
    * @return  id of variable
    */
   int id() { return id; }

   /**
    * reset
    *
    * The size of the substitution set grows as the variable
    * ID grows. (The variable ID is used to index into the
    * substitution set.) If the variable ID is allowed to grow
    * without limit, the substitution set will also grow without
    * limit, which will greatly slow down the algorithm.
    * Reset the variable ID before each search for a solution.
    */
   public static void reset() { nextId = 1; }

   /**
    * unify
    *
    * Unifies this variable with another unifiable expression.
    * See Unifiable interface for details.
    *
    * @param  other unifiable term
    * @param  substitution set
    * @return  new substitution set
    */
   public SubstitutionSet unify(Unifiable other, SubstitutionSet ss) {

      if (this == other) return ss; // A variable unifies with itself.

      // The unify method of a function evaluates the function,
      // so if the other expression is a function, call its unify method.
      if (other instanceof PFunction) return other.unify(this, ss);

      if (ss.isBound(this)) {
         return ss.getBinding(this).unify(other, ss);
      }

      SubstitutionSet newSubstitutionSet = new SubstitutionSet(ss);
      try {
         newSubstitutionSet.add(this, other);
      }
      catch (AlreadyBoundException abx) {
         System.err.println(abx.toString());
      }
      return newSubstitutionSet;
   }

   /**
    * replaceVariables
    *
    * Replace bound variables with their bindings.
    *
    * @param substitution set
    * @param expression
    */
   public Expression replaceVariables(SubstitutionSet ss) {
      if (ss.isBound(this)) {
         return ss.getBinding(this).replaceVariables(ss);
      }
      else return this;
   }

   /**
    * standardizeVariablesApart()
    *
    * Refer to Expression interface for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, Variable> newVars) {
      // Check if the expression already has a substitute variable.
      Variable newVar = newVars.get(this.toString());
      if (newVar == null) {     // If not create one.
         newVar = new Variable(this);
         newVars.put(this.toString(), newVar);
      }
      return newVar;
   }  // standardizeVariablesApart

}  // Variable
