import java.util.*;

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
    private Map<Integer, Node> graph = new HashMap<>();

    public void addEdge(int u, int v) {
        graph.putIfAbsent(u, new Node(u));
        graph.putIfAbsent(v, new Node(v));
        graph.get(u).addNeighbor(graph.get(v));
        graph.get(v).addNeighbor(graph.get(u));
    }

    /**
     * The recursion passes a QUEUE representing the entire current level.
     * Time Complexity: O(V + E) - Every node and edge is processed exactly once.
     * Space Complexity: O(V) - For the visited set, queue, and recursion stack frames.
     */
    public void recursiveBFS(Queue<Node> queue, Set<Integer> visited) {
        // 1. BASE CASE: If the queue is empty, the current level has no nodes to process.
        if (queue.isEmpty()) {
            return;
        }

        // Process ALL nodes present in the current level
        int levelSize = queue.size();
        System.out.println("\n[LEVEL EXPLORATION]: Processing a level with " + levelSize + " node(s).");
        
        Queue<Node> nextLevelQueue = new LinkedList<>();

        // Process the current level completely (Simulating the classic iterative BFS layer)
        for (int i = 0; i < levelSize; i++) {
            Node currentNode = queue.poll();
            System.out.println(" -> Visited: " + currentNode);

            // Collect all unvisited neighbors to form the NEXT level
            for (Node neighbor : currentNode.neighbors) {
                if (!visited.contains(neighbor.id)) {
                    visited.add(neighbor.id); // Mark visited immediately upon discovery to prevent duplicates
                    nextLevelQueue.add(neighbor);
                }
            }
        }

        // 2. RECURSIVE STEP: Move to the next horizontal level of the graph
        recursiveBFS(nextLevelQueue, visited);
    }

    public void runRecursiveBFS() {
        System.out.println("--- Starting True Recursive BFS Traversal ---");
        Node startNode = graph.get(1);
        if (startNode == null) {
            System.out.println("Graph is empty or start node not found.");
            return;
        }

        // Setup for layer tracking
        Set<Integer> visited = new HashSet<>();
        Queue<Node> initialQueue = new LinkedList<>();

        // Initialize tracking with the root node
        visited.add(startNode.id);
        initialQueue.add(startNode);

        // Kick off level-by-level recursion
        recursiveBFS(initialQueue, visited);
        System.out.println("\n--- Recursive Traversal Complete ---");
    }

    public static void main(String[] args) {
        RecursiveBFS app = new RecursiveBFS();
        
        // Graph structure: 1 -- 2 -- 3 -- 5 -- 4 -- 1 (With cross edge 3-4)
        app.addEdge(1, 2);
        app.addEdge(2, 3);
        app.addEdge(3, 5);
        app.addEdge(5, 4);
        app.addEdge(4, 1);
        app.addEdge(3, 4);

        app.runRecursiveBFS();
    }
}
