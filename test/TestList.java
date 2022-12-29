/**
 * TestList
 *
 * Tests the inference engine. Testing SLinkedList.
 *
 * Useful reference: https://swish.swi-prolog.org/
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestList {

   public static void main(String[] args) {

      SLinkedList  jobs  = SLinkedList.parse("[lawyer, teacher, programmer, janitor]");
      SLinkedList  jobs2 = SLinkedList.parse("[lawyer, teacher, programmer, janitor]");

      Constant scientist = new Constant("scientist");
      SLinkedList  jobs3 = SLinkedList.parse("[doctor, carpenter, sales manager]");
      SLinkedList  jobs4 = new SLinkedList(false, scientist, jobs3);

      // Set up the knowledge base.
      KnowledgeBase kb = new KnowledgeBase(
         new Rule(
            new Complex("test1"),
            new Unify(jobs, jobs2)
         ),
         new Rule(
            new Complex("test2($H, $H2, $T)"),
            new Unify(
               jobs,
               //new SLinkedList(new LogicVar("$H"),
               //          new LogicVar("$H2"),
               //          new LogicVar("$T"))
               SLinkedList.parse("[$H, $H2 | $T]")
            )
         ),
         new Rule(
            new Complex("test3($H, $T)"),
            new Unify(
               jobs, SLinkedList.parse("[$H, $_, $_ | $T]")
            )
         ),
         new Rule(
            new Complex("test4($T)"),
            new Unify(
               jobs,
               SLinkedList.parse("[$_ | $T]")
               // new SLinkedList(Anon.anon, new LogicVar("$T"))
            )
         ),
         new Rule(
            new Complex("test5($X)"),
            new Unify(jobs4, new LogicVar("$X"))
         ),
         new Rule(
            new Complex("test6($H, $T)"),
            new Unify(jobs4, SLinkedList.parse("[$H, $_ | $T]"))
         ),
         new Rule(
            new Complex("test7"),
            new Unify(SLinkedList.parse("[a, b, c]"), SLinkedList.parse("[a, b, c, d]"))
         )
      );

      kb.addRule("job(lawyer)");
      kb.addRule("job(teacher)");
      kb.addRule("job(programmer)");
      kb.addRule("job(janitor)");

      //kb.showKB();

      System.out.println("Test SLinkedList: ");

      Complex query;
      ArrayList<String> solutions;

      try {

         query = Make.query("test1");
         String[] expected1 = {"test1"};
         Solutions.verifyAll(query, kb, expected1, 0);

         query = Make.query("test2($H, $H2, $T)");
         String[] expected2 = {"test2(lawyer, teacher, [programmer, janitor])"};
         Solutions.verifyAll(query, kb, expected2, 0);

         query = Make.query("test3($A, $B)");
         String[] expected3 = {"test3(lawyer, [janitor])"};
         Solutions.verifyAll(query, kb, expected3, 0);

         query = Make.query("test4($T)");
         String[] expected4 = {"test4([teacher, programmer, janitor])"};
         Solutions.verifyAll(query, kb, expected4, 0);

         query = Make.query("test5($X)");
         String[] expected5 = {"test5([scientist, doctor, carpenter, sales manager])"};
         Solutions.verifyAll(query, kb, expected5, 0);

         query = Make.query("test6($H, $T)");
         String[] expected6 = {"test6(scientist, [carpenter, sales manager])"};
         Solutions.verifyAll(query, kb, expected6, 0);

         query = Make.query("test7");
         String[] expected7 = {};  // should not unify
         Solutions.verifyAll(query, kb, expected7, 0);

      } catch (TimeOverrunException tox) { }

   }
}  // TestList
