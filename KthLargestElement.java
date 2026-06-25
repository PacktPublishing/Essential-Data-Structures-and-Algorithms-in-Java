import java.util.*;

public class KthLargestElement {
    
    // Method 1: Using Sorting (Simple but not optimal)
    public static int findKthLargestSorting(int[] nums, int k) {
        // Step 1: Sort the array in ascending order
        // Time Complexity: O(n log n)
        // Space Complexity: O(1) if we sort in-place
        Arrays.sort(nums);
        
        // Step 2: Return the kth largest element from the end
        // Since array is sorted in ascending order, kth largest is at index (n - k)
        return nums[nums.length - k];
    }
    
    // Method 2: Using Min-Heap (Optimal for this problem)
    public static int findKthLargestHeap(int[] nums, int k) {
        // Step 1: Create a min-heap (priority queue) with capacity k
        // A min-heap will keep the smallest element at the root
        // This ensures that we only keep the k largest elements
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // Step 2: Process each element in the array
        for (int num : nums) {
            // Step 3: If heap size is less than k, add the element
            if (minHeap.size() < k) {
                minHeap.offer(num);
            }
            // Step 4: If current element is larger than the smallest in heap
            else if (num > minHeap.peek()) {
                // Remove the smallest and add the current element
                minHeap.poll();
                minHeap.offer(num);
            }
        }
        
        // Step 5: The root of min-heap contains the kth largest element
        return minHeap.peek();
    }
    
    // Method 3: Using QuickSelect (Most Optimal)
    public static int findKthLargestQuickSelect(int[] nums, int k) {
        // Step 1: Use QuickSelect algorithm (based on QuickSort partitioning)
        // QuickSelect finds the kth element without fully sorting
        return quickSelect(nums, 0, nums.length - 1, k);
    }
    
    // Helper method for QuickSelect algorithm
    private static int quickSelect(int[] nums, int left, int right, int k) {
        // Step 1: Base case - if left equals right, we found our element
        if (left == right) {
            return nums[left];
        }
        
        // Step 2: Partition the array and get pivot index
        int pivotIndex = partition(nums, left, right);
        
        // Step 3: If pivot is at the kth position, we found our answer
        if (pivotIndex == k - 1) {
            return nums[pivotIndex];
        }
        // Step 4: If pivot is to the right of k, search left subarray
        else if (pivotIndex > k - 1) {
            return quickSelect(nums, left, pivotIndex - 1, k);
        }
        // Step 5: If pivot is to the left of k, search right subarray
        else {
            return quickSelect(nums, pivotIndex + 1, right, k);
        }
    }
    
    // Partition method for QuickSelect (similar to QuickSort)
    private static int partition(int[] nums, int left, int right) {
        // Step 1: Choose the rightmost element as pivot
        int pivot = nums[right];
        
        // Step 2: Index of smaller element (indicates right position of pivot)
        int i = left - 1;
        
        // Step 3: Traverse through all elements
        for (int j = left; j < right; j++) {
            // Step 4: If current element is greater than or equal to pivot
            // (We want kth largest, so we sort in descending order)
            if (nums[j] >= pivot) {
                i++; // Increment index of smaller element
                swap(nums, i, j); // Swap elements
            }
        }
        
        // Step 5: Place pivot in its correct position
        swap(nums, i + 1, right);
        return i + 1; // Return the partition index
    }
    
    // Helper method to swap two elements
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    public static void main(String[] args) {
        // Test cases
        int[] nums1 = {3, 2, 1, 5, 6, 4};
        int k1 = 2;
        System.out.println("Test 1:");
        System.out.println("Array: " + Arrays.toString(nums1));
        System.out.println("k = " + k1);
        System.out.println("Sorting approach: " + findKthLargestSorting(nums1, k1));
        System.out.println("Heap approach: " + findKthLargestHeap(nums1, k1));
        System.out.println("QuickSelect approach: " + findKthLargestQuickSelect(nums1, k1));
        
        int[] nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int k2 = 4;
        System.out.println("\nTest 2:");
        System.out.println("Array: " + Arrays.toString(nums2));
        System.out.println("k = " + k2);
        System.out.println("Sorting approach: " + findKthLargestSorting(nums2, k2));
        System.out.println("Heap approach: " + findKthLargestHeap(nums2, k2));
        System.out.println("QuickSelect approach: " + findKthLargestQuickSelect(nums2, k2));
        
        System.out.println("\n=== Analysis ===");
        System.out.println("Approach 1 (Sorting):");
        System.out.println("- Time: O(n log n)");
        System.out.println("- Space: O(1)");
        System.out.println("- Simple but not optimal for large datasets");
        
        System.out.println("\nApproach 2 (Min-Heap):");
        System.out.println("- Time: O(n log k)");
        System.out.println("- Space: O(k)");
        System.out.println("- Good when k is much smaller than n");
        
        System.out.println("\nApproach 3 (QuickSelect):");
        System.out.println("- Time: O(n) average, O(n²) worst case");
        System.out.println("- Space: O(1)");
        System.out.println("- Most optimal for this specific problem");
    }
}

