/**
 * TestErrorCheck
 *
 * Tests the CheckError predicate.
 *
 * @author  Klivo
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestErrorCheck {

   public static void main(String[] args) {   // Set up the knowledge base.

      LogicVar x   = new LogicVar("$X");
      LogicVar y   = new LogicVar("$Y");
      LogicVar z   = new LogicVar("$Z");

      LogicVar inErr  = new LogicVar("$InErr");
      LogicVar outErr = new LogicVar("$OutErr");

      Constant test = new Constant("test");
      Constant error1 = new Constant("Error 1");
      Constant error2 = new Constant("Error 2");
      Constant error3 = new Constant("Error 3");

      /*
          First, set Global.maxError to 2. Should get two results.

          test(InErrors, OutErrors) :- check_errors(InError, Error1, OutErrors).

          test(InErrors, OutErrors) :- check_errors(InError, Error1, X),
                                       check_errors(X, Error2, OutError).

          test(InErrors, OutErrors) :- check_errors(InError, Error1, X),
                                       check_errors(X, Error2, Y),
                                       check_errors(Y, Error3, OutError).
       */

      KnowledgeBase kb = new KnowledgeBase(
         new Rule(new Complex(test, inErr, outErr),
                              new And(
                                  new CheckError(inErr, error1, outErr)
                              )
                 ),
         new Rule(new Complex(test, inErr, outErr),
                              new And(
                                  new CheckError(inErr, error1, x),
                                  new CheckError(x, error2, outErr)
                              )
                 ),
         new Rule(new Complex(test, inErr, outErr),
                              new And(
                                  new CheckError(inErr, error1, x),
                                  new CheckError(x, error2, y),
                                  new CheckError(y, error3, outErr)
                              )
                 )
      );

      try {

         Global.maxErrors = 2;
         System.out.print("Test CheckError: ");
         Complex query = Make.query(test, SLinkedList.empty, x);
         String[] expected = {"[Error 1]", "[Error 2, Error 1]"};
         Solutions.verifyAll(query, kb, expected, 2);

         /*
         SolutionNode root = query.getSolver(kb, new SubstitutionSet(), null);
         SubstitutionSet solution = root.nextSolution();
         Complex result;
         int count = 0;
         while (solution != null) {
            result = (Complex)query.replaceVariables(solution);
            System.out.println(result.getTerm(2));
            solution = root.nextSolution();
            count++; if (count > 10) break;  // for safety
         }
         */

      } catch (TimeOverrunException tox) { }
   }
}


