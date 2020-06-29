/**
 * ConjunctionList
 *
 * A conjunction list has this form: word1 comma word2 comma word3 conjunction word4.
 * For example: Canada, Italy and Germany. Or: Canada, Sweden, Italy and Germany.
 * Note: There may be a comma before the conjunction.
 *
 * This class is used to collect the items of such a list.
 *
 * ConjunctionList is not a built-in predicate; it generates a rule in the given
 * knowledge base, based on the given parameters.
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class ConjunctionList {

   public static Constant middle_of_list = new Constant("middle_of_list");

   public static Variable in  = Variable.instance("$In");
   public static Variable i2  = Variable.instance("$I2");
   public static Variable i3  = Variable.instance("$I3");
   public static Variable i4  = Variable.instance("$I4");
   public static Variable out = Variable.instance("$Out");
   public static Variable h   = Variable.instance("$H");
   public static Variable t   = Variable.instance("$T");

   public static Variable k1 = Variable.instance("$K1");
   public static Variable k2 = Variable.instance("$K2");
   public static Variable k3 = Variable.instance("$K3");

   public static Variable start          = Variable.instance("$Start");
   public static Variable middle         = Variable.instance("$Middle");
   public static Variable end_of_list    = Variable.instance("$End");

   public static Variable out_list  = Variable.instance("$OutList");
   public static Complex  the_list  = new Complex("the_list(list)");

   /**
    * constructor
    */
   public ConjunctionList() { }


   /**
    * makeRule
    *
    * This method creates a new rule for collecting a conjunction-list.
    *
    * The list of elements includes the text for 'comma' and 'conjunction',
    * followed by a list of parts of speech. The reason 'comma' and 'conjunction'
    * are needed is because these words are different in other languages.
    * For example, in Esperanto, these words are 'komo' and 'konjunkcio'.
    *
    * @param  knowledge base
    * @param  name of rule (Constant)
    * @param  array of list elements (comma, conjunction, noun...)
    */
   public static void makeRule(KnowledgeBase kb, Constant ruleName,
                                            String... listElements) {
      Rule rule;

      /*
         The predicate 'comma_word' collects a comma and a word (, Italy).
         middle_of_list(In, K3, Out) :- comma_word(In, K1, I2),
                                        middle_of_list(I2, K2, Out),
                                        append(K1, K2, K3).
      */

      rule = new Rule(new Complex(middle_of_list, in, k3, out),
                new And(
                    new CommaWord(listElements, in, k1, i2),
                    new Complex(middle_of_list, i2, k2, out),
                    new Append(k1, k2, k3)
                )
      );
      kb.addRule(rule);

      /*
         middle_of_list(In, [], In).
      */
      rule = new Rule(new Complex(middle_of_list, in, PList.empty, in));
      kb.addRule(rule);


      /*
         collect_list(In, OutList, Out) :-
                      start_of_list(In, Start, I2),
                      middle_of_list(I2, Middle, I3),
                      optional_comma(I3, Comma, I4),
                      end_of_list(I4, End, Out),
                      replace_term1(the_list, OutList, Start, Middle, Comma, End).
      */
      rule = new Rule(new Complex(ruleName, in, out_list, out),
                new And(
                   new StartList(listElements, in, start, i2),
                   new Complex(middle_of_list, i2, middle, i3),
                   new OptionalComma(listElements, i3, k1, i4),
                   new ConjunctionWord(listElements, i4, end_of_list, out),
                   new ReplaceTerm1(the_list, out_list, start, middle, k1, end_of_list)
                )
      );
      kb.addRule(rule);
   }

}


