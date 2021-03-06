/**
 * TestPList
 *
 * Tests the inference engine. Testing PList (Prolog List).
 *
 * Useful reference: https://swish.swi-prolog.org/
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestPList {

   public static void main(String[] args) {

      PList  jobs  = PList.parse("[lawyer, teacher, programmer, janitor]");
      PList  jobs2 = PList.parse("[lawyer, teacher, programmer, janitor]");

      Constant scientist = new Constant("scientist");
      PList  jobs3 = PList.parse("[doctor, carpenter, sales manager]");
      PList  jobs4 = new PList(false, scientist, jobs3);

      // Set up the knowledge base.
      KnowledgeBase kb = new KnowledgeBase(
         new Rule(
            new Complex("goal1"),
            new Unify(jobs, jobs2)
         ),
         new Rule(
            new Complex("goal2($H, $H2, $T)"),
            new Unify(
               jobs,
               //new PList(new Variable("$H"),
               //          new Variable("$H2"),
               //          new Variable("$T"))
               PList.parse("[$H, $H2 | $T]")
            )
         ),
         new Rule(
            new Complex("goal3($H, $T)"),
            new Unify(
               jobs, PList.parse("[$H, $_, $_ | $T]")
            )
         ),
         new Rule(
            new Complex("goal4($T)"),
            new Unify(
               jobs,
               PList.parse("[$_ | $T]")
               // new PList(Anon.anon, new Variable("$T"))
            )
         ),
         new Rule(
            new Complex("goal5($X)"),
            new Unify(jobs4, new Variable("$X"))
         ),
         new Rule(
            new Complex("goal6($H, $T)"),
            new Unify(jobs4, PList.parse("[$H, $_ | $T]"))
         ),
         new Rule(
            new Complex("goal7"),
            new Unify(PList.parse("[a, b, c]"), PList.parse("[a, b, c, d]"))
         )
      );

      kb.addRule("job(lawyer)");
      kb.addRule("job(teacher)");
      kb.addRule("job(programmer)");
      kb.addRule("job(janitor)");

      //kb.showKB();

      System.out.println("Test PList: ");

      Complex goal;
      ArrayList<String> solutions;

      try {

         goal = Make.goal("goal1");
         String[] expected1 = {"goal1"};
         Solutions.verifyAll(goal, kb, expected1, 0);

         goal = Make.goal("goal2($H, $H2, $T)");
         String[] expected2 = {"goal2(lawyer, teacher, [programmer, janitor])"};
         Solutions.verifyAll(goal, kb, expected2, 0);

         goal = Make.goal("goal3($A, $B)");
         String[] expected3 = {"goal3(lawyer, [janitor])"};
         Solutions.verifyAll(goal, kb, expected3, 0);

         goal = Make.goal("goal4($T)");
         String[] expected4 = {"goal4([teacher, programmer, janitor])"};
         Solutions.verifyAll(goal, kb, expected4, 0);

         goal = Make.goal("goal5($X)");
         String[] expected5 = {"goal5([scientist, doctor, carpenter, sales manager])"};
         Solutions.verifyAll(goal, kb, expected5, 0);

         goal = Make.goal("goal6($H, $T)");
         String[] expected6 = {"goal6(scientist, [carpenter, sales manager])"};
         Solutions.verifyAll(goal, kb, expected6, 0);

         goal = Make.goal("goal7");
         String[] expected7 = {};  // should not unify
         Solutions.verifyAll(goal, kb, expected7, 0);

      } catch (TimeOverrunException tox) { }

   }
}  // TestPList
