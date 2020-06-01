/**
 * Constant
 *
 * In Prolog, an 'atom' is a sequence of letters and digits which starts with a lower case letter:
 * For example: blue, happy, suspect3
 * An atom is a constant symbol. Integers and floating point numbers are also constants.
 *
 * In this inference engine, a Constant holds an internal String. Integers and floats can be
 * represented internally as Strings. Arithmetic functions which operate on these Constants
 * must convert their internal strings into a numbers (if possible), execute their functions,
 * and convert the result back to a Constants.
 *
 * Reference: http://www.cs.toronto.edu/~hojjat/384w09/Lectures/Prolog_Intro.pdf
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;
import java.util.regex.*;

public class Constant implements Unifiable {

   // For detecting numbers.
   private static Pattern numberPattern = Pattern.compile("[+-]?\\d+(\\.\\d+)?");

   private String value = null;

   /**
    * constructor
    *
    * @param  value (String)
    */
   public Constant(String value) { this.value = value; }

   /**
    * toString
    */
   public String toString() { return value; }

   /*
    * isNumber
    *
    * Use regex to determine whether a String represents a number.
    * Valid numbers can have a decimal point and/or a sign.
    * Eg. 1, 2, 3.14159, -42, +21
    *
    * @param  string to convert
    * @return  true if number, false otherwise
    */
   private boolean isNumber(String str) {
      return numberPattern.matcher(str).matches();
   }


   /*
    * convertDouble
    *
    * Convert the string value into a double floating point, if possible.
    * (Returns 0.0 if not.)
    *
    * @param   number as string
    * @return   number as double float
    */
   private double convertDouble(String str) {
      try {
         return Double.parseDouble(str);
      } catch (NumberFormatException nfe) {
         return 0.0;
      }
   }  // convertDouble


   /**
    * unify
    *
    * Perform logical unification.
    *
    * Note:
    * A constant unifies with itself, i.e., 'verb = verb' succeeds.
    * To determine if two constants are the same, this method does
    * a string comparison of the internal string values.
    * If the internal values represents numbers, unify will convert
    * the two strings to numbers; 1 unifies with 1.000 .
    *
    * @param  other unifiable
    * @param  substitution set
    * @return new substitution set
    */
   public SubstitutionSet unify(Unifiable other, SubstitutionSet ss) {
      if (this == other) return ss;
      if (other instanceof Variable) return other.unify(this, ss);
      if (other instanceof Anon) return ss;
      if (!(other instanceof Constant)) return null;
      String strThis = "" + this;
      String strThat = "" + other;
      if (strThis.equals(strThat)) return ss;
      // Check for number strings, because 1 unifies with 1.0 .
      if (isNumber(strThis) && isNumber(strThat)) {
           if (convertDouble(strThis) == convertDouble(strThat)) return ss;
      }
      return null;   // No unification.
   }


   /**
    * replaceVariables
    *
    * Replace bound variables with their bindings.
    * Nothing to do here.
    *
    * @param substitution set
    * @param expression
    */
   public Expression replaceVariables(SubstitutionSet ss) {
      return this;
   }

   /**
    * standardizeVariablesApart
    *
    * When a rule is fetched from the knowledge base, the variable terms must be
    * made distinct. (Eg. $X becomes $X_442.) Constants are exactly the same every
    * time a rule is fetched, so this function simply returns this class.
    *
    * Refer to class Expression for fuller comments.
    *
    * @param  prev - previously standardized variables (not used)
    * @return  this constant
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> prev) {
      return this;
   }

}  // Constant
