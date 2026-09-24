import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PrintSpooler {
    // Bounded FIFO buffer: at most 8 jobs can wait at once
    private final BlockingQueue<String> jobs = new ArrayBlockingQueue<>(8);

    // Producer side: called concurrently by many user threads
    public void submit(String job) throws InterruptedException {
        jobs.put(job);                        // blocks while the queue is full
    }

    // Consumer side: one printer thread drains jobs in FIFO order
    public void startPrinter() {
        Thread printer = new Thread(() -> {
            try {
                while (true) {
                    String job = jobs.take(); // blocks while the queue is empty
                    System.out.println("Printing " + job);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // exit cleanly on shutdown
            }
        });
        printer.start();
    }
}
