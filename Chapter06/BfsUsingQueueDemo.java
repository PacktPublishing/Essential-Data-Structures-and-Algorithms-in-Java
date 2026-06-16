import java.util.*;

/**
 * BREADTH-FIRST SEARCH (BFS) USING QUEUE
 * 
 * BFS explores a graph level by level, visiting all neighbors at the current
 * depth before moving to nodes at the next depth level.
 * 
 * KEY INSIGHT: Queue (FIFO) ensures we process nodes in the order discovered,
 * which naturally gives us level-order traversal.
 * 
 * Applications:
 * - Finding shortest path in unweighted graphs
 * - Level-order tree traversal
 * - Web crawling
 * - Social network friend suggestions (friends of friends)
 * - GPS navigation systems
 */
public class BfsUsingQueueDemo {
    
    /**
     * Simple Graph representation using adjacency list
     * Each node maps to a list of its neighbors
     */
    static class Graph {
        private Map<Integer, List<Integer>> adjacencyList;
        
        public Graph() {
            this.adjacencyList = new HashMap<>();
        }
        
        /**
         * Add a directed edge from source to destination
         */
        public void addEdge(int source, int destination) {
            adjacencyList.putIfAbsent(source, new ArrayList<>());
            adjacencyList.putIfAbsent(destination, new ArrayList<>());
            adjacencyList.get(source).add(destination);
        }
        
        /**
         * Add an undirected edge (bidirectional)
         */
        public void addUndirectedEdge(int node1, int node2) {
            addEdge(node1, node2);
            addEdge(node2, node1);
        }
        
        /**
         * Get neighbors of a node
         */
        public List<Integer> getNeighbors(int node) {
            return adjacencyList.getOrDefault(node, new ArrayList<>());
        }
        
        /**
         * Get all nodes in the graph
         */
        public Set<Integer> getAllNodes() {
            return adjacencyList.keySet();
        }
    }
    
    /**
     * BREADTH-FIRST SEARCH IMPLEMENTATION
     * 
     * Algorithm:
     * 1. Start at the source node, mark it as visited
     * 2. Add source to queue
     * 3. While queue is not empty:
     *    a. Dequeue a node (remove from front)
     *    b. Process/visit this node
     *    c. For each unvisited neighbor:
     *       - Mark it as visited
     *       - Enqueue it (add to back)
     * 
     * Time Complexity: O(V + E) where V = vertices, E = edges
     *   - Each vertex is enqueued/dequeued once: O(V)
     *   - Each edge is examined once: O(E)
     * 
     * Space Complexity: O(V)
     *   - Queue can hold up to V nodes
     *   - Visited set stores V nodes
     * 
     * @param graph The graph to traverse
     * @param startNode The starting node for BFS
     */
    public static void bfs(Graph graph, int startNode) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("BREADTH-FIRST SEARCH (BFS) - Starting from node " + startNode);
        System.out.println("=".repeat(70));
        
        // STEP 1: Initialize data structures
        
        // Queue to store nodes to visit (FIFO order ensures level-by-level)
        // LinkedList implements Queue interface in Java
        Queue<Integer> queue = new LinkedList<>();
        
        // Set to track which nodes we've already visited
        // Prevents infinite loops in graphs with cycles
        // Also prevents processing same node multiple times
        Set<Integer> visited = new HashSet<>();
        
        // Track the order in which nodes are visited
        List<Integer> visitOrder = new ArrayList<>();
        
        // STEP 2: Start with the initial node
        queue.offer(startNode);  // offer() = add to back of queue
        visited.add(startNode);   // Mark as visited immediately when enqueued
        
        System.out.println("Initialization:");
        System.out.println("  Queue: " + queue);
        System.out.println("  Visited: " + visited);
        System.out.println();
        
        int step = 1;
        
