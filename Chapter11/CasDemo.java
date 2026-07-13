import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstration of CAS (Compare-And-Swap) operation at the hardware level.
 * This example shows how atomic operations internally use CAS for thread safety.
 */
public class CasDemo {
    
    public static void main(String[] args) {
        System.out.println("=== CAS (Compare-And-Swap) Demo ===");
        System.out.println("CAS operates at hardware level as atomic instructions");
        System.out.println("No explicit locks required for thread safety");
        System.out.println();
        
        // Example 1: AtomicInteger uses CAS internally
        AtomicInteger atomicInt = new AtomicInteger(10);
        System.out.println("Initial value: " + atomicInt.get());
        
        // CAS operation: "if current value is 10, set to 15"
        boolean success = atomicInt.compareAndSet(10, 15);
        System.out.println("CAS (10 -> 15): " + success + " | New value: " + atomicInt.get());
        
        // CAS operation that fails: "if current value is 10, set to 20"
        success = atomicInt.compareAndSet(10, 20);
        System.out.println("CAS (10 -> 20): " + success + " | Value unchanged: " + atomicInt.get());
        
        // Example 2: Simulating CAS behavior manually
        System.out.println("\n--- Manual CAS Simulation ---");
        int value = 5;
        int expected = 5;
        int update = 10;
        
        System.out.println("Before CAS: value = " + value + ", expected = " + expected);
        
        // Simulate what happens internally in CAS
        if (value == expected) {
            value = update;
            System.out.println("CAS succeeded: value updated to " + value);
        } else {
            System.out.println("CAS failed: value (" + value + ") != expected (" + expected + ")");
        }
        
        System.out.println("\nCAS provides lock-free thread safety through hardware guarantees");
        System.out.println("Eliminates blocking and contention overhead of traditional locks");
    }
}

