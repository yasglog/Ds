import java.util.Scanner;

public class RingAlgorithm {
    private int numProcesses;
    private int[] processIDs;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RingAlgorithm ra = new RingAlgorithm();
        ra.readInputs(sc);
        ra.startElection();
        sc.close();
    }

    private void readInputs(Scanner sc) {
        System.out.print("Enter the number of processes: ");
        numProcesses = sc.nextInt();
        processIDs = new int[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Enter the process ID for process " + (i + 1) + ": ");
            processIDs[i] = sc.nextInt();
        }
    }

    private void startElection() {
        int coordinator = 0;
        for (int i = 0; i < numProcesses; i++) {
            if (processIDs[i] >= processIDs[coordinator]) {
                coordinator = i;
            }
        }
        System.out.println("Process " + processIDs[coordinator] + " is the coordinator.");
        int currentProcess = (coordinator + 1) % numProcesses;
        while (currentProcess != coordinator) {
            System.out.println("Process " + processIDs[coordinator] + " sends message to Process " + processIDs[currentProcess]);
            if (processIDs[currentProcess] >= processIDs[coordinator]) {
                coordinator = currentProcess;
                System.out.println("Process " + processIDs[currentProcess] + " is the new coordinator.");
            }
            currentProcess = (currentProcess + 1) % numProcesses;
        }
    }
}

