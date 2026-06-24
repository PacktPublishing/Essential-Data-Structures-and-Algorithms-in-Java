import java.math.BigInteger;

public class PowerSum {
    /**
     * Calculates S(N) = 2^N - 1 recursively.
     * 
     * @param n The non-negative integer input
     * @return BigInteger result to prevent overflow
     */
    public static BigInteger sumConsecutivePowers(int n) {
        // Constraint: N must be a non-negative integer
        if (n < 0) {
            throw new IllegalArgumentException("N must be a non-negative integer.");
        }
        
        // Base case constraint: For N = 0, the result should be 0
        if (n == 0) {
            return BigInteger.ZERO;
        }

        // Recursive verification of the formula S(N) = 2^N - 1
        // Base case step of the recursion uses bit shifting instead of Math.pow
        if (n == 1) {
            // (1 << 1) - 1 = 1
            return BigInteger.ONE.shiftLeft(1).subtract(BigInteger.ONE);
        }

        // Recursive case: S(N) = S(N-1) + 2^(N-1)
        // Bit shifting (shiftLeft) is used over exponentiation for efficiency
        BigInteger currentTerm = BigInteger.ONE.shiftLeft(n - 1);
        return currentTerm.add(sumConsecutivePowers(n - 1));
    }

    public static void main(String[] args) {
        // Edge case test: N = 0
        System.out.println("N = 0: " + sumConsecutivePowers(0)); // Output: 0
        
        // Standard test: N = 4 (1 + 2 + 4 + 8 = 15)
        System.out.println("N = 4: " + sumConsecutivePowers(4)); // Output: 15
        
        // Large value edge case test: N = 65 (Exceeds standard 64-bit 'long' capacity)
        System.out.println("N = 65: " + sumConsecutivePowers(65)); 
    }
}
