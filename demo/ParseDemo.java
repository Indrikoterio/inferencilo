/**
 * ParseDemo
 *
 * This program parses English sentences. It was written as a demo for
 * the inference engine Inferencilo. It is not intended to be complete
 * or practical.
 *
 * In order to understand the comments below, it is necessary to have
 * a basic understanding of the programming language Prolog. Here are
 * a couple references:
 *
 * http://athena.ecs.csus.edu/~mei/logicp/prolog.html
 * https://courses.cs.washington.edu/courses/cse341/12au/prolog/basics.html
 *
 * The program starts at main. The first thing is does is to create a
 * KnowledgeBase. Next it defines some Constants and Variables, and some
 * Rules, which it adds to the KnowledgeBase.
 *
 * The static method ReadRules.fromFile(...) fetches additional rules
 * from a text file, demo_grammar.txt. These rules are also added to
 * the KnowledgeBase.
 *
 * After the rules are created, the program reads English sentences from
 * a text file, sentences.txt. It calls the method splitIntoSentences(),
 * which divides the text according to sentence ending punctuation (?!.).
 *
 * Next, the program calls sentenceToFacts(). This method does several
 * things. It divides each sentence into words and punctuation, by calling
 * sentenceToWords().
 *
 *    "They envy us."
 *
 * becomes...
 *
 *    ["They", "envy", "us", "."]
 *
 * Next it creates a Prolog-style linked list (PList), by calling
 * makeLinkedList():
 *
 *      [They, envy, us, .]
 *
 * Note: In Prolog, a word beginning with a capital letter is a variable.
 * This inference engine is different. 'They' is a Constant. Variables
 * begin with a dollar sign.
 *
 * Finally, sentenceToFacts() creates a list of facts which identify
 * part of speech, by calling PartOfSpeech.makeFacts(). This method
 * (makeFacts) creates facts such as...
 *
 *    word(we, pronoun(we , subject, first, plural))
 *
 * ...which are added to the KnowledgeBase.
 *
 * Note: Many words can have more than one part of speech. The word 'envy',
 * for example, can be a noun or a verb. In order to parse English sentences,
 * the program needs facts which identify all possible parts of speech:
 *
 *     word(envy, noun(envy, singular)).
 *     word(envy, verb(envy, present, base)).
 *
 * Finally, the program calls the method oneSolution(), which tries to find
 * a solution for the goal 'parse'.
 *
 * The arguments of oneSolution() are:
 *   functor of goal (= parse)
 *   input argument - a word list, such as [They, envy, us, .]
 *   knowledge base
 *
 * During analysis, the rule words_to_pos/2 is applied to convert the input
 * word list, created by sentenceToFacts(), into a list of terms which
 * identify part of speech.
 *
 *   words_to_pos([$H1 | $T1], [$H2 | $T2]) :-
 *                                  word($H1, $H2), words_to_pos($T1, $T2).
 *   words_to_pos([], []).
 *
 * The sentence "They envy us." will become:
 *
 * [pronoun(They, subject, third, plural), verb(envy, present, base),
 *          pronoun(us, object, first, plural), period(.)]
 *
 * The inference rule 'sentence' identifies (unifies with) various types
 * of sentence, such as:
 *
 *   subject pronoun, verb
 *   subject noun, verb
 *   subject pronoun, verb, object
 *   subject noun, verb, object
 *
 * There are rules to check subject/verb agreement of these sentences:
 *
 *    check_pron_verb
 *    check_noun_verb
 *
 * When a mismatch is found (*He envy), these rules print out an error
 * message:
 *
 * 'He' and 'envy' do not agree.
 *
 * @author Cleve (Klivo) Lendon, 2020
 * @version 1.0
 */

import inferencilo.*;

import java.io.*;
import java.util.*;

class ParseDemo {

   /*
    * constructor
    *
    * Read in a file of Part of Speech data. Create a hashmap.
    */
   private ParseDemo() { }


   /*
    * oneSolution
    *
    * Searches for a solution to the given goal and list.
    *
    * @param  ruleFunctor
    * @param  inList
    * @param  knowledgeBase
    */
   private static void oneSolution(Constant ruleFunctor,
                                   PList inList,
                                   KnowledgeBase kb) {

      Variable X = Variable.inst("$X");  // placeholder variable
      try {
         Complex goal = new Complex(ruleFunctor, inList, X);
         SolutionNode node = goal.getSolver(kb, new SubstitutionSet(), null);
         SubstitutionSet solution = node.nextSolution();
         if (solution != null) {
            Complex result = (Complex)goal.replaceVariables(solution);
            PList outList = (PList)result.getTerm(2);  // Get the out list.
            System.out.print("\n");
         }
         else {
            System.out.println("\nNo solution for " + ruleFunctor + ".");
         }
      }
      catch (TimeOverrunException tox) {
         System.out.println("Time overrun exception.");
      }

   } // oneSolution


