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
   private final static int PLIST = 2;   // [...]
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
    * functorChar
    *
    * Determines whether the given character is valid in a functor.
    * Eg.: functor_22($X, $Y)
    *
    * @param character
    * @return t/f
    */
   private boolean functorChar(char ch) {
      if (ch >= '0' && ch <= '9') return true;
      if (ch >= 'a' && ch <= 'z') return true;
      if (ch >= 'A' && ch <= 'Z') return true;
      if (ch == '_') return true;
      return false;
   }


   /**
    * tokenize
    *
    * Divide the given string into a series of tokens.
    *
    * @param  str
    */
   private static String invalid = "\"<>#@"; // Invalid between terms.

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
         char ch = s.charAt(i);
         if (ch == '\\') i++;   // For comma escapes, eg. \,
         else if (ch == '(') {
            if (functorChar(previous)) {
               stkParenth.push(COMPLEX);
            }
            else {
               stkParenth.push(GROUP);
               tokens.add(new Token("("));
            }
         }
         else if (ch == ')') {
            if (stkParenth.size() < 1)
               throw new UnmatchedParenthesesException();
            top = (Integer)stkParenth.pop();
            if (top == GROUP) {
               tokens.add(new Token(")"));
            }
            else if (top != COMPLEX) {
               throw new UnmatchedParenthesesException();
            }
         }
         else if (ch == '[') {
            stkParenth.push(PLIST);
         }
         else if (ch == ']') {
            if (stkParenth.size() < 1)
               throw new UnmatchedBracketsException();
            top = (Integer)stkParenth.pop();
            if (top != PLIST) {
               throw new UnmatchedBracketsException();
            }
         }
         else {
            top = NONE;
            if (stkParenth.size() > 0) top = (Integer)stkParenth.peek();
            // If not inside complex term or Prolog list...
            if (top != COMPLEX && top != PLIST) {
               if (invalid.indexOf(ch) > -1)
                  throw new InvalidExpressionException("Invalid character");
               if (ch == ',') {
                  String subgoal = s.substring(startIndex, i);
                  tokens.add(new Token(subgoal));
                  tokens.add(new Token(","));
                  startIndex = i + 1;
               }
               if (ch == ';') {
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
         top = (Integer)stkParenth.peek();
         throw new UnmatchedParenthesesException();
      }

      if (s.length() - startIndex > 0) {
         String subgoal = s.substring(startIndex, s.length());
         tokens.add(new Token(subgoal));
      }

   } // tokenize


   /*
    * noSemicolons
    *
    * Returns true if there are no semicolon tokens in the list of tokens.
    * Semicolon represents logical Or.
    *
    * @param  tokens
    * @return  t/f
    */
   private boolean noSemicolons(ArrayList<Token> tokens) {
      for (Token token : tokens) {
         if (token.type() == TokenType.SEMICOLON) return false;
      }
      return true;
   }

   /*
    * countTerms
    *
    * Returns the number of TERMs in the token list.
    * COMMAs and SEMICOLONs are not counted.
    *
    * @param  token list
    * @return count
    */
   private int countTerms(ArrayList<Token> tokens) {
      int count = 0;
      for (Token token : tokens) {
         if (token.type() == TokenType.TERM) count++;
      }
      return count;
   }


   /**
    * generateGoal
    *
    * Generates a goal object (And, Or, Complex, etc.) from a
    * Prolog-like string representation. For example, a String
    * such as 'can_swim($X), can_fly($X)' will become an And
    * goal, with two complex statements (can_swim, can_fly) as
    * subgoals.
    *
    * @param  list of tokens
    * @return operator
    */
   public Goal generateGoal(String str) {
      tokenize(str);
      return generateGoal(tokens);
   }

   /*
    * generateGoal
    *
    * Generates a goal from the tokens in the given token list.
    *
    * @param  list of tokens
    * @return operator
    */
   private Goal generateGoal(ArrayList<Token> tokens) {
      if (countTerms(tokens) == 1) {
         Token token = tokens.get(0);
         if (token.type() == TokenType.TERM) {
            return Make.subgoal(token.token());
         }
      }
      // If there are no semicolons in the list, generate an And operator.
      else if (noSemicolons(tokens)) {
         ArrayList<Goal> operands = new ArrayList<Goal>();
         for (Token token : tokens) {
            if (token.type() == TokenType.TERM) {
               operands.add(Make.subgoal(token.token()));
            }
         }
         return new And(operands);
      }
      // If there are semicolons in the list, generate an Or operator.
      else {
         // Make an array of array lists.
         ArrayList<ArrayList<Token>> lists = new ArrayList<ArrayList<Token>>();
         ArrayList<Token> tokenList = new ArrayList<Token>();
         for (Token token : tokens) {
            if (token.type() != TokenType.SEMICOLON) {
               tokenList.add(token);
            }
            else {
               lists.add(tokenList);
               tokenList = new ArrayList<Token>();
            }
         }
         lists.add(tokenList);
         ArrayList<Goal> operands = new ArrayList<Goal>();
         for (ArrayList<Token> list : lists) {
            operands.add(generateGoal(list));
         }
         return new Or(operands);
      }
      return null;

   } // generateGoal()

   /**
    * showTokens
    *
    * For debugging purposes.
    */
   public void showTokens() {
      boolean first = true;
      for (Token token : tokens) {
         if (!first) System.out.print(" ");
         first = false;
         System.out.print(token.type().name());
      }
   } //showTokens

}  // Tokenizer


