/**
 * ============================================================
 * FILE: RecursionPitfalls.java
 *
 * Five common Java bugs that stem directly from poor base case
 * design, each shown broken then fixed.
 *
 * PITFALL SUMMARY:
 *   1. Missing base case entirely        → StackOverflowError
 *   2. Array access before bounds check  → ArrayIndexOutOfBoundsException
 *   3. Base case condition too narrow    → StackOverflowError
 *   4. Missing null check on structures  → NullPointerException
 *   5. Wrong identity value in base case → silent wrong answer
 * ============================================================
 */
public class RecursionPitfalls {

    // ==========================================================
    // SIMPLE LINKED-LIST NODE — used by Pitfall 4
    // ==========================================================
    static class Node {
        int val;
        Node next;
        Node(int v, Node n) { val = v; next = n; }
    }


    // ==========================================================
    // PITFALL 1 — MISSING BASE CASE ENTIRELY
    // Exception: java.lang.StackOverflowError
    //
    // Root cause: there is no condition that ever stops the
    // recursion.  The method calls itself with (n-1) forever.
    // n passes through 0, then -1, -2, ... without stopping.
    // The JVM call stack has a fixed size (~500-1000 frames on
    // default settings) — once full, the JVM crashes hard.
    // ==========================================================

    /** BROKEN — no base case, infinite recursion. */
    static int sumBroken(int n) {
        // PROBLEM: no if-statement to stop recursion.
        // sumBroken(5) → sumBroken(4) → ... → sumBroken(0)
        //   → sumBroken(-1) → sumBroken(-2) → ... forever
        return n + sumBroken(n - 1);  // ← StackOverflowError
    }

    /** FIXED — base case returns 0 (additive identity). */
    static int sumFixed(int n) {
        // BASE CASE: 0 is the additive identity.
        // sum(0) = 0 is correct and stops the chain.
        if (n == 0) return 0;
        // RECURSIVE CASE: problem shrinks by 1 each call.
        return n + sumFixed(n - 1);
        // sumFixed(5) = 5+4+3+2+1+0 = 15
    }


    // ==========================================================
    // PITFALL 2 — ARRAY ACCESS BEFORE BOUNDS CHECK
    // Exception: java.lang.ArrayIndexOutOfBoundsException
    //
    // Root cause: the method reads arr[i] on the FIRST line,
    // THEN checks if i is in bounds.  When the recursive chain
    // reaches i == arr.length, the read fires before the guard
    // and the JVM throws ArrayIndexOutOfBoundsException.
    // ==========================================================

    /** BROKEN — dereferences arr[i] before the bounds guard. */
    static int sumArrayBroken(int[] arr, int i) {
        int val = arr[i];             // ← BOOM when i == arr.length
        if (i == arr.length) return 0; // guard too late — already crashed
        return val + sumArrayBroken(arr, i + 1);
    }

    /** FIXED — bounds check is the very first statement. */
    static int sumArrayFixed(int[] arr, int i) {
        // BASE CASE: i == arr.length means we walked off the end.
        // This must fire BEFORE any arr[i] access.
        if (i == arr.length) return 0;
        // Only reach here when i is a valid index.
        return arr[i] + sumArrayFixed(arr, i + 1);
    }


    // ==========================================================
    // PITFALL 3 — BASE CASE CONDITION TOO NARROW (WRONG EQUALITY)
    // Exception: java.lang.StackOverflowError
    //
    // Root cause: the step is (n-2), so n takes values
    // 5, 3, 1, -1, -3 ...  The value 0 is never visited, so
    // the check (n == 0) never fires.  Using a range check
    // (n <= 0) catches 0, negatives, and any overshoot.
    // ==========================================================

    /** BROKEN — exact equality can be skipped by the step size. */
    static int countDownBroken(int n) {
        if (n == 0) return 0;         // never reached for odd n
        return countDownBroken(n - 2);// step of 2 jumps over 0
        // countDownBroken(5): 5→3→1→-1→-3→... forever
    }

    /** FIXED — range check guarantees the base case is always hit. */
    static int countDownFixed(int n) {
        // BASE CASE: <= 0 handles zero, negatives, and overshoots.
        if (n <= 0) return 0;
        return countDownFixed(n - 2);
        // countDownFixed(5): 5→3→1→-1 (caught by <=0) → 0
    }


    // ==========================================================
    // PITFALL 4 — NULL CHECK MISSING ON LINKED STRUCTURE
    // Exception: java.lang.NullPointerException
    //
    // Root cause: in a linked list, the node AFTER the last real
    // node is null.  null has no fields — reading .val or .next
    // on null immediately throws NullPointerException.
    // null IS the natural base case for any pointer-linked structure.
    // ==========================================================

