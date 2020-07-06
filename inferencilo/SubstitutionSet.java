/**
 * SubstitutionSet
 *
 * The substitution set is a dictionary (aka hashmap) of bindings
 * of logic Variables.
 *
 * As the inference engine searches for a solution, it adds Variable
 * bindings to the SubstitutionSet. Because the SubstitutionSet
 * contains all Variable bindings, it can be thought of as the
 * solution (partial or final).
 *
 * This class also has methods to get the ground term of a
 * Variable, and cast it as a Constant, Complex term, or PList.
 * See castConstant(), castComplex(), castPList().
 *
 * @author  Cleve (Klivo) Lendon
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
    * @param  Variable
    * @param  Unifiable expression
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


   /**
    * castConstant
    *
    * If the given unifiable is an instance of Constant, cast it
    * as Constant and return it. Otherwise return null.
    *
    * @param  Unifiable term
    * @return Constant term or null
    */
   public Constant castConstant(Unifiable term) {
      if (term instanceof Constant) return (Constant)term;
      Unifiable outTerm = null;
      if (term instanceof Variable) {
         if (isGround((Variable)term)) {
            outTerm = getGroundTerm((Variable)term);
         }
         else return null;
      }
      if (outTerm instanceof Constant) return (Constant)outTerm;
      return null;
   } // castConstant()


   /**
    * castComplex
    *
    * If the given unifiable is an instance of Complex, cast it
    * as complex. Otherwise return null.
    *
    * @param  Unifiable term
    * @return Complex term or null
    */
   public Complex castComplex(Unifiable term) {
      if (term instanceof Complex) return (Complex)term;
      Unifiable outTerm = null;
      if (term instanceof Variable) {
         if (isGround((Variable)term)) {
            outTerm = getGroundTerm((Variable)term);
         }
         else return null;
      }
      if (outTerm instanceof Complex) return (Complex)outTerm;
      return null;
   } // castComplex()


   /**
    * castPList
    *
    * If the given unifiable is an instance of PList, cast it
    * as PList and return it. If it is a Variable, get the term
    * which it is bound to. If that term is a PList, cast it as
    * a PList and return it. Otherwise return null.
    *
    * @param  unifiable term
    * @param  plist or null
    */
   public PList castPList(Unifiable term) {
      if (term instanceof PList) return (PList)term;
      Unifiable outTerm = null;
      if (term instanceof Variable) {
         if (isGround((Variable)term)) {
            outTerm = getGroundTerm((Variable)term);
         }
         else return null;
      }
      if (outTerm instanceof PList) return (PList)outTerm;
      return null;
   } // castPList()

} // SubstitutionSet

