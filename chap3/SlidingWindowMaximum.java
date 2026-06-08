import java.util.ArrayDeque;
import java.util.Deque;

public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1]; // [1] one max per window position
        // [2] deque stores indices; front = index of current window max
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // [3] Remove indices outside the current window
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            // [4] Remove indices from back whose values are ≤ nums[i]
            //     They are useless: i is newer and at least as large
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i); // [5] Add current index

            // [6] Window is full once i >= k-1; record the max (front of deque)
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        SlidingWindowMaximum swm = new SlidingWindowMaximum();
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int[] result = swm.maxSlidingWindow(nums, 3);
        // Expected: [3, 3, 5, 5, 6, 7]
        for (int r : result) System.out.print(r + " ");
    }
}

