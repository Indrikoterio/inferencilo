/**
 * Append
 *
 * This class implements a built-in predicate which appends terms
 * to make a PList. For example:
 *
 *   $X = a, append($X, b, [c, d, e], [f, g], $OutList)
 *
 * The $OutList will bind to [a, b, c, d, e, f, g]
 *
 * The last argument is an output argument. The input arguments can
 * be Constants, Variables, Complex terms, or PLists.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Append extends BuiltInPredicate implements Unifiable, Goal {

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
      List<String> strTerms = Make.splitTerms(str, ',');
      Unifiable[] terms = new Unifiable[strTerms.size()];
      int index = 0;
      for (String strTerm : strTerms) {
         Make.addTerm(strTerm, terms, index++);
      }
      arguments = terms;
   }  // constructor


   /**
    * numOfArguments
    *
    */
   public int numOfArguments() {
      return arguments.length;
   }

   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new AppendSolutionNode(this, knowledge, parentSolution, parentNode);
   }


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Append(newArguments);
   }


   /**
    * evaluate
    *
    * Append all arguments together.
    *
    * @param   substitution set of parent
    * @return  unifiable term or null
    */
   public Unifiable evaluate(SubstitutionSet ss) {

      PList resultPList = null;

      int numOfArguments = arguments.length;
      if (numOfArguments < 2) return null;

      List<Unifiable> argList = new ArrayList<Unifiable>();

      for (int i = 0; i < numOfArguments - 1; i++) {

         Unifiable term = arguments[i];

         // Get ground term.
         if (term instanceof Variable) {
            if (ss.isGround((Variable)term)) {
               term = ss.getGroundTerm((Variable)term);
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
      return outPList;

   }  // evaluate()

} // Append
