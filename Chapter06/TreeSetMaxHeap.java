import java.util.Collections;
import java.util.TreeSet;

public class TreeSetMaxHeap {
    private TreeSet<Integer> maxHeap;
    
    public TreeSetMaxHeap() {
        // Create TreeSet with reverse order to simulate max-heap behavior
        this.maxHeap = new TreeSet<>(Collections.reverseOrder());
    }
    
    // Insert element - O(log n)
    public void insert(int value) {
        maxHeap.add(value);
    }
    
    // Remove and return maximum element - O(log n)
    public Integer poll() {
        if (maxHeap.isEmpty()) {
            return null;
        }
        Integer max = maxHeap.first(); // Get maximum element
        maxHeap.remove(max); // Remove it - O(log n)
        return max;
    }
    
    // Peek at maximum element without removing - O(1)
    public Integer peek() {
        return maxHeap.isEmpty() ? null : maxHeap.first();
    }
    
    // Check if heap is empty
    public boolean isEmpty() {
        return maxHeap.isEmpty();
    }
    
    // Get size of heap
    public int size() {
        return maxHeap.size();
    }
    
    public static void main(String[] args) {
        TreeSetMaxHeap heap = new TreeSetMaxHeap();
        
        // Insert elements
        heap.insert(10);
        heap.insert(30);
        heap.insert(20);
        heap.insert(5);
        
        // Test heap operations
        System.out.println("Max element: " + heap.peek()); // 30
        System.out.println("Removed: " + heap.poll()); // 30
        System.out.println("Max element: " + heap.peek()); // 20
        System.out.println("Removed: " + heap.poll()); // 20
        System.out.println("Max element: " + heap.poll()); // 10
        System.out.println("Removed: " + heap.poll()); // 5
    }
}

