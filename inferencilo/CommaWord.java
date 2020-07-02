/**
 * CommaWord
 *
 * This class is used for building lists.
 *
 * A list has the form: word1, word2, word3, word4 conjunction word5.
 *
 * Lists will be analysed as having a start, middle, and end.
 * The start consists of one word (word1). The middle consists of
 * 'comma word' pairs. That is: (, word2) (, word3) (, word4)
 * The end consists of an optional comma, a conjunction and a word
 * (word5).
 *
 * This class collects comma-word pairs.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class CommaWord extends ConjunctionListBase implements Unifiable, Goal {

   Unifiable newHead;   // of list
   Unifiable newTail;   // of list

   /**
    * constructor
    *
    * @param  array of item types ("comma", "noun", "adjective", etc.)
    * @param  unifiable arguments
    */
   public CommaWord(String[] itemTypes, Unifiable... arguments) {
      super("comma_word", itemTypes, arguments);
   }

   /**
    * evaluate the parameters
    *
    * @param   substitution set of parent
    * @return  new unifiable
    */
   public Unifiable evaluate(SubstitutionSet ss) {

      if (arguments.length < 3) return null;
      if (itemTypes.length < 2) return null;

      PList list = castPList(arguments[0], ss);
      if (list == null) return null;

      Unifiable[] termoj = list.flatten(2);  // Get terms. (H1, H2, T)
      if (termoj == null) return null;

      Complex c1 = castComplex(termoj[0], ss);
      if (c1 == null) return null;
      if (!checkComma(c1)) return null;

      Complex c2 = castComplex(termoj[1], ss);
      if (c2 == null) return null;
      if (!checkWordType(c2)) return null;

      newHead = new PList(false, c1, c2);
      newTail = termoj[2];
      return newHead;
   }

   public Unifiable getHead() { return newHead; }
   public Unifiable getTail() { return newTail; }

} // CommaWord
