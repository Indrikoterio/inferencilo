/**
 * Count
 *
 * This predicate returns the number of items in a list (PList).
 * It doesn't actually count them. The count is stored in each item.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Count extends BuiltInPredicate implements Unifiable, Goal {

   private static final String NAME = "COUNT";

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Count(Unifiable... arguments) {
      super(NAME, arguments);
      if (arguments.length != 2)
         throw new FatalParsingException("count() takes 2 arguments.");
   }

   /**
    * constructor
    *
    * This constructor takes a string, such as:
    *
    *   "[a, b, c], $Count"
    *
    * @param  argument string
    */
   public Count(String str) {
      super(NAME);
      List<String> strTerms = Make.splitTerms(str, ',');
      int size = strTerms.size();
      if (size != 2)
         throw new FatalParsingException("count() takes 2 arguments: " + str);
      arguments = strTerms.stream().map(Make::term).toArray(Unifiable[]::new);
   }  // constructor


   /**
    * evaluate
    *
    * Get the count from the head of the PList.
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {

      SubstitutionSet ss = parentSolution;

      // Get first argument.
      PList pList = ss.castPList(arguments[0]);
      if (pList == null) return null;
      Constant count = new Constant("" + pList.count());
      return arguments[1].unify(count, ss);

   } // evaluate

}  // Count
