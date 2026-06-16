public class CoinChange {
    
    public static int coinChange(int[] coins, int amount) {
        // dp[i] represents minimum coins needed to make amount i
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // Initialize with a value larger than any possible answer
        dp[0] = 0; // Base case: 0 coins needed for amount 0
        
        // For each amount from 1 to target amount
        for (int i = 1; i <= amount; i++) {
            // Try each coin denomination
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    public static void main(String[] args) {
        int[] coins1 = {1, 3, 4};
        int amount1 = 6;
        System.out.println("Minimum coins for " + amount1 + ": " + coinChange(coins1, amount1));
        // Output: 2 (3 + 3)
        
        int[] coins2 = {2};
        int amount2 = 3;
        System.out.println("Minimum coins for " + amount2 + ": " + coinChange(coins2, amount2));
        // Output: -1 (impossible)
    }
}

