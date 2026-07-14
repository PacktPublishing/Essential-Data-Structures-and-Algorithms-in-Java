import java.util.Arrays;

/**
 * MergeSortDemo provides a complete, heavily annotated implementation of Merge Sort,
 * including an explicit theoretical analysis of its time complexity based on
 * the Master Theorem.
 */
public class MergeSortDemo {

    public static void main(String[] args) {
        int[] arr = {12, 11, 13, 5, 6, 7};
        System.out.println("Original array: " + Arrays.toString(arr));
        
        int[] arrCopy = Arrays.copyOf(arr, arr.length);
        mergeSort(arrCopy, 0, arrCopy.length - 1);
        
        System.out.println("Sorted array:   " + Arrays.toString(arrCopy));
    }

    /**
     * Divides the array recursively into halves.
     * 
     * TIME COMPLEXITY ANALYSIS (Master Theorem):
     * 1. Recurrence Relation: T(n) = 2T(n/2) + O(n)
     * 2. Parameters: a = 2 (subproblems), b = 2 (division factor), f(n) = O(n) (merge work)
     * 3. Critical Value: n^(log_b a) = n^(log_2 2) = n^1 = n
     * 4. Master Theorem Case: Since f(n) = Theta(n), this falls under Case 2 (Balanced Scenario).
     *    Formula: T(n) = Theta(n^(log_b a) * log n)
     *    Result:  T(n) = Theta(n log n) across Best, Average, and Worst cases.
     * 
     * SPACE COMPLEXITY: O(n) due to auxiliary temporary arrays allocated during merging.
     */
    public static void mergeSort(int[] arr, int left, int right) {
        // Base Case: An array of size 0 or 1 is already sorted
        if (left >= right) {
            return;
        }

        // Prevents integer overflow compared to (left + right) / 2
        int mid = left + (right - left) / 2;

        // Divide phase: Concurrently split the problem down the tree height (log n levels)
        mergeSort(arr, left, mid);      
        mergeSort(arr, mid + 1, right); 

        // Conquer phase: Combine solutions linearly at the current level
        merge(arr, left, mid, right);
    }

    /**
     * Linearly merges two sorted contiguous sub-arrays into a single sorted segment.
     * Time Complexity: O(n) where n is the number of elements in the range [left, right].
     */
    public static void merge(int[] arr, int left, int mid, int right) {
        // KEY CHOICE: Allocation of an auxiliary tracking buffer
        int[] temp = new int[right - left + 1];
        
        int i = left;      // Starting pointer for the left sorted partition
        int j = mid + 1;   // Starting pointer for the right sorted partition
        int k = 0;         // Target pointer for our temporary array

        // Step 1: Compare elements side-by-side and pull the smaller value
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) { 
                temp[k] = arr[i];
                i++;
            } else {
                temp[k] = arr[j];
                j++;
            }
            k++;
        }

        // Step 2: Copy leftover elements from the left side if any remain
        while (i <= mid) {
            temp[k] = arr[i];
            i++;
            k++;
        }

        // Step 3: Copy leftover elements from the right side if any remain
        while (j <= right) {
            temp[k] = arr[j];
            j++;
            k++;
        }

        // Step 4: Drain temporary buffer back into the original working array
        for (int p = 0; p < temp.length; p++) {
            arr[left + p] = temp[p];
        }
    }
}
