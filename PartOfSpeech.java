/**
 * PartOfSpeech
 *
 * This class has static methods which read in a list of words with
 * part-of-speech tags, and creates a hashmap keyed by word. The source
 * file is part_of_speech.txt.
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
 * NPS proper noun, plural Vikings
 * PDT predeterminer 'all the kids'
 * PN nominal pronoun (everybody, nothing)
 * PP$ possessive personal pronoun (my, our)
 * PP$$ second (nominal) personal pronoun (mine, ours)
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

   // Word functor. Use capitals to distinguish from the variable 'word'.
   private static Constant WORD = new Constant("word");

   private static Constant noun = new Constant("noun");
   private static Constant verb = new Constant("verb");
   private static Constant pronoun = new Constant("pronoun");
   private static Constant adjective = new Constant("adjective");
   private static Constant participle = new Constant("participle");
   private static Constant preposition = new Constant("preposition");
   private static Constant unknown = new Constant("unknown");

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

   // Plurality for nouns and pronouns
   private static Constant singular = new Constant("singular"); // table, mouse
   private static Constant plural   = new Constant("plural"); // tables, mice
   private static Constant both     = new Constant("both"); // you

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
   private static Constant object  = new Constant("object");   // object

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
    * makePronounTerm
    *
    * This method creates a pronoun term, eg.
    *       pronoun(they, subject).
    *
    * @param  word
    * @param  lower case word
    * @param  part of speech tag
    * @return term
    */
   private static Complex makePronounTerm(String word, String lower, String tag) {
      Complex term = null;
      if (tag.equals("PPSS")) {
         if (lower.equals("we") || lower.equals("they")) {
            term = new Complex(pronoun, new Constant(word), subject, plural);
         }
         else {
            term = new Complex(pronoun, new Constant(word), subject, singular);
         }
      }
      else if (tag.equals("PPO")) {
         if (lower.equals("us") || lower.equals("them")) {
            term = new Complex(pronoun, new Constant(word), object, plural);
         }
         else {
            term = new Complex(pronoun, new Constant(word), object, singular);
         }
      }
      return term;
   } // makePronounTerm


   /*
    * makeYouFact
    *
    * This method creates a fact for the pronoun 'you', eg.
    *       word(you, pronoun(you, subject, singular)).
    *       word(you, pronoun(you, object, plural)).
    *
    * @param  word
    * @param  subject or object
    * @param  plurality
    * @return fact
    */
   private static Complex makeYouFact(String word, Constant sub_obj, Constant plurality) {
      Complex term = new Complex(pronoun, new Constant(word), sub_obj, plurality);
      return new Complex(WORD, new Constant(word), term);
   } // makeYouFact


   /*
    * makeVerbTerm
    *
    * This method creates a verb term, eg. verb(listen, present, base).
    *
    * @param  word
    * @param  part of speech tag
    * @return term
    */
   private static Complex makeVerbTerm(String word, String tag) {
      Complex term = null;
      if (tag.equals("VB")) {
         term = new Complex(verb, new Constant(word), present, base);
      }
      else if (tag.equals("VBZ")) {
         term = new Complex(verb, new Constant(word), present, third_sing);
      }
      else if (tag.equals("VBD")) {
         term = new Complex(verb, new Constant(word), past, base);
      }
      else if (tag.equals("VBG")) {
         term = new Complex(participle, new Constant(word), active);
      }
      else if (tag.equals("VBN")) {
         term = new Complex(participle, new Constant(word), passive);
      }
      return term;
   } // makeVerbTerm


   /*
    * makeNounTerm
    *
    * This method creates a noun term, eg. noun(speaker, singular).
    *
    * @param  word
    * @param  part of speech tag
    * @return term
    */
   private static Complex makeNounTerm(String word, String tag) {
      Complex term = null;
      if (tag.equals("NN")) {
         term = new Complex(noun, new Constant(word), singular);
      }
      else if (tag.equals("NNS")) {
         term = new Complex(noun, new Constant(word), plural);
      }
      else if (tag.equals("NNP")) {
         term = new Complex(noun, new Constant(word), singular);
      }
      return term;
   } // makeNounTerm


   /*
    * makeAdjectiveTerm
    *
    * This method creates an adjective term, eg. adjective(happy).
    *
    * @param  word
    * @param  part of speech tag
    * @return term
    */
   private static Complex makeAdjectiveTerm(String word, String tag) {
      Complex term = null;
      if (tag.equals("JJ")) {
         term = new Complex(adjective, new Constant(word), positive);
      }
      else if (tag.equals("JJR")) {
         term = new Complex(adjective, new Constant(word), comparative);
      }
      else if (tag.equals("JJS")) {
         term = new Complex(adjective, new Constant(word), superlative);
      }
      return term;
   } // makeAdjectiveTerm


   /*
    * makeArticleTerm
    *
    * This method creates terms for articles, eg. article(the, definite).
    *
    * @param  word
    * @return term
    */
   private static Complex makeArticleTerm(String word) {
      Complex term = null;
      String wordLower = word.toLowerCase();
      if (wordLower.equals("the")) {
         term = new Complex(article, new Constant(word), definite);
      }
      else {
         term = new Complex(article, new Constant(word), indefinite);
      }
      return term;
   } // makeArticleTerm


   /*
    * makeAdverbTerm
    *
    * This method creates adverb terms, eg. adverb(happily).
    *
    * @param  word
    * @return term
    */
   private static Complex makeAdverbTerm(String word) {
      Complex term = new Complex(adverb, new Constant(word));
      return term;
   } // makeAdverbTerm


   /*
    * makePrepositionTerm
    *
    * This method creates preposition terms, eg. preposition(from).
    *
    * @param  word
    * @return term
    */
   private static Complex makePrepositionTerm(String word) {
      Complex term = new Complex(preposition, new Constant(word));
      return term;
   } // makePrepositionTerm


   /*
    * makeUnknownTerm
    *
    * This method creates terms for words with unknown part of speech.
    *
    * @param  word
    * @return term
    */
   private static Complex makeUnknownTerm(String word) {
      Complex term = new Complex(preposition, new Constant(word));
      return term;
   } // makeUnknownTerm


   /*
    * makeTerm
    *
    * This method creates one (complex) Term object for an English word.
    * The second parameter is a part of speech tag, such as NNS or VBD.
    * Tags are listed at the top of this file.
    *
    * @param  word
    * @param  lower case word
    * @param  part of speech tag
    * @return complex term
    */
   private static Complex makeTerm(String word, String lower, String tag) {
      if (tag.startsWith("VB")) return makeVerbTerm(word, tag);
      if (tag.startsWith("NN")) return makeNounTerm(word, tag);
      if (tag.startsWith("PP")) return makePronounTerm(word, lower, tag);
      if (tag.startsWith("JJ")) return makeAdjectiveTerm(word, tag);
      if (tag.equals("AT")) return makeArticleTerm(word);
      if (tag.equals("IN")) return makePrepositionTerm(word);
      if (tag.equals("RB")) return makeAdverbTerm(word);
      return null;
   } // makeTerm


   /*
    * makeFacts
    *
    * This method takes an English word and produces Fact objects,
    * which can be analyzed by the inference engine.
    *
    * For some words, the part of speech is unambiguous. For
    * example, 'the' can only be a definite article:
    *
    *      article(the, definite)
    *
    * Other words can have more than one part of speech. The word
    * 'envy', for example, might be a noun or a verb.
    *
    *      noun(envy, singular)
    *      verb(envy, present, base)
    *
    * For 'envy', a parsing algorithm must be able to test both
    * possibilities. Therefore, the inference engine will need two
    * facts for the knowledge base:
    *
    *      word(envy, noun(envy, singular)).
    *      word(envy, verb(envy, present, base)).
    *
    * @param  word
    * @return facts
    */
   private static List<Rule> makeFacts(String word) {

      Complex  term;
      Rule     fact;

      String low = lowerCaseExceptI(word);
      List<Rule> facts = new ArrayList<Rule>();

      // Handle pronoun 'you', which is very ambiguous.
      if (low.equals("you")) {
         fact = new Rule(makeYouFact(word, subject, singular));
         facts.add(fact);
         fact = new Rule(makeYouFact(word, subject, plural));
         facts.add(fact);
         fact = new Rule(makeYouFact(word, object, singular));
         facts.add(fact);
         fact = new Rule(makeYouFact(word, object, plural));
         facts.add(fact);
         return facts;
      }

      String[] posData = get(word);
      if (posData == null) {
         posData = get(low);
      }

      if (posData != null && posData.length > 0) {
         for (String pos : posData) {
            term = makeTerm(word, low, pos);
            if (term != null) {
               Complex wordTerm = new Complex(WORD, new Constant(word), term);
               fact = new Rule(wordTerm);
               facts.add(fact);
            }
         }
      }
      if (facts.size() < 1) {
         term = new Complex(unknown, new Constant(word));
         Complex wordTerm = new Complex(WORD, new Constant(word), term);
         fact = new Rule(wordTerm);
         facts.add(fact);
      }
      return facts;

   } // makeFacts


   /**
    * makeFacts
    *
    * This method takes a list of words, and creates a list
    * of facts which can be analyzed by the inference engine.
    * The word 'envy', for example, should produce two facts.
    *
    *      word(envy, noun(envy, singular)).
    *      word(envy, verb(envy, present, base)).
    *
    * Note: A Fact is the same as a Rule without a body.
    *
    * @param  list of words
    * @return list of facts
    */
   public static List<Rule> makeFacts(List<String> words) {
      List<Rule> facts = new ArrayList<Rule>();
      List<Rule> wordFacts;
      for (String word : words) {
         wordFacts = makeFacts(word);
         if (wordFacts != null) {
            facts.addAll(wordFacts);
         }
      }
      return facts;
   } // makeFacts


}  // PartOfSpeech

