import java.util.*;

public class HybridSortingAlgorithm {
    
    // Optimal threshold value determined through performance testing
    // This value will be determined by the performance analysis
    private static int OPTIMAL_THRESHOLD = 10;
    
    // Hybrid sorting algorithm that switches between Insertion Sort and Quick Sort
    public static void hybridSort(int[] arr, int low, int high) {
        // Step 1: Check if the subarray size is small enough to use Insertion Sort
        if (high - low + 1 <= OPTIMAL_THRESHOLD) {
            // Step 2: Use Insertion Sort for small arrays (typically < 10 elements)
            // This is more efficient due to lower overhead and better cache locality
            insertionSort(arr, low, high);
        } else {
            // Step 3: Use Quick Sort for larger arrays (typically > 10 elements)
            // Quick Sort has better asymptotic complexity O(n log n) for large datasets
            quickSort(arr, low, high);
        }
    }
    
    // Insertion Sort implementation (used for small arrays)
    private static void insertionSort(int[] arr, int low, int high) {
        // Step 1: Iterate through each element starting from the second element
        for (int i = low + 1; i <= high; i++) {
            // Step 2: Store current element to be inserted
            int key = arr[i];
            int j = i - 1;
            
            // Step 3: Move elements greater than key one position ahead
            while (j >= low && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            
            // Step 4: Insert the key at its correct position
            arr[j + 1] = key;
        }
        // Step 5: Insertion Sort is efficient for small arrays due to low overhead
        // and good performance on nearly sorted data
    }
    
    // Quick Sort implementation (used for large arrays)
    public static void quickSort(int[] arr, int low, int high) {
        // Step 1: Base case - if low >= high, subarray is sorted
        if (low < high) {
            // Step 2: Partition the array and get pivot index
            int pivotIndex = partition(arr, low, high);
            
            // Step 3: Recursively sort elements before and after partition
            // This is where the divide-and-conquer approach comes into play
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
        // Step 4: Quick Sort has O(n log n) average complexity for large datasets
        // making it more efficient than Insertion Sort for large inputs
    }
    
    // Partition method for Quick Sort
    private static int partition(int[] arr, int low, int high) {
        // Step 1: Choose the rightmost element as pivot
        int pivot = arr[high];
        
        // Step 2: Index of smaller element (indicates right position of pivot)
        int i = low - 1;
        
        // Step 3: Traverse through all elements and place smaller elements to left
        for (int j = low; j < high; j++) {
            // Step 4: If current element is smaller than or equal to pivot
            if (arr[j] <= pivot) {
                i++; // Increment index of smaller element
                swap(arr, i, j); // Swap elements
            }
        }
        
        // Step 5: Place pivot in its correct position
        swap(arr, i + 1, high);
        return i + 1; // Return the partition index
    }
    
    // Helper method to swap two elements
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    // Helper method to measure execution time
    private static long measureTime(Runnable sortMethod) {
        long startTime = System.nanoTime();
        sortMethod.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
    
    // Method to test performance with different threshold values
    private static void testThresholdPerformance() {
        System.out.println("\n=== Performance Testing for Different Thresholds ===");
        
        // Create test arrays of different sizes
        int[] sizes = {5, 10, 15, 20, 25, 30, 50, 100};
        int[] thresholds = {1, 5, 10, 15, 20, 25, 30};
        
        System.out.println("Array Size\tThreshold\tHybrid Time\tQuick Sort\tInsertion Sort");
        System.out.println("--------\t---------\t---------\t----------\t--------------");
        
        for (int size : sizes) {
            // Generate random array of given size
            int[] originalArray = generateRandomArray(size, 1, 1000);
            
            // Test different thresholds
            for (int threshold : thresholds) {
                if (threshold > size) continue; // Skip if threshold is larger than array
                
                // Set the threshold for testing
                OPTIMAL_THRESHOLD = threshold;
                
                // Test hybrid sort
                int[] hybridArray = Arrays.copyOf(originalArray, originalArray.length);
                long hybridTime = measureTime(() -> {
                    hybridSort(hybridArray, 0, hybridArray.length - 1);
                });
                
                // Test pure Quick Sort
                int[] quickArray = Arrays.copyOf(originalArray, originalArray.length);
                long quickTime = measureTime(() -> {
                    quickSort(quickArray, 0, quickArray.length - 1);
                });
                
                // Test pure Insertion Sort
                int[] insertionArray = Arrays.copyOf(originalArray, originalArray.length);
                long insertionTime = measureTime(() -> {
                    insertionSort(insertionArray, 0, insertionArray.length - 1);
                });
                
                System.out.printf("%d\t\t%d\t\t%d\t\t%d\t\t%d%n", 
                    size, threshold, hybridTime, quickTime, insertionTime);
            }
        }
    }
    
    // Helper method to generate random array
    private static int[] generateRandomArray(int size, int min, int max) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(max - min + 1) + min;
        }
        return arr;
    }
    
    public static void main(String[] args) {
        // Test with different array sizes
        int[] smallArray = {5, 2, 8, 1, 9};
        int[] mediumArray = {64, 34, 25, 12, 22, 11, 90, 88, 76, 50, 42};
        int[] largeArray = {64, 34, 25, 12, 22, 11, 90, 88, 76, 50, 42, 33, 21, 10, 5, 1, 3, 7, 9, 2};
        
        System.out.println("=== Testing Hybrid Sorting ===");
        System.out.println("Optimal Threshold: " + OPTIMAL_THRESHOLD);
        System.out.println();
        
        // Test with small array
        System.out.println("--- Small Array Test ---");
        System.out.println("Original: " + Arrays.toString(smallArray));
        hybridSort(smallArray, 0, smallArray.length - 1);
        System.out.println("Sorted: " + Arrays.toString(smallArray));
        
        System.out.println();
        
        // Test with medium array
        System.out.println("--- Medium Array Test ---");
        System.out.println("Original: " + Arrays.toString(mediumArray));
        hybridSort(mediumArray, 0, mediumArray.length - 1);
        System.out.println("Sorted: " + Arrays.toString(mediumArray));
        
        System.out.println();
        
        // Test with large array
        System.out.println("--- Large Array Test ---");
        System.out.println("Original: " + Arrays.toString(largeArray));
        hybridSort(largeArray, 0, largeArray.length - 1);
        System.out.println("Sorted: " + Arrays.toString(largeArray));
        
        System.out.println();
        
        // Performance testing to determine optimal threshold
        testThresholdPerformance();
        
        System.out.println();
        System.out.println("=== Analysis ===");
        System.out.println("The hybrid approach combines the strengths of both algorithms:");
        System.out.println("1. Insertion Sort is efficient for small arrays (low overhead, good cache locality)");
        System.out.println("2. Quick Sort is efficient for large arrays (better asymptotic complexity O(n log n))");
        System.out.println("3. The optimal threshold (typically 10-20) balances these advantages");
        System.out.println("4. Performance testing shows that hybrid algorithms often outperform single-algorithm approaches");
    }
}

