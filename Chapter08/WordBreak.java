import java.util.*;

public class WordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
        // [1] HashSet for O(1) word lookup
        Set<String> dict = new HashSet<>(wordDict);
        int n = s.length();

        // [2] dp[i] = true if s[0..i-1] can be segmented using dict
        boolean[] dp = new boolean[n + 1];
        dp[0] = true; // [3] Empty prefix is always valid

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                // [4] If s[0..j-1] is segmentable AND s[j..i-1] is a dict word
                //     then s[0..i-1] is segmentable
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // [5] Found one valid split — no need to check more j
                }
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        WordBreak wb = new WordBreak();
        System.out.println(wb.wordBreak("applepenapple",
            Arrays.asList("apple", "pen")));                    // true
        System.out.println(wb.wordBreak("catsandog",
            Arrays.asList("cats", "dog", "sand", "and", "cat"))); // false
    }
}
