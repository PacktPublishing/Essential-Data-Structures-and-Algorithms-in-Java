import java.util.ArrayDeque;
import java.util.Deque;

public class SlidingWindowRateLimiter {
    private final int maxRequests;
    private final long windowMillis;
    private final Deque<Long> timestamps = new ArrayDeque<>();

    public SlidingWindowRateLimiter(int maxRequests, long windowMillis) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
    }

    public synchronized boolean tryAcquire(long nowMillis) {
        // Dequeue timestamps that have slid out of the window (oldest first)
        while (!timestamps.isEmpty()
                && nowMillis - timestamps.peekFirst() >= windowMillis) {
            timestamps.pollFirst();
        }
        if (timestamps.size() < maxRequests) {
            timestamps.offerLast(nowMillis);  // enqueue this request
            return true;
        }
        return false;                         // throttled
    }
}
