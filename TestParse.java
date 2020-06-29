/**
 * TestParse
 *
 * Does some simple parsing tests.
 *
 * Hint: A Rule has a head and a body, separated by :- .
 *    head(...) :- body(...).
 * A Fact is a the same as a Rule without a body.
 *    head(...).
 *
 * Useful tool: https://swish.swi-prolog.org/
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

import java.util.List;
import java.util.ArrayList;

public class TestParse {

   /*
    * convertToTerms
    *
    * Converts a sentence (space separated string of symbols)
    * into a Prolog list of terms.
    *
    * @param sentence (string)
    * @return Prolog list
    */
   private static PList convertToTerms(String sentence) {
      String[] tokens = sentence.split(" ");
      List<Unifiable> list = new ArrayList<Unifiable>();
      for (String token : tokens) {
         if (token.length() == 1) {
            list.add(Punctuation.makeTerm(token));
         }
         else {
            list.add(Word.makeTerm(token));
         }
      }
      PList p = new PList(false, list);
      return p;
   }

   public static void main(String[] args) {

      KnowledgeBase kb = new KnowledgeBase(
         //new Rule(new Complex("noun_phrase(noun)")),
         new Rule("word2term(word(after), preposition(after))"),
         new Rule("word2term(word(after), conjunction(after))"),
         new Rule("word2term(word(after), adverb(after))")
      );


      PList plist = convertToTerms("We go after .");
      //PList plist = convertToTerms("We .");
      System.out.println("----------------Word List: " + plist);

      Rule r1 = new Rule("convert_words([], [])");
      Rule r2 = new Rule("convert_words([$H | $T], [$H2 | $T2])",
                            new And(
                               new Complex("word2term($H, $H2)"),
                               new Complex("convert_words($T, $T2)")
                            )
                         );
      Rule r3 = new Rule("convert_words([$H | $T], [$H | $T2])",
                            new And(
                               //new Not(new Unify(Variable.instance("$H"), new Complex("word(after)"))),
                               new Not(new Unify("$H = word(after)")),
                               new Complex("convert_words($T, $T2)")
                            )
                         );
      kb.addRule(r1);
      kb.addRule(r2);
      kb.addRule(r3);

      System.out.print("Test Parse: ");
      Complex goal = new Complex(new Constant("convert_words"), plist, Variable.instance("$X"));

      try {
         String[] expected = {
            "[pronoun(We), verb(go), preposition(after), period(.)]",
            "[pronoun(We), verb(go), conjunction(after), period(.)]",
            "[pronoun(We), verb(go), adverb(after), period(.)]"
         };
         Solutions.verifyAll(goal, kb, expected, 2);
      } catch (TimeOverrunException tox) { }

/*    Left for reference.
      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      while (solution != null) {
         Complex result = (Complex)goal.replaceVariables(solution);
         System.out.println("result: " + result.getTerm(2));
         solution = root.nextSolution();
      }
*/

   }
}
