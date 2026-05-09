/**
 * ============================================================
 * FILE: ImplicitExplicitStack.java
 *
 * CENTRAL THESIS:
 *   Recursion and an explicit stack (java.util.Deque) are
 *   computationally EQUIVALENT.  Every recursive algorithm can
 *   be mechanically rewritten to use a Deque instead.
 *
 *   The difference is not WHAT is computed — it is WHO OWNS
 *   the stack state:
 *
 *   Implicit (recursive):
 *     The JVM manages a fixed-size call stack automatically.
 *     Your code never sees it.  Overflow = unrecoverable crash.
 *
 *   Explicit (Deque):
 *     YOU push and pop work items onto a heap-allocated Deque.
 *     It is a plain Java object you can inspect, iterate,
 *     serialize, pause, or hand off to another thread.
 *
 * DEQUE VS STACK CLASS:
 *   java.util.Stack is a legacy class (extends Vector, synchronized).
 *   Always prefer Deque<T> implemented by ArrayDeque<T>:
 *     - ArrayDeque: faster (array-backed, no sync overhead)
 *     - LinkedList: useful when you also need a queue
 *   Key Deque methods used as a stack:
 *     push(e)    → addFirst(e)   — push to top (head)
 *     pop()      → removeFirst() — pop from top (head)
 *     peek()     → peekFirst()   — inspect top without removing
 *     isEmpty()  → check before every pop to avoid NoSuchElementException
 * ============================================================
 */

import java.util.ArrayDeque;
import java.util.Deque;

public class ImplicitExplicitStack {

    // ==========================================================
    // PART 1 — IMPLICIT STACK (RECURSIVE)
    //
    // The JVM call stack IS the stack. Every call to this method
    // pushes a frame containing:
    //   - local variable 'i'        (which element we are at)
    //   - the pending "+ a[i]"      (work deferred until unwind)
    //   - the return address        (where to go when we pop)
    //
    // You write clean, concise code. You pay with invisibility:
    // there is no way to peek at, log, pause, or save those frames.
    //
    // SPACE: O(n) on the JVM call stack.
    //   Limit: ~5,000–10,000 frames before StackOverflowError.
    // ==========================================================

    /**
     * Returns sum of a[i..a.length-1] using the JVM's implicit call stack.
     *
     * @param a  the array (heap-allocated, shared across all frames)
     * @param i  current index; call with 0 from main()
     */
    static int sumRecursive(int[] a, int i) {

        // BASE CASE — deepest frame, no more work to push.
        // Returns 0 (additive identity) to begin the unwind.
        // The JVM pops this frame and resumes the frame above.
        if (i == a.length)
            return 0;

        // RECURSIVE CASE — the JVM implicitly:
        //   1. Saves the current value of 'i' and the pending "+a[i]"
        //      into a new stack frame.
        //   2. Pushes the frame for sumRecursive(a, i+1).
        //   3. On return, pops THIS frame and evaluates a[i] + result.
        //
        // The pending addition lives invisibly in the JVM frame.
        // You cannot inspect it or recover it if the stack overflows.
        return a[i] + sumRecursive(a, i + 1);
    }


    // ==========================================================
    // PART 2 — EXPLICIT STACK (Deque)
    //
    // We replicate what the JVM does automatically, but using our
    // own Deque<Integer> allocated on the heap.
    //
    // The "work items" we push are the raw array values (integers).
    // For more complex problems the work item is a custom object
    // (see Part 3: StackFrame).
    //
    // PHASE 1 — PUSH: load all values onto the Deque.
    //   This mirrors the JVM pushing call frames on each recursion.
    //
    // PHASE 2 — POP: drain the Deque, accumulating the sum.
    //   This mirrors the JVM unwinding frames on the way back up.
    //
    // SPACE: O(n) on the HEAP (inside the Deque object).
    //   Heap limit is the whole JVM heap — millions of items
    //   before OutOfMemoryError, which is far safer than the
    //   tiny, fixed JVM call stack.
    // ==========================================================

    /**
     * Returns the sum of all elements in 'a' using an explicit Deque.
     * Prints each pop step so you can see the unwind in real time.
     *
     * @param a  the array to sum
     */
    static int sumExplicit(int[] a) {

        // ── Create the explicit stack ──────────────────────────
        // ArrayDeque is the recommended Deque implementation:
        //   - Array-backed (amortized O(1) push/pop)
        //   - Not synchronized (faster than legacy Stack class)
        //   - push() adds to head (index 0), pop() removes from head
        Deque<Integer> stack = new ArrayDeque<>();

        // ── PUSH PHASE — mirrors recursive descent ─────────────
        // We push each element's VALUE onto the Deque.
        // (In a recursive call, the JVM would push a frame
        //  containing 'i' and the pending '+ a[i]' instead.)
        //
        // Push order: a[0] first, a[n-1] last.
        // Because Deque.push() is LIFO (adds to head),
        // a[0] ends up at the BOTTOM and a[n-1] at the TOP.
        for (int i = 0; i < a.length; i++) {
            stack.push(a[i]);   // Deque.push() = addFirst()
        }

        // ── Demonstrate visibility — this is impossible with recursion ──
        // We can inspect the entire stack at any point.
        // The recursive version offers zero equivalent capability.
        System.out.println("  After pushes, stack top-to-bottom: " + stack);

        // ── POP PHASE — mirrors recursive unwind ───────────────
        // Pop items one by one and accumulate the sum.
        // Each pop() retrieves from the HEAD (LIFO order):
        //   first pop → a[0] (was pushed first, sank to bottom... wait,
        //   actually push adds to HEAD, so a[n-1] is at head after loop).
        //   The order of addition doesn't matter for a sum.
        int total = 0;
        while (!stack.isEmpty()) {
            // ALWAYS check isEmpty() before pop() to avoid
            // java.util.NoSuchElementException on an empty Deque.
            int value = stack.pop();  // Deque.pop() = removeFirst()
            total += value;
            System.out.printf("  pop %-4d → running total = %d%n",
                              value, total);
        }

        return total;
    }


