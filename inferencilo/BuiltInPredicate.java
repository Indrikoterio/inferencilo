/**
 * BuiltInPredicate
 *
 * This is a base class for built-in predicates. Subclasses should
 * override the abstract evaluate() method.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.HashMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class BuiltInPredicate implements Goal {

   // These are public for the convenience of subclasses.
   // It's a sin, but I don't care.
   public String predicateName = null;
   public Unifiable[] arguments;

   /**
    * constructor
    *
    * @param  predicate name
    * @param  unifiable arguments
    */
   public BuiltInPredicate(String predicateName, Unifiable... arguments) {
      this.predicateName = predicateName;
      this.arguments = arguments;
   }


   /**
    * getSolver
    *
    * Returns a SolutionNode (object) for built-in predicates.
    *
    * The main method in a SolutionNode is nextSolution().
    * nextSolution() calls evaluate(), which is defined by the
    * subclass. evaluate() takes the parentSolution (a substitution
    * set), performs some logic on the predicate's arguments, and
    * returns a new substitution set.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return

         new SolutionNode(this, knowledge, parentSolution, parentNode) {

            boolean moreSolutions = true;

            /**
             * nextSolution
             *
             * Call evaluate() to do some work on the input argument(s),
             * then return the new solution set.
             *
             * @return  new substitution set
             */
            public SubstitutionSet nextSolution() {
               if (noBackTracking()) return null;
               if (!moreSolutions) return null;
               moreSolutions = false;
               return evaluate(parentSolution);
            }
         };
   } // getSolver


   /**
    * standardizeOne
    *
    * Standardize one argument.
    *
    * This method assists standardizeVariablesApart() in the subclass.
    * If the argument is a logic variable, this method will 'standardize' it.
    * (That is, substitute a new unique variable.)
    * If not, it just returns the argument as is.
    *
    * @param   unifiable argument
    * @param   already standardized variables (hash)
    * @return  out argument
    */
   public Unifiable standardizeOne(Unifiable argument,
                                      HashMap<String, LogicVar> newVars) {
      if (argument instanceof LogicVar) {
         LogicVar arg = (LogicVar)argument;
         Unifiable newArgument = (Unifiable)arg.standardizeVariablesApart(newVars);
         return newArgument;
      }
      return argument;
   }

   /**
    * replaceVariables
    *
    * Refer to Expression.java for full comments.
    *
    * @param   substitution set
    * @return  new expression, without variables
    */
   public Expression replaceVariables(SubstitutionSet ss) {

      for (Unifiable arg : arguments) {
         if (arg instanceof Constant || arg instanceof Complex) {
            return arg;
         }
         else if (arg instanceof LogicVar) {
            LogicVar var = (LogicVar)arg;
            if (ss.isBound(var)) {
               Expression exp = var.replaceVariables(ss);
               return exp;
            }
            else {
               return new Constant("BIP, replaceVariables(): This is very bad.");
            } 
         }
         else {
            return new Constant("BIP, replaceVariables(): Got a problem.");
         }
      }
      return null;

   }  // replaceVariables


   /**
    * evaluate
    *
    * The unique work of the built-in predicate is done by this method.
    *
    * @param   substitution set of parent
    * @return  new substitution set
    */
   public abstract SubstitutionSet evaluate(SubstitutionSet ss);


   /**
    * getTerm - according to index
    *
    * @param   index of term/argument
    * @return  unifiable term
    */
   public Unifiable getTerm(int index) { return arguments[index]; }

   public String toString() { return predicateName + "(...)"; }


   /**
    * displayString
    *
    * If the given term is a Constant, return its string value,
    * If the given term is a Complex term, return the string value of the first term.
    * If it is a SLinkedList, iterate through the list to produce a readable string.
    * (Commas should have no space in front.)
    *
    * @param   term
    * @param   substitution set
    * @return  string representation
    */
   public String displayString(Unifiable term, SubstitutionSet ss) {

      if (term instanceof LogicVar) {
         Unifiable t = ss.getGroundTermOrNull((LogicVar)term);
         if (t == null) return "";
         return displayString(t, ss);
      }

      if (term instanceof Constant) return term.toString();
      if (term instanceof Complex) {
         Complex t = (Complex)term;
         if (t.length() < 2) return "";
         return displayString(t.getTerm(1), ss);
      }
      if (term instanceof SLinkedList) {

         SLinkedList sList = (SLinkedList)term;

         StringBuilder sb = new StringBuilder("");
         Unifiable head = sList.getHead();
         if (head == null) return "";
         sb.append(displayString(head, ss));  // recursion

         SLinkedList theTail = sList.getTail();
         while (theTail != null) {
            head = theTail.getHead();
            if (head == null) break;
            String str = displayString(head, ss);
            if (str.equals(",")) sb.append(str);
            else sb.append(" " + str);
            theTail = theTail.getTail();
         }
         return sb.toString();
      }
      return "";
   } // displayString

}  // BuiltInPredicate
