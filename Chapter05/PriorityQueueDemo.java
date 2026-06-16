import java.util.Arrays;

/**
 * PriorityQueueDemo demonstrates two fundamental, non-heap implementations
 * of the Priority Queue ADT using simple, fixed-size arrays.
 * 
 * Note: These implementations are highly educational, demonstrating O(n) complexity
 * for most operations, and are NOT suitable for high-throughput, real-world use.
 */
public class PriorityQueueDemo {

    // --- UNSTRUCTURED ARRAY IMPLEMENTATION ---
    // Best when writes are frequent, but reads are rare.
    public static class UnsortedArrayPQ {
        private int[] storage; 
        private int currentSize;
        private final int CAPACITY = 10;

        public UnsortedArrayPQ() {
            storage = new int[CAPACITY];
            currentSize = 0;
        }

        // O(1) Time Complexity
        public void insert(int item) {
            if (currentSize >= CAPACITY) {
                System.out.println("Error: Array is full.");
                return;
            }
            storage[currentSize] = item;
            currentSize++;
            System.out.println(" -> Inserted " + item + " at index " + (currentSize - 1) + ".");
        }

        // O(n) Time Complexity
        public int findMin() {
            if (currentSize == 0) {
                System.out.println("Queue is empty.");
                return Integer.MIN_VALUE;
            }
            int minVal = storage[0];
            for (int i = 1; i < currentSize; i++) {
                if (storage[i] < minVal) {
                    minVal = storage[i];
                }
            }
            return minVal;
        }

        // O(n) Time Complexity
        public int deleteMin() {
            if (currentSize == 0) {
                System.out.println("Queue is empty.");
                return Integer.MIN_VALUE;
            }
            int minIndex = 0;
            for (int i = 1; i < currentSize; i++) {
                if (storage[i] < storage[minIndex]) {
                    minIndex = i;
                }
            }
            int deletedValue = storage[minIndex];
            // Swap with the last element to fill the gap in O(1)
            storage[minIndex] = storage[currentSize - 1];
            currentSize--;
            return deletedValue;
        }

        public void printArray() {
            System.out.println(Arrays.toString(Arrays.copyOf(storage, currentSize)));
        }
    }

    // --- SORTED ARRAY IMPLEMENTATION ---
    // Best when reads are frequent, but writes are rare.
    // Sorted in descending order so the minimum element is always at the end (index currentSize - 1).
    // This allows O(1) removal.
    public static class SortedArrayPQ {
        private int[] storage;
        private int currentSize;
        private final int CAPACITY = 10;

        public SortedArrayPQ() {
            storage = new int[CAPACITY];
            currentSize = 0;
        }

        // O(n) Time Complexity due to shifting elements
        public void insert(int item) {
            if (currentSize >= CAPACITY) {
                System.out.println("Error: Array is full.");
                return;
            }

            // Find position to insert (descending order)
            int i;
            for (i = currentSize - 1; i >= 0; i--) {
                if (storage[i] < item) {
                    storage[i + 1] = storage[i]; // Shift right
                } else {
                    break;
                }
            }
            
            storage[i + 1] = item;
            currentSize++;
            System.out.println(" -> Inserted " + item + " into sorted position.");
        }

        // O(1) Time Complexity: Min item is always at the very end
        public int findMin() {
            if (currentSize == 0) {
                System.out.println("Queue is empty.");
                return Integer.MIN_VALUE;
            }
            return storage[currentSize - 1];
        }

        // O(1) Time Complexity: No shifting needed when removing from the end
        public int deleteMin() {
            if (currentSize == 0) {
                System.out.println("Queue is empty.");
                return Integer.MIN_VALUE;
            }
            int deletedValue = storage[currentSize - 1];
            currentSize--;
            return deletedValue;
        }

        public void printArray() {
            System.out.println(Arrays.toString(Arrays.copyOf(storage, currentSize)));
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Testing Unsorted Array Priority Queue ===");
        UnsortedArrayPQ unsortedPQ = new UnsortedArrayPQ();
        unsortedPQ.insert(40);
        unsortedPQ.insert(10);
        unsortedPQ.insert(30);
        unsortedPQ.insert(20);
        System.out.print("Current Array: ");
        unsortedPQ.printArray();
        
        System.out.println("Minimum found: " + unsortedPQ.findMin());
        System.out.println("Deleted minimum: " + unsortedPQ.deleteMin());
        System.out.print("Array after deletion: ");
        unsortedPQ.printArray();

        System.out.println("\n=== Testing Sorted Array Priority Queue ===");
        SortedArrayPQ sortedPQ = new SortedArrayPQ();
        sortedPQ.insert(40);
        sortedPQ.insert(10);
        sortedPQ.insert(30);
        sortedPQ.insert(20);
        System.out.print("Current Sorted Array (Descending): ");
        sortedPQ.printArray();
        
        System.out.println("Minimum found: " + sortedPQ.findMin());
        System.out.println("Deleted minimum: " + sortedPQ.deleteMin());
        System.out.print("Array after deletion: ");
        sortedPQ.printArray();
    }
}

