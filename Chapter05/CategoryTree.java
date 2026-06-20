import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* Hierarchical knowledge base category tree.
* Supports finding the full path from root to any target node via DFS.
*/
public class CategoryTree {

   private final String name;
   private final List<CategoryTree> children;

   public CategoryTree(String name) {
       this.name = name;
       this.children = new ArrayList<>();
   }

   public CategoryTree addChild(String childName) {
       CategoryTree child = new CategoryTree(childName);
       this.children.add(child);
       return child;
   }

   /**
    * Finds the path from this node (root) to the target node.
    * Returns empty list if target is not found.
    *
    * Approach: DFS with backtracking. Build path on descent,
    * remove on backtrack if branch doesn't contain target.
    *
    * Time: O(n) — visits each node at most once
    * Space: O(h) — recursion depth equals tree height
    */
   public List<String> findPath(String target) {
       List<String> path = new ArrayList<>();
       if (dfs(this, target, path)) {
           return path;
       }
       return Collections.emptyList();
   }

   private boolean dfs(CategoryTree node, String target, List<String> path) {
       if (node == null) return false;

       // Add current node to path (tentatively)
       path.add(node.name);

       // Base case: found the target
       if (node.name.equals(target)) {
           return true;
       }

       // Recursive case: search all children
       for (CategoryTree child : node.children) {
           if (dfs(child, target, path)) {
               return true; // target found in this subtree
           }
       }

       // Backtrack: target not in this subtree, remove current node
       path.remove(path.size() - 1);
       return false;
   }

   public static void main(String[] args) {
       // Build the knowledge base tree
       CategoryTree root = new CategoryTree("Knowledge");

       CategoryTree engineering = root.addChild("Engineering");
       CategoryTree product = root.addChild("Product");

       CategoryTree backend = engineering.addChild("Backend");
       CategoryTree frontend = engineering.addChild("Frontend");

       CategoryTree databases = backend.addChild("Databases");
       databases.addChild("PostgreSQL");
       databases.addChild("DynamoDB");

       // Find path to PostgreSQL
       List<String> path = root.findPath("PostgreSQL");
       System.out.println(path);
       // [Knowledge, Engineering, Backend, Databases, PostgreSQL]

       // Find path to non-existent node
       List<String> missing = root.findPath("Redis");
       System.out.println(missing);
       // []
   }
}
