import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;

public class ArrayDequeHeap {
    private ArrayDeque<Integer> heap;
    private boolean isMaxHeap;
    
    public ArrayDequeHeap(boolean isMaxHeap) {
        this.heap = new ArrayDeque<>();
        this.isMaxHeap = isMaxHeap;
    }
    
    // Insert element maintaining heap property
    public void insert(int value) {
        heap.add(value);
        heapifyUp(heap.size() - 1);
    }
    
    // Remove and return the root element
    public Integer poll() {
        if (heap.isEmpty()) return null;
        
        Integer root = heap.pollFirst();
        if (!heap.isEmpty()) {
            Integer last = heap.pollLast();
            heap.addFirst(last);
            heapifyDown(0);
        }
        return root;
    }
    
    // Heapify up operation to maintain heap property
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            int parent = heap.get(parentIndex);
            int current = heap.get(index);
            
            if ((isMaxHeap && parent >= current) || (!isMaxHeap && parent <= current)) {
                break;
            }
            
            // Swap parent and current
            heap.set(parentIndex, current);
            heap.set(index, parent);
            index = parentIndex;
        }
    }
    
    // Heapify down operation to maintain heap property
    private void heapifyDown(int index) {
        int size = heap.size();
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int target = index;
            
            if (leftChild < size) {
                int leftValue = heap.get(leftChild);
                int currentIndexValue = heap.get(target);
                if ((isMaxHeap && leftValue > currentIndexValue) || 
                    (!isMaxHeap && leftValue < currentIndexValue)) {
                    target = leftChild;
                }
            }
            
            if (rightChild < size) {
                int rightValue = heap.get(rightChild);
                int currentIndexValue = heap.get(target);
                if ((isMaxHeap && rightValue > currentIndexValue) || 
                    (!isMaxHeap && rightValue < currentIndexValue)) {
                    target = rightChild;
                }
            }
            
            if (target == index) break;
            
            // Swap
            int temp = heap.get(index);
            heap.set(index, heap.get(target));
            heap.set(target, temp);
            index = target;
        }
    }
    
    // Get the root element without removing it
    public Integer peek() {
        return heap.isEmpty() ? null : heap.getFirst();
    }
    
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

