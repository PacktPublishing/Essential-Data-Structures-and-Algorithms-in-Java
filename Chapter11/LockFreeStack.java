import java.util.concurrent.atomic.AtomicReference; 

public class LockFreeStack<T> { 
    // Node class for stack elements 
    private static class Node<T> { 
        final T data; 
        volatile Node<T> next; 

        Node(T data) { 
            this.data = data; 
        } 
    } 

    // Head pointer using AtomicReference 
    private final AtomicReference<Node<T>> head = new AtomicReference<>(); 

    // Push operation - lock-free using CAS 
    public void push(T data) { 
        Node<T> newNode = new Node<>(data); 
        Node<T> currentHead; 

        do { 
            currentHead = head.get(); // Read current head 
            newNode.next = currentHead; // Link new node to current head 
        } while (!head.compareAndSet(currentHead, newNode));  
    } 

    // Pop operation - lock-free using CAS 
    public T pop() { 
        Node<T> currentHead; 
        Node<T> newHead; 

        do { 
            currentHead = head.get(); // Read current head 
            if (currentHead == null) { 
                return null; // Stack is empty 
            } 
            newHead = currentHead.next; // New head will be next node 
        } while (!head.compareAndSet(currentHead, newHead)); 

        return currentHead.data; // Return popped data 
    } 
}

