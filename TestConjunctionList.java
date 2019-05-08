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

   public static Variable h        = new Variable("$H");
   public static Variable t        = new Variable("$T");
   public static Variable t1       = new Variable("$T1");
   public static Variable t2       = new Variable("$T2");

   public static Variable in       = new Variable("$In");
   public static Variable i2       = new Variable("$I2");
   public static Variable i3       = new Variable("$I3");
   public static Variable out      = new Variable("$Out");

   public static Variable sentence_out  = new Variable("$SentenceOut");

   public static Variable out_list   = new Variable("$OutList");
   public static Complex  noun_list  = new Complex("noun_list(list)");

   public static void main(String[] args) {   // Set up the knowledge base.

      // Test conjunction list(s).
      String str1 = "[pronoun(We), verb(visited), ";
      String str2 = "noun(Canada), comma(\\,), noun(Italy), ";
      String str3 = "conjunction(and), noun(Germany)]";
      PList sentence_in = PList.make(str1 + str2 + str3);

      KnowledgeBase kb = new KnowledgeBase();
      ConjunctionList.makeRule(kb, collect_noun_list, "comma", "conjunction", "noun");

      Rule rule;

      rule = new Rule("scan_sentence([], [])");
      kb.addRule(rule);

      rule = new Rule(new Complex(scan_sentence, in, out),
                         new And(
                            new Complex(collect_noun_list, in, out_list, i2),
                            new Complex(scan_sentence, i2, i3),
                            new Unify(out, new JoinHeadTail(out_list, i3))
                         )
                      );
      kb.addRule(rule);

      rule = new Rule(new Complex(scan_sentence, in, out),
                            new And(
                               new Unify(in, new PList(true, h, t1)),
                               new Complex(scan_sentence, t1, t2),
                               new Unify(out, new JoinHeadTail(h, t2))
                            )
                         );
      kb.addRule(rule);
      //kb.showKB();
      System.out.print("-------------------------\n");


      //System.out.println("" + sentence_in);
      System.out.print("Test Conjunction-Lists: ");
      Complex goal = new Complex(scan_sentence, sentence_in, sentence_out);
      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);


      try {
         String[] expected = {
            "[pronoun(We), verb(visited), the_list(Canada \\, Italy and Germany)]",
            "[pronoun(We), verb(visited), noun(Canada), comma(\\,), the_list(Italy and Germany)]",
            "[pronoun(We), verb(visited), noun(Canada), comma(\\,), noun(Italy), conjunction(and), noun(Germany)]"
         };
         Solutions.verifyAll(goal, kb, expected, 2);
      } catch (TimeOverrunException tox) { }


   /*
      try {
         SubstitutionSet solution = root.nextSolution();
         Complex result;
         int count = 0;
         while (solution != null) {
            result = (Complex)goal.replaceVariables(solution);
            System.out.println(result.getTerm(2));
            solution = root.nextSolution();
            count++; if (count > 10) break;  // for safety
         }
      } catch (TimeOverrunException tox) { }
    */

   }
}


