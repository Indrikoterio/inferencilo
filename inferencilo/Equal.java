/**
 * Equal
 *
 * This built-in predicate compares two arguments.
 * It evaluates as true if they are equal. For example:
 *
 *   equal(14, 14)  # True.
 *   equal(13, 14)  # False.
 *
 * This can also be used for strings.
 *
 *   equal(Terence, Milena)   # False.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Equal extends CompareBase {

   private static final String NAME = "EQUAL";

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Equal(Unifiable... arguments) {
      super(NAME, arguments);
   }

   /**
    * constructor
    *
    * @param  argument list
    */
   public Equal(List<String> args) {
      super(NAME, args);
   }  // constructor

   /**
    * constructor
    *
    * @param  argument string
    */
   public Equal(String strArguments) {
      super(NAME, strArguments);
   }  // constructor


   /**
    * evaluate
    *
    * Get two arguments. If both are numbers, compare them as numbers.
    * Otherwise compare them as strings. Succeed if terms are equal.
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet ss) {

      Constant first  = first(ss);
      Constant second = second(ss);

      if (first.isNumber() && second.isNumber()) {
         double dFirst  = first.convertDouble();
         double dSecond = second.convertDouble();
         if (dFirst == dSecond) return ss;
         return null;  // Failure.
      }
      else {
         String sFirst  = first.toString();
         String sSecond = second.toString();
         if (sFirst.compareTo(sSecond) == 0) return ss;
         return null;  // Failure.
      }

   } // evaluate

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, LogicVar> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Equal(newArguments);
   } // standardizeVariablesApart

}  // Equal
