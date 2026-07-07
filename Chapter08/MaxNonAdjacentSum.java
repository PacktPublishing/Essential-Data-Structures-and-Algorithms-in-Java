public class MaxNonAdjacentSum {
    public int maxSum(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        // [1] dp[i] = max sum from indices 0..i without choosing adjacent elements
        int[] dp = new int[n];
        dp[0] = nums[0];                           // [2] Only one element: take it
        dp[1] = Math.max(nums[0], nums[1]);        // [3] Two elements: pick the larger

        for (int i = 2; i < n; i++) {
            // [4] Include current: add nums[i] + best result up to i-2
            // [5] Exclude current: carry forward best result from i-1
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        return dp[n - 1];
    }

    // [6] Space-optimized version
    public int maxSumOptimized(int[] nums) {
        int prev2 = 0, prev1 = 0;
        for (int num : nums) {
            int curr = Math.max(prev1, prev2 + num);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }

    public static void main(String[] args) {
        MaxNonAdjacentSum solution = new MaxNonAdjacentSum();
        System.out.println(solution.maxSum(new int[]{1, 2, 3, 1}));       // Output: 4
        System.out.println(solution.maxSum(new int[]{2, 7, 9, 3, 1}));    // Output: 12
        System.out.println(solution.maxSumOptimized(new int[]{2, 1, 1, 2})); // Output: 4
    }
}

