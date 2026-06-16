import java.util.concurrent.locks.Condition; 
import java.util.concurrent.locks.Lock; 
import java.util.concurrent.locks.ReentrantLock; 

public class ConditionExample { 
    private final Lock lock = new ReentrantLock(); 
    private final Condition condition = lock.newCondition();  
    private boolean isReady = false; 

    // Thread waiting for condition 
    public void waitForReady() throws InterruptedException { 
        lock.lock(); // Acquire lock 
        try { 
            while (!isReady) { 
                condition.await(); // Wait until condition is true 
            } 
            // Continue when condition is met 
        } finally { 
            lock.unlock(); // Release lock 
        } 
    } 

    // Thread signaling condition 
    public void setReady() { 
        lock.lock(); // Acquire lock 
        try { 
            isReady = true; 
            condition.signalAll(); // Notify all waiting threads 
        } finally { 
            lock.unlock(); // Release lock 
        } 
    } 
}

