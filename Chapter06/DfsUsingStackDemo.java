import java.util.*;

/**
 * DEPTH-FIRST SEARCH (DFS) USING STACK
 * 
 * DFS explores a graph by going as deep as possible along each branch
 * before backtracking to explore other branches.
 * 
 * KEY INSIGHT: Stack (LIFO) ensures we process the most recently discovered
 * node first, which gives us depth-first exploration.
 * 
 * Applications:
 * - Detecting cycles in graphs
 * - Topological sorting (task scheduling with dependencies)
 * - Finding connected components
 * - Solving mazes and puzzles
 * - Path finding (though not guaranteed shortest)
 * - Detecting deadlocks in operating systems
 */
public class DfsUsingStackDemo {
    
    /**
     * Simple Graph representation using adjacency list
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
     * DEPTH-FIRST SEARCH IMPLEMENTATION (ITERATIVE WITH STACK)
     * 
     * Algorithm:
     * 1. Start at the source node, mark it as visited
     * 2. Push source onto stack
     * 3. While stack is not empty:
     *    a. Pop a node from stack (remove from top)
     *    b. Process/visit this node
     *    c. For each unvisited neighbor:
     *       - Mark it as visited
     *       - Push it onto stack (add to top)
     * 
     * Time Complexity: O(V + E) where V = vertices, E = edges
     *   - Each vertex is pushed/popped once: O(V)
     *   - Each edge is examined once: O(E)
     * 
     * Space Complexity: O(V)
     *   - Stack can hold up to V nodes (worst case: linear graph)
     *   - Visited set stores V nodes
     * 
     * NOTE: Iterative DFS with stack may visit nodes in different order
     * than recursive DFS, depending on how neighbors are added to stack.
     * 
     * @param graph The graph to traverse
     * @param startNode The starting node for DFS
     */
    public static void dfs(Graph graph, int startNode) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("DEPTH-FIRST SEARCH (DFS) - Starting from node " + startNode);
        System.out.println("=".repeat(70));
        
        // STEP 1: Initialize data structures
        
        // Stack to store nodes to visit (LIFO order ensures depth-first)
        // Stack class in Java extends Vector
        Stack<Integer> stack = new Stack<>();
        
        // Set to track which nodes we've already visited
        // Prevents infinite loops and repeated processing
        Set<Integer> visited = new HashSet<>();
        
        // Track the order in which nodes are visited
        List<Integer> visitOrder = new ArrayList<>();
        
        // STEP 2: Start with the initial node
        stack.push(startNode);    // push() = add to top of stack
        visited.add(startNode);   // Mark as visited when pushed
        
        System.out.println("Initialization:");
        System.out.println("  Stack: " + stack);
        System.out.println("  Visited: " + visited);
        System.out.println();
        
        int step = 1;
        
        // STEP 3: Process nodes depth-first
        while (!stack.isEmpty()) {
            System.out.println("Step " + step++ + ":");
            System.out.println("  Stack before pop: " + stack);
            
            // POP: Remove node from top of stack (LIFO)
            // This is the most recently added unprocessed node
            int currentNode = stack.pop();  // pop() = remove from top
            
            System.out.println("  Popped node: " + currentNode);
            
            // PROCESS: Do whatever we need with this node
            visitOrder.add(currentNode);
            System.out.println("  Processing node: " + currentNode);
            
            // EXPLORE NEIGHBORS: Add all unvisited neighbors to stack
            List<Integer> neighbors = graph.getNeighbors(currentNode);
            System.out.println("  Neighbors of " + currentNode + ": " + neighbors);
            
            // Track which neighbors we're adding this iteration
            List<Integer> newlyAdded = new ArrayList<>();
            
            // IMPORTANT: Order matters for stack!
            // We often reverse neighbors so leftmost neighbor is processed first
            // (because stack is LIFO, last pushed = first popped)
            List<Integer> reversedNeighbors = new ArrayList<>(neighbors);
            Collections.reverse(reversedNeighbors);
            
            for (int neighbor : reversedNeighbors) {
                // Only process if not yet visited
                if (!visited.contains(neighbor)) {
                    // Mark as visited NOW (when we push it)
                    // This prevents the same node being pushed multiple times
                    visited.add(neighbor);
                    
                    // Push onto stack for future processing
                    stack.push(neighbor);
                    
                    newlyAdded.add(neighbor);
                    
                    System.out.println("    → Pushing neighbor " + neighbor + 
                        " to stack");
                }
            }
            
            if (newlyAdded.isEmpty()) {
                System.out.println("    (All neighbors already visited)");
            }
            
            System.out.println("  Stack after push: " + stack);
            System.out.println("  Visited so far: " + visited);
            System.out.println();
        }
        
