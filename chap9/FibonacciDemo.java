/**
 * FibonacciDemo contains an illustration of the Fibonacci sequence calculation.
 * This demo showcases three approaches: naive recursion, tail recursion, and
 * a conceptual bridge to Dynamic Programming patterns.
 */
public class FibonacciDemo {

    /**
     * Calculates the nth Fibonacci number using pure recursion.
     * <p>
     * WARNING: This implementation has an exponential time complexity, O(2^n),
     * due to redundant recalculations of the same values (e.g., calculating
     * fib(3) multiple times when finding fib(5)). This approach is highly
     * inefficient and should never be used for production code.
     * </p>
     * @param n The index of the Fibonacci number to calculate (n >= 0).
     * @return The nth Fibonacci number.
     * @throws IllegalArgumentException if n is negative.
     */
    public static long fibNaive(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Fibonacci index cannot be negative.");
        }
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        // Highly inefficient: recalculates fib(n-2) and fib(n-1) independently,
        // leading to overlapping subproblems.
        return fibNaive(n - 1) + fibNaive(n - 2);
    }

        /**
     * Calculates the nth Fibonacci number using an optimized, tail-recursive approach.
     * <p>
     * This iterative pattern reduces the time complexity to O(n) by keeping track
     * only of the two preceding numbers, thus avoiding the exponential recalculations
     * present in the naive method. This structure mimics the conceptual step toward
     * Dynamic Programming.
     * </p>
     * @param n The index of the Fibonacci number to calculate (n >= 0).
     * @return The nth Fibonacci number.
     * @throws IllegalArgumentException if n is negative.
     */
    public static long fibTailRecursive(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Fibonacci index cannot be negative.");
        }
        if (n == 0) {
            return 0;
        }

        long a = 0; // Represents fib(i-2)
        long b = 1; // Represents fib(i-1)

        // Start loop from i=2 up to n
        for (int i = 2; i <= n; i++) {
            long next = a + b;
            a = b; // The previous 'b' becomes the new 'a'
            b = next; // The newly calculated 'next' becomes the new 'b'
        }
        return b;
    }
}
