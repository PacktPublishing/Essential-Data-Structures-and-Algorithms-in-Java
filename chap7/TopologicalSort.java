import java.util.*;

public class TopologicalSort {
    // DFS approach
    public static List<Integer> topologicalSortDFS(Map<Integer, List<Integer>> graph) {
        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new HashSet<>();
        
        for (int vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsTopo(vertex, graph, visited, stack);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }
    
    private static void dfsTopo(int vertex, Map<Integer, List<Integer>> graph, 
                               Set<Integer> visited, Stack<Integer> stack) {
        visited.add(vertex);
        
        for (int neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsTopo(neighbor, graph, visited, stack);
            }
        }
        
        stack.push(vertex);
    }
    
    // Kahn's algorithm approach
    public static List<Integer> topologicalSortKahn(Map<Integer, List<Integer>> graph) {
        Map<Integer, Integer> inDegree = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> result = new ArrayList<>();
        
        // Initialize in-degrees
        for (int vertex : graph.keySet()) {
            inDegree.putIfAbsent(vertex, 0);
            for (int neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }
        
        // Add vertices with 0 in-degree to queue
        for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            result.add(vertex);
            
            for (int neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return result;
    }
}

