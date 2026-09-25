/**
 * ============================================================================
 * FILE: QuickSortDFS.java
 *
 * DEMONSTRATES: Recursion as DFS on an IMPLICIT recursion tree.
 *
 * KEY CONCEPT:
 *   QuickSort has NO tree data structure in memory.  There are no TreeNode
 *   objects, no .left or .right pointers.  The "tree" exists ONLY as the
 *   CONCEPTUAL PATTERN of recursive calls:
 *
 *     - Each call to quickSort() is ONE NODE in the recursion tree
 *     - That node's "work" is partitioning a subarray [lo..hi]
 *     - The node's LEFT CHILD = recursive call on elements < pivot
 *     - The node's RIGHT CHILD = recursive call on elements > pivot
 *     - A LEAF = base case where lo >= hi (subarray size ≤1, already sorted)
 *
 *   The JVM call stack IS this recursion tree being explored via DFS.
 *
 * EXAMPLE RECURSION TREE for array [5, 2, 9, 1, 7, 6]:
 *
 *   Initial call: quickSort(arr, 0, 5) partitions around pivot 6
 *   After partition: [5, 2, 1] | 6 | [9, 7]
 *                     ↑ elements < 6    ↑ elements > 6
 *
 *   Recursion tree (each node = one call to quickSort):
 *
 *            quickSort(0,5)
 *            pivot = 6
 *           /              \
 *    quickSort(0,2)    quickSort(4,5)
 *    pivot = 1         pivot = 7
 *   /         \       /         \
 * qs(0,-1)  qs(1,2) qs(4,3)   qs(5,5)
 * BASE      piv=2   BASE      BASE
 *          /     \
 *      qs(1,0) qs(2,2)
 *      BASE    BASE
 *
 * DFS TRAVERSAL ORDER (pre-order: visit node before children):
 *   qs(0,5) → qs(0,2) → qs(0,-1) [base] → qs(1,2) → qs(1,0) [base] →
 *   qs(2,2) [base] → qs(4,5) → qs(4,3) [base] → qs(5,5) [base]
 *
 * CALL STACK ↔ RECURSION TREE MAPPING:
 *   - Pushing a frame = descending to a child node
 *   - Popping a frame = backtracking up to parent
 *   - Stack depth = current depth in the recursion tree
 * ============================================================================
 */
public class QuickSortDFS {

