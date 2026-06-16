import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A comprehensive demonstration of Java's atomic classes:
 * - AtomicInteger
 * - AtomicReference
 * - Compare-And-Swap (CAS) mechanism
 *
 * These are essential for thread-safe programming without explicit locking.
 */
public class AtomicExamples {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("Java Atomic Examples Demo");
        System.out.println("=================================================");

        // 1. Demonstration of Unsafe vs. Safe Increment
        System.out.println("\n--- 1. Unsafe vs. Safe Increment ---");
        unsafeIncrementTest();

        // 2. Demonstration of Compare-And-Swap (CAS)
        System.out.println("\n--- 2. Compare-And-Swap (CAS) ---");
        casExample();

        // 3. Demonstration of AtomicReference (CAS on objects)
        System.out.println("\n--- 3. AtomicReference Example ---");
        atomicReferenceExample();

        // 4. Practical use case: thread-safe max tracker
        System.out.println("\n--- 4. Thread-Safe Max Tracker ---");
        updateMaxDemo();
    }

    /**
     * NOT atomic — race condition
     * In a multithreaded environment, this can lead to lost updates.
     * This is because incrementing is not a single atomic operation.
     * It involves read -> increment -> write — which can be interleaved.
     */
    private static int unsafeCount = 0;
    private static void unsafeIncrement() {
        unsafeCount++; // This is NOT atomic!
    }

    /**
     * ✅ Atomic — thread-safe without locks
     * AtomicInteger provides atomic operations using hardware-level CAS.
     * No need for synchronized blocks or locks.
     */
    private static final AtomicInteger safeCount = new AtomicInteger(0);
    private static void safeIncrement() {
        safeCount.incrementAndGet(); // Single atomic operation
    }

    /**
     * Helper method to demonstrate the difference between unsafe and safe increments.
     * Note: In a real multi-threaded scenario, unsafeCount would likely be much less than expected.
     */
    private static void unsafeIncrementTest() {
        // Resetting state for clean demonstration
        unsafeCount = 0;
        safeCount.set(0);

        System.out.println("Running 10,000 increments on both unsafe and safe methods.");

        int iterations = 10000;

        // Simulate multiple threads trying to increment by calling repeatedly
        for (int i = 0; i < iterations; i++) {
            unsafeIncrement(); // Not safe in real threads
            safeIncrement();   // Safe due to atomicity
        }

        // Output results
        System.out.println("After " + iterations + " calls:");
        System.out.println("Unsafe Count (potential race condition): " + unsafeCount);
        System.out.println("Safe Count (AtomicInteger): " + safeCount.get());
        System.out.println("Expected result for both: " + iterations);
        System.out.println("In a real multithreaded environment, unsafeCount will likely be less than 10000.");
    }

    /**
     * Compare-And-Swap (CAS) — The core mechanism behind all atomic operations.
     * It compares the current value with an expected value and updates only if they match.
     * This is used internally by AtomicInteger, AtomicReference, etc.
     */
    private static void casExample() {
        AtomicInteger value = new AtomicInteger(5);
        System.out.println("Initial Value: " + value.get());

        // Attempt to set value from 5 to 10
        boolean success = value.compareAndSet(5, 10);
        System.out.println("CAS (Expected True): " + success + " | New Value: " + value.get());

        // Try again, but expect 5 (but value is now 10)
        success = value.compareAndSet(5, 20);
        System.out.println("CAS (Expected False): " + success + " | Value remains: " + value.get());
    }

    /**
     * AtomicReference — Allows atomic operations on object references.
     * Useful when you need to atomically update complex objects.
     */
    private static void atomicReferenceExample() {
        AtomicReference<String> ref = new AtomicReference<>("initial");

        // Thread-safe swap using CAS
        boolean success = ref.compareAndSet("initial", "updated");
        System.out.println("AtomicReference CAS success: " + success + " | Current Value: " + ref.get());

        // Atomic update using a function (Java 8+)
        ref.updateAndGet(current -> {
            System.out.println("  [Internal update step: " + current + " -> " + current.toUpperCase() + "]");
            return current.toUpperCase();
        });
        System.out.println("AtomicReference updateAndGet: " + ref.get());
    }

    /**
     * Practical Example: Thread-Safe Max Tracker
     * Demonstrates how to use updateAndGet() for atomic updates based on current state.
     * This is useful for tracking max/min values across threads.
     */
    private static final AtomicInteger maxSeen = new AtomicInteger(Integer.MIN_VALUE);

    private static void updateMaxDemo() {
        // Resetting state
        maxSeen.set(Integer.MIN_VALUE);

        System.out.println("Testing Max Tracker:");
        System.out.println("Setting max with 10 -> Current Max: " + maxSeen.updateAndGet(current -> Math.max(current, 10)));
        System.out.println("Setting max with 5 -> Current Max: " + maxSeen.updateAndGet(current -> Math.max(current, 5)));
        System.out.println("Setting max with 100 -> Current Max: " + maxSeen.updateAndGet(current -> Math.max(current, 100)));
        System.out.println("Setting max with 50 -> Current Max: " + maxSeen.updateAndGet(current -> Math.max(current, 50)));

        System.out.println("Final Max Seen: " + maxSeen.get());
    }
}

