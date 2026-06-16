import java.util.Stack;

public class QueueUsingTwoStacks {
    // [1] inbox: receives all new elements
    private Stack<Integer> inbox = new Stack<>();
    // [2] outbox: serves dequeue/peek in FIFO order
    private Stack<Integer> outbox = new Stack<>();

    // [3] Always push to inbox — O(1)
    public void enqueue(int val) {
        inbox.push(val);
    }

    // [4] Transfer inbox→outbox only when outbox is empty
    //     This gives amortized O(1) per operation
    public int dequeue() {
        transferIfNeeded();
        return outbox.pop();
    }

    public int peek() {
        transferIfNeeded();
        return outbox.peek();
    }

    public boolean isEmpty() {
        return inbox.isEmpty() && outbox.isEmpty();
    }

    // [5] Reverse inbox into outbox — restores FIFO order
    private void transferIfNeeded() {
        if (outbox.isEmpty()) {
            while (!inbox.isEmpty()) {
                outbox.push(inbox.pop());
            }
        }
    }

    public static void main(String[] args) {
        QueueUsingTwoStacks q = new QueueUsingTwoStacks();
        q.enqueue(1); q.enqueue(2); q.enqueue(3);
        // inbox=[1,2,3], outbox=[]
        System.out.println(q.dequeue()); // transfer → outbox=[3,2,1], pop → 1
        q.enqueue(4);
        // inbox=[4], outbox=[3,2]
        System.out.println(q.dequeue()); // outbox not empty, pop → 2
        System.out.println(q.dequeue()); // pop → 3
        System.out.println(q.dequeue()); // transfer inbox→outbox, pop → 4
    }
}

