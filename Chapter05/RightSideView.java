import java.util.*;

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
public class RightSideView {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        
        // Handle empty tree case
        if (root == null) {
            return result;
        }
        
        // Queue for level-order traversal
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // Process each level
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            
            // Process all nodes in current level
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                
                // The last node processed in this level is the rightmost node
                if (i == levelSize - 1) {
                    result.add(node.val);
                }
                
                // Add children to queue for next level
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        RightSideView solution = new RightSideView();
        
        // Test case 1: [1,2,3,null,5,null,4]
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.right = new TreeNode(5);
        root1.right.right = new TreeNode(4);
        System.out.println("Test 1: " + solution.rightSideView(root1)); // Expected: [1,3,4]
        
        // Test case 2: [1,null,3]
        TreeNode root2 = new TreeNode(1);
        root2.right = new TreeNode(3);
        System.out.println("Test 2: " + solution.rightSideView(root2)); // Expected: [1,3]
    }
}

