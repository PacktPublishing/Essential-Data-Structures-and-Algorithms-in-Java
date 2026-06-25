import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Concurrent counter using a shared variable with atomic (lock-free) updates.
 * Each update is a single hardware compare-and-swap, so increments never
 * interleave and no count is lost — without any locking.
 */
public class ConcurrentCounter {

    // Shared state. AtomicLong makes each read-modify-write atomic.
    private final AtomicLong count = new AtomicLong(0);

    public long increment()      { return count.incrementAndGet(); }
    public long decrement()      { return count.decrementAndGet(); }
    public long add(long delta)  { return count.addAndGet(delta); }
    public long get()            { return count.get(); }

    /** Reset to zero atomically, returning the previous value. */
    public long reset()          { return count.getAndSet(0); }

    // ------------------------------------------------------------------          // This will prove correctness under heavy contention.
    // Without atomicity, the final count would be LESS than expected
    // because concurrent read-modify-write sequences would clobber each other.
    // ------------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {
        ConcurrentCounter counter = new ConcurrentCounter();
        final int threads = 8, perThread = 100_000;
        final long expected = (long) threads * perThread;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);     // line all threads up
        CountDownLatch done  = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try {
                    start.await();                        // maximize real contention
                    for (int i = 0; i < perThread; i++) counter.increment();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();                                // fire
        done.await();                                     // join
        pool.shutdown();

        long actual = counter.get();
        System.out.printf("expected=%,d  actual=%,d  %s%n", expected, actual,
                actual == expected ? "PASS" : "FAIL (lost updates)");
    }
}
