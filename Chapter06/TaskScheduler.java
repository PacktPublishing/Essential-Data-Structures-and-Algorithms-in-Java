import java.util.*;

public class TaskScheduler {
    public int leastInterval(char[] tasks, int n) {
        // Step 1: Count frequency of each task
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char task : tasks) {
            frequencyMap.put(task, frequencyMap.getOrDefault(task, 0) + 1);
        }
        
        // Step 2: Create max-heap based on task frequencies
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        maxHeap.addAll(frequencyMap.values());
        
        int intervals = 0;
        
        // Step 3: Process tasks in batches
        while (!maxHeap.isEmpty()) {
            List<Integer> temp = new ArrayList<>();
            int cycle = n + 1;  // Number of slots in one cycle
            
            // Process up to (n+1) tasks in this cycle
            for (int i = 0; i < cycle; i++) {
                if (!maxHeap.isEmpty()) {
                    int frequency = maxHeap.poll();
                    if (frequency > 1) {
                        temp.add(frequency - 1);  // Reduce frequency
                    }
                    intervals++;
                } else if (temp.isEmpty()) {
                    // No more tasks to process
                    break;
                } else {
                    // Need to wait (idle time)
                    intervals++;
                }
            }
            
            // Add back reduced frequencies to heap
            maxHeap.addAll(temp);
        }
        
        return intervals;
    }
    
    public static void main(String[] args) {
        TaskScheduler solution = new TaskScheduler();
        
        // Test case 1
        char[] tasks1 = {'A','A','A','B','B','B'};
        int n1 = 2;
        System.out.println("Test 1: " + solution.leastInterval(tasks1, n1)); // Expected: 8
        
        // Test case 2
        char[] tasks2 = {'A','A','A','B','B','B'};
        int n2 = 0;
        System.out.println("Test 2: " + solution.leastInterval(tasks2, n2)); // Expected: 6
    }
}

