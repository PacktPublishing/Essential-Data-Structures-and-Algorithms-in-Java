/**
 * Removes duplicates from a sorted array in-place
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 */
public class RemoveDuplicates {
    /**
     * Removes duplicates from sorted array in-place
     * @param nums sorted array with possible duplicates
     * @return new length of array without duplicates
     */
    public int removeDuplicates(int[] nums) {
        // Handle edge case: empty array
        if (nums == null || nums.length == 0) return 0;
        
        // writeIndex points to position where next unique element should be placed
        int writeIndex = 1;
        
        // readIndex iterates through the array starting from index 1
        for (int readIndex = 1; readIndex < nums.length; readIndex++) {
            // If current element is different from previous element
            if (nums[readIndex] != nums[readIndex - 1]) {
                // Place the unique element at writeIndex position
                nums[writeIndex] = nums[readIndex];
                // Move writeIndex forward for next unique element
                writeIndex++;
            }
            // If elements are equal, we skip the duplicate and continue reading
        }
        // writeIndex represents the new length of the array without duplicates
        return writeIndex;
    }
    
    // Example usage
    public static void main(String[] args) {
        RemoveDuplicates solution = new RemoveDuplicates();
        
        // Test case 1
        int[] nums1 = {1, 1, 2};
        int length1 = solution.removeDuplicates(nums1);
        System.out.println("Test 1 - Length: " + length1);
        System.out.print("Result: ");
        for (int i = 0; i < length1; i++) {
            System.out.print(nums1[i] + " ");
        }
        System.out.println();
        
        // Test case 2
        int[] nums2 = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int length2 = solution.removeDuplicates(nums2);
        System.out.println("Test 2 - Length: " + length2);
        System.out.print("Result: ");
        for (int i = 0; i < length2; i++) {
            System.out.print(nums2[i] + " ");
        }
        System.out.println();
    }
}

