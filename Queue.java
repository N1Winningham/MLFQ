package mutiLevelFeedback;
import java.util.LinkedList;
import java.util.List;


public class Queue {
	
	public int level;
	public LinkedList<Process> queue = new LinkedList<>();
	
	public Queue(int integer) {
		level = integer;
	}
	
	public void append(Process process) {
		queue.add(process);
		
	}
	//Runs 1 Time_Slice:
	public void runProcess(Process process, List<Queue> queues) {	
		
		//Used for RT;
				if (!process.hasRun) {
					process.setRT(Driver.total_time);
					process.hasRun = true;
				}
				
		timeSlice(process);		
		//System.out.println("Time Slice for Process: " + process.getID() + " from Queue has just finished!");		
		allotment(process);
		
		//Boolean to say if process has already been removed
		boolean removal = false;
		//Kills of process when done and Calculates TT as well as prints out RT and TT
		if (process.getJobLength() == 0) {
			
			process.setTT(Driver.total_time);
			
			System.out.println("Process: " + process.getID() + " has just finished! "
					+ "Its RT was: " + process.getRT() + " and its TT was: ");
			
			queue.remove(process);
			
			removal = true;
		}
		
		//Lowers process when allotment is reached
		if (process.getAllotmentCount() == Driver.allotment && !removal) {
			System.out.println("Process: " + process.getID() + " has just reached its allotment in Level: " + getLevel() + "!");
			process.resetAllotmentCount();
			
			if (level < queues.size()) {
				queues.get(level).append(process);
			}
			queue.remove(process);
		}
		
		//Checks if boost is needed
		if (Driver.total_time >= Driver.queue_reset) {
			boostPriority(queues);
		}
		//System.out.println("Total Time after TS: " + Driver.total_time + " Level of Current Queue: " + level);
		
	}
	
	public void timeSlice(Process process) {
		
		//Checks if job length is less than the time slice to avoid negative values
		if (process.getJobLength() < 10) {
			Driver.total_time += process.getJobLength();
			process.setJobLength(0);
			
		}
		//Performs the time slice
		else {
			Driver.total_time += Driver.time_slice;
			//System.out.println("Job Length before time_slice:" + process.getJobLength());
			process.setJobLength(process.getJobLength() - Driver.time_slice);
			//System.out.println("Job Length after time_slice:" + process.getJobLength());
		}
	}
	public void allotment(Process process) {
		
		//System.out.println("Job Allotment before time_slice:" + process.getAllotmentCount());
		process.incAllotmentCount();
		//System.out.println("Job Allotment after time_slice:" + process.getAllotmentCount());
	}
	public void remove(Process process) {
		queue.remove(process);
	}
	public int getLevel() {
		return level;
	}
	
	public void boostPriority(List<Queue> queues) {
	    System.out.println("Priority boost triggered at time: " + Driver.total_time);
	    Queue highestPriorityQueue = queues.get(0);

	    // Reset allotment counts for processes already in the highest-priority queue
	    for (Process process : highestPriorityQueue.queue) {
	        process.resetAllotmentCount();
	    }

	    // Move processes from lower-priority queues to the highest-priority queue
	    for (int i = 1; i < queues.size(); i++) {
	        Queue lowerQueue = queues.get(i);

	        while (!lowerQueue.queue.isEmpty()) {
	            Process process = lowerQueue.queue.poll();
	            process.resetAllotmentCount();
	            highestPriorityQueue.append(process);
	        }
	    }
	    Driver.queue_reset += Driver.time_slice * 100;
	}

	
}
