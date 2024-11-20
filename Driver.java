package HW3Package;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Driver {
    public static void main(String[] args) {
        new Driver().runCode();
    }

    JobGenerator jG = new JobGenerator();
    Process[] jobList;

    public void runCode() {
        // Generate processes
        jobList = jG.generateProcesses(27, 50, 80, 0, 50, 10, 100);
        
        // Run MLFQ Simulation
        simulateMultilevelFeedbackQueue(jobList);
    }

    private void simulateMultilevelFeedbackQueue(Process[] processes) {
        int currentTime = 0;
        int resetCounter = 0;
        int finishOrder = 1;

        // List to track completion order
        ArrayList<Character> completionOrder = new ArrayList<>();

        Queue<Process>[] queues = new Queue[3];
        for (int i = 0; i < 3; i++) {
            queues[i] = new LinkedList<>();
        }

        // Sort processes by arrival time manually
        Arrays.sort(processes, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int index = 0;
        // Execution order tracking
        System.out.println("\nExecution Order:");

        while (index < processes.length || !queues[0].isEmpty() || !queues[1].isEmpty() || !queues[2].isEmpty()) {
            // Add arriving processes to the highest priority queue
            while (index < processes.length && processes[index].getArrivalTime() <= currentTime) {
                queues[0].add(processes[index]);
                index++;
            }

            // Reset queues periodically
            if (resetCounter >= Process.getResetTime()) {
                System.out.println("\n--- Resetting All Queues to Highest Priority ---");
                resetCounter = 0;
                for (int i = 1; i < 3; i++) {
                    while (!queues[i].isEmpty()) {
                        Process p = queues[i].poll();
                        p.setQueueNum(1);  // Reset to highest priority
                        p.resetAllotmentCount();  // Reset allotment count
                        queues[0].add(p);
                    }
                }
            }

            // Find the first non-empty queue
            Process currentProcess = null;
            int currentQueue = -1;
            for (int i = 0; i < 3; i++) {
                if (!queues[i].isEmpty()) {
                    currentProcess = queues[i].poll();
                    currentQueue = i;
                    break;
                }
            }

            if (currentProcess != null) {
                int timeSlice = currentProcess.getTimeSlice();
                int remainingTime = currentProcess.getRemainingTime();

                // If it's the first execution, set return time
                if (currentProcess.getReturnTime() == 0) {
                    currentProcess.setReturnTime(currentTime - currentProcess.getArrivalTime());
                }

                // Execute the process for up to the time slice
                int executionTime = Math.min(timeSlice, remainingTime);
                currentProcess.executeProcess(executionTime);
                currentTime += executionTime;
                resetCounter += executionTime;

                // Print execution details
                System.out.printf("Process %c (Queue %d) executed for %d time units. Remaining time: %d\n", 
                    currentProcess.jobID(), currentQueue + 1, executionTime, currentProcess.getRemainingTime());

                // If the process is completed
                if (currentProcess.isCompleted()) {
                    currentProcess.setTurnAroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setFinishOrder(finishOrder++);
                    
                    // Add to completion order
                    completionOrder.add(currentProcess.jobID());
                    
                    System.out.printf("Process %c COMPLETED at time %d\n", currentProcess.jobID(), currentTime);
                } else {
                    // Increment allotment count and check for demotion
                    currentProcess.incAllotmentCount();
                    if (currentProcess.getAllotmentCount() >= currentProcess.getAllotMax()) {
                        currentProcess.resetAllotmentCount();
                        if (currentQueue < 2) {
                            currentProcess.setQueueNum(currentQueue + 1);
                            queues[currentQueue + 1].add(currentProcess);
                        } else {
                            queues[currentQueue].add(currentProcess);
                        }
                    } else {
                        queues[currentQueue].add(currentProcess);
                    }
                }
            } else {
                // No process available, increment time
                currentTime++;
                resetCounter++;
            }
        }

        // Print completion order
        System.out.println("\nCompletion Order: " + 
            completionOrder.toString().replace("[", "").replace("]", ""));

        // Print final results
        System.out.println("\nFinal Process Details:");
        for (Process process : processes) {
            System.out.printf("Process %c - Turn Around Time: %d, Return Time: %d\n", 
                process.jobID(), process.getTurnAroundTime(), process.getReturnTime());
        }
    }
}