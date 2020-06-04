/**
 * TestMake
 *
 * This test is similar to TestAndOr. The difference is that instead of
 * using class constructors to define logical operands, the factory methods
 * in the Make class are used. Thus, instead of:
 *   new And(new Complex("father($X, $Z)"), new Complex("parent($Z, $Y)"))
 * this test uses:
 *   Make.and("father($X, $Z), parent($Z, $Y)")
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestMake {

   public static void main(String[] args) {

      // Set up the knowledge base.
      KnowledgeBase kb = new KnowledgeBase(
         new Rule("song(Cache ta joie)"),
         new Rule("song(Fade to Grey)"),
         new Rule("father(George, Frank)"),
         new Rule("father(George, Sam)"),
         new Rule("mother(Gina, Frank)"),
         new Rule("mother(Gina, Sam)"),
         new Rule("mother(Maria, Marcus)"),
         new Rule("father(Frank, Marcus)"),
         new Rule("parent($X, $Y)", "father($X, $Y); mother($X, $Y)"),
         new Rule("relative($X, $Y)", "grandfather($X, $Y); father($X, $Y); mother($X, $Y)"),
         new Rule("grandfather($X, $Y)", "father($X, $Z), parent($Z, $Y)"),
         new Rule("get_song($X)", "song($Y), $Y = $X")
      );

      System.out.print("Test Make: ");

      try {
         // Define goal and root of search space.
         Complex goal = new Complex("relative($X, Marcus)");
         String[] expected = {
            "relative(George, Marcus)",
            "relative(Frank, Marcus)",
            "relative(Maria, Marcus)"
         };
         Solutions.verifyAll(goal, kb, expected, 0);
      } catch (TimeOverrunException tox) { }

      System.out.print("Test Make2: ");

      try {
         // Define goal and root of search space.
         Complex goal = new Complex("get_song($X)");
         String[] expected = { "Cache ta joie", "Fade to Grey" };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) { }
   }

}  // TestMake
