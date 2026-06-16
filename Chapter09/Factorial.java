/**
 * ============================================================
 * FILE: Factorial.java
 *
 * KEY DIFFERENCE FROM countdown():
 *   countdown() added +1 on the way back up the stack.
 *   factorial() MULTIPLIES by n on the way back up.
 *   This matters because each frame must hold onto its OWN
 *   value of n as its pending multiplier while every deeper
 *   frame is still running below it on the stack.
 *
 * CALL STACK TRACE for factorial(4):
 *   PUSH factorial(4) → suspends, holds n=4, calls factorial(3)
 *   PUSH factorial(3) → suspends, holds n=3, calls factorial(2)
 *   PUSH factorial(2) → suspends, holds n=2, calls factorial(1)
 *   PUSH factorial(1) → suspends, holds n=1, calls factorial(0)
 *   PUSH factorial(0) → BASE CASE → returns 1
 *   POP  factorial(0) → returns 1    up to factorial(1)
 *   POP  factorial(1) → 1 × 1  = 1  up to factorial(2)
 *   POP  factorial(2) → 2 × 1  = 2  up to factorial(3)
 *   POP  factorial(3) → 3 × 2  = 6  up to factorial(4)
 *   POP  factorial(4) → 4 × 6  = 24  ← final answer
 * ============================================================
 */
public class Factorial {

    // ----------------------------------------------------------
    // METHOD: factorial(int n)
    //
    // Uses long (64-bit) as the return type because factorials
    // grow explosively: 13! = 6,227,020,800 overflows int (max
    // ~2.1 billion), but fits easily in a long (max ~9.2 × 10^18).
    //
    // PARAMETER n:
    //   Stored in THIS frame's local variable slot on the stack.
    //   factorial(4) has its own n=4, factorial(3) has its own
    //   n=3, etc.  They coexist simultaneously on the stack and
    //   NEVER interfere with each other.
    // ----------------------------------------------------------
    static long factorial(int n) {

        // ── BASE CASE ─────────────────────────────────────────
        // n == 0: stop recursing and return 1.
        //
        // Why 1 and not 0?
        //   1 is the MULTIPLICATIVE IDENTITY — multiplying by 1
        //   leaves any product unchanged.  Every frame above will
        //   compute "n * 1", "n * prev", etc., correctly.
        //   Returning 0 would collapse the whole product chain
        //   to 0 (anything × 0 = 0), which is wrong.
        //
        // Call-stack effect: this is the DEEPEST frame pushed.
        // It returns immediately — no further push occurs.
        if (n == 0)
            return 1;   // ← POP this frame; hand 1 back to the caller

        // ── RECURSIVE CASE ────────────────────────────────────
        // Two things are fused into one return statement:
        //
        //  PART A — factorial(n-1)
        //    The JVM PUSHES a new frame onto the call stack.
        //    THIS frame SUSPENDS here — n is safely preserved in
        //    this frame's local variable slot while the entire
        //    sub-chain below us runs to completion.
        //    (n-1) guarantees progress toward the base case.
        //
        //  PART B — n * (result)
        //    Once the deepest base-case frame resolves and every
        //    frame below us has popped back up to here, THIS frame
        //    multiplies its own preserved n by whatever came back.
        //    The product grows at each level:
        //      level 4: 4 × 6   = 24
        //      level 3: 3 × 2   = 6
        //      level 2: 2 × 1   = 2
        //      level 1: 1 × 1   = 1
        //      level 0: base      1
        //
        //  PART C — return
        //    POP this frame; send the product up to our caller.
        return n * factorial(n - 1);
        //     ^   ^^^^^^^^^^^^^^^^^
        //     |   PUSH new frame here; this frame suspends
        //     |
        //     this frame's n — sitting safely in its own stack
        //     slot while every sub-call runs — becomes the
        //     multiplier the moment control returns here
    }

    // ----------------------------------------------------------
    // METHOD: traceFactorial(int n, int depth)
    //
    // Mirrors factorial() exactly but prints every push/pop
    // event so you can watch the call stack grow and shrink.
    //
    // depth: indentation level — purely cosmetic.  Deeper frame
    //        = more spaces = visual nesting in the console output.
    // ----------------------------------------------------------
    static long traceFactorial(int n, int depth) {

        // 2 spaces per stack level — purely for readability.
        String indent = "  ".repeat(depth);

        // ── Announce PUSH ─────────────────────────────────────
        // This frame just landed on the call stack.
        System.out.println(indent + "PUSH  factorial(" + n
                         + ")  [depth " + depth + "]");

        long result;

        if (n == 0) {
            // Base case: return the multiplicative identity.
            result = 1;
            System.out.println(indent + "  -> base case -- returning 1");
        } else {
            // Push a deeper frame to solve (n-1) …
            long sub = traceFactorial(n - 1, depth + 1);
            // … then, back in THIS frame after the chain unwinds,
            // multiply THIS frame's n by what came back.
            result = n * sub;
            System.out.println(indent + "  -> received " + sub
                             + ", multiplying by n=" + n
                             + " -> " + result);
        }

        // ── Announce POP ──────────────────────────────────────
        // This frame is about to be removed from the call stack.
        System.out.println(indent + "POP   factorial(" + n
                         + ")  returns " + result);
        return result;
    }

    // ----------------------------------------------------------
    // METHOD: main(String[] args)
    //
    // JVM entry point — main's frame is the first pushed and the
    // last popped.  Every countdown/trace frame is nested inside.
    // ----------------------------------------------------------
    public static void main(String[] args) {

        // Test inputs and their known correct answers.
        int[]  tests    = { 0,  1,  4,   5,      10 };
        long[] expected = { 1,  1, 24, 120, 3628800 };

        System.out.println("========================================");
        System.out.printf("%-8s %-14s %-14s %-6s%n",
                          "n", "result", "expected", "ok?");
        System.out.println("----------------------------------------");

        for (int i = 0; i < tests.length; i++) {
            // Each call here pushes a full chain of factorial frames.
            // When factorial() returns, all those frames are gone and
            // we are back inside main's frame with just 'result'.
            long result = factorial(tests[i]);
            String ok   = (result == expected[i]) ? "YES" : "BUG!";
            System.out.printf("%-8d %-14d %-14d %-6s%n",
                              tests[i], result, expected[i], ok);
        }

        System.out.println("========================================");

        // Live push/pop walkthrough for factorial(4).
        System.out.println("\n--- Push/Pop trace for factorial(4) ---\n");
        traceFactorial(4, 0);
    }
}
