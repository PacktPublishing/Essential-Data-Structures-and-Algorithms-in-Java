import java.util.ArrayList; 

import java.util.List; 

 

public class BfsRecursiveDemo { 

    static class Node { 

        int value; 

        List<Node> children = new ArrayList<>(); 

        Node(int v) { value = v; } 

        void addChild(Node c) { children.add(c); } 

    } 

    public static void bfs(Node root) { 

        if (root == null) return; 

        List<Node> level = new ArrayList<>(); 

        level.add(root); 

        bfsRecursive(level); 

    } 

    private static void bfsRecursive(List<Node> level) { 

        if (level.isEmpty()) return; 

        for (Node n : level) System.out.print(n.value + " "); 

        List<Node> next = new ArrayList<>(); 

        for (Node n : level) next.addAll(n.children); 

        bfsRecursive(next); 

    } 

    public static void main(String[] args) { 

        Node root = new Node(1); 

        Node a = new Node(2), b = new Node(3), c = new Node(4), d = new Node(5); 

        root.addChild(a); root.addChild(b); 

        a.addChild(c); a.addChild(d); 

        bfs(root);  // prints: 1 2 3 4 5 

    } 

} 
