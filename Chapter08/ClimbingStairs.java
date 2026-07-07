public class ClimbingStairs {
    public int climbStairs(int n) {
        if (n <= 2) return n;

        // dp[i] = number of ways to reach step i
        int[] dp = new int[n + 1];
        dp[1] = 1;  // one way to reach step 1: take one 1-step
        dp[2] = 2;  // two ways to reach step 2: (1+1) or (2)

        for (int i = 3; i <= n; i++) {
            // Reach step i from step i-1 (take 1 step)
            // OR from step i-2 (take 2 steps)
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
