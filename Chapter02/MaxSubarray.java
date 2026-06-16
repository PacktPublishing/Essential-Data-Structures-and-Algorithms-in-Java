/**
 * Finds maximum sum of contiguous subarray (Kadane's algorithm)
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 */
public class MaxSubarray {
    /**
     * Finds maximum sum of contiguous subarray
     * @param nums array of integers
     * @return maximum sum of contiguous subarray
     */
    public int maxSubArray(int[] nums) {
        // Handle edge case
        if (nums == null || nums.length == 0) return 0;
        
        // Initialize variables to track maximum sums
        int maxSoFar = nums[0];      // Maximum sum found so far
        int maxEndingHere = nums[0]; // Maximum sum ending at current position
        
        // Iterate through array starting from second element
        for (int i = 1; i < nums.length; i++) {
            // Either extend existing subarray or start new one
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            
            // Update global maximum if current sum is larger
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        // Return the maximum sum found
        return maxSoFar;
    }
    
    // Example usage
    public static void main(String[] args) {
        MaxSubarray solution = new MaxSubarray();
        
        // Test case 1
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int result1 = solution.maxSubArray(nums1);
        System.out.println("Test 1 - Maximum subarray sum: " + result1);
        
        // Test case 2
        int[] nums2 = {1};
        int result2 = solution.maxSubArray(nums2);
        System.out.println("Test 2 - Maximum subarray sum: " + result2);
    }
}

