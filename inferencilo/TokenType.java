/**
 * TokenType
 *
 * For parsing the goals of a Prolog rule.
 *
 * For this goal: (mother($X, $Y); father($X, $Y))
 * the token types would be: LPAREN SUBGOAL SEMICOLON SUBGOAL RPAREN.
 *
 * There is a precedence to Prolog subgoals. From highest to lowest.
 *
 *    groups (...)  -> GROUP
 *    conjunction , -> AND
 *    disjunction ; -> OR
 *
 * (I think.) Because of this, it is necessary to build a tree
 * of tokens. Some tokens will function as a parent node.
 *   
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public enum TokenType {
   SUBGOAL, COMMA, SEMICOLON, LPAREN, RPAREN,
   GROUP, AND, OR;
}  // TokenType
