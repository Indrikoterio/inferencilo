/**
 * Make
 *
 * Factory for generating Unifiable terms and operands from strings.
 * Use thusly:
 *    Make.term("[1, 2, 3]")
 *    Make.and("mother($X, $Z), mother($Z, $Y)")
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.List;
import java.util.ArrayList;

public class Make {

   /*
    * isList
    *
    * Determines whether the string represents a proper list. eg.:
    *   [subject(pronoun), verb, object(noun)]
    * It prints out an error message if the list is invalid, eg:  [1, 2, 3
    *
    * @param   string representing a Prolog list
    * @return    true or false
    */
   private static boolean isList(String str) {
      if (str.length() < 2) return false;
      int bracket1 = str.indexOf("[");
      if (bracket1 == -1) return false;
      int parenthesis1 = str.indexOf("(");
      if (parenthesis1 != -1 && parenthesis1 < bracket1) return false;
      int bracket2 = str.lastIndexOf("]");
      if (bracket2 > bracket1) return true;
      throw new FatalParsingException("Invalid: " + str);
   }


   /*
    * isComplex
    *
    * Determines whether the string represents a complex term. eg.:
    *   member(Chris, Taylor)
    *
    * @param   string representing a complex term
    * @return    true or false
    */
   private static boolean isComplex(String str) {
      if (str.length() < 4) return false;
      int parenthesis1 = str.indexOf("(");
      if (parenthesis1 < 1) return false; // Must find (, not first character.
      int bracket1 = str.indexOf("[");
      if (bracket1 != -1 && bracket1 < parenthesis1) return false;
      int parenthesis2 = str.lastIndexOf(")");
      if (parenthesis2 > parenthesis1 + 1) return true;
      throw new FatalParsingException("Invalid: " + str);
   }

   /**
    * addTerm
    *
    * Takes a string representation of a term (constant, variable, list, complex term),
    * converts it to an object, and adds it to the terms array at the given index.
    *
    * @param term as String
    * @param unifiable terms (array)
    * @param index to terms
    */
   public static void addTerm(String t, Unifiable[] terms, int index) {
      String strTerm = t.trim();
      int len = strTerm.length();
      if (len > 1 && strTerm.startsWith("$")) {
         if (strTerm.charAt(1) == '_') {     // Anonymous variable $_
            terms[index] = Anon.anon;
         }
         else {
            terms[index] = VarCache.get(strTerm);
         }
      }
      else if (isComplex(strTerm)) {
         terms[index] = new Complex(strTerm);
      }
      else if (isList(strTerm)) {
         terms[index] = PList.make(strTerm);
      }
      else {
         terms[index] = new Constant(strTerm);
      }
   }


   /**
    * term
    *
    * A factory method to produce a unifiable term from a string.
    *
    * @param   string representing term, eg.: "noun", "$X", "[1, 2, 3]"
    * @return  Unifiable object, Constant, Variable, PList etc.
    * @throws  InvalidComplexTermException, InvalidListException
    */
   public static Unifiable term(String str) {

      Unifiable[] terms;

      String s = str.trim();
      int len = s.length();
      if (len == 0) return null;

      // Return a variable.
      if (s.startsWith("$") && len > 1) {
         if (s.charAt(1) == '_') return Anon.anon;
         else return VarCache.get(s);
      }

      // Escape for commas: \,
      if (s.startsWith("\\") && len > 1) {
         if (s.charAt(1) == ',') return new Constant(",");
         else return new Constant(s);
      }

      int parenthesis1 = s.indexOf("(");
      int parenthesis2 = s.lastIndexOf(")");

      int bracket1 = s.indexOf("[");
      int bracket2 = s.lastIndexOf("]");

      // Complex term, eg:   sentence(subject, verb, object)
      if (parenthesis1 == 0) throw new InvalidComplexTermException(s);

      // Consider:  functor(a, b, [1,2,3]) vs. [a, b, c(1,2,3), d]
      else if (parenthesis1 > 0 && (bracket1 == -1 || parenthesis1 < bracket1)) {
         if (parenthesis2 > parenthesis1) {  // if OK.
            return new Complex(s);
         }
         else throw new InvalidComplexTermException(s);
      }  // Complex terms

      // List (PList), eg:   [a, b, c]
      if (bracket1 >= 0) {
         if (bracket2 > bracket1) {
            return PList.make(s);
         }
         throw new InvalidListException(s);
      }  // List

      return new Constant(s);

   }  // term()


   /**
    * subgoal
    *
    * This function accepts a string which represents a subgoal,
    * and creates its corresponding Goal object. At present, it
    * recognizes complex terms, the Unify operator, the Not
    * operator and the Cut. That is:
    *
    *    symptom(influenza, fever)
    *    $X = $Y
    *    not($X = $Y)
    *    !
    *
    * @param  subgoal as String
    * @return subgoal as Goal object
    * @throws FatalParsingException
    */
   public static Goal subgoal(String subgoal) {
      String s = subgoal.trim();
      if (s.indexOf('=') > 0) {  // unify
         return new Unify(s);
      }
      else if (s.equals("!")) {  // cut
         return new Cut();
      }
      else { // complex terms, built-in functions

         String[] parsed = parseComplex(s);
         if (parsed == null) throw new FatalParsingException("Invalid term: " + s);
         String functor = parsed[0];
         String contents = parsed[1];

         if (functor.equals("append")) {
            return new Append(contents);
         }
         else if (functor.equals("print")) {
            return new Print(new Constant(contents));
         }
         else if (functor.equals("not")) {
            return new Not((Goal)term(contents));
         }
         return new Complex(s);
      }
   } // subgoal



   /**
    * and
    *
    * A factory method to produce a logical And operator from a string.
    * In Prolog, logical And is represented by a comma in the body of
    * a rule. Eg. "article('The'), adj(A), noun(N), verb(V)"
    *
    * @param  string form
    * @return And operator
    */
   public static And and(String str) {
      List<String> terms = splitTerms(str, ',');
      ArrayList<Goal> operands = new ArrayList<Goal>();
      for (String term : terms) {
         Goal subgoal = subgoal(term);
         if (subgoal != null) operands.add(subgoal);
      }
      return new And(operands);
   }

   /**
    * or
    *
    * A factory method to produce a logical Or operator from a string.
    * In Prolog, logical Or is represented by a semicolon in the body of
    * a rule. Eg. "father(richard, X); mother(mary, X)"
    *
    * @param  string form
    * @return Or operator
    */
   public static Or or(String str) {
      List<String> terms = splitTerms(str, ';');
      ArrayList<Goal> operands = new ArrayList<Goal>();
      for (String term : terms) {
         Goal subgoal = subgoal(term);
         if (subgoal != null) operands.add(subgoal);
      }
      return new Or(operands);
   }


   /**
    * addOperand
    *
    * This function creates a subgoal from a string, then adds
    * it to the list of operands.
    *
    * @param  subgoal as string
    * @param  operand list
    */
   private static void addOperand(String str, ArrayList<Goal> operands) {
      Goal subgoal = subgoal(str);
      if (subgoal != null) operands.add(subgoal);
   } // addOperand


   /**
    * splitTerms
    *
    * Divides a comma separated (or semicolon separated) string
    * of terms (subgoals) into separate strings. For example, the
    * string "one, [two, three], four" will be divided into:
    * "one", "[two, three]", "four".
    *
    * This method will be used to create operands for operators
    * such as And and Or, and terms for Complex terms.
    *
    * @param  string of terms
    * @param  separator (comma or semicolon)
    * @return list of strings
    * @throws FatalParsingException
    */
   public static ArrayList<String> splitTerms(String str, char separator) {

      String s = str.trim();
      if (s.length() < 1) throw new FatalParsingException("splitTerms - zero length");
      ArrayList<String> terms = new ArrayList<String>();

      int startIndex = 0;
      int roundDepth = 0;   // depth of round parenthesis (())
      int squareDepth = 0;   // depth of square brackets [[]]

      // Find comma, if there is one.
      for (int i = startIndex; i < s.length(); i++) {
         char ch = s.charAt(i);
         if (ch == '[') squareDepth++;
         else if (ch == ']') squareDepth--;
         else if (ch == '(') roundDepth++;
         else if (ch == ')') roundDepth--;
         else if (ch == '\\') i++;   // For comma escapes, eg. \,
         else if (ch == separator && roundDepth == 0 && squareDepth == 0) {
            terms.add(s.substring(startIndex, i));
            startIndex = i + 1;
         }
      }
      if (s.length() - startIndex > 0) {
         terms.add(s.substring(startIndex, s.length()));
      }

      return terms;

   } // splitTerms


   /*
    * parseComplex
    *
    * This method takes the string representation of a complex
    * term or built in function, such as:
    *
    *    "father(Philip, Alize)"
    *    "print(Hello world!)"
    *
    * and returns the functor and contents in an array of strings.
    *
    *   ["father", "Philip, Alize"]
    *   ["print", "Hello world!"]
    *
    * If the complex term or function is invalid (missing a brace),
    * the function returns null.
    *
    * @param  string representation of term
    * @return functor and contents in string array
    */
   private static String[] parseComplex(String str) {

      int length = str.length();
      int first = str.indexOf("(");
      if (first < 1) return null;
      int second = str.indexOf(")");
      if (second < first) return null;

      String functor = str.substring(0, first);
      String contents = str.substring(first + 1, second);
      String[] arr = new String[2];
      arr[0] = functor;
      arr[1] = contents;
      return arr;

   } // parseComplex

} // Make
