import java.util.*;

public class StronglyConnectedComponents {
    public static List<Set<Integer>> findSCC(Map<Integer, List<Integer>> graph) {
        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new HashSet<>();
        
        // Step 1: Get finishing times using DFS
        for (int vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFinish(vertex, graph, visited, stack);
            }
        }
        
        // Step 2: Get transpose graph
        Map<Integer, List<Integer>> transpose = getTranspose(graph);
        
        // Step 3: Process vertices in reverse finishing time order
        visited.clear();
        List<Set<Integer>> sccs = new ArrayList<>();
        
        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            if (!visited.contains(vertex)) {
                Set<Integer> component = new HashSet<>();
                dfsComponent(vertex, transpose, visited, component);
                sccs.add(component);
            }
        }
        
        return sccs;
    }
    
    private static void dfsFinish(int vertex, Map<Integer, List<Integer>> graph, 
                                 Set<Integer> visited, Stack<Integer> stack) {
        visited.add(vertex);
        
        for (int neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsFinish(neighbor, graph, visited, stack);
            }
        }
        
        stack.push(vertex);
    }
    
    private static Map<Integer, List<Integer>> getTranspose(Map<Integer, List<Integer>> graph) {
        Map<Integer, List<Integer>> transpose = new HashMap<>();
        
        for (int vertex : graph.keySet()) {
            transpose.putIfAbsent(vertex, new ArrayList<>());
            for (int neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                transpose.putIfAbsent(neighbor, new ArrayList<>());
                transpose.get(neighbor).add(vertex);
            }
        }
        
        return transpose;
    }
    
    private static void dfsComponent(int vertex, Map<Integer, List<Integer>> graph, 
                                    Set<Integer> visited, Set<Integer> component) {
        visited.add(vertex);
        component.add(vertex);
        
        for (int neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsComponent(neighbor, graph, visited, component);
            }
        }
    }
}

