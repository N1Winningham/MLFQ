package HW3Package;

import java.util.LinkedList;
import java.util.List;


public class Queue {
	
	public int level;	//The priority level of this queue.
	public LinkedList<Process> queue = new LinkedList<>();
	
	//Constructor that initializes the queue with the level.
	public Queue(int integer) {
		level = integer;
	}
	
	public void append(Process process) {
		queue.add(process);
	}
	
	//Runs 1 Time_Slice:
	public void runProcess(Process process, List<Queue> queues) {	
		
		//If the job is running for the first time, it records its RT.
		if (!process.hasRun) {
			process.setRT(Driver.total_time);
			process.hasRun = true;	//Marks the job as having run before.
			Driver.executionOrder.add(process);
		}
		
		timeSlice(process);	//Reduces the job length.
		System.out.println("Time Slice for Process: " + process.getID() + " from Queue has just finished!");		
		allotment(process);	//Increases allotment total for the job.
		
		boolean removal = false;	//Boolean to say if process has already been removed
		
		//Kills off job when done and Calculates TT as well as prints out RT and TT
		if (process.getJobLength() == 0) {
			process.setTT(Driver.total_time);
			System.out.println("Process: " + process.getID() + " has just finished! "
					+ "Its RT was: " + process.getRT() + " and its TT was: " + process.getTT());
			queue.remove(process);
			Driver.completedJobs.add(process);
			removal = true;
		}
		
		//If the job reaches its allotment, it resets its allotment count and moves it to the next queue level.
		if (process.getAllotmentCount() == Driver.allotment && !removal) {
			System.out.println("Process: " + process.getID() + " has just reached its allotment in Level: " + getLevel() + "!");
			process.resetAllotmentCount();
			
			//Move the job to the next lower-priority queue, if one exists.
			if (level < queues.size()) {
				queues.get(level).append(process);
			}
			queue.remove(process);
		}
		
		//Checks if boost is needed.
		if (Driver.total_time >= Driver.queue_reset) {
			boostPriority(queues);
		}
		System.out.println("Total Time after TS: " + Driver.total_time + " Level of Current Queue: " + level);
		
	}
	
	public void timeSlice(Process process) {
		
		//Checks if job length is less than the time slice to avoid negative values
		if (process.getJobLength() < 10) {
			Driver.total_time += process.getJobLength();	//Add the remaining job length to the total time.
			process.setJobLength(0);	//Mark the job as finished.
			
		}
		else {
			//Performs the time slice
			Driver.total_time += Driver.time_slice;	//Increase total time by the time slice.
			System.out.println("Job Length before time_slice:" + process.getJobLength());
			process.setJobLength(process.getJobLength() - Driver.time_slice);	//Decrease job length by the time slice.
			System.out.println("Job Length after time_slice:" + process.getJobLength());
		}
		//Moves the job to the end of the queue.
		queue.remove(process);
		queue.add(process);
	}
	// Increments the process's allotment count.
	public void allotment(Process process) {
		System.out.println("Job Allotment before time_slice:" + process.getAllotmentCount());
		process.incAllotmentCount();	//Increase the allotment count.
		System.out.println("Job Allotment after time_slice:" + process.getAllotmentCount());
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

	    //Reset allotment counts for jobs already in the highest-priority queue.
	    for (Process process : highestPriorityQueue.queue) {
	        process.resetAllotmentCount();
	    }

	    //Move jobs from lower-priority queues to the highest-priority queue.
	    for (int i = 1; i < queues.size(); i++) {
	        Queue lowerQueue = queues.get(i);

	        //Transfer all jobs to the highest-priority queue.
	        while (!lowerQueue.queue.isEmpty()) {
	            Process process = lowerQueue.queue.poll();	//Removes the job from lower queue.
	            process.resetAllotmentCount();	//Resets its allotment count.
	            highestPriorityQueue.append(process);	//Adds it to the highest-priority queue.
	        }
	    }
	    //Update the reset time for the next priority boost.
	    Driver.queue_reset += Driver.time_slice * 100;
	}	
}
