public interface Map<K,V> {
    
    // Insert a new key-value pair or overwrite the value for an existing key.
    V put(K key, V value);
   
    // Retrieve the value associated with key k.
    V get(K key);

    // Delete the entry identified by key k.
    V remove(K key);

    // Checks whether the map contains any elements
    boolean isEmpty();

    // Number of elements currently stored
    int size();

}

