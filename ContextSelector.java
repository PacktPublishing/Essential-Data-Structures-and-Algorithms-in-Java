import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContextSelector {

    public record Candidate(
        String chunkId,
        double relevanceScore, // higher = more relevant
        int tokenCount         // size of this chunk in tokens
    ) {}

    /**
     * Select up to k candidates, ranked by relevanceScore descending
     * (tie-break: smaller tokenCount first), whose combined tokenCount
     * does not exceed tokenBudget. Returned in ranked order.
     */
    public static List<Candidate> selectContext(
            List<Candidate> candidates, int k, int tokenBudget) {

        // --- Guard clauses: nothing can be selected. ---
        // Covers null/empty input plus the degenerate caps (k <= 0, budget <= 0).
        // Handling these up front keeps the main path free of edge-case checks.
        if (candidates == null || candidates.isEmpty() || k <= 0 || tokenBudget <= 0) {
            return new ArrayList<>();
        }

        // --- Step 1: Rank. ---
        // Copy first so the caller's list is never mutated.
        // This O(N log N) sort is the heart of the problem: the re-ranker's
        // scores decide inclusion, so we must order by them before selecting.
        List<Candidate> ranked = new ArrayList<>(candidates);
        ranked.sort(
            // Primary key: relevanceScore DESCENDING (reverse natural order).
            Comparator.comparingDouble(Candidate::relevanceScore).reversed()
                // Tie-break: on equal score, smaller tokenCount first.
                // tokenCount is compared ascending (not reversed), so the
                // smaller chunk wins a score tie.
                .thenComparingInt(Candidate::tokenCount)
        );

        // --- Step 2: Greedy selection under the two hard caps. ---
        // Walk most-relevant first. Include a chunk only when BOTH caps allow it:
        //   (a) fewer than k chunks chosen so far, AND
        //   (b) it still fits the remaining token budget.
        // A chunk that doesn't fit is SKIPPED, not a stop signal — a smaller,
        // less-relevant chunk later may still fit.
        List<Candidate> selected = new ArrayList<>(Math.min(k, ranked.size()));
        int usedTokens = 0;

        for (Candidate c : ranked) {
            if (selected.size() == k) {
                break; // k-cap reached; nothing further can be added.
            }
            if (usedTokens + c.tokenCount() <= tokenBudget) {
                selected.add(c);
                usedTokens += c.tokenCount();
            }
            // else: doesn't fit; skip and keep scanning for a smaller chunk.
        }

        // Already in ranked order because we iterated `ranked` in order.
        return selected;
    }

    public static void main(String[] args) {
        // Tie-break + room → [c, b]: b & c tie at 0.91, smaller (c) first.
        List<Candidate> t1 = List.of(
            new Candidate("a", 0.42, 300),
            new Candidate("b", 0.91, 500),
            new Candidate("c", 0.91, 200),
            new Candidate("d", 0.88, 400)
        );
        System.out.println(selectContext(t1, 2, 1000)); // c, b

        // Budget forces a skip → [a, c]: take a (800), b (500) won't fit,
        // c (150) fits (800+150=950).
        List<Candidate> t2 = List.of(
            new Candidate("a", 0.95, 800),
            new Candidate("b", 0.90, 500),
            new Candidate("c", 0.80, 150)
        );
        System.out.println(selectContext(t2, 3, 1000)); // a, c
    }
}
