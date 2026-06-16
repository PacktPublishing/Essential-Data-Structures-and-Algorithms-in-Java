/**
 * Max-Heap implementation for task scheduling by priority score
 */
public class MaxHeap {
    private int[] heap;
    private int size;
    private int capacity;
    
    /**
     * Constructor to initialize the max-heap with given capacity
     * @param capacity maximum number of elements the heap can hold
     */
    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.heap = new int[capacity + 1]; // Index 0 unused for easier heap operations
        this.size = 0;
    }
    
    /**
     * Insert a new element into the max-heap
     * @param element the element to be inserted
     */
    public void insert(int element) {
        if (size >= capacity) {
            throw new RuntimeException("Heap is full");
        }
        
        // Insert at the end
        size++;
        heap[size] = element;
        
        // Bubble up to maintain heap property
        heapifyUp(size);
    }
    
    /**
     * Extract and remove the maximum element from the heap
     * @return the maximum element
     * @throws RuntimeException if heap is empty
     */
    public int extractMax() {
        if (isEmpty()) {
            throw new RuntimeException("Heap is empty");
        }
        
        int max = heap[1];
        
        // Move last element to root
        heap[1] = heap[size];
        size--;
        
        // Bubble down to maintain heap property
        if (size > 0) {
            heapifyDown(1);
        }
        
        return max;
    }
    
    /**
     * Get the maximum element without removing it
     * @return the maximum element
     * @throws RuntimeException if heap is empty
     */
    public int getMax() {
        if (isEmpty()) {
            throw new RuntimeException("Heap is empty");
        }
        return heap[1];
    }
    
    /**
     * Check if the heap is empty
     * @return true if heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Bubble up operation to maintain max-heap property
     * @param index the index to start bubbling up from
     */
    private void heapifyUp(int index) {
        while (index > 1) {
            int parentIndex = index / 2;
            
            // If parent is greater than or equal to child, we're done
            if (heap[parentIndex] >= heap[index]) {
                break;
            }
            
            // Swap parent and child
            swap(parentIndex, index);
            index = parentIndex;
        }
    }
    
    /**
     * Bubble down operation to maintain max-heap property
     * @param index the index to start bubbling down from
     */
    private void heapifyDown(int index) {
        while (true) {
            int leftChild = 2 * index;
            int rightChild = 2 * index + 1;
            int largest = index;
            
            // Find the largest among parent and children
            if (leftChild <= size && heap[leftChild] > heap[largest]) {
                largest = leftChild;
            }
            
            if (rightChild <= size && heap[rightChild] > heap[largest]) {
                largest = rightChild;
            }
            
            // If parent is already the largest, we're done
            if (largest == index) {
                break;
            }
            
            // Swap and continue bubbling down
            swap(index, largest);
            index = largest;
        }
    }
    
    /**
     * Swap two elements in the heap
     * @param i first index
     * @param j second index
     */
    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    /**
     * Main method with test cases
     */
    public static void main(String[] args) {
        // Test Case 1: insert(5), insert(3), insert(8), insert(1), extractMax()
        System.out.println("Test Case 1:");
        MaxHeap maxHeap1 = new MaxHeap(10);
        maxHeap1.insert(5);
        maxHeap1.insert(3);
        maxHeap1.insert(8);
        maxHeap1.insert(1);
        int result1 = maxHeap1.extractMax();
        System.out.println("Expected: 8, Got: " + result1);
        
        // Test Case 2: insert(10), insert(5), insert(15), insert(2), insert(7), extractMax(), extractMax()
        System.out.println("\nTest Case 2:");
        MaxHeap maxHeap2 = new MaxHeap(10);
        maxHeap2.insert(10);
        maxHeap2.insert(5);
        maxHeap2.insert(15);
        maxHeap2.insert(2);
        maxHeap2.insert(7);
        int result2 = maxHeap2.extractMax();
        int result3 = maxHeap2.extractMax();
        System.out.println("Expected: 15, 10, Got: " + result2 + ", " + result3);
        
        // Additional test: getMax without removing
        System.out.println("\nAdditional Test:");
        MaxHeap maxHeap3 = new MaxHeap(5);
        maxHeap3.insert(4);
        maxHeap3.insert(2);
        maxHeap3.insert(6);
        int max = maxHeap3.getMax();
        System.out.println("GetMax should return 6, Got: " + max);
        maxHeap3.extractMax();
        max = maxHeap3.getMax();
        System.out.println("After extracting 6, GetMax should return 4, Got: " + max);
    }
}

