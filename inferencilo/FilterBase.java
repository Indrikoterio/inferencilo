/**
 * FilterBase
 *
 * This class is a parent class for filtering predicates
 * Include and Exclude.
 *
 * The first argument, a predicate name, must be a Constant,
 * which corresponds to a fact/rule with an arity of 1.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class FilterBase extends BuiltInPredicate {

   String filter;

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

      filter = arguments[0].toString();
      if (filter.length() < 1)
          throw new InvalidArgumentException("first argument in " + name);

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

      filter = arguments[0].toString();
      if (filter.length() < 1)
          throw new InvalidArgumentException("first argument in " + name);

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
               if (noBackChaining()) return null;
               if (!moreSolutions) return null;
               moreSolutions = false;
               return evaluate(parentSolution, knowledge);
            }
         };

   } // getSolver


   /*
    * passOrDiscard
    *
    * Does the given pass the filter test?
    *
    * @param  value
    * @param  knowledge base
    * @throws TimeOverrunException
    * @return true if value passes
    *
    */
   abstract boolean passOrDiscard(Unifiable value, KnowledgeBase kb)
                           throws TimeOverrunException;

   /**
    * evaluate
    *
    * This 'evaluate' it not used in Include. The one below it, which has
    * a two parameters, is in use. This evaluate() is defined to override
    * the abstract method definition in BuiltInPredicate.
    *
    * @param   substitution set of parent
    * @return  new substitution set
    */
   public SubstitutionSet evaluate(SubstitutionSet ss) {
      return null;
   }


   /**
    * evaluate the arguments
    *
    * @param   Substitution Set
    * @param   Knowledge Base
    * @throws  TimeOverrunException
    * @return  new Substitution Set
    */
   SubstitutionSet evaluate(SubstitutionSet ss, KnowledgeBase kb)
                            throws TimeOverrunException {

      PList inList  = ss.castPList(arguments[1]);
      if (inList == null) return null;

      List<Unifiable> out  = new ArrayList<Unifiable>();

      PList pList = inList;
      int count = inList.recursiveCount(ss);

      for (int i = 0; i < count; i++) {
         Unifiable head = pList.getHead();
         if (pList.isTailVar() && head != Anon.anon) {
            Variable hVar  = (Variable)(head);
            PList term = ss.castPList(hVar);
            if (term != null) {
               pList = (PList)term;
               head = pList.getHead();
            }
         }
         if (head == null) break;
         if (passOrDiscard(ss.getGroundTerm(head), kb)) {
            out.add(head);
         }
         pList = pList.getTail();
      } // for

      PList outList = new PList(false, out);
      return outList.unify(arguments[2], ss);

   } // evaluate()

}  // FilterBase
