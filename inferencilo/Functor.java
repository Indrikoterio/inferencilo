/**
 * Functor
 *
 * Built-in predicate to get the functor and arity of a term.
 * Use thusly:
 *
 *     functor(boss(Zack, Stephen), $F, $A)
 *
 * $F will bind to 'boss' and $A will bind to '3'.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Functor extends BuiltInPredicate implements Unifiable, Goal {

   private static final String NAME = "FUNCTOR";

   private String functor;
   private int arity;

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public Functor(Unifiable... arguments) {
      super(NAME, arguments);
   }

   /**
    * constructor
    *
    * This constructor takes a string, such as:
    *
    *   "boss(Zack, Stephen), $F, $A"
    *
    * and produces the represented arguments. There must be at least 2.
    *
    * @param  argument string
    */
   public Functor(String str) {
      super(NAME);
      List<String> strTerms = Make.splitTerms(str, ',');
      int size = strTerms.size();
      if (size < 2 || size > 3)
         throw new FatalParsingException(
               "functor() takes 2 or 3 arguments: " + str);
      Unifiable[] terms = new Unifiable[size];
      int index = 0;
      for (String strTerm : strTerms) {
         Make.addTerm(strTerm, terms, index++);
      }
      arguments = terms;
   }  // constructor


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
      return new FunctorSolutionNode(this, knowledge, parentSolution, parentNode);
   }


   /**
    * numOfArguments
    *
    */
   public int numOfArguments() { return arguments.length; }

   /**
    * getFunctor
    *
    * @return functor
    */
   public String getFunctor() { return functor; }

   /**
    * getArity
    *
    * @return arity
    */
   public int getArity() { return arity; }


   /**
    * evaluate
    *
    * Determines the functor and arity of the first term.
    * If the evaluation succeeds, return an anonymous variable $_,
    * which is not used. If there is a failure, return null.
    *
    * @param  substitution set
    * @return anonymous variable or null
    */
   public Unifiable evaluate(SubstitutionSet ss) {

      // Get first argument.
      Complex first = castComplex(arguments[0], ss);
      if (first != null) {
         functor = first.functor();
         arity = first.arity();
      }
      else {
         Constant conTerm = castConstant(arguments[0], ss);
         if (conTerm == null) return null;
         functor = "" + conTerm;
         arity = 0;
      }
      return Anon.anon;   // return value not needed

   } // evaluate

}  // Functor
