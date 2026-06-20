import java.util.*;

class Edge<T> {
    final T to;        // the vertex this edge points to
    final int weight;  // edge weight

    Edge(T to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}

public class GraphSolution {

    // Adjacency list: each vertex maps to its list of outgoing edges.
    // For an undirected graph, edge (u,v) appears in BOTH u's and v's lists.
    private final Map<Integer, List<Edge<Integer>>> adjacency = new HashMap<>();

    /**
     * Add a vertex. computeIfAbsent makes this idempotent: calling it on an
     * existing vertex leaves its edge list untouched.
     */
    public void addVertex(int v) {
        adjacency.computeIfAbsent(v, k -> new ArrayList<>());
    }

    /**
     * Add an undirected weighted edge. We auto-create both endpoints so the
     * caller doesn't have to addVertex first, then store the edge on each side
     * because the graph is undirected.
     */
    public void addEdge(int u, int v, int weight) {
        addVertex(u);
        addVertex(v);
        adjacency.get(u).add(new Edge<>(v, weight));
        adjacency.get(v).add(new Edge<>(u, weight));
    }

    /**
     * Sum every edge weight, then halve. Each undirected edge is stored twice
     * (once per endpoint), so the raw sum is exactly double the true total.
     * Using long avoids overflow when many large weights accumulate.
     */
    public long totalWeight() {
        long sum = 0;
        for (List<Edge<Integer>> edges : adjacency.values()) {
            for (Edge<Integer> e : edges) {
                sum += e.weight;
            }
        }
        return sum / 2;  // each edge counted twice
    }

    public static void main(String[] args) {
        int passed = 0, total = 0;

        // Test case 1: triangle 1-2(5), 2-3(7), 1-3(2) -> 14
        GraphSolution g1 = new GraphSolution();
        g1.addEdge(1, 2, 5);
        g1.addEdge(2, 3, 7);
        g1.addEdge(1, 3, 2);
        total++; passed += check("triangle", g1.totalWeight(), 14L);

        // Test case 2: vertices only, no edges -> 0
        GraphSolution g2 = new GraphSolution();
        g2.addVertex(1);
        g2.addVertex(2);
        total++; passed += check("no edges", g2.totalWeight(), 0L);

        System.out.printf("%n%d/%d checks passed.%n", passed, total);
    }

    private static int check(String name, long actual, long expected) {
        boolean ok = actual == expected;
        System.out.printf("[%s] %-12s expected=%d actual=%d%n",
                ok ? "PASS" : "FAIL", name, expected, actual);
        return ok ? 1 : 0;
    }
}
