/**
 * PrintList
 *
 * This built-in predicate prints out a list in a readable form.
 * It's mainly for debugging purposes.
 *
 * Example:
 *    $List1 = [pronoun(They, subject, third, plural), verb(envy, present, base),
 *              pronoun(us, object, first, plural), period(.)],
 *    print_list($List1).
 *
 * Prints:
 *    [pronoun(They, subject, third, plural), verb(envy, present, base)...
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class PrintList extends BuiltInPredicate {

   private static final String NAME = "PRINT_LIST";

   /**
    * constructor
    *
    * @param  unifiable arguments
    */
   public PrintList(Unifiable... arguments) {
      super(NAME, arguments);
   }

   /**
    * constructor
    *
    * This constructor takes a string of arguments, such as:
    *    "cherry, [strawberry, blueberry], $X, $Out"
    * and parses it to produce an array of Unifiable arguments.
    *
    * @param  arguments (String)
    */
   public PrintList(String str) {

      super(NAME);
      arguments = Make.splitTerms(str, ',')
                      .stream()
                      .map(Make::term)
                      .toArray(Unifiable[]::new);

   }  // constructor


   /*
    * getGround
    *
    * If the term is a LogicVar, return its ground term.
    * Else, return the term unchanged.
    *
    * @param  term
    * @param  substitution set
    * @return ground term
    */
   private Unifiable getGround(Unifiable term, SubstitutionSet ss) {
      if (term instanceof LogicVar) {
         if (ss.isGround((LogicVar)term)) {
            return ss.getGroundTerm((LogicVar)term);
         }
      }
      return term;
   } // getGround


   /*
    * showList
    *
    * Prints out all terms in a list.
    *
    * @param  list
    * @param  substitution set
    */
   private void showList(SLinkedList listo, SubstitutionSet ss) {
      SLinkedList sList = listo;
      Unifiable head = sList.getHead();
      if (head == null) {
         System.out.print("");
      }
      else {
         System.out.print(getGround(head, ss));
      }
      while (head != null) {
         sList = sList.getTail();
         head = sList.getHead();
         if (sList.isTailVar() && head != Anon.anon) {
            LogicVar hVar  = (LogicVar)(head);
            SLinkedList term = ss.castSLinkedList(hVar);
            if (term != null) {
               sList = (SLinkedList)term;
               head = sList.getHead();
            }
         }
         if (head == null) break;
         System.out.print(", " + getGround(head, ss));
      }
      System.out.println("");
   } // showList


   /**
    * evaluate
    *
    * Prints out an identifying message and the given terms.
    *
    * @param  parentSolution
    * @return new solution
    */
   public SubstitutionSet evaluate(SubstitutionSet parentSolution) {

      if (arguments.length == 0) return parentSolution;
      for (Unifiable term : arguments) {
         Unifiable term2 = getGround(term, parentSolution);
         if (term2 instanceof SLinkedList) showList((SLinkedList)term2, parentSolution);
      }
      return parentSolution;

   } // evaluate

   /**
    * standardizeVariablesApart()
    * Refer to Expression.java for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, LogicVar> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      return new PrintList(newArguments);
   } // standardizeVariablesApart

}  // PrintList
