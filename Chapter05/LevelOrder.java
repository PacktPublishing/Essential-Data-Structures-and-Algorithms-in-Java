import java.util.*;

public class LevelOrder {

    public static String levelOrder(TreeNode root) {
        // One bucket (list) per level. levels.get(d) holds the values at depth d,
        // in left-to-right order because we recurse left before right.
        List<List<Integer>> levels = new ArrayList<>();
        dfs(root, 0, levels);

        // Join: values within a level by ',', levels by ';'.
        StringBuilder sb = new StringBuilder();
        for (int d = 0; d < levels.size(); d++) {
            if (d > 0) sb.append(';');
            List<Integer> level = levels.get(d);
            for (int i = 0; i < level.size(); i++) {
                if (i > 0) sb.append(',');
                sb.append(level.get(i));
            }
        }
        return sb.toString();
    }

    private static void dfs(TreeNode node, int depth, List<List<Integer>> levels) {
        if (node == null) return;

        // First time we reach this depth, create its bucket. Because DFS reaches
        // a given depth on the LEFT side before the right, and we always recurse
        // left first, values land in correct left-to-right order per level.
        if (depth == levels.size()) {
            levels.add(new ArrayList<>());
        }
        levels.get(depth).add(node.val);

        dfs(node.left, depth + 1, levels);
        dfs(node.right, depth + 1, levels);
    }
}
