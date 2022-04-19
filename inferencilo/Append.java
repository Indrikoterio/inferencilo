/**
 * Append
 *
 * This class implements a built-in predicate which appends terms to
 * make a PList. For example:
 *
 *   $X = a, append($X, b, [c, d, e], [f, g], $OutList)
 *
 * The last argument, $OutList, is an output argument which will bind
 * to [a, b, c, d, e, f, g]
 *
 * Input arguments can be Constants, Variables, Complex terms, or PLists.
 *
 * There must be at least 2 arguments.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Append extends BuiltInPredicate {

   private static final String name = "APPEND";

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Append(Unifiable... arguments) {
      super(name, arguments);
   }

   /**
    * constructor
    *
    * This constructor takes a string of arguments, such as:
    *    "cherry, [strawberry, blueberry], $X, $Out"
    * and parses it to produce an array of Unifiable arguments.
    *
    * @param  arguments (String)
    */
   public Append(String str) {
      super(name);
      arguments = Make.splitTerms(str, ',')
                      .stream()
                      .map(Make::term)
                      .toArray(Unifiable[]::new);
   }  // constructor


   /**
    * evaluate
    *
    * Append all arguments together.
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {

      PList resultPList = null;

      if (arguments.length < 2) return null;

      List<Unifiable> argList = new ArrayList<Unifiable>();

      for (int i = 0; i < arguments.length - 1; i++) {

         Unifiable term = arguments[i];

         // Get ground term.
         if (term instanceof Variable) {
            if (parentSolution.isGround((Variable)term)) {
               term = parentSolution.getGroundTerm((Variable)term);
            }
            // Long explanation: In a list of words (and punctuation), some terms
            // are optional. For example, a modifier may consist of an adverb and
            // an adjective, or just an adjective. For a Prolog predicate which
            // collects adverbs, it is useful to return an adverb or a term which
            // can be ignored. I chose Anon (= anonymous variable) for this.
            // Anon works well when passed as a term to a rule, but as a return
            // item, it will not be unified with the variable which corresponds
            // to it. The output variable does not bind to Anon. (Maybe it should.)
            // Such unbound variables were causing Append to abort, so I'm commenting
            // out the following 'return'. Maybe there is a better solution to this.
            //else return null;
         }

         if (term instanceof Constant) {
            argList.add(term);
         }
         else if (term instanceof Complex) {
            argList.add(term);
         }
         else if (term instanceof PList) {
            PList plist = (PList)term;
            while (true) {
               Unifiable head = plist.getHead();
               if (head == null) break;
               argList.add(head);
               plist = plist.getTail();
               if (plist == null) break;
            }
         }
      }

      PList outPList = new PList(false, argList);
      if (outPList == null) return null;

      Unifiable lastTerm = getTerm(arguments.length - 1);

      return lastTerm.unify(outPList, parentSolution);

   }  // evaluate()

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Append(newArguments);
   } // standardizeVariablesApart

} // Append
