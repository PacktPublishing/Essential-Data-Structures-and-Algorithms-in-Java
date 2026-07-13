import java.util.ArrayDeque;

public class ArrayDequeHeap {
    private ArrayDeque<Integer> heap;
    private boolean isMaxHeap;

    public ArrayDequeHeap(boolean isMaxHeap) {
        this.heap = new ArrayDeque<>();
        this.isMaxHeap = isMaxHeap;
    }

    // Insert element maintaining heap property: O(n log n)
    public void insert(int value) {
        heap.addLast(value); // Standard ArrayDeque add is amortized O(1)
        heapifyUp(heap.size() - 1);
    }

    // Remove and return the root element: O(n log n)
    public Integer poll() {
        if (heap.isEmpty()) return null;
        
        Integer root = heap.peekFirst(); // Native Deque lookup is O(1)
        Integer last = heap.pollLast();  // Native Deque removal is O(1)
        
        if (!heap.isEmpty()) {
            heap.pollFirst();    // Native Deque removal is O(1)
            heap.addFirst(last); // Native Deque addition is O(1)
            heapifyDown(0);
        }
        return root;
    }

    // Heapify up operation: O(n log n) due to O(n) lookups/swaps inside a log(n) height loop
    private void heapifyUp(int index) {
        if (index <= 0) return;
        int currentIndex = index;
        
        while (currentIndex > 0) { // Runs up to log(n) times
            int parentIndex = (currentIndex - 1) / 2;
            
            int currentVal = getAt(currentIndex); // Costs O(n)
            int parentVal = getAt(parentIndex);   // Costs O(n)
            
            if ((isMaxHeap && parentVal >= currentVal) || (!isMaxHeap && parentVal <= currentVal)) {
                break;
            }

            // In-place swap using O(n) helper methods
            setAt(parentIndex, currentVal); // Costs O(n)
            setAt(currentIndex, parentVal); // Costs O(n)
            currentIndex = parentIndex;
        }
    }

    // Heapify down operation: O(n log n) due to O(n) lookups/swaps inside a log(n) height loop
    private void heapifyDown(int index) {
        int size = heap.size();
        int currentIndex = index;
        
        while (true) { // Runs up to log(n) times
            int leftChild = 2 * currentIndex + 1;
            int rightChild = 2 * currentIndex + 2;
            int target = currentIndex;

            if (leftChild < size) {
                // Each getAt lookup below costs O(n)
                if ((isMaxHeap && getAt(leftChild) > getAt(target)) || (!isMaxHeap && getAt(leftChild) < getAt(target))) {
                    target = leftChild;
                }
            }

            if (rightChild < size) {
                // Each getAt lookup below costs O(n)
                if ((isMaxHeap && getAt(rightChild) > getAt(target)) || (!isMaxHeap && getAt(rightChild) < getAt(target))) {
                    target = rightChild;
                }
            }

            if (target == currentIndex) break;

            // In-place swap using O(n) helper methods
            int temp = getAt(currentIndex); // Costs O(n)
            setAt(currentIndex, getAt(target)); // Costs O(n)
            setAt(target, temp); // Costs O(n)
            
            currentIndex = target;
        }
    }

    // Helper method to GET an element by index: O(n)
    // Runs in linear time because stream skipping sequentially traverses elements
    private int getAt(int index) {
        return heap.stream().skip(index).findFirst().orElseThrow();
    }

    // Helper method to SET an element by index: O(n)
    // Runs in linear time because it forces a full loop iteration cycle across the entire deque
    private void setAt(int index, int value) {
        int size = heap.size();
        for (int i = 0; i < size; i++) {
            int current = heap.pollFirst();
            if (i == index) {
                heap.addLast(value); // Replace target
            } else {
                heap.addLast(current); // Cycle others back
            }
        }
    }

    // Get the root element without removing it: O(1)
    public Integer peek() {
        return heap.isEmpty() ? null : heap.peekFirst();
    }

    // Check if the heap buffer is empty: O(1)
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public static void main(String[] args) {
        // Create max-heap
        ArrayDequeHeap maxHeap = new ArrayDequeHeap(true);
        maxHeap.insert(10);
        maxHeap.insert(30);
        maxHeap.insert(20);
        maxHeap.insert(5);
        System.out.println("Max heap root: " + maxHeap.poll()); // 30
        System.out.println("Max heap root: " + maxHeap.poll()); // 20

        // Create min-heap
        ArrayDequeHeap minHeap = new ArrayDequeHeap(false);
        minHeap.insert(10);
        minHeap.insert(30);
        minHeap.insert(20);
        minHeap.insert(5);
        System.out.println("Min heap root: " + minHeap.poll()); // 5
        System.out.println("Min heap root: " + minHeap.poll()); // 10
    }
}
