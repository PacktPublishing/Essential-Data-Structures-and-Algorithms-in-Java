import java.util.concurrent.Semaphore; 

public class SemaphoreExample { 
    public static void main(String[] args) throws InterruptedException { 
        // Create a semaphore with 2 permits (i.e., 2 threads can access  
        // the shared resource at any given time) 
        Semaphore semaphore = new Semaphore(2); 

        // Create and start 5 threads for accessing the shared resource 
        for (int i = 0; i < 5; i++) { 
            Thread thread = new Thread(() -> { 
                try { 
                    System.out.println("Thread " +  
                       Thread.currentThread().getName() +  
                         " is waiting for access..."); 
                    // Acquire a permit from the semaphore 
                    semaphore.acquire(); 
                       System.out.println("Thread " +   
                         Thread.currentThread().getName() +  
                           " has accessed the shared resource."); 
                    // Simulate some work... 
                    try { 
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) { 
                        Thread.currentThread().interrupt(); 
                    } 
                } finally { 
                    // Release the permit back to the semaphore 
                    semaphore.release(); 
                } 
            }); 
            thread.start(); 
        } 

        // Wait for all threads to finish 
        Thread.sleep(5000); 
    } 
}

