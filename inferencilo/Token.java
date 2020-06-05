/**
 * Token
 *
 * Represents a token used for parsing the goal of a Prolog rule.
 *   
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

public class Token {

   private TokenType type;
   private String token;

   /**
    * constructor
    *
    * @param  token as string
    */
   public Token(String token) {
      String t = token.trim();
      this.token = t;
      if (t.equals(",")) type = TokenType.COMMA;
      else if (t.equals(";")) type = TokenType.SEMICOLON;
      else if (t.equals("(")) type = TokenType.LPAREN;
      else if (t.equals(")")) type = TokenType.RPAREN;
      else type = TokenType.TERM;
   }

   /**
    * token - a getter
    *
    * @return  token
    */
   public String token() { return token; }

   /**
    * type - a getter
    *
    * @return  token type
    */
   public TokenType type() { return type; }

}  // Token


