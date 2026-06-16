import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;

public final class ArrayDequeStack<T> implements StackADT<T> {
    private final Deque<T> backing = new ArrayDeque<>();

    @Override public void push(T item) {
        // ArrayDeque guarantees amortised O(1) growth.
        backing.addLast(item);
    }

    @Override public T pop() {
        if (backing.isEmpty()) throw new EmptyStackException();
        return backing.removeLast();
    }

    @Override public T peek() {
        if (backing.isEmpty()) throw new EmptyStackException();
        return backing.peekLast();
    }

    @Override public boolean isEmpty() { return backing.isEmpty(); }

    @Override public int size() { return backing.size(); }

    @Override
    public void resize() {
        throw new UnsupportedOperationException("Unimplemented method 'resize'");
    }
}
