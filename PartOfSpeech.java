/**
 * PartOfSpeech
 *
 * This class reads in a list of words with part-of-speech data from
 * the file part_of_speech.txt, and creates a hashmap keyed by word.
 *
 * In addition, there are methods to create Fact objects which can be
 * analyzed by the inference engine Inferencilo.
 *
 * The part-of-speech tags inpart_of_speech.txt are from Penn State's
 * Treebank tagset. There is a reference here:
 * https://sites.google.com/site/partofspeechhelp/home
 *
 * ABN pre-quantifier (half, all)
 * AP post-determiner (many, several, next)
 * AT article (a, the, no)
 * CC coordinating conjunction
 * CD cardinal digit
 * DT determiner
 * EX existential there (like: “there is” … think of it like “there exists”)
 * FW foreign word
 * IN preposition/subordinating conjunction
 * JJ adjective ‘big’
 * JJR adjective, comparative ‘bigger’
 * JJS adjective, superlative ‘biggest’
 * LS list marker 1)
 * MD modal could, will
 * NN noun, singular ‘desk’
 * NNS noun plural ‘desks’
 * NNP proper noun, singular ‘Harrison’
 * NNPS proper noun, plural ‘Americans’
 * OD ordinal numeral (first, 2nd)
 * PDT predeterminer ‘all the kids’
 * POS possessive ending parent‘s
 * PRP personal pronoun I, he, she
 * PRP$ possessive pronoun my, his, hers
 * QL qualifier (very, fairly)
 * QLP post-qualifier (enough, indeed)
 * RB adverb very, silently,
 * RBR adverb, comparative better
 * RBS adverb, superlative best
 * RP particle give up
 * SYM symbol
 * TO to go ‘to‘ the store.
 * UH interjection errrrrrrrm
 * VB verb, base form take
 * VBD verb, past tense took
 * VBG verb, gerund/present participle taking
 * VBN verb, past participle taken
 * VBP verb, sing. present, non-3d take
 * VBZ verb, 3rd person sing. present takes
 * WDT wh-determiner which
 * WP wh-pronoun who, what
 * WP$ possessive wh-pronoun whose
 * WRB wh-abverb where, when
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

class PartOfSpeech {

   private final static String FILENAME = "part_of_speech.txt";
   private static PartOfSpeech partOfSpeech;

   private static Constant noun = new Constant("noun");
   private static Constant verb = new Constant("verb");
   private static Constant participle = new Constant("participle");
   private static Constant adjective  = new Constant("adjective");

   // tenses
   private static Constant past  = new Constant("past");
   private static Constant present  = new Constant("present");

   // voice
   private static Constant active  = new Constant("active");
   private static Constant passive  = new Constant("passive");

   // Person, for verbs.
   private static Constant first_sing  = new Constant("first_sing");   // I am
   private static Constant second_sing  = new Constant("second_sing"); // Thou art
   private static Constant third_sing  = new Constant("third_sing");   // it is
   private static Constant base  = new Constant("base");   // you see

   // Plurality for nouns.
   private static Constant singular = new Constant("singular"); // table, mouse
   private static Constant plural   = new Constant("plural"); // tables, mice

   // For adjectives
   private static Constant positive  = new Constant("positive");        // good
   private static Constant comparative  = new Constant("comparative");  // better
   private static Constant superlative  = new Constant("superlative");  // best

   // For adjectives
   private static Constant adverb = new Constant("adverb");  // happily

   // For articles
   private static Constant article = new Constant("article");       // the, a, an
   private static Constant definite = new Constant("definite");     // the
   private static Constant indefinite = new Constant("indefinite"); // a, an

   // HashMap: word / Part of Speech.
   private static Map<String, String[]> wordPoS;

   /*
    * constructor
    *
    * Read in a file of Part of Speech data. Create a hashmap.
    */
   private PartOfSpeech() {

      BufferedReader reader = openFile(FILENAME);
      if (reader == null) return;
      wordPoS = new HashMap<String, String[]>();
      try {
         String line = reader.readLine();
         while (line != null) {
            line = line.trim();
            int index = line.indexOf(" ");
            if (index < 0) {
               wordPoS.put(line, null);
            }
            else {
               String word = line.substring(0, index);
               String[] pos = line.substring(index + 1).split(" ");
               wordPoS.put(word, pos);
            }
            line = reader.readLine();
         }
      } catch (IOException iox) {}
   }  // constructor


   /**
    * getPartOfSpeech
    *
    * PartOfSpeech is a singleton. Instantiate only once.
    *
    * @return PartOfSpeech object
    */
   public static PartOfSpeech getPartOfSpeech() {
      if (partOfSpeech == null) partOfSpeech = new PartOfSpeech();
      return partOfSpeech;
   }



   /*
    * openFile
    *
    * Opens a file for reading, UTF-8 format.
    *
    * @param  name of file
    * @return BufferedReader
    */
   private static BufferedReader openFile(String name) {
      try {
         return new BufferedReader(new InputStreamReader(
                                   new FileInputStream(name),"UTF-8"));
      }
      catch (Exception e) {
         System.out.println("PartOfSpeech: Cannot open file " + name + " . ");
         return null;
      }
   } // openFile


   /**
    * display
    *
    * Displays the entire contents of the wordPoS hashmap.
    */
   public static void display() {
      for (Map.Entry<String, String[]> entry : wordPoS.entrySet()) {
         System.out.println(entry.getKey());
         String[] values = entry.getValue();
         for (String v : values) {
            System.out.println("   " + v);
         }
      }
   } // display


   /**
    * get
    *
    * Gets part of speech data for a specific word.
    *
    * @param  word (string)
    * @return Part of Speech (array of strings)
    */
   public static String[] get(String word) {
      return wordPoS.get(word);
   } // get


   /*
    * makeVerbFact
    *
    * This method creates a verb fact, eg. verb(listens,...).
    *
    * @param  word
    * @param  pos code
    * @return fact
    */
   private static Rule makeVerbFact(String word, String code) {
      Complex term = null;
      if (code.equals("VB")) {
         term = new Complex(verb, new Constant(word), present, base);
      }
      else if (code.equals("VBZ")) {
         term = new Complex(verb, new Constant(word), present, third_sing);
      }
      else if (code.equals("VBD")) {
         term = new Complex(verb, new Constant(word), past, base);
      }
      else if (code.equals("VBG")) {
         term = new Complex(participle, new Constant(word), active);
      }
      else if (code.equals("VBN")) {
         term = new Complex(participle, new Constant(word), passive);
      }
      if (term != null) return new Rule(term);
      return null;
   } // makeVerbFact


   /*
    * makeNounFact
    *
    * This method creates a verb fact, eg. verb(listens).
    *
    * @param  word
    * @param  pos code
    * @return fact
    */
   private static Rule makeNounFact(String word, String code) {
      Complex term = null;
      if (code.equals("NN")) {
         term = new Complex(noun, new Constant(word), singular);
      }
      else if (code.equals("NNS")) {
         term = new Complex(noun, new Constant(word), plural);
      }
      if (term != null) return new Rule(term);
      return null;
   } // makeNounFact


   /*
    * makeAdjectiveFact
    *
    * This method creates an adjective fact, eg. adjective(happy).
    *
    * @param  word
    * @param  pos code
    * @return fact
    */
   private static Rule makeAdjectiveFact(String word, String code) {
      Complex term = null;
      if (code.equals("JJ")) {
         term = new Complex(adjective, new Constant(word), positive);
      }
      else if (code.equals("JJR")) {
         term = new Complex(adjective, new Constant(word), comparative);
      }
      else if (code.equals("JJS")) {
         term = new Complex(adjective, new Constant(word), superlative);
      }
      if (term != null) return new Rule(term);
      return null;
   } // makeAdjectiveFact


   /*
    * makeArticleFact
    *
    * This method creates facts for articles, eg. article(the, definite).
    *
    * @param  word
    * @return fact
    */
   private static Rule makeArticleFact(String word) {
      Complex term;
      String wordLower = word.toLowerCase();
      if (wordLower.equals("the")) {
         term = new Complex(article, new Constant(word), definite);
      }
      else {
         term = new Complex(article, new Constant(word), indefinite);
      }
      if (term != null) return new Rule(term);
      return null;
   } // makeArticleFact

   /*
    * makeAdverbFact
    *
    * This method creates facts for adverbs, eg. adverb(happily).
    *
    * @param  word
    * @return fact
    */
   private static Rule makeAdverbFact(String word) {
      Complex term = new Complex(adverb, new Constant(word));
      if (term != null) return new Rule(term);
      return null;
   } // makeAdverbFact


   /*
    * makeFact
    *
    * This method takes an English word and produces a Fact
    * object, which can be analyzed by the inference engine.
    *
    * @param  word
    * @return fact
    */
   private static List<Rule> makeFact(String word) {
      String[] posData = get(word.toLowerCase());
      if (posData.length < 1) return null;
      List<Rule> rules = new ArrayList<Rule>();
      for (String pos : posData) {
         Rule newRule = null;
         if (pos.startsWith("VB")) newRule = makeVerbFact(word, pos);
         else
         if (pos.startsWith("NN")) newRule = makeNounFact(word, pos);
         else
         if (pos.startsWith("JJ")) newRule = makeAdjectiveFact(word, pos);
         else
         if (pos.equals("AT")) newRule = makeArticleFact(word);
         if (pos.equals("RB")) newRule = makeAdverbFact(word);
         if (newRule != null) rules.add(newRule);
      }
      return rules;
   } // makeFact


   /**
    * makeFacts
    *
    * This method takes an array of words, and creates a list
    * of facts which can be analyzed by the inference engine.
    *
    * For example the array ["We", "will", "visit"]
    * should produce a list of the following facts:
    *
    *     pronoun(We).
    *     auxiliary(will).
    *     verb(visit, present, not_3rd_sing).
    *
    * Note: A Fact is just a Rule without a body.
    *
    * @param  array of words
    * @return list of facts
    */
   public static List<Rule> makeFacts(String[] words) {
      List<Rule> facts = new ArrayList<Rule>();
      for (String word : words) { facts.addAll(makeFact(word)); }
      return facts;
   } // makeFacts


   public static void main(String[] args) {
      String[] words = {"suspect", "saw", "dance", "dancing",
                        "The", "A", "an", "happily"};
      PartOfSpeech pos = PartOfSpeech.getPartOfSpeech();
      List<Rule> rules = makeFacts(words);
      ListIterator<Rule> ruleIterator = rules.listIterator();
      while (ruleIterator.hasNext()) { System.out.println(ruleIterator.next()); }
   }

}  // PartOfSpeech

