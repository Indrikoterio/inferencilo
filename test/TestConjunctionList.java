/**
 * TestConjunctionList
 *
 * Test built-in predicates which collect conjunction lists.
 * Eg. word1, word2 conjunction word3
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestConjunctionList {

   public static Constant collect_noun_list = new Constant("collect_noun_list");

   public static Constant scan_sentence = new Constant("scan_sentence");

   public static Variable H   = new Variable("$H");
   public static Variable T1  = new Variable("$T1");
   public static Variable T2  = new Variable("$T2");

   public static Variable In  = new Variable("$In");
   public static Variable I2  = new Variable("$I2");
   public static Variable I3  = new Variable("$I3");
   public static Variable Out = new Variable("$Out");

   public static Variable SentenceOut  = new Variable("$SentenceOut");
   public static Variable ConjList   = new Variable("$ConjList");

   public static void main(String[] args) {   // Set up the knowledge base.

      // Test conjunction list(s).
      String str1 = "[pronoun(We), verb(visited), ";
      String str2 = "noun(Canada), comma(\\,), noun(Italy), ";
      String str3 = "conjunction(and), noun(Germany)]";
      SLinkedList sentence_in = SLinkedList.make(str1 + str2 + str3);

      KnowledgeBase kb = new KnowledgeBase();
      ConjunctionList.makeRule(kb, collect_noun_list, "comma", "conjunction", "noun");

      Rule rule;

      rule = new Rule("scan_sentence([], [])");
      kb.addRule(rule);

      /* Prolog form:
       scan_sentence(In, [OutList | I3]) :- collect_noun_list(In, OutList, I2),
            scan_sentence(I2, I3)).
       */

      rule = new Rule(new Complex(scan_sentence, In, new SLinkedList(true, ConjList, I3)),
                         new And(
                            new Complex(collect_noun_list, In, ConjList, I2),
                            new Complex(scan_sentence, I2, I3)
                         )
                      );
      kb.addRule(rule);

      /* Prolog form:
       scan_sentence([H | T1], [H | T2]) :- scan_sentence(T1, T2).
       */

      rule = new Rule(new Complex(scan_sentence, new SLinkedList(true, H, T1),
                                                 new SLinkedList(true, H, T2)),
                            new And(new Complex(scan_sentence, T1, T2))
                         );

      kb.addRule(rule);
      //kb.showKB();

      System.out.print("-------------------------\n");

      //System.out.println("" + sentence_in);
      System.out.print("Test Conjunction-Lists: ");
      Complex goal = new Complex(scan_sentence, sentence_in, SentenceOut);
//      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);

      try {
         String[] expected = {
            "[pronoun(We), verb(visited), the_list(Canada, Italy and Germany)]",
            "[pronoun(We), verb(visited), noun(Canada), comma(,), the_list(Italy and Germany)]",
            "[pronoun(We), verb(visited), noun(Canada), comma(,), noun(Italy), conjunction(and), noun(Germany)]"
         };
         Solutions.verifyAll(goal, kb, expected, 2);
      } catch (TimeOverrunException tox) {}
   }

} // TestConjunctionList



