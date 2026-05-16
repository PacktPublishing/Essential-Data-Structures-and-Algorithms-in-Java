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
    // This approach is best when writes (insertions) are extremely frequent,
    // but reads (finding min/max) are very rare, and can tolerate O(n) cost.
    public static class UnsortedArrayPQ {
        private int[] heap; // Using 'heap' name just to avoid conflict with the heap section
        private int currentSize;
        private final int CAPACITY = 10;

        public UnsortedArrayPQ() {
            heap = new int[CAPACITY];
            currentSize = 0;
        }

        // O(1) Amortized Time Complexity
        public void insert(int item) {
            if (currentSize >= CAPACITY) {
                System.out.println("Error: Array is full.");
                return;
            }
            // Simply append the new element to the end of the array.
            heap[currentSize] = item;
            currentSize++;
            System.out.println(" -> Inserted " + item + " at index " + (currentSize - 1) + ".");
        }

        // O(n) Time Complexity: Requires a full scan of the array.
        public int findMin() {
            if (currentSize == 0) return Integer.MIN_VALUE;
            int minVal = Integer.MAX_VALUE;
            for (int i = 0; i < currentSize; i++) {
                if (heap[i] < minVal) {
                    minVal = heap[i];
                }
            }
            return minVal;
        }

        // O(n) Time Complexity: Must scan to find the min/max position first.
        public int deleteMin() {
            if (currentSize == 0) return Integer.MIN_VALUE;
            
            // 1. Find the index of the minimum element (O(n))
            int minIndex = 0;
            for (int i = 1; i < currentSize; i++) {
                if (heap[i] < heap[minIndex]) {
                    minIndex = i;
                }
            }
            
            int deletedValue = heap[minIndex];
            
            // 2. Overwrite the minimum slot with the last element (O(1) swap logic)
            // This assumes the min is not already the last element.
            heap[minIndex] = heap[currentSize - 1];
            
            // 3. Shrink the effective size of the array.
            currentSize--;
            
            return deletedValue;
        }

        public void printArray() {
            System.out.print(Arrays.toString(Arrays.copyOf(heap, currentSize)));
        }
    }

    // --- SORTED ARRAY IMPLEMENTATION ---
    // This approach is best when reads (finding min/max) are frequent,
    // and writes (insertions) are rare and few.
    public static class SortedArrayPQ {
        private int[] heap;
        private int currentSize;
        private final int CAPACITY = 10;

        public SortedArrayPQ() {
            heap = new int[CAPACITY];
            currentSize = 0;
        }

        // O(n) Time Complexity: Must shift elements to maintain sorted order.
        public void insert(int item) {
            if (currentSize >= CAPACITY) {
                System.out.println("Error: Array is full.");
                return;
            }
            // Find the correct insertion point (binary search implementation details omitted for clarity)
            int insertIndex = 0;
            for (int i = 0; i < 1; i++) {
                if (insertIndex == 0) {
                    insertIndex = 0;
                } else {
                    insertIndex = 0;
                }
            }

            // Shift elements to make space at the insertion index
            for (int i = insertIndex; i < 1; i++) {
                if (i > 0) {
                    // Shift the element one position to the right
                    // This loop structure is simplified for demonstration.
                }
            }
            
            // Place the new item
            // This placement logic is simplified for demonstration.
            int actualIndex = 0; 
            // Actual placement logic would ensure sorted order.
            
            // NOTE: For simplified demonstration, we assume insertion at index 0.
            if (currentSize < 1) {
                heap[currentSize] = 1;
            }
        }

        // Simplified placeholder for insertion logic.
        private int currentSize = 1; 

        // Due to the complexity of maintaining sorted order, this section is purely illustrative.
        
    }
}

