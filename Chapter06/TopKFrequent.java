import java.util.*;

public class TopKFrequent {
    public int[] topKFrequent(int[] nums, int k) {
        // Step 1: Count frequency of each element
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create a min-heap to keep track of k most frequent elements
        // The heap will store elements by their frequency (ascending order)
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = 
            new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());
        
        // Step 3: Process each frequency entry
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (minHeap.size() < k) {
                minHeap.offer(entry);
            } else if (entry.getValue() > minHeap.peek().getValue()) {
                minHeap.poll();  // Remove least frequent
                minHeap.offer(entry);  // Add more frequent
            }
        }
        
        // Step 4: Extract the elements from heap
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll().getKey();
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        TopKFrequent solution = new TopKFrequent();
        
        // Test case 1
        int[] nums1 = {1, 1, 1, 2, 2, 3};
        int k1 = 2;
        int[] result1 = solution.topKFrequent(nums1, k1);
        System.out.println("Test 1: " + Arrays.toString(result1)); // Expected: [1, 2]
        
        // Test case 2
        int[] nums2 = {1};
        int k2 = 1;
        int[] result2 = solution.topKFrequent(nums2, k2);
        System.out.println("Test 2: " + Arrays.toString(result2)); // Expected: [1]
    }
}