    // ==========================================================
    // PART 3 — EXPLICIT DFS WITH A CUSTOM StackFrame OBJECT
    //
    // For problems where "push a raw int" is not enough
    // (e.g., tree traversal, graph DFS), the work item pushed
    // onto the Deque must carry whatever state the recursive
    // frame would have held implicitly.
    //
    // Here we traverse a tiny binary tree using an explicit Deque.
    // Each Deque entry is a TreeNode — exactly what a recursive
    // DFS frame would have held as its local variable.
    //
    // This pattern lets you:
    //   - Traverse millions of nodes without StackOverflowError.
    //   - Pause mid-traversal (just stop the while loop).
    //   - Serialize the Deque to disk and resume later.
    //   - Hand the Deque to a different thread.
    // ==========================================================

    /** Minimal binary tree node. */
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v, TreeNode l, TreeNode r) {
            val = v; left = l; right = r;
        }
    }

    /**
     * Recursive pre-order DFS: root → left → right.
     * Uses the implicit JVM call stack.
     */
    static void dfsRecursive(TreeNode node) {
        if (node == null) return;              // base case: null leaf
        System.out.println("  Visiting node " + node.val);
        dfsRecursive(node.left);              // push left frame implicitly
        dfsRecursive(node.right);             // push right frame implicitly
    }

    /**
     * Iterative pre-order DFS: root → left → right.
     * Uses an explicit Deque<TreeNode> instead of the JVM stack.
     *
     * KEY INSIGHT on push order:
     *   To visit LEFT before RIGHT in LIFO order, push RIGHT first.
     *   The last thing pushed is the first thing popped.
     *   So push right → push left → pop left first (correct order).
     */
    static void dfsExplicit(TreeNode root) {
        if (root == null) return;

        // The Deque holds TreeNode references — exactly what a
        // recursive DFS frame would hold as its local variable.
        // Instead of "call dfsRecursive(node)", we "push node".
        // Instead of "return from dfsRecursive", we "pop node".
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);   // Push the root to start

        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();   // "enter" this node

            // Process current node (pre-order: node before children)
            System.out.println("  Visiting node " + current.val);

            // Push RIGHT child first — it will be processed AFTER left.
            // (Deque is LIFO: last pushed = first popped.)
            // In the recursive version, the JVM would push the right
            // call frame after the left one completes.  Here we
            // simulate that by pushing right before left.
            if (current.right != null) stack.push(current.right);
            if (current.left  != null) stack.push(current.left);
        }
    }


    // ==========================================================
    // MAIN — run all three demonstrations
    // ==========================================================
    public static void main(String[] args) {

        int[] arr = {10, 20, 30, 40, 50};   // sum = 150

        // ── Part 1: Implicit stack ─────────────────────────────
        System.out.println("--- Implicit stack (recursive) ---");
        int rec = sumRecursive(arr, 0);
        System.out.printf("sumRecursive(%s, 0) = %d%n",
                          java.util.Arrays.toString(arr), rec);

        // ── Part 2: Explicit stack (Deque) ────────────────────
        System.out.println("\n--- Explicit stack (Deque) ---");
        int exp = sumExplicit(arr);
        System.out.printf("sumExplicit(%s)    = %d%n",
                          java.util.Arrays.toString(arr), exp);

        // ── Part 3: Tree DFS comparison ────────────────────────
        //   Build a small tree:
        //         1
        //        / \
        //       2   3
        //   Expected pre-order: 1, 2, 3
        TreeNode tree = new TreeNode(1,
                            new TreeNode(2, null, null),
                            new TreeNode(3, null, null));

        System.out.println("\n--- Recursive DFS (implicit stack) ---");
        dfsRecursive(tree);

        System.out.println("\n--- Explicit DFS (Deque) ---");
        dfsExplicit(tree);

        // ── Sanity check ───────────────────────────────────────
        System.out.printf("%nResults match: %b%n", rec == exp);

        // ── Deque API cheat-sheet demo ────────────────────────
        System.out.println("\n--- Deque API reference ---");
        Deque<String> demo = new ArrayDeque<>();
        demo.push("A");                              // addFirst — top of stack
        demo.push("B");
        demo.push("C");
        System.out.println("After push A,B,C: " + demo); // [C, B, A]
        System.out.println("peek() = " + demo.peek());   // C — no removal
        System.out.println("pop()  = " + demo.pop());    // C — removes top
        System.out.println("After pop:        " + demo); // [B, A]
        System.out.println("size() = " + demo.size());   // 2
    }
}
