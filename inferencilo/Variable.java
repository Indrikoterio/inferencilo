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
   private static int nextId = 1;
   private int id;

   private static HashMap<String, Variable> cache = new HashMap<>();

   /**
    * constructor
    *
    * Note: In this implementation, variable names should
    * start with a $ sign. Eg. $X.
    *
    * @param  name of variable
    */
   private Variable(String name) {
      this.id = nextId++;
      this.name = name;
   }


   /**
    * inst
    *
    * Factory method to produce Variables, or fetch
    * them from cache.
    *
    * @param  name of Variable
    * @return Variable
    */
   public static Variable inst(String name) {
      Variable v = cache.get(name);
      if (v == null) {
         v = new Variable(name);
         cache.put(name, v);
      }
      return v;
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
   public String toString() { return name + "_" + id; }

   /**
    * name
    *
    * @return  name of variable
    */
   public String name() { return name; }


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
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      // Check if the expression already has a substitute variable.
      Variable newVar = newVars.get(this);
      if (newVar == null) {     // If not create one.
         newVar = new Variable(this);
         newVars.put(this, newVar);
      }
      return newVar;
   }  // standardizeVariablesApart


   /**
    * printCache
    *
    * This method displays entries in the variable cache.
    * It's useful for debugging.
    */
   public static void printCache() {
      System.out.println("---------- Variable Cache ----------");
      for (String key : cache.keySet()) {
         Variable v = cache.get(key);
         System.out.println(v.toString());
      }
      System.out.println("------------------------------------");
   } // printCache


   /**
    * cacheSize
    *
    * @param size of the cache
    */
   public static int cacheSize() {
      return cache.size();
   }


}  // Variable
