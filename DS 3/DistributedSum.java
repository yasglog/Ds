/*Develop a distributed system, to find sum of N elements in an array by distributing N/n elements
to n number of processors MPI or OpenMP.Demonstrate by displaying the intermediate sums
calculated at different processors.
DistributedSum
*/
public class DistributedSum {
 private static final int N = 120; // Number of elements in the array
 private static final int NUM_THREADS = 5; // Number of threads/processors
 private static int[] array;
 private static int[] partialSums;
 public static void main(String[] args) throws InterruptedException {
 // Initialize the array with values
 array = new int[N];
 for (int i = 0; i < N; i++) {
 array[i] = i + 1;
 }
 partialSums = new int[NUM_THREADS];
 Thread[] threads = new Thread[NUM_THREADS];
 int elementsPerThread = N / NUM_THREADS;
 int startIndex = 0;
 // Create and start the threads
 for (int i = 0; i < NUM_THREADS; i++) {
 int endIndex = startIndex + elementsPerThread;
 if (i == NUM_THREADS - 1) {
 // Last thread handles remaining elements
 endIndex = N;
 }
 threads[i] = new Thread(new SumCalculator(startIndex, endIndex, i));
 threads[i].start();
 startIndex = endIndex;
 }
 // Wait for all threads to finish
 for (Thread thread : threads) {
 thread.join();
 }
 // Calculate the final sum
 int sum = 0;
 for (int partialSum : partialSums) {
 sum += partialSum;
 }
 // Display the final sum
 System.out.println("Final Sum = " + sum);
 }
 static class SumCalculator implements Runnable {
 private final int startIndex;
 private final int endIndex;
 private final int threadIndex;
 public SumCalculator(int startIndex, int endIndex, int threadIndex) {
 this.startIndex = startIndex;
 this.endIndex = endIndex;
 this.threadIndex = threadIndex;
 }
 @Override
 public void run() {
 int sum = 0;
 for (int i = startIndex; i < endIndex; i++) {
 sum += array[i];
 }
 partialSums[threadIndex] = sum;
 System.out.println("Thread " + threadIndex + ": Local Sum = " + sum);
 }
 }
}

