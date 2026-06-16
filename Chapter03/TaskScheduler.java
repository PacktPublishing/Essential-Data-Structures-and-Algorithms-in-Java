import java.util.*;

public class TaskScheduler {
    public int leastInterval(char[] tasks, int n) {
        // [1] Count task frequencies
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;

        // [2] Max-heap: tasks with highest remaining count processed first
        PriorityQueue<Integer> maxHeap =
            new PriorityQueue<>(Collections.reverseOrder());
        for (int f : freq) if (f > 0) maxHeap.offer(f);

        // [3] Cooldown queue: [remaining count, time when task becomes available]
        Queue<int[]> cooldown = new LinkedList<>();
        int time = 0;

        while (!maxHeap.isEmpty() || !cooldown.isEmpty()) {
            time++;

            if (!maxHeap.isEmpty()) {
                // [4] Execute the most frequent available task
                int count = maxHeap.poll() - 1;
                if (count > 0) {
                    // [5] Task still has work — put in cooldown for n intervals
                    cooldown.offer(new int[]{count, time + n});
                }
            }
            // [6] CPU idles if maxHeap is empty (all tasks in cooldown)

            // [7] Release tasks whose cooldown has expired back to the heap
            if (!cooldown.isEmpty() && cooldown.peek()[1] == time) {
                maxHeap.offer(cooldown.poll()[0]);
            }
        }
        return time;
    }

    public static void main(String[] args) {
        TaskScheduler ts = new TaskScheduler();
        // tasks=["A","A","A","B","B","B"], n=2
        // One optimal: A B idle A B idle A B → 8
        System.out.println(ts.leastInterval(
            new char[]{'A','A','A','B','B','B'}, 2)); // 8
    }
}

