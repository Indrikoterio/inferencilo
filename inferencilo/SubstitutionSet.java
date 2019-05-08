/**
 * SubstitutionSet
 *
 * A dictionary (hash) of bindings of logic Variables.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class SubstitutionSet {

   private HashMap<Variable, Unifiable> bindings;

   /**
    * constructor
    */
   public SubstitutionSet() { 
      bindings = new HashMap<Variable, Unifiable>();
   }

   /**
    * constructor
    *
    * @param  substitution set
    */
   public SubstitutionSet(SubstitutionSet ss) {
      bindings = new HashMap<Variable, Unifiable>(ss.bindings);
   }

   /**
    * add
    *
    * Binds a variable to an expression.
    * The variable becomes the key the binding dictionary (hash).
    *
    * @param  variable
    * @param  unifiable expression
    * @throws AlreadyBoundException
    */
   public void add(Variable v, Unifiable e) throws AlreadyBoundException {
      if (isBound(v)) {
         throw new AlreadyBoundException();
      }
      else {
         bindings.put(v, e);
      }
   }

   /**
    * getBinding
    *
    * @param   variable
    * @return  unifiable
    */
   public Unifiable getBinding(Variable v) {
      return (Unifiable)bindings.get(v);
   }

   /**
    * isBound
    *
    * A logic variable is bound if there exists an entry
    * for it in the substitution set.
    *
    * @param   variable
    * @return  t/f
    */
   public boolean isBound(Variable v) {
      return (bindings.get(v) != null);
   }


   /**
    * isGround
    *
    * "Terms not containing variables are called Ground."
    *
    * A variable is ground if it is ultimately bound to
    * to something other than a variable.
    *
    * @param   variable
    * @return  t/f
    */
   public boolean isGround(Variable var) {
      Variable  v = var;
      Unifiable u;
      while (true) {
         u = getBinding(v);
         if (u == null) return false;
         if (!(u instanceof Variable)) return true;  // Constant, Complex, PList
         v = (Variable)u;
      }
   }

   /**
    * getGroundTerm
    *
    * If a term is a grounded Variable, get the ground term.
    * Otherwise, return the non-variable.
    *
    * @param   term
    * @return  ground term
    */
   public Unifiable getGroundTerm(Unifiable term) {
      if (!(term instanceof Variable)) return term;
      Variable  v = (Variable)term;
      Unifiable u;
      while (true) {
         u = getBinding(v);
         if (u == null) return v;
         if (!(u instanceof Variable)) return u;
         v = (Variable)u;
      }
   }

   public String toString() { return "Bindings: (" + bindings + ")"; }

}

