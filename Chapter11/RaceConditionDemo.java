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

    public static void main(String[] args) throws InterruptedException {
        System.out.println("============================================================");
        System.out.println("Java Race Condition Demo (Write-Write Race)");
        System.out.println("============================================================");

        // 1. UNSAFE DEMO (Race Condition)
        System.out.println("\n--- 1. Unsafe Increment (Race Condition Expected) ---");

        // Wrapped inside a single-element array to bypass Java's lambda 
        // "effectively final" rule while preserving data race mechanics.
        final int[] unsafeCounter = new int[1]; 

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < NUM_THREADS; i++) {
            tasks.add(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    // TIME COMPLEXITY: O(1)
                    // Read-Modify-Write is split across separate CPU instructions.
                    // Threads will context switch mid-operation, interleaving and 
                    // destroying updates.
                    unsafeCounter[0]++; 
                }
            });
        }

        for (Runnable task : tasks) {
            executor.submit(task);
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Expected final count: " + EXPECTED_TOTAL);
        System.out.println("Actual unsafe count:   " + unsafeCounter[0]);

        if (unsafeCounter[0] != EXPECTED_TOTAL) {
            System.out.println("!!! RACE CONDITION DETECTED: The actual count is lower than expected due to lost updates.");
        } else {
            System.out.println("Warning: Race condition was not observable on this run, but the structure is unsafe.");
        }

        // 2. SAFE DEMO (Atomic Solution)
        System.out.println("\n\n--- 2. Safe Increment (AtomicInteger Fix) ---");

        // AtomicInteger uses lock-free hardware-level instructions.
        AtomicInteger safeCounter = new AtomicInteger(0);

        ExecutorService safeExecutor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Runnable> safeTasks = new ArrayList<>();

        for (int i = 0; i < NUM_THREADS; i++) {
            safeTasks.add(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    // TIME COMPLEXITY: Amortized O(1) loop
                    // Leverages CAS (Compare-And-Swap) hardware architecture.
                    // If a conflict occurs, it retries safely at the hardware 
                    // level without locking.
                    safeCounter.incrementAndGet(); 
                }
            });
        }

        for (Runnable task : safeTasks) {
            safeExecutor.submit(task);
        }

        safeExecutor.shutdown();
        safeExecutor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Expected final count: " + EXPECTED_TOTAL);
        System.out.println("Actual safe count:     " + safeCounter.get());

        if (safeCounter.get() == EXPECTED_TOTAL) {
            System.out.println("SUCCESS: The atomic counter maintained correct state, preventing race conditions.");
        }
    }
}