    /**
     * QuickSort: recursively partition and sort subarrays.
     *
     * ALGORITHM (Divide and Conquer):
     *   1. If subarray has ≤1 element, it's already sorted (base case).
     *   2. Otherwise, PARTITION the subarray around a pivot element so that:
     *        - All elements left of pivot are ≤ pivot
     *        - All elements right of pivot are ≥ pivot
     *        - Pivot is in its final sorted position
     *   3. Recursively sort the left partition (elements < pivot)
     *   4. Recursively sort the right partition (elements > pivot)
     *
     * RECURSION TREE NODE DEFINITION:
     *   Each call quickSort(arr, lo, hi) represents ONE NODE in the tree:
     *     - Node's WORK: partition arr[lo..hi] around a pivot
     *     - Node's LEFT CHILD: quickSort(arr, lo, pivotIndex-1)
     *     - Node's RIGHT CHILD: quickSort(arr, pivotIndex+1, hi)
     *     - LEAF NODE: lo >= hi (nothing to partition, return immediately)
     *
     * DFS TRAVERSAL:
     *   Pre-order: do the partition FIRST (visit this node), then recurse
     *   left (sort smaller elements), then recurse right (sort larger).
     *
     * CALL STACK ↔ TREE MAPPING:
     *   - Each recursive call PUSHES a frame → descend to a child node
     *   - Each return POPS a frame → backtrack to parent node
     *   - Stack depth at any moment = depth in the recursion tree
     *
     * SPACE COMPLEXITY: O(log n) average case for the call stack depth.
     *   The recursion tree's depth depends on how balanced the partitions are.
     *   Best case (pivot always splits evenly): depth = log₂(n)
     *   Worst case (pivot always smallest/largest): depth = n
     *
     * TIME COMPLEXITY: O(n log n) average, O(n²) worst case.
     *
     * @param arr    the array being sorted (shared across all frames on heap)
     * @param lo     start index of current subarray (local to THIS frame)
     * @param hi     end index of current subarray (local to THIS frame)
     * @param depth  indentation level for trace output (cosmetic)
     */
    static void quickSort(int[] arr, int lo, int hi, int depth) {

        // Build indentation string proportional to recursion depth.
        // Visually shows the nesting of recursive calls (the tree depth).
        String indent = "  ".repeat(depth);

        // ── Trace: announce this call (entering a new tree node) ───────
        // This frame was just PUSHED onto the call stack.
        // We are now at a NEW NODE in the recursion tree.
        // This node's job is to sort the subarray arr[lo..hi].
        System.out.printf("%sCALL quickSort(arr, lo=%d, hi=%d) " +
                         "[call stack depth %d]%n",
                         indent, lo, hi, depth);

        // ── BASE CASE — LEAF NODE ───────────────────────────────────────
        // When lo >= hi, the subarray has ≤1 element.
        // A single element is already sorted by definition.
        // There is nothing to partition, no children to spawn.
        //
        // RECURSION TREE MAPPING: This is a LEAF in the recursion tree.
        // No further recursive calls are made below this node.
        // We return immediately without pushing any additional frames.
        //
        // CALL STACK EFFECT: POP this frame; control returns to the parent
        // frame (the quickSort call that spawned this one).
        //
        // TREE PERSPECTIVE: We hit the END of a branch.  Backtrack up.
        if (lo >= hi) {
            System.out.printf("%s  → base case (subarray size ≤1), " +
                             "already sorted — POP frame%n", indent);
            return;   // ← POP; parent frame resumes at the line after the call
        }

        // ── PARTITION — THIS NODE'S WORK ────────────────────────────────
        // Partition the subarray arr[lo..hi] around a pivot element.
        //
        // After partition completes:
        //   - All elements arr[lo .. pivotIndex-1] are ≤ pivot
        //   - arr[pivotIndex] == pivot (in its FINAL sorted position)
        //   - All elements arr[pivotIndex+1 .. hi] are ≥ pivot
        //
        // RECURSION TREE MAPPING: This is the WORK done at THIS NODE.
        // In a binary tree DFS, "visiting a node" meant printing its value.
        // Here, "visiting" means partitioning the array segment.
        //
        // CALL STACK PERSPECTIVE: This work happens INSIDE the current frame.
        // No new frames are pushed yet — partition() is a helper function
        // that does NOT recurse.  It rearranges elements in-place and returns
        // the pivot's final index.
        //
        // COMPARISON TO BINARY TREE DFS: In the binary tree, we printed the
        // node's value before recursing on children (pre-order).  Here, we
        // partition before recursing on the two sub-arrays.  Same pattern.
        int pivotIndex = partition(arr, lo, hi);
        System.out.printf("%s  partitioned around pivot %d at index %d%n",
                         indent, arr[pivotIndex], pivotIndex);
        System.out.printf("%s    left partition:  arr[%d..%d] (elements ≤ %d)%n",
                         indent, lo, pivotIndex - 1, arr[pivotIndex]);
        System.out.printf("%s    right partition: arr[%d..%d] (elements ≥ %d)%n",
                         indent, pivotIndex + 1, hi, arr[pivotIndex]);

        // ── RECURSIVE CASE — LEFT CHILD ─────────────────────────────────
        // Sort the LEFT partition: all elements smaller than the pivot.
        // Subarray range: [lo .. pivotIndex-1]
        //
        // RECURSION TREE MAPPING: This spawns the LEFT CHILD node in the tree.
        // That child's job is to recursively sort the left partition.
        //
        // CALL STACK EFFECT: PUSH a new frame for quickSort(arr, lo, pivotIndex-1).
        // THIS frame (holding lo, hi, pivotIndex) SUSPENDS here.
        // It cannot continue to the next line until the ENTIRE left subtree
        // has been fully sorted (all frames pushed below have popped).
        //
        // DFS PATH: We descend LEFT in the recursion tree.
        // Stack depth increases by 1.
        System.out.printf("%s  ↓ recurse LEFT: quickSort(arr, %d, %d)%n",
                         indent, lo, pivotIndex - 1);
        quickSort(arr, lo, pivotIndex - 1, depth + 1);  // ← PUSH left child
        // ... entire left partition is now fully sorted ...
        // ... all frames below have popped ...
        // ... control returns HERE to this line ...
        System.out.printf("%s  ← left partition sorted%n", indent);

        // ── RECURSIVE CASE — RIGHT CHILD ────────────────────────────────
        // Sort the RIGHT partition: all elements larger than the pivot.
        // Subarray range: [pivotIndex+1 .. hi]
        //
        // RECURSION TREE MAPPING: This spawns the RIGHT CHILD node in the tree.
        //
        // CALL STACK EFFECT: PUSH a new frame for quickSort(arr, pivotIndex+1, hi).
        // Again, THIS frame suspends while the entire right subtree is explored.
        //
        // DFS PATH: We descend RIGHT in the recursion tree.
        System.out.printf("%s  ↓ recurse RIGHT: quickSort(arr, %d, %d)%n",
                         indent, pivotIndex + 1, hi);
        quickSort(arr, pivotIndex + 1, hi, depth + 1);  // ← PUSH right child
        // ... entire right partition is now fully sorted ...
        // ... control returns HERE ...
        System.out.printf("%s  ← right partition sorted%n", indent);

        // ── BACKTRACK ────────────────────────────────────────────────────
        // Both partitions (left and right) are now fully sorted.
        // The subarray arr[lo..hi] is now completely sorted.
        // This node's work is complete.
        //
        // RECURSION TREE MAPPING: We are BACKTRACKING from this node up
        // to its parent in the recursion tree.
        //
        // CALL STACK EFFECT: POP this frame.
        // If this was called from another quickSort frame, that parent resumes.
        // If this was the initial call from main() (depth 0), the entire array
        // is now sorted and we return to main().
        //
        // DFS PATH: Stack depth decreases by 1.
        System.out.printf("%sqSort(arr, %d, %d) complete — " +
                         "subarray fully sorted, POP frame%n",
                         indent, lo, hi);
        // The 'return' happens implicitly at the end of the method.
        // JVM pops this frame and resumes the caller.
    }

