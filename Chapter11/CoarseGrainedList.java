import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * A sorted singly-linked set with COARSE-GRAINED locking: one lock guards the
 * entire list. Every mutating or reading operation acquires the same lock, so
 * at most one thread touches the structure at a time.
 *
 * Trade-off: dead simple and obviously correct, but the single lock serializes
 * ALL access — no concurrency between operations, even reads. Fine when
 * contention is low or operations are short; a bottleneck under heavy load.
 */
public class CoarseGrainedList<T extends Comparable<T>> {

    private static class Node<T> {
        final T value;
        Node<T> next;
        Node(T value) { this.value = value; }
    }

    // Sentinel head simplifies edge cases (empty list, insert/remove at front).
    private final Node<T> head = new Node<>(null);
    // ONE lock for the whole structure — the defining property of coarse-grained.
    private final Lock lock = new ReentrantLock();

    /** Insert value keeping the list sorted. Returns false if already present. */
    public boolean add(T value) {
        lock.lock();
        try {
            Node<T> pred = head;
            Node<T> curr = head.next;
            // Walk to the insertion point (list stays sorted ascending).
            while (curr != null && curr.value.compareTo(value) < 0) {
                pred = curr;
                curr = curr.next;
            }
            if (curr != null && curr.value.compareTo(value) == 0) {
                return false;               // already present — set semantics
            }
            Node<T> node = new Node<>(value);
            node.next = curr;
            pred.next = node;
            return true;
        } finally {
            lock.unlock();                  // ALWAYS release, even on exception
        }
    }

    /** Remove value. Returns true if it was present. */
    public boolean remove(T value) {
        lock.lock();
        try {
            Node<T> pred = head;
            Node<T> curr = head.next;
            while (curr != null && curr.value.compareTo(value) < 0) {
                pred = curr;
                curr = curr.next;
            }
            if (curr == null || curr.value.compareTo(value) != 0) {
                return false;               // not found
            }
            pred.next = curr.next;          // unlink
            return true;
        } finally {
            lock.unlock();
        }
    }

    /** Membership test. */
    public boolean contains(T value) {
        lock.lock();
        try {
            Node<T> curr = head.next;
            while (curr != null && curr.value.compareTo(value) < 0) {
                curr = curr.next;
            }
            return curr != null && curr.value.compareTo(value) == 0;
        } finally {
            lock.unlock();
        }
    }

    /** Snapshot of the list contents, in sorted order. */
    public List<T> toList() {
        lock.lock();
        try {
            List<T> out = new ArrayList<>();
            for (Node<T> curr = head.next; curr != null; curr = curr.next) {
                out.add(curr.value);
            }
            return out;
        } finally {
            lock.unlock();
        }
    }

    // ------------------------------------------------------------------
    // Demo: many threads mutate concurrently; the single lock keeps the
    // structure consistent (no lost links, no torn reads, no duplicates).
    // ------------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {
        CoarseGrainedList<Integer> list = new CoarseGrainedList<>();
        final int threads = 8, perThread = 1000;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(threads);

        // Each thread adds a disjoint block of integers concurrently.
        for (int t = 0; t < threads; t++) {
            final int base = t * perThread;
            pool.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < perThread; i++) list.add(base + i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        done.await();
        pool.shutdown();

        List<Integer> contents = list.toList();
        int expectedSize = threads * perThread;

        // Verify: correct size, fully sorted, no duplicates.
        boolean sorted = true;
        for (int i = 1; i < contents.size(); i++) {
            if (contents.get(i - 1) >= contents.get(i)) { sorted = false; break; }
        }
        boolean ok = contents.size() == expectedSize && sorted;
        System.out.printf("size=%d (expected %d), sorted=%b -> %s%n",
                contents.size(), expectedSize, sorted, ok ? "PASS" : "FAIL");
    }
}
