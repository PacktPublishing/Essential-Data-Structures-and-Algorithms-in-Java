/**
 * Queue implementation using a singly linked list
 */
public class LinkedListQueue<T> implements QueueADT<T> {

    private Node<T> head; 
    private Node<T> tail; 
    private int    size;  

    // Add an element to the back of the queue
    @Override
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (tail == null) {          
            head = tail = newNode;
        } else {
            tail.next = newNode;     
            tail = newNode;          
        }
        size++;
    }

    // Remove an element at the front of the queue
    @Override
    public T dequeue() {
        if (head == null) {
            throw new IllegalStateException("Queue is empty");
        }

        T result = head.value;
        head = head.next;            
        if (head == null) {          
            tail = null;
        }
        size--;
        return result;
    }

    // Return an element at the front without removing it
    @Override
    public T peek() {
        if (head == null) {
            throw new IllegalStateException("Queue is empty");
        }
        return head.value;
    }

    // Return the number of elements in the queue
    @Override
    public int size() { return size; }

    // Return true if the queue contains no elements
    @Override
    public boolean isEmpty() { return size == 0; }
}

class Node<T> {
    T data;
    Node next;

    Node(T data) {
        this.data = data;
        this.next = null;
    }
}