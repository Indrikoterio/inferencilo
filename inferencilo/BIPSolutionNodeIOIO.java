/**
 * BIPSolutionNodeIOIO
 *
 * Built-In Predicate Solution Node, for predicates which have the form: 
 *
 *    do_something(InList, OutList, InErrors, OutErrors)
 *
 * IOIO stands for In, Out, In, Out.
 *
 * This predicate is intended to be useful for parsing. The first
 * argument is a word list; the second is the output for the transformed
 * word list. The third is an input list of previously generated errors.
 * The fourth argument outputs the error list with any additional errors
 * generated which may have been generated.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class BIPSolutionNodeIOIO extends SolutionNode {

   BuiltInPredicateIOIO goal;
   boolean moreSolutions = true;

   /**
    * constructor
    *
    * @param  unifiable goal
    * @param  knowlegde base
    * @param  parent solution set
    * @param  parent solution node
    * @param  solution node
    */
   public BIPSolutionNodeIOIO(BuiltInPredicateIOIO goal, KnowledgeBase kb,
                           SubstitutionSet parentSolution,
                           SolutionNode parentNode) {
      super(goal, kb, parentSolution, parentNode);
      this.goal = goal;
   }


   /**
    * nextSolution
    *
    * Call evaluate() to do some work on the input argument(s),
    * then unify the result(s) with the output arguments.
    *
    * @return  new substitution set
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;

      Unifiable newTerm = goal.evaluate(getParentSolution());   // Do the logical business.
      if (newTerm == null) return null;
      Unifiable outTerm = goal.getTerm(1);
      Unifiable newErrors = goal.getErrors();
      Unifiable outErrors = goal.getTerm(3);

      SubstitutionSet solution = newTerm.unify(outTerm, getParentSolution());
      solution = newErrors.unify(outErrors, solution);

      return solution;
   }

}
