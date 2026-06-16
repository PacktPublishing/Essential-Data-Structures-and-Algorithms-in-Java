import java.util.*;

// Definition for a binary tree node
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    
    TreeNode() {}
    
    TreeNode(int val) {
        this.val = val;
    }
    
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class PostorderWithStackTraversal {
    
    /**
     * Iterative postorder traversal using one stack
     * Algorithm:
     * 1. Use a stack to simulate recursion
     * 2. Push root node first
     * 3. For each node popped, add its value to result (but in reverse order)
     * 4. Push left child first, then right child (so right is processed first)
     * 5. Reverse the final result to get correct postorder
     * 
     * Time Complexity: O(n) where n is number of nodes
     * Space Complexity: O(h) where h is height of tree
     */
    public static String postorderTraversal(TreeNode root) {
        if (root == null) {
            return "";
        }
        
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> result = new ArrayList<>();
        
        // Step 1: Push root node to stack
        stack.push(root);
        
        // Step 2: Process nodes iteratively
        while (!stack.isEmpty()) {
            // Step 3: Pop node from stack
            TreeNode node = stack.pop();
            
            // Step 4: Add node value to result (in reverse order)
            result.add(node.val);
            
            // Step 5: Push left child first, then right child
            // This ensures right child is processed before left child
            if (node.left != null) {
                stack.push(node.left);
            }
            if (node.right != null) {
                stack.push(node.right);
            }
        }
        
        // Step 6: Reverse result to get correct postorder
        Collections.reverse(result);
        
        // Step 7: Convert to comma-separated string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            sb.append(result.get(i));
            if (i < result.size() - 1) {
                sb.append(",");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Alternative approach using stack with visited flag
     * This approach is more intuitive but uses additional space
     */
    public static String postorderTraversalAlternative(TreeNode root) {
        if (root == null) {
            return "";
        }
        
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> result = new ArrayList<>();
        TreeNode prev = null; // Keep track of previously visited node
        
        stack.push(root);
        
        while (!stack.isEmpty()) {
            TreeNode current = stack.peek();
            
            // Case 1: Leaf node or we've already visited children
            if (current.left == null && current.right == null || 
                (prev != null && (prev == current.left || prev == current.right))) {
                
                result.add(current.val);
                stack.pop();
                prev = current;
            }
            // Case 2: Process right child first, then left child
            else {
                if (current.right != null) {
                    stack.push(current.right);
                }
                if (current.left != null) {
                    stack.push(current.left);
                }
            }
        }
        
        // Convert result to comma-separated string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            sb.append(result.get(i));
            if (i < result.size() - 1) {
                sb.append(",");
            }
        }
        
        return sb.toString();
    }
    
    // Test method
    public static void main(String[] args) {
        // Create test tree:
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        
        System.out.println("Tree structure:");
        System.out.println("       1");
        System.out.println("      / \\");
        System.out.println("     2   3");
        System.out.println("    / \\");
        System.out.println("   4   5");
        System.out.println();
        
        System.out.println("Postorder traversal (iterative, one stack):");
        String result1 = postorderTraversal(root);
        System.out.println("Result: " + result1); // Expected: 4,5,2,3,1
        
        System.out.println("\nAlternative approach:");
        String result2 = postorderTraversalAlternative(root);
        System.out.println("Result: " + result2); // Expected: 4,5,2,3,1
        
        // Test with single node
        TreeNode singleNode = new TreeNode(42);
        System.out.println("\nSingle node test:");
        System.out.println("Result: " + postorderTraversal(singleNode)); // Expected: 42
        
        // Test with empty tree
        System.out.println("\nEmpty tree test:");
        System.out.println("Result: " + postorderTraversal(null)); // Expected: ""
    }
}

