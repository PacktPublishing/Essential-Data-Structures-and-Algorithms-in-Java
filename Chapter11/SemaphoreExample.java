import java.util.concurrent.Semaphore;

public class SemaphoreExample {
    public static void main(String[] args) throws InterruptedException {
        // Permits set to 2. At most 2 threads can bypass this bottleneck simultaneously.
        Semaphore semaphore = new Semaphore(2);

        // Spawn 5 worker threads to compete for the 2 slots
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                System.out.println("Thread " + Thread.currentThread().getName() + " is waiting for access...");
                
                try {
                    // If this fails or gets interrupted, the finally block below will NOT run.
                    semaphore.acquire(); // O(1) blocking operation
                    
                    // Protected Critical Section
                    System.out.println("Thread " + Thread.currentThread().getName() + " has accessed the shared resource.");
                    
                    try {
                        Thread.sleep(1000); // Simulate critical section workload
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Clean up thread status flag
                    }
                    
                } catch (InterruptedException e) {
                    System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted while waiting.");
                } finally {
                    // This block only fires if acquire() completely succeeded.
                    System.out.println("Thread " + Thread.currentThread().getName() + " is releasing access.");
                    semaphore.release(); // O(1) constant time permit return
                }
            });
            thread.start();
        }

        // Main thread sleep acting as a primitive join barrier
        Thread.sleep(5000); 
        System.out.println("Simulation finished.");
    }
}
