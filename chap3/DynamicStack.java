import java.util.Arrays;

/**
 * A generic stack implemented with a dynamic array buffer.
 */
public class DynamicStack<T> implements StackADT<T> {

    private Object[] data;     
    private int size;

    // Initial user-defined construction
    public DynamicStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Stack capacity must be > 0");
        }
        data = new Object[capacity];
        size = 0;
    }

    // Push an element onto the top of the stack
    @Override
    public void push(T element) {
        resize(); // expand if needed
        data[size++] = element;
    }

    // Remove the top element from the stack
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot pop from an empty stack");
        }
        T element = (T) data[--size];
        return element;
    }

    // Return a copy of the top element without removing it
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot peek on an empty stack");
        }
        return (T) data[size - 1];
    }

    // Return the number of elements in the stack
    @Override
    public int size() { return size; }

    // Return whether the stack has any elements in it
    @Override
    public boolean isEmpty() { return size == 0; }

    // Resize the stack when it is at capacity only, to keep push/pop operations
    // O(1) amortized
    public void resize() {
        if (size == data.length) {
            // array is full, double the size, and copy over
            int newCapacity = data.length * 2;
            data = Arrays.copyOf(data, newCapacity);
        }
    }
}