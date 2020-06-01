/**
 * TestConstant
 *
 * The Constant class stores a string value; it is instantiated as: new Constant("red").
 * As a convenience, the Constant class has a concat() method, which concatenates
 * parameters (Unifiable terms). Thus, Constant.concat(one, [two, three], X) should be
 * equivalent to: new Constant("one two three four").
 *
 * @authro  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestConstant {

   public static void main(String[] args) {   // Set up the knowledge base.

      Variable x   = new Variable("$X");
      Variable y   = new Variable("$Y");
      Variable out = new Variable("$Out");

      Constant test_constant = new Constant("test_constant");
      Constant red = new Constant("red");
      Constant green = new Constant("green");
      Constant blue = new Constant("blue");
      Constant comma = new Constant(",");

      /*
          test_constant(Out) :- Out = constant(red, green, blue).
       */

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(test_constant, out), new And( new Unify(out, new Constant("red")))),
         new Rule(new Complex(test_constant, out), new And( new Unify(out, Constant.concat(null, red, green, blue)))),
         new Rule(new Complex(test_constant, out),
                                new And(new Unify(out, Constant.concat(null, red, comma, green, comma, blue))))
      );


      try {
         System.out.print("Test Constant: ");
         Complex goal = new Complex(test_constant, x);
         String[] expected = {"red", "red green blue", "red, green, blue"};
         Solutions.verifyAll(goal, kb, expected, 1);
      }
      catch (TimeOverrunException tox) { }


      /*
      System.out.print("Test Constant:\n");
      Complex goal = new Complex(test_constant, x);
      SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
      SubstitutionSet solution = root.nextSolution();
      Complex result;
      int count = 0;
      while (solution != null) {
         result = (Complex)goal.replaceVariables(solution);
         System.out.println(result.getTerm(1));
         solution = root.nextSolution();
         count++; if (count > 10) break;  // for safety
      }
      */

   }
}


