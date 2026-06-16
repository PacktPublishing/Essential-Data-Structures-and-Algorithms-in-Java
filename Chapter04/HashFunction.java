public class HashFunction {
    /**
     * Implements polynomial rolling hash function with base 31
     * @param key the string to hash
     * @param tableSize the size of the hash table
     * @return the hash index for the given key
     */
    public static int hash(String key, int tableSize) {
        // Handle null key case
        if (key == null) {
            return 0;
        }
        
        // Initialize hash value
        int hash = 0;
        
        // Apply polynomial rolling hash with base 31
        for (int i = 0; i < key.length(); i++) {
            hash = 31 * hash + key.charAt(i);
        }
        
        // Apply modulo to get index within table bounds
        // Use Math.abs to handle negative hash values
        return Math.abs(hash) % tableSize;
    }
}
