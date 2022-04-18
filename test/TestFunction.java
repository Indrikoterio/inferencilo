/**
 * TestFunction
 *
 * Inferencilo allows programmers to easily implement their own
 * predicates and functions, by subclassing BuiltInPredicate and
 * PFunction, respectively.
 *
 * This program tests Capitalize, which extends PFunction.
 *
 * Useful reference: https://swish.swi-prolog.org/
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestFunction {

   public static void main(String[] args) {

      Variable Y      = Variable.inst("$Y");
      Variable Word   = Variable.inst("$Word");

      Constant fix_cap = Constant.inst("fix_cap");
      Constant tokyo   = Constant.inst("tokyo");

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("fix_cap($X, $Y)"),
            new Unify(Y, new Capitalize("$X"))
         )
      );

      Complex goal, result;

      try {
         System.out.print("Test PFunction: ");
         goal = Make.goal(fix_cap, tokyo, Word);
         String[] expected = { "Tokyo" };
         Solutions.verifyAll(goal, kb, expected, 2);
      }
      catch (TimeOverrunException tox) { }

   }
} // TestFunction
