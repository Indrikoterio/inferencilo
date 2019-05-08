/**
 * ChaChaCha
 *
 * A silly function to test Function class inheritance.
 *
 * @author   Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class ChaChaCha extends Function {

   /**
    * constructor
    *
    * @param  unifiable parameters
    */
   public ChaChaCha(Unifiable... parameters) {
      super("ChaChaCha", parameters);
   }

   /**
    * evaluate the parameters
    *
    * @param   Substitution Set
    * @param   Unifiable parameters
    * @return  Prolog constant
    */
   public Unifiable evaluate(SubstitutionSet ss, Unifiable... param) {
      return new Constant("" + param[0] + " + " + param[1] + " + Cha Cha Cha!");
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
         //if (newParameter == null) return this;
      }
      return new ChaChaCha(newParameters);
   }


}
