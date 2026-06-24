import java.util.*;

public class GraphDFS {

    // Recursive DFS
    public static List<Integer> recursiveDFS(List<List<Integer>> adj, int start, int n) {
        List<Integer> result = new ArrayList<>();
        dfs(adj, start, new boolean[n], result);
        return result;
    }

    private static void dfs(List<List<Integer>> adj, int curr, boolean[] visited, List<Integer> result) {
        visited[curr] = true;
        result.add(curr);
        for (int next : adj.get(curr)) {
            if (!visited[next]) dfs(adj, next, visited, result);
        }
    }

    // Iterative DFS — push neighbors in reverse so lower indices pop first (matches recursive order)
    public static List<Integer> iterativeDFS(List<List<Integer>> adj, int start, int n) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int curr = stack.pop();
            if (visited[curr]) continue;
            visited[curr] = true;
            result.add(curr);

            List<Integer> neighbors = adj.get(curr);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                if (!visited[neighbors.get(i)]) stack.push(neighbors.get(i));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int n = 4;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(0).add(1);   // edges: 0-1, 0-2, 1-3
        adj.get(0).add(2);
        adj.get(1).add(3);

        System.out.println("Recursive DFS: " + recursiveDFS(adj, 0, n));  // [0, 1, 3, 2]
        System.out.println("Iterative DFS: " + iterativeDFS(adj, 0, n));  // [0, 1, 3, 2]
    }
}
