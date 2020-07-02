/**
 * ReplaceTerm1
 *
 * This built-in predicate groups terms together to form a new
 * complex term.
 *
 * The first argument is an input complex term; the second argument
 * is the modified output.
 *
 *   replace_term1(verb(_), verb(am dancing), [am, dancing])
 *
 * The following terms can be constants, variables and/or lists.
 * This predicate concatenates their string values into a single
 * string, creates a new constant, and replaces the first term of
 * the input complex term with the newly created constant.
 *
 * For example, if Article unifies with 'the', and Adjective
 * unifies with 'blue', and Noun unifies with 'sky':
 *
 *  replace_term1(noun_phrase(wot-evah), Out, Article, Adjective, Noun)
 *
 *  Out = noun_phrase(the blue sky)
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class ReplaceTerm1 extends BuiltInPredicate implements Unifiable, Goal {

   /**
    * constructor
    *
    * @param  predicate name
    * @param  unifiable arguments
    */
   public ReplaceTerm1(Unifiable... arguments) {
      super("replace_term1", arguments);
   }


   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return new ReplaceTerm1SolutionNode(this, knowledge, parentSolution, parentNode);
   }

   /*
    * getFirstString
    *
    * If the given term is a Constant, this method will
    * return its string value. If the given term is a
    * Complex term, this method will return the string
    * value of its first term.
    *
    * @param  unifiable term
    * @return string value
    */
   private String getFirstString(Unifiable term) {
      if (term instanceof Complex) {
         Complex c = (Complex)term;
         return "" + c.getTerm(1);
      }
      else return "" + term;
   }


   /**
    * evaluate
    *
    * Append all arguments together.
    *
    * @param   substitution set of parent
    * @return  unifiable term or null
    */
   public Unifiable evaluate(SubstitutionSet ss) {

      int numOfArguments = arguments.length;
      if (numOfArguments < 3) return null;

      Unifiable firstTerm = arguments[0];
      if (firstTerm instanceof Variable) {
         if (ss != null && ss.isGround((Variable)firstTerm)) {
            firstTerm = ss.getGroundTerm((Variable)firstTerm);
         }
         else return null;
      }

      Complex inComplex = castComplex(firstTerm, ss);
      if (inComplex == null) return null;

      Unifiable[] copiedTerms = inComplex.copyTerms();
      if (copiedTerms.length < 1) return null;  // nuthin' to do

      StringBuilder sb = new StringBuilder("");
      String str;

      boolean first = true;
      for (int i = 2; i < numOfArguments; i++) {

         Unifiable term = arguments[i];
         if (term == null) continue;

         // Get grounded term.
         if (term instanceof Variable) {
            if (ss != null && ss.isGround((Variable)term)) {
               term = (Unifiable)term.replaceVariables(ss);
            }
            else continue;
         }

         if (term instanceof Constant || term instanceof Complex) {
            str = getFirstString(term);
            if (first || str.equals(",")) sb.append(str);
            else { sb.append(" "); sb.append(str); }
            first = false;
         }
         else if (term instanceof PList) {

            PList plist = (PList)term;
            while (true) {
               Unifiable head = plist.getHead();
               if (head == null) break;
               str = getFirstString(head);
               if (first || str.equals(",")) sb.append(str);
               else {
                  sb.append(" ");
                  sb.append(str);
               }
               first = false;
               plist = plist.getTail();
               if (plist == null) break;
            }
         }
      }

      Constant c = new Constant(sb.toString());
      copiedTerms[1] = c;

      //Complex cx = new Complex(copiedTerms);
      return new Complex(copiedTerms);

   }  // evaluate()

}
