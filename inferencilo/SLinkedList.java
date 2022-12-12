/**
 * SLinkedList
 *
 * SLinkedList represents a singly linked list.
 *
 * Examples:
 *            [a, b, c]
 *            [$X, $Y, $Z]
 *            [$X, $Y | $Z]
 *
 * Note: SLinkedLists are recursive structures. [a, b, c] can be represented
 * as [a [b [c]]].
 *
 * A pipe, |, is used to divide the list between head terms and the tail,
 * which is everything left over. Thus, in the following code,
 *
 *    [a, b, c, d, e] = [$X, $Y | $Z]
 *
 *  $X is bound to 'a'
 *  $Y is bound to 'b'
 *  $Z is bound to [c, d, e]
 *
 * There are several ways to instantiate a SLinkedList. For example:
 *
 * 1. SLinkedList list = new SLinkedList(false, a, b, c);   // a, b, and c are Constants
 * 2. SLinkedList list = SLinkedList.parse("[a, b, c]");    // Parse string.
 *
 * 3. SLinkedList list = new SLinkedList(true, a, b, c, X);   // X is a tail Variable
 * 4. SLinkedList list = SLinkedList.parse("[a, b, c | $X]"); // Parse string.
 *
 * For the constructor method, the first parameter must be true if
 * the last item is a tail variable (See #3.).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class SLinkedList implements Unifiable {

   private String printName = "LIST";

   private Unifiable    term;
   private SLinkedList  next;       // Null = end of list

   private int  count;  // Number of elements in this list.

   public  static SLinkedList empty = new SLinkedList();

   /* About isTailVar:

      It is necessary to distinguish between
            [$A, $B, $C]
       and
            [$A, $B | $C]
      If the last item in a list is a variable ($C above), it can be
      an ordinary variable, as in the first list, or a tail variable,
      as in the second. A tail variable will unify with the the tail
      of another list. An ordinary variable will not; it only unifies
      with one term from the other list.
    */
   private boolean isTailVar;


   /**
    * SLinkedList constructor for empty list
    */
   public SLinkedList() {
      term    = null;
      next    = null;
      count   = 0;
      isTailVar = false;
   } // constructor


   /**
    * SLinkedList constructor
    *
    * Makes a Prolog-style list, such as [a, b, c]
    *
    *     SLinkedList list = new SLinkedList(false, a, b, c);
    *
    * The first parameter, hasPipe, is set true for lists which have a tail
    * variable, such as [a, b, c | $T]
    *
    *     SLinkedList list = new SLinkedList(true, a, b, c, $T);
    *
    * @param   hasPipe t/f
    * @param   array of unifiable arguments.
    */
   public SLinkedList(boolean hasPipe, Unifiable... args) {
      if (args.length == 0) return;
      SLinkedList tailList = null;
      boolean tailVar = hasPipe;
      int num = 1;
      int lastIndex = args.length - 1;
      for (int i = lastIndex; i > 0; i--) {
         Unifiable term = args[i];
         if (i == lastIndex && SLinkedList.class.isInstance(term)) {
            // If the last term is empty [], there is no need
            // to add it to the tail.
            tailList = (SLinkedList)term;
            if (tailList.term == null) tailList = null;
            else {  // Get number of terms in list.
               num = tailList.count() + 1;
            }
         }
         else {
            tailList = new SLinkedList(tailVar, term, tailList, num);
            num++;
         }
         tailVar = false;
      }
      term = args[0];
      next = tailList;
      count = num;
   } // constructor


   /**
    * SLinkedList constructor
    *
    * Makes a Prolog-style list, such as [a, b, c]
    *
    *     SLinkedList list = new SLinkedList(false, a, b, c);
    *
    * The first parameter, hasPipe, is set true for lists which have a tail
    * variable, such as [a, b, c | $T]
    *
    *     SLinkedList list = new SLinkedList(true, a, b, c, $T);
    *
    * @param   hasPipe t/f
    * @param   list of unifiable arguments.
    */
   public SLinkedList(boolean hasPipe, List<Unifiable> args) {
      if (args.size() == 0) return;
      SLinkedList tailList = null;
      boolean tailVar = hasPipe;
      int num = 1;
      int lastIndex = args.size() - 1;
      for (int i = lastIndex; i > 0; i--) {
         Unifiable term = args.get(i);
         if (i == lastIndex && SLinkedList.class.isInstance(term)) {
            // If the last term is empty [], there is no need
            // to add it to the tail.
            tailList = (SLinkedList)term;
            if (tailList.term == null) tailList = null;
            else {  // Get number of terms in list.
               num = tailList.count() + 1;
            }
         }
         else {
            tailList = new SLinkedList(tailVar, term, tailList, num);
            num++;
         }
         tailVar = false;
      }
      term = args.get(0);
      next = tailList;
      count = num;
   } // constructor


   /**
    * SLinkedList constructor
    *
    * @param   isTailVar t/f
    * @param   head term
    * @param   tail SLinkedList
    * @param   count - number of elements
    */
   private SLinkedList(boolean isTailVar, Unifiable h, SLinkedList t, int num) {
      this.term = h;
      this.next = t;
      this.isTailVar = isTailVar;
      // If this is the tail var...
      if (isTailVar) {
         this.next = null;
      }
      count = num;
   } // constructor


   /**
    * parse
    *
    * Factory method to create a Prolog list.
    *
    * Usage. SLinkedList.parse("[boss, Carl, Jim]")
    * or     SLinkedList.parse("[subject, verb | $T]")
    *
    * @param   string representing a Prolog list.
    * @return  SLinkedList object
    */
   public static SLinkedList parse(String str) {

      String      strTerm;
      Unifiable   term;
      boolean     hasPipe = false;

      int bracket1 = str.indexOf("[");
      int bracket2 = str.lastIndexOf("]");

      if (bracket1 == -1 || bracket2 < bracket1) {
         System.out.println("Ooops. Invalid list: " + str);
         return null;
      }

      String arguments = str.substring(bracket1 + 1, bracket2).trim();
      int argLength = arguments.length();  // length of string
      SLinkedList list = empty;   // Start with empty list.

      // Check for empty list.
      if (argLength == 0) return list;

      // Check if pipe is valid.
      int indexOfPipe = arguments.indexOf('|');
      if (indexOfPipe >= 0) {
         hasPipe = true;
         int indexOfComma = arguments.lastIndexOf(',');
         if (indexOfComma >= 0 && indexOfPipe < indexOfComma) {
            System.out.println("Whoa! Commas after pipe: " + str);
            return null;
         }
      }

      int endIndex = argLength;
      int roundDepth = 0;   // depth of round parentheses (())
      int squareDepth = 0;   // depth of square brackets [[]]

      // Iterate backwards.
      int count = 1;  // Count the number of list elements.
      for (int i = argLength - 1; i >= 0; i--) {
         char ch = arguments.charAt(i);
         if (ch == ']') squareDepth++;
         else if (ch == '[') squareDepth--;
         else if (ch == ')') roundDepth++;
         else if (ch == '(') roundDepth--;
         else if (roundDepth == 0 && squareDepth == 0) {
            if (ch == ',') {
               strTerm = arguments.substring(i + 1, endIndex);
               term = Make.term(strTerm);
               if (term != null) list = new SLinkedList(false, term, list, count++);
               endIndex = i;
            }
            else if (i == 0) {  // final
               strTerm = arguments.substring(0, endIndex);
               term = Make.term(strTerm);
               list = new SLinkedList(false, term, list, count++);
            }
            else if (ch == '|') {  // There must be a tail variable.
               strTerm = arguments.substring(i + 1, endIndex);
               term = Make.term(strTerm);
               list = new SLinkedList(true, term, list, count++);
               endIndex = i;
            }
         }
      }
      return list;

   }  // parse


   /*
    * commaString
    *
    * Produces a string of list elements separated by commas.
    * If the last list element is a tail variable, replace
    * the comma with a pipe |.
    *
    * @return  comma separated string
    */
   private String commaString() {
      String str = term.toString();
      SLinkedList theTail = next;
      while (theTail != null) {
         Unifiable term = theTail.getHead();
         if (term == null) break;
         if (theTail.isTailVar()) {
            str += " | " + term;
         }
         else { str += ", " + term; }
         theTail = theTail.getTail();
      }
      return str;
   }

   /**
    * toString
    *
    * A Prolog style list looks like this:
    *    [a, b, c]  [$H | $T]  [a, b | $T]
    *
    * @return  print form of list
    */
   public String toString() {
      if (term == null) return "[]";
      String str = commaString();
      return "[" + str + "]";
   }


   /**
    * spaceString
    *
    * Produces a string of list elements separated by spaces.
    * This method is useful for printing error messages.
    *
    * @return  space separated string
    */
   public String spaceString() {
      if (term == null) return "";
      String str = term.toString();
      SLinkedList theTail = next;
      while (theTail != null) {
         Unifiable term = theTail.getHead();
         if (term == null) break;
         str += " " + term;
         theTail = theTail.getTail();
      }
      return str;
   }


   /**
    * getHead
    *
    * Returns the head term of this list.
    *
    * @return head term.
    */
   public Unifiable getHead() { return term; }


   /**
    * getTail
    *
    * Return the tail item of this list.
    * If the tail is null, return an empty list.
    *
    * @return tail
    */
   public SLinkedList getTail() {
      if (next == null) return empty;
      return next;
   }


   /**
    * isTailVar
    *
    * Return true if the term is a tail variable.
    * False otherwise.
    *
    * @return true/false
    */
   public boolean isTailVar() { return isTailVar; }


   /**
    * count
    *
    * @return number of terms
    */
   public int count() { return count; }


   /**
    * recursiveCount
    *
    * Count the number of terms by recursion.
    * The tail term may be a variable, so this method
    * requires the substitution set.
    *
    * @param  substitution set
    * @return number of terms
    */
   public int recursiveCount(SubstitutionSet ss) {
      int count = 0;
      SLinkedList sList = this;
      Unifiable head = sList.getHead();
      while (head != null) {
         count++;
         sList = sList.getTail();
         head = sList.getHead();
         if (sList.isTailVar() && head != Anon.anon) {
            Variable hVar  = (Variable)(head);
            SLinkedList term = ss.castSLinkedList(hVar);
            if (term != null) {
               sList = (SLinkedList)term;
               head = sList.getHead();
            }
         }
      }
      return count;
   } // recursiveCount


   /**
    * flatten
    *
    * Partially flatten this recursive list.
    * If the number of terms requested is two, this
    * function will return an array of the first and
    * second terms, and the tail. In other words, the
    * SLinkedList [H1, H2 | T] becomes an array of H1, H2, T.
    *
    * If one of the head terms is null, return null,
    * because whatever processing was planned for the
    * terms cannot proceed.
    *
    * @param   number of terms (int)
    * @param   substitution set
    * @return  array of terms and tail (unifiable)
    */
   public Unifiable[] flatten(int numOfTerms, SubstitutionSet ss) {
      SLinkedList sList = this;
      Unifiable[] theArray = new Unifiable[numOfTerms + 1];
      int i = 0;
      for (; i < numOfTerms; i++) {
         if (sList == null) return null;
         Unifiable head = sList.getHead();
         if (sList.isTailVar() && head != Anon.anon) {
            Variable hVar  = (Variable)(head);
            SLinkedList term = ss.castSLinkedList(hVar);
            if (term != null) {
               sList = (SLinkedList)term;
               head = sList.getHead();
            }
         }
         if (head == null) return null;
         theArray[i] = head;
         sList = sList.getTail();
      }
      theArray[i] = sList;
      return theArray;
   } // flatten()


   /**
    * unify
    *
    * Unify is complicated for lists. If the last parameter is a
    * Variable, it can unify with the tail of another list.
    *
    * @param  other unifiable
    * @param  substitution set
    * @return new substitution set
    */
   public SubstitutionSet unify(Unifiable term, SubstitutionSet ss) {

      Unifiable other = term;

      if (other instanceof SLinkedList) {

         // Empty lists have the same ID.
         // [] == []
         if (other == this) return ss;

         SubstitutionSet newSS = ss;

         SLinkedList thisList = this;
         Unifiable thisTerm;
         boolean thisIsTailVar;

         SLinkedList otherList = (SLinkedList)other;
         Unifiable otherTerm;
         boolean otherIsTailVar;

         while (thisList != null && otherList != null) {

            thisTerm = thisList.getHead();
            otherTerm = otherList.getHead();
            thisIsTailVar = thisList.isTailVar();
            otherIsTailVar = otherList.isTailVar();

            if (thisIsTailVar && otherIsTailVar) {
               if (otherTerm == Anon.anon) return newSS;
               if (thisTerm == Anon.anon) return newSS;
               return thisTerm.unify(otherTerm, newSS);
            }
            else if (thisIsTailVar) {
               return thisTerm.unify(otherList, newSS);
            }
            else if (otherIsTailVar) {
               return otherTerm.unify(thisList, newSS);
            }
            else {
               if (thisTerm == null && otherTerm == null) return newSS;
               if (thisTerm == null || otherTerm == null) return null;
               newSS = thisTerm.unify(otherTerm, newSS);
               if (newSS == null) return null;
            }
            thisList = thisList.getTail();
            otherList = otherList.getTail();
         }

         return null;
      }
      else if (other instanceof Variable) return other.unify(this, ss);
      return null;
   }


   /**
    * replaceVariables
    *
    * Replace all bound variables with their bindings.
    *
    * @param substitution set
    * @param expression
    */
   public Expression replaceVariables(SubstitutionSet ss) {

      List<Unifiable> newTerms = new ArrayList<Unifiable>();
      SLinkedList thisList = this;
      Unifiable term = thisList.getHead();
      Unifiable newTerm = null;
      if (term == null) return this;  // Must be empty list.

      while (term != null) {
         newTerm = (Unifiable)term.replaceVariables(ss);

         if (newTerm instanceof SLinkedList) {   // flatten it
            SLinkedList list = (SLinkedList)newTerm;
            Unifiable head = list.getHead();
            while (head != null) {
               newTerms.add(head);
               list = list.getTail();
               if (list == null) break;
               head = list.getHead();
            }
         }
         else {  // not a list
            newTerms.add(newTerm);
         }
         thisList = thisList.getTail();
         if (thisList == null) break;
         term = thisList.getHead();
      }

      SLinkedList result = new SLinkedList(false, newTerms);
      return result;
   }


   /**
    * standardizeVariablesApart()
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<String, Variable> newVars) {
      ArrayList<Unifiable> newTerms = new ArrayList<Unifiable>();
      SLinkedList thisList = this;
      boolean hasPipe = thisList.isTailVar();
      Unifiable term = thisList.getHead();
      if (term == null) return empty;
      while (term != null) {
         newTerms.add((Unifiable)term.standardizeVariablesApart(newVars));
         hasPipe = thisList.isTailVar();
         thisList = thisList.getTail();
         if (thisList == null) break;
         term = thisList.getHead();
      }
      SLinkedList result = new SLinkedList(hasPipe, newTerms);
      return result;
   }

} // SLinkedList

/*
	Scan the list from head to tail,
	Curse recursion, force a fail.
	Hold your chin, hypothesize.
	Predicate logic never lies.
*/
