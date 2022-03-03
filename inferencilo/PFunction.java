/**
 * PFunction
 *
 * This class is a base class for Prolog-like functions. Subclasses
 * should override the 'evaluate' method. The class is called 'PFunction'
 * to distinguish it from Java Functions.
 *
 * A function returns a Constant. It should be used with a unification
 * operator, for example:
 *
 *   $X = add(7, 3)   // $X is bound to 10.
 *
 * Note: Currently, this inference engine does not have an is-operator.
 * Unification will cause a function to be evaluated. In other words,
 * there is no: $X is add(1, 2).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.List;
import java.util.HashMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class PFunction implements Unifiable {

   // public for convenience of subclasses
   String functionName = null;
   public Unifiable[] arguments;

   /**
    * constructor
    *
    * @param  function name
    * @param  array of unifiable arguments
    */
   public PFunction(String functionName, Unifiable... arguments) {
      this.functionName = functionName;
      this.arguments = arguments;
   }

   /**
    * constructor
    *
    * @param  function name
    * @param  arguments as string
    */
   public PFunction(String functionName, String strArgs) {

      this.functionName = functionName;

      if (strArgs == null || strArgs.length() == 0)
          throw new TooFewArgumentsException("in " + functionName + ".");

      arguments = Make.splitTerms(strArgs, ',')
                 .stream()
                 .map(Make::term)
                 .toArray(Unifiable[]::new);

   } // constructor


   public String toString() { return functionName; }


   /**
    * evaluate - abstract
    *
    * Each subclass must do its own evaluation.
    *
    * @param  substitution set
    * @param  array of unifiable arguments
    * @return Unifiable (Constant)
    */
   public abstract Unifiable evaluate(SubstitutionSet ss, Unifiable[] arguments);


   /**
    * bindAllArguments
    *
    * In order to evaluate a function, all of its arguments must
    * be bound. This method returns the bound arguments or null.
    *
    * @param  substitution set
    * @return array of bound arguments
    */
   public Unifiable[] bindAllArguments(SubstitutionSet ss) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         if (arguments[i] instanceof Variable) {
            Variable varArg = (Variable)arguments[i];
            if (ss.isBound(varArg)) {
               newArguments[i] = ss.getBinding(varArg);
            }
            else return null;  // Variables must be bound. Fail.
         }
         else {
            newArguments[i] = arguments[i];
         }
      }
      return newArguments;
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
      Unifiable[] boundArguments = bindAllArguments(ss);
      if (boundArguments == null) return null;
      Unifiable result = evaluate(ss, boundArguments);
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

      for (Unifiable arg : arguments) {
         if (arg instanceof Constant || arg instanceof Complex) {
            return arg;
         }
         else if (arg instanceof Variable) {
            Variable vArg = (Variable)arg;
            if (ss.isBound(vArg)) {
               Expression exp = vArg.replaceVariables(ss);
               return exp;
            }
            else {
               return new Constant("PFunction: This is very bad.");
            } 
         }
         else {
            return new Constant("PFunction: Got a problem.");
         }
      }
      return null;

   }  // replaceVariables



   /**
    * standardizeOne
    *
    * Standardize one argument. This method is copied from BuiltInPredicate.
    *
    * This method assists standardizeVariablesApart() in the subclass.
    * If the argument is a Variable, this method will 'standardize' it.
    * (That is, substitute a new unique variable.)
    * If not, it just returns the argument as is.
    *
    * @param   unifiable argument
    * @param   already standardized variables (hash)
    * @return  out argument
    */
   public Unifiable standardizeOne(Unifiable argument,
                                      HashMap<Variable, Variable> newVars) {
      if (argument instanceof Variable) {
         Variable arg = (Variable)argument;
         Unifiable newArgument = (Unifiable)arg.standardizeVariablesApart(newVars);
         return newArgument;
      }
      return argument;
   }

   /**
    * standardizeVariablesApart
    *
    * Refer to class Expression for full comments.
    *
    * This method uses reflection to instantiate the subclass,
    * so that the method does not need to be repeated in each
    * subclass.
    */
   public Expression standardizeVariablesApart(HashMap<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      // Now instantiate the class with with new standardized arguments.
      try {
         String className = this.getClass().getName();
         Constructor<?> c = Class.forName(className)
                                 .getDeclaredConstructor(Unifiable[].class);
         c.setAccessible(true);  // Necessary?
         return (Expression)c.newInstance(new Object[] {newArguments});
      }
      catch (ClassNotFoundException cnfx) {}
      catch (NoSuchMethodException nsmx) {}
      catch (InstantiationException ix) {}
      catch (IllegalAccessException iax) {}
      catch (InvocationTargetException itx) {}
      System.err.println("PFunction::standardizeVariablesApart - Major failure.");
      System.exit(0);
      return null;
   } // standardizeVariablesApart

} // PFunction

