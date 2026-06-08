import java.util.Stack;

public class MinStack {
    // [2] Main stack holds all values
    private Stack<Integer> stack = new Stack<>();
    // [3] Min stack tracks current minimum at each depth
    private Stack<Integer> minStack = new Stack<>();

    // [4] Push value; update minStack if this is a new minimum
    public void push(int val) {
        stack.push(val);
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }

    // [5] Pop value; if it was the minimum, remove it from minStack too
    public void pop() {
        int val = stack.pop();
        if (val == minStack.peek()) {
            minStack.pop();
        }
    }

    // [6] Top of main stack
    public int top() {
        return stack.peek();
    }

    // [7] Top of min stack = current minimum — O(1)
    public int getMin() {
        return minStack.peek();
    }

    public static void main(String[] args) {
        MinStack ms = new MinStack();
        ms.push(5);   // stack=[5],    minStack=[5]
        ms.push(3);   // stack=[5,3],  minStack=[5,3]
        ms.push(7);   // stack=[5,3,7],minStack=[5,3]  — 7 not ≤ 3
        ms.push(2);   // stack=[5,3,7,2],minStack=[5,3,2]
        System.out.println(ms.getMin()); // 2
        ms.pop();      // remove 2; minStack pops 2 → minStack=[5,3]
        System.out.println(ms.getMin()); // 3
    }
}

