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

public class BinaryTreeReconstruction {
    
    // Main method to reconstruct tree and get preorder traversal
    public static List<Integer> buildTreeAndPreorder(int[] inorder, int[] postorder) {
        if (inorder == null || postorder == null || inorder.length == 0 || postorder.length == 0) {
            return new ArrayList<>();
        }
        
        // Create a map for quick lookup of indices in inorder array
        Map<Integer, Integer> inorderMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderMap.put(inorder[i], i);
        }
        
        // Reconstruct the tree
        TreeNode root = buildTreeHelper(inorder, postorder, 0, inorder.length - 1, 
                                      0, postorder.length - 1, inorderMap);
        
        // Get preorder traversal
        List<Integer> preorder = new ArrayList<>();
        preorderTraversal(root, preorder);
        
        return preorder;
    }
    
    // Helper method to reconstruct tree recursively
    private static TreeNode buildTreeHelper(int[] inorder, int[] postorder, 
                                          int inStart, int inEnd, 
                                          int postStart, int postEnd, 
                                          Map<Integer, Integer> inorderMap) {
        // Base case
        if (inStart > inEnd || postStart > postEnd) {
            return null;
        }
        
        // The last element in postorder is the root
        int rootVal = postorder[postEnd];
        TreeNode root = new TreeNode(rootVal);
        
        // Find root position in inorder
        int rootIndex = inorderMap.get(rootVal);
        
        // Calculate the size of left subtree
        int leftSubtreeSize = rootIndex - inStart;
        
        // Recursively build left and right subtrees
        root.left = buildTreeHelper(inorder, postorder, inStart, rootIndex - 1,
                                  postStart, postStart + leftSubtreeSize - 1, inorderMap);
        
        root.right = buildTreeHelper(inorder, postorder, rootIndex + 1, inEnd,
                                   postStart + leftSubtreeSize, postEnd - 1, inorderMap);
        
        return root;
    }
    
    // Helper method to get preorder traversal
    private static void preorderTraversal(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        result.add(root.val);
        preorderTraversal(root.left, result);
        preorderTraversal(root.right, result);
    }
    
    // Helper method to print tree structure (for visualization)
    public static void printTree(TreeNode root) {
        if (root == null) {
            System.out.println("Empty tree");
            return;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node != null) {
                    System.out.print(node.val + " ");
                    queue.offer(node.left);
                    queue.offer(node.right);
                } else {
                    System.out.print("null ");
                }
            }
            System.out.println();
        }
    }
    
    // Main method with demo
    public static void main(String[] args) {
        System.out.println("=== Binary Tree Reconstruction Demo ===\n");
        
        // Demo 1
        System.out.println("Demo 1:");
        int[] inorder1 = {4, 2, 5, 1, 6, 3, 7};
        int[] postorder1 = {4, 5, 2, 6, 7, 3, 1};
        
        System.out.println("Inorder traversal: " + Arrays.toString(inorder1));
        System.out.println("Postorder traversal: " + Arrays.toString(postorder1));
        
        List<Integer> preorder1 = buildTreeAndPreorder(inorder1, postorder1);
        System.out.println("Preorder traversal: " + preorder1);
        
        // Reconstruct tree for visualization
        Map<Integer, Integer> inorderMap1 = new HashMap<>();
        for (int i = 0; i < inorder1.length; i++) {
            inorderMap1.put(inorder1[i], i);
        }
        TreeNode root1 = buildTreeHelper(inorder1, postorder1, 0, inorder1.length - 1, 
                                       0, postorder1.length - 1, inorderMap1);
        System.out.println("Tree structure:");
        printTree(root1);
        System.out.println();
        
        // Demo 2
        System.out.println("Demo 2:");
        int[] inorder2 = {9, 3, 15, 20, 7};
        int[] postorder2 = {9, 15, 7, 20, 3};
        
        System.out.println("Inorder traversal: " + Arrays.toString(inorder2));
        System.out.println("Postorder traversal: " + Arrays.toString(postorder2));
        
        List<Integer> preorder2 = buildTreeAndPreorder(inorder2, postorder2);
        System.out.println("Preorder traversal: " + preorder2);
        
        // Reconstruct tree for visualization
        Map<Integer, Integer> inorderMap2 = new HashMap<>();
        for (int i = 0; i < inorder2.length; i++) {
            inorderMap2.put(inorder2[i], i);
        }
        TreeNode root2 = buildTreeHelper(inorder2, postorder2, 0, inorder2.length - 1, 
                                       0, postorder2.length - 1, inorderMap2);
        System.out.println("Tree structure:");
        printTree(root2);
        System.out.println();
        
        // Demo 3 - Single node
        System.out.println("Demo 3 (Single node):");
        int[] inorder3 = {1};
        int[] postorder3 = {1};
        
        System.out.println("Inorder traversal: " + Arrays.toString(inorder3));
        System.out.println("Postorder traversal: " + Arrays.toString(postorder3));
        
        List<Integer> preorder3 = buildTreeAndPreorder(inorder3, postorder3);
        System.out.println("Preorder traversal: " + preorder3);
        
        // Reconstruct tree for visualization
        Map<Integer, Integer> inorderMap3 = new HashMap<>();
        for (int i = 0; i < inorder3.length; i++) {
            inorderMap3.put(inorder3[i], i);
        }
        TreeNode root3 = buildTreeHelper(inorder3, postorder3, 0, inorder3.length - 1, 
                                       0, postorder3.length - 1, inorderMap3);
        System.out.println("Tree structure:");
        printTree(root3);
        System.out.println();
        
        // Demo 4 - Empty tree
        System.out.println("Demo 4 (Empty tree):");
        int[] inorder4 = {};
        int[] postorder4 = {};
        
        System.out.println("Inorder traversal: " + Arrays.toString(inorder4));
        System.out.println("Postorder traversal: " + Arrays.toString(postorder4));
        
        List<Integer> preorder4 = buildTreeAndPreorder(inorder4, postorder4);
        System.out.println("Preorder traversal: " + preorder4);
    }
}

