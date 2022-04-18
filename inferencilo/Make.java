/**
 * Make
 *
 * Factory for generating Unifiable terms and operands from strings.
 *
 * In Java, use thusly:
 *
 *    Make.term("[1, 2, 3]")
 *    Make.and("mother($X, $Z), mother($Z, $Y)")
 *
 * 'Make' is also used by the parser (see Tokenizer).
 *
 * About text strings:
 *
 * In Java, a String is delimited with double quotes, as above.
 * In a text file of Inferencilo facts and rules, text strings are
 * delimited with backticks (`). (Backticks are also known as
 * backquotes or grave accents.)
 *
 * Backticks are not always necessary. In the following subgoal,
 *
 *  $Name = Cleve Lendon.
 *
 * The Constant 'Cleve Lendon' is bound to $Name. The following
 * wouldn't work:
 *
 *  $Name = Cleve (Klivo) Lendon.
 *
 * ... because the parentheses are interpreted by the parser as part
 * of a complex term. It causes a parsing error.
 *
 * Putting backticks arround the string solves the problem.
 *
 *  $Name = `Cleve (Klivo) Lendon`.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Make {

   private static final int UNIFY = 1;  // Unify does unification
   private static final int EQUAL = 2;  // ==  No unification. Simply compares.
   private static final int GREATER_THAN = 3;
   private static final int LESS_THAN = 4;
   private static final int GREATER_THAN_OR_EQUAL = 5;
   private static final int LESS_THAN_OR_EQUAL = 6;

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

      char firstChar = s.charAt(0);

      // Return a variable.
      if (firstChar == '$' && len > 1) {
         if (s.charAt(1) == '_') return Anon.anon;
         else return Variable.inst(s);
      }

      // If a string begins and ends with a backtick, everything
      // inside becomes a Constant.
      if (firstChar == '`' && len > 1) {
         int index = s.indexOf("`", 1);
         if (index == -1) throw new UnmatchedBacktickException("Make.term()");
         else {
            String s2 = s.substring(1, index);
            return Constant.inst(s2);
         }
      }

      int parenthesis1 = s.indexOf("(");
      int parenthesis2 = s.indexOf(")");
      int bracket1     = s.indexOf("[");
      int bracket2     = s.indexOf("]");

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


   /*
    * findInfix
    *
    * findInfix() searches the given string for an infix (eg. <= > ==).
    * If it finds one, it returns the index of the infix. For example,
    *    $X < 6
    * ...has a 'less than' infix at index 3.
    *
    * @param  string to search
    * @return index if found, or -1
    */
   private static int findInfix(String search) {
      int len = search.length();
      int i;
      for (i = 0; i < len; i++) {
         char c1 = search.charAt(i);
         if (c1 == '`') {
            for (int j = i + 1; j < len; j++) {
               char c2 = search.charAt(j);
               if (c2 == '`') {
                  i = j; break;
               }
            } // for
         }
         else
         if (c1 == '(') {
            for (int j = i + 1; j < len; j++) {
               char c2 = search.charAt(j);
               if (c2 == ')') {
                  i = j; break;
               }
            } // for
         }
         else if (c1 == '<' || c1 == '>' || c1 == '=') {
             return i;
         }
      } // for
      return -1;
   } // findInfix

   /*
    * identifyInfix
    *
    * identifyInfix() determines the kind of infix at the given index.
    * For example, if the given string is "$X < 6" and the given index
    * is 3, the method will return LESS_THAN.
    *
    * @param  string
    * @param  index of infix
    * @return type of infix
    */
   private static int identifyInfix(String str, int index) {
      int length = str.length();
      char c1 = str.charAt(index);
      char c2 = ' ';
      if (c1 == '<') {
         if (index < length - 1) { c2 = str.charAt(index + 1); }
         if (c2 == '=') return LESS_THAN_OR_EQUAL;
         return LESS_THAN;
      }
      if (c1 == '>') {
         if (index < length - 1) { c2 = str.charAt(index + 1); }
         if (c2 == '=') return GREATER_THAN_OR_EQUAL;
         return GREATER_THAN;
      }
      if (c1 == '=') {
         if (index < length - 1) { c2 = str.charAt(index + 1); }
         if (c2 == '=') return EQUAL;
         return UNIFY;
      }
      return 0;
   } // identifyInfix()


   /*
    * getTwoTerms
    *
    * Divides a string representation of a comparison predicate
    * such as "$X >= 27" into two terms.
    *
    * @param  string of predicate
    * @param  index of comparison operator
    * @param  length of operator
    * @return array of strings
    */
   private static List<String> getTwoTerms(String str, int index, int length) {
      String s1 = str.substring(0, index);
      String s2 = str.substring(index + length, str.length());
      String args[] = new String[2];
      args[0] = s1;
      args[1] = s2;
      return Arrays.asList(args);
   }

   /**
    * subgoal
    *
    * This function accepts a string which represents a subgoal, and
    * creates its corresponding Goal object. At present, it parses
    * complex terms, the Unify operator (=), the Cut (!), and others.
    *
    * The Not and Time operators are dealt with first, because they
    * enclose subgoals. Eg.
    *
    *    not($X = $Y)
    *    time(qsort)
    *
    * @param  subgoal as String
    * @return subgoal as Goal object
    * @throws FatalParsingException
    */
   static String args[] = new String[2];
   public static Goal subgoal(String subgoal) {

      String s1, s2;
      String s = subgoal.trim();
      int len = s.length();
      int index;

      // Not and Time operators.
      if (len > 5) {
         String end = s.substring(len - 1);
         if (end.equals(")")) {
            if (s.startsWith("not(")) {
               s2 = s.substring(4, len - 1);
               return new Not(subgoal(s2));
            }
            if (s.startsWith("time(")) {
               s2 = s.substring(5, len - 1);
               return new Time(subgoal(s2));
            }
         }
      }

      if (s.equals("!")) {  // cut
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

      // Handle infixes: > <  >= <= ==
      index = findInfix(s);
      int typeOfInfix = 0;

      if (index > -1) {
         typeOfInfix = identifyInfix(s, index);
         if (typeOfInfix == UNIFY) {
            return new Unify(s);
         } else if (typeOfInfix == EQUAL) {
            List<String> lst = getTwoTerms(s, index, 2);
            return new Equal(lst);
         } else if (typeOfInfix == GREATER_THAN) {
            List<String> lst = getTwoTerms(s, index, 1);
            return new GreaterThan(lst);
         } else if (typeOfInfix == LESS_THAN) {
            List<String> lst = getTwoTerms(s, index, 1);
            return new LessThan(lst);
         } else if (typeOfInfix == GREATER_THAN_OR_EQUAL) {
            List<String> lst = getTwoTerms(s, index, 2);
            return new GreaterThanOrEqual(lst);
         } else if (typeOfInfix == LESS_THAN_OR_EQUAL) {
            List<String> lst = getTwoTerms(s, index, 2);
            return new LessThanOrEqual(lst);
         }
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
      else if (functor.equals("equal")) {
         return new Equal(contents);
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

      int startIndex  = 0;
      int roundDepth  = 0;   // depth of round parenthesis (())
      int squareDepth = 0;   // depth of square brackets [[]]

      // Find comma, if there is one.
      for (int i = startIndex; i < s.length(); i++) {
         char ch = s.charAt(i);
         if (ch == '[') squareDepth++;
         else if (ch == ']') squareDepth--;
         else if (ch == '(') roundDepth++;
         else if (ch == ')') roundDepth--;
         else if (ch == '`') {  // Backticks mark literal strings.
            int index = s.indexOf("`", i + 1);
            if (index == -1)
               throw new UnmatchedBacktickException("Make.splitTerms()");
            else i = index;
         }
         else if (ch == separator && roundDepth == 0 && squareDepth == 0) {
            s2 = s.substring(startIndex, i);
            terms.add(s2);
            startIndex = i + 1;
         }
      } // for
      if (s.length() - startIndex > 0) {
         s2 = s.substring(startIndex, s.length());
         terms.add(s2);
      }

      return terms;

   } // splitTerms


   /*
    * cleanEscapes
    *
    * Cleans the escape characters (backtick, `) from a string.
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
         int index = in.indexOf("`", i);
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
    * by a backtick, for example:  `, `(
    *
    * @param  character
    * @return t/f
    */
    private static boolean escapable(char c) {
       if (c >= ' ' && c <= '/') return true;
       if (c >= ':' && c <= '?') return true;
       if (c >= '[' && c <= '_') return true;
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
      int first = str.indexOf("(");
      if (first < 1) return null;
      int second = str.lastIndexOf(")");
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


   /**
    * goal
    *
    * A goal is the same as a complex term, but it is necessary to ensure
    * that the global variable id is reset to 0 and that all variable ids
    * are standardized, before querying the knowledgebase.
    *
    * @param  arguments
    * @return goal as complex term
    */
   public static Complex goal(Unifiable... args) {
      Variable.reset();  // Always reset the variable ID.
      HashMap<Variable, Variable> vars = new HashMap<Variable, Variable>();
      int n = args.length;
      Unifiable[] newArgs = new Unifiable[n];
      for (int i = 0; i < n; i++) {
         newArgs[i] = (Unifiable)args[i].standardizeVariablesApart(vars);
      }
      return new Complex(newArgs);
   } // goal()

   /**
    * goal
    *
    * A goal is the same as a complex term, but it is necessary to ensure
    * that the global variable id is reset to 0 and that all variable ids
    * are recreated, before querying the knowledgebase.
    *
    * @param  goal as string
    * @return goal as complex term
    */
   public static Complex goal(String str) {
      Complex c = new Complex(str);
      return Make.goal(c.getTerms());
   }

} // Make
