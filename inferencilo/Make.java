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
    * @return  true or false
    * @throws  FatalParsingException
    */
   private static boolean isComplex(String str) {
      if (str.length() < 3) return false;
      int parenthesis1 = str.indexOf("(");
      if (parenthesis1 < 1) return false; // Must find (, not first character.
      int bracket1 = str.indexOf("[");
      if (bracket1 != -1 && bracket1 < parenthesis1) return false;
      int parenthesis2 = str.lastIndexOf(")");
      if (parenthesis2 > parenthesis1 + 1) return true;
      throw new FatalParsingException("Invalid: " + str);
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
         else return Variable.inst(s);
      }

      int parenthesis1 = s.indexOf("(");
      int parenthesis2 = s.lastIndexOf(")");
      int bracket1     = s.indexOf("[");
      int bracket2     = s.lastIndexOf("]");

      // Complex term, eg:   sentence(subject, verb, object)
      if (parenthesis1 == 0) throw new InvalidComplexTermException(s);

      // Consider:  analyze(a, b, [1,2,3]) vs. [a, b, analyze(1,2,3), d]
      else if (parenthesis1 > 0 && (bracket1 == -1 || parenthesis1 < bracket1)) {
         if (parenthesis2 > parenthesis1) {  // if OK.
            String[] arr = splitComplex(s, parenthesis1, parenthesis2);
            return complexOrFunction(arr[0], arr[1]);
         }
         else throw new InvalidComplexTermException(s);
      }  // Complex terms

      // List (PList), eg:   [a, b, c]
      if (bracket1 >= 0) {
         if (bracket2 > bracket1) {
            return PList.parse(s);
         }
         throw new InvalidListException(s);
      }  // List

      return Constant.inst(s);

   }  // term()


   /**
    * subgoal
    *
    * This function accepts a string which represents a subgoal, and
    * creates its corresponding Goal object. At present, it parses
    * complex terms, the Unify operator (=), the Cut (!), and others.
    *
    * The Not operator must be dealt with first, because it encloses
    * a subgoal. Eg.
    *
    *    not($X = $Y)
    *
    * @param  subgoal as String
    * @return subgoal as Goal object
    * @throws FatalParsingException
    */
   public static Goal subgoal(String subgoal) {

      String s = subgoal.trim();

      // Parse not() goals first.
      if (s.length() > 5) {
         String start = s.substring(0, 4).toLowerCase();
         String end = s.substring(s.length() - 1);
         if (start.equals("not(") && end.equals(")")) {
            String s2 = s.substring(4, s.length() - 1);
            return new Not(subgoal(s2));
         }
      }

      if (s.indexOf('=') > 0) {  // unify
         return new Unify(s);
      }
      else if (s.equals("!")) {  // cut
         return new Cut();
      }
      else if (s.equals("fail")) {
         return new Fail();
      }
      else if (s.equals("nl")) {  // new line
         return new NewLine();
      }
      else if (s.equals("check_time")) {
         return new CheckTime();
      }

      // complex terms, built-in functions
      String[] parsed = parseComplex(s);
      if (parsed == null) throw new FatalParsingException("Invalid term: " + s);
      String functor = parsed[0];
      String contents = parsed[1];

      if (functor.equals("append")) {
         return new Append(contents);
      }
      else if (functor.equals("functor")) {
         return new Functor(contents);
      }
      else if (functor.equals("print")) {
         return new Print(contents);
      }
      else if (functor.equals("print_list")) {
         return new PrintList(contents);
      }
      else if (functor.equals("greater_than")) {
         return new GreaterThan(contents);
      }
      else if (functor.equals("less_than")) {
         return new LessThan(contents);
      }
      else if (functor.equals("greater_than_or_equal")) {
         return new GreaterThanOrEqual(contents);
      }
      else if (functor.equals("less_than_or_equal")) {
         return new LessThanOrEqual(contents);
      }
      else if (functor.equals("count")) {
         return new Count(contents);
      }
      else if (functor.equals("stats")) {
         return new Stats();
      }
      else if (functor.equals("include")) {
         return new Include(contents);
      }

      return new Complex(s);

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
      return new And(strToGoals(terms));
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
      return new Or(strToGoals(terms));
   }


   /*
    * strToGoals
    *
    * Converts a list of terms (strings) to a list of subgoals.
    *
    * @param  list of terms
    * @return list of subgoals
    */
   private static List<Goal> strToGoals(List<String> terms) {
      List<Goal> arguments = new ArrayList<Goal>();
      for (String term : terms) {
         Goal subgoal = subgoal(term);
         if (subgoal != null) arguments.add(subgoal);
      }
      return arguments;
   } // strToGoals


   /**
    * splitTerms
    *
    * Divides a comma separated (or semicolon separated) string
    * of terms (subgoals) into separate strings. For example, the
    * string "one, [two, three], four" will be divided into:
    * "one", "[two, three]", "four".
    *
    * This method will be used to create arguments for operators
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
    * term or built-in predicates, such as:
    *
    *    "father(Philip, Alize)"
    *    "print(Hello world!)"
    *
    * and returns the functor and contents in an array of strings.
    *
    *   ["father", "Philip, Alize"]
    *   ["print", "Hello world!"]
    *
    * If the complex term or predicate is invalid (missing a brace),
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

      return splitComplex(str, first, second);

   } // parseComplex


   /*
    * splitComplex
    *
    * This method splits a string representation of a complex
    * term into its functor and terms. For example, if the
    * complex term is:
    *
    *    "father(Philip, Alize)"
    *
    * and the indices (index1, index2) are 6 and 20, the function
    * will return this array:
    *
    *   ["father", "Philip, Alize"]
    *
    * This method assumes that index1 and index2 are valid.
    *
    * @param  complex term (string)
    * @param  index1
    * @param  index2
    * @return array with functor and terms
    */
   private static String[] splitComplex(String comp, int index1, int index2) {

      String functor = comp.substring(0, index1);
      String terms = comp.substring(index1 + 1, index2);
      String[] arr = new String[2];
      arr[0] = functor;
      arr[1] = terms;
      return arr;

   } // splitComplex


   /*
    * complexOrFunction
    *
    * Makes a complex term or a function.
    *
    * @param  functor
    * @param  arguments
    * @return complex term or function
    */
   private static Unifiable complexOrFunction(String functor,
                                              String arguments) {

      if (functor.equals("add")) return new Add(arguments);
      if (functor.equals("subtract")) return new Subtract(arguments);
      if (functor.equals("join")) return new Join(arguments);
      return new Complex(functor, arguments);

   } // complexOrFunction

} // Make
