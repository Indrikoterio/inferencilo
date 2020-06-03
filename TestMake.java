/**
 * TestMake
 *
 * This test is similar to TestAndOr. The difference is that instead of
 * using class constructors to define a logical operand, the factory methods
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
         new Rule(new Complex("song(Cache ta joie)")),
         new Rule(new Complex("song(Fade to Grey)")),
         new Rule(new Complex("father(George, Frank)")),
         new Rule(new Complex("father(George, Sam)")),
         new Rule(new Complex("mother(Gina, Frank)")),
         new Rule(new Complex("mother(Gina, Sam)")),
         new Rule(new Complex("mother(Maria, Marcus)")),
         new Rule(new Complex("father(Frank, Marcus)")),
         new Rule(
            new Complex("parent($X, $Y)"),
            Make.or("father($X, $Y); mother($X, $Y)")
            //new Or(
            //   new Complex("father($X, $Y)"),
            //   new Complex("mother($X, $Y)")
            //)
         ),
         new Rule(
            new Complex("relative($X, $Y)"),
            Make.or("grandfather($X, $Y); father($X, $Y); mother($X, $Y)")
            //new Or(
            //   new Complex("grandfather($X, $Y)"),
            //   new Complex("father($X, $Y)"),
            //   new Complex("mother($X, $Y)")
            //)
         ),
         new Rule(
            new Complex("grandfather($X, $Y)"),
            Make.and("father($X, $Z), parent($Z, $Y)")
            //new And(new Complex("father($X, $Z)"), new Complex("parent($Z, $Y)"))
         ),
         new Rule(
            new Complex("get_song($X)"),
            Make.and("song($Y), $Y = $X")
            //new And(new Complex("song($Y)"), new Unify("$Y = $X"))
         )
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
