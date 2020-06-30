/**
 * AppendSolutionNode
 *
 * Solution Node for the built-in predicate Append.
 * Unify the resulting PList with the output argument.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class AppendSolutionNode extends SolutionNode {

   Append goal;
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
   public AppendSolutionNode(Append goal,
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
    * @throws  TimeOverrunException
    */
   public SubstitutionSet nextSolution() throws TimeOverrunException {

      if (noBackChaining()) { return null; }
      if (!moreSolutions) { return null; }
      moreSolutions = false;

      PList resultPList = (PList)goal.evaluate(getParentSolution());
      if (resultPList == null) return null;

      int num = goal.numOfArguments();
      Unifiable lastTerm = goal.getTerm(num - 1);

      return lastTerm.unify(resultPList, getParentSolution());
   }
}
