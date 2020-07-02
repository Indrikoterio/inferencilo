/**
 * ConjunctionListBase
 *
 * A conjunction list has the form: word1, word2 conjunction word3.
 *
 * This is a base class for predicates which are used to build
 * conjunction lists. Current child classes are StartList, CommaWord
 * and ConjunctionWord.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public abstract class ConjunctionListBase extends BuiltInPredicateIHT
                                          implements Unifiable, Goal {

   String[]  itemTypes; // list item types, eg. "conjunction", "noun", "adjective"

   /**
    * constructor
    *
    * @param  name of predicate
    * @param  array of item types ("comma", "noun", "adjective", etc.)
    * @param  unifiable arguments
    */
   public ConjunctionListBase(String name, String[] itemTypes, Unifiable... arguments) {
      super(name, arguments);
      this.itemTypes = itemTypes;
   }


   /*
    * checkWordType
    *
    * This method checks to see if the type of the given word matches
    * one of the list-item types. For example, if the word term is
    * noun('book'), does 'noun' appear in itemTypes. Return true if so.
    *
    * Note: The first items in the itemType are the functors for 'comma'
    * and 'conjunction'. These is skipped when checking the word type.
    * 
    * @param   word (Complex term)
    * @return  t/f
    */
   boolean checkWordType(Complex word) {
      String functor = "" + word.getTerm(0);
      for (int i = 2; i < itemTypes.length; i++) {
         String itemType = itemTypes[i];
         if (functor.equals(itemType)) return true;
      }
      return false;
   }

   /*
    * checkComma
    *
    * This method checks if the functor of the given complex term
    * matches the first item in the itemType array. (Normally 'comma')
    * 
    * @param   term (Complex term)
    * @return  t/f
    */
   boolean checkComma(Complex term) {
      String functor = "" + term.getTerm(0);
      if (itemTypes.length < 1) return false;
      if (functor.equals(itemTypes[0])) return true;
      return false;
   }

   /*
    * checkConjunction
    *
    * This method checks if the functor of the given complex term
    * matches the second item in the itemType array. (Normally 'conjunction')
    * 
    * @param   term (Complex term)
    * @return  t/f
    */
   boolean checkConjunction(Complex term) {
      String functor = "" + term.getTerm(0);
      if (itemTypes.length < 2) return false;
      if (functor.equals(itemTypes[1])) return true;
      return false;
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
      return new BIPSolutionNodeIHT(this, knowledge, parentSolution, parentNode);
   }

}
