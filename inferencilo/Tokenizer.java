/**
 * Tokenizer
 *
 * For parsing the goal of a Prolog rule.
 * This class is a singleton. Use getTokenizer() to instantiate.
 *   
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class Tokenizer {

   private ArrayList<Token> tokens;
   private static Tokenizer tokenizer;

   /*
    * constructor
    */
   private Tokenizer() {
      //System.out.println("Instantiating Tokenizer");
      tokens = new ArrayList<Token>();
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

   /**
    * tokenize
    *
    * Divide the given string into a series of tokens.
    *
    * @param  str
    */
   private static String invalid = "\"<>#@"; // Invalid between terms.

   private void tokenize(String str) {

      //System.out.println("tokenize");
      tokens.clear();

      String s = str.trim();
      if (s.length() < 1) throw new InvalidOperatorException(s);

      int startIndex = 0;
      int roundDepth = 0;   // depth of round parenthesis (())
      int squareDepth = 0;   // depth of square brackets [[]]

      // Find a separator (comma, semicolon), if there is one.
      for (int i = startIndex; i < s.length(); i++) {
         char ch = s.charAt(i);
         if (ch == '[') squareDepth++;
         else if (ch == ']') squareDepth--;
         else if (ch == '(') roundDepth++;
         else if (ch == ')') roundDepth--;
         else if (ch == '\\') i++;   // For comma escapes, eg. \,
         else if (roundDepth == 0 && squareDepth == 0) {
            if (invalid.indexOf(ch) > -1) throw new InvalidOperatorException(s);
            if (ch == ',') {
               String subgoal = s.substring(startIndex, i).trim();
               tokens.add(new Token(subgoal));
               tokens.add(new Token(","));
               startIndex = i + 1;
            }
            if (ch == ';') {
               String subgoal = s.substring(startIndex, i).trim();
               tokens.add(new Token(subgoal));
               tokens.add(new Token(";"));
               startIndex = i + 1;
            }
         }
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

}  // Tokenizer


