/**
 * ============================================================================
 * FILE: BinaryTreeDFS.java
 *
 * DEMONSTRATES: Recursion as DFS on an EXPLICIT tree structure.
 *
 * KEY CONCEPT:
 *   The binary tree is a physical data structure in memory (TreeNode objects).
 *   Each recursive call to dfs() corresponds to VISITING one node in the tree.
 *   The JVM call stack IS the current path from root to the node being visited.
 *
 * CALL STACK ↔ TREE PATH MAPPING:
 *   - Each call pushes a frame → descend one level deeper in the tree
 *   - Each return pops a frame → backtrack one level up toward the root
 *   - Stack depth at any moment = depth of the current node in the tree
 *
 * TREE STRUCTURE (the example we'll traverse):
 *          1
 *         / \
 *        2   3
 *       / \ / \
 *      4  5 6  7
 *
 * PRE-ORDER DFS VISIT SEQUENCE: 1 → 2 → 4 → 5 → 3 → 6 → 7
 *   (Visit root first, then left subtree, then right subtree)
 *
 * CALL STACK EVOLUTION over time:
 *   [dfs(1)]                    ← initial call at root
 *   [dfs(1), dfs(2)]            ← pushed left child of 1
 *   [dfs(1), dfs(2), dfs(4)]    ← pushed left child of 2
 *   [dfs(1), dfs(2)]            ← node 4 is a leaf, popped
 *   [dfs(1), dfs(2), dfs(5)]    ← pushed right child of 2
 *   [dfs(1), dfs(2)]            ← node 5 is a leaf, popped
 *   [dfs(1)]                    ← entire subtree 2 done, popped
 *   [dfs(1), dfs(3)]            ← pushed right child of 1
 *   [dfs(1), dfs(3), dfs(6)]    ← pushed left child of 3
 *   [dfs(1), dfs(3)]            ← node 6 is a leaf, popped
 *   [dfs(1), dfs(3), dfs(7)]    ← pushed right child of 3
 *   [dfs(1), dfs(3)]            ← node 7 is a leaf, popped
 *   [dfs(1)]                    ← entire subtree 3 done, popped
 *   []                          ← entire tree traversed, stack empty
 * ============================================================================
 */
public class BinaryTreeDFS {

    /**
     * Binary tree node structure.
     * 
     * Each TreeNode is a physical object on the heap containing:
     *   - val: the data stored at this node
     *   - left: a reference to the left child (or null if none)
     *   - right: a reference to the right child (or null if none)
     *
     * The tree's SHAPE is defined by these left/right references.
     * Following a reference = traversing an edge in the tree.
     */
    static class TreeNode {
        int val;           // Data at this node
        TreeNode left;     // Pointer to left child (null = no child)
        TreeNode right;    // Pointer to right child (null = no child)

        /**
         * Constructor: create a tree node with a value and two children.
         * 
         * @param v  the value to store at this node
         * @param l  reference to left child (pass null for no left child)
         * @param r  reference to right child (pass null for no right child)
         */
        TreeNode(int v, TreeNode l, TreeNode r) {
            val = v;
            left = l;
            right = r;
        }
    }

