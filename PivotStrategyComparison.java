import java.util.*;

public class PivotStrategyComparison {
    
    // Strategy 1: First element as pivot
    public static void quickSortFirstPivot(int[] arr, int low, int high) {
        if (low < high) {
            // Partition the array using first element as pivot
            int pivotIndex = partitionFirst(arr, low, high);
            // Recursively sort elements before and after partition
            quickSortFirstPivot(arr, low, pivotIndex - 1);
            quickSortFirstPivot(arr, pivotIndex + 1, high);
        }
    }
    
    private static int partitionFirst(int[] arr, int low, int high) {
        // Step 1: Select first element as pivot
        int pivot = arr[low];  // First element as pivot
        int i = low + 1;       // Index for elements greater than pivot
        int j = high;          // Index for elements less than pivot
        
        // Step 2: Partition the array
        while (i <= j) {
            // Move i forward while elements are <= pivot
            while (i <= j && arr[i] <= pivot) i++;
            // Move j backward while elements are > pivot
            while (i <= j && arr[j] > pivot) j--;
            // Swap elements if i and j haven't crossed
            if (i <= j) {
                swap(arr, i, j);
                i++;
                j--;
            }
        }
        // Step 3: Place pivot in its correct position
        swap(arr, low, j);
        return j;
    }
    
    // Strategy 2: Random element as pivot
    public static void quickSortRandomPivot(int[] arr, int low, int high) {
        if (low < high) {
            // Partition the array using random element as pivot
            int pivotIndex = partitionRandom(arr, low, high);
            // Recursively sort elements before and after partition
            quickSortRandomPivot(arr, low, pivotIndex - 1);
            quickSortRandomPivot(arr, pivotIndex + 1, high);
        }
    }
    
    private static int partitionRandom(int[] arr, int low, int high) {
        // Step 1: Generate random index between low and high
        Random rand = new Random();
        int randomIndex = low + rand.nextInt(high - low + 1);
        
        // Step 2: Swap random element with first element to make it pivot
        swap(arr, low, randomIndex);
        
        // Step 3: Continue with standard partitioning using arr[low] as pivot
        int pivot = arr[low];
        int i = low + 1;
        int j = high;
        
        while (i <= j) {
            while (i <= j && arr[i] <= pivot) i++;
            while (i <= j && arr[j] > pivot) j--;
            if (i <= j) {
                swap(arr, i, j);
                i++;
                j--;
            }
        }
        swap(arr, low, j);
        return j;
    }
    
    // Strategy 3: Median-of-three pivot
    public static void quickSortMedianPivot(int[] arr, int low, int high) {
        if (low < high) {
            // Partition the array using median-of-three as pivot
            int pivotIndex = partitionMedian(arr, low, high);
            // Recursively sort elements before and after partition
            quickSortMedianPivot(arr, low, pivotIndex - 1);
            quickSortMedianPivot(arr, pivotIndex + 1, high);
        }
    }
    
    private static int partitionMedian(int[] arr, int low, int high) {
        // Step 1: Find median of first, middle, and last elements
        int first = arr[low];
        int middle = arr[low + (high - low) / 2];
        int last = arr[high];
        
        // Find the median among these three
        int median = findMedian(first, middle, last);
        
        // Step 2: Swap the median element with the first element
        if (median == first) {
            // Median is already at first position, no swap needed
        } else if (median == middle) {
            // Swap middle with first
            swap(arr, low, low + (high - low) / 2);
        } else {
            // Swap last with first
            swap(arr, low, high);
        }
        
        // Step 3: Continue with standard partitioning using arr[low] as pivot
        int pivot = arr[low];
        int i = low + 1;
        int j = high;
        
        while (i <= j) {
            while (i <= j && arr[i] <= pivot) i++;
            while (i <= j && arr[j] > pivot) j--;
            if (i <= j) {
                swap(arr, i, j);
                i++;
                j--;
            }
        }
        swap(arr, low, j);
        return j;
    }
    
