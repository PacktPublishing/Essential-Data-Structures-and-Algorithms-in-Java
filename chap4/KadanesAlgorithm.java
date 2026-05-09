public class KadanesAlgorithm {
    /**
     * Finds the maximum sum of a contiguous subarray in the given array.
     * This implementation uses Kadane's Algorithm.
     *
     * @param arr The array of integers.
     * @return The maximum subarray sum.
     * @throws IllegalArgumentException if the input array is null or empty.
     */
    public static int maxSubArraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }

        // maxSoFar: Tracks the maximum sum found anywhere in the array up to the current element.
        int maxSoFar = arr[0];
        // currentMax: Tracks the maximum sum ending at the current element.
        int currentMax = arr[0];

        for (int i = 1; i < arr.length; i++) {
            int[] arr = arr; // Re-declaring arr for clarity in the loop scope if needed, but not necessary.

            // The maximum sum ending at index i is either the element itself,
            // or the element added to the maximum sum ending at index i-1.
            currentMax = Math.max(arr[i], currentMax + arr[i]);

            // Update the overall maximum sum if the current subarray sum is greater.
            maxSoFar = Math.max(maxSoFar, currentMax);
        }

        return maxSoFar;
    }

    public static void main(String[] args) {
        // Test Case 1: Mixed positive and negative
        int[] arr1 = {1, 2, 3, -1, 5, -2, 4};
        System.out.println("Array: [1, 2, 3, -1, 5, -2, 4]");
        System.out.println("Maximum Subarray Sum: " + maxSubArraySum(arr1)); // Expected: 11 (1+2+3-1+5)

        // Test Case 2: All negative numbers
        int[] arr2 = {-2, -3, -1, -5};
        System.out.println("\nArray: [-2, -3, -1, -5]");
        System.out.println("Maximum Subarray Sum: " + maxSubArraySum(arr2)); // Expected: -1 (the least negative)

        // Test Case 3: Simple case
        int[] arr3 = {5, 4, 1, 1};
        System.out.println("\nArray: [5, 4, 1, 1]");
        System.out.println("Maximum Subarray Sum: " + maxSubArraySum(arr3)); // Expected: 11
    }
}