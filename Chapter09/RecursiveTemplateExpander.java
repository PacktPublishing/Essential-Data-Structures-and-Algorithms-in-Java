import java.util.*;

public class RecursiveTemplateExpander {

    // Delegate to helper with cycle-detection set
    public String expand(String template, Map<String, String> vars) {
        return expand(template, vars, new HashSet<>());
    }

    private String expand(String template, Map<String, String> vars, Set<String> seen) {

        // [1] Find next {{varName}} in template
        int start = template.indexOf("{{");
        int end = (start != -1) ? template.indexOf("}}", start) : -1;

        // [2] If not found → return template as-is (base case)
        if (start == -1 || end == -1) {
            return template;
        }

        // [3] If varName already in seen → throw cycle error
        String varName = template.substring(start + 2, end);
        if (seen.contains(varName)) {
            throw new IllegalStateException("Circular reference: " + varName);
        }

        // [4] Look up value, add to seen, recurse on value, remove from seen
        String value = vars.getOrDefault(varName, "{{" + varName + "}}");
        seen.add(varName);
        String expanded = expand(value, vars, seen);
        seen.remove(varName);

        // [5] Replace placeholder with expanded value
        String result = template.substring(0, start) + expanded + template.substring(end + 2);

        // [6] Recurse on remaining template (may have more variables)
        return expand(result, vars, seen);
    }

    public static void main(String[] args) {
        RecursiveTemplateExpander expander = new RecursiveTemplateExpander();

        // Test 1: Nested expansion
        Map<String, String> vars = Map.of(
            "greeting", "Hello {{name}}",
            "name", "Agent {{version}}",
            "version", "3.5",
            "task", "summarize {{topic}}",
            "topic", "recursion"
        );
        System.out.println(expander.expand("{{greeting}}, please {{task}}", vars));
        // → "Hello Agent 3.5, please summarize recursion"

        // Test 2: Cycle detection
        Map<String, String> cyclic = new HashMap<>();
        cyclic.put("a", "{{b}}");
        cyclic.put("b", "{{a}}");
        try {
            expander.expand("{{a}}", cyclic);
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
            // → "Caught: Circular reference: a"
        }
    }
}
