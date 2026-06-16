public class SynchronizedExample { 
    private int counter = 0; 

    // Synchronized method - ensures only one thread can execute at a time 
    public synchronized void increment() { 
        counter++; // This block is atomic due to synchronization 
    } 

    // Synchronized block - locks on specific object 
    public void safeIncrement() { 
        synchronized(this) { // Lock acquired on current object 
            counter++; // Critical section 
        } // Lock automatically released when block exits 
    } 

    // Synchronized block with different lock object 
    private final Object lock = new Object(); 
    public void anotherMethod() { 
        synchronized(lock) { // Lock acquired on specific object 
            // Critical section 
            counter++; 
        } // Lock released automatically 
    } 
}

