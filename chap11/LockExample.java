import java.util.concurrent.locks.ReentrantLock; 
import java.util.concurrent.locks.Lock; 
import java.util.concurrent.TimeUnit; 

public class LockExample { 
    private final Lock lock = new ReentrantLock(); // ReentrantLock 
    private int counter = 0; 

    // Example 1: Basic lock usage with try-finally (recommended pattern) 
    public void basicLockUsage() { 
        lock.lock(); // Acquire the lock 
        try { 
            counter++; // Critical section 
        } finally { 
            lock.unlock(); // Always release lock in finally block 
        } 
    } 

    // Example 2: Timed wait - attempt to acquire lock with timeout 
    public boolean tryLockWithTimeout() { 
        try { 
            // Attempt to acquire lock within 1 second 
            if (lock.tryLock(1, TimeUnit.SECONDS)) { 
                try { 
                    counter++; 
                    return true; 
                } finally { 
                    lock.unlock(); // Release lock 
                } 
            } 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); // Restore status 
        } 
        return false; // Lock acquisition failed or interrupted 
    } 

    // Example 3: Non-blocking attempt - returns immediately 
    public boolean tryNonBlockingLock() { 
        if (lock.tryLock()) { // Non-blocking attempt 
            try { 
                counter++; 
                return true; 
            } finally { 
                lock.unlock(); 
            } 
        } 
        return false; // Lock not available immediately 
    } 

    // Example 4: Reentrant - same thread acquiring lock many times 
    public void reentrantExample() { 
        lock.lock(); // First acquisition 
        try { 
            // Same thread can acquire the lock again (reentrant) 
            lock.lock(); // Second acquisition - allowed! 
            try { 
                counter++; 
            } finally { 
                lock.unlock(); // Must match number of acquisitions 
            } 
        } finally { 
            lock.unlock(); // Release first acquisition 
        } 
    } 
}

