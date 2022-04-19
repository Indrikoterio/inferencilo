/**
 * LessThan
 *
 * This built-in predicate compares two arguments. It evaluates as true
 * if the first is less than the second. For example:
 *
 *   less_than(11, 14)  # True.
 *   less_than(14, 11)  # False.
 *
 * This can also be used for strings.
 *
 *   less_than(Agatha, James)   # True.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class LessThan extends CompareBase {

   private static final String NAME = "LESS_THAN";

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public LessThan(Unifiable... arguments) {
      super(NAME, arguments);
   }

   /**
    * constructor
    *
    * @param  argument list
    */
   public LessThan(List<String> args) {
      super(NAME, args);
   }  // constructor


   /**
    * constructor
    *
    * This constructor takes arguments as a string.
    *
    * @param  argument string
    */
   public LessThan(String strArguments) {
      super(NAME, strArguments);
   }  // constructor


   /**
    * evaluate
    *
    * Get two arguments. If both are numbers, compare them as numbers.
    * Otherwise compare them as strings. Succeed if first term is less
    * than the second.
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
         if (dFirst < dSecond) return ss;
         return null;  // Failure.
      }
      else {
         String sFirst  = first.toString();
         String sSecond = second.toString();
         if (sFirst.compareTo(sSecond) < 0) return ss;
         return null;  // Failure.
      }

   } // evaluate

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new LessThan(newArguments);
   } // standardizeVariablesApart

}  // LessThan
