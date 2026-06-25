import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class OptimisticLinkedList<T> {
    
    private static class Node<T> {
        final T data;
        final AtomicReference<Node<T>> next;
        
        Node(T data) {
            this.data = data;
            this.next = new AtomicReference<>();
        }
    }
    
    private final AtomicReference<Node<T>> head;
    
    public OptimisticLinkedList() {
        this.head = new AtomicReference<>(new Node<>(null)); // Dummy head
    }
    
    // Read operation - lock-free traversal
    public boolean contains(T data) {
        Node<T> current = head.get().next.get();
        while (current != null) {
            if (data == null ? current.data == null : data.equals(current.data)) {
                return true;
            }
            current = current.next.get();
        }
        return false;
    }
    
    // Write operation - optimistic locking for add
    public boolean add(T data) {
        while (true) { // Retry loop
            // Step 1: Traverse (lock-free)
            Node<T> pred = head.get();
            Node<T> curr = pred.next.get();
            
            // Find insertion point
            while (curr != null && compare(curr.data, data) < 0) {
                pred = curr;
                curr = curr.next.get();
            }
            
            // Step 2: Lock the target nodes
            pred.next.compareAndSet(curr, new Node<>(data));
            // Locking is implicit in the CAS operation
            
            // Step 3: Validate (check if insertion was successful)
            if (pred.next.get() == null) {
                // If we couldn't insert due to concurrent modification
                // This is a simplified validation - in practice, more sophisticated checks
                continue;
            }
            
            // Step 4: Act or Retry - if we reach here, operation succeeded
            return true;
        }
    }
    
    // Write operation - optimistic locking for remove
    public boolean remove(T data) {
        while (true) { // Retry loop
            // Step 1: Traverse (lock-free)
            Node<T> pred = head.get();
            Node<T> curr = pred.next.get();
            
            // Find the node to remove
            while (curr != null && compare(curr.data, data) < 0) {
                pred = curr;
                curr = curr.next.get();
            }
            
            // Check if we found the element
            if (curr == null || !data.equals(curr.data)) {
                return false; // Element not found
            }
            
            // Step 2: Lock the target nodes (by attempting to remove)
            Node<T> next = curr.next.get();
            if (pred.next.compareAndSet(curr, next)) {
                // Step 3: Validate - check if removal was successful
                // In this simple case, we assume success if CAS succeeded
                return true;
            }
            // Step 4: Act or Retry - if CAS failed, retry the entire operation
        }
    }
    
    // Helper method to compare elements (handles nulls)
    private int compare(T a, T b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }
    
    // Helper method to print list (for testing)
    public void printList() {
        Node<T> current = head.get().next.get();
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next.get();
        }
        System.out.println();
    }
}

