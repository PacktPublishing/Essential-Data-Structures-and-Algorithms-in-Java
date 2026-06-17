/**
 * Min-Heap implementation for processing network packets by timestamp
 */
public class MinHeap {
    private int[] heap;
    private int size;
    private int capacity;
    
    /**
     * Constructor to initialize the min-heap with given capacity
     * @param capacity maximum number of elements the heap can hold
     */
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.heap = new int[capacity + 1]; // Index 0 unused for easier heap operations
        this.size = 0;
    }
    
    /**
     * Insert a new element into the min-heap
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
     * Extract and remove the minimum element from the heap
     * @return the minimum element
     * @throws RuntimeException if heap is empty
     */
    public int extractMin() {
        if (isEmpty()) {
            throw new RuntimeException("Heap is empty");
        }
        
        int min = heap[1];
        
        // Move last element to root
        heap[1] = heap[size];
        size--;
        
        // Bubble down to maintain heap property
        if (size > 0) {
            heapifyDown(1);
        }
        
        return min;
    }
    
    /**
     * Get the minimum element without removing it
     * @return the minimum element
     * @throws RuntimeException if heap is empty
     */
    public int getMin() {
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
     * Bubble up operation to maintain min-heap property
     * @param index the index to start bubbling up from
     */
    private void heapifyUp(int index) {
        while (index > 1) {
            int parentIndex = index / 2;
            
            // If parent is smaller than or equal to child, we're done
            if (heap[parentIndex] <= heap[index]) {
                break;
            }
            
            // Swap parent and child
            swap(parentIndex, index);
            index = parentIndex;
        }
    }
    
    /**
     * Bubble down operation to maintain min-heap property
     * @param index the index to start bubbling down from
     */
    private void heapifyDown(int index) {
        while (true) {
            int leftChild = 2 * index;
            int rightChild = 2 * index + 1;
            int smallest = index;
            
            // Find the smallest among parent and children
            if (leftChild <= size && heap[leftChild] < heap[smallest]) {
                smallest = leftChild;
            }
            
            if (rightChild <= size && heap[rightChild] < heap[smallest]) {
                smallest = rightChild;
            }
            
            // If parent is already the smallest, we're done
            if (smallest == index) {
                break;
            }
            
            // Swap and continue bubbling down
            swap(index, smallest);
            index = smallest;
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
        // Test Case 1: insert(5), insert(3), insert(8), insert(1), extractMin()
        System.out.println("Test Case 1:");
        MinHeap minHeap1 = new MinHeap(10);
        minHeap1.insert(5);
        minHeap1.insert(3);
        minHeap1.insert(8);
        minHeap1.insert(1);
        int result1 = minHeap1.extractMin();
        System.out.println("Expected: 1, Got: " + result1);
        
        // Test Case 2: insert(10), insert(5), insert(15), insert(2), insert(7), extractMin(), extractMin()
        System.out.println("\nTest Case 2:");
        MinHeap minHeap2 = new MinHeap(10);
        minHeap2.insert(10);
        minHeap2.insert(5);
        minHeap2.insert(15);
        minHeap2.insert(2);
        minHeap2.insert(7);
        int result2 = minHeap2.extractMin();
        int result3 = minHeap2.extractMin();
        System.out.println("Expected: 2, 5, Got: " + result2 + ", " + result3);
        
        // Additional test: getMin without removing
        System.out.println("\nAdditional Test:");
        MinHeap minHeap3 = new MinHeap(5);
        minHeap3.insert(4);
        minHeap3.insert(2);
        minHeap3.insert(6);
        int min = minHeap3.getMin();
        System.out.println("GetMin should return 2, Got: " + min);
        minHeap3.extractMin();
        min = minHeap3.getMin();
        System.out.println("After extracting 2, GetMin should return 4, Got: " + min);
    }
}

