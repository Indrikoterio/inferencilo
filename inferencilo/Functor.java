/**
 * Functor
 *
 * Functor is a built-in predicate to get the functor and arity
 * of a complex term. Eg.:
 *
 *     functor(boss(Zack, Stephen), $Func, $Arity)
 *
 * Note: The first term must be the complex term to be tested.
 *
 * $Func will bind to 'boss' and $Arity will bind to '2' (because
 * there are two arguments, Zack and Stephen). Arity is optional:
 *
 *     functor(boss(Zack, Stephen), $Func)
 *
 * Of course, the first argument would normally be a Variable, and
 * the second would be a Constant. The following goal will succeed.
 *
 *     $X = boss(Zack, Stephen), functor($X, boss)
 *
 * The next goal will not succeed, because the arity is incorrect:
 *
 *     functor($X, boss, 3)
 *
 * If the second argument has an asterisk at the end, the match will
 * test only the start of the string. For example, the following
 * will succeed:
 *
 *     $X = noun_phrase(the blue sky), functor($X, noun*)
 *
 * TODO:
 * Perhaps the functionality could be expanded to accept a regex
 * string for the second argument.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Functor extends BuiltInPredicate {

   private static final String NAME = "FUNCTOR";

   private String functor;
   private int arity;

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Functor(Unifiable... arguments) {
      super(NAME, arguments);
      int len = arguments.length;
      if (len < 2 || len > 3)
         throw new FatalParsingException(
               "functor() takes 2 or 3 arguments.");
   }

   /**
    * constructor
    *
    * This constructor takes a string, such as:
    *
    *   "boss(Zack, Stephen), $F, $A"
    *
    * @param  argument string
    */
   public Functor(String str) {
      super(NAME);
      List<String> strTerms = Make.splitTerms(str, ',');
      int size = strTerms.size();
      if (size < 2 || size > 3)
         throw new FatalParsingException(
               "functor() takes 2 or 3 arguments: " + str);
      arguments = strTerms.stream().map(Make::term).toArray(Unifiable[]::new);

   }  // constructor

   /**
    * evaluate
    *
    * Determines the functor and arity of the first argument. Bind
    * the functor to the second argument, and the arity to the third
    * argument, if there is one. Return the new substitution set,
    * or null for failure.
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {

      SubstitutionSet ss = parentSolution;

      // Get first argument.
      Complex first = ss.castComplex(arguments[0]);
      if (first != null) {  // Must be a Complex term.
         functor = first.functor();
         arity = first.arity();
      }
      else {  // Maybe a Constant.
         Constant conTerm = ss.castConstant(arguments[0]);
         if (conTerm == null) return null;
         functor = conTerm.toString();
         arity = 0;
      }

      if (arguments.length > 1) {
         Unifiable term = getTerm(1);
         if (term instanceof Constant) {
            String f2 = term.toString();
            if (f2.endsWith("*")) {
               // Strip off asterisk.
               f2 = f2.substring(0, f2.length() - 1);
               if (!functor.startsWith(f2)) return null;
            }
            else {
               if (!functor.equals(f2)) return null;
            }
         }
         else {
            ss = term.unify(new Constant(functor), ss);
         }
      }
      if (arguments.length > 2) {
         Unifiable term = getTerm(2);
         ss = term.unify(new Constant("" + arity), ss);
      }
      return ss;

   } // evaluate

}  // Functor
