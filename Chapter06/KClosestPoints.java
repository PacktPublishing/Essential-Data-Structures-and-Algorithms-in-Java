import java.util.*;

public class KClosestPoints {
    public int[][] kClosest(int[][] points, int k) {
        // Create a max-heap to store the k closest points
        // We use max-heap because we want to keep the farthest among k closest points
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> {
            // Calculate squared distances (avoiding sqrt for efficiency)
            long distA = (long)a[0] * a[0] + (long)a[1] * a[1];
            long distB = (long)b[0] * b[0] + (long)b[1] * b[1];
            // Compare distances in descending order (max-heap)
            return Long.compare(distB, distA);
        });
        
        // Process each point
        for (int[] point : points) {
            // If heap size is less than k, add the point
            if (maxHeap.size() < k) {
                maxHeap.offer(point);
            }
            // If current point is closer than the farthest in heap
            else {
                long currentDist = (long)point[0] * point[0] + (long)point[1] * point[1];
                long heapDist = (long)maxHeap.peek()[0] * maxHeap.peek()[0] + 
                               (long)maxHeap.peek()[1] * maxHeap.peek()[1];
                
                if (currentDist < heapDist) {
                    maxHeap.poll();  // Remove farthest point
                    maxHeap.offer(point);  // Add current point
                }
            }
        }
        
        // Extract all points from the heap
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        KClosestPoints solution = new KClosestPoints();
        
        // Test case 1
        int[][] points1 = {{1, 3}, {-2, 2}};
        int k1 = 1;
        int[][] result1 = solution.kClosest(points1, k1);
        System.out.println("Test 1: " + Arrays.deepToString(result1)); // Expected: [[-2, 2]]
        
        // Test case 2
        int[][] points2 = {{3, 3}, {5, -1}, {-2, 4}};
        int k2 = 2;
        int[][] result2 = solution.kClosest(points2, k2);
        System.out.println("Test 2: " + Arrays.deepToString(result2)); // Expected: [[3, 3], [-2, 4]]
    }
}

