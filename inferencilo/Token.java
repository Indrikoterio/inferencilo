/**
 * Token
 *
 * Token is used for parsing Prolog goals.
 *
 * Each token represents a node in a tree of tokens.
 *
 * A token 'leaf' can be: SUBGOAL, COMMA, SEMICOLON, LPAREN, RPAREN.
 * If the token is a parent node, its type will be: GROUP, AND, OR.
 *   
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.ArrayList;

public class Token {

   private TokenType type;
   private String token;
   private ArrayList<Token> children;

   /**
    * constructor
    *
    * Constructs a child node.
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
      else type = TokenType.SUBGOAL;
   }

   /**
    * constructor
    *
    * Constructs a parent node.
    *
    * @param  token as string
    */
   public Token(ArrayList<Token> tokens, TokenType type) {
      children = tokens;
      this.type = type;
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

   /**
    * getChildren
    *
    * @return  array of tokens
    */
   public ArrayList<Token> getChildren() { return children; }

   /**
    * setChildren
    *
    * @param  array of tokens
    */
   public void setChildren(ArrayList<Token> children) {
      this.children = children;
   }

   /**
    * size
    *
    * @return number of children
    */
   public int size() {
      if (children == null) return 1;
      return children.size();
   }

   /**
    * toString
    *
    * For debugging purposes. Eg.
    *
    *    SUBGOAL > sister(Janelle, Amanda)
    *
    * return printable string
    */
   public String toString() {

      String s = "" + type.name();
      if (type == TokenType.AND ||
          type == TokenType.OR) {
         s += " > ";
         for (Token child : children) {
            s += child + " ";
         }
      }
      else if (type == TokenType.SUBGOAL) {
         s += " > " + token;
      }
      else { }
      
      return s;
   }

}  // Token


