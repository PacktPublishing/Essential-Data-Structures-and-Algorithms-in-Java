public class FixedInventoryManager {
    private final ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>();
    
    public void purchase(String itemId) {
        // Atomic operation - no race condition
        stock.computeIfPresent(itemId, (key, value) -> value > 0 ? value - 1 : value);
    }
}

