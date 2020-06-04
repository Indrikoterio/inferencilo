/**
 * PList
 *
 * Represents a Prolog list. Eg.:
 *     [a, b, c], [X, Y, Z], [X, Y | Z]
 *
 * It's called PList to avoid confusion with Java Lists.
 *
 * Note: Prolog Lists are recursive structures.
 * [a, b, c] is a representation of [a [b [c]]]
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class PList implements Unifiable {

   private String printName = "LIST";
   private static int nextId = 1;
   private int id;

   private Unifiable  term;
   private PList      tail;       // Null = end of list

   private int  count;  // Number of elements in this list.

   public  static PList empty = new PList();

   /* About isTailVar:
      It is necessary to distinguish between [$A, $B]  and  [$A | $B].
      If the last item in a list (tail == null) is a variable, it can
      be an ordinary variable or a 'tailVariable', as in the latter list
      above. A tail variable will unify with the the tail of another list.
      An ordinary variable will not; it only unifies with one term from
      the other list.
    */
   private boolean    isTailVar;


   /**
    * PList constructor for empty list
    */
   public PList() {
      id = nextId++;
      term    = null;
      tail    = null;
      count   = 0;
      isTailVar = false;
   }

   /**
    * PList constructor
    *
    * Makes a Prolog list, eg: [a, b, c]
    *
    * The parameter hasPipe is true for lists such as [a, b, c | $T]
    *
    * Note: It may seem logical to add a fact to a list using this
    * constructor, that is: new_plist = new PList(term, old_plist).
    * This does not work. It creates a new list with two terms.
    * To add a term to an existing list, use the built-in predicate
    * JoinHeadTail: new_plist = new JoinHeadTail(term, old_plist).
    *
    * @param   hasPipe t/f
    * @param   array of unifiable arguments.
    */
   public PList(boolean hasPipe, Unifiable... args) {
      super();
      if (args.length == 0) return;
      PList list = null;
      boolean tailVar = hasPipe;
      int num = 1;
      for (int i = args.length - 1; i > 0; i--) {
         list = new PList(tailVar, args[i], list, num);
         tailVar = false;
         num++;
      }
      term = args[0];
      tail = list;
      count = num;
   }

   /**
    * PList constructor
    *
    * Makes a Prolog list, eg: [a, b, c]
    *
    * The parameter hasPipe is true for lists such as [a, b, c | $T]
    *
    * Note: It may seem logical to add a fact to a list using this
    * constructor, that is: new_plist = new PList(term, old_plist).
    * This does not work. It creates a new list with two terms.
    * To add a term to an existing list, use the built-in predicate
    * JoinHeadTail. (new_plist = new JoinHeadTail(term, old_plist)).
    *
    * @param   hasPipe t/f
    * @param   list of unifiable arguments.
    */
   public PList(boolean hasPipe, List<Unifiable> args) {
      super();
      if (args.size() == 0) return;
      PList list = null;
      boolean tailVar = hasPipe;
      int num = 1;
      for (int i = args.size() - 1; i > 0; i--) {
         list = new PList(tailVar, args.get(i), list, num);
         tailVar = false;
         num++;
      }
      term = args.get(0);
      tail = list;
      count = num;
   }


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
   }


   /**
    * make
    *
    * Factory method to create a Prolog list.
    * Usage. PList.make("[boss, Carl, Jim]")
    * or     PList.make("[subject, verb | $T]")
    *
    * @param   string representing a Prolog list.
    * @return    PList object
    */
   public static PList make(String str) {

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
      PList list = new PList();   // Start with empty list.

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
      int count = 1;
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
               list = new PList(false, term, list, count);
               endIndex = i;
            }
            else if (i == 0) {  // final
               strTerm = arguments.substring(0, endIndex);
               term = Make.term(strTerm);
               list = new PList(false, term, list, count);
            }
            else if (ch == '|') {  // There must be a tail variable.
               strTerm = arguments.substring(i + 1, endIndex);
               term = Make.term(strTerm);
               list = new PList(true, term, list, count);
               endIndex = i;
            }
         }
         count++;
      }
      return list;

   }  // make


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
      String str = "" + term;
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
      String str = "" + term;
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
    * @return the head term of this list.
    */
   public Unifiable getHead() { return term; }

   /**
    * getTail
    *
    * If the tail is null, return an empty list.
    *
    * @return the tail item of this list.
    */
   public PList getTail() {
      if (tail == null) return new PList();
      return tail;
   }


   /**
    * isTailVar
    *
    * @return true if the term is a tail variable
    */
   public boolean isTailVar() { return isTailVar; }


   /**
    * count
    *
    * @return number of elements
    */
   public int count() { return count; }


   /**
    * flatten
    *
    * Flatten or partially flatten this recursive list.
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
    * @return  array of terms and tail (unifiable)
    */
   public Unifiable[] flatten(int numOfTerms) {
      PList plist = this;
      Unifiable[] theArray = new Unifiable[numOfTerms + 1];
      int i = 0;
      for (; i < numOfTerms; i++) {
         if (plist == null) return null;
         Unifiable head = plist.getHead();
         if (head == null) return null;
         theArray[i] = head;
         plist = plist.getTail();
      }
      theArray[i] = plist;
      return theArray;
   }


   /**
    * joinHeadTail
    *
    * This static method is used to join a head term with a tail (PList).
    * The constructor PList(boolean isTailVar, Unifiable h, PList t) cannot
    * be used for this. The method produces a list of unifiable terms which
    * can be used to create a new PList.
    *
    * @param  head term (Unifiable)
    * @param  tail (PList)
    * @return list of unifiable terms
    */
   public static List<Unifiable> joinHeadTail(Unifiable head, PList tail) {
      List<Unifiable> outList = new ArrayList<Unifiable>();
      outList.add(head);
      PList plist = tail;
      while (plist != null) {
         head = plist.getHead();
         if (head == null) break;
         outList.add(head);
         plist = plist.getTail();
      }
      return outList;
   }


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
               SubstitutionSet result = otherTerm.unify(new PList(), newSS);
               return result;
            }
            return null;
         }
         else
         if (thisList != null && otherList == null) {
            if (thisList.isTailVar()) {
               thisTerm = otherList.getHead();
               SubstitutionSet result = thisTerm.unify(new PList(), newSS);
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
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      ArrayList<Unifiable> newTerms = new ArrayList<Unifiable>();
      PList thisList = this;
      boolean hasPipe = thisList.isTailVar();
      Unifiable term = thisList.getHead();
      if (term == null) return new PList();
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

}