        // STEP 4: Display results
        System.out.println("=".repeat(70));
        System.out.println("DFS COMPLETE");
        System.out.println("=".repeat(70));
        System.out.println("Visit order: " + visitOrder);
        System.out.println("Total nodes visited: " + visitOrder.size());
        System.out.println();
    }
    
    /**
     * RECURSIVE DFS (Alternative implementation)
     * 
     * This is the classic DFS implementation using the call stack
     * instead of an explicit stack data structure.
     * 
     * The recursion implicitly uses the call stack, which is LIFO,
     * giving us the same depth-first behavior.
     * 
     * Often more intuitive and concise than iterative version.
     */
    public static void dfsRecursive(Graph graph, int startNode) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("RECURSIVE DFS - Starting from node " + startNode);
        System.out.println("=".repeat(70));
        
        Set<Integer> visited = new HashSet<>();
        List<Integer> visitOrder = new ArrayList<>();
        
        dfsRecursiveHelper(graph, startNode, visited, visitOrder, 0);
        
        System.out.println("\n=".repeat(70));
        System.out.println("RECURSIVE DFS COMPLETE");
        System.out.println("=".repeat(70));
        System.out.println("Visit order: " + visitOrder);
        System.out.println("Total nodes visited: " + visitOrder.size());
        System.out.println();
    }
    
    /**
     * Helper method for recursive DFS
     * 
     * @param depth Recursion depth for visualization
     */
    private static void dfsRecursiveHelper(Graph graph, int node, 
                                          Set<Integer> visited,
                                          List<Integer> visitOrder,
                                          int depth) {
        // Indentation to show recursion depth
        String indent = "  ".repeat(depth);
        
        System.out.println(indent + "Visiting node: " + node + 
            " (depth " + depth + ")");
        
        // Mark as visited and record
        visited.add(node);
        visitOrder.add(node);
        
        // Recursively visit all unvisited neighbors
        List<Integer> neighbors = graph.getNeighbors(node);
        System.out.println(indent + "Neighbors: " + neighbors);
        
        for (int neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                System.out.println(indent + "→ Going deeper to neighbor " + 
                    neighbor);
                
                // RECURSIVE CALL: Go deeper into this branch
                dfsRecursiveHelper(graph, neighbor, visited, visitOrder, 
                    depth + 1);
                
                // When we return here, we've fully explored that branch
                System.out.println(indent + "← Backtracked from " + neighbor + 
                    " to " + node);
            } else {
                System.out.println(indent + "  (neighbor " + neighbor + 
                    " already visited)");
            }
        }
    }
    
    /**
     * DFS to detect cycles in directed graph
     * 
     * Uses three colors:
     * - White (not visited)
     * - Gray (currently being processed - in recursion stack)
     * - Black (fully processed - all descendants visited)
     * 
     * If we encounter a GRAY node, we found a back edge → cycle!
     */
    public static boolean hasCycle(Graph graph) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CYCLE DETECTION USING DFS");
        System.out.println("=".repeat(70));
        
        Map<Integer, NodeColor> colors = new HashMap<>();
        
        // Initialize all nodes as WHITE (unvisited)
        for (int node : graph.getAllNodes()) {
            colors.put(node, NodeColor.WHITE);
        }
        
        // Check each component
        for (int node : graph.getAllNodes()) {
            if (colors.get(node) == NodeColor.WHITE) {
                if (hasCycleHelper(graph, node, colors)) {
                    System.out.println("✓ Cycle detected!");
                    return true;
                }
            }
        }
        
        System.out.println("✗ No cycle found");
        return false;
    }
    
    private static boolean hasCycleHelper(Graph graph, int node, 
                                         Map<Integer, NodeColor> colors) {
        // Mark current node as GRAY (being processed)
        colors.put(node, NodeColor.GRAY);
        System.out.println("Processing node " + node + " (marked GRAY)");
        
        for (int neighbor : graph.getNeighbors(node)) {
            NodeColor neighborColor = colors.get(neighbor);
            
            if (neighborColor == NodeColor.GRAY) {
                // Found a back edge! (edge to node currently being processed)
                System.out.println("  Found back edge: " + node + " → " + 
                    neighbor + " (CYCLE!)");
                return true;
            }
            
            if (neighborColor == NodeColor.WHITE) {
                if (hasCycleHelper(graph, neighbor, colors)) {
                    return true;
                }
            }
        }
        
        // Mark as BLACK (fully processed)
        colors.put(node, NodeColor.BLACK);
        System.out.println("Finished processing " + node + " (marked BLACK)");
        
        return false;
    }
    
    enum NodeColor {
        WHITE,  // Not visited
        GRAY,   // Currently being processed
        BLACK   // Fully processed
    }
    
    /**
     * DFS to find all paths between two nodes
     * 
     * Uses backtracking: explores path, then "undoes" to try other paths
     */
    public static void findAllPaths(Graph graph, int start, int target) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("FIND ALL PATHS: " + start + " → " + target);
        System.out.println("=".repeat(70));
        
        List<List<Integer>> allPaths = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        
        currentPath.add(start);
        visited.add(start);
        
        findAllPathsHelper(graph, start, target, visited, currentPath, allPaths);
        
        System.out.println("\nAll paths found: " + allPaths.size());
        for (int i = 0; i < allPaths.size(); i++) {
            System.out.println("  Path " + (i + 1) + ": " + allPaths.get(i));
        }
    }
    
    private static void findAllPathsHelper(Graph graph, int current, int target,
                                          Set<Integer> visited,
                                          List<Integer> currentPath,
                                          List<List<Integer>> allPaths) {
        // Found a path to target!
        if (current == target) {
            allPaths.add(new ArrayList<>(currentPath));
            System.out.println("Found path: " + currentPath);
            return;
        }
        
        // Explore neighbors
        for (int neighbor : graph.getNeighbors(current)) {
            if (!visited.contains(neighbor)) {
                // Add to path and mark visited
                visited.add(neighbor);
                currentPath.add(neighbor);
                
                // Recurse
                findAllPathsHelper(graph, neighbor, target, visited, 
                    currentPath, allPaths);
                
                // BACKTRACK: Remove from path and mark unvisited
                // This allows us to explore other paths through this node
                currentPath.remove(currentPath.size() - 1);
                visited.remove(neighbor);
            }
        }
    }
    
    /**
     * DEMONSTRATION
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║           DEPTH-FIRST SEARCH (DFS) DEMONSTRATION                  ║");
        System.out.println("║                    Using Stack (LIFO)                              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        
        // Create example graph (same as BFS demo for comparison)
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
        
        // Demo 1: Iterative DFS with stack
        dfs(graph, 0);
        
        // Demo 2: Recursive DFS
        dfsRecursive(graph, 0);
        
        // Demo 3: DFS from different starting point
        dfs(graph, 2);
        
        // Demo 4: Cycle detection
        System.out.println("\nDemo: Cycle Detection");
        Graph cyclicGraph = new Graph();
        cyclicGraph.addEdge(0, 1);
        cyclicGraph.addEdge(1, 2);
        cyclicGraph.addEdge(2, 0);  // Creates cycle: 0→1→2→0
        printGraph(cyclicGraph);
        hasCycle(cyclicGraph);
        
        Graph acyclicGraph = new Graph();
        acyclicGraph.addEdge(0, 1);
        acyclicGraph.addEdge(1, 2);
        acyclicGraph.addEdge(2, 3);  // No cycle (DAG)
        System.out.println();
        printGraph(acyclicGraph);
        hasCycle(acyclicGraph);
        
        // Demo 5: Find all paths
        findAllPaths(graph, 0, 5);
        
        // Key concepts summary
        printKeyConcepts();
        
        // BFS vs DFS comparison
        printComparison();
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
     * Print key DFS concepts
     */
    private static void printKeyConcepts() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("KEY DFS CONCEPTS");
        System.out.println("=".repeat(70));
        
        System.out.println("""
            
            WHY STACK?
            • Stack is LIFO (Last In, First Out)
            • Most recently discovered node is processed first
            • This naturally gives depth-first exploration
            • Explore as deep as possible before backtracking
            
            TWO IMPLEMENTATIONS:
            1. Iterative (explicit stack): Uses Stack data structure
            2. Recursive (implicit stack): Uses call stack automatically
            • Both have same time/space complexity
            • Recursive is often cleaner and more intuitive
            • Iterative gives more control over traversal order
            
            MARKING VISITED:
            • Mark nodes as visited WHEN PUSHING (not when popping)
            • Prevents same node being pushed multiple times
            • Critical for both correctness and efficiency
            
            DFS CHARACTERISTICS:
            • Does NOT guarantee shortest path
            • Goes deep before going wide
            • Uses less memory than BFS in wide graphs
            • Natural choice for backtracking problems
            
            STACK OPERATIONS:
            • push(element) - add to top of stack
            • pop() - remove and return top element
            • peek() - look at top without removing
            • isEmpty() - check if stack is empty
            
            TIME COMPLEXITY: O(V + E)
            • V = number of vertices (nodes)
            • E = number of edges
            • Each vertex pushed/popped once: O(V)
            • Each edge examined once: O(E)
            
            SPACE COMPLEXITY:
            • Iterative: O(V) for stack + visited set
            • Recursive: O(V) for call stack + visited set
            • Worst case: O(V) when graph is a long chain
            
            APPLICATIONS:
            ✓ Cycle detection
            ✓ Topological sorting
            ✓ Finding connected components
            ✓ Solving mazes
            ✓ Path finding (any path, not shortest)
            ✓ Detecting deadlocks
            ✓ Backtracking problems (N-Queens, Sudoku)
            """);
    }
    
    /**
     * Print BFS vs DFS comparison
     */
    private static void printComparison() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("BFS vs DFS COMPARISON");
        System.out.println("=".repeat(70));
        
        System.out.println("""
            
            ┌──────────────────────┬─────────────────────┬─────────────────────┐
            │ Aspect               │ BFS (Queue)         │ DFS (Stack)         │
            ├──────────────────────┼─────────────────────┼─────────────────────┤
            │ Data Structure       │ Queue (FIFO)        │ Stack (LIFO)        │
            │ Exploration Order    │ Level by level      │ As deep as possible │
            │ Shortest Path        │ YES (unweighted)    │ NO                  │
            │ Memory Usage         │ O(w) - width        │ O(h) - height       │
            │ Implementation       │ Usually iterative   │ Recursive or iter.  │
            │ When wider than deep │ Uses MORE memory    │ Uses LESS memory    │
            │ When deeper than wide│ Uses LESS memory    │ Uses MORE memory    │
            │ Time Complexity      │ O(V + E)            │ O(V + E)            │
            └──────────────────────┴─────────────────────┴─────────────────────┘
            
            USE BFS WHEN:
            • You need shortest path
            • Target is likely close to start
            • Graph is deeper than it is wide
            • You need to explore by distance/level
            
            USE DFS WHEN:
            • You need any path (not necessarily shortest)
            • Detecting cycles
            • Topological sorting
            • Graph is wider than it is deep
            • Backtracking problems
            
            EXAMPLE:
            Starting from node 0 in our demo graph:
            
            BFS order: [0, 1, 3, 2, 4, 5]
              → Level 0: 0
              → Level 1: 1, 3
              → Level 2: 2, 4
              → Level 3: 5
            
            DFS order: [0, 1, 2, 4, 3, 5] (may vary based on neighbor order)
              → Goes 0 → 1 → 2 → 4 (as deep as possible)
              → Backtracks to explore other branches
            """);
    }
}

