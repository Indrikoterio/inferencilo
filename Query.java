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

   private static final String usage = "Inferencilo - 2021\n" +
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
      List<String> rules = ReadRules.fromFile(filename);
      if (rules == null) return;

      kb.addRules(rules);

      try {

         // Get goal from command line.
         BufferedReader inReader =
                 new BufferedReader(
                     new InputStreamReader(System.in, "UTF-8"));

         while (true) {

            System.out.print("?- ");

            String input = inReader.readLine();
            if (input.length() == 0) break;
            int len = input.length();
            char c = input.charAt(len - 1);
            if (c == '.') input = input.substring(0,len - 1);

            try {
               // Define goal and root of search space.
               String strGoal = input;
               Complex goal = new Complex(strGoal);
               SolutionNode root = goal.getSolver(kb, new SubstitutionSet(), null);
               while (true) {
                  SubstitutionSet solution = root.nextSolution();
                  if (solution == null) {
                     System.out.println("No"); break;
                  }
                  String s = Solutions.toString(goal, solution);
                  System.out.print(s);
                  input = inReader.readLine();
               } // while
            } catch (TimeOverrunException tox) {
               System.out.println("Time overrun.");
            }

         } // while loop

      } catch (IOException iox) {
         System.out.println("I/O error\n" + iox.toString());
      }

   } // main

}  // Query