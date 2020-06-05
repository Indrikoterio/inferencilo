/**
 * TokenType
 *
 * For parsing the goal of a Prolog rule.
 * For the goal: father($X, $Z), parent($Z, $Y)
 * the token types will be: TERM, COMMA, TERM, LPAREN, RPAREN.
 *   
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public enum TokenType {
   TERM, COMMA, SEMICOLON, LPAREN, RPAREN;
}  // TokenType


