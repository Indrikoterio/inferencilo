/**
 * ReadRules
 *
 * This class has static methods which read Prolog-like rules from a file.
 *
 * @author Cleve (Klivo) Lendon
 * @version 1.0
 */

package inferencilo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ReadRules {

   /**
    * constructor
    */
   public ReadRules() {}


   /*
    * checkBrackets
    *
    * This method checks to see if brackets are balanced.
    * It throws a FatalParsingException if they are not.
    *
    * @param  round bracket count
    * @param  square bracket count
    * @throws FatalParsingException
    */
   private static String error = "Unmatched bracket: ";
   private static void checkBrackets(int round, int square) {
      if (round != 0 || square != 0) {
         if (round > 0) error += "(";
         else if (round < 0) error += ")";
         if (square > 0) error += "[";
         else if (square < 0) error += "]";
         throw new FatalParsingException(error);
      }
   } // checkBrackets


   /*
    * openFile
    *
    * Opens a file for reading, UTF-8 format.
    *
    * @param  name of file
    * @return InputStreamReader
    */
   private static InputStreamReader openFile(String name) {
      try {
         return new InputStreamReader(new FileInputStream(name),"UTF-8");
      }
      catch (Exception e) {
         System.out.println("ReadRule - Cannot open file: " + name);
         return null;
      }
   } // openFile


   /**
    * fromFile
    *
    * Opens a file and reads in Prolog-like rules.
    * Returns the rules as a list of strings.
    *
    * @param  name of file
    * @return rules (list of rules)
    */
   public static List<String> fromFile(String filename) {

      InputStreamReader reader = openFile(filename);
      if (reader == null) return null;

      StringBuilder sb = new StringBuilder(80);
      List<String> rules = new ArrayList<String>();

      int roundDepth = 0;   // depth of round parenthesis (())
      int squareDepth = 0;  // depth of square brackets [[]]

      try {
         int ch = reader.read();
         while (ch != -1) {

            // Skip past comments, marked by #
            if (ch == '#') {
               int ch2 = reader.read();
               while (ch2 != -1) {
                  if (ch2 == '\n') break;
                  ch2 = reader.read();
               }
               if (ch2 == -1) {
                  rules.add(sb.toString());
                  checkBrackets(roundDepth, squareDepth);
                  return rules;
               }
               ch = reader.read();
               continue;
            }

            if (ch == '\n') ch = ' ';

            sb.append((char)ch);

            if (ch == '.' && roundDepth == 0 && squareDepth == 0) {
               rules.add(sb.toString());
               sb.setLength(0);
            }
            else if (ch == '(') roundDepth++;
            else if (ch == '[') squareDepth++;
            else if (ch == ')') roundDepth--;
            else if (ch == ']') squareDepth--;

            ch = reader.read();
         }  // end of while

         checkBrackets(roundDepth, squareDepth);
         return rules;
      }
      catch (IOException iox) {
         return null;
      }

   } // fromFile()

}  // ReadRules