    // Helper method to find median of three numbers
    private static int findMedian(int a, int b, int c) {
        if (a <= b && a <= c) {
            return (b <= c) ? b : c;  // a is minimum
        } else if (b <= a && b <= c) {
            return (a <= c) ? a : c;  // b is minimum
        } else {
            return (a <= b) ? a : b;  // c is minimum
        }
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
    
    public static void main(String[] args) {
        // Test with different datasets
        int[] sortedArray = {1, 2, 3, 4, 5};
        int[] reverseArray = {5, 4, 3, 2, 1};
        int[] randomArray = {3, 1, 4, 2, 5};
        
        System.out.println("=== Testing Pivot Strategies ===");
        System.out.println("Array sizes: " + sortedArray.length);
        System.out.println();
        
        // Test with sorted array
        System.out.println("--- Sorted Array Test ---");
        System.out.println("Original: " + Arrays.toString(sortedArray));
        
        // Test First Pivot Strategy
        int[] testArray1 = Arrays.copyOf(sortedArray, sortedArray.length);
        long time1 = measureTime(() -> quickSortFirstPivot(testArray1, 0, testArray1.length - 1));
        System.out.println("First Pivot Time: " + time1 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray1));
        
        // Test Random Pivot Strategy
        int[] testArray2 = Arrays.copyOf(sortedArray, sortedArray.length);
        long time2 = measureTime(() -> quickSortRandomPivot(testArray2, 0, testArray2.length - 1));
        System.out.println("Random Pivot Time: " + time2 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray2));
        
        // Test Median Pivot Strategy
        int[] testArray3 = Arrays.copyOf(sortedArray, sortedArray.length);
        long time3 = measureTime(() -> quickSortMedianPivot(testArray3, 0, testArray3.length - 1));
        System.out.println("Median Pivot Time: " + time3 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray3));
        
        System.out.println();
        
        // Test with reverse-sorted array
        System.out.println("--- Reverse-Sorted Array Test ---");
        System.out.println("Original: " + Arrays.toString(reverseArray));
        
        // Test First Pivot Strategy
        int[] testArray4 = Arrays.copyOf(reverseArray, reverseArray.length);
        long time4 = measureTime(() -> quickSortFirstPivot(testArray4, 0, testArray4.length - 1));
        System.out.println("First Pivot Time: " + time4 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray4));
        
        // Test Random Pivot Strategy
        int[] testArray5 = Arrays.copyOf(reverseArray, reverseArray.length);
        long time5 = measureTime(() -> quickSortRandomPivot(testArray5, 0, testArray5.length - 1));
        System.out.println("Random Pivot Time: " + time5 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray5));
        
        // Test Median Pivot Strategy
        int[] testArray6 = Arrays.copyOf(reverseArray, reverseArray.length);
        long time6 = measureTime(() -> quickSortMedianPivot(testArray6, 0, testArray6.length - 1));
        System.out.println("Median Pivot Time: " + time6 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray6));
        
        System.out.println();
        
        // Test with random array
        System.out.println("--- Random Array Test ---");
        System.out.println("Original: " + Arrays.toString(randomArray));
        
        // Test First Pivot Strategy
        int[] testArray7 = Arrays.copyOf(randomArray, randomArray.length);
        long time7 = measureTime(() -> quickSortFirstPivot(testArray7, 0, testArray7.length - 1));
        System.out.println("First Pivot Time: " + time7 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray7));
        
        // Test Random Pivot Strategy
        int[] testArray8 = Arrays.copyOf(randomArray, randomArray.length);
        long time8 = measureTime(() -> quickSortRandomPivot(testArray8, 0, testArray8.length - 1));
        System.out.println("Random Pivot Time: " + time8 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray8));
        
        // Test Median Pivot Strategy
        int[] testArray9 = Arrays.copyOf(randomArray, randomArray.length);
        long time9 = measureTime(() -> quickSortMedianPivot(testArray9, 0, testArray9.length - 1));
        System.out.println("Median Pivot Time: " + time9 + " nanoseconds");
        System.out.println("Result: " + Arrays.toString(testArray9));
        
        System.out.println();
        System.out.println("=== Analysis ===");
        System.out.println("The median-of-three strategy typically performs best on sorted/reverse-sorted data");
        System.out.println("because it avoids the worst-case O(n²) performance of first-element pivot selection.");
        System.out.println("Random pivot selection provides good average performance by avoiding worst-case inputs.");
    }
}

