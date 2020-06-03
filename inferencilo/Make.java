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
      System.out.println("Ooops. Invalid list: " + str);
      return false;
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
      System.out.println("Ooops. Invalid complex term: " + str);
      return false;
   }

   /*
    * isNot
    *
    * Determines whether the string represents the not operator.
    * Eg.:  not(father(albert, $X))
    *
    * @param   string representation of 'not'
    * @return  true or false
    */
   private static boolean isNot(String str) {
      String s + str.trim().toLower();
      if (s.startsWith("not(")) return true;
      return false;
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
    * countArguments
    *
    * @param list of comma separated arguments, eg. noun, conj, noun
    * @return  count  (eg. 3)
    */
   public static int countArguments(String args) {
      int len = args.length();
      if (len == 0) return 0;
      int startIndex = 0;
      int roundDepth = 0;   // depth of round parenthesis (())
      int squareDepth = 0;   // depth of square brackets [[]]
      int count = 0;
      for (int i = startIndex; i < len; i++) {
         char ch = args.charAt(i);
         if (ch == '[') squareDepth++;
         else if (ch == ']') squareDepth--;
         else if (ch == '(') roundDepth++;
         else if (ch == ')') roundDepth--;
         else if (ch == '\\') i++;   // For comma escapes, eg. \,
         else if (roundDepth == 0 && squareDepth == 0) {
            if (ch == ',') {
               count++;
            }
            else if (ch == '|') {
               count++;
               break;    // Must be last argument.
            }
         }
      }
      count++;
      return count;
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
    * and
    *
    * A factory method to produce a logical And operator from a string.
    * In Prolog, logical And is represented by a comma in the body of
    * a rule. Eg. "article('The'), adj(A), noun(N), verb(V)"
    *
    * Note: I don't intend to make a full parser at this time. Maybe later.
    *
    * @param  string form
    * @return And operator
    */
   public static And and(String str) {
      return new And(getOperands(str, ',', ";\"<>#@"));
   }

   /**
    * or
    *
    * A factory method to produce a logical Or operator from a string.
    * In Prolog, logical Or is represented by a semicolon in the body of
    * a rule. Eg. "father(richard, X); mother(mary, X)"
    *
    * @param  string form
    * @return And operator
    */
   public static Or or(String str) {
      return new Or(getOperands(str, ';', ",\"<>#@"));
   }


   /*
    * addOperand
    *
    * This function examines a string which represents an operand
    * (subgoal), creates its corresponding object, then adds it
    * to the list of operands.
    *
    * @param  operand (String)
    * @param  operand list
    */
   private static void addOperand(String subgoal, ArrayList<Goal> operands) {
      if (subgoal.indexOf('=') > 0) {
         operands.add(new Unify(subgoal));
      }
      else {
         operands.add(new Complex(subgoal));
      }
   } // addOperand


   /*
    * getOperands
    *
    * Collects operands for a logical operator from a string.
    * In Prolog, the logical operator And is represented by a comma,
    * and logical Or is represented by a semicolon in the body of
    * a rule. Eg.
    *    "article('The'), adj(A), noun(N), verb(V)"   (And)
    *    "father(richard, X); mother(mary, X)"   (Or)
    *
    * This method will parse the string to collect operands, which
    * are then used to create the operator (And/Or).
    *
    * @param  string form
    * @param  separator (char)
    * @param  invalid characters (String)
    * @return operands (goals)
    * @throws InvalidOperatorException
    */
   private static ArrayList<Goal> getOperands(String str, char separator, String invalid) {

      String s = str.trim();
      if (s.length() < 1) throw new InvalidOperatorException(s);
      ArrayList<Goal> operands = new ArrayList<Goal>();

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
         // Check for invalid chars between goals.
         else if (roundDepth == 0 &&
                  squareDepth == 0 && invalid.indexOf(ch) > -1)
                  throw new InvalidOperatorException(s);
         else if (ch == separator && roundDepth == 0 && squareDepth == 0) {
            String subgoal = s.substring(startIndex, i);
            addOperand(subgoal, operands);
            startIndex = i + 1;
         }
      }
      if (s.length() - startIndex > 0) {
         String subgoal = s.substring(startIndex, s.length());
         addOperand(subgoal, operands);
      }

      // For debugging.
      //for (Goal g : operands) System.out.println(g);
      return operands;

   } // getOperands

} // Make
