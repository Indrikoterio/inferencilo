/**
 * SubstitutionSet
 *
 * The substitution set is an array of bindings of logic variables.
 * Each logic variable has a unique ID, which is used as an index into
 * the substitution set. If a binding exists, the indexed item is a
 * unifiable term. If there is no binding, the indexed item is null.
 *
 * As the inference engine searches for a solution, it adds logic
 * variable bindings to the SubstitutionSet. Because the SubstitutionSet
 * contains all variable bindings, it can be thought of as the solution
 * (partial or final).
 *
 * This class also has methods to get the ground term of a logic
 * variable, and cast it as a Constant, Complex term, or SLinkedList.
 * See castConstant(), castComplex(), castSLinkedList().
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class SubstitutionSet {

   private Unifiable[] bindings;

   /**
    * constructor
    */
   public SubstitutionSet() { 
      bindings = new Unifiable[10];
   }

   /**
    * constructor
    *
    * @param  substitution set
    */
   public SubstitutionSet(SubstitutionSet ss) {
      int len = ss.bindings.length;
      bindings = new Unifiable[len + 1];
      System.arraycopy(ss.bindings, 0, bindings, 0, len);
   }

   /**
    * add
    *
    * Binds a variable to an expression.
    * The variable becomes the key the binding dictionary (hash).
    *
    * @param  LogicVar
    * @param  Unifiable expression
    * @throws AlreadyBoundException
    */
   public void add(LogicVar v, Unifiable e) throws AlreadyBoundException {
      int id = v.id();
      if (id == 0) { // This should not happen.
         throw new InvalidVariableException(v.toString());
      }
      if (isBound(v)) {
         throw new AlreadyBoundException();
      }
      else {
         int len = bindings.length;
         if (id >= len) {
            Unifiable[] newBindings = new Unifiable[id + 16];
            System.arraycopy(bindings, 0, newBindings, 0, len);
            bindings = newBindings;
         }
         bindings[id] = e;
      }
   }

   /**
    * getBinding
    *
    * @param   variable
    * @return  unifiable
    */
   public Unifiable getBinding(LogicVar v) {
      int id = v.id();
      if (id >= bindings.length) return null;
      return (Unifiable)bindings[id];
   }

   /**
    * isBound
    *
    * A logic variable is bound if there exists an entry
    * for it in the substitution set.
    *
    * @param   logic variable
    * @return  t/f
    */
   public boolean isBound(LogicVar v) {
      int id = v.id();
      if (id >= bindings.length) return false;
      return (bindings[id] != null);
   }


   /**
    * isGround
    *
    * "Terms not containing variables are called Ground."
    *
    * A logic variable is ground if it is ultimately bound
    * to something other than a logic variable.
    *
    * @param   logic variable
    * @return  t/f
    */
   public boolean isGround(LogicVar var) {
      LogicVar  v = var;
      Unifiable u;
      while (true) {
         u = (Unifiable)bindings[v.id()];
         if (u == null) return false;
         if (!(u instanceof LogicVar)) return true;  // Constant, Complex, SLinkedList
         v = (LogicVar)u;
      }
   }

   /**
    * getGroundTerm
    *
    * If a term is a grounded variable, get the ground term.
    * Otherwise, return the variable.
    *
    * @param   term
    * @return  ground term
    */
   public Unifiable getGroundTerm(Unifiable term) {
      if (!(term instanceof LogicVar)) return term;
      LogicVar  v = (LogicVar)term;
      Unifiable u;
      while (true) {
         u = (Unifiable)bindings[v.id()];
         if (u == null) return v;
         if (!(u instanceof LogicVar)) return u;
         v = (LogicVar)u;
      }
   }


   /**
    * getGroundTermOrNull
    *
    * If a logic variable is grounded, get the ground term.
    * Otherwise, return null.
    *
    * @param   logic variable  (Known to be a logic variable.)
    * @return  ground term or null
    */
   public Unifiable getGroundTermOrNull(LogicVar var) {
      LogicVar  v = var;
      Unifiable u;
      while (true) {
         u = (Unifiable)bindings[v.id()];
         if (u == null) return null;
         if (!(u instanceof LogicVar)) return u;
         v = (LogicVar)u;
      }
   }

   /**
    * toString()
    *
    * This method formats a list of bindings.
    * It can be useful for debugging.
    *
    * @return  bindings (as string)
    */
   public String toString() {
      StringBuilder sb = new StringBuilder("Bindings:\n");
      int count = 0;
      for (Unifiable u : bindings) {
         sb.append(" " + count + "  " + u + "\n");
         count++;
      }
      return sb.toString();
   }

   /**
    * castConstant
    *
    * If the given Unifiable is an instance of Constant, cast it
    * as Constant and return it. Otherwise return null.
    *
    * @param  Unifiable term
    * @return Constant term or null
    */
   public Constant castConstant(Unifiable term) {
      if (term instanceof Constant) return (Constant)term;
      Unifiable outTerm = null;
      if (term instanceof LogicVar) {
         outTerm = getGroundTermOrNull((LogicVar)term);
         if (outTerm == null) return null;
      }
      if (outTerm instanceof Constant) return (Constant)outTerm;
      return null;
   } // castConstant()


   /**
    * castComplex
    *
    * If the given Unifiable is an instance of Complex, cast it
    * as complex. Otherwise return null.
    *
    * @param  Unifiable term
    * @return Complex term or null
    */
   public Complex castComplex(Unifiable term) {
      if (term instanceof Complex) return (Complex)term;
      Unifiable outTerm = null;
      if (term instanceof LogicVar) {
         outTerm = getGroundTermOrNull((LogicVar)term);
         if (outTerm == null) return null;
      }
      if (outTerm instanceof Complex) return (Complex)outTerm;
      return null;
   } // castComplex()


   /**
    * castSLinkedList
    *
    * If the given Unifiable is an instance of SLinkedList, cast it
    * as SLinkedList and return it. If it is a LogicVar, get the term
    * which it is bound to. If that term is a SLinkedList, cast it as
    * a SLinkedList and return it. Otherwise return null.
    *
    * @param  unifiable term
    * @param  linked list or null
    */
   public SLinkedList castSLinkedList(Unifiable term) {
      if (term instanceof SLinkedList) return (SLinkedList)term;
      Unifiable outTerm = null;
      if (term instanceof LogicVar) {
         outTerm = getGroundTermOrNull((LogicVar)term);
         if (outTerm == null) return null;
      }
      if (outTerm instanceof SLinkedList) return (SLinkedList)outTerm;
      return null;
   } // castSLinkedList()

} // SubstitutionSet