   /*
    * makeRule
    *
    * Parses a Prolog-like rule string, creates a Rule object,
    * and stores the Rule into the knowledge base.
    *
    * @param str
    * @param kb
    */
   private static void makeRule(String str, KnowledgeBase kb) {
      Rule rule = new Rule(str);
      kb.addRule(rule);
   }


   /*
    * sentenceToWords
    *
    * Divides a sentence into words.
    *
    * @param  original sentence
    * @return list of words
    */
   private static List<String> sentenceToWords(String sentence) {
      // Clean up the string. New line becomes a space.
      String s = sentence.replace('\n', ' ');
      // Divide string into words and punctuation.
      return Sentence.getWords(s);
   } // sentenceToWords


   /*
    * makeLinkedList
    *
    * Converts a list of words into a Prolog-style, singly linked
    * list of words.
    *
    * @param  list of words
    * @return linked list of words
    */
   private static PList makeLinkedList(List<String> words) {

      // Put all the words into a Prolog-style linked list.
      // First, create a list of Constant terms.
      List<Unifiable> terms = new ArrayList<Unifiable>();
      for (String word : words) { terms.add(new Constant(word)); }

      boolean hasPipe = false;
      return new PList(hasPipe, terms);

   } // makeLinkedList


   /*
    * sentenceToFacts
    *
    * Takes a sentence, divides it into words, and creates facts
    * which are written to the knowledge base.
    *
    * @param  sentence
    * @param  knowledge base
    * @return word list (linked list)
    */
   private static PList sentenceToFacts(String sentence, KnowledgeBase kb) {

      List<String> words = sentenceToWords(sentence);
      PList wordList = makeLinkedList(words);

      // Load part of speech data.
      PartOfSpeech pos = PartOfSpeech.getPartOfSpeech();

      // Make word facts, such as: word(envy, noun(envy, singular)).
      List<Rule> facts = pos.makeFacts(words);

      // Fill the knowledge base with word facts.
      facts.forEach(fact -> kb.addRule(fact));

      return wordList;

   } // sentenceToFacts


   /*
    * openFile
    *
    * Opens a file for reading, in UTF-8 format.
    * Returns a buffered reader, or null if an exception occurs.
    *
    * @param  file name
    * @return BufferedReader
    */
   private static BufferedReader openFile(String filename) {
      try {
         return new BufferedReader(
            new InputStreamReader(new FileInputStream(filename),"UTF-8")
         );
      }
      catch (Exception e) {
         System.err.println("ParseDemo - Cannot open " + filename + ".");
         return null;
      }
   } // openFile


   /*
    * readFile
    *
    * Reads a file into a string.
    *
    * @param  buffered reader
    * @return file contents as string
    */
   private static String readFile(BufferedReader reader) {

      String line;
      StringBuilder sb = new StringBuilder("");

      try {
         line = reader.readLine();
         while (true) {
            if (line == null) break;
            sb.append(line).append("\n");
            line = reader.readLine();
         } // while
      }
      catch (IOException e) {
         System.err.println("ParseDemo: io error:\n" + e);
         return null;
      }
      return sb.toString();

   } // readFile


   /*
    * isPunc
    *
    * Returns true if the character is a punctuation mark,
    * possibly marking the end of a sentence.
    *
    * @param  character to test
    * @return t/f
    */
   private static boolean isPunc(char c) {
      if (c == '!' || c == '?' || c == '.') return true;
      return false;
   }  // isPunc

   /*
    * endOfWord
    *
    * Returns true if the current character is a space,
    * or is at the end of a line.
    *
    * @param  character to test
    * @return t/f
    */
   private static boolean endOfWord(char c) {
      if (c == ' ' || c == '\n') return true;
      return false;
   }  // endOfWord


