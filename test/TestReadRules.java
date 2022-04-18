/**
 * TestReadRules
 *
 * Read in the rules from a text file (kings.txt).
 * Who is Skule's grandfather?
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import java.util.*;
import inferencilo.*;

public class TestReadRules {

   public static void main(String[] args) {

      KnowledgeBase kb = new KnowledgeBase();
      List<String> reguloj = ReadRules.fromFile("kings.txt");
      kb.addRules(reguloj);
      //kb.showKB();

      System.out.print("Test ReadRules: ");

      try {
         Complex goal = Make.goal("grandfather($X, Skule)");
         String[] expected = { "Godwin" };
         Solutions.verifyAll(goal, kb, expected, 1);
      } catch (TimeOverrunException tox) {}

   }

} // TestReadRules
