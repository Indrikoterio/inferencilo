/**
 * Rule
 *
 * Defines a Prolog rule or fact. Eg:
 *    grandfather(G, C) :- father(G, A), father(A, C).   // Prolog rule, head :- body
 *    father(john, kaitlyn).                             // Prolog fact.
 *
 * @author  Klivo
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
    * For rules without a body (ie. facts).
    * Create a fact from a string representation.
    * For example: genre(jazz).
    *
    * @param  head (String)
    */
   public Rule(String str) {
      this(new Complex(str), null);
   }

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
         return "" + head + ".";
      }
      else {
         return "" + head + " :- " + body + ".";
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
