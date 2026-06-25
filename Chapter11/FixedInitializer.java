public class FixedInitializer {
    private volatile ExpensiveResource resource;  // Add volatile keyword
    
    public ExpensiveResource getResource() {
        if (resource == null) {
            synchronized (this) {
                if (resource == null) {
                    resource = new ExpensiveResource();
                }
            }
        }
        return resource;
    }
}

