/**
 * ============================================================
 * FILE: SumArray.java
 *
 * PROBLEM: compute the sum of all elements in an integer array.
 *
 * TWO IMPLEMENTATIONS — same result, different memory cost:
 *
 *   sumRecursive(arr, i)  →  Time: O(n)  |  Space: O(n)
 *   sumIterative(arr)     →  Time: O(n)  |  Space: O(1)
 *
 * THE SPACE TRADE-OFF (the key insight):
 *   Recursive: every element causes a NEW stack frame to be pushed
 *     and to stay alive until the base case is hit.  For an array
 *     of n elements, n+1 frames coexist on the call stack at peak.
 *     Stack space grows linearly with n → O(n).
 *
 *   Iterative: a single method frame is entered once and stays.
 *     The loop variables (total, i) are overwritten each iteration.
 *     Old values are discarded immediately — they never accumulate.
 *     Stack space stays flat regardless of n → O(1).
 *
 * TIME COMPLEXITY: both visit each element exactly once → O(n).
 *   The time trade-off is negligible.
 *   The SPACE trade-off is significant and can cause
 *   StackOverflowError for large n with the recursive version.
 * ============================================================
 */
public class SumArray {

    // ==========================================================
    // APPROACH 1 — RECURSIVE
    //
    // How it works conceptually:
    //   "The sum of an array starting at index i is
    //    arr[i] + the sum of the rest starting at i+1."
    //
    // Call-stack consequence:
    //   sumRecursive({10,20,30,40}, 0)
    //     PUSHES sumRecursive({10,20,30,40}, 1)    [n=4 deep]
    //       PUSHES sumRecursive({10,20,30,40}, 2)  [n=4 deep]
    //         PUSHES sumRecursive({10,20,30,40}, 3)[n=4 deep]
    //           PUSHES sumRecursive({10,20,30,40}, 4) — BASE
    //
    //   All 5 frames live simultaneously on the stack.
    //   The "+arr[i]" in each frame is PENDING — it cannot run
    //   until the frame below it returns a value.
    //   Only then do they unwind: 0 → 40 → 70 → 90 → 100.
    //
    // SPACE: O(n)
    //   Peak stack depth = n+1 frames (n elements + 1 base case).
    //   On a typical JVM (512 KB default stack) this breaks around
    //   n ≈ 5,000–10,000 with a StackOverflowError.
    //
    // TIME: O(n) — each element visited exactly once.
    // ==========================================================

    /**
     * Returns the sum of arr[i..arr.length-1] recursively.
     *
     * @param arr  the array (shared reference — lives on the heap,
     *             not duplicated per frame; only the index i differs)
     * @param i    the current position; start with 0 from main()
     */
    static int sumRecursive(int[] arr, int i) {

        // ── BASE CASE ─────────────────────────────────────────
        // i == arr.length means we have stepped past the last element.
        // 0 is the additive identity: adding 0 contributes nothing
        // to the product accumulating in the frames above us.
        // This is the DEEPEST frame; it returns immediately and starts
        // the unwinding process.
        if (i == arr.length)
            return 0;       // ← POP this frame, hand 0 up to caller

        // ── RECURSIVE CASE ────────────────────────────────────
        // This line does three things:
        //   1. Read arr[i] — safe because the base case above
        //      guarantees i < arr.length here.
        //   2. PUSH a new frame for sumRecursive(arr, i+1).
        //      THIS frame suspends — the "+ arr[i]" is pending.
        //   3. When the sub-call returns, resume here and add arr[i]
        //      to whatever came back from below.
        //   4. Return the sum → POP this frame.
        //
        // SPACE IMPLICATION: every call to this line pushes a frame
        // that stays alive.  For arr of length n, there will be n+1
        // live frames on the stack simultaneously at peak depth.
        return arr[i] + sumRecursive(arr, i + 1);
        //     ^^^^^^   ^^^^^^^^^^^^^^^^^^^^^^^^^
        //     this      new frame pushed here;
        //     frame's   THIS frame's arr[i] waits
        //     value     in its stack slot until
        //               the sub-chain fully unwinds
    }


    // ==========================================================
    // APPROACH 2 — ITERATIVE
    //
    // How it works conceptually:
    //   "Walk the array left to right, adding each element to a
    //    running total.  When the loop ends, return the total."
    //
    // Call-stack consequence:
    //   sumIterative({10,20,30,40}) pushes EXACTLY ONE frame.
    //   That frame houses two local variables: total and i.
    //   Each loop iteration OVERWRITES total and i in place.
    //   No sub-frames are ever pushed.
    //
    //   Iteration-by-iteration memory state:
    //     before loop:  total=0,  i=0
    //     after iter 0: total=10, i=1  (old values gone)
    //     after iter 1: total=30, i=2
    //     after iter 2: total=60, i=3
    //     after iter 3: total=100,i=4  → loop ends
    //
    // SPACE: O(1)
    //   The SAME two variables exist regardless of whether n is 4
    //   or 4,000,000.  Stack consumption is flat and constant.
    //
    // TIME: O(n) — each element visited exactly once (same as recursive).
    // ==========================================================

