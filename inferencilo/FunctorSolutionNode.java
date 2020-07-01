/**
 * FunctorSolutionNode
 *
 * Solution Node for functor function. Functor takes 2 or 3 arguments.
 *
 *   functor(alphabet(a, b, c), $Functor, $Arity)
 *     $Functor binds to 'alphabet'
 *     $Arity binds to '3'
 *
 * This predicate has a special feature. If the second parameter is
 * a constant, and it has an asterisk, such as "alpha*", then the
 * match is valid. Thus, this goal,
 *
 *    functor(alphabet(a, b, c), alpha*)
 *
 * will succeed.
 * 
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class FunctorSolutionNode extends SolutionNode {

   Functor goal;
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
   public FunctorSolutionNode(Functor goal, KnowledgeBase kb,
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

      Unifiable result = goal.evaluate(getParentSolution());
      if (result == null) return null;

      String functor = goal.getFunctor();
      int arity = goal.getArity();

      int num = goal.numOfArguments();
      SubstitutionSet ss = getParentSolution();
      if (num > 1) {
         Unifiable term = goal.getTerm(1);
         if (term instanceof Constant) {
            String f2 = "" + term;
            if (f2.endsWith("*")) {
               // Strip off asterisk.
               f2 = f2.substring(0, f2.length() - 1);
               if (!functor.startsWith(f2)) return null;
            }
            else {
               if (!functor.equals(f2)) return null;
            }
         }
         else {
            ss = term.unify(new Constant(functor), ss);
         }
      }
      if (num > 2) {
         Unifiable term = goal.getTerm(2);
         ss = term.unify(new Constant("" + arity), ss);
      }
      return ss;
   } // nextSolution

} // FunctorSolutionNode
