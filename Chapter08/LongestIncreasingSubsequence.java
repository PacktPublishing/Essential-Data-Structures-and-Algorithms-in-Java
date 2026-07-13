import java.util.Arrays;

public class LongestIncreasingSubsequence {

    // [1] O(n^2) DP approach — intuitive
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        // [2] dp[i] = length of LIS ending at index i; min is 1 (just nums[i])
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int maxLen = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // [3] If nums[j] < nums[i], we can extend the LIS ending at j
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        return maxLen;
    }

    // O(n log n) patience sorting approach
    public int lengthOfLISBinarySearch(int[] nums) {
        // tails[k] = smallest tail element of any 
        // increasing subseq of length k+1
        int n = nums.length;
        if (n == 0) return 0;
        int[] tails = new int[n];
        int size = 0; // Current LIS length

        for (int num : nums) {
            // Binary search: find leftmost position where tails[pos] >= num
            int lo = 0, hi = size;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (tails[mid] < num) lo = mid + 1;
                else hi = mid;
            }
            tails[lo] = num; // Replace or extend tails
            if (lo == size) size++; // num extends the longest subsequence
        }
        return size;
    }

    public static void main(String[] args) {
        LongestIncreasingSubsequence lis = new LongestIncreasingSubsequence();
        int[] nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println(lis.lengthOfLIS(nums1));           // 4  [2,3,7,101]
        System.out.println(lis.lengthOfLISBinarySearch(nums1)); // 4
        int[] nums2 = {0, 1, 0, 3, 2, 3};
        System.out.println(lis.lengthOfLISBinarySearch(nums2)); // 4  [0,1,2,3]
        int[] nums3 = {7, 7, 7, 7};
        System.out.println(lis.lengthOfLISBinarySearch(nums3)); // 1  (strictly increasing)
    }
}
