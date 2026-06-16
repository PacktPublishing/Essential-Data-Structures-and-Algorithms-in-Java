import java.util.*;

public class DAGShortestPath {
    
    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    public static int[] shortestPath(int vertices, List<List<Edge>> adj, int source) {
        int[] dist = new int[vertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        // Topological sort
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[vertices];
        
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                topologicalSortUtil(i, adj, visited, stack);
            }
        }
        
        // Process vertices in topological order
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge edge : adj.get(u)) {
                    int v = edge.to;
                    int weight = edge.weight;
                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                    }
                }
            }
        }
        
        return dist;
    }
    
    private static void topologicalSortUtil(int v, List<List<Edge>> adj, 
                                          boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;
        for (Edge edge : adj.get(v)) {
            if (!visited[edge.to]) {
                topologicalSortUtil(edge.to, adj, visited, stack);
            }
        }
        stack.push(v);
    }
    
    public static void main(String[] args) {
        int vertices = 6;
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
        
        // Add edges: u -> v with weight
        adj.get(0).add(new Edge(1, 5));
        adj.get(0).add(new Edge(2, 3));
        adj.get(1).add(new Edge(3, 6));
        adj.get(1).add(new Edge(2, 2));
        adj.get(2).add(new Edge(4, 4));
        adj.get(2).add(new Edge(5, 2));
        adj.get(3).add(new Edge(4, -1));
        adj.get(4).add(new Edge(5, -2));
        
        int[] result = shortestPath(vertices, adj, 0);
        System.out.println("Shortest distances from vertex 0:");
        for (int i = 0; i < vertices; i++) {
            System.out.println("To vertex " + i + ": " + result[i]);
        }
    }
}

