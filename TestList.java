/**
 * TestList
 *
 * Tests the inference engine.
 * Testing PList (Prolog List).
 *
 * Useful reference: https://swish.swi-prolog.org/
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestList {

   public static void main(String[] args) {

      PList    jobs  = PList.make("[lawyer, teacher, programmer, janitor]");
      PList    jobs2 = PList.make("[lawyer, teacher, programmer, janitor]");

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
               //new PList(Var.get("$H"), Var.get("$H2"), Var.get("$T"))
               PList.make("[$H, $H2 | $T]")
            )
         ),
         new Rule(
            new Complex("goal3($H, $T)"),
            new Unify(
               jobs,
               //new PList(Var.get("$H"), Anon.anon, Anon.anon, Var.get("$T"))
               PList.make("[$H, $_, $_ | $T]")
            )
         ),
         new Rule(
            new Complex("goal4($T)"),
            new Unify(
               jobs,
               PList.make("[$_ | $T]")
               // new PList(Anon.anon, Var.get("$T"))
            )
         )
      );

      kb.addFact("job(lawyer)");
      kb.addFact("job(teacher)");
      kb.addFact("job(programmer)");
      kb.addFact("job(janitor)");

      System.out.println("Test List: ");

      Complex goal;
      ArrayList<String> solutions;

      try {
         goal = new Complex("goal1");
         String[] expected1 = {"goal1"};
         Solutions.verifyAll(goal, kb, expected1, 0);

         goal = new Complex("goal2($H, $H2, $T)");
         String[] expected2 = {"goal2(lawyer, teacher, [programmer, janitor])"};
         Solutions.verifyAll(goal, kb, expected2, 0);

         goal = new Complex("goal3($A, $B)");
         String[] expected3 = {"goal3(lawyer, [janitor])"};
         Solutions.verifyAll(goal, kb, expected3, 0);

         goal = new Complex("goal4($T)");
         String[] expected4 = {"goal4([teacher, programmer, janitor])"};
         Solutions.verifyAll(goal, kb, expected4, 0);
      } catch (TimeOverrunException tox) { }

   }
}


