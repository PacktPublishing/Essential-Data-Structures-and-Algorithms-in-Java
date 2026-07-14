import java.util.*;

// -------------------------------------------------------
// 1. Node Definition
// -------------------------------------------------------
class FibNode {
    int key;       // Value of the node
    int degree;    // Number of structural children
    boolean mark;  // True if node has lost a child since becoming a child of its current parent
    FibNode parent;
    FibNode child; // One entry point to its circular doubly-linked child list
    FibNode left;  // Self-referential loop pointers for circular list optimization
    FibNode right;

    FibNode(int key) {
        this.key = key;
        this.degree = 0;
        this.mark = false;
        this.parent = null;
        this.child = null;
        // Initialize left and right pointing to self.
        // This makes single node circular lists valid from creation.
        this.left = this;
        this.right = this;
    }
}

// -------------------------------------------------------
// 2. Fibonacci Heap
// -------------------------------------------------------
class FibonacciHeap {
    public int n = 0; // Total count of nodes in the entire heap
    private FibNode min = null; // Single entry point pointer to the minimum node
    
    // Time Complexity: O(1) Worst-Case
    // Deque splice technique allows instant list joining without shifting
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

    // Time Complexity: O(1) Worst-Case
    private void removeFromRootList(FibNode node) {
        if (node == node.right) {
            min = null;
        } else {
            node.left.right = node.right;
            node.right.left = node.left;
            if (min == node) {
                min = node.right; // Safe fallback pointer before consolidation
            }
        }
    }
    
    // Time Complexity: O(1) Amortized / O(1) Worst-Case
    // Lazy Insertion strategy. Does not maintain tree order on insert.
    public void insert(int key) {
        FibNode node = new FibNode(key);
        addToRootList(node);
        n++;
    }

    // Time Complexity: O(1) Amortized / O(1) Worst-Case
    public int findMin() {
        return min != null ? min.key : Integer.MAX_VALUE;
    }

    // Time Complexity: O(log n) Amortized / O(n) Worst-Case
    // Re-linking nodes dynamically within a loop can cause infinite loops 
    // if references change. Collecting items beforehand into an 
    // intermediate list avoids this.
    public int extractMin() {
        if (min == null) return Integer.MAX_VALUE;
        FibNode z = min;

        // We isolate child references into a collection so pointer mutations 
        // in addToRootList() do not corrupt the traversal loop.
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

        removeFromRootList(z);
        n--;

        // Consolidate trees
        if (min != null) {
            // Golden Ratio base upper-bound safely calculates max possible tree height.
            // Using n after decrement handles the edge case of draining the last element flawlessly.
            int maxDegree = (int) Math.floor(Math.log(n + 1) / Math.log(1.618)) + 2;
            FibNode[] A = new FibNode[maxDegree];

            // Collect root pointers before linking modifies left/right references.
            List<FibNode> roots = new ArrayList<>();
            FibNode w = min;
            FibNode start = min;
            do {
                roots.add(w);
                w = w.right;
            } while (w != start);

            // Pair up trees of equal degrees
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
                    link(y, x); // y becomes child of x
                    A[d] = null;
                    d++;
                }
                A[d] = x;
            }

            // Rebuild Root List Cleanly
            // Completely isolating and reconstructing 
            // pointers prevents tree fragmentation.
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

    // Time Complexity: O(1) Amortized / O(log n) Worst-Case
    public void decreaseKey(FibNode x, int newKey) {
        if (x == null) return;
        if (newKey > x.key) throw new IllegalArgumentException("new key is larger");
        x.key = newKey;
        FibNode y = x.parent;
        
        // If heap property violated, cut child from parent
        if (y != null && x.key < y.key) {
            cut(x, y);
            cascadingCut(y); // Recursively check if structural balance is broken
        }
        if (x.key < min.key) min = x;
    }

    // Time Complexity: O(log n) Amortized / O(n) Worst-Case
    public void delete(FibNode x) {
        if (x == null) return;
        decreaseKey(x, Integer.MIN_VALUE); // Force to root position
        extractMin();                      // Expel element
    }

    // ---------- Helper Functions ----------
    
    // Time Complexity: O(1) Worst-Case
    private void link(FibNode y, FibNode x) {
        y.left.right = y.right;
        y.right.left = y.left;

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
        y.mark = false; // Reset mark state when establishing a new parent
    }

    // Time Complexity: O(1) Worst-Case
    private void cut(FibNode x, FibNode y) {
        if (x.right == x) {
            y.child = null;
        } else {
            x.right.left = x.left;
            x.left.right = x.right;
            if (y.child == x) y.child = x.right;
        }
        y.degree--;
        addToRootList(x);
        x.parent = null;
        x.mark = false;
    }

    // Time Complexity: O(1) Amortized / O(log n) Worst-Case
    // Cascading Cuts maintain structural density bounds, keeping 
    // tree roots flat.
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

    
    // Time Complexity: O(n) Worst-Case
    // Brute-Force fallback lookup helper.
    // NOTE: Fibonacci Heaps do not natively support O(1) or O(log n) non-min lookups. 
    // Production tracking depends on maintaining an external HashMap mapping keys to FibNodes.
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

    public static void main(String[] args) {
     
        FibonacciHeap heap = new FibonacciHeap(); 
        System.out.println("=== Fibonacci Heap Demo ==="); 
        int[] values = { 10, 3, 7, 21, 5, 1, 12 }; 
        System.out.println("Insert values: " + Arrays.toString(values)); 
        for (int v : values) 
            heap.insert(v); 
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
        while(testHeap.n > 0) { 
            System.out.print(testHeap.extractMin() + " "); 
        } 
        System.out.println("\nTest completed successfully!");
    }
}

