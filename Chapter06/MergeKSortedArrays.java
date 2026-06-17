import java.util.*;

public class MergeKSortedArrays {
    public int[] mergeKArrays(int[][] arrays) {
        // Create a min-heap to keep track of the smallest elements from each array
        // Each heap element will be an array [value, arrayIndex, elementIndex]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // Initialize heap with first element from each array
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) {
                minHeap.offer(new int[]{arrays[i][0], i, 0});
            }
        }
        
        // Result array to store merged elements
        List<Integer> result = new ArrayList<>();
        
        // Process elements from heap
        while (!minHeap.isEmpty()) {
            // Get the smallest element
            int[] current = minHeap.poll();
            int value = current[0];
            int arrayIndex = current[1];
            int elementIndex = current[2];
            
            // Add to result
            result.add(value);
            
            // If there are more elements in the same array, add next element to heap
            if (elementIndex + 1 < arrays[arrayIndex].length) {
                int nextValue = arrays[arrayIndex][elementIndex + 1];
                minHeap.offer(new int[]{nextValue, arrayIndex, elementIndex + 1});
            }
        }
        
        // Convert list to array
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    public static void main(String[] args) {
        MergeKSortedArrays solution = new MergeKSortedArrays();
        
        // Test case 1
        int[][] arrays1 = {{1, 4, 5}, {1, 3, 4}, {2, 6}};
        int[] result1 = solution.mergeKArrays(arrays1);
        System.out.println("Test 1: " + Arrays.toString(result1)); // Expected: [1, 1, 2, 3, 4, 4, 5, 6]
        
        // Test case 2
        int[][] arrays2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[] result2 = solution.mergeKArrays(arrays2);
        System.out.println("Test 2: " + Arrays.toString(result2)); // Expected: [1, 2, 3, 4, 5, 6, 7, 8, 9]
    }
}

