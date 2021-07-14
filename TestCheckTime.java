/**
 * TestCheckTime
 *
 * Testing the CheckTime predicate, using an infinite loop.
 *
 * fact(a,b).
 * fact(b,c).
 * doit(X,Z) :- fact(X,Z).
 * doit(X,Z) :- print("."), doit(X,Y), fact(Y,Z), check_time.
 *
 * Note: It is necessary to set startTime before searching
 * for a solution.
 *
 * The global variable maxTime is 300 milliseconds by default,
 * but here it is set to 10 milliseconds.
 *
 * There is a second test to check parsing of 'check_time'.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestCheckTime {

   public static void main(String[] args) {   // Set up the knowledge base.

      Constant a = new Constant("a");
      Constant b = new Constant("b");
      Constant c = new Constant("c");

      Variable X = Variable.inst("$X");
      Variable Y = Variable.inst("$Y");
      Variable Z = Variable.inst("$Z");

      Constant fact = new Constant("fact");
      Constant doit = new Constant("doit");

      KnowledgeBase kb = new KnowledgeBase(

         new Rule(new Complex(fact, a, b)),
         new Rule(new Complex(fact, b, c)),

         new Rule(
            new Complex(doit, X, Z),
            new And( new Complex(fact, X, Z) )
         ),

         new Rule(
            new Complex(doit, X, Z),
            new And(
               new Print("."),
               new Complex(doit, X, Y),
               new Complex(fact, Y, Z),
               new CheckTime()
            )
         )

      );

      KnowledgeBase kb2 = new KnowledgeBase(
         new Rule(new Complex(fact, a, b)),
         new Rule(new Complex(fact, b, c)),
         new Rule(
            new Complex(doit, X, Z),
            new And( new Complex(fact, X, Z) )
         ),
         new Rule("doit($X, $Z) :- print(.), doit($X, $Y), fact($Y, $Z), check_time.")
      ); // kb2


      System.out.print("Test CheckTime\n");

      Global.maxTime = 10;  // milliseconds
      Global.startTime = System.nanoTime();

      try {
         Complex goal = new Complex("doit($_, Y)");
         String[] expected = { "something" };
         Solutions.verifyAll(goal, kb, expected, 2);
      } catch (TimeOverrunException tox) {
         System.err.println("Time out. ✓");
      }

      Global.startTime = System.nanoTime();
      try {
         Complex goal = new Complex("doit($_, Y)");
         String[] expected = { "something" };
         Solutions.verifyAll(goal, kb, expected, 2);
      } catch (TimeOverrunException tox) {
         System.err.println("Time out 2. ✓");
      }
   }

} // TestCheckTime


