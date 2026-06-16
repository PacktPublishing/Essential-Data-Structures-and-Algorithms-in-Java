public class AIStateHashMap {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    // Inner class to store agent state data
    private static class AgentState {
        String agentId;
        Map<String, Object> state;
        
        AgentState(String agentId) {
            this.agentId = agentId;
            this.state = new HashMap<>();
        }
    }
    
    // HashMap to store agent states - key is agentId, value is AgentState object
    private Map<String, AgentState> agentStates;
    private int size;
    private int capacity;
    
    public AIStateHashMap() {
        // Initialize with initial capacity
        this.capacity = INITIAL_CAPACITY;
        this.agentStates = new HashMap<>(capacity);
        this.size = 0;
    }
    
    /**
     * Put a key-value pair in agent's state
     * @param agentId Unique identifier for the agent
     * @param key State key to set
     * @param value State value to set
     * Time Complexity: O(1) average case
     * Space Complexity: O(1) average case
     */
    public void put(String agentId, String key, Object value) {
        // Handle null agent ID gracefully
        if (agentId == null) {
            return;
        }
        
        // Check if agent already exists in the map
        AgentState agentState = agentStates.get(agentId);
        
        // If agent doesn't exist, create new AgentState object
        if (agentState == null) {
            agentState = new AgentState(agentId);
            agentStates.put(agentId, agentState);
            size++;
        }
        
        // Add the key-value pair to the agent's state map
        agentState.state.put(key, value);
    }
    
    /**
     * Get a value from agent's state
     * @param agentId Unique identifier for the agent
     * @param key State key to retrieve
     * @return State value or null if not found
     * Time Complexity: O(1) average case
     * Space Complexity: O(1) average case
     */
    public Object get(String agentId, String key) {
        // Handle null agent ID gracefully
        if (agentId == null) {
            return null;
        }
        
        // Retrieve the agent's state from the main HashMap
        AgentState agentState = agentStates.get(agentId);
        
        // If agent doesn't exist, return null
        if (agentState == null) {
            return null;
        }
        
        // Retrieve the specific key-value pair from agent's state map
        return agentState.state.get(key);
    }
    
    /**
     * Get all state for an agent
     * @param agentId Unique identifier for the agent
     * @return Complete state map or null if agent not found
     * Time Complexity: O(1) average case
     * Space Complexity: O(1) average case
     */
    public Map<String, Object> getAgentState(String agentId) {
        // Handle null agent ID gracefully
        if (agentId == null) {
            return null;
        }
        
        // Retrieve the agent's state from the main HashMap
        AgentState agentState = agentStates.get(agentId);
        
        // If agent doesn't exist, return null
        if (agentState == null) {
            return null;
        }
        
        // Return the complete state map for this agent
        return agentState.state;
    }
    
    /**
     * Remove an agent's state
     * @param agentId Unique identifier for the agent
     * @return Removed state or null if agent not found
     * Time Complexity: O(1) average case
     * Space Complexity: O(1) average case
     */
    public Map<String, Object> remove(String agentId) {
        // Handle null agent ID gracefully
        if (agentId == null) {
            return null;
        }
        
        // Remove and return the agent's state from the main HashMap
        AgentState removedAgentState = agentStates.remove(agentId);
        
        // If agent existed, decrement size and return the state
        if (removedAgentState != null) {
            size--;
            return removedAgentState.state;
        }
        
        // If agent didn't exist, return null
        return null;
    }
}

