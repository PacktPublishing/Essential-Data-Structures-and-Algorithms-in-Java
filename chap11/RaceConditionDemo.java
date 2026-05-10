/**
 * Demonstrates a write-write race condition in a multi-threaded Java environment
 * and shows how to fix it using java.util.concurrent.atomic.AtomicInteger.
 *
 * A race condition occurs when the final result depends on the unpredictable
 * timing of thread execution. A simple increment (read -> modify -> write) is not atomic
 * and can lead to lost updates when executed concurrently.
 *
 */
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RaceConditionDemo {

    private static final int NUM_THREADS = 10;
    private static final int INCREMENTS_PER_THREAD = 10000;
    private static final int EXPECTED_TOTAL = NUM_THREADS * INCREMENTS_PER_THREAD;

    /**
     * Main method to execute and compare the race condition demonstration.
     * @param args Command line arguments (unused).
     * @throws InterruptedException If the thread waiting times out.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("============================================================");
        System.out.println("Java Race Condition Demo (Write-Write Race)");
        System.out.println("============================================================");

        // --- 1. UNSAFE DEMO (Race Condition) ---
        System.out.println("\n--- 1. Unsafe Increment (Race Condition Expected) ---");

        // The shared resource susceptible to race conditions
        int unsafeCounter = 0;

        // Use a thread pool to simulate concurrent writes
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < NUM_THREADS; i++) {
            // Creating a task that increments the shared int directly
            tasks.add(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeCounter++; // Read -> Modify -> Write (interleave-able)
                }
            });
        }

        for (Runnable task : tasks) {
            executor.submit(task);
        }

        // Wait for all tasks to complete
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Expected final count: " + EXPECTED_TOTAL);
        System.out.println("Actual unsafe count:   " + unsafeCounter);
        if (unsafeCounter != EXPECTED_TOTAL) {
            System.out.println("!!! RACE CONDITION DETECTED: The actual count is lower than expected due to lost updates.");
        } else {
            System.out.println("Warning: Race condition was not observable on this run, but the structure is unsafe.");
        }


        // --- 2. SAFE DEMO (Atomic Solution) ---
        System.out.println("\n\n--- 2. Safe Increment (AtomicInteger Fix) ---");

        // The atomic resource that guarantees thread safety
        AtomicInteger safeCounter = new AtomicInteger(0);

        // Re-initialize executor for the safe test
        ExecutorService safeExecutor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Runnable> safeTasks = new ArrayList<>();
        for (int i = 0; i < NUM_THREADS; i++) {
            // Creating a task using the atomic method
            safeTasks.add(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    safeCounter.incrementAndGet(); // Single atomic operation
                }
            });
        }

        for (Runnable task : safeTasks) {
            safeExecutor.submit(task);
        }

        // Wait for all tasks to complete
        safeExecutor.shutdown();
        safeExecutor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Expected final count: " + EXPECTED_TOTAL);
        System.out.println("Actual safe count:   " + safeCounter.get());
        if (safeCounter.get() == EXPECTED_TOTAL) {
            System.out.println("SUCCESS: The atomic counter maintained correct state, preventing race conditions.");
        }
    }
}
