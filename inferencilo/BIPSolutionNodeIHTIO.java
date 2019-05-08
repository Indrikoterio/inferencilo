/**
 * BIPSolutionNodeIHTIO
 *
 * Built-In Predicate Solution Node, for predicates which have the form: 
 *
 *    do_something(InList, OutHead, OutTail, InErrors, OutErrors)
 *
 * IHTIO stands for In, Head, Tail, In, Out.
 *
 * For the purpose of recursion, instead of producing an Output list,
 * it may be more convenient to return the Head and Tail separately,.
 * Consider:
 *
 *    a_rule(In, [H | T2], InErrors, OutErrors) :-
 *               do_something(In, H, T1, InErrors, Errors2),
 *               a_rule(T1, T2, Errors2, OutErrors).
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class BIPSolutionNodeIHTIO extends SolutionNode {

   BuiltInPredicateIHTIO goal;
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
   public BIPSolutionNodeIHTIO(BuiltInPredicateIHTIO goal,
                               KnowledgeBase kb,
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

      goal.evaluate(getParentSolution());   // Do the logical business.

      Unifiable newHead   = goal.getHead();
      if (newHead == null) return null;
      Unifiable newTail   = goal.getTail();
      Unifiable newErrors = goal.getErrors();

      Unifiable outHead   = goal.getTerm(1);
      Unifiable outTail   = goal.getTerm(2);
      Unifiable outErrors = goal.getTerm(4);

      SubstitutionSet solution;
      solution = newErrors.unify(outErrors, getParentSolution());
      solution = newHead.unify(outHead, solution);
      if (newTail != null) solution = newTail.unify(outTail, solution);

      return solution;
   }

}