    /**
     * Pre-order DFS traversal: visit root, then left subtree, then right.
     *
     * PRE-ORDER means:
     *   1. VISIT (process) the current node FIRST
     *   2. Recursively traverse the LEFT subtree
     *   3. Recursively traverse the RIGHT subtree
     *
     * CALL STACK MECHANICS:
     *   - When this method is called, a NEW frame is PUSHED onto the JVM stack.
     *   - That frame contains the local variables: 'node' and 'depth'.
     *   - The 'node' variable points to ONE node in the tree — THIS is the
     *     node we are currently visiting.
     *   - The two recursive calls (left, right) each PUSH additional frames
     *     onto the stack — those frames represent descending deeper into
     *     the tree to visit child nodes.
     *   - When this method returns, its frame is POPPED — we backtrack to
     *     the parent node (the frame that called us).
     *
     * BASE CASE (null node):
     *   When we call dfs(node.left) on a leaf node, node.left is null.
     *   The null check catches this and returns immediately without pushing
     *   any further frames.  This is a LEAF's CHILD — the end of a branch.
     *
     * RECURSIVE CASE (non-null node):
     *   We visit the current node (print its value), then recursively visit
     *   its left and right children.  Each recursive call pushes a frame.
     *   The current frame SUSPENDS while the child subtrees are explored.
     *
     * @param node   current tree node to visit (null means no node here)
     * @param depth  current depth in the tree (0 = root, purely for display)
     */
    static void dfsPreOrder(TreeNode node, int depth) {

        // Build an indentation string proportional to depth.
        // Deeper in the tree = more indentation in the console output.
        // This visually shows the call stack nesting.
        String indent = "  ".repeat(depth);

        // ── BASE CASE — null node (leaf's child, end of a branch) ──────
        // When a node has no left or right child, that child pointer is null.
        // We reach here when recursing on a missing child.
        //
        // TREE PERSPECTIVE: null represents the ABSENCE of a node.
        // There is nothing to visit, so we return immediately.
        //
        // CALL STACK EFFECT: This frame was just pushed (via the recursive
        // call in the parent), and now it immediately POPS without doing
        // any work or pushing any further frames.
        //
        // DFS PERSPECTIVE: This is where a branch ENDS.  We backtrack
        // immediately to the parent without going any deeper.
        if (node == null) {
            System.out.println(indent + "null — no node here, return (POP frame)");
            return;   // ← POP this frame, control returns to caller
        }

        // ── VISIT THIS NODE ────────────────────────────────────────────
        // In pre-order DFS, we VISIT (process) the current node BEFORE
        // exploring its children.  "Visiting" means doing whatever work
        // this node represents — here, we just print its value.
        //
        // CALL STACK PERSPECTIVE: This code executes inside the CURRENT
        // frame, which was pushed when the parent node called dfs(node).
        // The frame's local variable 'node' points to THIS tree node.
        //
        // This is the FIRST thing we do after entering this frame —
        // visit the node, then recurse on children.
        System.out.println(indent + "VISIT node " + node.val 
                         + " [call stack depth " + depth + "]");

        // ── RECURSE LEFT — explore left subtree ────────────────────────
        // Follow the left child pointer and recursively visit that subtree.
        //
        // TREE PERSPECTIVE: node.left is the EDGE connecting this node
        // to its left child.  Following that edge = descending left.
        //
        // CALL STACK EFFECT: This line PUSHES a new frame onto the stack
        // for dfs(node.left, depth+1).  The CURRENT frame (holding 'node')
        // SUSPENDS here — it cannot continue to the next line until the
        // ENTIRE left subtree has been fully explored and all those frames
        // have popped.
        //
        // DFS PATH: We go DEEPER into the tree.  Stack depth increases by 1.
        // If node.left is null, the pushed frame will hit the base case
        // above and return immediately.
        System.out.println(indent + "  ↓ recurse LEFT from node " + node.val);
        dfsPreOrder(node.left, depth + 1);   // ← PUSH frame for left child
        // ... entire left subtree is now fully visited ...
        // ... all those frames have popped ...
        // ... control returns HERE to this line ...
        System.out.println(indent + "  ← left subtree of node " + node.val + " complete");

        // ── RECURSE RIGHT — explore right subtree ───────────────────────
        // The left subtree is fully explored.  We are BACK in the current
        // frame, still holding 'node'.  Now follow the right child pointer.
        //
        // TREE PERSPECTIVE: node.right is the edge to the right child.
        //
        // CALL STACK EFFECT: PUSH a new frame for dfs(node.right, depth+1).
        // Again, the current frame suspends while the entire right subtree
        // is explored.
        //
        // DFS PATH: We descend into the tree again, this time to the RIGHT.
        System.out.println(indent + "  ↓ recurse RIGHT from node " + node.val);
        dfsPreOrder(node.right, depth + 1);  // ← PUSH frame for right child
        // ... entire right subtree is now fully visited ...
        // ... all those frames have popped ...
        // ... control returns HERE ...
        System.out.println(indent + "  ← right subtree of node " + node.val + " complete");

        // ── BACKTRACK ───────────────────────────────────────────────────
        // Both children (left and right subtrees) have been fully explored.
        // All work for THIS node is complete.  The method returns.
        //
        // CALL STACK EFFECT: THIS frame is POPPED.  If this node was called
        // from a parent node, control returns to that parent's frame (the
        // line after the recursive call that pushed us).  If this was the
        // root node (depth 0), control returns to main().
        //
        // TREE PERSPECTIVE: We are BACKTRACKING from this node up to its
        // parent.  We have finished visiting this entire subtree.
        //
        // DFS PATH: Stack depth decreases by 1.
        System.out.println(indent + "node " + node.val 
                         + " complete — POP frame, backtrack to parent");
        // The 'return' happens implicitly at the end of the method.
        // The JVM pops this frame and resumes the caller.
    }


