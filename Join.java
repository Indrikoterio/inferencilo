/**
 * Join
 *
 * This test function joins two complex terms to produce another.
 * For example:
 *
 * article(the), noun(city) --> noun phrase(the city)
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

import java.util.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Join extends Function {

   Constant np = new Constant("noun phrase");

   /**
    * constructor
    *
    * @param  function name
    * @param  unifiable parameters
    */
   public Join(Unifiable... parameters) {
      super("Join", parameters);
   }

   /**
    * evaluate the parameters
    *
    * @param   substitution set
    * @param   unifiable expressions
    * @return  new unifiable
    */
   public Unifiable evaluate(SubstitutionSet ss, Unifiable... param) {
      if (param.length < 2) return null;
      Complex c1 = castComplex(param[0], ss);
      Complex c2 = castComplex(param[1], ss);
      if (c1 == null || c2 == null) return null;
      Constant first = (Constant)c1.getTerm(0);   // get functor
      Constant second = (Constant)c2.getTerm(0);
      String s1 = "" + first;
      String s2 = "" + second;
      if (!s1.equals("article") || !s2.equals("noun")) return null;
      Constant word1 = (Constant)c1.getTerm(1);
      Constant word2 = (Constant)c2.getTerm(1);
      Constant newWord = new Constant("" + word1 + " " + word2);
      return new Complex(np, newWord);
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
//      return new Join(newParameters);

try {
String className = this.getClass().getName();
System.out.println(">>>>>> " + className);
System.out.println("22>>>>>> " + newParameters.getClass().getName());

Constructor<?> c = Class.forName(className).getDeclaredConstructor(Unifiable[].class);
c.setAccessible(true);
return (Expression)c.newInstance(new Object[] {newParameters});
}
catch (ClassNotFoundException cnfx) {}
catch (NoSuchMethodException nsmx) {}
catch (InstantiationException ix) {}
catch (IllegalAccessException iax) {}
catch (InvocationTargetException itx) {}
//catch (Exception iax) {}
return null;
   }

} // Join
