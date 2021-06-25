/**
 * Expression
 *
 * This interface defines a predicate calculus expression.
 * It is the base of unifiable objects (Constants, Variables,
 * Complex terms) and goal objects (Complex terms and operators).
 *
 * The method standardizeVariablesApart() creates unique
 * variables every time the inference engine tries to solve
 * a goal.
 *
 * The method replaceVariables() is called after a solution
 * is found. It replaces Variables with the Constants which
 * they are bound to, in order to display results.
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
    * replace all variables with the constants to which they have been bound.
    * The bindings are recorded in the 'substitution set'.
    *
    * For example, suppose that our query was 'grandfather(Frank, $X)',
    * and in our resulting substitution set, $X was bound to $Y was bound
    * to Cindy. $X would be replaced by Cindy to give:
    *
    *     grandfather(Frank, Cindy).
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
    *
    *    grandparent($X, $Y) = parent($X, $Z), parent($Z, $Y).
    *    parent($X, $Y) :- father($X, $Y).
    *    parent($X, $Y) :- mother($X, $Y).
    *
    * To find a solution for the goal 'grandparent(Frank, $X)', the parent/2
    * rule will be fetched from the knowledge base. When parent/2 is fetched
    * a second time, the variables ($X, $Y) must be unique, different from
    * the variables which were previously bound.
    * Every time a rule is fetched from the knowledge base, the variables
    * must be recreated (standardized).
    * A variable is uniquely identified by its print name and id number.
    * For example, the variable $X for the first call might become '$X_22',
    * and $X in the second call might become '$X_23'.
    *
    * @param   hash table of standardized variables
    * @return  expression with standardized variables
    */
   public Expression standardizeVariablesApart(Hashtable<Variable, Variable> newVars);

}

