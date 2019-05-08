/**
 * StartList
 *
 * This class is used for building lists.
 *
 * A list has the form: word1, word2, word3, word4 conjunction word5.
 *
 * This class collects the first word of the list.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class StartList extends ConjunctionListBase implements Unifiable, Goal {

   Unifiable newHead;   // of list
   Unifiable newTail;   // of list

   /**
    * constructor
    *
    * Note: For this class, the first array element of itemTypes is ignored.
    * That is, if item types has ["conjunction", "noun", "adjective"],
    * 'conjunction' is ignored.
    *
    * @param  array of item types
    * @param  unifiable arguments
    */
   public StartList(String[] itemTypes, Unifiable... arguments) {
      super("start_list", itemTypes, arguments);
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

      PList listo = castPList(arguments[0], ss);
      if (listo == null) return null;

      Unifiable[] termoj = listo.flatten(1);  // Get terms. (H, T)
      if (termoj == null) return null;

      Complex c1 = castComplex(termoj[0], ss);
      if (c1 == null) return null;
      if (!checkWordType(c1)) return null;

      newHead = c1;
      newTail = termoj[1];
      return newHead;
   }

   public Unifiable getHead() { return newHead; }
   public Unifiable getTail() { return newTail; }


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new StartList(itemTypes, newArguments);
   }


}
