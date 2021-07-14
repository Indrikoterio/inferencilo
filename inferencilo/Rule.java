/**
 * Rule
 *
 * Defines a Prolog rule or fact. Eg:
 *    grandfather(G, C) :- father(G, A), father(A, C).   // Prolog rule, head :- body
 *    father(john, kaitlyn).                             // Prolog fact.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Rule implements Expression {

   private Complex head;
   private Goal    body;

   /**
    * constructor
    *
    * For rules without a body (ie. facts).
    *
    * @param  head (Complex term)
    */
   public Rule(Complex head) {
      this(head, null);
   }


   /**
    * constructor
    *
    * Create a fact or rule from a string representation.
    *
    * For example:
    *    genre(jazz).
    *    parent($X, $Y) :- father($X, $Y); mother($X, $Y).
    *
    * @param  fact or rule
    * @throws InvalidRuleException
    */
   public Rule(String str) {

      String s = str.trim();
      if (s.endsWith(".")) {
         int len = s.length();
         s = s.substring(0, len - 1);
      }
      int index = s.indexOf(":-");

      if (index > -1) {
         String head = s.substring(0, index);
         String body = s.substring(index + 2);
         // Make sure there is not a second ':-' .
         if (body.indexOf(":-") >= 0)
            throw new InvalidRuleException(":- occurs twice:\n" + str);
         this.head = new Complex(head);
         Tokenizer tok = Tokenizer.getTokenizer();
         this.body = tok.generateGoal(body);
      }
      else {  // Must be a fact (no body).
         this.head = new Complex(s);
         this.body = null;
      }
   } // constructor


   /**
    * constructor
    *
    * A rule consists of a head term and a tail term.
    * For example:  parent($X, $Y) :- mother($X, $Y).
    *
    * @param  head (Complex)
    * @param  body (Goal)
    */
   public Rule(Complex head, Goal body) {
      this.head = head;
      this.body = body;
   }


   /**
    * constructor
    *
    * A rule consists of a head term and a tail term.
    * For example:  parent($X, $Y) :- mother($X, $Y).
    *
    * @param  head (String)
    * @param  body (Goal)
    */
   public Rule(String str, Goal body) {
      this.head = new Complex(str);
      this.body = body;
   }


   /**
    * getHead
    *
    * @return head term (Complex)
    */
   public Complex getHead() {
      return head;
   }

   /**
    * getBody
    *
    * @return body of rule (Goal)
    */
   public Goal getBody() {
      return body;
   }

   /**
    * replaceVariables
    *
    * Refer to Expression for full comments.
    *
    * @param   substitution set
    * @return  new expression, without variables
    */
   public Expression replaceVariables(SubstitutionSet s) {
      return body.replaceVariables(s);
   }

   /**
    * toString
    *
    * For display only. Eg. mortal($X) :- man($X).
    *
    * @return string
    */
   public String toString() {
      if (body == null) {
         return head.toString() + ".";
      }
      else {
         return head.toString() + " :- " + body + ".";
      }
   }

   /**
    * key
    *
    * Produce a key for Knowledge Base. Predicate name + arity.
    * Eg.: father(john, mary) =>  father/2
    *
    * @return  key (String)
    */
   public String key() { return head.key(); }


   /**
    * standardizeVariablesApart()
    *
    * Refer to Expression interface for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {

      Complex newHead = (Complex)head.standardizeVariablesApart(newVars);
      Goal newBody = null;
      if (body != null) {
         newBody = (Goal)body.standardizeVariablesApart(newVars);
      }

      return new Rule(newHead, newBody);
   }

}
