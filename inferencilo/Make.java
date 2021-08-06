/**
 * Make
 *
 * Factory for generating Unifiable terms and operands from strings.
 *
 * Use thusly:
 *    Make.term("[1, 2, 3]")
 *    Make.and("mother($X, $Z), mother($Z, $Y)")
 *
 * About Escapes
 *
 * In Inferencilo, the vertical bar | acts as an escape character.
 * In a text source file, this is possible:
 *
 *  $X = Cleve Lendon.
 *
 * "Cleve Lendon" becomes a Constant. Unfortunately the following
 * wouldn't work:
 *
 *  $X = Cleve (Klivo) Lendon.
 *
 * ... because the parentheses are interpreted by the parser as part
 * of a complex term. It causes a parsing error. Such characters can
 * be escaped by a vertical bar as follows:
 *
 *  $X = Cleve |(Klivo|) Lendon.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.List;
import java.util.ArrayList;

public class Make {

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

      int parenthesis1 = specialIndexOf(s, "(");
      int parenthesis2 = specialLastIndexOf(s, ")");
      int bracket1     = specialIndexOf(s, "[");
      int bracket2     = specialLastIndexOf(s, "]");

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

      String s2 = cleanEscapes(s);
      return Constant.inst(s2);

   }  // term()


   /*
    * specialIndexOf
    *
    * This method returns the first index of the given character,
    * but only if it is not preceded by an escape character (|).
    *
    * @param  string to search
    * @param  string to match
    * @return index if found, or -1
    */
   private static int specialIndexOf(String search, String match) {
      int index = search.indexOf(match);
      if (index == -1) return -1;
      if (index > 0) {
         char ch = search.charAt(index - 1);
         if (ch == '|') return -1;
      }
      return index;
   }


   /*
    * specialLastIndexOf
    *
    * This method returns the last index of the given character,
    * but only if it is not preceded by an escape character (|).
    *
    * @param  string to search
    * @param  string to match
    * @return index if found, or -1
    */
   private static int specialLastIndexOf(String search, String match) {
      int index = search.lastIndexOf(match);
      if (index == -1) return -1;
      if (index > 0) {
         char ch = search.charAt(index - 1);
         if (ch == '|') return -1;
      }
      return index;
   }

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
      else if (functor.equals("exclude")) {
         return new Exclude(contents);
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
      String s2;

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
         else if (ch == '|') i++;   // For character escapes: |,  |(
         else if (ch == separator && roundDepth == 0 && squareDepth == 0) {
            s2 = s.substring(startIndex, i);
            terms.add(cleanEscapes(s2));
            startIndex = i + 1;
         }
      }
      if (s.length() - startIndex > 0) {
         s2 = s.substring(startIndex, s.length());
         terms.add(cleanEscapes(s2));
      }

      return terms;

   } // splitTerms


   /*
    * cleanEscapes
    *
    * Cleans the escape characters (vertical bar, |) from a string.
    * Double bars are replaced by a single bar.
    *
    * @param  input string
    * @return output string
    */
   public static String cleanEscapes(String in) {
      int i = 0;
      int len = in.length();
      StringBuilder sb = new StringBuilder("");
      String sub;
      while (i < len) {
         int index = in.indexOf("|", i);
         if (index == -1) index = len;
         if (index < len - 1) {
            char ch = in.charAt(index + 1);
            if (escapable(ch)) {
               sub = in.substring(i, index);
            }
            else {
               sub = in.substring(i, index + 1);
            }
            index++;
         }
         else {
            sub = in.substring(i, index);
         }
         sb.append(sub);
         i = index;
      }
      return sb.toString();
   } // cleanEscapes


   /*
    * escapable
    *
    * Returns true if the given character can be escaped
    * by a vertical bar, for example:  |, |(
    *
    * @param  character
    * @return t/f
    */
    private static boolean escapable(char c) {
       if (c == ',') return true;
       if (c == '(') return true;
       if (c == ')') return true;
       if (c == '[') return true;
       if (c == ']') return true;
       if (c == '.') return true;
       return false;
    }


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
      int first = specialIndexOf(str, "(");
      if (first < 1) return null;
      int second = specialIndexOf(str, ")");
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
