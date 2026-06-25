import java.util.*;

public class MergeIntervals {
    
    // Method 1: Sort and Merge
    public static int[][] merge(int[][] intervals) {
        // Step 1: Handle edge case - empty or single interval
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        
        // Step 2: Sort intervals by start time
        // This ensures we process intervals in chronological order
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        
        // Step 3: Create a list to store merged intervals
        List<int[]> merged = new ArrayList<>();
        
        // Step 4: Process each interval
        for (int[] interval : intervals) {
            // Step 5: If merged list is empty or current interval doesn't overlap
            // with the last interval in merged list, add it as is
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval);
            }
            // Step 6: If current interval overlaps with the last interval,
            // merge them by updating the end time of the last interval
            else {
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], interval[1]);
            }
        }
        
        // Step 7: Convert list back to array
        return merged.toArray(new int[merged.size()][]);
    }
    
    // Helper method to print intervals
    private static void printIntervals(int[][] intervals) {
        System.out.print("[");
        for (int i = 0; i < intervals.length; i++) {
            System.out.print("[" + intervals[i][0] + "," + intervals[i][1] + "]");
            if (i < intervals.length - 1) System.out.print(",");
        }
        System.out.println("]");
    }
    
    public static void main(String[] args) {
        // Test cases
        int[][] intervals1 = {{1,3},{2,6},{8,10},{9,12}};
        System.out.println("Test 1:");
        System.out.print("Input: ");
        printIntervals(intervals1);
        int[][] result1 = merge(intervals1);
        System.out.print("Output: ");
        printIntervals(result1);
        
        System.out.println();
        
        int[][] intervals2 = {{1,4},{4,5}};
        System.out.println("Test 2:");
        System.out.print("Input: ");
        printIntervals(intervals2);
        int[][] result2 = merge(intervals2);
        System.out.print("Output: ");
        printIntervals(result2);
        
        System.out.println();
        
        int[][] intervals3 = {{1,4},{0,4}};
        System.out.println("Test 3:");
        System.out.print("Input: ");
        printIntervals(intervals3);
        int[][] result3 = merge(intervals3);
        System.out.print("Output: ");
        printIntervals(result3);
        
        System.out.println();
        
        int[][] intervals4 = {{1,4},{2,3}};
        System.out.println("Test 4:");
        System.out.print("Input: ");
        printIntervals(intervals4);
        int[][] result4 = merge(intervals4);
        System.out.print("Output: ");
        printIntervals(result4);
        
        System.out.println("\n=== Algorithm Explanation ===");
        System.out.println("1. Sort intervals by start time to process them chronologically");
        System.out.println("2. Iterate through sorted intervals and compare with last merged interval");
        System.out.println("3. If no overlap: add current interval to result");
        System.out.println("4. If overlap exists: merge by extending the end time");
        System.out.println("5. Time Complexity: O(n log n) due to sorting");
        System.out.println("6. Space Complexity: O(1) excluding output space");
    }
}

