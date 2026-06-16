import java.util.*;

// Binomial Heap Node
class BinomialNode {
    int key;
    int degree;
    BinomialNode child;
    BinomialNode sibling;
    BinomialNode parent;
    
    public BinomialNode(int key) {
        this.key = key;
        this.degree = 0;
        this.child = null;
        this.sibling = null;
        this.parent = null;
    }
}

// Binomial Heap
public class BinomialHeap {
    private BinomialNode head;
    
    public BinomialHeap() {
        this.head = null;
    }
    
    // Insert a new element
    public void insert(int key) {
        BinomialNode newNode = new BinomialNode(key);
        BinomialHeap newHeap = new BinomialHeap();
        newHeap.head = newNode;
        this.head = union(this.head, newHeap.head);
    }
    
    // Union two binomial heaps
    private BinomialNode union(BinomialNode head1, BinomialNode head2) {
        if (head1 == null) return head2;
        if (head2 == null) return head1;
        
        BinomialNode newHead = merge(head1, head2);
        BinomialNode prev = null;
        BinomialNode current = newHead;
        BinomialNode next = current.sibling;
        
        while (next != null) {
            if (current.degree != next.degree || 
                (next.sibling != null && next.sibling.degree == current.degree)) {
                prev = current;
                current = next;
            } else {
                if (current.key <= next.key) {
                    current.sibling = next.sibling;
                    link(next, current);
                } else {
                    if (prev == null) {
                        newHead = next;
                    } else {
                        prev.sibling = next;
                    }
                    link(current, next);
                    current = next;
                }
            }
            next = current.sibling;
        }
        return newHead;
    }
    
    // Merge two binomial heaps
    private BinomialNode merge(BinomialNode head1, BinomialNode head2) {
        BinomialNode newHead = null;
        BinomialNode current = null;
        
        while (head1 != null && head2 != null) {
            if (head1.degree <= head2.degree) {
                if (newHead == null) {
                    newHead = head1;
                    current = head1;
                } else {
                    current.sibling = head1;
                    current = head1;
                }
                head1 = head1.sibling;
            } else {
                if (newHead == null) {
                    newHead = head2;
                    current = head2;
                } else {
                    current.sibling = head2;
                    current = head2;
                }
                head2 = head2.sibling;
            }
        }
        
        if (head1 != null) {
            current.sibling = head1;
        } else {
            current.sibling = head2;
        }
        
        return newHead;
    }
    
    // Link two trees
    private void link(BinomialNode child, BinomialNode parent) {
        child.sibling = parent.child;
        parent.child = child;
        child.parent = parent;
        parent.degree++;
    }
    
    // Find minimum element
    public int findMin() {
        if (head == null) return Integer.MAX_VALUE;
        
        BinomialNode minNode = head;
        BinomialNode current = head;
        
        while (current != null) {
            if (current.key < minNode.key) {
                minNode = current;
            }
            current = current.sibling;
        }
        
        return minNode.key;
    }
    
    // Extract minimum element
    public int extractMin() {
        if (head == null) return Integer.MAX_VALUE;
        
        // Find minimum node
        BinomialNode minNode = head;
        BinomialNode prev = null;
        BinomialNode current = head;
        
        while (current != null) {
            if (current.key < minNode.key) {
                minNode = current;
                prev = current;
            }
            current = current.sibling;
        }
        
        // Remove minimum node from root list
        if (prev == null) {
            head = head.sibling;
        } else {
            prev.sibling = minNode.sibling;
        }
        
        // Create a new heap from children of min node
        BinomialHeap newHeap = new BinomialHeap();
        newHeap.head = minNode.child;
        
        // Reverse the child list
        BinomialNode child = null;
        BinomialNode next = null;
        BinomialNode currentChild = newHeap.head;
        
        while (currentChild != null) {
            next = currentChild.sibling;
            currentChild.sibling = child;
            child = currentChild;
            currentChild = next;
        }
        
        newHeap.head = child;
        
        // Union with original heap
        head = union(head, newHeap.head);
        
        return minNode.key;
    }
    
    // Display heap structure
    public void display() {
        if (head == null) {
            System.out.println("Empty heap");
            return;
        }
        
        System.out.print("Binomial Heap: ");
        BinomialNode current = head;
        while (current != null) {
            System.out.print(current.key + "(" + current.degree + ") ");
            current = current.sibling;
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.out.println("=== Binomial Heap Demo ===");
        
        BinomialHeap heap = new BinomialHeap();
        
        // Insert elements
        System.out.println("Inserting elements: 10, 20, 5, 15, 30");
        heap.insert(10);
        heap.insert(20);
        heap.insert(5);
        heap.insert(15);
        heap.insert(30);
        
        heap.display();
        System.out.println("Minimum element: " + heap.findMin());
        
        // Extract minimum
        System.out.println("\nExtracting minimum elements:");
        System.out.println("Extracted: " + heap.extractMin()); // 5
        System.out.println("Extracted: " + heap.extractMin()); // 10
        System.out.println("Extracted: " + heap.extractMin()); // 15
        
        heap.display();
        System.out.println("Minimum element: " + heap.findMin());
    }
}

