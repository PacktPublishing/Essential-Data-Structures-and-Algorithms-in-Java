import java.util.Arrays;

public class CoinChange {
    public int coinChange(int[] coins, int amount) {
        // dp[a] = minimum coins to reach amount a
        //     Sentinel value: amount+1 means "not yet reachable"
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0; // Base case: 0 coins to make amount 0

        for (int a = 1; a <= amount; a++) {
            for (int coin : coins) {
                if (coin <= a) {
                    // Try using this coin: cost is 1 + best way to make (a - coin)
                    dp[a] = Math.min(dp[a], dp[a - coin] + 1);
                }
            }
        }
        // If still sentinel, amount is unreachable
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        CoinChange cc = new CoinChange();
        System.out.println(cc.coinChange(new int[]{1, 5, 6, 9}, 11)); // 2 (5+6)
        System.out.println(cc.coinChange(new int[]{2}, 3));            // -1
        System.out.println(cc.coinChange(new int[]{1, 2, 5}, 11));    // 3 (5+5+1)
    }
}
