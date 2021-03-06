/**
 * CompareBase
 *
 * This class is the base of built-in predicates which
 * compare two values, such as greater_than() and less_than().
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public abstract class CompareBase extends BuiltInPredicate {

   /**
    * constructor
    *
    * @param  name of predicate
    * @param  unifiable arguments
    * @throws TooManyArgumentsException, TooFewArgumentsException
    */
   public CompareBase(String name, Unifiable... arguments) {
      super(name, arguments);
      if (arguments.length > 2) throw new TooManyArgumentsException("- " + name);
      if (arguments.length < 2) throw new TooFewArgumentsException("- " + name);
   }

   /**
    * constructor
    *
    * This constructor takes a list of strings which represent the
    * arguments of the predicate. For example, if the predicate is
    *     greater_than($X, 10),
    * ...the list will have "$X" and "10".
    *
    * @param  name of predicate
    * @param  argument list
    * @throws TooManyArgumentsException, TooFewArgumentsException
    */
   public CompareBase(String name, List<String> args) {

      super(name);
      int size = args.size();
      if (size > 2) throw new TooManyArgumentsException("- " + name);
      if (size < 2) throw new TooFewArgumentsException("- " + name);

      this.arguments = args.stream().map(Make::term)
                                    .toArray(Unifiable[]::new);
   }  // constructor


   /**
    * constructor
    *
    * This constructor takes arguments in string form.
    * The two arguments are separated by a comma.
    *
    * For the predicate greater_than($X, 10),
    * ...the argument string is: "$X, 10"
    *
    * @param  name of predicate
    * @param  argument string
    * @throws TooManyArgumentsException, TooFewArgumentsException
    */
   public CompareBase(String name, String strArguments) {

      super(name);
      List<String> strTerms = Make.splitTerms(strArguments, ',');
      int size = strTerms.size();
      if (size > 2) throw new TooManyArgumentsException("- " + name);
      if (size < 2) throw new TooFewArgumentsException("- " + name);

      arguments = strTerms.stream()
                          .map(Make::term)
                          .toArray(Unifiable[]::new);
   }  // constructor

   /**
    * first
    *
    * Getter for the first argument. The argument is returned
    * as a Constant.
    *
    * @param  substitution set
    * @return Constant
    * @throws UnboundArgumentException
    */
   public Constant first(SubstitutionSet ss) {
      Constant first  = ss.castConstant(arguments[0]);
      if (first  == null) throw new UnboundArgumentException(": " + predicateName);
      return first;
   }

   /**
    * second
    *
    * Getter for the second argument. The argument is returned
    * as a Constant.
    *
    * @param  substitution set
    * @return Constant
    * @throws UnboundArgumentException
    */
   public Constant second(SubstitutionSet ss) {
      Constant second  = ss.castConstant(arguments[1]);
      if (second  == null) throw new UnboundArgumentException(": " + predicateName);
      return second;
   }

}  // CompareBase
