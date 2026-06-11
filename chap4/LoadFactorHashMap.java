public class LoadFactorHashMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    private Object[] keys;
    private Object[] values;
    private boolean[] occupied;
    private int size;
    private int capacity;
    private int rehashCount;
    
    public LoadFactorHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.keys = new Object[capacity];
        this.values = new Object[capacity];
        this.occupied = new boolean[capacity];
        this.size = 0;
        this.rehashCount = 0;
    }
    
    /**
     * Calculates hash index for given key
     */
    private int hash(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        return Math.abs(h) % capacity;
    }
    
    /**
     * Resizes the hash table and rehashes all existing entries
     */
    private void resize() {
        int oldCapacity = capacity;
        Object[] oldKeys = keys;
        Object[] oldValues = values;
        boolean[] oldOccupied = occupied;
        
        // Double the capacity
        capacity *= 2;
        size = 0;
        rehashCount++;  // Increment rehash counter
        keys = new Object[capacity];
        values = new Object[capacity];
        occupied = new boolean[capacity];
        
        // Rehash all existing entries
        for (int i = 0; i < oldCapacity; i++) {
            if (oldOccupied[i]) {
                @SuppressWarnings("unchecked")
                K key = (K) oldKeys[i];
                @SuppressWarnings("unchecked")
                V value = (V) oldValues[i];
                put(key, value);
            }
        }
    }
    
    /**
     * Inserts or updates a key-value pair with load factor monitoring
     */
    public void put(K key, V value) {
        // Check if we need to resize based on load factor
        if (size >= capacity * LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        
        int index = hash(key);
        
        // Linear probing to find empty slot
        while (occupied[index]) {
            // If key already exists, update value
            if (keys[index] == null ? key == null : keys[index].equals(key)) {
                values[index] = value;
                return;
            }
            index = (index + 1) % capacity;
        }
        
        // Insert new key-value pair
        keys[index] = key;
        values[index] = value;
        occupied[index] = true;
        size++;
    }
    
    /**
     * Retrieves value for given key
     */
    @SuppressWarnings("unchecked")
    public V get(K key) {
        int index = hash(key);
        
        // Linear probing to find key
        while (occupied[index]) {
            if (keys[index] == null ? key == null : keys[index].equals(key)) {
                return (V) values[index];
            }
            index = (index + 1) % capacity;
        }
        
        return null;
    }
    
    /**
     * Removes key-value pair and returns the value
     */
    @SuppressWarnings("unchecked")
    public V remove(K key) {
        int index = hash(key);
        
        // Linear probing to find key
        while (occupied[index]) {
            if (keys[index] == null ? key == null : keys[index].equals(key)) {
                V value = (V) values[index];
                // Mark as deleted (tombstone)
                occupied[index] = false;
                size--;
                return value;
            }
            index = (index + 1) % capacity;
        }
        
        return null;
    }
    
    /**
     * Calculates and returns current load factor
     */
    public double getLoadFactor() {
        if (capacity == 0) {
            return 0.0;
        }
        return (double) size / capacity;
    }
    
    /**
     * Returns number of rehash operations performed
     */
    public int getRehashCount() {
        return rehashCount;
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

