/**
 * Query
 *
 * Reads in facts and rules from a text file, and allows the user
 * to query the knowledge base.
 *
 * First compile this program (javac).
 * Run the program with a source file (test/kings.txt)
 * Type in a goal: father($F, $C).
 * Type <Enter> until all solutions are exhausted.
 *
     > javac Query.java
     > java Query test/kings.txt
     ?- father($F, $C).
     $F = Godwin, $C = Harold II
     $F = Godwin, $C = Tostig
     ...
     No
     ?-
     >
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;

import java.io.*;
import java.util.*;

public class Query {

   private static String previous;

   private static final String usage = "Inferencilo - 2023\n" +
                                       "Usage:\njava Query filename.inf";

   /**
    * main
    *
    * @param  arguments
    */
   public static void main(String[] args) {

      // Maximum execution time in milliseconds.
      Global.maxTime = 300;

      // Check for a filename.
      if (args.length < 1 || args[0].length() < 1) {
         System.out.println(usage);
         return;
      }
      String filename = args[0];

      // Get knowledge.
      KnowledgeBase kb = new KnowledgeBase();
      try {
         List<String> rules = ReadRules.fromFile(filename);
         if (rules == null) return;
         kb.addRules(rules);
      } catch (UnmatchedBacktickException|
               UnmatchedParenthesesException|
               UnmatchedBracketsException x) {
         System.out.println(x.getMessage());
         return;
      }

      Console cons = System.console();
      if (cons == null) {
         System.err.println("No console.");
         System.exit(1);
      }

      while (true) {

         System.out.print("?- ");

         String input = cons.readLine();
         int len = input.length();
         if (len == 0) break;

         if (previous == null) {  // First time.
            if (input.equals(".")) break;
            previous = input;
         }

         if (input.equals(".")) { input = previous; }
         else {
            // First, validate the input.
            String err = Complex.validate(input);
            if (err.length() > 0) {  // If the input is invalid.
               System.out.println(err);
               continue;
            }
            previous = input;
         }

         try {

            // Define query and root of search space.
            // Please note: queries must be created with Make.query().
            Complex query = Make.query(input);
            SolutionNode root = query.getSolver(kb, new SubstitutionSet(), null);
            while (true) {
               SubstitutionSet solution = root.nextSolution();
               if (solution == null) {
                  System.out.println("No"); break;
               }
               String s = Solutions.toString(query, solution);
               System.out.print(s);
               input = cons.readLine();
               if (input.equals(".")) { input = previous; }
               else if (input.length() > 0) previous = input;
            } // while
         } catch (TimeOverrunException tox) {
            System.out.println("Time overrun.");
         }

      } // while loop

   } // main

}  // Query
