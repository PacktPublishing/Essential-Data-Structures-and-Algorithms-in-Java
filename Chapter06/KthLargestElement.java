import java.util.*;

public class KthLargestElement {
    public int findKthLargest(int[] nums, int k) {
        // Create a min-heap to keep track of k largest elements
        // The smallest among the k largest elements will be at the root
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // Iterate through all elements in the array
        for (int num : nums) {
            // If heap size is less than k, simply add the element
            if (minHeap.size() < k) {
                minHeap.offer(num);
            }
            // If current element is larger than the smallest in heap (root)
            else if (num > minHeap.peek()) {
                // Remove the smallest and add the current element
                minHeap.poll();  // Remove root (smallest)
                minHeap.offer(num);  // Add current element
            }
        }
        
        // The root of min-heap contains the kth largest element
        return minHeap.peek();
    }
    
    public static void main(String[] args) {
        KthLargestElement solution = new KthLargestElement();
        
        // Test case 1
        int[] nums1 = {3, 2, 1, 5, 6, 4};
        int k1 = 2;
        System.out.println("Test 1: " + solution.findKthLargest(nums1, k1)); // Expected: 5
        
        // Test case 2
        int[] nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int k2 = 4;
        System.out.println("Test 2: " + solution.findKthLargest(nums2, k2)); // Expected: 4
    }
}

