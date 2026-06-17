import java.util.*;

// -------------------------------------------------------
// 1. Node definition
// -------------------------------------------------------
class FibNode {
    int key;          // value of the node
    int degree;       // number of children
    boolean mark;     // true if node has lost a child
    FibNode parent;
    FibNode child;    // one of the children (singly‑linked list)
    FibNode left;     // left sibling
    FibNode right;    // right sibling

    FibNode(int key) {
        this.key = key;
        this.degree = 0;
        this.mark = false;
        this.parent = this.child = this.left = this.right = this;
    }
}

// -------------------------------------------------------
// 2. Fibonacci Heap
// -------------------------------------------------------
class FibonacciHeap {
    private FibNode min = null;   // pointer to minimum node
    private int n = 0;            // number of nodes

    // ---------- basic list operations ----------
    private void addToRootList(FibNode node) {
        if (min == null) {
            min = node;
        } else {
            node.right = min.right;
            node.right.left = node;
            node.left = min;
            min.right = node;
            if (node.key < min.key) min = node;
        }
    }

    private void removeFromRootList(FibNode node) {
        if (node == node.right) {          // only node
            min = null;
        } else {
            node.left.right = node.right;
            node.right.left = node.left;
            if (min == node) min = node.right;
        }
    }

    // ---------- public operations ----------
    public void insert(int key) {
        FibNode node = new FibNode(key);
        addToRootList(node);
        n++;
    }

    public int findMin() {
        return min != null ? min.key : Integer.MAX_VALUE;
    }

    public int extractMin() {
        if (min == null) return Integer.MAX_VALUE;

        FibNode z = min;

        /* add children of z to root list */
        if (z.child != null) {
            List<FibNode> children = new ArrayList<>();
            FibNode x = z.child;
            do {
                children.add(x);
                x = x.right;
            } while (x != z.child);

            for (FibNode xNode : children) {
                xNode.parent = null;
                addToRootList(xNode);
            }
        }

        /* remove z from root list */
        removeFromRootList(z);
        n--;

        /* consolidate trees */
        if (min != null) {
            int arraySize = ((int) Math.floor(Math.log(n) / Math.log(2))) + 1;
            FibNode[] A = new FibNode[arraySize];

            List<FibNode> roots = new ArrayList<>();
            FibNode w = min;
            do {
                roots.add(w);
                w = w.right;
            } while (w != min);

            for (FibNode wNode : roots) {
                FibNode xNode = wNode;
                int d = xNode.degree;
                while (A[d] != null) {
                    FibNode y = A[d];
                    if (xNode.key > y.key) {
                        FibNode tmp = xNode;
                        xNode = y;
                        y = tmp;
                    }
                    link(y, xNode);            // make y child of xNode
                    A[d] = null;
                    d++;
                }
                A[d] = xNode;
            }

            /* recompute min */
            min = null;
            for (FibNode a : A) if (a != null) {
                if (min == null || a.key < min.key) min = a;
            }
        }

        return z.key;
    }

    public void decreaseKey(FibNode x, int newKey) {
        if (newKey > x.key) throw new IllegalArgumentException("new key is larger");
        x.key = newKey;
        FibNode y = x.parent;
        if (y != null && x.key < y.key) {
            cut(x, y);
            cascadingCut(y);
        }
        if (x.key < min.key) min = x;
    }

    public void delete(FibNode x) {
        decreaseKey(x, Integer.MIN_VALUE);
        extractMin();
    }

    // ---------- helper functions ----------
    private void link(FibNode y, FibNode x) {   // make y child of x
        /* remove y from root list */
        y.left.right = y.right;
        y.right.left = y.left;

        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.right = y.left = y;
        } else {
            y.right = x.child.right;
            y.right.left = y;
            y.left = x.child;
            x.child.right = y;
        }
        x.degree++;
        y.mark = false;
    }

    private void cut(FibNode x, FibNode y) {
        /* remove x from child list of y */
        if (x.right == x) {
            y.child = null;
        } else {
            x.right.left = x.left;
            x.left.right = x.right;
            if (y.child == x) y.child = x.right;
        }
        y.degree--;

        /* add x to root list */
        addToRootList(x);
        x.parent = null;
        x.mark = false;
    }

    private void cascadingCut(FibNode y) {
        FibNode z = y.parent;
        if (z != null) {
            if (!y.mark) y.mark = true;
            else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

    // For demo purposes – returns the node with a given key
    public FibNode findNode(int key) {
        FibNode cur = min;
        if (cur == null) return null;
        do {
            if (cur.key == key) return cur;
            cur = cur.right;
        } while (cur != min);
        return null;
    }

    // -------------------------------------------------
    // 3. Demo
    // -------------------------------------------------
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();

        System.out.println("=== Fibonacci Heap Demo ===");

        // insert some values
        int[] values = { 10, 3, 7, 21, 5, 1, 12 };
        System.out.println("Insert values: " + Arrays.toString(values));
        for (int v : values) heap.insert(v);

        System.out.println("Current min: " + heap.findMin());   // 1

        // decrease key of node 21 to 2
        FibNode node21 = heap.findNode(21);
        System.out.println("\nDecrease key of 21 to 2");
        heap.decreaseKey(node21, 2);
        System.out.println("New min: " + heap.findMin());       // 1 still

        // extract min repeatedly
        System.out.println("\nExtracting all elements:");
        while (heap.n > 0) {
            int minVal = heap.extractMin();
            System.out.print(minVal + " ");
        }
        System.out.println("\nHeap empty? " + (heap.min == null));
    }
}

