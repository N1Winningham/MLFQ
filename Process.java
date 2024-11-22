package mutiLevelFeedback;



public class Process {
	private static char ID = 'A';
	
	private char jobID;
	private int jobLength;
	private int tickets;
	private int allotmentCount = 0;
	private int arrivalTime;
	public boolean hasRun = false;
	private int TT;
	private int RT;
	
	public Process() {
		jobID = ID++;
		jobLength = 30;
		tickets = 50;
		arrivalTime = 0;
	}
	
	public Process(int minLength, int maxLength, 
	 int earliestArrival, int latestArrival,
	 int minTickets, int maxTickets) {
		jobID = ID++;
		jobLength = 10*((int)(Math.random()*((maxLength/10)-(minLength/10)))+(minLength/10));
		arrivalTime = 10*(int)(Math.random()*((latestArrival/10)-(earliestArrival/10))+(earliestArrival/10));
		tickets = (int)(Math.random()*(maxTickets-minTickets))+minTickets;
		
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
	
	public void printProcess() {
		System.out.println("Process: "+jobID+" Arrival Time: "+arrivalTime+" Job Length: "+jobLength+" Tickets: "+tickets);
	}
	public char getID() {
		return jobID;
	}
	public void setTT(int total) {
		TT = total - arrivalTime;
	}
	public void setRT(int total) {
		RT = total - arrivalTime;
	}
	public int getTT() {
		return TT;
	}
	public int getRT() {
		return RT;
	}

}
