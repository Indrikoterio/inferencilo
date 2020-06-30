/**
 * PrintSolutionNode
 *
 * Solution Node for Print function.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class PrintSolutionNode extends SolutionNode {

   Print goal;
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
   public PrintSolutionNode(Print goal,
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

      SubstitutionSet ss = getParentSolution();
      goal.evaluate(ss);  // Do the logical business.
      return ss;
   }

} // PrintSolutionNode
