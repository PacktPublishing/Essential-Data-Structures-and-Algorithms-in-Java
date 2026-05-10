/**
 * Simple Java example demonstrating thread execution and thread isolation
 * Each thread has its own program counter, stack, and local variables
 */
public class ThreadIsolationDemo {
    
    // Shared variable accessible by all threads
    private static int sharedCounter = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Thread Isolation Demo ===");
        System.out.println("Demonstrating that each thread has its own execution flow");
        System.out.println();
        
        // Create multiple threads that will execute concurrently
        Thread thread1 = new Thread(new Task("Task-1"));
        Thread thread2 = new Thread(new Task("Task-2"));
        Thread thread3 = new Thread(new Task("Task-3"));
        
        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();
        
        // Wait for all threads to complete
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\nFinal shared counter value: " + sharedCounter);
        System.out.println("All threads have completed execution");
    }
    
    /**
     * Task implementation that demonstrates thread isolation
     * Each thread will have its own execution context
     */
    static class Task implements Runnable {
        private String taskName;
        
        public Task(String taskName) {
            this.taskName = taskName;
        }
        
        @Override
        public void run() {
            // Each thread has its own local variables
            int localCounter = 0;
            int localSum = 0;
            
            System.out.println(taskName + " starting execution");
            
            // Each thread has its own program counter (execution flow)
            for (int i = 0; i < 5; i++) {
                localCounter++;
                localSum += i;
                
                // Each thread has its own stack frame
                System.out.println(taskName + " - Iteration " + i + 
                                 " | Local Counter: " + localCounter + 
                                 " | Local Sum: " + localSum);
                
                // Simulate some work
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // Shared counter is accessed by all threads (but with potential race condition)
                sharedCounter++;
            }
            
            System.out.println(taskName + " completed. Final local counter: " + localCounter);
        }
    }
}

