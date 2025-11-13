/**
 * A generic FIFO queue backed by a resizable circular array.
 */
public class DynamicArrayQueue<E> {

    private Object[] data;  
    private int head;       
    private int tail;       
    private int size;      

    // Initialize user-defined constructor with a given initial capacity
    public DynamicArrayQueue(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be positive");
        data = new Object[capacity];
    }

    // Add an element to the back of the queue
    public void enqueue(E e) {
        if (size == data.length)
            resize();
        data[tail] = e;
        tail = (tail + 1) % data.length;
        size++;
    }

    // Remove an element at the front of the queue
    public E dequeue() {
        if (size == 0) throw new IllegalStateException("Queue is empty");

        E result = (E) data[head];
        data[head] = null;            
        head = (head + 1) % data.length;
        size--;
        return result;
    }

    // Return an element at the front without removing it
    public E peek() {
        if (size == 0) throw new IllegalStateException("Queue is empty");
        return (E) data[head];
    }

    // Return true if the queue contains no elements
    public boolean isEmpty() { return size == 0; }

    // Return the number of elements in the queue
    public int size() { return size; }

    // Resize the array when it is at capacity
    private void resize() {
        // Recompute a new data size that is twice the original using
        // the signed left-shift operator
        int newCapacity = data.length << 1;
        Object[] newData = new Object[newCapacity];

        if (head < tail) {
            System.arraycopy(data, head, newData, 0, size);
        } else {
            int rightPart = data.length - head;
            System.arraycopy(data, head, newData, 0, rightPart);
            System.arraycopy(data, 0, newData, rightPart, tail);
        }

        data = newData;
        head = 0;
        tail = size;
    }
}
