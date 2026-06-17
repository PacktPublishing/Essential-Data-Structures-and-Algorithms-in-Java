import java.util.*;

/**
 * HashMap-based system for managing AI agent states in a distributed AI platform
 * Supports efficient state lookup, update, and retrieval operations with O(1) average-case performance
 */
public class AIStateHashMap {

    // Inner class to store agent state data
    private static class AgentState {
        String agentId;
        Map<String, Object> state;

        AgentState(String agentId) {
            this.agentId = agentId;
            this.state = new HashMap<>();
        }
    }

    // HashMap to store agent states
    private Map<String, AgentState> agentStates;

    /**
     * Constructor to initialize the AI state HashMap
     */
    public AIStateHashMap() {
        this.agentStates = new HashMap<>();
    }

    /**
     * Put a key-value pair in agent's state
     * @param agentId Unique identifier for the agent
     * @param key State key to set
     * @param value State value to set
     * @throws IllegalArgumentException if agentId is null
     */
    public void put(String agentId, String key, Object value) {
        if (agentId == null) {
            throw new IllegalArgumentException("Agent ID cannot be null");
        }

        AgentState agentState = agentStates.get(agentId);

        if (agentState == null) {
            agentState = new AgentState(agentId);
            agentStates.put(agentId, agentState);
        }

        agentState.state.put(key, value);
    }

    /**
     * Get a value from agent's state
     * @param agentId Unique identifier for the agent
     * @param key State key to retrieve
     * @return State value or null if not found
     * @throws IllegalArgumentException if agentId is null
     */
    public Object get(String agentId, String key) {
        if (agentId == null) {
            throw new IllegalArgumentException("Agent ID cannot be null");
        }

        AgentState agentState = agentStates.get(agentId);
        if (agentState == null) {
            return null;
        }

        return agentState.state.get(key);
    }

    /**
     * Get all state for an agent
     * @param agentId Unique identifier for the agent
     * @return Complete state map or null if agent not found
     * @throws IllegalArgumentException if agentId is null
     */
    public Map<String, Object> getAgentState(String agentId) {
        if (agentId == null) {
            throw new IllegalArgumentException("Agent ID cannot be null");
        }

        AgentState agentState = agentStates.get(agentId);
        if (agentState == null) {
            return null;
        }

        // Return a copy to prevent external modification
        return new HashMap<>(agentState.state);
    }

    /**
     * Remove an agent's state
     * @param agentId Unique identifier for the agent
     * @return Removed state or null if agent not found
     * @throws IllegalArgumentException if agentId is null
     */
    public Map<String, Object> remove(String agentId) {
        if (agentId == null) {
            throw new IllegalArgumentException("Agent ID cannot be null");
        }

        AgentState removedState = agentStates.remove(agentId);
        if (removedState == null) {
            return null;
        }

        return new HashMap<>(removedState.state);
    }

    /**
     * Get the number of agents in the system
     * @return Number of agents
     */
    public int size() {
        return agentStates.size();
    }

    /**
     * Check if the system is empty
     * @return true if no agents, false otherwise
     */
    public boolean isEmpty() {
        return agentStates.isEmpty();
    }

    /**
     * Check if an agent exists in the system
     * @param agentId Unique identifier for the agent
     * @return true if agent exists, false otherwise
     * @throws IllegalArgumentException if agentId is null
     */
    public boolean containsAgent(String agentId) {
        if (agentId == null) {
            throw new IllegalArgumentException("Agent ID cannot be null");
        }
        return agentStates.containsKey(agentId);
    }

    /**
     * Clear all agent states from the system
     */
    public void clear() {
        agentStates.clear();
    }

    /**
     * Main method with test cases
     */
    public static void main(String[] args) {
        // Test Case 1: put("agent-001", "status", "active"), get("agent-001", "status")
        System.out.println("Test Case 1:");
        AIStateHashMap aiSystem = new AIStateHashMap();
        aiSystem.put("agent-001", "status", "active");
        Object result1 = aiSystem.get("agent-001", "status");
        System.out.println("Expected: \"active\", Got: " + result1);

        // Test Case 2: put("agent-002", "cpu_usage", 85.5), put("agent-002", "memory_usage", 42.3), get("agent-002", "cpu_usage")
        System.out.println("\nTest Case 2:");
        aiSystem.put("agent-002", "cpu_usage", 85.5);
        aiSystem.put("agent-002", "memory_usage", 42.3);
        Object result2 = aiSystem.get("agent-002", "cpu_usage");
        System.out.println("Expected: 85.5, Got: " + result2);

        // Additional test: getAgentState
        System.out.println("\nAdditional Test:");
        Map<String, Object> agentState = aiSystem.getAgentState("agent-002");
        System.out.println("Agent state for agent-002: " + agentState);

        // Additional test: remove
        System.out.println("\nRemove Test:");
        Map<String, Object> removedState = aiSystem.remove("agent-001");
        System.out.println("Removed agent-001 state: " + removedState);
        Object remaining = aiSystem.get("agent-001", "status");
        System.out.println("Status after removal should be null: " + remaining);

        // Test null handling
        System.out.println("\nNull Handling Test:");
        try {
            aiSystem.get(null, "key");
        } catch (IllegalArgumentException e) {
            System.out.println("Null agent ID properly handled: " + e.getMessage());
        }
    }
}
