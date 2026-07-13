import java.util.Arrays;

class Student {
    String name;
    int grade;
    int id;
    
    public Student(String name, int grade, int id) {
        this.name = name;
        this.grade = grade;
        this.id = id;
    }
    
    @Override
    public String toString() {
        return String.format("Name: %s, Grade: %d, ID: %d", name, grade, id);
    }
}

public class SpaceComplexityAnalysis {
    
    // --- IN-PLACE SORT (Quick Sort) ---
    public static void inPlaceSort(Student[] students) {
        // Quick Sort is in-place - it modifies the original array
        // Auxiliary space is only the recursion stack (O(log n))
        quickSort(students, 0, students.length - 1);
    }
    
    private static void quickSort(Student[] arr, int low, int high) {
        if (low < high) {
            // Partition the array - this is the in-place operation
            int pivotIndex = partition(arr, low, high);
            
            // Recursively sort elements before and after partition
            quickSort(arr, low, pivotIndex - 1);   // Left subarray
            quickSort(arr, pivotIndex + 1, high);  // Right subarray
        }
    }
    
    private static int partition(Student[] arr, int low, int high) {
        // Choose the rightmost element as pivot
        Student pivot = arr[high];
        int i = low - 1; // Index of smaller element
        
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (arr[j].grade <= pivot.grade) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }
    
    private static void swap(Student[] arr, int i, int j) {
        Student temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    // --- OUT-OF-PLACE SORT (Merge Sort) ---
    public static void outOfPlaceSort(Student[] students) {
        // Merge Sort is out-of-place - it creates new arrays
        // Auxiliary space is O(n) for temporary arrays
        mergeSort(students, 0, students.length - 1);
    }
    
    private static void mergeSort(Student[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            // Recursively sort both halves
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            
            // Merge the sorted halves
            merge(arr, left, mid, right);
        }
    }
    
    private static void merge(Student[] arr, int left, int mid, int right) {
        // Create temporary arrays - this is the out-of-place aspect
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        Student[] leftArr = new Student[n1];
        Student[] rightArr = new Student[n2];
        
        // Copy data to temporary arrays
        for (int i = 0; i < n1; i++)
            leftArr[i] = arr[left + i];
        for (int j = 0; j < n2; j++)
            rightArr[j] = arr[mid + 1 + j];
        
        // Merge the temporary arrays back
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i].grade <= rightArr[j].grade) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }
        
        // Copy remaining elements
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }
    
    public static void main(String[] args) {
        // Test data
        Student[] students = {
            new Student("Alice", 800, 101),
            new Student("Bob", 500, 102),
            new Student("Charlie", 800, 103),
            new Student("Dana", 700, 104)
        };
        
        System.out.println("--- Original Array ---");
        printArray(students);
        
        // Test In-Place Sort
        Student[] inPlaceStudents = Arrays.copyOf(students, students.length);
        System.out.println("\n--- In-Place Sort (Quick Sort) ---");
        inPlaceSort(inPlaceStudents);
        printArray(inPlaceStudents);
        
        // Test Out-Of-Place Sort
        Student[] outOfPlaceStudents = Arrays.copyOf(students, students.length);
        System.out.println("\n--- Out-Of-Place Sort (Merge Sort) ---");
        outOfPlaceSort(outOfPlaceStudents);
        printArray(outOfPlaceStudents);
    }
    
    private static void printArray(Student[] arr) {
        for (Student s : arr) {
            System.out.println(s);
        }
    }
}

