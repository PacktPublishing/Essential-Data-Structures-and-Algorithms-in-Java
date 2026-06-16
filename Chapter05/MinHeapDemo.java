import java.util.Arrays;
import java.util.NoSuchElementException;

public class MinHeapDemo {
    private int[] heap;
    private int size;
    private int capacity;

    public MinHeapDemo(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;
        this.heap = new int[capacity];
    }

    public void insert(int value) {
        if (size == capacity) {
            resize();
        }
        heap[size] = value;
        size++;
        heapifyUp(size - 1);
    }

    public int removeMin() {
        // Step 1: If heap is empty, return error
        if (size == 0) {
            throw new NoSuchElementException("Heap is empty!");
        }

        // Step 2: Save root (minimum)
        int minValue = heap[0];

        // Step 3 & 4: Move last element to root and decrease size
        heap[0] = heap[size - 1];
        size--;

        // Step 5: Restore heap property by moving down
        heapifyDown(0);

        // Step 6: Return minValue
        return minValue;
    }

    // Step 5 implementation: Heapify Down
    private void heapifyDown(int index) {
        int i = index;

        // While i has at least one child (the left child must exist)
        while (leftChildIndex(i) < size) {
            int leftChild = leftChildIndex(i);
            int rightChild = rightChildIndex(i);
            
            // Assume the left child is the smaller child first
            int smallerChild = leftChild;
            
            // If right child exists and is smaller than left child, update smallerChild
            if (rightChild < size && heap[rightChild] < heap[leftChild]) {
                smallerChild = rightChild;
            }

            // If parent <= smaller child, done (heap property satisfied)
            if (heap[i] <= heap[smallerChild]) {
                break;
            }

            // Swap parent with smaller child
            swap(i, smallerChild);

            // Move down to the smaller child's position
            i = smallerChild;
        }
    }

    // --- HELPER METHODS ---

    private int leftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int rightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private void heapifyUp(int index) {
        int i = index;
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i] >= heap[parent]) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
    }

    private void resize() {
        capacity = capacity * 2;
        heap = Arrays.copyOf(heap, capacity);
    }

    private void swap(int index1, int index2) {
        int temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    public void printHeap() {
        System.out.println(Arrays.toString(Arrays.copyOf(heap, size)));
    }

    // --- MAIN METHOD TO TEST ---
    public static void main(String[] args) {
        MinHeapDemo minHeap = new MinHeapDemo(10);

        minHeap.insert(15);
        minHeap.insert(10);
        minHeap.insert(20);
        minHeap.insert(5);
        minHeap.insert(3);

        System.out.print("Initial Heap: ");
        minHeap.printHeap(); // Root should be 3

        System.out.println("Removed Min: " + minHeap.removeMin()); // Expected: 3
        System.out.print("Heap after 1st removal: ");
        minHeap.printHeap();

        System.out.println("Removed Min: " + minHeap.removeMin()); // Expected: 5
        System.out.print("Heap after 2nd removal: ");
        minHeap.printHeap();
    }
}

