/**
 * Variable
 *
 * A logical variable, which can be bound or unbound.
 *
 * Prolog variables are strings which begin with a capital letter, eg. X, Y, Noun.
 * In this inference engine, a Variable begins with a dollar sign and a letter:
 * $X, $Y, $Noun. This was done so that Constants which begin with a capital
 * letter do not have to be put inside quote marks. (Harold, instead of "Harold".)
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Variable implements Unifiable {

   private String name = null;
   private static int nextId = 1;
   private int id;

   /**
    * constructor
    *
    * Note: In this implemenation, variable names should
    * start with a $ sign. Eg. $X.
    *
    * @param  name of variable
    */
   public Variable(String name) {
      this.id = nextId++;
      this.name = name;
   }

   /**
    * copy constructor
    *
    * Creates a Variable with the same name as the one given,
    * but having a different ID.
    *
    * @param variable to copy
    */
   public Variable(Variable v) {
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
   public String toString() { return name + "_" + id; }

   /**
    * name
    *
    * @return  name of variable
    */
   protected String name() { return name; }


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
      if (other instanceof Function) return other.unify(this, ss);

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
         Expression s2 = ss.getBinding(this).replaceVariables(ss);
         return s2;
      }
      else return this;
   }


   /**
    * standardizeVariablesApart()
    *
    * Refer to Expression interface for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      // Check if the expression already has a substitute variable.
      Variable newVar = newVars.get(this);
      if (newVar == null) {     // If not create one.
         newVar = new Variable(this);
         newVars.put(this, newVar);
      }
      return newVar;
   }  // standardizeVariablesApart

}  // Variable
