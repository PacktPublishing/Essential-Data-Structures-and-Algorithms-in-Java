import java.util.ArrayList;
import java.util.List;

public class BinaryTreeTraversals {

    /**
     * Pre-order Traversal: Processes the tree in the order of Root -> Left -> Right.
     * Use Case: Excellent for cloning, copying, or serializing a tree structure.
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        
        // Edge/Base Case: An empty node returns an empty list to prevent 
        // a NullPointerException during parent frame collection merging.
        if (root == null) {
            return res;
        }
        
        // Step 1: Visit and process the current Root node immediately.
        res.add(root.val);
        
        // Step 2: Recursively exhaust the Left subtree entirely.
        res.addAll(preorderTraversal(root.left));
        
        // Step 3: Recursively exhaust the Right subtree entirely.
        res.addAll(preorderTraversal(root.right));
        
        return res;
    }

    /**
     * In-order Traversal: Processes the tree in the order of Left -> Root -> Right.
     * Use Case: Extracting elements from a Binary Search Tree (BST) in sorted order.
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        
        if (root == null) {
            return res;
        }
        
        // Step 1: Drill down to the deepest elements of the Left subtree first.
        res.addAll(inorderTraversal(root.left));
        
        // Step 2: Process the current Root node after returning from the left.
        res.add(root.val);
        
        // Step 3: Switch context and descend down into the Right subtree.
        res.addAll(inorderTraversal(root.right));
        
        return res;
    }

    /**
     * Post-order Traversal: Processes the tree in the order of Left -> Right -> Root.
     * Use Case: Bottom-up tasks like calculating subtree sizes, heights, or deletion.
     */
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        
        if (root == null) {
            return res;
        }
        
        // Step 1: Fully evaluate the Left subtree.
        res.addAll(postorderTraversal(root.left));
        
        // Step 2: Fully evaluate the Right subtree.
        res.addAll(postorderTraversal(root.right));
        
        // Step 3: Process the current Root node only after both children complete.
        res.add(root.val);
        
        return res;
    }
}
