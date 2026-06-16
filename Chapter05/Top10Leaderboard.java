import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * TOP 10 LEADERBOARD SYSTEM
 * 
 * Demonstrates a practical application: maintaining a fixed-size top-N leaderboard
 * Shows both PriorityQueue approach and custom implementation side-by-side.
 * 
 * Use case: Game leaderboard that keeps only top 10 players to save memory
 * when potentially thousands of players are competing.
 */
public class Top10Leaderboard {
    
    /**
     * VERSION 1: Using PriorityQueue (MIN-HEAP for top-K pattern)
     * 
     * Key insight: For top-K problems, use a MIN-heap of size K
     * - The smallest element in the heap is the Kth largest overall
     * - If new element > min, remove min and insert new element
     * - Heap always contains the K largest elements seen so far
     */
    static class PriorityQueueLeaderboard {
        private static final int TOP_K = 10;
        
        // MIN-heap: smallest score at root (natural ordering)
        // This keeps the 10th place score at the top for easy comparison
        private PriorityQueue<Player> topPlayers;
        
        public PriorityQueueLeaderboard() {
            // Initial capacity of 11, natural ordering (min-heap)
            this.topPlayers = new PriorityQueue<>(TOP_K + 1, 
                (a, b) -> Integer.compare(a.score, b.score));
        }
        
        /**
         * Add a player score to leaderboard
         * Time: O(log K) where K = 10, effectively O(1)
         */
        public void addScore(String name, int score) {
            Player player = new Player(name, score);
            
            if (topPlayers.size() < TOP_K) {
                // Leaderboard not full yet - just add
                topPlayers.offer(player);
            } else {
                // Leaderboard full - check if this score makes top 10
                Player minPlayer = topPlayers.peek();  // O(1) - check 10th place
                
                if (score > minPlayer.score) {
                    // New score beats 10th place
                    topPlayers.poll();      // O(log K) - remove 10th place
                    topPlayers.offer(player); // O(log K) - insert new score
                }
                // else: score doesn't make top 10, ignore it
            }
        }
        
        /**
         * Get top 10 players in descending order
         * Time: O(K log K) = O(10 log 10) ≈ O(1)
         */
        public List<Player> getTop10() {
            // Extract all and re-insert to preserve original heap
            List<Player> result = new ArrayList<>(topPlayers);
            
            // Sort descending (highest score first)
            result.sort((a, b) -> Integer.compare(b.score, a.score));
            
            return result;
        }
        
        public void printLeaderboard() {
            List<Player> top = getTop10();
            System.out.println("\n🏆 TOP 10 LEADERBOARD");
            System.out.println("━".repeat(40));
            
            for (int i = 0; i < top.size(); i++) {
                Player p = top.get(i);
                String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : "  ";
                System.out.printf("%s #%-2d  %-12s %,6d pts%n", 
                    medal, i + 1, p.name, p.score);
            }
        }
    }
    
    
    /**
     * VERSION 2: Custom Min-Heap Implementation
     * 
     * Same logic as PriorityQueue version but showing the mechanics
     */
    static class CustomLeaderboard {
        private static final int TOP_K = 10;
        
        // Array-based min-heap for top K players
        private ArrayList<Player> heap;
        
        public CustomLeaderboard() {
            this.heap = new ArrayList<>();
        }
        
        /**
         * Add a player score - same logic as PriorityQueue version
         */
        public void addScore(String name, int score) {
            Player player = new Player(name, score);
            
            if (heap.size() < TOP_K) {
                // Heap not full - insert normally
                insert(player);
            } else {
                // Check against minimum (root of min-heap)
                if (score > heap.get(0).score) {
                    // Replace minimum with new player
                    heap.set(0, player);
                    bubbleDown(0);  // Restore heap property
                }
            }
        }
        
        /**
         * Insert player into min-heap
         * Bubble up to maintain: parent <= children
         */
        private void insert(Player player) {
            heap.add(player);
            bubbleUp(heap.size() - 1);
        }
        
        /**
         * Bubble up for MIN-HEAP (opposite of max-heap)
         * Move element up while it's LESS than parent
         */
        private void bubbleUp(int index) {
            int current = index;
            
            while (current > 0) {
                int parent = (current - 1) / 2;
                
                // MIN-HEAP: if current >= parent, stop
                if (heap.get(current).score >= heap.get(parent).score) {
                    break;
                }
                
                swap(current, parent);
                current = parent;
            }
        }
        
