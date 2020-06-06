/**
 * Word
 *
 * This class converts a few words ('We', 'will', 'go'...) into
 * Prolog-ish terms: pronoun(We), auxiliary(will), verb(go), etc.
 * for testing purposes.
 *
 * Note: Unlike Prolog, constants can start with an upper or lower
 * case letter. Variables start with $, eg. $X.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class Word {

   public static Constant verb = new Constant("verb");
   public static Constant adverb = new Constant("adverb");
   public static Constant pronoun = new Constant("pronoun");
   public static Constant auxiliary = new Constant("auxiliary");
   public static Constant word = new Constant("word");

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
         System.out.println("Word: bad string.");
         return null;
      }

      if (str.equals("We")) {
         c = new Complex(pronoun, new Constant(str));
      }

      else
      if (str.equals("will")) {
         c = new Complex(auxiliary, new Constant(str));
      }

      else
      if (str.equals("are")) {
         c = new Complex(verb, new Constant(str));
      }

      else
      if (str.equals("go")) {
         c = new Complex(verb, new Constant(str));
      }

      else
      if (str.equals("here")) {
         c = new Complex(adverb, new Constant(str));
      }

      else
      if (str.equals("after")) {
         c = new Complex(word, new Constant(str));
      }

      return c;

   } // makeTerm

}  // Word
