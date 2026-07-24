public class RadixSort {

    public static void radixSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }

        int max = arr[0];
        for (int value : arr) {
            if (value > max) {
                max = value;
            }
        }

        // Sort by each digit, from least to most significant.
        // exp is the place value: 1 (ones), 10 (tens), 100 (hundreds), ...
        for (long exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(arr, exp);
        }
    }

    // Stable counting sort on the digit at the given place value (exp).
    private static void countingSortByDigit(int[] arr, long exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        // Tally how many elements fall into each digit bucket (0-9).
        for (int value : arr) {
            count[(int) ((value / exp) % 10)]++;
        }

        // Turn counts into prefix sums: count[d] now holds the index
        // (one past the last slot) where digit d's block ends.
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Place elements into output in reverse to keep the sort stable.
        for (int i = n - 1; i >= 0; i--) {
            int digit = (int) ((arr[i] / exp) % 10);
            output[--count[digit]] = arr[i];
        }

        System.arraycopy(output, 0, arr, 0, n);
    }

    public static void main(String[] args) {
        int[] arr = { 4321, 12, 9, 654, 88, 3000, 1 };
        radixSort(arr);

        System.out.println("Sorted array:");
        for (int value : arr) {
            System.out.print(value + " ");
        }
    }
}