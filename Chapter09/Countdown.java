/**
 * ============================================================
 * FILE: Countdown.java
 *
 * CALL STACK PRIMER:
 *   Every time a method is invoked, the JVM PUSHES a new "stack
 *   frame" onto the call stack.  That frame owns:
 *     - its own copy of local variables (here: int n)
 *     - a return address (where to resume in the caller)
 *     - any pending expression that is waiting for a sub-call
 *       to finish (here: the unresolved "+ 1")
 *
 *   When the method executes a return statement, its frame is
 *   POPPED and the return value is handed to the frame below.
 * ============================================================
 */
public class Countdown {

    // ----------------------------------------------------------
    // METHOD: countdown(int n)
    //
    // PARAMETER n:
    //   Lives in THIS stack frame's local variable slot.
    //   Each recursive call gets its OWN copy of n in its OWN
    //   frame — frames never share a variable.
    //
    // RETURNS: the value of n (reconstructed through recursion
    //          depth + additions on the way back up the stack)
    // ----------------------------------------------------------
    static int countdown(int n) {

        // ── BASE CASE ─────────────────────────────────────────
        // When n reaches 0 we stop recursing.
        // Without a base case the stack would grow unbounded and
        // the JVM would eventually throw a StackOverflowError.
        //
        // Call-stack effect: this is the DEEPEST frame ever pushed.
        // It returns 0 immediately — no further push happens.
        if (n == 0)
            return 0;   // ← POP this frame; hand the value 0 up to caller

        // ── RECURSIVE CASE ────────────────────────────────────
        // n > 0, so we must go deeper.  Three things happen here:
        //
        //  STEP 1 — countdown(n-1)
        //    The JVM PUSHES a brand-new frame onto the call stack.
        //    Execution in the CURRENT frame SUSPENDS right here.
        //    The "+ 1" that follows cannot run yet — it is PENDING
        //    inside this frame until the deeper call gives back a value.
        //    Using (n-1) shrinks the problem by 1 every level, which
        //    guarantees we eventually reach the base case.
        //
        //  STEP 2 — the deeper call returns
        //    That deeper frame resolves, gets popped, and hands its
        //    integer result back to THIS frame.  Execution resumes
        //    at the "+ 1" arithmetic.
        //
        //  STEP 3 — return (subResult + 1)
        //    THIS frame is now POPPED; the sum is handed up to whoever
        //    called us (either main() or a shallower countdown() frame).
        else
            return countdown(n - 1) + 1;
            //     ^^^^^^^^^^^^^^^^^   ^^^
            //     new frame pushed    added in THIS frame after
            //     here; current       the deeper call returns —
            //     frame suspends      the "unwinding" step
    }

    // ----------------------------------------------------------
    // METHOD: traceCountdown(int n, int depth)
    //
    // PURPOSE: Mirrors countdown() exactly but PRINTS each
    //          push/pop event so you can follow the call stack
    //          live in the console.
    //
    // depth: purely cosmetic — controls indentation to visually
    //        show how deep we currently are in the stack.
    // ----------------------------------------------------------
    static int traceCountdown(int n, int depth) {

        // "  ".repeat(depth) gives 2 spaces per stack level.
        // Deeper frame = more indentation = visual nesting.
        String indent = "  ".repeat(depth);

        // Announce that THIS frame was just pushed onto the stack.
        System.out.println(indent + "PUSH  countdown(" + n
                         + ")  [depth " + depth + "]");

        int result;

        if (n == 0) {
            // Base case: return 0, nothing further pushed.
            result = 0;
            System.out.println(indent + "  → base case hit — returning 0");
        } else {
            // Push a deeper frame first …
            int sub = traceCountdown(n - 1, depth + 1);
            // … then, back in THIS frame after it returns, add 1.
            result = sub + 1;
            System.out.println(indent + "  → received " + sub
                             + " from below, +1 = " + result);
        }

        // Announce that THIS frame is about to be popped.
        System.out.println(indent + "POP   countdown(" + n
                         + ")  returns " + result);
        return result;
    }

    // ----------------------------------------------------------
    // METHOD: main(String[] args)
    //
    // The JVM pushes main's frame FIRST when the program starts.
    // All other frames are pushed/popped while main is running.
    // main's frame is the last one popped when the program ends.
    // ----------------------------------------------------------
    public static void main(String[] args) {

        // ── Correctness check ─────────────────────────────────
        // countdown(n) should always return n.
        // Stored on the heap; the reference lives in main's frame.
        int[] testValues = {0, 1, 3, 5, 10};

        System.out.println("===========================================");
        System.out.println("  Countdown — Correctness Check");
        System.out.println("===========================================");
        System.out.printf("%-10s %-10s %-10s%n", "n", "result", "ok?");
        System.out.println("-------------------------------------------");

        for (int n : testValues) {
            // Each call below pushes a chain of countdown frames,
            // which unwind completely before we reach the next line.
            int result = countdown(n);
            System.out.printf("%-10d %-10d %-10s%n",
                              n, result, result == n ? "YES" : "BUG!");
        }

        System.out.println("===========================================");

        // ── Step-by-step trace ────────────────────────────────
        // Show the exact push/pop sequence for countdown(4).
        System.out.println("\n--- Push/Pop trace for countdown(4) ---\n");
        traceCountdown(4, 0);
        System.out.println("\nFinal answer: " + countdown(4));
    }
}
