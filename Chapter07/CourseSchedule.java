import java.util.*;

/**
 * Course Schedule — can all courses be finished?
 *
 * All courses are finishable iff
 * the graph is a DAG; a leftover course means a cycle blocked it.
 */
public class CourseSchedule {

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // adj.get(b) = list of courses that depend on b (i.e. b is their prereq).
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

        // indegree[c] = number of prerequisites course c is still waiting on.
        int[] indegree = new int[numCourses];

        // Build the graph: edge prereq -> course, and bump the course's indegree.
        for (int[] p : prerequisites) {
            int course = p[0], prereq = p[1];
            adj.get(prereq).add(course);   // prereq -> course
            indegree[course]++;
        }

        // Seed the queue with every course that has no prerequisites (indegree 0):
        // these are the courses we can take immediately.
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) queue.add(i);
        }

        // Process courses in topological order, "taking" each one.
        int taken = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            taken++;
            // Taking this course removes a prerequisite from each dependent course.
            for (int next : adj.get(course)) {
                if (--indegree[next] == 0) queue.add(next);  // all prereqs met -> unlocked
            }
        }

        // We could take 'taken' courses. If that's fewer than numCourses, the
        // remaining ones are stuck in a cycle (their indegree never hit 0).
        return taken == numCourses;
    }

    // ------------------------------------------------------------------
    // Test harness — run `java CourseSchedule`.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        CourseSchedule s = new CourseSchedule();
        int passed = 0, total = 0;

        total++; passed += check("simple ok",
                s.canFinish(2, new int[][]{{1, 0}}), true);

        total++; passed += check("2-cycle",
                s.canFinish(2, new int[][]{{1, 0}, {0, 1}}), false);

        total++; passed += check("linear chain",
                s.canFinish(4, new int[][]{{1, 0}, {2, 1}, {3, 2}}), true);

        total++; passed += check("3-cycle",
                s.canFinish(3, new int[][]{{0, 1}, {1, 2}, {2, 0}}), false);

        total++; passed += check("no prereqs",
                s.canFinish(3, new int[][]{}), true);

        System.out.printf("%n%d/%d checks passed.%n", passed, total);
    }

    private static int check(String name, boolean actual, boolean expected) {
        boolean ok = actual == expected;
        System.out.printf("[%s] %-14s expected=%b actual=%b%n",
                ok ? "PASS" : "FAIL", name, expected, actual);
        return ok ? 1 : 0;
    }
}
