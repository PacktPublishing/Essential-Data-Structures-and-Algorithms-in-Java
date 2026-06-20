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
public class LowestCommonAncestor {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // Base case: if root is null or root is one of the target nodes
        if (root == null || root == p || root == q) {
            return root;
        }
        
        // Recursively search in left and right subtrees
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // If both left and right are not null, current root is the LCA
        if (left != null && right != null) {
            return root;
        }
        
        // If only one side has a match, return that side's result
        return left != null ? left : right;
    }
    
    public static void main(String[] args) {
        LowestCommonAncestor solution = new LowestCommonAncestor();
        
        // Test case 1: [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(5);
        root1.right = new TreeNode(1);
        root1.left.left = new TreeNode(6);
        root1.left.right = new TreeNode(2);
        root1.right.left = new TreeNode(0);
        root1.right.right = new TreeNode(8);
        root1.left.right.left = new TreeNode(7);
        root1.left.right.right = new TreeNode(4);
        
        TreeNode p1 = root1.left;  // Node with value 5
        TreeNode q1 = root1.right; // Node with value 1
        System.out.println("Test 1: " + solution.lowestCommonAncestor(root1, p1, q1).val); // Expected: 3
        
        // Test case 2: [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
        TreeNode p2 = root1.left;      // Node with value 5
        TreeNode q2 = root1.left.right.right; // Node with value 4
        System.out.println("Test 2: " + solution.lowestCommonAncestor(root1, p2, q2).val); // Expected: 5
    }
}

