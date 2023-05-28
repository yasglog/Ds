import java.util.Scanner;
public class BullyAlgorithm {
class Process {
int id;
boolean active;
public Process(int id) {
this.id = id;
active = true;
}
}
int totalProcesses;
Process[] processes;
public BullyAlgorithm(int totalProcesses) {
this.totalProcesses = totalProcesses;
processes = new Process[totalProcesses];
for (int i = 0; i < totalProcesses; i++) {
processes[i] = new Process(i);
}
}
public void election(int initializedProcess) {
System.out.println("Process " + processes[initializedProcess].id + " initiates the election.");
int maxId = processes[initializedProcess].id;
int maxIndex = initializedProcess;
for (int i = initializedProcess + 1; i < totalProcesses; i++) {
if (processes[i].active) {
if (processes[i].id > maxId) {
System.out.println("Process " + processes[i].id + " receives election message from process " +
processes[initializedProcess].id);
maxId = processes[i].id;
maxIndex = i;
}
}}
if (maxIndex != initializedProcess) {
System.out.println("Process " + processes[maxIndex].id + " is the new coordinator.");
for (int i = maxIndex + 1; i < totalProcesses; i++) {
if (processes[i].active) {
System.out.println("Process " + processes[i].id + " receives coordinator message from process " +
processes[maxIndex].id);
}
}
} else {
System.out.println("Process " + processes[initializedProcess].id + " is the coordinator.");
}
}
public static void main(String[] args) {
Scanner input = new Scanner(System.in);
System.out.print("Enter the number of processes: ");
int totalProcesses = input.nextInt();
BullyAlgorithm algorithm = new BullyAlgorithm(totalProcesses);
System.out.print("Enter the ID of the process that initiates the election: ");
int initializedProcess = input.nextInt();
algorithm.election(initializedProcess);
input.close();
}
}
