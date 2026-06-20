/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
public class MaxDepthBinaryTree {
    public int maxDepth(TreeNode root) {
        // Base case: if node is null, depth is 0
        if (root == null) {
            return 0;
        }
        
        // Recursive case: depth = 1 + max depth of left and right subtrees
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        
        // Return the maximum depth + 1 (for current node)
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    public static void main(String[] args) {
        MaxDepthBinaryTree solution = new MaxDepthBinaryTree();
        
        // Test case 1: [3,9,20,null,null,15,7]
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        System.out.println("Test 1: " + solution.maxDepth(root1)); // Expected: 3
        
        // Test case 2: [1,null,2]
        TreeNode root2 = new TreeNode(1);
        root2.right = new TreeNode(2);
        System.out.println("Test 2: " + solution.maxDepth(root2)); // Expected: 2
    }
}

