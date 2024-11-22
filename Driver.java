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
        // Generate processes with more realistic parameters
        jobList = jG.generateProcesses(20, 20, 100, 0, 100, 5, 15);
        
        // Run MLFQ Simulation
        simulateMultilevelFeedbackQueue(jobList);
    }

    private void simulateMultilevelFeedbackQueue(Process[] processes) {
        int currentTime = 0;
        int resetCounter = 0;
        int finishOrder = 1;

        // Completion tracking
        ArrayList<Character> completionOrder = new ArrayList<>();

        // Three priority queues with different time slices
        Queue<Process>[] queues = new Queue[3];
        int[] timeSlices = {10, 20, 30};  // Increasing time slices for lower priority
        
        for (int i = 0; i < 3; i++) {
            queues[i] = new LinkedList<>();
        }

        // Sort processes by arrival time
        Arrays.sort(processes, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int index = 0;
        System.out.println("\nExecution Order:");

        while (index < processes.length || !isAllQueuesEmpty(queues)) {
            // Add arriving processes to the highest priority queue
            while (index < processes.length && processes[index].getArrivalTime() <= currentTime) {
                queues[0].add(processes[index]);
                index++;
            }

            // Periodic queue reset
            if (resetCounter >= Process.getResetTime()) {
                System.out.println("\n--- Resetting All Queues to Highest Priority ---");
                resetCounter = 0;
                resetAllQueues(queues);
            }

            // Select process from highest non-empty queue
            Process currentProcess = selectNextProcess(queues);

            if (currentProcess != null) {
                int queueLevel = findQueueLevel(queues, currentProcess);
                int timeSlice = timeSlices[queueLevel];

                // Execute the process
                if (currentProcess.getStartTime() == -1) {
                    currentProcess.startProcess(currentTime);
                }

                int executionTime = Math.min(timeSlice, currentProcess.getRemainingTime());
                currentProcess.executeProcess(executionTime);
                currentTime += executionTime;
                resetCounter += executionTime;

                System.out.printf("Process %c (Queue %d) executed for %d time units. Remaining time: %d\n", 
                    currentProcess.jobID(), queueLevel + 1, executionTime, currentProcess.getRemainingTime());

                // Process completion handling
                if (currentProcess.isCompleted()) {
                    currentProcess.completeProcess(currentTime);
                    currentProcess.setTurnAroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setFinishOrder(finishOrder++);
                    completionOrder.add(currentProcess.jobID());
                    
                    System.out.printf("Process %c COMPLETED at time %d\n", currentProcess.jobID(), currentTime);
                } else {
                    // Process demotion logic
                    demoteProcess(queues, currentProcess, queueLevel);
                }
            } else {
                // No process available, increment time
                currentTime++;
                resetCounter++;
            }
        }

        // Print results
        printResults(processes, completionOrder);
    }

    private boolean isAllQueuesEmpty(Queue<Process>[] queues) {
        for (Queue<Process> queue : queues) {
            if (!queue.isEmpty()) return false;
        }
        return true;
    }

    private Process selectNextProcess(Queue<Process>[] queues) {
        for (Queue<Process> queue : queues) {
            if (!queue.isEmpty()) {
                return queue.poll();
            }
        }
        return null;
    }

    private int findQueueLevel(Queue<Process>[] queues, Process process) {
        for (int i = 0; i < queues.length; i++) {
            if (queues[i].contains(process)) {
                return i;
            }
        }
        return 0;
    }

    private void demoteProcess(Queue<Process>[] queues, Process process, int currentQueueLevel) {
        process.incAllotmentCount();
        
        if (process.getAllotmentCount() >= process.getAllotMax()) {
            process.resetAllotmentCount();
            
            if (currentQueueLevel < queues.length - 1) {
                // Remove from current queue
                queues[currentQueueLevel].remove(process);
                
                // Move to next lower priority queue
                queues[currentQueueLevel + 1].add(process);
                process.setQueueNum(currentQueueLevel + 2);
            } else {
                // Lowest queue, readd to same queue
                queues[currentQueueLevel].add(process);
            }
        } else {
            queues[currentQueueLevel].add(process);
        }
    }

    private void resetAllQueues(Queue<Process>[] queues) {
        for (int i = 1; i < queues.length; i++) {
            while (!queues[i].isEmpty()) {
                Process p = queues[i].poll();
                p.setQueueNum(1);  // Reset to highest priority
                p.resetAllotmentCount();
                queues[0].add(p);
            }
        }
    }

    private void printResults(Process[] processes, ArrayList<Character> completionOrder) {
        System.out.println("\nCompletion Order: " + 
            completionOrder.toString().replace("[", "").replace("]", ""));

        System.out.println("\nFinal Process Details:");
        for (Process process : processes) {
            System.out.printf("Process %c - Turn Around Time: %d, Return Time: %d\n", 
                process.jobID(), process.getTurnAroundTime(), process.getReturnTime());
        }
    }
}
