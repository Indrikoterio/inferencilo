/**
 * BuiltInPredicate
 *
 * This is a base class for built-in predicates. In this project,
 * a built-in predicate is a Java class which is treated by the
 * solution searching algorithm as a Prolog predicate.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public abstract class BuiltInPredicate implements Unifiable, Goal {

   // These are public for the convenience of subclasses.
   // It's a sin, but I don't care.
   public String predicateName = null;
   public Unifiable[] arguments;

   /**
    * constructor
    *
    * @param  predicate name
    * @param  unifiable arguments
    */
   public BuiltInPredicate(String predicateName, Unifiable... arguments) {
      this.predicateName = predicateName;
      this.arguments = arguments;
   }

   /**
    * getSolver
    *
    * Returns a solution node for this predicate.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public abstract SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode);

   /*
    * standardizeOne
    *
    * Standardize one argument.
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
   protected Unifiable standardizeOne(Unifiable argument,
                                      Hashtable<Variable, Variable> newVars) {
      if (argument instanceof Variable) {
         Variable arg = (Variable)argument;
         Unifiable newArgument = (Unifiable)arg.standardizeVariablesApart(newVars);
         return newArgument;
      }
      return argument;
   }

   /**
    * replaceVariables
    *
    * Refer to Expression for full comments.
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
            Variable var = (Variable)arg;
            if (ss.isBound(var)) {
               Expression exp = var.replaceVariables(ss);
               return exp;
            }
            else {
               return new Constant("BIP, replaceVariables(): This is very bad.");
            } 
         }
         else {
            return new Constant("BIP, replaceVariables(): Got a problem.");
         }
      }
      return null;

   }  // replaceVariables


   /**
    * standardizeVariablesApart()
    *
    * Refer to class Expression for full comments.
    *
    * Each subclass must return its own class. This is a base class,
    * so this method is abstract.
    */
   public abstract Expression
          standardizeVariablesApart(Hashtable<Variable, Variable> newVars);

   /**
    * evaluate
    *
    * The unique work of the built-in predicate is done by this method.
    *
    * @param   substitution set of parent
    * @return  unifiable term or null
    */
   public abstract Unifiable evaluate(SubstitutionSet ss);


   /**
    * getTerm - according to index
    *
    * @param   index of term/argument
    * @return  unifiable term
    */
   public Unifiable getTerm(int index) { return arguments[index]; }

   public String toString() { return predicateName + "(...)"; }


   /**
    * unify
    *
    * Performs unification with another term.
    * For built-in predicates, this method is not used.
    *
    * @param  expression
    * @param  substitution set
    * @return new substitution set
    */
   public SubstitutionSet unify(Unifiable uni, SubstitutionSet ss) {
      return ss;  // Don't know what else to do. :-)
   }


   /**
    * castConstant
    *
    * If the given unifiable is an instance of Constant, cast it
    * as Constant and return it. Otherwise return null. This
    * function is useful for subclasses.
    *
    * @param  Unifiable term
    * @return Constant term or null
    */
   public Constant castConstant(Unifiable term) {
      if (!(term instanceof Constant)) return null;
      return (Constant)term;
   }

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
   }


   /**
    * castPList
    *
    * If the given unifiable is an instance of PList, cast it
    * as PList and return it. If it is a Variable, get the term
    * which it is bound to. If that term is a PList, cast it as
    * a PList and return it. Otherwise return null. This function
    * is useful for subclasses.
    *
    * @param  unifiable term
    * @param  substitution set
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
   }

}
