import java.util.*;

/**
 * Agent Execution Order — Iterative Layer Removal
 *
 * Problem: Given agents with dependencies, find a valid execution order.
 *          If circular dependencies exist, detect and reject.
 *
 * Approach: Repeatedly find agents with no unresolved dependencies,
 *           add them to the execution order, and remove them from
 *           everyone else's dependency lists. If no agent is ready
 *           but agents remain, a cycle exists.
 *
 * Time:  O(n²) — each round scans all remaining agents
 * Space: O(n + m) — copy of dependency map
 */
public class AgentExecutionOrder {

    /**
     * Resolves execution order by iterative layer removal.
     *
     * @param dependencies map of agent → list of agents it depends on
     * @return ordered list where each agent appears after all its dependencies
     * @throws IllegalStateException if circular dependency exists
     */
    public List<String> resolveExecutionOrder(Map<String, List<String>> dependencies) {

        // Step 1: Deep copy the input so we can mutate safely
        // Each agent gets a mutable copy of its dependency list
        Map<String, List<String>> remaining = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            remaining.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        // Step 2: Execution order we're building
        List<String> order = new ArrayList<>();

        // Step 3: Repeat until all agents are placed or cycle detected
        while (!remaining.isEmpty()) {

            // Step 4: Find all agents whose dependency list is now empty
            // These agents have no unresolved dependencies — they're ready
            List<String> ready = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : remaining.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    ready.add(entry.getKey());
                }
            }

            // Step 5: If no agent is ready, remaining agents are in a cycle
            // They all depend on something that also hasn't been resolved
            if (ready.isEmpty()) {
                throw new IllegalStateException(
                    "Circular dependency detected among: " + remaining.keySet()
                );
            }

            // Step 6: Add all ready agents to execution order
            // These form one "layer" — they could execute in parallel
            order.addAll(ready);

            // Step 7: Remove ready agents from the remaining pool
            for (String agent : ready) {
                remaining.remove(agent);
            }

            // Step 8: Remove ready agents from everyone else's dependency lists
            // This "unblocks" agents that were waiting on them
            for (List<String> deps : remaining.values()) {
                deps.removeAll(ready);
            }
        }

        // Step 9: All agents placed — return valid execution order
        return order;
    }

    // ===== Demonstration =====

    public static void main(String[] args) {
        AgentExecutionOrder solver = new AgentExecutionOrder();

        // --- Test Case 1: Valid DAG ---
        System.out.println("=== Test Case 1: Valid DAG ===");
        Map<String, List<String>> validDeps = new LinkedHashMap<>();
        validDeps.put("QueryParser", List.of());
        validDeps.put("Retriever", List.of("QueryParser"));
        validDeps.put("Ranker", List.of("QueryParser"));
        validDeps.put("Summarizer", List.of("Retriever", "Ranker"));
        validDeps.put("ResponseGenerator", List.of("Summarizer"));

        List<String> order = solver.resolveExecutionOrder(validDeps);
        System.out.println("Execution order: " + order);
        // Expected: [QueryParser, Retriever, Ranker, Summarizer, ResponseGenerator]

        // Trace:
        // Round 1: ready=[QueryParser]         → remove from others
        // Round 2: ready=[Retriever, Ranker]   → remove from others
        // Round 3: ready=[Summarizer]          → remove from others
        // Round 4: ready=[ResponseGenerator]   → done

        // --- Test Case 2: Circular Dependency ---
        System.out.println("\n=== Test Case 2: Cycle Detection ===");
        Map<String, List<String>> cyclicDeps = new LinkedHashMap<>();
        cyclicDeps.put("Planner", List.of("Validator"));
        cyclicDeps.put("Executor", List.of("Planner"));
        cyclicDeps.put("Validator", List.of("Executor"));
        cyclicDeps.put("Reporter", List.of("Validator"));

        try {
            solver.resolveExecutionOrder(cyclicDeps);
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
            // Expected: Circular dependency detected among: [Planner, Executor, Validator, Reporter]
        }

        // Trace:
        // Round 1: scan all — nobody has empty deps → ready=[] → CYCLE
    }
}
