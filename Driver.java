package mutiLevelFeedback;

import java.util.ArrayList;
import java.util.List;

public class Driver {

	public static void main(String[] args) {
		new Driver().runCode();
	}
	
	JobGenerator jG = new JobGenerator();
	Process[] jobList; 
	static int total_time = 0;
	static int time_slice = 10;
	static int queue_reset = time_slice * 100; 
	static int allotment = 3;

	
	public void runCode() {
		jobList = jG.generateProcesses(27, 50, 80, 0, 50, 10, 100);
		
		
		for (int index=0; index<jobList.length; index++) {
			jobList[index].printProcess();
		}
		//indicates the amount of Queue levels
        int level_amount = 3;
        
        //Creates a placeholder Process variable
        Process process;

        //list to hold queues for each level
        List<Queue> Queues = new ArrayList<>();

     
        for (int i = 1; i <= level_amount; i++) {
            Queues.add(new Queue(i));
        }
        
        while (!isEmpty(Queues, jobList)) {
        	addJobs(jobList,Queues);
        	for (Queue queue : Queues) {
                if (!queue.queue.isEmpty()) {
                	process = queue.queue.peek();
                    queue.runProcess(process, Queues);
                    break; // Exit the loop once a process is run
                }
            }
        	
        	
        }
        
	}
	public void addJobs(Process[] process, List<Queue> queues) {
		for (int index=0; index<process.length; index++) {
			if(process[index] != null && process[index].getArrivalTime() == Driver.total_time) {
				queues.get(0).append(process[index]);
				process[index] = null;
				
			}
		
		}
	}
	public boolean isEmpty(List<Queue> queues, Process[] jobList) {
		for (Queue queue : queues) {
			if (!queue.queue.isEmpty()) {
				return false;
				
			}
		}
		for (int index=0; index<jobList.length; index++) {
			if (jobList[index] != null) {
				return false;
			}
		}
		return true;
	}
}