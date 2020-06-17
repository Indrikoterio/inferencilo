/**
 * JoinHeadTail
 *
 * Function to create a Prolog list by joining a head
 * to a tail. The input parameters might be Variables.
 * This function takes two parameters (H, T). Extra are ignored.
 *
 * @author   Klivo
 * @version  1.0
 */

package inferencilo;

import java.util.*;

public class JoinHeadTail extends Function {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public JoinHeadTail(Unifiable... parameters) {
      super("JoinHeadTail", parameters);
   }

   /**
    * toString
    */
   public String toString() {
      if (parameters.length >= 2) {
         Unifiable head = parameters[0];
         Unifiable tail = parameters[1];
         return functionName + " " + head + " " + tail;
      }
      else {
         return functionName;
      }
   }

   /**
    * evaluate
    *
    * Evaluate the parameters.
    *
    * The first (Unifiable) parameter represents the head of a list.
    * The second represents the tail. This method joins these to
    * make a new plist. (Prolog list, that is, a recursive list.)
    *
    * @param   substitution set
    * @param   array of unifiable (at least 2)
    * @return  new prolog list
    */
   public Unifiable evaluate(SubstitutionSet ss, Unifiable... param) {
      if (param.length < 2) return null;
      List<Unifiable> outList = new ArrayList<Unifiable>();
      Unifiable head = param[0];
      outList.add(head);
      PList plist = castPList(param[1], ss);
      while (plist != null) {
         head = plist.getHead();
         if (head == null) break;
         outList.add(head);
         plist = plist.getTail();
      }
      return new PList(false, outList);
   }


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newParameters = new Unifiable[parameters.length];
      for (int i = 0; i < parameters.length; i++) {
         newParameters[i] = standardizeParameter(parameters[i], newVars);
      }
      return new JoinHeadTail(newParameters);
   }


}
