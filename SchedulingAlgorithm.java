// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector processVector, Results result) {
    int comptime = 0;
    int size = processVector.size();
    int completed = 0;
    int quantum = 2; // Define the time quantum for round-robin scheduling
    int[] remainingTime = new int[size]; // Array to store remaining time for each process

    // Initialize remaining time for each process
    for (int i = 0; i < size; i++) {
      sProcess process = (sProcess) processVector.elementAt(i);
      remainingTime[i] = process.cputime;
    }

    String resultsFile = "Summary-Processes";

    result.schedulingType = "Interactive (Preemptive)";
    result.schedulingName = "Round-Robin";

    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

      while (completed < size) {
        for (int i = 0; i < size; i++) {
          sProcess process = (sProcess) processVector.elementAt(i);

          int executeTime = Math.min(quantum, remainingTime[i]);
          remainingTime[i] -= executeTime;

          if (remainingTime[i] == 0) {
            completed++;
            process.cpudone += executeTime;
            out.println("Process: " + i + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
          } else {
            process.cpudone += executeTime;
            out.println("Process: " + i + " running... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
          }

          if (process.ioblocking == process.ionext) {
            out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
            process.numblocked++;
          }
        }
        comptime += quantum;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = comptime;
    return result;
  }
}
