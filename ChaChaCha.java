/**
 * ChaChaCha
 *
 * A silly function to test Function class inheritance.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class ChaChaCha extends PFunction {

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

}  // ChaChaCha
