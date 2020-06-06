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

   private final static String PRON = "pronoun";
   private final static String AUX = "auxiliary";
   private final static String COP = "copula";
   private final static String VERB = "verb";
   private final static String ADV = "adverb";
   private final static String WORD = "word";

   // HashMap: word / Part of Speech.
   private static Map<String, String> wordPoS;
   static {
      wordPoS = new HashMap<>();
      wordPoS.put("i", PRON);
      wordPoS.put("you", PRON);
      wordPoS.put("he", PRON);
      wordPoS.put("she", PRON);
      wordPoS.put("it", PRON);
      wordPoS.put("we", PRON);
      wordPoS.put("they", PRON);
      wordPoS.put("will", AUX);
      wordPoS.put("am", COP);
      wordPoS.put("is", COP);
      wordPoS.put("are", COP);
      wordPoS.put("go", VERB);
      wordPoS.put("went", VERB);
      wordPoS.put("think", VERB);
      wordPoS.put("here", ADV);
      wordPoS.put("after", WORD);
   }

   /**
    * makeTerm
    *
    * Creates a term for the given symbol.
    *
    * @param  punctuation symbol (str)
    * @return punctuation term, eg. semicolon(;)
    */
   public static Complex makeTerm(String str) {

      if (str.length() < 1) {
         System.out.println("Word: bad string.");
         return null;
      }

      String s = str.toLowerCase();
      String partOfSpeech = wordPoS.get(s);

      if (partOfSpeech == null) {
         System.out.println("Word: Unknown word.");
         return null;
      }

      Constant word = new Constant(str);
      Constant pos  = new Constant(partOfSpeech);
      return new Complex(pos, word);

   } // makeTerm

}  // Word
