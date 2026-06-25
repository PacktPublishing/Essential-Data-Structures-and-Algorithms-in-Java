import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
 
/**
* Thread-safe model registry optimized for read-heavy access.
* ReadWriteLock eliminates contention between concurrent readers.
*/
public class ModelRegistry {
 
   private final Map<String, String> models = new HashMap<>();
   private final ReadWriteLock lock = new ReentrantReadWriteLock();
   private final Lock readLock = lock.readLock();
   private final Lock writeLock = lock.writeLock();
 
   /**
    * Looks up a model endpoint. Read lock allows concurrent access —
    * N threads can execute this simultaneously with zero contention.
    * Only blocks if a writer is currently active.
    */
   public Optional<String> getEndpoint(String modelName) {
       readLock.lock();
       try {
           return Optional.ofNullable(models.get(modelName));
       } finally {
           readLock.unlock(); // always in finally — prevents deadlock on exception
       }
   }
 
   /**
    * Returns all model names. Read lock — concurrent with other reads.
    * Returns defensive copy so callers can't mutate internal state.
    */
   public Set<String> listModels() {
       readLock.lock();
       try {
           return new HashSet<>(models.keySet());
       } finally {
           readLock.unlock();
       }
   }
 
   /**
    * Registers a model. Write lock — exclusive access.
    * All concurrent readers block until this completes.
    * This is acceptable because writes are rare (deployment events).
    */
   public void register(String modelName, String endpoint) {
       writeLock.lock();
       try {
           models.put(modelName, endpoint);
       } finally {
           writeLock.unlock();
       }
        // log after releasing the lock
        System.out.println("[Registry] Registered: " + modelName + " → " + endpoint);       }
 
   /**
    * Removes a model. Write lock — exclusive.
    */
   public boolean deregister(String modelName) {
       writeLock.lock();
       try {
           return models.remove(modelName) != null;
       } finally {
           writeLock.unlock();
       }
   }
 
   /**
    * Atomic check-and-register. Write lock held for entire operation
    * to prevent TOCTOU race (check if exists → register).
    */
   public boolean registerIfAbsent(String modelName, String endpoint) {
       writeLock.lock();
       try {
           if (models.containsKey(modelName)) return false;
           models.put(modelName, endpoint);
           return true;
       } finally {
           writeLock.unlock();
       }
   }
 
   public static void main(String[] args) throws InterruptedException {
       ModelRegistry registry = new ModelRegistry();
       registry.register("claude-3.5", "https://api.anthropic.com/v1/messages");
       registry.register("gpt-4", "https://api.openai.com/v1/chat/completions");
 
       ExecutorService pool = Executors.newFixedThreadPool(10);
 
       // 8 reader threads — all run concurrently, zero lock contention between them
       for (int i = 0; i < 8; i++) {
           final int id = i;
           pool.submit(() -> {
               for (int j = 0; j < 5; j++) {
                   Optional<String> ep = registry.getEndpoint("claude-3.5");
                   System.out.printf("[Reader-%d] claude-3.5 → %s%n", id, ep.orElse("N/A"));
                   try { Thread.sleep(50); } catch (InterruptedException ignored) {}
               }
           });
       }
 
       // 1 writer thread — briefly blocks readers during update
       pool.submit(() -> {
           try { Thread.sleep(150); } catch (InterruptedException ignored) {}
           registry.register("claude-4", "https://api.anthropic.com/v2/messages");
           registry.deregister("gpt-4");
       });
 
       pool.shutdown();
       pool.awaitTermination(5, TimeUnit.SECONDS);
 
       System.out.println("\nFinal models: " + registry.listModels());
       // [claude-3.5, claude-4]
   }
}
 
