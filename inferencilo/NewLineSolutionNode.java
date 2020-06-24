/**
 * NewLineSolutionNode
 *
 * Solution Node for the NewLine function. The NewLine function
 * simply prints out an empty new line.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class NewLineSolutionNode extends SolutionNode {

   NewLine goal;
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
   public NewLineSolutionNode(NewLine goal, KnowledgeBase kb,
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

      SubstitutionSet ss = getParentSolution();
      goal.evaluate(ss);  // Do the logical business.
      return ss;
   }

} // NewLineSolutionNode