        /**
         * Bubble down for MIN-HEAP
         * Move element down while it's GREATER than a child
         */
        private void bubbleDown(int index) {
            int current = index;
            
            while (true) {
                int left = 2 * current + 1;
                int right = 2 * current + 2;
                int smallest = current;
                
                // MIN-HEAP: find smallest among current and children
                if (left < heap.size() && 
                    heap.get(left).score < heap.get(smallest).score) {
                    smallest = left;
                }
                
                if (right < heap.size() && 
                    heap.get(right).score < heap.get(smallest).score) {
                    smallest = right;
                }
                
                if (smallest == current) break;
                
                swap(current, smallest);
                current = smallest;
            }
        }
        
        private void swap(int i, int j) {
            Player temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
        
        public List<Player> getTop10() {
            List<Player> result = new ArrayList<>(heap);
            result.sort((a, b) -> Integer.compare(b.score, a.score));
            return result;
        }
        
        public void printLeaderboard() {
            List<Player> top = getTop10();
            System.out.println("\n🏆 TOP 10 LEADERBOARD (Custom Heap)");
            System.out.println("━".repeat(40));
            
            for (int i = 0; i < top.size(); i++) {
                Player p = top.get(i);
                String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : "  ";
                System.out.printf("%s #%-2d  %-12s %,6d pts%n", 
                    medal, i + 1, p.name, p.score);
            }
            
            // Show internal heap structure
            System.out.println("\n📊 Internal min-heap structure:");
            System.out.println("   (Minimum at root = 10th place score)");
            System.out.print("   [");
            for (int i = 0; i < heap.size(); i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(heap.get(i).score);
            }
            System.out.println("]");
        }
    }
    
    
    /**
     * Player class - used by both implementations
     */
    static class Player {
        String name;
        int score;
        
        Player(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
    
    
    /**
     * Demo: Compare both implementations
     */
    public static void main(String[] args) {
        // Test data: 15 players, but only top 10 will be kept
        String[] names = {
            "Alice", "Bob", "Charlie", "Diana", "Eve",
            "Frank", "Grace", "Henry", "Iris", "Jack",
            "Kate", "Liam", "Mia", "Noah", "Olivia"
        };
        
        int[] scores = {
            1450, 980, 1340, 750, 1200,
            890, 1520, 660, 1150, 920,
            1480, 1010, 780, 1390, 870
        };
        
        // Test both implementations
        System.out.println("TESTING BOTH LEADERBOARD IMPLEMENTATIONS");
        System.out.println("=".repeat(50));
        System.out.println("Adding 15 players, keeping only top 10...\n");
        
        PriorityQueueLeaderboard pqLeaderboard = new PriorityQueueLeaderboard();
        CustomLeaderboard customLeaderboard = new CustomLeaderboard();
        
        // Add all players to both
        for (int i = 0; i < names.length; i++) {
            System.out.printf("Adding: %-10s with %,6d points%n", 
                names[i], scores[i]);
            
            pqLeaderboard.addScore(names[i], scores[i]);
            customLeaderboard.addScore(names[i], scores[i]);
        }
        
        // Display results
        System.out.println("\n" + "=".repeat(50));
        pqLeaderboard.printLeaderboard();
        
        System.out.println("\n" + "=".repeat(50));
        customLeaderboard.printLeaderboard();
        
        // Explain the min-heap strategy
        System.out.println("\n" + "=".repeat(50));
        System.out.println("💡 WHY MIN-HEAP FOR TOP-K?");
        System.out.println("=".repeat(50));
        System.out.println("For top 10 leaderboard, we use MIN-heap (not max):");
        System.out.println("• Root = smallest in heap = 10th place score");
        System.out.println("• New score > root? Remove root, insert new");
        System.out.println("• New score <= root? Ignore (doesn't make top 10)");
        System.out.println("• Memory: O(K) = O(10) regardless of total players");
        System.out.println("• Per score: O(log K) = O(log 10) ≈ O(1)");
        System.out.println("\nResult: Efficient streaming top-K algorithm!");
    }
}

