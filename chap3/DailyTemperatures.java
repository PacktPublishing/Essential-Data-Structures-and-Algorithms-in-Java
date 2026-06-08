import java.util.Stack;

public class DailyTemperatures {
    public int[] dailyTemperatures(int[] temps) {
        int n = temps.length;
        int[] answer = new int[n]; // [1] default 0 for "no warmer day"
        // [2] monotonic stack stores indices of days waiting for a warmer day
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < n; i++) {
            // [3] Current day is warmer than the day at stack top
            //     → resolve all waiting days that are colder than today
            while (!stack.isEmpty() && temps[i] > temps[stack.peek()]) {
                int prevIdx = stack.pop();
                answer[prevIdx] = i - prevIdx; // [4] days waited = gap
            }
            // [5] Push current index — still looking for its warmer day
            stack.push(i);
        }
        // [6] Any indices left on stack have answer[i] = 0 (already default)
        return answer;
    }

    public static void main(String[] args) {
        DailyTemperatures dt = new DailyTemperatures();
        int[] temps = {73, 74, 75, 71, 69, 72, 76, 73};
        int[] result = dt.dailyTemperatures(temps);
        // Expected: [1, 1, 4, 2, 1, 1, 0, 0]
        for (int r : result) System.out.print(r + " ");
    }
}

