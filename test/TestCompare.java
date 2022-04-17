/**
 * TestCompare
 *
 * Test built-in predicates which compare two arguments,
 * such as greater_than() or less_than().
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestCompare {

   public static void main(String[] args) {

      Variable X = Variable.inst("$X");
      Variable Y = Variable.inst("$Y");
      Variable Z = Variable.inst("$Z");

      Constant passed = new Constant("passed");
      Constant failed = new Constant("failed");

      KnowledgeBase kb = new KnowledgeBase(

         new Rule(new Complex("test_greater_than($X, $Y, $Z)"),
            new And( new GreaterThan(X, Y),
                     new Cut(),
                     new Unify(Z, passed)
                   )
         ),
         new Rule(new Complex("test_greater_than($_, $_, $Z)"),
            new Unify(Z, failed)
         ),
         new Rule(new Complex("test_less_than($X, $Y, $Z)"),
            new And( new LessThan(X, Y),
                     new Cut(),
                     new Unify(Z, passed)
                   )
         ),
         new Rule(new Complex("test_less_than($_, $_, $Z)"),
            new Unify(Z, failed)
         ),
         new Rule(new Complex("test_greater_than_or_equal($X, $Y, $Z)"),
            new And( new GreaterThanOrEqual(X, Y),
                     new Cut(),
                     new Unify(Z, passed)
                   )
         ),
         new Rule(new Complex("test_greater_than_or_equal($_, $_, $Z)"),
            new Unify(Z, failed)
         ),
         new Rule(new Complex("test_less_than_or_equal($X, $Y, $Z)"),
            new And( new LessThanOrEqual(X, Y),
                     new Cut(),
                     new Unify(Z, passed)
                   )
         ),
         new Rule(new Complex("test_less_than_or_equal($_, $_, $Z)"),
            new Unify(Z, failed)
         ),
         new Rule(new Complex("test_equal($X, $Y, $Z)"),
            new And( new Equal(X, Y),
                     new Cut(),
                     new Unify(Z, passed)
                   )
         ),
         new Rule(new Complex("test_equal($_, $_, $Z)"),
            new Unify(Z, failed)
         ),

         new Rule(
            new Complex("test($Z)"),
            new Or(
               new Complex("test_greater_than(4, 3, $Z)"),
               new Complex("test_greater_than(Beth, Albert, $Z)"),
               new Complex("test_greater_than(2, 3, $Z)"),
               new Complex("test_less_than(1.6, 7.2, $Z)"),
               new Complex("test_less_than(Samantha, Trevor, $Z)"),
               new Complex("test_less_than(4.222, 4., $Z)"),
               new Complex("test_greater_than_or_equal(4, 4.0, $Z)"),
               new Complex("test_greater_than_or_equal(Joseph, Joseph, $Z)"),
               new Complex("test_greater_than_or_equal(3.9, 4.0, $Z)"),
               new Complex("test_less_than_or_equal(7.000, 7, $Z)"),
               new Complex("test_less_than_or_equal(7.000, 7.1, $Z)"),
               new Complex("test_less_than_or_equal(0.0, -20, $Z)"),
               new Complex("test_equal(37.4, 37.4, $Z)"),
               new Complex("test_equal(37.4, 37.0, $Z)")
            )
         )
      );

      Complex goal, result;
      SubstitutionSet solution;
      SolutionNode root;

      System.out.print("Test Compare: ");

      try {
         goal = new Complex("test($W)");
         String[] expected = { "passed", "passed", "failed",
                               "passed", "passed", "failed",
                               "passed", "passed", "failed",
                               "passed", "passed", "failed",
                               "passed", "failed"
                                };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) {}
   }

} // TestCompare
