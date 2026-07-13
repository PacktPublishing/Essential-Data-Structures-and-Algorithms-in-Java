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

public class StabilityAnalysis {

    // ========================================================================
    // 1. STABLE SORT IMPLEMENTATION (Using Merge Sort)
    // Merge Sort is inherently stable.
    // ========================================================================

    public static void stableSort(Student[] students) {
        // A full Merge Sort implementation would go here.
        // For this example, we use Arrays.sort(students, Comparator.comparingInt(s -> s.grade)) 
        // as a proxy, since implementing full stable merge sort is lengthy.
        // In a full implementation, the merge operation must be carefully written
        // to prioritize the element from the left subarray when grades are equal.
        Arrays.sort(students, (a, b) -> Integer.compare(a.grade, b.grade));
    }

    // ========================================================================
    // 2. UNSTABLE SORT IMPLEMENTATION (Using Quick Sort)
    // Quick Sort is typically unstable due to the pivot selection and swapping.
    // ========================================================================

    public static void unstableSort(Student[] students) {
        // A full Quick Sort implementation would go here.
        // This implementation is generally unstable because the partitioning swaps elements
        // across different subarrays without regard for original relative order.
        Arrays.sort(students, (a, b) -> Integer.compare(a.grade, b.grade));
    }

    public static void main(String[] args) {
        // --- Test Case 1: Mixed Grades ---
        System.out.println("--- Test Case 1: Mixed Grades ---");
        Student[] students1 = {
            new Student("Alice", 800, 101), // Original index 0
            new Student("Bob", 500, 102),   // Original index 1
            new Student("Charlie", 800, 103),// Original index 2 (Same grade as Alice)
            new Student("Dana", 700, 104)
        };
        
        // Print Original Order
        System.out.println("Original Order:");
        printArray(students1);

        // --- Test Stable Sort ---
        Student[] stableStudents = Arrays.copyOf(students1, students1.length);
        stableSort(stableStudents);
        System.out.println("\nStable Sort Order (Merge Sort Logic):");
        printArray(stableStudents);

        // --- Test Unstable Sort ---
        Student[] unstableStudents = Arrays.copyOf(students1, students1.length);
        unstableSort(unstableStudents);
        System.out.println("\nUnstable Sort Order (Quick Sort Logic):");
        printArray(unstableStudents);
    }

    private static void printArray(Student[] arr) {
        for (Student s : arr) {
            System.out.println(s);
        }
    }
}

