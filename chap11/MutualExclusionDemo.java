import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstration of mutual exclusion mechanisms in Java:
 * - synchronized keyword
 * - Lock interface with ReentrantLock
 * - Condition for thread coordination
 * 
 * This example shows how to protect shared resources from race conditions
 * using different synchronization approaches.
 */
public class MutualExclusionDemo {
    
    // Shared resource that needs protection
    private static int sharedCounter = 0;
    
    // Lock object for explicit locking mechanism
    private static final Lock lock = new ReentrantLock();
    
    // Condition object for advanced thread coordination
    private static final Condition condition = lock.newCondition();
    
    // Counter for synchronized approach
    private static int synchronizedCounter = 0;
    
    // Counter for lock approach
    private static int lockCounter = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Mutual Exclusion Mechanisms Demo ===");
        System.out.println("This demo demonstrates three approaches to ensure thread safety:");
        System.out.println("1. synchronized keyword");
        System.out.println("2. Lock interface with ReentrantLock");
        System.out.println("3. Condition for thread coordination");
        System.out.println();
        
        // Demonstrate synchronized approach
        System.out.println("--- 1. Synchronized Keyword Approach ---");
        synchronizedApproach();
        
        // Demonstrate Lock approach
        System.out.println("\n--- 2. Lock Interface Approach ---");
        lockApproach();
        
        // Demonstrate Condition approach
        System.out.println("\n--- 3. Condition Coordination Approach ---");
        conditionApproach();
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    /**
     * Demonstrates the use of synchronized keyword for mutual exclusion.
     * 
     * The synchronized keyword ensures that only one thread can execute
     * the synchronized block or method at a time, preventing race conditions.
     * 
     * Key characteristics:
     * - Automatic acquisition and release of lock
     * - Built-in monitor-based locking
     * - Simpler syntax but less flexible than Lock interface
     */
    private static void synchronizedApproach() {
        // Create a thread pool with 5 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // Submit 1000 tasks to increment the counter
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                // Synchronized block protects the critical section
                synchronized (MutualExclusionDemo.class) {
                    synchronizedCounter++;
                    System.out.println("Synchronized Counter: " + synchronizedCounter);
                }
            });
        }
        
        // Shutdown the executor and wait for completion
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final Synchronized Counter Value: " + synchronizedCounter);
    }
    
    /**
     * Demonstrates the use of Lock interface with ReentrantLock for mutual exclusion.
     * 
     * The Lock interface provides more control and flexibility compared to synchronized:
     * - Explicit acquisition and release of locks
     * - Support for timed waits
     * - Non-blocking attempts
     * - Ability to interrupt waiting threads
     * 
     * Key characteristics:
     * - Requires explicit lock() and unlock() calls
     * - More flexible than synchronized blocks
     * - Can be interrupted and timed out
     * - Supports reentrant behavior
     */
    private static void lockApproach() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // Submit 1000 tasks to increment the counter
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                try {
                    // Acquire the lock explicitly
                    lock.lock();
                    lockCounter++;
                    System.out.println("Lock Counter: " + lockCounter);
                } finally {
                    // Always release the lock in finally block
                    lock.unlock();
                }
            });
        }
        
        // Shutdown the executor and wait for completion
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final Lock Counter Value: " + lockCounter);
    }
    
    /**
     * Demonstrates the use of Condition for advanced thread coordination.
     * 
     * Conditions work with Lock objects to enable threads to wait for specific
     * conditions to become true before proceeding. This allows for more complex
     * synchronization patterns than simple locking.
     * 
     * Key characteristics:
     * - Wait for specific conditions to be met
     * - Signal other threads when conditions change
     * - More flexible than object monitors
     * - Can be used with multiple conditions
     */
    private static void conditionApproach() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Thread that will increment a counter and signal when threshold is reached
        executor.submit(() -> {
            try {
                lock.lock();
                System.out.println("Condition Thread: Starting to increment counter...");
                
                // Simulate some work and increment counter
                for (int i = 0; i < 5; i++) {
                    sharedCounter++;
                    System.out.println("Shared Counter: " + sharedCounter);
                    
                    // Wait for a condition (e.g., counter reaches 3)
                    if (sharedCounter >= 3) {
                        System.out.println("Condition met: Counter reached 3, signaling waiting threads...");
                        condition.signalAll(); // Notify all waiting threads
                    }
                    
                    // Small delay to allow other threads to run
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });
        
        // Thread that waits for a specific condition
        executor.submit(() -> {
            try {
                lock.lock();
                System.out.println("Waiting Thread: Waiting for counter to reach 3...");
                
                // Wait until condition is met
                while (sharedCounter < 3) {
                    condition.await(1, TimeUnit.SECONDS); // Wait up to 1 second
                }
                
                System.out.println("Waiting Thread: Condition met! Counter is now " + sharedCounter);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });
        
        // Another thread that waits for a specific condition
        executor.submit(() -> {
            try {
                lock.lock();
                System.out.println("Second Waiting Thread: Waiting for counter to reach 5...");
                
                // Wait until condition is met
                while (sharedCounter < 5) {
                    condition.await(1, TimeUnit.SECONDS); // Wait up to 1 second
                }
                
                System.out.println("Second Waiting Thread: Condition met! Counter is now " + sharedCounter);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });
        
        // Shutdown the executor and wait for completion
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final Shared Counter Value: " + sharedCounter);
    }
    
    /**
     * Utility method to demonstrate the importance of proper lock management.
     * 
     * This method shows what happens when locks are not properly released,
     * which can lead to deadlocks or resource leaks.
     */
    private static void demonstrateLockSafety() {
        // This approach shows why it's crucial to use try-finally blocks
        // to ensure locks are always released
        System.out.println("Demonstrating proper lock handling with try-finally...");
        
        try {
            lock.lock();
            // Simulate some work
            Thread.sleep(100);
            System.out.println("Lock acquired and work completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Always release the lock to prevent deadlocks
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("Lock released successfully");
            }
        }
    }
}

