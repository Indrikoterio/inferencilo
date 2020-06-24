/**
 * Print
 *
 * This built-in function prints out terms. If the first term has format
 * specifiers (%s), it is treated as a format string. For example,
 *
 *    $Name = John, $Age = 23, print(%s is %s years old., $Name, $Age)
 *
 * will print out,
 *
 *    John is 23 years old.
 *
 * Commas and new lines (\n) must be escaped with a double backslash,
 * for example:
 *
 *    print(%s\\, my friend\\, is $s years old.\\n, $Name, $Age)
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Print extends BuiltInPredicate implements Unifiable, Goal {

   private static final String NAME = " PRINT ";
   private static final String FORMAT_SPECIFIER = "%s";

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Print(Unifiable... arguments) {
      super(NAME, arguments);
   }

   /**
    * constructor
    *
    * This constructor takes a string of arguments, such as:
    *    "cherry, [strawberry, blueberry], $X, $Out"
    * and parses it to produce an array of Unifiable arguments.
    *
    * @param  arguments (String)
    */
   public Print(String str) {
      super(NAME);
      List<String> strTerms = Make.splitTerms(str, ',');
      Unifiable[] terms = new Unifiable[strTerms.size()];
      int index = 0;
      for (String strTerm : strTerms) {
         Make.addTerm(strTerm, terms, index++);
      }
      arguments = terms;
   }  // constructor


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Print(newArguments);
   }


   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new PrintSolutionNode(this, knowledge, parentSolution, parentNode);
   }


   /*
    * isFormatString
    *
    * If a string has a format specifier, it is a format string.
    *
    * @param  original string
    * @return true/false
    */
   private boolean isFormatString(String str) {
      if (str.indexOf(FORMAT_SPECIFIER) < 0) return false;
      return true;
   }


   /*
    * splitFormatString
    *
    * A format string looks like this:
    *  "Hello %s, my name is %s."
    * This function will divide a string into substrings:
    *  "Hello ", "%s", ", my name is ", "%s", "."
    *
    * @param  original string
    * @return array of substrings
    */
   private List<String> splitFormatString(String str) {
      List<String> sections = new ArrayList<String>();
      int start = 0;
      int length = str.length();
      while (start < length) {
         int index = str.indexOf(FORMAT_SPECIFIER, start);
         if (index < 0) {
            sections.add(str.substring(start, length));
            start = length;
         }
         else {
            sections.add(str.substring(start, index));
            start = index;
            index += 2;
            sections.add(str.substring(start, index));
            start = index;
         }
      }
      return sections;
   }  // splitFormatString


   /*
    * getGround
    *
    * If the term is a Variable, return its ground term.
    * Else, return the term unchanged.
    *
    * @param  term
    * @param  substitution set
    * @return ground term
    */
   private Unifiable getGround(Unifiable term, SubstitutionSet ss) {
      if (term instanceof Variable) {
         if (ss.isGround((Variable)term)) {
            return ss.getGroundTerm((Variable)term);
         }
      }
      return term;
   } // getGround


   /**
    * evaluate
    *
    * Prints out an identifying message and the given terms.
    */
   public Unifiable evaluate(SubstitutionSet ss) {

      if (arguments.length == 0) return Anon.anon;

      // Get first argument.
      Unifiable term0 = arguments[0];
      term0 = getGround(term0, ss);
      if (isFormatString("" + term0)) {
         List<String> formatSubstrings = splitFormatString("" + term0);
         int count = 1;
         for (String format : formatSubstrings) {
            if (format.equals(FORMAT_SPECIFIER)) {
               if (count < arguments.length) {
                  Unifiable t = arguments[count];
                  t = getGround(t, ss);
                  System.out.print(t);
                  count++;
               }
               else {
                  System.out.print(format);
               }
            }
            else {
               System.out.print(format);
            }
         }
      }
      else {
         System.out.print("" + term0);
         boolean first = true;
         for (Unifiable term : arguments) {
            if (first) first = false;
            else {
               term = getGround(term, ss);
               System.out.print(", " + term);
            }
         }
      }
      return Anon.anon;   // not needed
   } // evaluate

}  // Print
