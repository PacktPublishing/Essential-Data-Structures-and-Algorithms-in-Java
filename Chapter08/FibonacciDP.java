import java.util.*;

public class FibonacciDP {
    
    // Top-down memoization approach
    public static long fibonacciMemo(int n, long[] memo) {
        if (n <= 1) return n;
        if (memo[n] != 0) return memo[n];
        memo[n] = fibonacciMemo(n - 1, memo) + fibonacciMemo(n - 2, memo);
        return memo[n];
    }
    
    // Bottom-up tabulation approach
    public static long fibonacciTab(int n) {
        if (n <= 1) return n;
        long[] dp = new long[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
    
    public static void main(String[] args) {
        int n = 45;
        
        // Test memoization
        long[] memo = new long[n + 1];
        long startTime = System.nanoTime();
        long result1 = fibonacciMemo(n, memo);
        long endTime = System.nanoTime();
        System.out.println("Memoization result: " + result1 + 
                          ", Time: " + (endTime - startTime) / 1000000.0 + " ms");
        
        // Test tabulation
        startTime = System.nanoTime();
        long result2 = fibonacciTab(n);
        endTime = System.nanoTime();
        System.out.println("Tabulation result: " + result2 + 
                          ", Time: " + (endTime - startTime) / 1000000.0 + " ms");
    }
}

