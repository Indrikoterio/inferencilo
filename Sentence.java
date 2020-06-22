/**
 * Sentence
 *
 * This class has static methods which divide an English sentence
 * into a list of words and punctuation.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;

class Sentence {

   private static final int MAX_WORD_LENGTH = 30;
   private static final int MAX_WORDS_IN_SENTENCE = 120;

   /**
    * constructor
    */
   public Sentence() { }


   /*
    * isAWordCharacter
    *
    * Determines whether the character is part of a word.
    *
    * Generally, words consist of Latin letters, but sometimes
    * other characters are parts of words, eg.: 1st, 2nd, 3rd
    *
    * @param   character to test
    * @return  t/f
    */
   private static boolean isAWordCharacter(char ch) {
      if (ch >= 'a' && ch <= 'z') return true;
      if (ch >= 'A' && ch <= 'Z') return true;
      if (ch >= '0' && ch <= '9') return true;
      if (ch >= 0xC0 && ch < 0x2C0) return true;
      if (ch == '-' || ch == 0xAD) return true;  // hyphen or soft hyphen
      if (ch >= 0x380 && ch < 0x510) return true;
      return false;
   }

  /*
   * isAnApostrophe
   *
   * Tests if a character is an apostrophe.
   *
   * @param  character
   * @return true/false
   */
   private static boolean isAnApostrophe(char ch) {
      if (ch == '\'' || ch == '\u2019' || ch == '\u02bc') return true;
      return false;
   }


   /*
    * isPunctuation
    *
    * Determines whether the character is punctuation.
    * EXCEPT if the character is a period (.).
    * A period could be part of an abbreviation or number (eg. 37.49).
    *
    * @param  character to test
    * @return t/f
    */
   private static boolean isPunctuation(char c) {
      if (c == '.') return false;
      if (c >= '!' && c <= '/') return true;
      if (c >= ':' && c <= '@') return true;
      if (c == '\u2013') return true;  // en-dash
      if (isQuoteMark(c) >= 0) return true;
      return false;
   }  // isPunctuation


   /*
    * isQuoteMark
    *
    * Checks whether the character is a quote mark ("'«).
    *
    * If yes, return the index of the quote mark.
    * If no, return -1.
    *
    * @param   character to test
    * @return  index of quote (or -1)
    */
   private static int isQuoteMark(char c) {
      for (int i = 0; i < leftQuotes.length; i++) {
         if (c == leftQuotes[i]) return i;
         if (c == rightQuotes[i]) return i;
      }
      return -1;
   }
   private static char[] leftQuotes = {'\'', '"', '\u00ab', '\u2018', '\u201c' };
   private static char[] rightQuotes = {'\'', '"', '\u00bb', '\u2019', '\u201d' };


   /*
    * endOfSentence
    *
    * Determines whether a period is at the end of a sentence.
    * (If it is at the end, it must be punctuation.)
    *
    * @param  sentence
    * @param  index
    * @return t/f
    */
   private static boolean endOfSentence(String sentence, int index) {
      int length = sentence.length();
      if (index >= length - 1) return true;
      while (index < length) {
         char c = sentence.charAt(index++);
         if (isAWordCharacter(c)) return false;
      }
      return true;
   }


   /**
    * getWords
    *
    * This method divides a sentence into a list of words
    * and punctuation.
    *
    * @param   sentence string
    * @return  list of words and punctuation
    */
   public static List<String> getWords(String sentence) {

      List<String> words  = new ArrayList<String>();
      int numberOfWords = 0;

      int length = sentence.length();

      String  word;
      char    character;

      int  startIndex = 0;
      int  lastIndex;

      while (startIndex < length && numberOfWords < MAX_WORDS_IN_SENTENCE) {

         character = ' ';

         // Skip spaces, etc.
         while (startIndex < length) {
            character = sentence.charAt(startIndex);
            if (character > ' ') break;
            startIndex++;
         }
         if (startIndex >= length) break;

         // A period at the end of a sentence is punctuation.
         // A period in the middle is probably part of an abbreviation
         // or number, eg.: 7.3
         if (character == '.' && endOfSentence(sentence, startIndex)) {
            words.add(".");
            startIndex++;
         }
         else
         if (isPunctuation(character)) {
            words.add("" + character);
            startIndex++;
         }
         else
         if (isAWordCharacter(character)) {

            for (lastIndex = startIndex + 1; lastIndex < length; lastIndex++) {
               character = sentence.charAt(lastIndex);
               if (character == '.') {
                  if (endOfSentence(sentence, lastIndex)) break;
               }
               // There might be an apostrophe within the word: don't, we've
               else
               if (isAnApostrophe(character)) {
                   if (lastIndex < length - 1) {
                      char ch2 = sentence.charAt(lastIndex + 1);
                      if (!isAWordCharacter(ch2)) break;
                   }
               }
               else {
                  if (!isAWordCharacter(character)) break;
               }
            } // for

            word = sentence.substring(startIndex, lastIndex);
            words.add(word);

            numberOfWords++;

            startIndex = lastIndex;

         }
         else {  // unknown character.
            startIndex++;
         }

      }  // end of while

      return words;

   }  // getWords


}  // Sentence

