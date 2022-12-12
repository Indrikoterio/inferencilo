/**
 * FilterBase
 *
 * This class is a parent class for the built-in filtering predicates include()
 * and exclude(). An input list of terms is filtered according to a filter goal.
 * Please refer to Include.java and Exclude.java for more details.
 *
 * The function evaluate() is defined here. Evaluate() calls passOrDiscard() in
 * the subclass, in order to determine which terms should be included or removed
 * from the output list.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public abstract class FilterBase extends BuiltInPredicate {

   Complex filter;  // This is a goal.

   /**
    * constructor
    *
    * @param  unifiable arguments
    * @throws TooFewArgumentsException, TooManyArgumentsException, InvalidArgumentException
    */
   public FilterBase(String name, Unifiable... arguments) {

      super(name, arguments);

      if (arguments.length > 3)
          throw new TooManyArgumentsException("in " + name);
      if (arguments.length < 3)
          throw new TooFewArgumentsException("in " + name);

      if (arguments[0] instanceof Complex) {
         filter = (Complex)arguments[0];
      }
      else {
         throw new InvalidArgumentException(name + " - first argument must be a goal.");
      }

   } // FilterBase

   /**
    * constructor
    *
    * @param  arguments as string
    * @throws TooFewArgumentsException, TooManyArgumentsException, InvalidArgumentException
    */
   public FilterBase(String name, String strArgs) {

      super(name);

      if (strArgs == null)
          throw new TooFewArgumentsException("in " + name);

      arguments = Make.splitTerms(strArgs, ',')
                  .stream()
                  .map(Make::term)
                  .toArray(Unifiable[]::new);

      if (arguments.length > 3)
          throw new TooManyArgumentsException("in " + name);
      if (arguments.length < 3)
          throw new TooFewArgumentsException("in " + name);

      if (arguments[0] instanceof Complex) {
         filter = (Complex)arguments[0];
      }
      else {
         throw new InvalidArgumentException(name + " - first argument must be a goal.");
      }

   } // FilterBase


   /**
    * getSolver
    *
    * Returns a SolutionNode (object) for filters.
    *
    * The main method in a SolutionNode is nextSolution().
    * nextSolution() calls evaluate().
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
            public SubstitutionSet nextSolution() throws TimeOverrunException {
               if (noBackTracking()) return null;
               if (!moreSolutions) return null;
               moreSolutions = false;
               return evaluate(parentSolution);
            }
         };

   } // getSolver


   /*
    * passOrDiscard
    *
    * Does the given pass the filter test?
    *
    * @param  term
    * @param  solution set
    * @return true if value passes
    *
    */
   abstract boolean passOrDiscard(Unifiable term, SubstitutionSet ss);

   /**
    * evaluate the arguments
    *
    * @param  Substitution Set
    * @return new Substitution Set
    */
   public SubstitutionSet evaluate(SubstitutionSet ss) {

      SLinkedList inList  = ss.castSLinkedList(arguments[1]);
      if (inList == null) return null;

      List<Unifiable> out = new ArrayList<Unifiable>();

      SLinkedList sList = inList;
      int count = inList.recursiveCount(ss);

      for (int i = 0; i < count; i++) {
         Unifiable head = sList.getHead();
         if (sList.isTailVar() && head != Anon.anon) {
            Variable hVar  = (Variable)(head);
            SLinkedList term = ss.castSLinkedList(hVar);
            if (term != null) {
               sList = (SLinkedList)term;
               head = sList.getHead();
            }
         }
         if (head == null) break;
         if (passOrDiscard(head, ss)) {
            out.add(head);
         }
         sList = sList.getTail();
      } // for

      SLinkedList outList = new SLinkedList(false, out);
      return outList.unify(arguments[2], ss);

   } // evaluate()

}  // FilterBase
