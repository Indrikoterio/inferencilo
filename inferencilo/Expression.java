/**
 * Expression
 *
 * This interface defines a predicate calculus expression.
 * It is the basis of unifiable objects.
 *
 * The method standardizeVariablesApart() creates unique
 * variables every time the inference engine tries to solve
 * a goal.
 *
 * The method replaceVariables() is called when a solution
 * is found. It replaces bound Variables with there Constants
 * in order to display results.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public interface Expression {

   /**
    * replaceVariables - explanation here
    *
    * Note: In this inference engine, variables begin with a a dollar sign.
    * Eg. $X. Constants can start with an upper case or a lower case letter.
    *
    * When the inference engine has found a solution to a query, in order
    * to display the solution in a useable manner, it is necessary to
    * replace all variables with the constants to which they have been
    * bound. The bindings are recorded in the substitution set.
    *
    * For example, suppose that our query was 'grandfather(Jack, $X)',
    * and in our resulting substitution set, $X was bound to $Y was bound
    * to Cindy. $X would be replaced by Cindy to give:
    *     grandfather(Jack, Cindy).
    *
    * @param   substitution set
    * @return  new expression, without variables
    */
   public Expression replaceVariables(SubstitutionSet ss);

   /**
    * standardizeVariablesApart  - explanation here
    *
    * In Prolog, the scope of a variable is the rule in which it is defined.
    * For example, in the knowledge base we have:
    *    grandparent($X, $Y) = parent($X, $Z), parent($Z, $Y).
    *    parent($X, $Y) :- father($X, $Y).
    *    parent($X, $Y) :- mother($X, $Y).
    * When searching for a solution, the first parent/2 rule will be called twice.
    * When called a second time, the $X must be unique, different from the
    * variable which was bound in the first call.
    * Every time a rule is fetched from the knowledge base, the variables must
    * be recreated (standardized).
    * A variable is uniquely identified by its print name and id number.
    * For example, the $X for the first call might become '$X_22', and the $X
    * in the second call might become '$X_23', etc.
    *
    * @param   hash table previously standardized variables
    * @return  expression with standardized variables
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars);

}

