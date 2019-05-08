/**
 * BIPSolutionNodeIHT
 *
 * Built-In Predicate Solution Node, for predicates which have the form: 
 *
 *    do_something(InList, OutHead, OutTail)
 *
 * IHT stands for In, Head, Tail.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

public class BIPSolutionNodeIHT extends SolutionNode {

   BuiltInPredicateIHT goal;
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
   public BIPSolutionNodeIHT(BuiltInPredicateIHT goal,
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

      Unifiable newHead = goal.evaluate(getParentSolution());  // Do the logical business.
      if (newHead == null) return null;
      Unifiable newTail   = goal.getTail();

      Unifiable outHead   = goal.getTerm(1);
      Unifiable outTail   = goal.getTerm(2);

      SubstitutionSet solution;
      solution = newHead.unify(outHead, getParentSolution());
      if (newTail != null) solution = newTail.unify(outTail, solution);

      return solution;
   }

}
