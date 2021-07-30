/**
 * Punctuation
 *
 * Makes Prolog terms for punctuation symbols: ()?![] etc.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class Punctuation {


   /**
    * makeTerm
    *
    * Creates a term for the given symbol.
    *
    * @param  punctuation symbol (str)
    * @return punctuation term, eg. semicolon(;)
    */
   public static Complex makeTerm(String str) {

      Complex c = null;

      if (str.length() < 1) {
         System.out.println("Punctuation: bad string.");
         return null;
      }

      char symbol = str.charAt(0);

      if (symbol == '.') {
         c = new Complex("period(.)");
      }
      else
      if (symbol == ',') {
         c = new Complex("comma(\\,)");  // Must escape the comma with backslash.
      }
      else
      if (symbol == '?') {
         c = new Complex("question_mark(?)");
      }
      else
      if (symbol == '!') {
         c = new Complex("exclamation_mark(!)");
      }
      else
      if (symbol == ':') {
         c = new Complex("colon(:)");
      }
      else
      if (symbol == ';') {
         c = new Complex("semicolon(;)");
      }
      else
      if (symbol == '-') {
         c = new Complex("dash(-)");
      }
      else
      if (symbol == '"') {    // The second argument is for comparisons.
         // 
         c = new Complex("quote_mark(\", \")");
      }
      else
      if (symbol == '\'') {
         c = new Complex("quote_mark(', ')");
      }
      else
      if (symbol == '«') {
         c = new Complex("quote_mark(«, «)");
      }
      else
      if (symbol == '»') {
         c = new Complex("quote_mark(», «)");
      }
      else
      if (symbol == '‘') {
         c = new Complex("quote_mark(‘, ‘)");
      }
      else
      if (symbol == '’') {
         c = new Complex("quote_mark(’, ‘)");
      }
      else
      if (symbol == '“') {
         c = new Complex("quote_mark(“, “)");
      }
      else
      if (symbol == '”') {
         c = new Complex("quote_mark(”, “)");
      }
      else
      if (symbol == '(') {
         c = new Complex("bracket((, ()");
      }
      else
      if (symbol == ')') {
         c = new Complex("bracket(), ()");
      }
      else
      if (symbol == '[') {
         c = new Complex("bracket([, [)");
      }
      else
      if (symbol == ']') {
         c = new Complex("bracket(], [)");
      }
      else
      if (symbol == '<') {
         c = new Complex("bracket(<, <)");
      }
      else
      if (symbol == '>') {
         c = new Complex("bracket(>, <)");
      }
      return c;
   }
}  // makeTerm
