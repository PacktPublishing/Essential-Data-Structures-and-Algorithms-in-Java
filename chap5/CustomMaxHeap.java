import java.util.ArrayList;
import java.util.List;

/**
 * Custom Max-Heap Implementation
 * 
 * Demonstrates how heaps work internally using array-based storage.
 * This implementation shows the mechanics that PriorityQueue hides.
 * 
 * Key heap properties:
 * 1. Complete binary tree (filled left-to-right, level-by-level)
 * 2. Heap invariant: parent >= children (for max-heap)
 * 
 * Array indexing for node at index i:
 * - Parent: (i - 1) / 2
 * - Left child: 2 * i + 1
 * - Right child: 2 * i + 2
 */
public class CustomMaxHeap {
    
    // Internal storage: ArrayList provides dynamic resizing
    // Index 0 is the root (maximum element)
    private List<Integer> heap;
    
    public CustomMaxHeap() {
        this.heap = new ArrayList<>();
    }
    
    /**
     * Returns the number of elements in the heap
     * Time: O(1)
     */
    public int size() {
        return heap.size();
    }
    
    /**
     * Checks if heap is empty
     * Time: O(1)
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    /**
     * Returns maximum element without removing it
     * Time: O(1)
     */
    public Integer peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);  // Root is always at index 0
    }
    
    /**
     * Inserts a new element into the heap
     * Time: O(log n) - height of the tree
     * 
     * Algorithm:
     * 1. Add element at the end (maintains complete tree property)
     * 2. Bubble up to restore heap invariant
     */
    public void insert(int value) {
        // Step 1: Add to end of array (bottom-right of tree)
        heap.add(value);
        
        // Step 2: Bubble up from last position
        int currentIndex = heap.size() - 1;
        bubbleUp(currentIndex);
        
        System.out.printf("Inserted %d | Heap size: %d%n", value, size());
    }
    
    /**
     * Removes and returns the maximum element (root)
     * Time: O(log n)
     * 
     * Algorithm:
     * 1. Save root value to return
     * 2. Move last element to root position
     * 3. Remove last element
     * 4. Bubble down from root to restore heap invariant
     */
    public int extractMax() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        
        // Step 1: Save the maximum value (root)
        int max = heap.get(0);
        
        // Step 2: Move last element to root
        int lastIndex = heap.size() - 1;
        heap.set(0, heap.get(lastIndex));
        
        // Step 3: Remove the last element
        heap.remove(lastIndex);
        
        // Step 4: Restore heap property by bubbling down from root
        if (!isEmpty()) {
            bubbleDown(0);
        }
        
        System.out.printf("Extracted max: %d | Heap size: %d%n", max, size());
        return max;
    }
    
    /**
     * Bubble up: Restore heap property by moving element upward
     * 
     * Keep swapping with parent while current element > parent
     * Stops when: heap property satisfied OR reached root
     */
    private void bubbleUp(int index) {
        int current = index;
        
        // Continue while not at root and current > parent
        while (current > 0) {
            int parentIndex = getParent(current);
            
            // If current element <= parent, heap property is satisfied
            if (heap.get(current) <= heap.get(parentIndex)) {
                break;
            }
            
            // Swap current with parent
            swap(current, parentIndex);
            
            // Move up to parent position
            current = parentIndex;
        }
    }
    
    /**
     * Bubble down: Restore heap property by moving element downward
     * 
     * Keep swapping with larger child while current element < a child
     * Stops when: heap property satisfied OR reached leaf node
     */
    private void bubbleDown(int index) {
        int current = index;
        
        while (true) {
            int leftChild = getLeftChild(current);
            int rightChild = getRightChild(current);
            int largest = current;
            
            // Check if left child exists and is larger
            if (leftChild < heap.size() && heap.get(leftChild) > heap.get(largest)) {
                largest = leftChild;
            }
            
            // Check if right child exists and is larger
            if (rightChild < heap.size() && heap.get(rightChild) > heap.get(largest)) {
                largest = rightChild;
            }
            
            // If current is already largest, heap property is satisfied
            if (largest == current) {
                break;
            }
            
            // Swap current with largest child
            swap(current, largest);
            
            // Move down to largest child's position
            current = largest;
        }
    }
    
    /**
     * Helper: Calculate parent index
     * For any node at index i, parent is at (i - 1) / 2
     */
    private int getParent(int index) {
        return (index - 1) / 2;
    }
    
    /**
     * Helper: Calculate left child index
     * For any node at index i, left child is at 2*i + 1
     */
    private int getLeftChild(int index) {
        return 2 * index + 1;
    }
    
    /**
     * Helper: Calculate right child index
     * For any node at index i, right child is at 2*i + 2
     */
    private int getRightChild(int index) {
        return 2 * index + 2;
    }
    
    /**
     * Helper: Swap two elements in the heap
     */
    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    /**
     * Visualization helper: Print heap structure
     * Shows both array representation and tree structure
     */
    public void printHeap() {
        if (isEmpty()) {
            System.out.println("Heap is empty");
            return;
        }
        
        System.out.println("\n📦 Array representation:");
        System.out.println("   " + heap);
        
        System.out.println("\n🌳 Tree structure:");
        printTree(0, 0);
        System.out.println();
    }
    
    /**
     * Recursive helper for tree visualization
     */
    private void printTree(int index, int level) {
        if (index >= heap.size()) {
            return;
        }
        
        // Print right subtree first (so it appears on top when rotated)
        printTree(getRightChild(index), level + 1);
        
        // Print current node with indentation
        System.out.println("   ".repeat(level) + "└─ " + heap.get(index));
        
        // Print left subtree
        printTree(getLeftChild(index), level + 1);
    }
    
    /**
     * Demonstration of heap operations
     */
    public static void main(String[] args) {
        System.out.println("CUSTOM MAX-HEAP IMPLEMENTATION");
        System.out.println("=".repeat(50));
        
        CustomMaxHeap heap = new CustomMaxHeap();
        
        // Insert elements
        System.out.println("\n1️⃣  INSERTION (Bubble Up)");
        System.out.println("-".repeat(50));
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        
        for (int value : values) {
            heap.insert(value);
        }
        
        heap.printHeap();
        
        // Peek at maximum
        System.out.println("\n2️⃣  PEEK OPERATION");
        System.out.println("-".repeat(50));
        System.out.println("Maximum element: " + heap.peek());
        System.out.println("Heap size: " + heap.size());
        
        // Extract maximum
        System.out.println("\n3️⃣  EXTRACTION (Bubble Down)");
        System.out.println("-".repeat(50));
        
        heap.extractMax();
        heap.printHeap();
        
        heap.extractMax();
        heap.printHeap();
        
        // Insert more elements
        System.out.println("\n4️⃣  MORE INSERTIONS");
        System.out.println("-".repeat(50));
        heap.insert(90);
        heap.insert(65);
        heap.printHeap();
        
        // Extract all in descending order
        System.out.println("\n5️⃣  HEAP SORT (Extract All)");
        System.out.println("-".repeat(50));
        System.out.print("Sorted descending: ");
        while (!heap.isEmpty()) {
            System.out.print(heap.extractMax() + " ");
        }
        System.out.println("\n");
    }
}