    /**
     * Partition helper: rearrange arr[lo..hi] around a pivot.
     *
     * ALGORITHM: Lomuto partition scheme (simple, though not optimal for arrays
     * with many duplicate values).
     *
     * STEPS:
     *   1. Choose arr[hi] as the pivot (last element in the subarray).
     *   2. Scan from left to right (indices lo to hi-1).
     *   3. Maintain a boundary 'i' — everything at arr[lo..i] is ≤ pivot.
     *   4. When we find an element ≤ pivot, move it to the ≤ region by
     *      swapping it with arr[i+1], then advance i.
     *   5. After the scan, swap the pivot (at arr[hi]) into its final position
     *      at index i+1.  Now arr[i+1] == pivot, with smaller elements to the
     *      left and larger elements to the right.
     *   6. Return i+1 (the pivot's final sorted index).
     *
     * CRITICAL: This is a HELPER function, not a recursive function.
     * It does NOT push any stack frames.  It is work done INSIDE the current
     * quickSort frame.
     *
     * INVARIANT maintained during the scan:
     *   arr[lo..i]   → all elements ≤ pivot (the "small" region)
     *   arr[i+1..j-1] → all elements > pivot (the "large" region)
     *   arr[j..hi-1]  → unprocessed elements (not yet compared to pivot)
     *   arr[hi]       → the pivot itself (not moved until the end)
     *
     * @param arr  the array being partitioned
     * @param lo   start index of the partition range
     * @param hi   end index of the partition range (pivot is arr[hi])
     * @return the final sorted index of the pivot after partition
     */
    static int partition(int[] arr, int lo, int hi) {

        // Choose the last element as the pivot.
        // (Other strategies: median-of-three, random pivot, etc.)
        int pivot = arr[hi];

        // Index 'i' tracks the boundary of the "≤ pivot" region.
        // Initially, we haven't seen any elements ≤ pivot yet, so i starts
        // one position BEFORE lo.  As we find elements ≤ pivot, we'll
        // increment i and move them into arr[lo..i].
        int i = lo - 1;

        // Scan all elements from lo to hi-1 (everything except the pivot).
        // For each element, decide: does it belong in the "≤ pivot" region?
        for (int j = lo; j < hi; j++) {

            // If arr[j] ≤ pivot, it belongs in the left (small) region.
            if (arr[j] <= pivot) {

                // Expand the "≤ pivot" region by moving the boundary i forward.
                i++;

                // Swap arr[j] into the "≤ pivot" region at index i.
                // This moves arr[j] into its correct partition.
                // Whatever was at arr[i] (an element > pivot) gets swapped
                // into the "unprocessed" region where j is scanning.
                swap(arr, i, j);
            }
            // If arr[j] > pivot, we do NOTHING.  It stays where it is,
            // and j moves forward.  This element is automatically in the
            // "> pivot" region arr[i+1..j].
        }

        // At this point:
        //   arr[lo..i]   → all elements ≤ pivot
        //   arr[i+1..hi-1] → all elements > pivot
        //   arr[hi]      → the pivot (still in its original position)
        //
        // Now place the pivot in its FINAL sorted position at index i+1.
        // This is between the ≤ region and the > region.
        swap(arr, i + 1, hi);

        // The pivot is now at arr[i+1], with:
        //   arr[lo..i]     → elements ≤ pivot
        //   arr[i+1]       → pivot (in FINAL sorted position)
        //   arr[i+2..hi]   → elements ≥ pivot
        //
        // Return the pivot's index.  The quickSort caller will use this
        // to know where to split the array for the two recursive calls.
        return i + 1;
    }

