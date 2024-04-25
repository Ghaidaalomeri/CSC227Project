package AI_ML;
import java.io.*;
import java.util.*;

public class Scheduler {
	List<PCB> q1;
	List<PCB> q2;
	int currentTime;
	int NotEnd;
	public Scheduler() {
	q1 = new ArrayList<>();
	q2 = new ArrayList<>();
	currentTime = 0;
	NotEnd=0; //SJF
	}
	public void addProcess(String processID, int priority, int arrivalTime, int cpuBurst) {
		PCB process = new PCB(processID, priority, arrivalTime, cpuBurst);
		if (priority == 1) {
		    q1.add(process);
		} else {
		    q2.add(process);
		}
	}
	public String calculateAverages(List<PCB> scheduleOrder) {
	int totalTurnaroundTime = 0;
	int totalWaitingTime = 0;
	int totalResponseTime = 0;
	// Calculate totals
	for (PCB process : scheduleOrder) {
	totalTurnaroundTime += process.turnaroundTime;
	totalWaitingTime += process.waitingTime;
	totalResponseTime += process.responseTime;
	}
	// Calculate averages
	double avgTurnaroundTime = (double) totalTurnaroundTime / scheduleOrder.size();
	double avgWaitingTime = (double) totalWaitingTime / scheduleOrder.size();
	double avgResponseTime = (double) totalResponseTime / scheduleOrder.size();
	// Format averages
	String averages = "Average Turnaround Time: " + avgTurnaroundTime + "\n"
	+ "Average Waiting Time: " + avgWaitingTime + "\n"
	+ "Average Response Time: " + avgResponseTime;
	return averages;
	}
	public void schedule() {
	Collections.sort(q1, Comparator.comparingInt(process -> process.newarrivalTime));
	Collections.sort(q2, Comparator.comparingInt(process -> process.newarrivalTime));
	PCB shortestJob = null;
	List<PCB> scheduleOrder = new ArrayList<>();
	while (!q1.isEmpty() || !q2.isEmpty()) { //
	// RR with Q=3
	if (!q1.isEmpty() && q1.get(0).arrivalTime <= currentTime) {
	PCB currentProcess = q1.remove(0);
	System.out.print(currentProcess.processID + " | ");
	if (currentProcess.startTime == -1) {
	currentProcess.startTime = currentTime; // Set start time only when the process starts
	}
	int remainingBurst = currentProcess.cpuBurst;
	if (remainingBurst > 3) {
	currentTime += 3;
	currentProcess.newarrivalTime = currentTime;
	currentProcess.cpuBurst -= 3;
	q1.add(currentProcess);
	Collections.sort(q1, Comparator.comparingInt(process -> process.
	newarrivalTime));
	} else {
	currentTime += remainingBurst;
	currentProcess.responseTime = currentProcess.startTime - currentProcess.
	arrivalTime;
	currentProcess.terminationTime = currentTime; // Set termination time when the process finishes
	currentProcess.turnaroundTime = currentProcess.terminationTime -
	currentProcess.arrivalTime;
	currentProcess.waitingTime = currentProcess.turnaroundTime -
	currentProcess.originalCpuBurst;
	scheduleOrder.add(currentProcess);
	}
	} else if (q2.isEmpty()) {
	System.out.print("C| ");
	currentTime++;
	} else if (!q2.isEmpty() && q2.get(0).arrivalTime <= currentTime) {
	if (NotEnd == 1) {
	shortestJob = q2.get(0);
	}
	else {
	if (q2.get(0).arrivalTime <= currentTime) {
	shortestJob = q2.get(0);
	for (PCB process : q2) {
	if (process.arrivalTime <= currentTime && process.cpuBurst <
	shortestJob.cpuBurst) {
	shortestJob = process;
	}
	}
	}
	}
	q2.remove(shortestJob);
	System.out.print(shortestJob.processID + " | ");
	if (shortestJob.startTime == -1) {
	shortestJob.startTime = currentTime; // Set start time only when the process starts
	}
	shortestJob.responseTime = shortestJob.startTime - shortestJob.arrivalTime;
	while ((q1.isEmpty()||q1.get(0).arrivalTime > currentTime) && shortestJob.
	cpuBurst > 0) {
	shortestJob.cpuBurst--;
	currentTime++;
	shortestJob.terminationTime = currentTime ;
	}
	if (shortestJob.cpuBurst > 0) {
	//not finished
	NotEnd = 1;
	q2.add(0, shortestJob);
	} else {
	NotEnd = 0;
	shortestJob.turnaroundTime = shortestJob.terminationTime - shortestJob.
	arrivalTime;
	shortestJob.waitingTime = shortestJob.turnaroundTime - shortestJob.
	originalCpuBurst;
	scheduleOrder.add(shortestJob);
	currentTime = shortestJob.terminationTime;
	}
	} else {
	System.out.print("C| ");
	currentTime++;
	}
	}
	// Print detailed information
	System.out.print(" ]");
	System.out.println("\n" +"\n" +
	"ProcessID | Pr | AT | Burst | ST | ET | TT | WT | RT");
	for (PCB process : scheduleOrder) {
	System.out.println(process.processID + " | "
	+ process.priority + " | "
	+ process.arrivalTime + " | "
	+ process.originalCpuBurst + " | "
	+ process.startTime + " | "
	+ process.terminationTime + " | "
	+ process.turnaroundTime + " | "
	+ process.waitingTime + " | "
	+ process.responseTime);
	}
	System.out.println( "\n" + calculateAverages(scheduleOrder));
	// Write to file
	try {
	FileWriter fileWriter = new FileWriter("E:\\file\\Report.txt");
	PrintWriter printWriter = new PrintWriter(fileWriter);
	printWriter.println( "\n" + "ProcessID | Pr | AT | Burst | ST | ET | TT | WT | RT");
	for (PCB process : scheduleOrder) {
	printWriter.println(process.processID + " | "
	+ process.priority + " | "
	+ process.arrivalTime + " | "
	+ process.originalCpuBurst + " | "
	+ process.startTime + " | "
	+ process.terminationTime + " | "
	+ process.turnaroundTime + " | "
	+ process.waitingTime + " | "
	+ process.responseTime);
	}
	printWriter.println("\n" +calculateAverages(scheduleOrder));
	printWriter.close();
	} catch (IOException e) {
	e.printStackTrace();
	}
	}
}
