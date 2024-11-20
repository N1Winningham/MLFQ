package HW3Package;

public class Process {
    private static char ID = 'A';

    private char jobID;
    private int jobLength;
    private int tickets;
    private int allotmentCount = 0;
    private int arrivalTime;
    private int turnAroundTime = 0;
    private int returnTime = 0;
    private int queueNum = 3;
    private static final int timeSlice = 10; // Default time slice
    private static final int allotMax = 3 * timeSlice; // Max allotment time
    private static final int resetTime = 32 * timeSlice;
    private int startTime = -1; // Start time of the job, initially not set.
    private int completionTime = -1; // Completion time, initially not set.

    public Process() {
        jobID = ID++;
        jobLength = 30;
        tickets = 50;
        arrivalTime = 0;
    }

    public Process(int minLength, int maxLength, int earliestArrival, int latestArrival, int minTickets, int maxTickets) {
        jobID = ID++;
        jobLength = 10 * ((int) (Math.random() * ((maxLength / 10) - (minLength / 10))) + (minLength / 10));
        arrivalTime = 10 * (int) (Math.random() * ((latestArrival / 10) - (earliestArrival / 10)) + earliestArrival / 10);
        tickets = (int) (Math.random() * (maxTickets - minTickets)) + minTickets;
    }

    public int getJobLength() {
        return jobLength;
    }

    public void setJobLength(int jLength) {
        jobLength = jLength;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int num) {
        tickets = num;
    }

    public int getAllotmentCount() {
        return allotmentCount;
    }

    public void resetAllotmentCount() {
        allotmentCount = 0;
    }

    public void incAllotmentCount() {
        allotmentCount++;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int start) {
        arrivalTime = start;
    }

    public void setFinishOrder(int order) {
        // No-op: Replace or implement tracking as necessary
    }

    public int getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(int queue) {
        queueNum = queue;
    }

    public int getTimeSlice() {
        return timeSlice;
    }

    public int getAllotMax() {
        return allotMax;
    }

    public static int getResetTime() {
        return resetTime;
    }

    public void printProcess() {
        System.out.println("Process: " + jobID + " Turn Around Time: " + turnAroundTime + " Return Time: " + returnTime);
    }

    // Return the current return time of the process
    public int getReturnTime() {
        return returnTime;
    }

    // Set the return time
    public void setReturnTime(int returnTime) {
        this.returnTime = returnTime;
    }

    // Return the current turnaround time of the process
    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    // Set the turnaround time
    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    // Return remaining time for the job
    public int getRemainingTime() {
        return jobLength;
    }

    // Execute the process for a given time slice, reduce job length
    public void executeProcess(int time) {
        jobLength -= time;
        if (jobLength < 0) {
            jobLength = 0;
        }
    }

    // Check if the process is completed
    public boolean isCompleted() {
        return jobLength == 0;
    }

    // Start the process, update start time
    public void startProcess(int startTime) {
        this.startTime = startTime;
        // Set return time once the process starts
        this.returnTime = startTime - arrivalTime;
    }

    // Set the completion time of the process
    public void completeProcess(int completionTime) {
        this.completionTime = completionTime;
        // Calculate turnaround time once the process is complete
        this.turnAroundTime = completionTime - arrivalTime;
    }

    public char jobID() {
        return this.jobID;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }
}