        // STEP 3: Process nodes level by level
        while (!queue.isEmpty()) {
            System.out.println("Step " + step++ + ":");
            System.out.println("  Queue before dequeue: " + queue);
            
            // DEQUEUE: Remove node from front of queue (FIFO)
            // This node is at the current level we're processing
            int currentNode = queue.poll();  // poll() = remove from front
            
            System.out.println("  Dequeued node: " + currentNode);
            
            // PROCESS: Do whatever we need with this node
            // For this demo, we just record the visit order
            visitOrder.add(currentNode);
            System.out.println("  Processing node: " + currentNode);
            
            // EXPLORE NEIGHBORS: Add all unvisited neighbors to queue
            List<Integer> neighbors = graph.getNeighbors(currentNode);
            System.out.println("  Neighbors of " + currentNode + ": " + neighbors);
            
            // Track which neighbors we're adding this iteration
            List<Integer> newlyAdded = new ArrayList<>();
            
            for (int neighbor : neighbors) {
                // Only process if not yet visited
                // This check prevents:
                // 1. Revisiting nodes (efficiency)
                // 2. Infinite loops (correctness)
                if (!visited.contains(neighbor)) {
                    // Mark as visited NOW (when we enqueue it)
                    // Not when we dequeue it!
                    // This prevents the same node being added multiple times
                    visited.add(neighbor);
                    
                    // Add to queue for future processing
                    queue.offer(neighbor);
                    
                    newlyAdded.add(neighbor);
                    
                    System.out.println("    → Adding neighbor " + neighbor + 
                        " to queue");
                }
            }
            
            if (newlyAdded.isEmpty()) {
                System.out.println("    (All neighbors already visited)");
            }
            
            System.out.println("  Queue after enqueue: " + queue);
            System.out.println("  Visited so far: " + visited);
            System.out.println();
        }
        
