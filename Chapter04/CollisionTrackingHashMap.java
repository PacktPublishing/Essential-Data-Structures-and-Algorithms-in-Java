public class CollisionTrackingHashMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    
    private Object[] keys;
    private Object[] values;
    private boolean[] occupied;
    private int size;
    private int capacity;
    private int collisionCount;
    private int totalProbes;
    
    public CollisionTrackingHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.keys = new Object[capacity];
        this.values = new Object[capacity];
        this.occupied = new boolean[capacity];
        this.size = 0;
        this.collisionCount = 0;
        this.totalProbes = 0;
    }
    
    /**
     * Calculates hash index for given key
     */
    private int hash(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        return Math.abs(h) % capacity;
    }
    
    /**
     * Inserts or updates a key-value pair with collision tracking
     */
    public void put(K key, V value) {
        int index = hash(key);
        int probes = 0;
        boolean collision = false;
        
        // Linear probing to find empty slot
        while (occupied[index]) {
            probes++;  // Count probes
            totalProbes += probes;
            
            // If key already exists, update value
            if (keys[index] == null ? key == null : keys[index].equals(key)) {
                values[index] = value;
                return;
            }
            collision = true;
            index = (index + 1) % capacity;
        }
        
        // Increment collision count if we had to probe
        if (collision) {
            collisionCount++;
        }
        
        // Insert new key-value pair
        keys[index] = key;
        values[index] = value;
        occupied[index] = true;
        size++;
        totalProbes += probes + 1;  // Add the final probe for insertion
    }
    
    /**
     * Retrieves value for given key with probe tracking
     */
    @SuppressWarnings("unchecked")
    public V get(K key) {
        int index = hash(key);
        int probes = 0;
        
        // Linear probing to find key
        while (occupied[index]) {
            probes++;  // Count probes
            totalProbes += probes;
            
            if (keys[index] == null ? key == null : keys[index].equals(key)) {
                return (V) values[index];
            }
            index = (index + 1) % capacity;
        }
        
        totalProbes += probes;  // Account for final probe
        return null;
    }
    
    /**
     * Removes key-value pair and returns the value with probe tracking
     */
    @SuppressWarnings("unchecked")
    public V remove(K key) {
        int index = hash(key);
        int probes = 0;
        
        // Linear probing to find key
        while (occupied[index]) {
            probes++;  // Count probes
            totalProbes += probes;
            
            if (keys[index] == null ? key == null : keys[index].equals(key)) {
                V value = (V) values[index];
                // Mark as deleted (tombstone)
                occupied[index] = false;
                size--;
                totalProbes += probes + 1;  // Account for final probe
                return value;
            }
            index = (index + 1) % capacity;
        }
        
        totalProbes += probes;  // Account for final probe
        return null;
    }
    
    /**
     * Returns number of collisions that occurred during put operations
     */
    public int getCollisionCount() {
        return collisionCount;
    }
    
    /**
     * Returns total number of probe operations performed
     */
    public int getTotalProbes() {
        return totalProbes;
    }
    
    /**
     * Calculates and returns average number of probes per operation
     */
    public double getAverageProbes() {
        if (size == 0) {
            return 0.0;
        }
        return (double) totalProbes / size;
    }
    
    /**
     * Returns current size of the map
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns current capacity of the map
     */
    public int capacity() {
        return capacity;
    }
}

