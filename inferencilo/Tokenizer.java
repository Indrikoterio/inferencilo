/**
 * Tokenizer
 *
 * For parsing Prolog-ish rules.
 *
 * This class is a singleton. Use getTokenizer() to instantiate:
 *
 *   Tokenizer tok = Tokenizer.getTokenizer();
 *   Goal goal = tok.generateGoal(body);
 *   
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.ArrayList;
import java.util.Stack;

public class Tokenizer {

   private ArrayList<Token> tokens;
   private Stack<Integer> stkParenth;  // Parentheses and brackets.
   private static Tokenizer tokenizer;

   // Define parentheses and braces.
   // (prof(Thompson, 5849238); prof(Hamilton, 5849238)), $X = [$H, $T].
   private final static int NONE = 0;
   private final static int GROUP = 1;   // (...)
   private final static int SLIST = 2;   // [...]
   private final static int COMPLEX = 3; // prof(...)

   /*
    * constructor
    */
   private Tokenizer() {
      //System.out.println("Instantiating Tokenizer");
      tokens = new ArrayList<Token>();
      stkParenth = new Stack<Integer>();
   }

   /**
    * getTokenizer
    *
    * Instantiate only once.
    *
    * @return tokenizer
    */
   public static Tokenizer getTokenizer() {
      if (tokenizer == null) tokenizer = new Tokenizer();
      return tokenizer;
   }

   /*
    * letterNumberHyphen
    *
    * Determines whether the given character is valid in a functor.
    * Eg.: functor_22($X, $Y)
    *
    * @param character
    * @return t/f
    */
   private boolean letterNumberHyphen(char ch) {
      if (ch >= 'a' && ch <= 'z') return true;
      if (ch >= 'A' && ch <= 'Z') return true;
      if (ch >= '0' && ch <= '9') return true;
      if (ch == '_' || ch == '-' || ch == 0xAD) return true;
      if (ch >= 0xC0 && ch < 0x2C0) return true;
      if (ch >= 0x380 && ch < 0x510) return true;
      return false;
   }


   /**
    * tokenize
    *
    * Divide the given string into a series of tokens.
    * Note: Parentheses can be part of a complex term: likes(Charles, Gina)
    * or used to group terms: (father($_, $X); mother($_, $X))
    *
    * @param  str
    */
   private static String invalid = "\"#@"; // Invalid between terms.
   private void tokenize(String str) {

      Integer top;  // top of parenthesis stack

      //System.out.println("tokenize");
      tokens.clear();
      stkParenth.clear();

      String s = str.trim();
      if (s.length() < 1) throw new InvalidExpressionException(s);

      int startIndex = 0;

      // Find a separator (comma, semicolon), if there is one.
      char previous = '#'; // random
      for (int i = startIndex; i < s.length(); i++) {
         // Get top of stack.
         top = NONE;
         if (stkParenth.size() > 0) top = (Integer)stkParenth.peek();
         char ch = s.charAt(i);
         if (noEsc(ch, '(', previous)) {
            if (letterNumberHyphen(previous)) {
               stkParenth.push(COMPLEX);
            }
            else {
               stkParenth.push(GROUP);
               tokens.add(new Token("("));
               startIndex = i + 1;
            }
         }
         else if (noEsc(ch, ')', previous)) {
            if (top == NONE) throw new UnmatchedParenthesesException("Tokenizer");
            top = (Integer)stkParenth.pop();
            if (top == GROUP) {
               String subgoal = s.substring(startIndex, i);
               tokens.add(new Token(subgoal));
               tokens.add(new Token(")"));
               startIndex = i + 1;
            }
            else if (top != COMPLEX) {
               throw new UnmatchedParenthesesException("Tokenizer");
            }
         }
         else if (noEsc(ch, '[', previous)) {
            stkParenth.push(SLIST);
         }
         else if (noEsc(ch, ']', previous)) {
            if (top == NONE) throw new UnmatchedBracketsException("Tokenizer");
            top = (Integer)stkParenth.pop();
            if (top != SLIST) {
               throw new UnmatchedBracketsException("Tokenizer");
            }
         }
         else {
            // If not inside complex term or Prolog list...
            if (top != COMPLEX && top != SLIST) {
               if (invalid.indexOf(ch) > -1)
                  throw new InvalidExpressionException("--> " + ch);
               if (noEsc(ch, '`', previous)) {  // Find the next backtick.
                  int j = i + 1;
                  char prev = '#';
                  while (j < s.length()) {
                     char ch2 = s.charAt(j);
                     if (noEsc(ch2, '`', prev)) {
                        i = j;
                        break;
                     }
                     j++;
                     prev = ch2;
                  }
               }
               else
               if (noEsc(ch, ',', previous)) {
                  String subgoal = s.substring(startIndex, i);
                  tokens.add(new Token(subgoal));
                  tokens.add(new Token(","));
                  startIndex = i + 1;
               }
               else
               if (noEsc(ch, ';', previous)) {
                  String subgoal = s.substring(startIndex, i);
                  tokens.add(new Token(subgoal));
                  tokens.add(new Token(";"));
                  startIndex = i + 1;
               }
            }
         }
         previous = ch;
      }
      if (stkParenth.size() > 0) {
         throw new UnmatchedParenthesesException("Tokenizer");
      }

      if (s.length() - startIndex > 0) {
         String subgoal = s.substring(startIndex, s.length());
         tokens.add(new Token(subgoal));
      }

   } // tokenize


   /**
    * generateGoal
    *
    * Generates a goal object (And, Or, etc.) from a Prolog-like
    * string representation. For example, a String such as 'can_swim($X),
    * can_fly($X)' will become an And goal, with two complex statements
    * (can_swim, can_fly) as subgoals.
    *
    * @param  string of tokens
    * @return goal
    */
   public Goal generateGoal(String str) {
      tokenize(str);
      //showTokens();
      Token baseToken = groupTokens(tokens, 0);
      groupAndTokens(baseToken);
      groupOrTokens(baseToken);
      return generateGoal(baseToken);
   }

   /*
    * noEsc
    *
    * Ensures that the character being checked matches the match
    * character, and is not escaped by a backslash. (Eg. \, \[ )
    *
    * @param character to check
    * @param match character
    * @param previous character
    * @return boolean - true if not escaped by backslash
    */
    boolean noEsc(char check, char match_char, char previous) {
       if (previous == '\\') { return false; }
       if (check != match_char) { return false; }
       return true;
    }

   /*
    * groupTokens
    *
    * This method collects tokens within parentheses into groups.
    * It converts a flat array of tokens into a tree of tokens.
    *
    * For example, this:   SUBGOAL SUBGOAL ( SUBGOAL  SUBGOAL )
    * becomes:
    *          GROUP
    *            |
    * SUBGOAL SUBGOAL GROUP
    *                   |
    *            SUBGOAL SUBGOAL
    *
    * There is a precedence order in Prolog subgoals.
    * From highest to lowest.
    *
    *    groups (...)  -> GROUP
    *    conjunction , -> AND
    *    disjunction ; -> OR
    *
    * @param  flat array of tokens
    * @param  starting index
    * @return tree of tokens
    */
   private Token groupTokens(final ArrayList<Token> tokens, int index) {

      ArrayList<Token> newTokens = new ArrayList<Token>();

      for (; index < tokens.size(); index++) {

         Token token = tokens.get(index);
         TokenType type = token.type();

         if (type == TokenType.LPAREN) {
            index++;
            // Make a GROUP token.
            Token t = groupTokens(tokens, index);
            newTokens.add(t);
            // Skip past tokens already processed.
            index += t.numberOfChildren() + 1;  // +1 for right parenthesis
         }
         else if (type == TokenType.RPAREN) {
            // Add all remaining tokens to the list.
            return new Token(newTokens, TokenType.GROUP);
         }
         else {
            newTokens.add(token);
         }
      }

      return new Token(newTokens, TokenType.GROUP);

   } // groupTokens


   /*
    * groupAndTokens
    *
    * Groups tokens which are separated by commas. (Prolog And)
    *
    * @param  token tree
    */
   private void groupAndTokens(Token token) {

      ArrayList<Token> children = token.getChildren();
      ArrayList<Token> newChildren = new ArrayList<Token>();
      ArrayList<Token> andList = new ArrayList<Token>();

      for (Token t : children) {

         TokenType type = t.type();

         if (type == TokenType.SUBGOAL) {
            andList.add(t);
         }
         else if (type == TokenType.COMMA) {
            // Nothing to do.
         }
         else if (type == TokenType.SEMICOLON) {
            // Must be end of comma separated list.
            int size = andList.size();
            if (size == 1) newChildren.add(andList.get(0));
            else newChildren.add(new Token(andList, TokenType.AND));
            newChildren.add(t);
            andList.clear();
         }
         else if (type == TokenType.GROUP) {
            groupAndTokens(t);
            groupOrTokens(t);
            andList.add(t);
         }
      } // for

      int size = andList.size();
      if (size == 1) newChildren.add(andList.get(0));
      else if (size > 1) newChildren.add(new Token(andList, TokenType.AND));

      token.setChildren(newChildren);

   } // groupAndTokens

   /*
    * groupOrTokens
    *
    * Groups tokens which are separated by semicolons. (Prolog Or)
    *
    * @param  token tree
    */
   private void groupOrTokens(Token token) {

      ArrayList<Token> children = token.getChildren();
      ArrayList<Token> newChildren = new ArrayList<Token>();
      ArrayList<Token> orList = new ArrayList<Token>();

      for (Token t : children) {

         TokenType type = t.type();
         if (type == TokenType.SUBGOAL)  { orList.add(t); }
         else if (type == TokenType.AND) { orList.add(t); }
         else if (type == TokenType.GROUP) { orList.add(t); }
         else if (type == TokenType.SEMICOLON) { } // Nothing to do.

      } // for

      int size = orList.size();
      if (size == 1) newChildren.add(orList.get(0));
      else if (size > 1) newChildren.add(new Token(orList, TokenType.OR));

      token.setChildren(newChildren);

   } // groupOrTokens


   /*
    * generateGoal
    *
    * Generates a goal from the token tree.
    *
    * @param  base of token tree
    * @return goal
    */
   private Goal generateGoal(Token token) {

      ArrayList<Token> children;
      ArrayList<Goal> operands;

      TokenType type = token.type();

      if (type == TokenType.SUBGOAL) {
         return Make.subgoal(token.token());
      }

      if (type == TokenType.AND) {
         operands = new ArrayList<Goal>();
         children = token.getChildren();
         for (Token t : children) {
            TokenType type2 = t.type();
            if (type2 == TokenType.SUBGOAL) {
               operands.add(Make.subgoal(t.token()));
            }
            else if (type2 == TokenType.GROUP) {
               operands.add(generateGoal(t));
            }
         }
         return new And(operands);
      }

      if (type == TokenType.OR) {
         // Get operands and create an OR goal.
         operands = new ArrayList<Goal>();
         children = token.getChildren();
         for (Token t : children) {
            TokenType type2 = t.type();
            if (type2 == TokenType.SUBGOAL) {
               operands.add(Make.subgoal(t.token()));
            }
            else if (type2 == TokenType.GROUP) {
               operands.add(generateGoal(t));
            }
         }
         return new Or(operands);
      }

      if (type == TokenType.GROUP) {
         if (token.numberOfChildren() != 1) {
            throw new FatalParsingException("Each GROUP should have 1 child.");
         }
         Token childToken = (Token)token.getChildren().get(0);
         return generateGoal(childToken);
      }

      return null;

   } // generateGoal()

   /**
    * showTokens
    *
    * For debugging purposes. Outputs a flat list of tokens
    * in a readable format.
    */
   public void showTokens() {
      boolean first = true;
      for (Token token : tokens) {
         if (!first) System.out.print(" ");
         first = false;
         TokenType type = token.type();
         if (type == TokenType.SUBGOAL) {
            System.out.print(token.token());
         }
         else {
            System.out.print(token.type().name());
         }
      }
      System.out.print("\n");
   } // showTokens

}  // Tokenizer