    /**
     * Main method: build a sample tree and run DFS on it.
     */
    public static void main(String[] args) {

        System.out.println("=".repeat(72));
        System.out.println("  Binary Tree DFS — Recursion IS the tree traversal");
        System.out.println("=".repeat(72));
        System.out.println();

        // ── Build the tree ──────────────────────────────────────────────
        // We construct the tree BOTTOM-UP: leaves first, then their parents.
        // 
        // Tree structure:
        //          1
        //         / \
        //        2   3
        //       / \ / \
        //      4  5 6  7
        //
        // Each TreeNode() call creates one node on the heap.
        // The left/right parameters are references to child nodes.
        // Passing null means "no child on that side".

        // Build leaves (nodes with no children):
        TreeNode node4 = new TreeNode(4, null, null);  // leaf
        TreeNode node5 = new TreeNode(5, null, null);  // leaf
        TreeNode node6 = new TreeNode(6, null, null);  // leaf
        TreeNode node7 = new TreeNode(7, null, null);  // leaf

        // Build internal nodes (nodes with children):
        TreeNode node2 = new TreeNode(2, node4, node5);  // 4 is left, 5 is right
        TreeNode node3 = new TreeNode(3, node6, node7);  // 6 is left, 7 is right

        // Build root:
        TreeNode root = new TreeNode(1, node2, node3);   // 2 is left, 3 is right

        // ── Display the tree structure ──────────────────────────────────
        System.out.println("Tree structure:");
        System.out.println("        1");
        System.out.println("       / \\");
        System.out.println("      2   3");
        System.out.println("     / \\ / \\");
        System.out.println("    4  5 6  7");
        System.out.println();
        System.out.println("Pre-order DFS: visit root, then left subtree, then right subtree");
        System.out.println("Expected visit sequence: 1 → 2 → 4 → 5 → 3 → 6 → 7");
        System.out.println();
        System.out.println("--- DFS traversal with call stack trace ---");
        System.out.println();

        // ── Run DFS starting at the root ────────────────────────────────
        // This initial call pushes the FIRST frame onto the call stack.
        // That frame represents visiting the root node (value 1, depth 0).
        // From there, the recursion unfolds as described in the annotations.
        dfsPreOrder(root, 0);

        // ── Summary ─────────────────────────────────────────────────────
        System.out.println();
        System.out.println("=".repeat(72));
        System.out.println("  KEY INSIGHT");
        System.out.println("=".repeat(72));
        System.out.println("The JVM call stack IS the path from root to current node.");
        System.out.println("  • Each PUSH = descend one level deeper in the tree");
        System.out.println("  • Each POP  = backtrack one level up toward the root");
        System.out.println("  • Stack depth = current depth in the tree");
        System.out.println("  • The recursion naturally implements DFS — no explicit");
        System.out.println("    stack data structure needed.");
    }
}
