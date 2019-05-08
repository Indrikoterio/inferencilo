/**
 * TimeOverrunException
 *
 * Throw when execution time exceeds Global.maxTime .
 * 300 milliseconds is reasonable.
 *
 * @version 1.0
 * @author Klivo
 */

package inferencilo;

public class TimeOverrunException extends Exception {

   private long time;

   /**
    * constructor
    *
    * @param time in milliseconds
    */
   public TimeOverrunException(long time) {
      this.time = time;
      // The application will create a suitable error message.
   }

   /**
    * time
    *
    * @return  execution time
    */
   public long time() { return time; }

}  // TimeOverrunException
