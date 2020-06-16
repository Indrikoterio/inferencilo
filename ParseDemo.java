/**
 * ParseDemo
 *
 * This class parses English sentences, identifying the part
 * of speech for each word. It was written as a demo for the
 * inference engine Inferencilo; it is not intended to be
 * complete or practical.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

import java.util.*;

class ParseDemo {

   private static String testString =
      "The ideal characteristic of artificial intelligence is its ability to rationalize.";

   /*
    * constructor
    *
    * Read in a file of Part of Speech data. Create a hashmap.
    */
   private ParseDemo() {
   }  // constructor


   public static void main(String[] args) {

      // Divide sentence into words and punctuation.
      List<String> words = Sentence.getWords(testString);
      for (String word : words) { System.out.println(word); }

      // Convert words to facts.
      // Note: A Fact is a Rule without a body. Eg.: preposition(to).
      PartOfSpeech pos = PartOfSpeech.getPartOfSpeech();
      List<Rule> facts = pos.wordsToFacts(words);

      // Set up the knowledge base.
      KnowledgeBase kb = new KnowledgeBase();
      ListIterator<Rule> factIterator = facts.listIterator();
      while (factIterator.hasNext()) {
         Rule fact = factIterator.next();
         System.out.println(fact);   // Display facts.
         kb.addRule(fact);
      }
      

   } // main

}  // ParseDemo

