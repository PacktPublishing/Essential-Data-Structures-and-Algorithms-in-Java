import java.util.*;

public class SimpleGraph {
    
    // Graph representation using adjacency list
    private List<List<Integer>> adjList;
    private int vertices;
    
    // Constructor
    public SimpleGraph(int n) {
        this.vertices = n;
        this.adjList = new ArrayList<>();
        // Initialize adjacency list for each vertex
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
    }
    
    // Add edge between vertices u and v
    public void addEdge(int u, int v) {
        // Since graph is undirected, add edge in both directions
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }
    
    // Check if graph is connected (all vertices reachable from each other)
    public boolean isConnected() {
        // If there are 0 or 1 vertices, it's connected
        if (vertices <= 1) return true;
        
        // Use BFS to check if all vertices are reachable from vertex 0
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        
        // Start BFS from vertex 0
        visited[0] = true;
        queue.offer(0);
        
        int visitedCount = 1; // Count of visited vertices
        
        // BFS traversal
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            // Visit all neighbors of current vertex
            for (int neighbor : adjList.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                    visitedCount++;
                }
            }
        }
        
        // If all vertices were visited, graph is connected
        return visitedCount == vertices;
    }
    
    // Count number of connected components in the graph
    public int getConnectedComponents() {
        // Handle edge cases
        if (vertices <= 1) return vertices;
        
        boolean[] visited = new boolean[vertices];
        int components = 0;
        
        // For each unvisited vertex, start a new BFS/DFS
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                // Found new connected component
                components++;
                
                // BFS to mark all vertices in this component as visited
                Queue<Integer> queue = new LinkedList<>();
                visited[i] = true;
                queue.offer(i);
                
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    
                    // Visit all neighbors
                    for (int neighbor : adjList.get(current)) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true;
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
        
        return components;
    }
    
    public static void main(String[] args) {
        // Test Case 1: Connected graph
        SimpleGraph graph1 = new SimpleGraph(4);
        graph1.addEdge(0, 1);
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        System.out.println("Test 1 - Connected graph: " + graph1.isConnected()); // Expected: true
        System.out.println("Test 1 - Connected components: " + graph1.getConnectedComponents()); // Expected: 1
        
        // Test Case 2: Disconnected graph
        SimpleGraph graph2 = new SimpleGraph(4);
        graph2.addEdge(0, 1);
        graph2.addEdge(2, 3);
        System.out.println("Test 2 - Disconnected graph: " + graph2.isConnected()); // Expected: false
        System.out.println("Test 2 - Connected components: " + graph2.getConnectedComponents()); // Expected: 2
    }
}

