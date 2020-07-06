/**
 * PFunction
 *
 * This class is a base class for logic Functions. Subclasses should
 * override the evaluate method.
 *
 * A function returns a Constant. It should be used with a unification
 * operator, for example:
 *
 *   $X = add(7, 3)   // $X is bound to 10.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.Hashtable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class PFunction implements Unifiable {

   // public for convenience of subclasses
   public String functionName = null;
   public Unifiable[] parameters;  // arguments?

   /**
    * constructor
    *
    * @param  function name
    * @param  unifiable parameter
    */
   public PFunction(String functionName, Unifiable... parameters) {
      this.functionName = functionName;
      this.parameters = parameters;
   }

   public String toString() { return functionName; }


   /**
    * evaluate - abstract
    *
    * Each subclass must do its own evaluation.
    *
    * @param  substitution set
    * @param  array of unifiable parameters
    * @return Unifiable (Constant)
    */
   public abstract Unifiable evaluate(SubstitutionSet ss, Unifiable[] parameters);


   /**
    * bindAllParameters
    *
    * In order to evaluate a function, all of its parameters must
    * be bound. This method returns the bound parameters or null.
    *
    * @param  substitution set
    * @return array of bound parameters
    */
   public Unifiable[] bindAllParameters(SubstitutionSet ss) {
      Unifiable[] newParameters = new Unifiable[parameters.length];
      for (int i = 0; i < parameters.length; i++) {
         if (parameters[i] instanceof Variable) {
            Variable varParam = (Variable)parameters[i];
            if (ss.isBound(varParam)) {
               newParameters[i] = ss.getBinding(varParam);
            }
            else return null;  // Variables must be bound. Fail.
         }
         else {
            newParameters[i] = parameters[i];
         }
      }
      return newParameters;
   } // bindAllParameters


   /**
    * unify
    *
    * Unify result of function with a unifiable expression,
    * which must be a constant or variable.
    *
    * @param  expression
    * @param  substitution set
    * @return new substitution set
    */
   public SubstitutionSet unify(Unifiable exp, SubstitutionSet ss) {
      Unifiable[] boundParameters = bindAllParameters(ss);
      if (boundParameters == null) return null;
      Unifiable result = evaluate(ss, boundParameters);
      if (result == null) return null;
      return result.unify(exp, ss);
   } // unify


   /**
    * replaceVariables
    *
    * Replaces bound Variables with their Constants in order to
    * display results.
    *
    * @param   substitution set
    * @return  new expression, without variables
    */
   public Expression replaceVariables(SubstitutionSet ss) {

      for (Unifiable param : parameters) {
         if (param instanceof Constant || param instanceof Complex) {
            return param;
         }
         else if (param instanceof Variable) {
            Variable vParam = (Variable)param;
            if (ss.isBound(vParam)) {
               Expression exp = vParam.replaceVariables(ss);
               return exp;
            }
            else {
               return new Constant("Function: This is very bad.");
            } 
         }
         else {
            return new Constant("Function: Got a problem.");
         }
      }
      return null;

   }  // replaceVariables


   /*
    * standardizeParameter
    *
    * This method assists standardizeVariablesApart() in the subclass.
    * If the parameter is a Variable, this method will standardize it.
    * If not, it returns null.
    *
    * @param   unifiable parameter
    * @param   already standardized variables (hash)
    * @return  new unifiable parameter or null
    */
   protected Unifiable standardizeParameter(Unifiable parameter,
                                            Hashtable<Variable, Variable> newVars) {
      if (parameter instanceof Variable) {
         Variable par = (Variable)parameter;
         Unifiable newParameter = (Unifiable)par.standardizeVariablesApart(newVars);
         return newParameter;
      }
      return parameter;
   } // standardizeParameter


   /**
    * castComplex
    *
    * If the given unifiable is an instance of Complex, cast it
    * as complex. Otherwise return null. This function is useful
    * for subclasses of Function.
    *
    * @param  Unifiable term
    * @param  Substitution Set
    * @return Complex term or null
    */
   public Complex castComplex(Unifiable term, SubstitutionSet ss) {
      if (term instanceof Complex) return (Complex)term;
      Unifiable outTerm = null;
      if (term instanceof Variable) {
         if (ss.isGround((Variable)term)) {
            outTerm = ss.getGroundTerm((Variable)term);
         }
         else return null;
      }
      if (outTerm instanceof Complex) return (Complex)outTerm;
      return null;
   } // castComplex


   /**
    * castConstant
    *
    * If the given unifiable is an instance of Constant, cast it
    * as Constant. Otherwise return null. This function is useful
    * for subclasses of Function.
    *
    * @param  Unifiable term
    * @param  Substitution Set
    * @return Constant term or null
    */
   public Constant castConstant(Unifiable term, SubstitutionSet ss) {
      if (term instanceof Constant) return (Constant)term;
      Unifiable outTerm = null;
      if (term instanceof Variable) {
         if (ss.isGround((Variable)term)) {
            outTerm = ss.getGroundTerm((Variable)term);
         }
         else return null;
      }
      if (outTerm instanceof Constant) return (Constant)outTerm;
      return null;
   } // castConstant

   /**
    * castPList
    *
    * If the given unifiable is an instance of PList, cast it
    * as PList and return it. If it is a Variable, get the term
    * which it is bound to. If that term is a PList, cast it as
    * a PList and return it. Otherwise return null. This function
    * is useful for subclasses.
    *
    * @param  Unifiable term
    * @param  Substitution Set
    * @param  plist or null
    */
   public PList castPList(Unifiable term, SubstitutionSet ss) {
      if (term instanceof PList) return (PList)term;
      Unifiable outTerm = null;
      if (term instanceof Variable) {
         if (ss.isGround((Variable)term)) {
            outTerm = ss.getGroundTerm((Variable)term);
         }
         else return null;
      }
      if (outTerm instanceof PList) return (PList)outTerm;
      return null;
   } // castPList


   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    *
    * This method uses reflection to instantiate the subclass,
    * so that the method does not need to be repeated in each
    * subclass.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newParameters = new Unifiable[parameters.length];
      for (int i = 0; i < parameters.length; i++) {
         newParameters[i] = standardizeParameter(parameters[i], newVars);
      }
      // Now instantiate the class with with new standardized parameters.
      try {
         String className = this.getClass().getName();
         Constructor<?> c = Class.forName(className)
                                 .getDeclaredConstructor(Unifiable[].class);
         c.setAccessible(true);  // Necessary?
         return (Expression)c.newInstance(new Object[] {newParameters});
      }
      catch (ClassNotFoundException cnfx) {}
      catch (NoSuchMethodException nsmx) {}
      catch (InstantiationException ix) {}
      catch (IllegalAccessException iax) {}
      catch (InvocationTargetException itx) {}
      System.err.println("Function::standardizeVariablesApart - Major failure.");
      System.exit(0);
      return null;
   } // standardizeVariablesApart

} // PFunction

