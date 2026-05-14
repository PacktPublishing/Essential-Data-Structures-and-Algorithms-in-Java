import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {
    private final ConcurrentHashMap<String, Integer> cache = new ConcurrentHashMap<>();
    // Basic thread-safe operations
    public void basicOperations() {
        cache.put("key1", 100);        	// Thread-safe put
        Integer value = cache.get("key1");  // Thread-safe get
        cache.remove("key1");          	// Thread-safe remove
    }
    
    // ❌ Dangerous pattern - non-atomic check-then-act
    public void dangerousPattern() {
        // Race condition: another thread might insert between 
        // containsKey and put
        if (!cache.containsKey("userCount")) {
            cache.put("userCount", 1);  // Risk of duplicate insertion
        }
    }
    // ✅ Safe atomic operations
    public void safeAtomicOperations() {
        // Atomic put if absent - no race condition
        cache.putIfAbsent("userCount", 1);
        // Atomic compute if absent - computes value only if key is absent
        cache.computeIfAbsent("expensiveValue", key -> {
            // Expensive computation here
            return performExpensiveCalculation();
        });
        
        // Atomic merge operation - reads existing value, applies 
        // function, stores result
        cache.merge("counter", 1, Integer::sum);
        // Atomic compute operation - full control over update logic
        cache.compute("counter", (key, oldValue) -> 
            oldValue == null ? 1 : oldValue + 1);
    }
    private int performExpensiveCalculation() {
        // Simulate expensive computation
        return 42;
    }
} 

