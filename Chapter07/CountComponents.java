import java.util.*;

public class CountComponents {

    public static int countComponents(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);   // undirected: both directions
        }

        boolean[] visited = new boolean[n];
        int components = 0;
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                components++;          // new unvisited vertex = new component
                dfs(v, adj, visited);  // flood-fill the whole component
            }
        }
        return components;
    }

    private static void dfs(int node, List<List<Integer>> adj, boolean[] visited) {
        visited[node] = true;
        for (int next : adj.get(node)) {
            if (!visited[next]) dfs(next, adj, visited);
        }
    }

    public static void main(String[] args) {
        int passed = 0, total = 0;

        total++; passed += check("three comps",
                countComponents(6, new int[][]{{0, 1}, {1, 2}, {3, 4}}), 3);

        total++; passed += check("no edges",
                countComponents(4, new int[][]{}), 4);

        total++; passed += check("fully connected",
                countComponents(3, new int[][]{{0, 1}, {1, 2}, {0, 2}}), 1);

        System.out.printf("%n%d/%d checks passed.%n", passed, total);
    }

    private static int check(String name, int actual, int expected) {
        boolean ok = actual == expected;
        System.out.printf("[%s] %-16s expected=%d actual=%d%n",
                ok ? "PASS" : "FAIL", name, expected, actual);
        return ok ? 1 : 0;
    }
}
