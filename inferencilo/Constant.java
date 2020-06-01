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
 * and convert the result back to a Constant.
 *
 * Reference:
 *     http://www.cs.toronto.edu/~hojjat/384w09/Lectures/Prolog_Intro.pdf
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Constant implements Unifiable {

   private String value = null;

   /**
    * constructor
    *
    * @param  value (string)
    */
   public Constant(String value) { this.value = value; }


  /**
   * concat
   *
   * This static method concatenates unifiable terms to produce a Constant.
   * Strings are concatenated and separated by a space, except for commas,
   * which have no spaces in front.
   *
   * Thus, if we have,
   *   Constant i = new Constant("I");
   *   Constant comma = new Constant(",");
   *   Constant robot = new Constant("Robot");
   * then
   *    Constant.make(ss, i, comma, robot)
   * is equivalent to
   *    new Constant("I, Robot")
   *
   * @param   substitution set  (can be null)
   * @param   unifiable terms
   * @return  Constant or null
   */
   public static Constant concat(SubstitutionSet ss, Unifiable... arr) {

      if (arr.length < 1) return null;

      StringBuilder sb = new StringBuilder("");
      String str;

      boolean first = true;
      for (Unifiable term : arr) {

         if (term == null) continue;

         // Get grounded term.
         if (term instanceof Variable) {
            if (ss != null && ss.isGround((Variable)term)) {
               term = ss.getGroundTerm((Variable)term);
            }
            else continue;
         }

         if (term instanceof Constant || term instanceof Complex) {
            str = "" + term;
            if (first || str.equals(",")) sb.append(str);
            else {
               sb.append(" ");
               sb.append(str);
            }
            first = false;
         }
         else if (term instanceof PList) {
            PList plist = (PList)term;
            while (true) {
               Unifiable head = plist.getHead();
               if (head == null) break;
               str = "" + head;
               if (first || str.equals(",")) sb.append(str);
               else {
                  sb.append(" ");
                  sb.append(str);
               }
               first = false;
               plist = plist.getTail();
               if (plist == null) break;
            }
         }
      }
      return new Constant(sb.toString());
   }

   public String toString() { return value; }

   /**
    * unify
    *
    * Perform logical unification.
    *
    * Note:
    * A constant unifies with itself, i.e., 'verb = verb' succeeds.
    * To determine if two constants are the same, this method does
    * a string comparison of the internal string values.
    *
    * @param  other unifiable
    * @param  substitution set
    * @return new substitution set
    */
   public SubstitutionSet unify(Unifiable other, SubstitutionSet ss) {
      if (this == other) return ss;
      String strThis = "" + this;
      String strThat = "" + other;
      if (strThis.equals(strThat)) return ss;
      if (other instanceof Variable) return other.unify(this, ss);
      if (other instanceof Anon) return ss;
      return null;
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