    /** BROKEN — accesses node fields before null check. */
    static int listSumBroken(Node n) {
        // PROBLEM: what if n is null? .val and .next don't exist.
        return n.val + listSumBroken(n.next); // ← NullPointerException
    }

    /** FIXED — null check is the very first statement. */
    static int listSumFixed(Node n) {
        // BASE CASE: null signals end-of-list.
        // This must be first — before any field access.
        if (n == null) return 0;
        // Only reach here when n is a valid non-null node.
        return n.val + listSumFixed(n.next);
    }


    // ==========================================================
    // PITFALL 5 — WRONG IDENTITY VALUE IN BASE CASE (SILENT BUG)
    // Exception: NONE — code runs and returns a wrong answer.
    //
    // Root cause: returning 0 instead of 1 from the factorial
    // base case poisons the entire product chain silently.
    // n * 0 = 0 at every level, so factorial(5) returns 0
    // instead of 120.  No exception is thrown — this is the
    // most dangerous category because tests are required to
    // catch it.  The fix: always return the IDENTITY ELEMENT
    // for the operation being accumulated:
    //   addition/subtraction  → 0
    //   multiplication/division → 1
    //   string concatenation  → ""
    //   list building         → new ArrayList<>()
    //   boolean AND           → true
    //   boolean OR            → false
    // ==========================================================

    /** BROKEN — base case returns 0, which zeroes every product. */
    static long factorialBroken(int n) {
        if (n == 0) return 0;  // WRONG: 0 is the additive identity,
                               // NOT the multiplicative identity.
        return n * factorialBroken(n - 1);
        // factorialBroken(5):
        //   5 * 4 * 3 * 2 * 1 * 0 = 0  (wrong — should be 120)
    }

    /** FIXED — base case returns 1 (multiplicative identity). */
    static long factorialFixed(int n) {
        // BASE CASE: 1 is the multiplicative identity.
        // n * 1 = n, so this contributes nothing unwanted to the product.
        if (n == 0) return 1;
        return n * factorialFixed(n - 1);
        // factorialFixed(5):
        //   5 * 4 * 3 * 2 * 1 * 1 = 120  (correct)
    }


    // ==========================================================
    // MAIN — demonstrate all pitfalls and their fixes
    // ==========================================================
    public static void main(String[] args) {

        // ── Pitfall 1 ──────────────────────────────────────────
        System.out.println("=== Pitfall 1: missing base case ===");
        System.out.println("sumFixed(5)  = " + sumFixed(5));  // 15
        try {
            sumBroken(5);  // will throw StackOverflowError
        } catch (StackOverflowError e) {
            // Catching Error (not Exception) — StackOverflowError
            // extends Error, not Exception, in the Java hierarchy.
            System.out.println("sumBroken(5) -> StackOverflowError caught!");
        }

        // ── Pitfall 2 ──────────────────────────────────────────
        System.out.println("\n=== Pitfall 2: array access before bounds check ===");
        int[] arr = {10, 20, 30, 40};
        System.out.println("sumArrayFixed(arr,0) = " + sumArrayFixed(arr, 0)); // 100
        try {
            sumArrayBroken(arr, 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("sumArrayBroken -> ArrayIndexOutOfBoundsException caught!");
        }

        // ── Pitfall 3 ──────────────────────────────────────────
        System.out.println("\n=== Pitfall 3: base case condition too narrow ===");
        System.out.println("countDownFixed(5) = " + countDownFixed(5)); // 0
        try {
            countDownBroken(5);
        } catch (StackOverflowError e) {
            System.out.println("countDownBroken(5) -> StackOverflowError caught!");
        }

        // ── Pitfall 4 ──────────────────────────────────────────
        System.out.println("\n=== Pitfall 4: missing null check on linked list ===");
        // Build the list: 1 -> 2 -> 3 -> null
        Node list = new Node(1, new Node(2, new Node(3, null)));
        System.out.println("listSumFixed(list) = " + listSumFixed(list)); // 6
        try {
            listSumBroken(null); // pass null directly to force the crash
        } catch (NullPointerException e) {
            System.out.println("listSumBroken(null) -> NullPointerException caught!");
        }

        // ── Pitfall 5 ──────────────────────────────────────────
        System.out.println("\n=== Pitfall 5: wrong identity value (silent bug) ===");
        System.out.println("factorialFixed(5)  = " + factorialFixed(5));  // 120
        System.out.println("factorialBroken(5) = " + factorialBroken(5)); // 0 — WRONG!
        // Note: no exception is thrown here — the wrong answer is returned silently.
        // This is why unit tests are essential for catching identity-value bugs.

        System.out.println("\n--- All five pitfalls demonstrated ---");
    }
}
