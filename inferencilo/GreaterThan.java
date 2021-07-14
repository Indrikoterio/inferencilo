/**
 * GreaterThan
 *
 * This built-in predicate compares two arguments. It evaluates as true
 * if the first is greater than the second. For example:
 *
 *   greater_than(14, 11)  # True.
 *   greater_than(11, 14)  # False.
 *
 * This can also be used for strings.
 *
 *   greater_than(Terence, Milena)   # True.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class GreaterThan extends CompareBase implements Unifiable, Goal {

   private static final String NAME = "GREATER_THAN";

   /**
    * constructor
    *
    * @param  unifiable arguments
    * @throws TooManyParameters, TooFewParameters
    */
   public GreaterThan(Unifiable... arguments) {
      super(NAME, arguments);
   }

   /**
    * constructor
    *
    * This constructor takes arguments as a string.
    *
    * @param  argument string
    * @throws TooManyParameters, TooFewParameters
    */
   public GreaterThan(String strArguments) {
      super(NAME, strArguments);
   }  // constructor


   /**
    * evaluate
    *
    * 
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
         if (dFirst > dSecond) return ss;
         return null;  // Failure.
      }
      else {
         String sFirst  = first.toString();
         String sSecond = second.toString();
         if (sFirst.compareTo(sSecond) > 0) return ss;
         return null;  // Failure.
      }

   } // evaluate

}  // GreaterThan
