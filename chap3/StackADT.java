public interface StackADT<T> {  
    // Push an element onto the top of the stack
    void push(T element);

    // Remove the top element from the stack
    T pop();

    // Return a copy of the top element without removing it
    T peek();

    // Return the number of elements in the stack
    int size();

    // Return whether the stack has any elements in it
    boolean isEmpty();

}