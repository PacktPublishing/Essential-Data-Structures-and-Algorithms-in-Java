public class KadaneAlgorithm {
    
    public static int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            // Either extend the existing subarray or start a new one
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            // Update the global maximum
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        return maxSoFar;
    }
    
    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("Maximum subarray sum: " + maxSubArray(nums));
        // Output: 6 (subarray [4, -1, 2, 1])
    }
}

