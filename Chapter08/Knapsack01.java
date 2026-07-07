public class Knapsack01 {
    public int knapsack(int[] weights, int[] values, int W) {
        int n = weights.length;
        // dp[i][w] = max value using first i items with capacity w
        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                // Option 1: skip item i — inherit from previous row
                dp[i][w] = dp[i - 1][w];
                // Option 2: take item i if it fits — add its value
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i][w],
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]);
                }
            }
        }
        return dp[n][W];
    }

    // Space-optimized 1D version — MUST iterate w backwards to stay 0/1
    public int knapsack1D(int[] weights, int[] values, int W) {
        int[] dp = new int[W + 1];
        for (int i = 0; i < weights.length; i++) {
            // [5] Backwards prevents using item i more than once
            for (int w = W; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }
        return dp[W];
    }

    public static void main(String[] args) {
        Knapsack01 ks = new Knapsack01();
        int[] w = {2, 3, 4, 5};
        int[] v = {3, 4, 5, 6};
        System.out.println(ks.knapsack(w, v, 8));    // 10  (items 1,2,3: w=2+3+4=9? no, items 0,2: 3+5=8 fits? 2+4=6≤8 → 8; items 1,2: 3+4=7≤8 → 9; items 0,1,2? 2+3+4=9>8; best: items 1,3: 3+5=8, val=4+6=10)
        System.out.println(ks.knapsack1D(w, v, 8));  // 10
    }
}

