package AI_ML ;
import java.util.*;
public class AI_ML {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Scheduler scheduler = new Scheduler();
		while (true) {
		System.out.println("\nMenu:");
		System.out.println("1. Enter process’ information.");
		System.out.println("2. Report detailed information about each process and different scheduling criteria.");
		System.out.println("3. Exit the program.");
		System.out.print("Enter your choice: ");
		int choice = scanner.nextInt();
		switch (choice) {
		case 1:
		System.out.print("Enter the number of processes (P): ");
		int numProcesses = scanner.nextInt();
		for (int i = 0; i < numProcesses; i++) {
		System.out.println("Enter process " + (i + 1) + " information:");
		System.out.print("Priority (1 or 2): ");
		int priority = scanner.nextInt();
		System.out.print("Arrival time: ");
		int arrivalTime = scanner.nextInt();
		System.out.print("CPU burst: ");
		int cpuBurst = scanner.nextInt();
		scheduler.addProcess("P"+(i+1),priority, arrivalTime, cpuBurst);
		}
		break;
		case 2:
		// Print scheduling order
		System.out.print("\n Scheduling Order: [ ");
		scheduler.schedule();
		break;
		case 3:
		System.out.println("Exiting the program.");
		System.exit(0);
		default:
		System.out.println("Invalid choice. Please enter again.");
		}
		}
		}
}
