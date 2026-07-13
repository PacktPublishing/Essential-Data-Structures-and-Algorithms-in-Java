import java.util.*;

// -------------------------------------------------------
// 1. Node definition
// -------------------------------------------------------
class FibNode {
    int key;       // value of the node
    int degree;    // number of children
    boolean mark;  // true if node has lost a child
    FibNode parent;
    FibNode child; // one of the children (circular doubly-linked list)
    FibNode left;  // left sibling
    FibNode right; // right sibling

    FibNode(int key) {
        this.key = key;
        this.degree = 0;
        this.mark = false;
        this.parent = null;
        this.child = null;
        this.left = this;
        this.right = this;
    }
}

// -------------------------------------------------------
// 2. Fibonacci Heap
// -------------------------------------------------------
class FibonacciHeap {
    // Made public so main method can access heap.n safely
    public int n = 0; // number of nodes
    private FibNode min = null; // pointer to minimum node

    // ---------- basic list operations ----------
    private void addToRootList(FibNode node) {
        if (min == null) {
            min = node;
            node.left = node;
            node.right = node;
        } else {
            node.right = min.right;
            node.right.left = node;
            node.left = min;
            min.right = node;
            if (node.key < min.key) {
                min = node;
            }
        }
    }

    private void removeFromRootList(FibNode node) {
        if (node == node.right) { // only node left
            min = null;
        } else {
            node.left.right = node.right;
            node.right.left = node.left;
            if (min == node) {
                min = node.right;
            }
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

        /* Add children safely by saving references before pointer modification */
        if (z.child != null) {
            FibNode firstChild = z.child;
            FibNode x = firstChild;
            List<FibNode> children = new ArrayList<>();
            do {
                children.add(x);
                x = x.right;
            } while (x != firstChild);

            for (FibNode child : children) {
                child.parent = null;
                addToRootList(child);
            }
        }

        /* remove z from root list */
        removeFromRootList(z);
        n--;

        /* consolidate trees */
        if (min != null) {
            /* Use upper bound for Golden Ratio base (approx 1.44 * log2(n)) */
            int maxDegree = (int) Math.floor(Math.log(n + 1) / Math.log(1.618)) + 2;
            FibNode[] A = new FibNode[maxDegree];

            /* Collect roots safely before changing tree structures */
            List<FibNode> roots = new ArrayList<>();
            FibNode w = min;
            FibNode start = min;
            do {
                roots.add(w);
                w = w.right;
            } while (w != start);

            // Consolidate
            for (FibNode wNode : roots) {
                FibNode x = wNode;
                
                if (x.parent != null) {
                    continue;
                }
                
                int d = x.degree;
                while (A[d] != null) {
                    FibNode y = A[d];
                    if (x.key > y.key) {
                        FibNode tmp = x;
                        x = y;
                        y = tmp;
                    }
                    link(y, x); // make y child of x
                    A[d] = null;
                    d++;
                }
                A[d] = x;
            }

            /* recompute min */
            min = null;
            for (FibNode a : A) {
                if (a != null) {
                    if (min == null) {
                        min = a;
                        a.left = a;
                        a.right = a;
                    } else {
                        a.right = min.right;
                        a.right.left = a;
                        a.left = min;
                        min.right = a;
                        if (a.key < min.key) {
                            min = a;
                        }
                    }
                }
            }
        }
        return z.key;
    }

    public void decreaseKey(FibNode x, int newKey) {
        if (x == null) return;
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
        if (x == null) return;
        decreaseKey(x, Integer.MIN_VALUE);
        extractMin();
    }

    // ---------- helper functions ----------
    private void link(FibNode y, FibNode x) {
        // remove y from root list
        y.left.right = y.right;
        y.right.left = y.left;
        
        // make y child of x
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
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
            if (!y.mark) {
                y.mark = true;
            } else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

    // ---------- Searches entire heap ----------
    public FibNode findNode(int key) {
        if (min == null) return null;
        FibNode cur = min;
        do {
            if (cur.key == key) return cur;
            cur = cur.right;
        } while (cur != min);

        cur = min;
        do {
            if (cur.child != null) {
                FibNode result = findNodeInSubtree(cur.child, key);
                if (result != null) return result;
            }
            cur = cur.right;
        } while (cur != min);
        return null;
    }

    private FibNode findNodeInSubtree(FibNode startNode, int key) {
        if (startNode == null) return null;
        FibNode cur = startNode;
        do {
            if (cur.key == key) return cur;
            if (cur.child != null) {
                FibNode childResult = findNodeInSubtree(cur.child, key);
                if (childResult != null) return childResult;
            }
            cur = cur.right;
        } while (cur != startNode);
        return null;
    }

    // -------------------------------------------------
    // 3. Demo
    // -------------------------------------------------
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        System.out.println("=== Fibonacci Heap Demo ===");

        int[] values = { 10, 3, 7, 21, 5, 1, 12 };
        System.out.println("Insert values: " + Arrays.toString(values));
        for (int v : values) heap.insert(v);
        System.out.println("Current min: " + heap.findMin());

        FibNode node21 = heap.findNode(21);
        System.out.println("\nDecrease key of 21 to 2");
        heap.decreaseKey(node21, 2);
        System.out.println("New min: " + heap.findMin());

        System.out.println("\nExtracting all elements:");
        while (heap.n > 0) {
            int minVal = heap.extractMin();
            System.out.print(minVal + " ");
        }

        System.out.println("\n\nTesting repeated operations:");
        FibonacciHeap testHeap = new FibonacciHeap();
        for (int i = 0; i < 15; i++) {
            testHeap.insert(i);
        }
        while(testHeap.n > 0){
            System.out.print(testHeap.extractMin() + " ");
        }
        System.out.println("\nTest completed successfully!");
    }
}
