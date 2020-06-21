/**
 * ParseDemo
 *
 * This class parses English sentences. It was written as a demo for
 * the inference engine Inferencilo; it is not intended to be complete
 * or practical.
 *
 * In order to understand the comments below, it is necessary to have
 * a basic understanding of the programming language Prolog. Here are
 * a couple references:
 *
 * http://athena.ecs.csus.edu/~mei/logicp/prolog.html
 * https://courses.cs.washington.edu/courses/cse341/12au/prolog/basics.html
 *
 * To parse an English sentence, it is necessary to identify the parts
 * of speech, such as noun, pronoun, verb, etc.
 *
 * This demo first divides a sentence into words and punctuation.
 * For example, the sentence 'They envy us.' becomes a list of strings:
 *
 *      ["They", "envy", "us", "."]
 *
 * Next it creates a Prolog-like recursive list of Constants:
 *
 *      [They, envy, us, .]
 *
 * Note: In Prolog, a word beginning with a capital letter is a variable.
 * This inference engine is different. 'They' is a constant.
 *
 * The next step is to convert the above list into terms which identify
 * parts of speech. For example:
 *
 * [pronoun(They, subject, plural), verb(envy, present, base), pronoun(us, object, plural), period(.)]
 *
 * Many words can have more than one part of speech. The word 'envy', for
 * example, can be a noun or a verb. In order to convert a list of word
 * terms into a list of POS terms, the program needs facts which identify
 * possible parts of speech, such as:
 *
 *     word(envy, noun(envy, singular)).
 *     word(envy, verb(envy, present, base)).
 *
 * These above facts are generated by calling the function makeFacts()
 * of the class PartOfSpeech. These facts are then added to the knowledge
 * base.
 *
 * A rule called 'words_to_pos/2' is defined to convert the list of word
 * constants into a list of terms which identify part of speech. The
 * Prolog-like format of this rule would be:
 *
 *   words_to_pos([$H1 | $T1], [$H2 | $T2]) :- word($H1, $H2), words_to_pos($T1, $T2).
 *   words_to_pos([], []).
 *
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

import java.util.*;

class ParseDemo {

   //private static String testString =
   //   "The ideal characteristic of artificial intelligence is its ability to rationalize.";

   //private static String testString = "They envy us.";
   private static String testString = "He envies us.";

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
    * @return outList
    * @throws TimeOverrunException
    */
   private static PList oneSolution(Constant ruleFunctor, PList inList, KnowledgeBase kb)
                          throws TimeOverrunException {

      Variable X = VarCache.get("$X");  // placeholder
      Complex goal = new Complex(ruleFunctor, inList, X);
      SolutionNode node = goal.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = node.nextSolution();
      if (solution != null) {
         Complex result = (Complex)goal.replaceVariables(solution);
         return (PList)result.getTerm(2);  // Get the out list.
      }
      else {
         System.out.println("Fatal error. " + ruleFunctor);
      }
      return null;
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


   /**
    * main - The program starts here.
    *
    * @param  arguments
    */
   public static void main(String[] args) {

      // Clean up the string to analyze.
      // New line becomes a space.
      testString = testString.replace('\n', ' ');

      // Divide string into words and punctuation.
      List<String> words = Sentence.getWords(testString);

      // Display these words.
      for (String word : words) { System.out.println(word); }

      // Put all the words into a Prolog-style recursive list.
      // Create a list of Constant terms.
      List<Unifiable> terms = new ArrayList<Unifiable>();
      for (String word : words) { terms.add(new Constant(word)); }
      boolean hasPipe = false;
      PList wordList = new PList(hasPipe, terms);

      // Load part of speech data.
      PartOfSpeech pos = PartOfSpeech.getPartOfSpeech();

      // Make word facts, such as word(envy, noun(envy, singular)).
      List<Rule> facts = pos.makeFacts(words);

      // Set up the knowledge base, and fill it with word facts.
      KnowledgeBase kb = new KnowledgeBase();
      ListIterator<Rule> factIterator = facts.listIterator();
      while (factIterator.hasNext()) {
         Rule fact = factIterator.next();
         kb.addRule(fact);
      }

      // -------------------------------

      Constant parse = new Constant("parse");
      Constant words_to_pos = new Constant("words_to_pos");
      Constant word = new Constant("word");

      // Define Prolog variables.
      Variable H1 = VarCache.get("$H1");
      Variable H2 = VarCache.get("$H2");
      Variable T1 = VarCache.get("$T1");
      Variable T2 = VarCache.get("$T2");
      Variable In = VarCache.get("$In");

      /*
        words_to_pos/2 is a rule to convert a list of words into
        a list of parts of speech. For example, the Constant term 'the'
        is converted to the Complex term 'article(the, definite)'.
        The Prolog format of words_to_pos/2 would be:

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
      // rule = new Rule("words_to_pos([$H1 | $T1], [$H2 | $T2]) :- word($H1, $H2), words_to_pos($T1, $T2)");
      // The constructor for Rule will parse the given string to produce
      // the Rule object defined above. In Prolog, variables begin with
      // a capital letter and atoms (constants) begin with a lower case
      // letter. This inference engine is a little different. The parser
      // requires a dollar sign to identify variables. A constant can begin
      // with an upper case or lower case letter.

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

      makeRule("parse($In, $Out2) :- words_to_pos($In, $POS), sentence($POS, $Out2)", kb);

      kb.showKB();

      try {

         // Find a solution for the goal 'parse'.
         PList result = oneSolution(parse, wordList, kb);
         if (result == null) {
            System.out.println("Could not find a solution.");
         }

         // Print out the solution.
         System.out.println(result);

      }
      catch (TimeOverrunException tox) {
         System.out.println("Time overrun exception.");
      }

   } // main

}  // ParseDemo

