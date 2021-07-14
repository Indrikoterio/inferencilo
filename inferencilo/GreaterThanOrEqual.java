/**
 * GreaterThanOrEqual
 *
 * This built-in predicate compares two arguments. It evaluates as true
 * if the first is greater than or equal to the second. For example:
 *
 *   greater_than_or_equal(14, 14)  # True.
 *   greater_than_or_equal(13, 14)  # False.
 *
 * This can also be used for strings.
 *
 *   greater_than_or_equal(Terence, Milena)   # True.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class GreaterThanOrEqual extends CompareBase implements Unifiable, Goal {

   private static final String NAME = "GREATER_THAN_OR_EQUAL";

   /**
    * constructor
    *
    * @param  unifiable arguments
    * @throws TooManyParameters, TooFewParameters
    */
   public GreaterThanOrEqual(Unifiable... arguments) {
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
   public GreaterThanOrEqual(String strArguments) {
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
         if (dFirst >= dSecond) return ss;
         return null;  // Failure.
      }
      else {
         String sFirst  = first.toString();
         String sSecond = second.toString();
         if (sFirst.compareTo(sSecond) >= 0) return ss;
         return null;  // Failure.
      }

   } // evaluate

}  // GreaterThanOrEqual
