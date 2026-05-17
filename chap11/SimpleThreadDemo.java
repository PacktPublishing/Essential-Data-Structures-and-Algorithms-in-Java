// Simple thread demonstration showing independent execution 
public class SimpleThreadDemo { 
    private static int sharedCounter = 0; 

    public static void main(String[] args) throws InterruptedException { 
        Thread thread1 = new Thread(() -> { 
            for (int i = 0; i < 3; i++) { 
                System.out.println("Thread-1: " + i); 
                sharedCounter++; 
            } 
        }); 

        Thread thread2 = new Thread(() -> { 
            for (int i = 0; i < 3; i++) { 
                System.out.println("Thread-2: " + i); 
                sharedCounter++; 
            } 
        }); 

        thread1.start(); 
        thread2.start(); 
        thread1.join(); 
        thread2.join(); 

        System.out.println("Final counter: " + sharedCounter); 
    } 
}

