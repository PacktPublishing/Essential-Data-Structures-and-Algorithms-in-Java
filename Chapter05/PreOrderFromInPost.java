import java.util.*;

public class PreOrderFromInPost {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    public static String preOrder(int[] inorder, int[] postorder) {
        // Map each in-order value to its index for O(1) root location.
        Map<Integer, Integer> inIndex = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inIndex.put(inorder[i], i);
        }

        // postIdx is a moving pointer consuming post-order from the RIGHT.
        // Passed via a single-element array so recursion can mutate it.
        int[] postIdx = { postorder.length - 1 };
        TreeNode root = build(0, inorder.length - 1, postorder, postIdx, inIndex);

        // Phase 2: ordinary pre-order traversal (root, left, right).
        List<Integer> pre = new ArrayList<>();
        emitPreOrder(root, pre);

        // Join with commas, no trailing comma.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pre.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(pre.get(i));
        }
        return sb.toString();
    }

    /** Reconstruct the subtree whose in-order span is [inLeft, inRight]. */
    private static TreeNode build(int inLeft, int inRight,
                                  int[] postorder, int[] postIdx,
                                  Map<Integer, Integer> inIndex) {
        if (inLeft > inRight) return null;       // empty subtree

        // The current end of post-order is this subtree's root.
        int rootVal = postorder[postIdx[0]--];
        TreeNode node = new TreeNode(rootVal);
        int mid = inIndex.get(rootVal);          // split point in in-order

        // Reading post-order from the right gives root, then RIGHT subtree,
        // then LEFT. So build the RIGHT child BEFORE the left to keep postIdx
        // aligned with the values we pop.
        node.right = build(mid + 1, inRight, postorder, postIdx, inIndex);
        node.left  = build(inLeft, mid - 1, postorder, postIdx, inIndex);
        return node;
    }

    private static void emitPreOrder(TreeNode node, List<Integer> pre) {
        if (node == null) return;
        pre.add(node.val);                       // root
        emitPreOrder(node.left, pre);            // left
        emitPreOrder(node.right, pre);           // right
    }
}
