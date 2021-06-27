/**
 * Print
 *
 * This built-in function prints out terms. If the first term is a
 * Constant which has format specifiers (%s), it is treated as a
 * format string. For example,
 *
 *  $Name = John, $Age = 23, print(%s is %s years old., $Name, $Age)
 *
 * will print out,
 *
 *  John is 23 years old.
 *
 * Commas which do not separate arguments, but are intended to be
 * printed, must be escaped with a backslash, for example:
 *
 *  print(%s\, my friend\, is $s years old.\n, $Name, $Age)
 *
 * will print out,
 *
 *  John, my friend, is 23 years old.
 *
 * If the string is compiled within a Java source file, the back-
 * slashes must be doubled.
 *
 *   print(%s\\, my friend\\, is $s years old.\\n, $Name, $Age)
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Print extends BuiltInPredicate {

   private static final String NAME = "PRINT";
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
      arguments = Make.splitTerms(str, ',')
                      .stream()
                      .map(Make::term)
                      .toArray(Unifiable[]::new);

   }  // constructor


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
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {

      if (arguments.length == 0) return parentSolution;

      // Get first argument.
      Unifiable term0 = arguments[0];
      term0 = getGround(term0, parentSolution);
      if (isFormatString("" + term0)) {
         List<String> formatSubstrings = splitFormatString("" + term0);
         int count = 1;
         for (String format : formatSubstrings) {
            if (format.equals(FORMAT_SPECIFIER)) {
               if (count < arguments.length) {
                  Unifiable t = arguments[count];
                  t = getGround(t, parentSolution);
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
               term = getGround(term, parentSolution);
               System.out.print(", " + term);
            }
         }
      }

      return parentSolution;

   } // evaluate

}  // Print