   /*
    * splitIntoSentences
    *
    * Splits a string of text into sentences, by searching
    * for punctuation. The punctuation must be followed by
    * a space. (The period in '3.14' doesn't mark the end
    * of a sentence.)
    *
    * @param  input string
    * @return list of sentences
    */
   private static List<String> splitIntoSentences(String str) {

      List<String> sentences = new ArrayList<String>();
      String sentence;
      int previousIndex = 0;
      int index;
      char[] prev3 = {'a', 'a', 'a'};

      int i;
      for (i = 0; i < str.length(); i++) {

         char c = str.charAt(i);
         if (endOfWord(c) && isPunc(prev3[2])) {
            if (prev3[2] == '.') {
               // Check for H.G. Wells or H. G. Wells
               if (prev3[0] != '.' && prev3[0] != ' ') {
                  sentences.add(str.substring(previousIndex, i).trim());
                  previousIndex = i;
               }
            }
            else {
               sentences.add(str.substring(previousIndex, i).trim());
               previousIndex = i;
            }
         }

         prev3[0] = prev3[1];
         prev3[1] = prev3[2];
         prev3[2] = c;

      } // for
      if (i >= str.length() ) {
          String s = str.substring(previousIndex, str.length()).trim();
          if (s.length() > 0) sentences.add(s);
      }

      return sentences;

   }  // splitIntoSentences


   /**
    * main - The program starts here.
    *
    * @param  arguments
    */
   public static void main(String[] args) {

      // A knowledge base stores rules and facts.
      KnowledgeBase kb = new KnowledgeBase();

      // -------------------------------

      Constant parse = new Constant("parse");
      Constant words_to_pos = new Constant("words_to_pos");
      Constant word = new Constant("word");

      // Define variables.
      Variable H1 = Variable.inst("$H1");
      Variable H2 = Variable.inst("$H2");
      Variable T1 = Variable.inst("$T1");
      Variable T2 = Variable.inst("$T2");
      Variable In = Variable.inst("$In");

      /*
        words_to_pos/2 is a rule to convert a list of words into a list
        of parts of speech. For example, the Constant term 'the' is
        converted to the Complex term 'article(the, definite)'. The Prolog
        format of words_to_pos/2 would be:

        words_to_pos([H1 | T1], [H2 | T2]) :- word(H1, H2), words_to_pos(T1, T2).
        words_to_pos([], []).

       */

      Rule rule = new Rule(new Complex(words_to_pos, new PList(true, H1, T1),
                                                     new PList(true, H2, T2)),
                         new And(
                            new Complex(word, H1, H2),
                            new Complex(words_to_pos, T1, T2)
                         )
                      );

      // Note: The Constant, Variable and Rule definitions above can be
      // replaced by a single line:
      // rule = new Rule("words_to_pos([$H1 | $T1], [$H2 | $T2]) :- " +
      //         "word($H1, $H2), words_to_pos($T1, $T2)");
      // The constructor for Rule will parse the given string to produce
      // the Rule object defined above. In Prolog, variables begin with
      // a capital letter and atoms (constants) begin with a lower case
      // letter. This inference engine is a little different. The parser
      // requires a dollar sign to identify variables. A constant can
      // begin with an upper case or lower case letter.

      kb.addRule(rule);  // Add the rule to our knowledge base.

      rule = new Rule(new Complex(words_to_pos, PList.empty, PList.empty));
      // Alternative (simpler) rule definition:
      //rule = new Rule("words_to_pos([], [])");
      kb.addRule(rule);

      // Rules for noun phrases.
      makeRule("make_np([adjective($Adj, $_), noun($Noun, $Plur) | $T], [$NP | $Out]) :- " +
            "!, $NP = np([$Adj, $Noun], $Plur), make_np($T, $Out)", kb);
      makeRule("make_np([$H | $T], [$H | $T2]) :- make_np($T, $T2)", kb);
      makeRule("make_np([], [])", kb);

      // It is also possible to read facts and rules from file.
      List<String> rules = ReadRules.fromFile("demo_grammar.txt");
      kb.addRules(rules);

      //kb.showKB();

      // Get English sentences.
      BufferedReader reader = openFile("sentences.txt");
      if (reader == null) return;
      String text = readFile(reader);
      if (text == null) return;
      List<String> sentences = splitIntoSentences(text);

      sentences
         .stream()
         .map(s -> { System.out.print(s + " "); return s; })
         .map(s2 -> sentenceToFacts(s2, kb)) // Returns word list (wl).
         .forEach(wl -> oneSolution(parse, wl, kb));

   } // main

}  // ParseDemo