    /**
     * Returns the sum of all elements in arr iteratively.
     *
     * @param arr  the array to sum
     */
    static int sumIterative(int[] arr) {

        // ── ACCUMULATOR ───────────────────────────────────────
        // 'total' is the ONLY extra memory this method ever uses,
        // no matter how large arr is.  It lives in this single
        // stack frame and is overwritten on every loop iteration.
        // 0 is the correct initial value (additive identity).
        int total = 0;

        // ── LOOP ──────────────────────────────────────────────
        // Standard for-loop traversal: index i goes 0, 1, ..., n-1.
        //
        // SPACE IMPLICATION: i and total are overwritten each pass.
        // The JVM never accumulates pending work the way recursive
        // frames do.  This loop could run for n = 1,000,000,000
        // and the stack depth would still be exactly 1 frame.
        for (int i = 0; i < arr.length; i++) {

            // Add element at index i to the running total.
            // The previous value of total is discarded immediately —
            // there is no frame holding onto it "for later" the way
            // there would be in the recursive version.
            total += arr[i];
        }

        // Return the completed sum.
        // This POPs the single frame — we're done.
        return total;
    }


    // ==========================================================
    // HELPER — prints stack frame growth side-by-side
    //   Visualises how many frames each approach would need
    //   at different array sizes, without actually running them
    //   to the dangerous sizes.
    // ==========================================================
    static void printSpaceComparison() {
        int[] sizes = {5, 10, 100, 1_000, 5_000, 10_000};

        System.out.println("\n--- Space (stack frame) comparison ---");
        System.out.printf("%-10s %-22s %-22s%n",
                          "n", "recursive frames", "iterative frames");
        System.out.println("-".repeat(56));

        for (int n : sizes) {
            // Recursive: n+1 frames (n recursive calls + 1 base case)
            int recFrames = n + 1;
            // Iterative: always exactly 1 frame regardless of n
            int itrFrames = 1;

            String warning = (n >= 5_000) ? "  <- StackOverflow risk!" : "";
            System.out.printf("%-10d %-22d %-22d%s%n",
                              n, recFrames, itrFrames, warning);
        }
    }


    // ==========================================================
    // MAIN — run both methods, verify same result, show comparison
    // ==========================================================
    public static void main(String[] args) {

        // ── Test arrays ───────────────────────────────────────
        int[] small = {10, 20, 30, 40, 50};         // n=5,  sum=150
        int[] medium = new int[100];                 // n=100, sum=5050
        for (int i = 0; i < medium.length; i++)
            medium[i] = i + 1;                      // {1, 2, 3, ..., 100}

        // ── Correctness check: small array ────────────────────
        System.out.println("=".repeat(52));
        System.out.println("  Sum of Array — Recursive vs Iterative");
        System.out.println("=".repeat(52));

        int recSmall = sumRecursive(small, 0);  // start index = 0
        int itrSmall = sumIterative(small);

        System.out.printf("small arr %s%n", java.util.Arrays.toString(small));
        System.out.printf("  sumRecursive = %d%n", recSmall);
        System.out.printf("  sumIterative = %d%n", itrSmall);
        System.out.printf("  Results match: %b%n", recSmall == itrSmall);

        // ── Correctness check: medium array ───────────────────
        int recMed = sumRecursive(medium, 0);
        int itrMed = sumIterative(medium);

        System.out.printf("%nmedium arr {1..100}%n");
        System.out.printf("  sumRecursive = %d%n", recMed);  // 5050
        System.out.printf("  sumIterative = %d%n", itrMed);  // 5050
        System.out.printf("  Results match: %b%n", recMed == itrMed);

        // ── Complexity summary ────────────────────────────────
        System.out.println("\n--- Complexity summary ---");
        System.out.printf("%-18s %-10s %-10s %s%n",
                          "approach", "time", "space", "stack overflow risk?");
        System.out.println("-".repeat(56));
        System.out.printf("%-18s %-10s %-10s %s%n",
                          "sumRecursive", "O(n)", "O(n)", "YES — for large n");
        System.out.printf("%-18s %-10s %-10s %s%n",
                          "sumIterative", "O(n)", "O(1)", "NO — ever");

        // ── Frame count comparison across sizes ───────────────
        printSpaceComparison();

        // ── Demonstrate StackOverflowError safely ─────────────
        System.out.println("\n--- Demonstrating StackOverflow on large n ---");
        int[] huge = new int[20_000]; // 20,000 elements
        java.util.Arrays.fill(huge, 1);

        // Iterative handles it fine — O(1) space
        int itrHuge = sumIterative(huge);
        System.out.printf("sumIterative(n=20000) = %d  (no problem)%n", itrHuge);

        // Recursive will crash — O(n) space overflows the JVM stack
        try {
            int recHuge = sumRecursive(huge, 0);
            // May or may not crash depending on JVM stack size setting.
            // Run with -Xss512k to reliably reproduce the error.
            System.out.printf("sumRecursive(n=20000) = %d  (lucky — stack was big enough)%n",
                              recHuge);
        } catch (StackOverflowError e) {
            // StackOverflowError extends Error, not Exception.
            // It must be caught explicitly as StackOverflowError
            // or its superclass Error — a plain catch(Exception e)
            // will NOT catch it.
            System.out.println("sumRecursive(n=20000) -> StackOverflowError! " +
                               "(20001 frames exceeded JVM stack limit)");
        }
    }
}
