package HW3Package;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Driver {

	public static void main(String[] args) {
		new Driver().runCode();
	}
	
	//Declaring and initializes variables
	JobGenerator jG = new JobGenerator();
	Process[] jobList; 
	static int total_time = 0;
	static int time_slice = 10;
	static int queue_reset = time_slice * 100; 
	static int allotment = 3;
	
	// New list to track job execution
	static List<Process> executionOrder = new ArrayList<>();
	static List<Process> completedJobs = new ArrayList<>();

	
	public void runCode() {
		//Generates the jobs with random attributes within specified ranges.
		jobList = jG.generateProcesses(27, 50, 80, 0, 50, 10, 100);
		
		//Prints the details of each generated process.
		for (int index=0; index<jobList.length; index++) {
			jobList[index].printProcess();
		}
		//indicates the amount of Queue levels
        int level_amount = 3;
        
        //Creates a placeholder Process variable
        Process process;

        //list to hold queues for each level
        List<Queue> Queues = new ArrayList<>();

        //Initializes and adds each queue to the list of queues.
        for (int i = 1; i <= level_amount; i++) {
            Queues.add(new Queue(i));
        }
        
        //Scheduling loop
        while (!isEmpty(Queues, jobList)) {
        	addJobs(jobList,Queues);	//Adds jobs to the top priority queue
        	for (Queue queue : Queues) {
        		//Iterate through the queues to find and run a job.
                if (!queue.queue.isEmpty()) {
                	process = queue.queue.peek();
                    queue.runProcess(process, Queues);
                    break; // Exit the loop once a process is run
                }
            }
        }
        
        // Print execution summary after all jobs are completed
        printExecutionSummary();
	}
	
	// New method to print execution summary
	private void printExecutionSummary() {
		
		// Sort completed jobs by completion time
		completedJobs.sort(Comparator.comparingInt(p -> p.getTT()));
		
		System.out.println("\nOrder of Job Completion:");
		for (Process p : completedJobs) {
			System.out.printf("Process %c - Return Time: %d, Turn Around Time: %d%n", 
				p.getID(), p.getRT(), p.getTT());
		}
	}
	
	//Adds jobs to the top priority queue when their arrival time matches the current time.
	public void addJobs(Process[] process, List<Queue> queues) {
		for (int index=0; index<process.length; index++) {
			if(process[index] != null && process[index].getArrivalTime() == Driver.total_time) {
				queues.get(0).append(process[index]);
				process[index] = null;	//Removes the process from the job list to prevent re-adding.
				
			}
		
		}
	}
	
	// Checks if all queues and the job list are empty.
	public boolean isEmpty(List<Queue> queues, Process[] jobList) {
		for (Queue queue : queues) {
			if (!queue.queue.isEmpty()) {
				return false;	//There is a non-empty queue.
				
			}
		}
		for (int index=0; index<jobList.length; index++) {
			if (jobList[index] != null) {
				return false;	//A job is still waiting.
			}
		}
		return true;	//All queues and jobs are empty.
	}
}
