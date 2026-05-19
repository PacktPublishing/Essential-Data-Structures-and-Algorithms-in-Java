public class EditDistance {
    
    public static int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        // Create DP table
        int[][] dp = new int[m + 1][n + 1];
        
        // Initialize base cases
        // If word1 is empty, we need j insertions
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        
        // If word2 is empty, we need i deletions
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        
        // Fill the DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // Characters match, no operation needed
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Take minimum of insert, delete, replace operations
                    dp[i][j] = 1 + Math.min(
                        Math.min(dp[i][j - 1], dp[i - 1][j]),  // insert or delete
                        dp[i - 1][j - 1]                       // replace
                    );
                }
            }
        }
        
        return dp[m][n];
    }
    
    public static void main(String[] args) {
        // Test cases
        System.out.println(minDistance("horse", "ros"));     // Output: 3
        System.out.println(minDistance("intention", "execution")); // Output: 5
        System.out.println(minDistance("", "abc"));          // Output: 3
        System.out.println(minDistance("abc", ""));          // Output: 3
        System.out.println(minDistance("same", "same"));     // Output: 0
    }
}

