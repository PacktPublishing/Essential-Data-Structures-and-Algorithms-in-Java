import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
* Fixed-size sliding token buffer for LLM context window management.
* Uses ArrayDeque as a circular buffer for O(1) append and eviction.
*/
public class TokenWindow {

   private final int capacity;
   private final Deque<String> buffer; // doubly-ended queue for O(1) at both ends

   public TokenWindow(int capacity) {
       if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
       this.capacity = capacity;
       this.buffer = new ArrayDeque<>(capacity);
   }

   /**
    * Appends a token to the window. Evicts the oldest token if at capacity.
    * Time: O(1)
    */
   public void append(String token) {
       if (buffer.size() == capacity) {
           buffer.removeFirst(); // evict oldest — O(1)
       }
       buffer.addLast(token); // append newest — O(1)
   }

   /**
    * Returns current window contents in insertion order.
    * Time: O(k) where k = capacity
    */
   public List<String> getWindow() {
       return new ArrayList<>(buffer);
   }

   public int size() {
       return buffer.size();
   }

   public static void main(String[] args) {
       TokenWindow window = new TokenWindow(4);
       window.append("The");
       window.append("agent");
       window.append("processes");
       window.append("input");
       System.out.println(window.getWindow()); // [The, agent, processes, input]

       window.append("tokens");
       System.out.println(window.getWindow()); // [agent, processes, input, tokens]

       window.append("efficiently");
       System.out.println(window.getWindow()); // [processes, input, tokens, efficiently]
   }
}
