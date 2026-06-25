import java.util.*;

public class TopKFrequentElements {
    
    // Method 1: Using HashMap + Sorting
    public static List<Integer> topKFrequentSorting(int[] nums, int k) {
        // Step 1: Count frequency of each element using HashMap
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Convert map entries to list for sorting
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(frequencyMap.entrySet());
        
        // Step 3: Sort entries by frequency in descending order
        entries.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // Step 4: Extract first k elements
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            result.add(entries.get(i).getKey());
        }
        
        return result;
    }
    
    // Method 2: Using HashMap + Min-Heap (Optimal)
    public static List<Integer> topKFrequentHeap(int[] nums, int k) {
        // Step 1: Count frequency of each element using HashMap
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create a min-heap to keep track of k most frequent elements
        // The heap will maintain the smallest frequency element at the root
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(
            (a, b) -> a.getValue().compareTo(b.getValue())
        );
        
        // Step 3: Process each frequency entry
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            // Step 4: If heap size is less than k, add the entry
            if (minHeap.size() < k) {
                minHeap.offer(entry);
            }
            // Step 5: If current frequency is greater than smallest in heap
            else if (entry.getValue() > minHeap.peek().getValue()) {
                // Remove the smallest and add current entry
                minHeap.poll();
                minHeap.offer(entry);
            }
        }
        
        // Step 6: Extract elements from heap to result list
        List<Integer> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll().getKey());
        }
        
        return result;
    }
    
    // Method 3: Using Bucket Sort (Most Optimal)
    public static List<Integer> topKFrequentBucketSort(int[] nums, int k) {
        // Step 1: Count frequency of each element using HashMap
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create buckets where index represents frequency
        // Bucket[i] will contain all elements with frequency i
        List<Integer>[] buckets = new List[nums.length + 1];
        for (int i = 0; i <= nums.length; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Step 3: Place elements in appropriate buckets based on frequency
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int frequency = entry.getValue();
            int element = entry.getKey();
            buckets[frequency].add(element);
        }
        
        // Step 4: Collect elements starting from highest frequency
        List<Integer> result = new ArrayList<>();
        for (int i = buckets.length - 1; i >= 0 && result.size() < k; i--) {
            if (!buckets[i].isEmpty()) {
                result.addAll(buckets[i]);
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        // Test cases
        int[] nums1 = {1, 1, 1, 2, 2, 3};
        int k1 = 2;
        System.out.println("Test 1:");
        System.out.println("Array: " + Arrays.toString(nums1));
        System.out.println("k = " + k1);
        System.out.println("Sorting approach: " + topKFrequentSorting(nums1, k1));
        System.out.println("Heap approach: " + topKFrequentHeap(nums1, k1));
        System.out.println("Bucket Sort approach: " + topKFrequentBucketSort(nums1, k1));
        
        int[] nums2 = {1};
        int k2 = 1;
        System.out.println("\nTest 2:");
        System.out.println("Array: " + Arrays.toString(nums2));
        System.out.println("k = " + k2);
        System.out.println("Sorting approach: " + topKFrequentSorting(nums2, k2));
        System.out.println("Heap approach: " + topKFrequentHeap(nums2, k2));
        System.out.println("Bucket Sort approach: " + topKFrequentBucketSort(nums2, k2));
        
        int[] nums3 = {1, 2, 3, 4, 5};
        int k3 = 3;
        System.out.println("\nTest 3:");
        System.out.println("Array: " + Arrays.toString(nums3));
        System.out.println("k = " + k3);
        System.out.println("Sorting approach: " + topKFrequentSorting(nums3, k3));
        System.out.println("Heap approach: " + topKFrequentHeap(nums3, k3));
        System.out.println("Bucket Sort approach: " + topKFrequentBucketSort(nums3, k3));
        
        System.out.println("\n=== Analysis ===");
        System.out.println("Approach 1 (Sorting):");
        System.out.println("- Time: O(n log n)");
        System.out.println("- Space: O(n)");
        System.out.println("- Simple but not optimal for large datasets");
        
        System.out.println("\nApproach 2 (Min-Heap):");
        System.out.println("- Time: O(n log k)");
        System.out.println("- Space: O(n + k)");
        System.out.println("- Good when k is much smaller than n");
        
        System.out.println("\nApproach 3 (Bucket Sort):");
        System.out.println("- Time: O(n)");
        System.out.println("- Space: O(n)");
        System.out.println("- Most optimal for this specific problem");
        System.out.println("- Works best when frequency range is small");
    }
}

