/**
 * LessThanOrEqual
 *
 * This built-in predicate compares two arguments. It evaluates as true
 * if the first is less than or equal to the second. For example:
 *
 *   less_than_or_equal(14, 14)  # True.
 *   less_than_or_equal(22, 20)  # False.
 *
 * This can also be used for strings.
 *
 *   less_than_or_equal(Joanne, John)   # True.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class LessThanOrEqual extends CompareBase implements Unifiable, Goal {

   private static final String NAME = "LESS_THAN_OR_EQUAL";

   /**
    * constructor
    *
    * @param  unifiable arguments
    * @throws TooManyParameters, TooFewParameters
    */
   public LessThanOrEqual(Unifiable... arguments) {
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
   public LessThanOrEqual(String strArguments) {
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
         if (dFirst <= dSecond) return ss;
         return null;  // Failure.
      }
      else {
         String sFirst  = "" + first;
         String sSecond = "" + second;
         if (sFirst.compareTo(sSecond) <= 0) return ss;
         return null;  // Failure.
      }

   } // evaluate

}  // LessThanOrEqual
