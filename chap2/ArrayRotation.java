/**
 * Rotates array to the right by k steps using reverse method
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 */
public class ArrayRotation {
    /**
     * Rotates array to the right by k steps
     * @param nums array to rotate
     * @param k number of steps to rotate
     */
    public void rotate(int[] nums, int k) {
        // Handle edge cases
        if (nums == null || nums.length <= 1) return;
        
        // Normalize k to avoid unnecessary full rotations
        k = k % nums.length;
        
        // Step 1: Reverse entire array
        reverse(nums, 0, nums.length - 1);
        
        // Step 2: Reverse first k elements
        reverse(nums, 0, k - 1);
        
        // Step 3: Reverse remaining elements from index k to end
        reverse(nums, k, nums.length - 1);
    }
    
    /**
     * Helper method to reverse array portion from start to end indices
     * @param nums array to reverse
     * @param start starting index
     * @param end ending index
     */
    private void reverse(int[] nums, int start, int end) {
        // Use two pointers approach to swap elements
        while (start < end) {
            int temp = nums[start];      // Store element at start
            nums[start] = nums[end];     // Swap start with end
            nums[end] = temp;            // Complete swap
            start++;                     // Move start pointer forward
            end--;                       // Move end pointer backward
        }
    }
    
    // Helper method to print array
    public void printArray(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(" ");
        }
        System.out.println();
    }
    
    // Example usage
    public static void main(String[] args) {
        ArrayRotation solution = new ArrayRotation();
        
        // Test case 1
        int[] nums1 = {1, 2, 3, 4, 5, 6, 7};
        System.out.print("Test 1 - Before rotation: ");
        solution.printArray(nums1);
        solution.rotate(nums1, 3);
        System.out.print("Test 1 - After rotation: ");
        solution.printArray(nums1);
        
        // Test case 2
        int[] nums2 = {-1, -100, 3, 99};
        System.out.print("Test 2 - Before rotation: ");
        solution.printArray(nums2);
        solution.rotate(nums2, 2);
        System.out.print("Test 2 - After rotation: ");
        solution.printArray(nums2);
    }
}

