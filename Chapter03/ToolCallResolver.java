import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
* Parses nested tool call expressions and returns execution order (innermost first).
* Uses a stack to track opening parentheses positions.
*/
public class ToolCallResolver {

   /**
    * Given a nested expression like "search(summarize(read_file(config.yaml)))",
    * returns the execution order: innermost tool executes first.
    *
    * Approach: scan left-to-right, push '(' positions onto stack.
    * On ')', pop to find the matching '(', extract the full tool call.
    *
    * Time: O(n) where n = expression length
    * Space: O(d) where d = max nesting depth
    */
   public List<String> getExecutionOrder(String expression) {
       List<String> order = new ArrayList<>();
       Deque<Integer> openParens = new ArrayDeque<>(); // stack of '(' indices

       for (int i = 0; i < expression.length(); i++) {
           char c = expression.charAt(i);

           if (c == '(') {
               openParens.push(i);
           } else if (c == ')') {
               if (openParens.isEmpty()) {
                   throw new IllegalArgumentException("Unmatched ')' at index " + i);
               }
               int parenStart = openParens.pop();

               // Walk backward from '(' to find the start of the function name
               int nameStart = parenStart;
               while (nameStart > 0 && isIdentifierChar(expression.charAt(nameStart - 1))) {
                   nameStart--;
               }

               // Extract the full tool call: functionName(...)
               String toolCall = expression.substring(nameStart, i + 1);
               order.add(toolCall);
           }
       }

       if (!openParens.isEmpty()) {
           throw new IllegalArgumentException("Unmatched '(' in expression");
       }

       return order;
   }

   private boolean isIdentifierChar(char c) {
       return Character.isLetterOrDigit(c) || c == '_';
   }

   public static void main(String[] args) {
       ToolCallResolver resolver = new ToolCallResolver();

       String expr = "search(summarize(read_file(config.yaml)))";
       List<String> order = resolver.getExecutionOrder(expr);

       System.out.println("Execution order:");
       for (int i = 0; i < order.size(); i++) {
           System.out.printf("  %d. %s%n", i + 1, order.get(i));
       }
       // 1. read_file(config.yaml)
       // 2. summarize(read_file(config.yaml))
       // 3. search(summarize(read_file(config.yaml)))
   }
}
