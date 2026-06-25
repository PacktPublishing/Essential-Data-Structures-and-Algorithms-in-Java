public class FixedRequestContextHolder {
    private static final ThreadLocal<UserContext> context = new ThreadLocal<>();
    
    public void handle(Request req) {
        try {
            context.set(new UserContext(req));
            process(req);
        } finally {
            context.remove(); // Clean up ThreadLocal data
        }
    }
}

