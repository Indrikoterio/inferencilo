/**
 * TestMemoryLimit
 *
 * Tests the memory limit exception.
 *
 * When there are too many variables, the substitution sets become
 * excessively large. This may be caused by an endless loop.
 *
 * A 'Memory Limit' exception is thrown to stop the search for a solution.
 *
 * @author  Cleve (Klivo) Lendon
 * @version 1.0
 */

import inferencilo.*;
import java.util.HashMap;

public class TestMemoryLimit {

   public static void main(String[] args) {

      int limit = Global.maxVariables;
      LogicVar v = new LogicVar("$X");

      // Get a high variable ID.
      for (int i = 0; i < limit; i++) { v.nextId(); }

      try {
         // standardizeVariablesApart should throw an exception.
         v = (LogicVar)v.standardizeVariablesApart(new HashMap<String, LogicVar>());
         System.err.println("TestMemoryLimit - Failed to throw exception.");
      } catch (MemoryLimitException mlx) {
         System.out.println("TestMemoryLimit ✓");
      }
   }
}  // TestMemoryLimit
