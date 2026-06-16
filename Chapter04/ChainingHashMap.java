public class ChainingHashMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    private class Node {
        K key;
        V value;
        Node next;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    private Node[] buckets;
    private int size;
    private int capacity;
    
    public ChainingHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.buckets = new Node[capacity];
        this.size = 0;
    }
    
    /**
     * Calculates hash index for given key
     */
    private int hash(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        return Math.abs(h) % capacity;
    }
    
    /**
     * Resizes the hash table when load factor exceeds threshold
     */
    private void resize() {
        int oldCapacity = capacity;
        Node[] oldBuckets = buckets;
        
        // Double the capacity
        capacity *= 2;
        size = 0;
        buckets = new Node[capacity];
        
        // Rehash all existing entries
        for (int i = 0; i < oldCapacity; i++) {
            Node current = oldBuckets[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
    
    /**
     * Inserts or updates a key-value pair using chaining
     */
    public void put(K key, V value) {
        // Check if we need to resize
        if (size >= capacity * LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        
        int index = hash(key);
        Node current = buckets[index];
        
        // Traverse chain to find existing key
        while (current != null) {
            if (current.key == null ? key == null : current.key.equals(key)) {
                current.value = value;  // Update existing value
                return;
            }
            current = current.next;
        }
        
        // Insert new node at head of chain
        Node newNode = new Node(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
    }
    
    /**
     * Retrieves value for given key using chaining
     */
    @SuppressWarnings("unchecked")
    public V get(K key) {
        int index = hash(key);
        Node current = buckets[index];
        
        // Traverse chain to find key
        while (current != null) {
            if (current.key == null ? key == null : current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        
        return null;
    }
    
    /**
     * Removes key-value pair and returns the value using chaining
     */
    @SuppressWarnings("unchecked")
    public V remove(K key) {
        int index = hash(key);
        Node current = buckets[index];
        Node prev = null;
        
        // Traverse chain to find key
        while (current != null) {
            if (current.key == null ? key == null : current.key.equals(key)) {
                V value = current.value;
                
                // Remove node from chain
                if (prev == null) {
                    buckets[index] = current.next;  // Remove head node
                } else {
                    prev.next = current.next;  // Skip the node to remove
                }
                
                size--;
                return value;
            }
            prev = current;
            current = current.next;
        }
        
        return null;
    }
}