    /**
     * Swap two elements in an array.
     *
     * This is a utility function used by partition().
     * It does NOT push any stack frames — it's pure work inside the current frame.
     *
     * @param arr  the array
     * @param i    index of first element to swap
     * @param j    index of second element to swap
     */
    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    /**
     * Main method: demonstrate QuickSort as DFS on an implicit recursion tree.
     */
    public static void main(String[] args) {

        System.out.println("=".repeat(72));
        System.out.println("  QuickSort DFS — Recursion IS the sorting algorithm");
        System.out.println("=".repeat(72));
        System.out.println();

        // ── Create an unsorted array ────────────────────────────────────
        int[] arr = {5, 2, 9, 1, 7, 6};

        System.out.println("Original array: " + java.util.Arrays.toString(arr));
        System.out.println();
        System.out.println("QuickSort recursion tree (each call = one node):");
        System.out.println();
        System.out.println("--- Recursive calls with call stack trace ---");
        System.out.println();

        // ── Run QuickSort on the full array ─────────────────────────────
        // This initial call pushes the FIRST frame onto the call stack.
        // That frame represents the root of the recursion tree: sorting
        // the entire array arr[0..5].
        //
        // From there, the recursion unfolds as annotated in quickSort().
        // The DFS traversal explores the recursion tree:
        //   - Each partition operation = visiting a node
        //   - Each recursive call = descending to a child
        //   - Each return = backtracking to the parent
        quickSort(arr, 0, arr.length - 1, 0);

        System.out.println();
        System.out.println("Sorted array:   " + java.util.Arrays.toString(arr));

        // ── Summary ──────────────────────────────────────────────────────
        System.out.println();
        System.out.println("=".repeat(72));
        System.out.println("  KEY INSIGHT");
        System.out.println("=".repeat(72));
        System.out.println("QuickSort has NO tree data structure in memory.");
        System.out.println("The tree exists ONLY as the pattern of recursive calls:");
        System.out.println();
        System.out.println("  • Each call = one node in the recursion tree");
        System.out.println("  • Node's work = partition one subarray");
        System.out.println("  • Left child = recursive call on elements < pivot");
        System.out.println("  • Right child = recursive call on elements > pivot");
        System.out.println("  • Leaf = base case (subarray size ≤1)");
        System.out.println();
        System.out.println("The JVM call stack IS this recursion tree being explored.");
        System.out.println("DFS happens automatically via the recursive calls:");
        System.out.println();
        System.out.println("  PUSH frame → descend to child (smaller subarray)");
        System.out.println("  POP frame  → backtrack to parent (resume sorting)");
        System.out.println();
        System.out.println("Same DFS pattern as binary tree traversal — but the tree");
        System.out.println("is implicit, defined by the divide-and-conquer splits.");
    }
}
