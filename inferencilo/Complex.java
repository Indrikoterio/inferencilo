/**
 * Complex (term)
 *
 * Note: 'Complex terms' are also called 'compound terms'. They are
 * sometimes called 'function terms'. Luger and Stubblefield call
 * them 'simple sentences'.
 *
 * This class represents a complex term, which consists of a functor
 * plus one or more arguments, which are unifiable terms.
 *
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
    * constructor 1
    *
    * The first argument (functor) must be a Constant.
    *
    * @param  unifiable arguments
    * @throws InvalidFunctorException
    */
   public Complex(Unifiable... args) throws InvalidFunctorException {
      if (!(args[0] instanceof Constant)) {
         throw new InvalidFunctorException("Complex constructor 1 - " + args[0]);
      }
      terms = args;
      functor = terms[0].toString();
   }


   /**
    * constructor 2
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
    * @throws InvalidFunctorException
    */
   public Complex(String strFunctor, String strTerms) {

      // Check the functor.
      if (strFunctor == null || strFunctor.length() == 0)
            throw new InvalidFunctorException("Complex constructor 2.");
      functor = strFunctor;

      if (strTerms == null || strTerms.length() == 0) {
         terms = new Unifiable[] { new Constant(strFunctor) };
         return;
      }

      String str = strFunctor + "," + strTerms;
      terms = Make.splitTerms(str, ',')
                  .stream()
                  .map(Make::term)
                  .toArray(Unifiable[]::new);

   } // constructor


   /**
    * constructor 3
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

      if (str == null) {
         throw new InvalidComplexTermException("Complex constructor 3 - null");
      }

      String s = str.trim();
      int strLength = s.length();
      if (strLength < 1) {
         throw new InvalidComplexTermException("Complex constructor 3 - empty");
      }

      char first = s.charAt(0);
      if (!Character.isLetter(first)) {
         throw new InvalidFunctorException("Complex constructor 3 - " + s);
      }

      int parenthesis1 = s.indexOf('(');
      int parenthesis2 = s.lastIndexOf(')');

      // If there are no arguments. Eg.: rainy
      if (parenthesis1 == -1 && parenthesis2 == -1) {
         // If last character is a period, remove it.
         char last = s.charAt(strLength - 1);
         if (last == '.') { s = s.substring(0, strLength - 1); }
         terms = new Unifiable[] { new Constant(s) };
         functor = s;
         return;
      }

      if (parenthesis1 == 0 || parenthesis2 < parenthesis1) {
         throw new InvalidComplexTermException(s);
      }

      functor = s.substring(0, parenthesis1);
      String arguments = s.substring(parenthesis1 + 1, parenthesis2);

      if (arguments.length() == 0) {
         terms = new Unifiable[] { new Constant(functor) } ;
         return;
      }

      String s2 = functor + "," + arguments;
      terms = Make.splitTerms(s2, ',')
                  .stream()
                  .map(Make::term)
                  .toArray(Unifiable[]::new);

   } // constructor

   /**
    * validate
    *
    * Checks whether the given string represents a valid Complex term.
    * Checks validity of parentheses and backticks.
    *
    * @param   complex term as string
    * @return  Error message or empty string.
    */
   public static String validate(String str) {

      if (str == null) { return "Null string."; }

      String s = str.trim();
      int strLength = s.length();
      if (strLength < 1) { return "Empty string."; }

      char first = s.charAt(0);
      if (!Character.isLetter(first)) {
         return "First character must be a letter.";
      }

      int left  = -1;
      int right = -1;
      int countLeft  = 0;
      int countRight = 0;
      int countBackticks = 0;

      for (int i = 0; i < strLength; i++) {
         char c1 = s.charAt(i);
         if (c1 == '`') {
            countBackticks++;
            for (int j = i + 1; j < strLength; j++) {
               int c2 = s.charAt(j);
               if (c2 == '`') {
                  countBackticks++;
                  i = j;
                  break;
               }
            }
         } else if (c1 == '(') {
            if (left == -1) { left = i; }
            countLeft++;
         } else if (c1 == ')') {
            right = i;
            countRight++;
         }
      } // for

      if (countBackticks % 2 != 0) { return "Unbalanced backticks."; }
      if (countLeft != countRight) { return "Unbalanced parentheses."; }

      return "";
   } // validate()


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
      String str2 = terms[index].toString();
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
         SubstitutionSet newSS = ss;

         for (int i = 0; i < this.length(); i++) {
            Unifiable termA = this.getTerm(i);
            Unifiable termB = comp.getTerm(i);
            if (termA == null || termB == null) continue;
            if (termA == Anon.anon || termB == Anon.anon) continue;
            newSS = termA.unify(termB, newSS);
            if (newSS == null) return null;
         }
         return newSS;
      }

      else if (other instanceof LogicVar) return other.unify(this, ss);
      else if (other == Anon.anon) return ss;
      return null;
   }

   /**
    * replaceVariables
    *
    * Replaces bound LogicVars with their Constants in order to
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
   public Expression standardizeVariablesApart(HashMap<String, LogicVar> newVars) {
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
