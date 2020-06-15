/**
 * PartOfSpeech
 *
 * This class reads in a list of words with part-of-speech data from
 * the file part_of_speech.txt, and creates a hashmap keyed by word.
 *
 * In addition, there are methods to create Fact objects which can be
 * analyzed by the inference engine Inferencilo.
 *
 * The part-of-speech tags in part_of_speech.txt are from Penn State's
 * Treebank tagset. There is a reference here:
 * https://sites.google.com/site/partofspeechhelp/home
 *
 * ABN pre-quantifier (half, all)
 * AP post-determiner (many, several, next)
 * AT article (a, the, no)
 * BE be
 * BED were
 * BEDZ was
 * BEG being
 * BEM am
 * BEN been
 * BER are, art
 * BBB is
 * CC coordinating conjunction
 * CD cardinal digit
 * DT determiner
 * EX existential there (like: “there is” … think of it like “there exists”)
 * FW foreign word
 * IN preposition/subordinating conjunction
 * JJ adjective 'big'
 * JJR adjective, comparative 'bigger'
 * JJS adjective, superlative 'biggest'
 * LS list marker 1)
 * MD modal could, will
 * NN noun, singular 'desk'
 * NNS noun plural 'desks'
 * NNP proper noun, singular 'Harrison'
 * NNPS proper noun, plural 'Americans'
 * OD ordinal numeral (first, 2nd)
 * PDT predeterminer 'all the kids'
 * PPO objective personal pronoun (me, him, it, them)
 * PPS 3rd. singular nominative pronoun (he, she, it, one)
 * PPSS other nominative personal pronoun (I, we, they, you)
 * POS possessive ending parent's
 * PRP personal pronoun I, he, she
 * PRP$ possessive pronoun my, his, hers
 * QL qualifier (very, fairly)
 * QLP post-qualifier (enough, indeed)
 * RB adverb very, silently,
 * RBR adverb, comparative better
 * RBS adverb, superlative best
 * RP particle give up
 * SYM symbol
 * TO to go 'to' the store.
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
   private static Constant pronoun = new Constant("pronoun");
   private static Constant adjective = new Constant("adjective");
   private static Constant participle = new Constant("participle");

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

   // For adjectives.
   private static Constant positive  = new Constant("positive");        // good
   private static Constant comparative  = new Constant("comparative");  // better
   private static Constant superlative  = new Constant("superlative");  // best

   // For adverbs.
   private static Constant adverb = new Constant("adverb");  // happily

   // For articles.
   private static Constant article = new Constant("article");       // the, a, an
   private static Constant definite = new Constant("definite");     // the
   private static Constant indefinite = new Constant("indefinite"); // a, an

   // For pronouns.
   private static Constant subject = new Constant("subject");  // subject
   private static Constant object = new Constant("object");    // object

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


   /*
    * lowerCaseExceptI
    *
    * Make word lower case, except if it's the pronoun I.
    *
    * @param  word
    * @return lower case word
    */
   private static String lowerCaseExceptI(String word) {
      int length = word.length();
      if (length > 0) {
         if (word.equals("I")) return word;
         if (word.startsWith("I'")) return word;
      }
      return word.toLowerCase();
   } // lowerCaseExceptI


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
    * makePronounFact
    *
    * This method creates a pronoun fact, eg. pronoun(they,...).
    *
    * @param  word
    * @param  pos code
    * @return fact
    */
   private static Rule makePronounFact(String word, String code) {
      Complex term = null;
      if (code.equals("PPSS")) {
         term = new Complex(pronoun, new Constant(word), subject);
      }
      else if (code.equals("PPO")) {
         term = new Complex(pronoun, new Constant(word), object);
      }
      if (term != null) return new Rule(term);
      return null;
   } // makePronounFact


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
      else if (code.equals("NNP")) {
         term = new Complex(noun, new Constant(word), singular);
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
    * makeFacts
    *
    * This method takes an English word and produces a Fact
    * object, which can be analyzed by the inference engine.
    *
    * @param  word
    * @return fact
    */
   private static List<Rule> makeFacts(String word) {
      String[] posData;
      posData = get(word);
      if (posData == null) {
         String low = lowerCaseExceptI(word);
         posData = get(low);
         if (posData == null) return null; 
      }
      if (posData.length < 1) return null;
      List<Rule> facts = new ArrayList<Rule>();
      for (String pos : posData) {
         Rule newFact = null;
         if (pos.startsWith("VB")) newFact = makeVerbFact(word, pos);
         else
         if (pos.startsWith("NN")) newFact = makeNounFact(word, pos);
         else
         if (pos.startsWith("PP")) newFact = makePronounFact(word, pos);
         else
         if (pos.startsWith("JJ")) newFact = makeAdjectiveFact(word, pos);
         else
         if (pos.equals("AT")) newFact = makeArticleFact(word);
         if (pos.equals("RB")) newFact = makeAdverbFact(word);
         if (newFact != null) facts.add(newFact);
      }
      return facts;
   } // makeFacts


   /**
    * wordsToFacts
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
    * @param  list of words
    * @return list of facts
    */
   public static List<Rule> wordsToFacts(List<String> words) {
      List<Rule> facts = new ArrayList<Rule>();
      List<Rule> wordFacts;
      for (String word : words) {
         wordFacts = makeFacts(word);
         if (wordFacts != null) {
            facts.addAll(wordFacts);
         }
      }
      return facts;
   } // wordsToFacts



   public static void main(String[] args) {
      String testString = "The ideal characteristic of artificial intelligence is its ability to rationalize.";
      List<String> words = Sentence.getWords(testString);
      for (String word : words) { System.out.println(word); }
      PartOfSpeech pos = PartOfSpeech.getPartOfSpeech();
      List<Rule> facts = wordsToFacts(words);
      ListIterator<Rule> factIterator = facts.listIterator();
      while (factIterator.hasNext()) { System.out.println(factIterator.next()); }
   }

}  // PartOfSpeech

