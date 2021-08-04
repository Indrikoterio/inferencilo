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
import java.io.Reader;
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


   /**
    * makeListOfRules
    *
    * Divides a text file to create a list of rules. One rule per item.
    * Rules (and facts) end with a perido.
    *
    * @param  original text
    * @return rules (list of rules)
    * @throws UnmatchedParenthesesException, UnmatchedBracketsException
    */
   private static List<String> makeListOfRules(String text) {

      if (text == null) return null;

      int length = text.length();
      StringBuilder sb = new StringBuilder(100);
      List<String> rules = new ArrayList<String>();
      int roundDepth = 0;   // depth of round parenthesis (())
      int squareDepth = 0;  // depth of square brackets [[]]

      for (int i = 0; i < length; i++) {

         char c = text.charAt(i);
         sb.append((char)c);

         if (c == '.' && roundDepth == 0 && squareDepth == 0) {
            rules.add(sb.toString());
            sb.setLength(0);
         }
         else if (c == '(') roundDepth++;
         else if (c == '[') squareDepth++;
         else if (c == ')') roundDepth--;
         else if (c == ']') squareDepth--;
      }  // for

      String last = sb.toString().trim();
      if (roundDepth != 0) throw new UnmatchedParenthesesException("ReadRules");
      if (squareDepth != 0) throw new UnmatchedBracketsException("ReadRules");
      if (last.length() > 0) rules.add(last);
      return rules;

   } // makeListOfRules()


   /**
    * fromFile
    *
    * Opens a file and reads in Prolog-like rules.
    * Returns the rules as a list of strings.
    *
    * @param  name of file
    * @return rules (list of rules)
    * @throws UnmatchedParenthesesException, UnmatchedBracketsException
    */
   public static List<String> fromFile(String filename) {

      BufferedReader reader = openFile(filename);
      if (reader == null) return null;
      String text = readStripComments(reader);
      if (text == null) return null;
      return makeListOfRules(text);

   } // fromFile()


  /**
   * fromJar
   *
   * Reads a prolog-like program from a Java jar-archive.
   * Returns the rules in a list of strings.
   *
   * @param   parent class
   * @param   name of file
   * @return  list of rules
   * @throws  UnmatchedParenthesesException, UnmatchedBracketsException
   */
   public static List<String> fromJar(Class klass, String filename) {

      if (klass == null) return null;
      if (filename == null) return null;
      String text;

      try {
         Reader in = new InputStreamReader(klass.getResourceAsStream(filename), "UTF-8");
         BufferedReader reader = new BufferedReader(in);
         if (reader == null) return null;
         text = readStripComments(reader);
         if (text == null) return null;
      }
      catch (IOException iox) {
         System.err.println("ReadRules, fromJar() - iox: " + iox.toString());
         return null;
      }
      return makeListOfRules(text);

   }  // fromJar()


   /*
    * openFile
    *
    * Opens a file for reading, in UTF-8 format.
    * Returns a buffered reader, or null if an
    * exception occurs.
    *
    * @param  file name
    * @return BufferedReader
    */
   private static BufferedReader openFile(String filename) {
      try {
         return new BufferedReader(
                   new InputStreamReader(
                      new FileInputStream(filename),"UTF-8"));
      }
      catch (Exception e) {
         System.err.println("ReadRules - Cannot open " + filename + ".");
         return null;
      }
   } // openFile


   /*
    * trimComments
    *
    * Trims comments from a line. In Inferencilo, valid comment
    * delimiters are:
    *
    *   %  Comment
    *   #  Comment
    *   // Comment
    *
    * Any text which occurs after these delimiters is considered
    * a comment, and removed from the line. But, if these
    * delimiters occur within braces, they are not treated as
    * comment delimiters. For example, in the line
    *
    *    print(Your rank is %s., $Rank),   % Print user's rank.
    *
    * the first percent sign does not start a comment, but the
    * second does.
    *
    * @param  original line
    * @return trimmed line
    */
   private static String trimComments(String line) {
      char c;
      char previous = 'x';
      int index = -1;
      int length = line.length();
      int roundDepth = 0;   // depth of round parenthesis (())
      int squareDepth = 0;  // depth of square brackets [[]]
      for(int i = 0; i < length ; i++) {
         c = line.charAt(i);
         if (c == '(') roundDepth++;
         else if (c == '[') squareDepth++;
         else if (c == ')') roundDepth--;
         else if (c == ']') squareDepth--;
         else if (roundDepth == 0 && squareDepth == 0) {
            if (c == '#' || c == '%') { index = i; break; }
            if (c == '/' && previous == '/') { index = i - 1; break;}
         }
         previous = c;
      }
      if (index >= 0) {
         return line.substring(0, index);
      }
      else return line;
   } // trimComments


   /*
    * readStripComments
    *
    * Reads program text from a buffered reader, line by line.
    * Comments are removed from each line, and the lines
    * are joined to produce a long string. The method returns
    * the stripped program text, or null if there is a failure.
    *
    * @param  buffered reader
    * @return stripped program text
    */
   private static String readStripComments(BufferedReader reader) {

      StringBuilder sb = new StringBuilder(80);

      try {
         String line = reader.readLine();
         while (true) {
            if (line == null) break;
            line = trimComments(line).trim();
            if (line.length() > 0) {
               sb.append(line + " ");
            }
            line = reader.readLine();
         } // while
      }
      catch (IOException e) {
         System.err.println("readStripComments: io error:\n" + e);
         return null;
      }
      return sb.toString();

   } // readStripComments


} // ReadRules
