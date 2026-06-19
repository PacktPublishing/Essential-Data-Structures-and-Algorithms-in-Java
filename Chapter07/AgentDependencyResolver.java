import java.util.*;

/**
* Topological sort for AI agent execution ordering.
* Uses Kahn's algorithm (BFS) to resolve dependencies and detect cycles.
*/
public class AgentDependencyResolver {

   /**
    * Given a map of agent → dependencies, returns a valid execution order.
    * Throws if circular dependencies exist.
    *
    * Approach: Kahn's algorithm
    * 1. Compute in-degree for each agent (number of unresolved dependencies)
    * 2. Start with agents that have zero dependencies (in-degree 0)
    * 3. Process each agent: decrement in-degree of its dependents
    * 4. If not all agents are processed, a cycle exists
    *
    * Time: O(V + E) where V = agents, E = dependency edges
    * Space: O(V + E) for adjacency list and in-degree map
    */
   public List<String> resolveExecutionOrder(Map<String, List<String>> dependencies) {
       // adjacency list: agent → list of agents that depend on it
       Map<String, List<String>> dependents = new HashMap<>();
       // in-degree: how many unresolved dependencies each agent has
       Map<String, Integer> inDegree = new HashMap<>();

       // Initialize all agents
       for (String agent : dependencies.keySet()) {
           dependents.putIfAbsent(agent, new ArrayList<>());
           inDegree.putIfAbsent(agent, 0);
       }

       // Build the graph
       for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
           String agent = entry.getKey();
           for (String dep : entry.getValue()) {
               dependents.get(dep).add(agent);       // dep → agent (agent depends on dep)
               inDegree.merge(agent, 1, Integer::sum); // agent has one more dependency
           }
       }

       // Seed queue with agents that have no dependencies
       Queue<String> ready = new LinkedList<>();
       for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
           if (entry.getValue() == 0) {
               ready.offer(entry.getKey());
           }
       }

       // Process agents in topological order
       List<String> executionOrder = new ArrayList<>();
       while (!ready.isEmpty()) {
           String agent = ready.poll();
           executionOrder.add(agent);

           // This agent is resolved — decrement dependents' in-degrees
           for (String dependent : dependents.get(agent)) {
               int newDegree = inDegree.merge(dependent, -1, Integer::sum);
               if (newDegree == 0) {
                   ready.offer(dependent); // all dependencies met
               }
           }
       }

       // Cycle detection: if we couldn't process all agents, there's a cycle
       if (executionOrder.size() != dependencies.size()) {
           Set<String> inCycle = new HashSet<>(dependencies.keySet());
           inCycle.removeAll(executionOrder);
           throw new IllegalStateException(
               "Circular dependency detected among agents: " + inCycle);
       }

       return executionOrder;
   }

   public static void main(String[] args) {
       AgentDependencyResolver resolver = new AgentDependencyResolver();

       // Valid DAG
       Map<String, List<String>> deps = new LinkedHashMap<>();
       deps.put("Planner", List.of());
       deps.put("Researcher", List.of("Planner"));
       deps.put("Coder", List.of("Planner"));
       deps.put("Reviewer", List.of("Researcher", "Coder"));
       deps.put("Publisher", List.of("Reviewer"));

       System.out.println("Execution order: " + resolver.resolveExecutionOrder(deps));
       // [Planner, Researcher, Coder, Reviewer, Publisher]

       // Circular dependency
       Map<String, List<String>> circular = new LinkedHashMap<>();
       circular.put("A", List.of("C"));
       circular.put("B", List.of("A"));
       circular.put("C", List.of("B"));

       try {
           resolver.resolveExecutionOrder(circular);
       } catch (IllegalStateException e) {
           System.out.println("Detected: " + e.getMessage());
           // Detected: Circular dependency detected among agents: [A, B, C]
       }
   }
}
