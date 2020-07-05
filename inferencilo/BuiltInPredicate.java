/**
 * BuiltInPredicate
 *
 * This is a base class for built-in predicates. Subclasses should
 * override the abstract evaluate() method.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.Hashtable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
    * Returns a SolutionNode (object) for built-in predicates.
    *
    * The main method in a SolutionNode is nextSolution().
    * nextSolution() calls evaluate(), which is defined by the
    * subclass. evaluate() takes the parentSolution (a substitution
    * set), performs some logic on the predicate's arguments, and
    * returns a new substitution set.
    *
    * @param  knowledge base
    * @param  parent solution set
    * @param  parent solution node
    * @return solution node
    */
   public SolutionNode getSolver(KnowledgeBase knowledge,
                                 SubstitutionSet parentSolution,
                                 SolutionNode parentNode) {
      return

         new SolutionNode(this, knowledge, parentSolution, parentNode) {

            boolean moreSolutions = true;

            /**
             * nextSolution
             *
             * Call evaluate() to do some work on the input argument(s),
             * then return the new solution set.
             *
             * @return  new substitution set
             */
            public SubstitutionSet nextSolution() {
               if (noBackChaining()) return null;
               if (!moreSolutions) return null;
               moreSolutions = false;
               return evaluate(parentSolution);
            }
         };
   } // getSolver


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
    * standardizeAll
    *
    * Standardize (remake) all given arguments.
    *
    * @param  array of arguments
    * @param  already standardized variables (hash)
    * @return new standardized arguments
    */
   private Unifiable[] standardizeAll(Unifiable[] arguments,
                                        Hashtable<Variable, Variable> newVars) {
      Unifiable[] novajArgumentoj = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         novajArgumentoj[i] = standardizeOne(arguments[i], newVars);
      }
      return novajArgumentoj;
   }


   /**
    * replaceVariables
    *
    * Refer to Expression.java for full comments.
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
    * Refer to Expression.java for full comments.
    *
    * This method uses reflection to instantiate the subclass, so
    * that this method does not need to be repeated in each subclass.
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars) {
      Unifiable[] newArguments = new Unifiable[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
         newArguments[i] = standardizeOne(arguments[i], newVars);
      }
      // Now instantiate the class with new standardized arguments.
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
      System.err.println("BuiltInPredicate::standardizeVariablesApart - Major failure.");
      System.exit(0);
      return null;
   } // standardizeVariablesApart


   /**
    * evaluate
    *
    * The unique work of the built-in predicate is done by this method.
    *
    * @param   substitution set of parent
    * @return  new substitution set
    */
   public abstract SubstitutionSet evaluate(SubstitutionSet ss);


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
    * as Constant and return it.
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


   /*
    * displayString
    *
    * If the given term is a Constant, return its string value,
    * If the given term is a Complex term, return the string value of the first term.
    * If it is a PList, iterate through the list to produce a readable string.
    * (Commas should have no space in front.)
    *
    * @param   term
    * @param   substitution set
    * @return  string representation
    */
   public String displayString(Unifiable term, SubstitutionSet ss) {

      if (term instanceof Variable) {
         if (ss.isGround((Variable)term)) {
            Unifiable t = ss.getGroundTerm((Variable)term);
            return displayString(t, ss);
         }
         else return "";
      }

      if (term instanceof Constant) return "" + term;
      if (term instanceof Complex) {
         Complex t = (Complex)term;
         if (t.length() < 2) return "";
         return displayString(t.getTerm(1), ss);
      }
      if (term instanceof PList) {

         PList plist = (PList)term;

         StringBuilder sb = new StringBuilder("");
         Unifiable head = plist.getHead();
         if (head == null) return "";
         sb.append(displayString(head, ss));  // recursion

         PList theTail = plist.getTail();
         while (theTail != null) {
            head = theTail.getHead();
            if (head == null) break;
            String str = displayString(head, ss);
            if (str.equals(",")) sb.append(str);
            else sb.append(" " + str);
            theTail = theTail.getTail();
         }
         return sb.toString();
      }
      return "";
   }

}  // BuiltInPredicate
