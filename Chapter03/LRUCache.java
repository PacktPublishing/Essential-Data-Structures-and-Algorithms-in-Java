import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache {
    private final int capacity;
    // [1] LinkedHashMap with accessOrder=true: get/put moves entry to tail
    //     Head = least recently used; tail = most recently used
    private final LinkedHashMap<Integer, Integer> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        // [2] initialCapacity, loadFactor, accessOrder=true
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                // [3] Auto-evict the LRU entry (head) when over capacity
                return size() > LRUCache.this.capacity;
            }
        };
    }

    // [4] get moves the entry to tail (most recently used) — O(1)
    public int get(int key) {
        return cache.getOrDefault(key, -1);
    }

    // [5] put inserts/updates and triggers eviction if needed — O(1)
    public void put(int key, int value) {
        cache.put(key, value);
    }

    public static void main(String[] args) {
        LRUCache lru = new LRUCache(2);
        lru.put(1, 1);   // cache: {1=1}
        lru.put(2, 2);   // cache: {1=1, 2=2}
        System.out.println(lru.get(1));  // 1 → moves 1 to tail: {2=2, 1=1}
        lru.put(3, 3);   // capacity exceeded → evict LRU (key=2): {1=1, 3=3}
        System.out.println(lru.get(2));  // -1 (evicted)
        lru.put(4, 4);   // evict LRU (key=1): {3=3, 4=4}
        System.out.println(lru.get(1));  // -1 (evicted)
        System.out.println(lru.get(3));  // 3
        System.out.println(lru.get(4));  // 4
    }
}

