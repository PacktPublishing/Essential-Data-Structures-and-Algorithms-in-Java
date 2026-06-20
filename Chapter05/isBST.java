public boolean isBST(TreeNode node, int min, int max) {
    if (node == null) {
        return true;
    }
    boolean left  = isBST(node.getLeft(),  min, node.getValue());
    boolean right = isBST(node.getRight(), node.getValue(), max);
    return left && right
        && (node.getValue() < max)
        && (node.getValue() >= min);
}
