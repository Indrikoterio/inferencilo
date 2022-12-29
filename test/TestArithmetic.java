/**
 * TestArithmetic
 *
 * Test built-in arithmetic functions: Add, Subtract, Multiply, Divide.
 *
 * f(x, y) = ((x + y) - 6) * 3.4 / 3.4
 *
 * f(3, 7)  = 4
 * f(3, -7) = -10
 *
 * The rule is:
 *
 * calculate($X, $Y, $Out) :- $A = add($X, $Y), $B = subtract($A, 6),
 *                            $C = multiply($B, 3.4), $Out = divide($C, 3.4).
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

public class TestArithmetic {

   public static void main(String[] args) {

      LogicVar X = new LogicVar("$X");
      LogicVar Y = new LogicVar("$Y");
      LogicVar A = new LogicVar("$A");
      LogicVar B = new LogicVar("$B");
      LogicVar C = new LogicVar("$C");
      LogicVar Out = new LogicVar("$Out");

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex("calculate($X, $Y, $Out)"),
            new And(
                   new Unify(A, new Add(X, Y)),
                   //new Print(A), new NewLine(),
                   new Unify(B, new Subtract(A, new Constant("6.0"))),
                   //new Print(B), new NewLine(),
                   new Unify(C, new Multiply(B, new Constant("3.4"))),
                   //new Print(C), new NewLine(),
                   new Unify(Out, new Divide(C, new Constant("3.4")))
                   //new Print(Out), new NewLine()
                )
         ),
         new Rule(new Complex("test($Out)"),
            new Or(
                   new Complex("calculate(3.0, 7.0, $Out)"),
                   new Complex("calculate(3.0, -7.0, $Out)")
                )
         )
      );

      System.out.print("Test Arithmetic: ");

      try {
         Complex query = Make.query("test($X)");
         String[] expected = { "4.0", "-10.0" };
         Solutions.verifyAll(query, kb, expected, 1);
      } catch (TimeOverrunException tox) {}
   }

} // TestArithmetic
