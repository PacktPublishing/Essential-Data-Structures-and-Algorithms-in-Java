import java.util.*;

/**
 * Represents a Node in the Graph.
 */
class Node {
    int id;
    List<Node> neighbors;

    public Node(int id) {
        this.id = id;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor);
    }

    @Override
    public String toString() {
        return "Node " + id;
    }
}

public class RecursiveBFS {

    // --- Graph Setup (The Tangible Structure) ---
    private Map<Integer, Node> graph = new HashMap<>();

    public void addEdge(int u, int v) {
        graph.putIfAbsent(u, new Node(u));
        graph.putIfAbsent(v, new Node(v));
        
        // Assuming an undirected graph for symmetry
        graph.get(u).addNeighbor(graph.get(v));
        graph.get(v).addNeighbor(graph.get(u));
    }

    /**
     * The Recursive BFS Function.
     * This function simulates the level-by-level exploration of BFS using recursion.
     *
     * @param startNode The node where the traversal begins (the starting point).
     * @param visited A set tracking all nodes visited to avoid cycles.
     */
    public void recursiveBFS(Node startNode, Set<Integer> visited) {
        // ============================================================
        // 1. BASE CASE (Termination Condition)
        // ============================================================
        // If there are no neighbors to explore from the current node, this branch stops.
        if (startNode == null || startNode.neighbors.isEmpty()) {
            System.out.println("--- Traversal stopped at: " + startNode);
            return;
        }

        // --- Action (Processing the Current Level) ---
        System.out.println("\n[LEVEL EXPLORATION]: Processing " + startNode);
        
        // 2. Process the Current Node (Simulating Queue Dequeue)
        // In a true BFS, we would dequeue here. In recursion, we process the node
        // before recursing further.
        visited.add(startNode.id); 

        // 3. Recursive Step (Exploring the Next Level)
        System.out.println("  -> Exploring neighbors of " + startNode);
        
        // We iterate through the neighbors and recursively call the function for each one.
        for (Node neighbor : startNode.neighbors) {
            // The recursive call handles the exploration of the entire subtree 
            // rooted at this neighbor (simulating depth).
            recursiveBFS(neighbor, visited); 
        }
        
        System.out.println("--- Backtracking from: " + startNode + " ---");
    }

    public void runRecursiveBFS() {
        System.out.println("--- Starting Recursive BFS Traversal ---");
        
        // 1. Initialization
        // We need a starting node to begin the exploration.
        Node startNode = graph.get(1); // Start at Node 1
        if (startNode == null) {
            System.out.println("Graph is empty or start node not found.");
            return;
        }
        
        Set<Integer> visited = new HashSet<>();

        // 2. Execution: Initiate the recursive process
        // The recursion starts by exploring all paths stemming from the starting node.
        recursiveBFS(startNode, visited);
        
        System.out.println("\n--- Recursive Traversal Complete ---");
    }

    public static void main(String[] args) {
        RecursiveBFS app = new RecursiveBFS();

        // --- Build the Graph (The Tangible Structure) ---
        // Graph structure: 1 -- 2 -- 3 -- 5 -- 4 -- 1 (A cycle)
        app.addEdge(1, 2);
        app.addEdge(2, 3);
        app.addEdge(3, 5);
        app.addEdge(5, 4);
        app.addEdge(4, 1); // Creates a cycle
        app.addEdge(3, 4); 

        // Run the recursive algorithm
        app.runRecursiveBFS();
    }
}

