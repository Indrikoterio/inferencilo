/**
 * Functor
 *
 * Function to get the functor of a term.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Functor extends Function {

   private static final String NAME = " Functor ";

   /**
    * constructor
    *
    * @param  function name
    * @param  unifiable parameters
    */
   public Functor(Unifiable... parameters) {
      super(NAME, parameters);
   }


   /**
    * constructor
    *
    * Parse a string to create the parameter for this function.
    *
    * @param  argument string
    */
   public Functor(String str) {
      super(NAME);
      Unifiable term = Make.term(str);
      Unifiable[] terms = new Unifiable[1];
      terms[0] = term;
      parameters = terms;
   }  // constructor


   /**
    * evaluate the parameter
    *
    * @param   substitution set
    * @param   array of unifiable
    * @return  functor of term (a Constant)
    */
   public Unifiable evaluate(SubstitutionSet ss, Unifiable... param) {
      Unifiable first = (Unifiable)param[0];
      if (first instanceof Complex) {
         return ((Complex)first).getTerm(0);
      }
      return null;
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
      return new Functor(newParameters);
   }
}  // Functor
