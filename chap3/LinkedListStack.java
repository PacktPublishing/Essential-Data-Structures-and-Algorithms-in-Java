/**
 * Stack implementation using a singly linked list
 */
public class LinkedListStack<T> implements StackADT<T> {

    private Node<T> top = null;
    private int size = 0;

    // Push a new element onto the stack
    @Override
    public void push(T data) {
        Node<T> n = new Node<T>(data);
        n.next = top;
        top = n;
        size++;
    }

    // Remove the top element from the stack
    @Override
    public T pop() {
        if (isEmpty()) { System.out.println("Stack Underflow"); return null; }
        T v = top.data;
        top = (Node<T>) top.next;
        size--;
        return v;
    }

    // Return a copy of the top element without removing it
    @Override
    public T peek() {
         return isEmpty() ? null : top.data;
    }

    // Return the size of the stack
    @Override
    public int size() {
        return size;
    }

    // Return whether the stack has any elements in it
    @Override
    public boolean isEmpty() {
        return top == null;
    }
}

class Node<T> {
    T data;
    @SuppressWarnings("rawtypes")
    Node next;

    Node(T data) {
        this.data = data;
        this.next = null;
    }
}