/**
 * PList
 *
 * PList represents a singly linked list. This list object is called
 * PList (i.e. Prolog List) to avoid confusion with Java Lists.
 *
 * Examples:
 *            [a, b, c]
 *            [$X, $Y, $Z]
 *            [$X, $Y | $Z]
 *
 * Note: PLists are recursive structures. [a, b, c] can be represented
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
 * There are several ways to instantiate a PList. For example:
 *
 * 1. PList list = new PList(false, a, b, c);   // a, b, and c are Constants
 * 2. PList list = PList.parse("[a, b, c]");     // Parse string.
 *
 * 3. PList list = new PList(true, a, b, c, X);  // X is a tail Variable
 * 4. PList list = PList.parse("[a, b, c | $X]"); // Parse string.
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

public class PList implements Unifiable {

   private String printName = "LIST";
   private static int nextId = 1;
   private int id;

   private Unifiable  term;
   private PList      tail;       // Null = end of list

   private int  count;  // Number of elements in this list.

   public  static PList empty = new PList();

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
    * PList constructor for empty list
    */
   public PList() {
      id = nextId++;
      term    = null;
      tail    = null;
      count   = 0;
      isTailVar = false;
   } // constructor


   /**
    * PList constructor
    *
    * Makes a Prolog-style list, such as [a, b, c]
    *
    *     PList list = new PList(false, a, b, c);
    *
    * The first parameter, hasPipe, is set true for lists which have a tail
    * variable, such as [a, b, c | $T]
    *
    *     PList list = new PList(true, a, b, c, $T);
    *
    * @param   hasPipe t/f
    * @param   array of unifiable arguments.
    */
   public PList(boolean hasPipe, Unifiable... args) {
      super();
      if (args.length == 0) return;
      PList tailList = null;
      boolean tailVar = hasPipe;
      int num = 1;
      int lastIndex = args.length - 1;
      for (int i = lastIndex; i > 0; i--) {
         Unifiable term = args[i];
         if (i == lastIndex && PList.class.isInstance(term)) {
            // If the last term is empty [], there is no need
            // to add it to the tail.
            tailList = (PList)term;
            if (tailList.term == null) tailList = null;
            else {  // Get number of terms in list.
               num = tailList.count() + 1;
            }
         }
         else {
            tailList = new PList(tailVar, term, tailList, num);
            num++;
         }
         tailVar = false;
      }
      term = args[0];
      tail = tailList;
      count = num;
   } // constructor


   /**
    * PList constructor
    *
    * Makes a Prolog-style list, such as [a, b, c]
    *
    *     PList list = new PList(false, a, b, c);
    *
    * The first parameter, hasPipe, is set true for lists which have a tail
    * variable, such as [a, b, c | $T]
    *
    *     PList list = new PList(true, a, b, c, $T);
    *
    * @param   hasPipe t/f
    * @param   list of unifiable arguments.
    */
   public PList(boolean hasPipe, List<Unifiable> args) {
      super();
      if (args.size() == 0) return;
      PList tailList = null;
      boolean tailVar = hasPipe;
      int num = 1;
      int lastIndex = args.size() - 1;
      for (int i = lastIndex; i > 0; i--) {
         Unifiable term = args.get(i);
         if (i == lastIndex && PList.class.isInstance(term)) {
            // If the last term is empty [], there is no need
            // to add it to the tail.
            tailList = (PList)term;
            if (tailList.term == null) tailList = null;
            else {  // Get number of terms in list.
               num = tailList.count() + 1;
            }
         }
         else {
            tailList = new PList(tailVar, term, tailList, num);
            num++;
         }
         tailVar = false;
      }
      term = args.get(0);
      tail = tailList;
      count = num;
   } // constructor


   /**
    * PList constructor
    *
    * @param   isTailVar t/f
    * @param   head term
    * @param   tail PList
    * @param   count - number of elements
    */
   private PList(boolean isTailVar, Unifiable h, PList t, int num) {
      id = nextId++;
      this.term = h;
      this.tail = t;
      this.isTailVar = isTailVar;
      // If this is the tail var...
      if (isTailVar) {
         this.tail = null;
      }
      count = num;
   } // constructor


   /**
    * parse
    *
    * Factory method to create a Prolog list.
    *
    * Usage. PList.parse("[boss, Carl, Jim]")
    * or     PList.parse("[subject, verb | $T]")
    *
    * @param   string representing a Prolog list.
    * @return  PList object
    */
   public static PList parse(String str) {

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
      PList list = empty;   // Start with empty list.

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
      int roundDepth = 0;   // depth of round parenthesis (())
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
               if (term != null) list = new PList(false, term, list, count++);
               endIndex = i;
            }
            else if (i == 0) {  // final
               strTerm = arguments.substring(0, endIndex);
               term = Make.term(strTerm);
               list = new PList(false, term, list, count++);
            }
            else if (ch == '|') {  // There must be a tail variable.
               strTerm = arguments.substring(i + 1, endIndex);
               term = Make.term(strTerm);
               list = new PList(true, term, list, count++);
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
      PList theTail = tail;
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
      PList theTail = tail;
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
   public PList getTail() {
      if (tail == null) return empty;
      return tail;
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
      PList pList = this;
      Unifiable head = pList.getHead();
      while (head != null) {
         count++;
         pList = pList.getTail();
         head = pList.getHead();
         if (pList.isTailVar() && !Anon.class.isInstance(head)) {
            Variable hVar  = (Variable)(head);
            PList term = ss.castPList(hVar);
            if (term != null && PList.class.isInstance(term)) {
               pList = (PList)term;
               head = pList.getHead();
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
    * PList [H1, H2 | T] becomes an array of H1, H2, T.
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
      PList plist = this;
      Unifiable[] theArray = new Unifiable[numOfTerms + 1];
      int i = 0;
      for (; i < numOfTerms; i++) {
         if (plist == null) return null;
         Unifiable head = plist.getHead();
         if (plist.isTailVar() && !Anon.class.isInstance(head)) {
            Variable hVar  = (Variable)(head);
            PList term = ss.castPList(hVar);
            if (term != null && PList.class.isInstance(term)) {
               plist = (PList)term;
               head = plist.getHead();
            }
         }
         if (head == null) return null;
         theArray[i] = head;
         plist = plist.getTail();
      }
      theArray[i] = plist;
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

      if (other instanceof PList) {

         // Empty lists have the same ID.
         // [] == []
         if (other == this) return ss;

         SubstitutionSet newSS = new SubstitutionSet(ss);

         PList thisList = this;
         Unifiable thisTerm;
         boolean thisIsTailVar;

         PList otherList = (PList)other;
         Unifiable otherTerm;
         boolean otherIsTailVar;

         while (thisList != null && otherList != null) {

            thisTerm = thisList.getHead();
            otherTerm = otherList.getHead();
            thisIsTailVar = thisList.isTailVar();
            otherIsTailVar = otherList.isTailVar();

            if (thisIsTailVar && otherIsTailVar) {
               if (otherTerm instanceof Anon) return newSS;
               if (thisTerm instanceof Anon) return newSS;
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

         if (thisList == null && otherList == null) { return newSS; }

         // Perhaps one of the lists has run out of items. (== null)
         if (thisList == null && otherList != null) {
            if (otherList.isTailVar()) {
               otherTerm = otherList.getHead();
               SubstitutionSet result = otherTerm.unify(empty, newSS);
               return result;
            }
            return null;
         }
         else
         if (thisList != null && otherList == null) {
            if (thisList.isTailVar()) {
               thisTerm = otherList.getHead();
               SubstitutionSet result = thisTerm.unify(empty, newSS);
            }
            return null;
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
      PList thisList = this;
      boolean hasPipe = thisList.isTailVar();
      Unifiable term = thisList.getHead();
      Unifiable newTerm = null;
      if (term == null) return this;  // Must be empty list.

      while (term != null) {
         newTerm = (Unifiable)term.replaceVariables(ss);

         if (newTerm instanceof PList) {   // flatten it
            PList list = (PList)newTerm;
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

      PList result = new PList(false, newTerms);
      return result;
   }


   /**
    * standardizeVariablesApart()
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      ArrayList<Unifiable> newTerms = new ArrayList<Unifiable>();
      PList thisList = this;
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
      PList result = new PList(hasPipe, newTerms);
      return result;
   }

} // PList

/*
	Scan the list from head to tail,
	Curse recursion, force a fail.
	Hold your chin, hypothesize.
	Predicate logic never lies.
*/
