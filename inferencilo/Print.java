/**
 * Print
 *
 * This predicate prints out a text message and the given term(s).
 * It can be used for debugging purposes.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Print extends BuiltInPredicate implements Unifiable, Goal {

   private static final String name = " PRINT ";

   String message = null;

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Print(Unifiable... arguments) {
      super(name, arguments);
   }


   /**
    * constructor
    *
    * @param  identifying message (string)
    * @param  unifiable arguments
    */
   public Print(String message, Unifiable... arguments) {
      super(name, arguments);
      this.message = message;
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
   public Print(String str) {
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
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new Print(message, newArguments);
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
      return new PrintSolutionNode(this, knowledge, parentSolution, parentNode);
   }


   /**
    * evaluate
    *
    * Prints out an identifying message and the given terms.
    */
   public Unifiable evaluate(SubstitutionSet ss) {
      int num = 1;
      if (message != null && message.length() > 0) {
         System.out.print(message + " ");
      }
      boolean first = true;
      for (Unifiable term : arguments) {
         if (term instanceof Variable) {
            if (ss.isGround((Variable)term)) {
               term = ss.getGroundTerm((Variable)term);
            }
         }
         if (first) {
            first = false;
            System.out.print("" + term);
         }
         else {
            System.out.print(", " + term);
         }
         num++;
      }
      System.out.print("\n");
      return Anon.anon;   // not needed
   }

}  // Print
