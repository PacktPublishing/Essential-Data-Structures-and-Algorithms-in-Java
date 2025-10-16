public interface QueueADT<T> {

    // Adds an element to the end of this queue
    void enqueue(T item);

    // Removes and returns the element at the front of this queue
    T dequeue();

    // Returns, but does not remove, the front of this queue
    T peek();

    // Returns the number of elements in this queue
    int size();
  
    // Returns whether the queue has any elements in it
    boolean isEmpty();
}