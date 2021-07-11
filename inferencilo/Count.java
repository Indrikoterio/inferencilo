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
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {

      SubstitutionSet ss = parentSolution;

      // Get first argument.
      /* Nope - have to count.
      PList pList = ss.castPList(arguments[0]);
      if (pList == null) return null;
      Constant count = new Constant("" + pList.count());
      return arguments[1].unify(count, ss);
      */

      /*
          In standard Prolog:

             doit(V2) :- V1 = [c, d, e], V2 = [a, b, Var].
             The goal doit(X) returns X = [a, b, [c, d, e]] .

             doit(V2) :- V1 = [c, d, e], V2 = [a, b | Var].
             The goal doit(X) returns X = [a, b, c, d, e] .

             doit(V2) :- V1 = [c, d, e], V2 = [a, Var, b].
             The goal doit(X) returns X = [a, [c, d, e], b] .

          Ref: https://swish.swi-prolog.org/

          Conclusion - If there is a tail variable which represents
          a list, its list items should be counted as part of the
          original list.

       */

      // Get first item.
      PList pList = ss.castPList(arguments[0]);
      if (pList == null) {
        throw new InvalidOperandException("Count's first argument must be a PList.");
      }

      int count = pList.recursiveCount(ss);
      return arguments[1].unify(new Constant("" + count), ss);

   } // evaluate

}  // Count
