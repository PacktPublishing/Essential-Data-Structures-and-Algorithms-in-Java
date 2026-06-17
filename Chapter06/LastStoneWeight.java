import java.util.*;

public class LastStoneWeight {
    public int lastStoneWeight(int[] stones) {
        // Create a max-heap to always access the heaviest stones
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        
        // Add all stones to the heap
        for (int stone : stones) {
            maxHeap.offer(stone);
        }
        
        // Continue until at most one stone remains
        while (maxHeap.size() > 1) {
            // Get the two heaviest stones
            int first = maxHeap.poll();   // Heaviest
            int second = maxHeap.poll();  // Second heaviest
            
            // If they are not equal, put the difference back
            if (first != second) {
                maxHeap.offer(first - second);
            }
            // If they are equal, both are destroyed (nothing added back)
        }
        
        // Return the weight of remaining stone, or 0 if none
        return maxHeap.isEmpty() ? 0 : maxHeap.poll();
    }
    
    public static void main(String[] args) {
        LastStoneWeight solution = new LastStoneWeight();
        
        // Test case 1
        int[] stones1 = {2, 7, 4, 1, 8, 1};
        System.out.println("Test 1: " + solution.lastStoneWeight(stones1)); // Expected: 1
        
        // Test case 2
        int[] stones2 = {1};
        System.out.println("Test 2: " + solution.lastStoneWeight(stones2)); // Expected: 1
    }
}

