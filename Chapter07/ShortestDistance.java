import java.util.*;

public class ShortestDistance {

    public static int shortestDistance(Map<String, List<String>> adj,
                                       String start, String target) {
        if (!adj.containsKey(start)) return -1;
        if (start.equals(target)) return 0;

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();
        visited.add(start);
        queue.add(start);
        int dist = 0;

        while (!queue.isEmpty()) {
            dist++; // advancing to the next level
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                String node = queue.poll();
                for (String next : adj.getOrDefault(node, List.of())) {
                    if (visited.contains(next)) continue;
                    if (next.equals(target)) return dist;
                    visited.add(next);
                    queue.add(next);
                }
            }
        }
        return -1;  // target unreachable
    }

    public static void main(String[] args) {
        Map<String, List<String>> adj = new HashMap<>();
        adj.put("A", Arrays.asList("B", "D"));
        adj.put("B", Arrays.asList("A", "C"));
        adj.put("C", Arrays.asList("B", "E"));
        adj.put("D", Arrays.asList("A", "E"));
        adj.put("E", Arrays.asList("C", "D"));

        int passed = 0, total = 0;

        total++; passed += check("A->C", shortestDistance(adj, "A", "C"), 2);
        total++; passed += check("A->E", shortestDistance(adj, "A", "E"), 2);
        total++; passed += check("A->A (self)", shortestDistance(adj, "A", "A"), 0);
        total++; passed += check("A->Z (unreachable)", shortestDistance(adj, "A", "Z"), -1);
        total++; passed += check("missing start", shortestDistance(adj, "X", "A"), -1);

        // Test case 1: shortest path is 3 edges (1 -> 2 -> 4 -> 5)
        Map<String, List<String>> g1 = new HashMap<>();
        g1.put("1", Arrays.asList("2", "3"));
        g1.put("2", Arrays.asList("1", "4"));
        g1.put("3", Arrays.asList("1", "4"));
        g1.put("4", Arrays.asList("2", "3", "5"));
        g1.put("5", Arrays.asList("4"));
        total++; passed += check("1->5", shortestDistance(g1, "1", "5"), 3);

        // Test case 2: disconnected graph -> no path
        Map<String, List<String>> g2 = new HashMap<>();
        g2.put("1", Arrays.asList("2"));
        g2.put("2", Arrays.asList("1"));
        g2.put("3", Arrays.asList("4"));
        g2.put("4", Arrays.asList("3"));
        total++; passed += check("1->4 (disconnected)", shortestDistance(g2, "1", "4"), -1);

        System.out.printf("%n%d/%d checks passed.%n", passed, total);
    }

    private static int check(String name, int actual, int expected) {
        boolean ok = actual == expected;
        System.out.printf("[%s] %-22s expected=%d actual=%d%n",
                ok ? "PASS" : "FAIL", name, expected, actual);
        return ok ? 1 : 0;
    }
}
