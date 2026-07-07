public class LongestCommonSubsequence {

    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        // dp[i][j] = LCS of text1[0..i-1] and text2[0..j-1].
        // Row 0 and col 0 stay 0: the empty prefix has LCS 0 with anything.
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // Characters match: extend the LCS from the diagonal.
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // No match: best of dropping one char from either string.
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];   // full strings compared
    }

// DP table trace ("abcde" vs "ace")
//        ""   a   c   e
//   ""    0   0   0   0
//   a     0   1   1   1
//   b     0   1   1   1
//   c     0   1   2   2
//   d     0   1   2   2
//   e     0   1   2   3   <- answer

// Complexity: Time O(m × n) — every cell filled once in constant work. 
// Space O(m × n) for the table (reducible to O(min(m, n)) — see extensions).

}
