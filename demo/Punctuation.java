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


   public static Constant bracket = new Constant("bracket");
   public static Constant quote_mark = new Constant("quote_mark");

   /**
    * makeTerm
    *
    * Creates a term for the given symbol,.
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
         c = new Complex(new Constant("period"), new Constant("."));
      }
      else
      if (symbol == ',') {
         c = new Complex(new Constant("comma"), new Constant(","));
      }
      else
      if (symbol == '?') {
         c = new Complex(new Constant("question_mark"), new Constant("?"));
      }
      else
      if (symbol == '!') {
         c = new Complex(new Constant("exclamation_mark"), new Constant("!"));
      }
      else
      if (symbol == ':') {
         c = new Complex(new Constant("colon"), new Constant(":"));
      }
      else
      if (symbol == ';') {
         c = new Complex(new Constant("semicolon"), new Constant(";"));
      }
      else
      if (symbol == '-') {
         c = new Complex(new Constant("dash"), new Constant("-"));
      }
      else
      if (symbol == '"') {    // The second argument is for comparisons.
         c = new Complex(quote_mark, new Constant("\""), new Constant("\""));
      }
      else
      if (symbol == '\'') {
         c = new Complex(quote_mark, new Constant("'"), new Constant("'"));
      }
      else
      if (symbol == '«') {
         c = new Complex(quote_mark, new Constant("«"), new Constant("«"));
      }
      else
      if (symbol == '»') {
         c = new Complex(quote_mark, new Constant("»"), new Constant("«"));
      }
      else
      if (symbol == '‘') {
         c = new Complex(quote_mark, new Constant("‘"), new Constant("‘"));
      }
      else
      if (symbol == '’') {
         c = new Complex(quote_mark, new Constant("’"), new Constant("‘"));
      }
      else
      if (symbol == '“') {
         c = new Complex(quote_mark, new Constant("“"), new Constant("“"));
      }
      else
      if (symbol == '”') {
         c = new Complex(quote_mark, new Constant("”"), new Constant("“"));
      }
      else
      if (symbol == '(') {
         c = new Complex(bracket, new Constant("("), new Constant("("));
      }
      else
      if (symbol == ')') {
         c = new Complex(bracket, new Constant(")"), new Constant("("));
      }
      else
      if (symbol == '[') {
         c = new Complex(bracket, new Constant("["), new Constant("["));
      }
      else
      if (symbol == ']') {
         c = new Complex(bracket, new Constant("]"), new Constant("["));
      }
      else
      if (symbol == '<') {
         c = new Complex(bracket, new Constant("<"), new Constant("<"));
      }
      else
      if (symbol == '>') {
         c = new Complex(bracket, new Constant(">"), new Constant("<"));
      }
      return c;
   }
}
