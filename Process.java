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
    private static final int allotMax = 1 * timeSlice ; // Max allotment time
    private static final int resetTime = 50 * timeSlice;
    private int startTime = -1; // Start time of the job, initially not set.
    private int completionTime = -1; // Completion time, initially not set.
    private boolean hasStarted = false; // Track if process has started executing

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

    public void executeProcess(int time) {
        // Set return time only on first execution
        if (!hasStarted) {
            this.returnTime = startTime - arrivalTime;
            hasStarted = true;
        }
        
        jobLength -= time;
        if (jobLength < 0) {
            jobLength = 0;
        }
    }

    public void startProcess(int startTime) {
        this.startTime = startTime;
    }

    public void completeProcess(int completionTime) {
        this.completionTime = completionTime;
    }

    // Getters and setters
    public int getJobLength() {
        return jobLength;
    }

    public int getTickets() {
        return tickets;
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

    public int getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(int returnTime) {
        this.returnTime = returnTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public int getRemainingTime() {
        return jobLength;
    }

    public boolean isCompleted() {
        return jobLength == 0;
    }

    public char jobID() {
        return this.jobID;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setFinishOrder(int order) {
        // Optional: Implement if needed for tracking
    }

    public int getCompletionTime() {
        return completionTime;
    }
}