        // STEP 4: Display results
        System.out.println("=".repeat(70));
        System.out.println("BFS COMPLETE");
        System.out.println("=".repeat(70));
        System.out.println("Visit order: " + visitOrder);
        System.out.println("Total nodes visited: " + visitOrder.size());
        System.out.println();
    }
    
    /**
     * BFS to find shortest path between two nodes
     * 
     * In unweighted graphs, BFS guarantees the shortest path because
     * it explores nodes level by level (distance by distance).
     * 
     * @return List of nodes in the shortest path, or empty if no path exists
     */
    public static List<Integer> bfsShortestPath(Graph graph, int start, int target) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("BFS SHORTEST PATH: " + start + " → " + target);
        System.out.println("=".repeat(70));
        
        // Queue stores pairs: (currentNode, pathToCurrentNode)
        Queue<NodeWithPath> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        
        // Initialize with start node
        List<Integer> startPath = new ArrayList<>();
        startPath.add(start);
        queue.offer(new NodeWithPath(start, startPath));
        visited.add(start);
        
        while (!queue.isEmpty()) {
            NodeWithPath current = queue.poll();
            int currentNode = current.node;
            List<Integer> currentPath = current.path;
            
            System.out.println("Exploring: " + currentNode + 
                ", Path so far: " + currentPath);
            
            // Found the target!
            if (currentNode == target) {
                System.out.println("✓ Target found!");
                System.out.println("Shortest path: " + currentPath);
                System.out.println("Path length: " + (currentPath.size() - 1) + 
                    " edges");
                return currentPath;
            }
            
            // Explore neighbors
            for (int neighbor : graph.getNeighbors(currentNode)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    
                    // Create new path including this neighbor
                    List<Integer> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);
                    
                    queue.offer(new NodeWithPath(neighbor, newPath));
                }
            }
        }
        
        // No path found
        System.out.println("✗ No path exists from " + start + " to " + target);
        return new ArrayList<>();
    }
    
    /**
     * Helper class to store node with its path
     */
    static class NodeWithPath {
        int node;
        List<Integer> path;
        
        NodeWithPath(int node, List<Integer> path) {
            this.node = node;
            this.path = path;
        }
    }
    
    /**
     * BFS Level-by-Level (with explicit level tracking)
     * 
     * Useful when you need to know which level each node is at
     * (e.g., social network: direct friends, friends of friends, etc.)
     */
    public static void bfsWithLevels(Graph graph, int startNode) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("BFS WITH LEVEL TRACKING - Starting from node " + startNode);
        System.out.println("=".repeat(70));
        
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> levels = new HashMap<>();  // node → level
        
        queue.offer(startNode);
        levels.put(startNode, 0);  // Start node is at level 0
        
        int currentLevel = 0;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();  // Number of nodes at current level
            
            System.out.println("\nLevel " + currentLevel + ":");
            System.out.print("  Nodes: ");
            
            // Process all nodes at current level
            for (int i = 0; i < levelSize; i++) {
                int node = queue.poll();
                System.out.print(node + " ");
                
                // Add all neighbors (they'll be at next level)
                for (int neighbor : graph.getNeighbors(node)) {
                    if (!levels.containsKey(neighbor)) {
                        levels.put(neighbor, currentLevel + 1);
                        queue.offer(neighbor);
                    }
                }
            }
            System.out.println();
            
            currentLevel++;
        }
        
        System.out.println("\nLevel summary:");
        for (int level = 0; level < currentLevel; level++) {
            int finalLevel = level;
            List<Integer> nodesAtLevel = new ArrayList<>();
            levels.forEach((node, lvl) -> {
                if (lvl == finalLevel) nodesAtLevel.add(node);
            });
            Collections.sort(nodesAtLevel);
            System.out.println("  Level " + level + ": " + nodesAtLevel);
        }
    }
    
    /**
     * DEMONSTRATION
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║           BREADTH-FIRST SEARCH (BFS) DEMONSTRATION                ║");
        System.out.println("║                    Using Queue (FIFO)                              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        
        // Create example graph
        /*
         *     Graph structure:
         * 
         *          1 ─── 2
         *         /│     │\
         *        / │     │ \
         *       0  │     │  5
         *        \ │     │ /
         *         \│     │/
         *          3 ─── 4
         * 
         *     Adjacency List:
         *     0: [1, 3]
         *     1: [0, 2, 3]
         *     2: [1, 4, 5]
         *     3: [0, 1, 4]
         *     4: [2, 3, 5]
         *     5: [2, 4]
         */
        
        Graph graph = new Graph();
        graph.addUndirectedEdge(0, 1);
        graph.addUndirectedEdge(0, 3);
        graph.addUndirectedEdge(1, 2);
        graph.addUndirectedEdge(1, 3);
        graph.addUndirectedEdge(2, 4);
        graph.addUndirectedEdge(2, 5);
        graph.addUndirectedEdge(3, 4);
        graph.addUndirectedEdge(4, 5);
        
        printGraph(graph);
        
        // Demo 1: Basic BFS
        bfs(graph, 0);
        
        // Demo 2: BFS from different starting point
        bfs(graph, 2);
        
        // Demo 3: Shortest path
        bfsShortestPath(graph, 0, 5);
        bfsShortestPath(graph, 1, 4);
        
        // Demo 4: Level-by-level BFS
        bfsWithLevels(graph, 0);
        
        // Demo 5: Disconnected graph
        System.out.println("\n" + "=".repeat(70));
        System.out.println("EXAMPLE: DISCONNECTED GRAPH");
        System.out.println("=".repeat(70));
        
        Graph disconnected = new Graph();
        // Component 1
        disconnected.addUndirectedEdge(0, 1);
        disconnected.addUndirectedEdge(1, 2);
        // Component 2 (separate)
        disconnected.addUndirectedEdge(3, 4);
        disconnected.addUndirectedEdge(4, 5);
        
        printGraph(disconnected);
        bfs(disconnected, 0);  // Will only visit component 1
        
        // Key concepts summary
        printKeyConcepts();
    }
    
    /**
     * Helper: Print graph structure
     */
    private static void printGraph(Graph graph) {
        System.out.println("\nGraph structure (Adjacency List):");
        List<Integer> nodes = new ArrayList<>(graph.getAllNodes());
        Collections.sort(nodes);
        
        for (int node : nodes) {
            List<Integer> neighbors = graph.getNeighbors(node);
            Collections.sort(neighbors);
            System.out.println("  " + node + " → " + neighbors);
        }
    }
    
    /**
     * Print key BFS concepts
     */
    private static void printKeyConcepts() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("KEY BFS CONCEPTS");
        System.out.println("=".repeat(70));
        
        System.out.println("""
            
            WHY QUEUE?
            • Queue is FIFO (First In, First Out)
            • Nodes discovered first are processed first
            • This naturally gives level-by-level exploration
            • All nodes at distance D are processed before nodes at distance D+1
            
            MARKING VISITED:
            • Mark nodes as visited WHEN ENQUEUING (not when dequeuing)
            • Prevents same node being added to queue multiple times
            • Critical for correctness and efficiency
            
            BFS GUARANTEES:
            • In unweighted graphs, finds shortest path
            • Visits all reachable nodes exactly once
            • Level-order traversal (by distance from start)
            
            QUEUE OPERATIONS:
            • offer(element) or add(element) - add to back of queue
            • poll() or remove() - remove from front of queue
            • peek() - look at front without removing
            • isEmpty() - check if queue is empty
            
            TIME COMPLEXITY: O(V + E)
            • V = number of vertices (nodes)
            • E = number of edges
            • Each vertex enqueued/dequeued once: O(V)
            • Each edge examined once: O(E)
            
            SPACE COMPLEXITY: O(V)
            • Queue can hold up to V nodes (worst case: all nodes at one level)
            • Visited set stores up to V nodes
            
            APPLICATIONS:
            ✓ Shortest path in unweighted graphs
            ✓ Level-order tree traversal
            ✓ Web crawling
            ✓ Social network analysis (degrees of separation)
            ✓ GPS navigation
            ✓ Peer-to-peer networks
            ✓ Garbage collection (finding reachable objects)
            """);
    }
}

