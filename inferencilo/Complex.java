/**
 * Complex
 *
 * Note: 'Complex terms' are also called 'compound terms'.
 * There doesn't seem to be a difference in meaning.
 *
 * This class represents a complex term, which consists of
 * a functor plus one or more arguments (unifiable terms).
 * Format:  functor(argument1, argument2)
 * Example: owns(john, house), owns($X, house)
 *
 * Also note: In this inference engine, a variable is defined
 * with a $ sign: owner($X, house)
 * This is to allow for upper case constants: owns(John, house).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Complex implements Unifiable, Goal {

   private String functor;
   private Unifiable[] terms;

   /**
    * constructor
    *
    * The first argument (functor) must be a Constant.
    *
    * @param  unifiable arguments
    * @throws InvalidFunctorException
    */
   public Complex(Unifiable... args) throws InvalidFunctorException {
      if (!(args[0] instanceof Constant)) {
         throw new InvalidFunctorException("" + args[0]);
      }
      terms = args;
      functor = "" + terms[0];
   }


   /**
    * constructor
    *
    * Creates a complex term from a string representation. The first
    * parameter is the functor, and the second is a list of terms.
    * For example, "boss(Susan, Jackie)" can be instantiated with:
    *
    *     new Complex("boss", "Susan, Jackie")
    *
    * Important:
    * To make an argument with a comma, use a (double) backslash
    * Eg.:  new Complex("comma", "\\,")
    *
    * @param  strFunctor
    * @param  strTerms
    */
   public Complex(String strFunctor, String strTerms) {

      // If there are no arguments. Eg.: rainy
      if (strTerms == null || strTerms.length() == 0) {
         terms = new Unifiable[1];
         // The functor is a constant, and must be unified,
         // like any other term.
         terms[0] = new Constant(strFunctor);
         functor = strFunctor;
         return;
      }

      List<String> listTerms = Make.splitTerms(strTerms, ',');
      terms = new Unifiable[listTerms.size() + 1];
      functor = strFunctor;
      terms[0] = new Constant(functor);

      int index = 1;
      for (String t : listTerms) {
         Make.addTerm(t, terms, index++);
      }

   } // constructor


   /**
    * constructor
    *
    * Creates a complex term from a string representation.
    * Eg. new Complex("boss(Susan, Jackie)")
    *
    * Important: To make an argument with a comma, use a backslash
    * to escape the comma. Eg.:  new Complex("comma(\,)")
    *
    * @param  complex term as string
    * @throws InvalidFunctorException, InvalidComplexTermException
    */
   public Complex(String str) throws InvalidFunctorException,
                                     InvalidComplexTermException {
      String s = str.trim();
      if (s.length() < 1) throw new InvalidFunctorException("Complex constructor.");
      char first = s.charAt(0);
      if (first == '$') throw new InvalidFunctorException(s);

      int parenthesis1 = s.indexOf('(');
      int parenthesis2 = s.lastIndexOf(')');

      // If there are no arguments. Eg.: rainy
      if (parenthesis1 == -1 && parenthesis2 == -1) {
         terms = new Unifiable[1];
         terms[0] = new Constant(s);
         functor = s;
         return;
      }

      if (parenthesis1 < 1 || parenthesis2 < parenthesis1) {
         throw new InvalidComplexTermException(s);
      }

      String arguments = s.substring(parenthesis1 + 1, parenthesis2);
      functor = s.substring(0, parenthesis1);

      if (arguments.length() == 0) {
         terms = new Unifiable[1];
         terms[0] = new Constant(functor);
         return;
      }

      List<String> strTerms = Make.splitTerms(arguments, ',');
      terms = new Unifiable[strTerms.size() + 1];
      terms[0] = new Constant(functor);

      int index = 1;
      for (String strTerm : strTerms) {
         Make.addTerm(strTerm, terms, index++);
      }

   } // constructor


   /**
    * key
    *
    * The knowledge base is keyed by predicate name (functor + arity).
    * Eg.: father(john, mary) =>  father/2
    *
    * @return predicate name
    */
   public String key() { return functor() + "/" + arity(); }


   /**
    * toString()
    *
    * Produces the Prolog form for readability.
    * Eg. "boss(carl, jim)"
    *
    * @return  Prolog format (String).
    */
   public String toString() {

      if (terms.length < 1) return "null";

      String s = null;
      String functor = null;

      for (Unifiable p : terms) {
         if (functor == null) functor = p.toString();
         else {
            if (s == null) s = p.toString();
            else s += ", " + p;
         }
      }
      if (s == null) return functor;
      return functor + "(" + s + ")";
   }

   /**
    * length
    *
    * Returns the number of terms, including the functor.
    *
    * @return number of terms
    */
   public int length() { return terms.length; }


   /**
    * arity
    *
    * terms = [functor, term1, term2, term3]
    * Arity = number of terms - 1
    *
    * @return  arity
    */
   public int arity() { return terms.length - 1; }


   /**
    * functor
    *
    * The functor is the first term: [functor, term1, term2, term3]
    *
    * @return  functor as string
    */
   public String functor() { return functor; }

   /**
    * getTerm
    *
    * Returns a term according to index.
    *
    * @param  index into terms
    * @return a unifiable term
    */
   public Unifiable getTerm(int index) { return terms[index]; }

   /**
    * getTerms
    *
    * Returns all terms as an array of Unifiable.
    *
    * @return array of terms
    */
   public Unifiable[] getTerms() { return terms; }

   /**
    * copyTerms
    *
    * Make a shallow copy of the array of terms.
    *
    * @return  array of unifiable terms
    */
   public Unifiable[] copyTerms() {
      Unifiable[] newTerms = new Unifiable[terms.length];
      for (int i = 0; i < terms.length; i++) { newTerms[i] = terms[i]; }
      return newTerms;
   }

   /**
    * equals
    *
    * Compares the indicated term with the given string.
    *
    * @param  index of term
    * @param  string to compare
    */
   public boolean equals(int index, String str) {
      if (index >= terms.length) return false;
      String str2 = "" + terms[index];
      return str.equals(str2);
   }


   /**
    * unify
    *
    * Unify this predicate with another.
    *
    * @param  other unifiable
    * @param  substitution set
    * @return new substitution set
    */
   public SubstitutionSet unify(Unifiable other, SubstitutionSet ss) {

      if (other instanceof Complex) {

         Complex comp = (Complex)other;

         if (terms.length != comp.length()) return null;  // Must be same size to unify.
         SubstitutionSet newSS = new SubstitutionSet(ss);

         for (int i = 0; i < this.length(); i++) {
            Unifiable termA = this.getTerm(i);
            Unifiable termB = comp.getTerm(i);
            if (termA == null || termB == null) continue;
            if (termA instanceof Anon || termB instanceof Anon) continue;
            newSS = termA.unify(termB, newSS);
            if (newSS == null) return null;
         }
         return newSS;
      }

      else if (other instanceof Variable) return other.unify(this, ss);
      else if (other instanceof Anon) return ss;
      return null;
   }

   /**
    * replaceVariables
    *
    * Replaces bound Variables with their Constants in order to
    * display results.
    *
    * @param   substitution set
    * @return  new expression, without variables
    */
   public Expression replaceVariables(SubstitutionSet ss) {
      Unifiable[] newTerms = new Unifiable[terms.length];
      for (int i = 0; i < terms.length; i++) {
         newTerms[i] = (Unifiable)terms[i].replaceVariables(ss);
      }
      return new Complex(newTerms);
   }

   /**
    * standardizeVariablesApart()
    *
    * Creates unique variables when the inference engine tries to solve a goal.
    *
    * @param   hash table previously standardized variables
    * @return  expression with standardized variables
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newTerms = new Unifiable[terms.length];
      // Create an array for new terms.
      for (int i = 0; i < length(); i++) {
         Unifiable term = (Unifiable)terms[i];
         if (term == null) continue;
         newTerms[i] = (Unifiable)term.standardizeVariablesApart(newVars);
         // Only variables will be affected.
      }
      return new Complex(newTerms);
   }

   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    * This method satisfies the goal interface.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new ComplexSolutionNode(this, knowledge, parentSolution, parentNode);
   }

}  // Complex
