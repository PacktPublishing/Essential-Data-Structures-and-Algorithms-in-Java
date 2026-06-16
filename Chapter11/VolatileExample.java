public class VolatileExample { 
    // Volatile ensures visibility across threads 
    private volatile boolean flag = false; 
    private volatile int counter = 0; 

    // Thread 1 - writer 
    public void setFlag() { 
        counter = 10;        // Write to volatile variable 
        flag = true;         // Write to volatile variable 
                             // both are visible immediately 
    } 

    // Thread 2 - reader 
    public void checkFlag() { 
        if (flag) {          // Read volatile variable 
                             // guaranteed to see latest value 
            // Visible due to volatile 
            System.out.println("Counter value: " + counter); 
        } 
    } 
}

