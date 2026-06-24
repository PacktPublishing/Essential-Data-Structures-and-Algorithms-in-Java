# Merge-Sort Recurrence Derivation and Analysis

This document provides a formal breakdown of the Merge-Sort algorithm's execution time using recurrence relations and solves it using the Master Theorem cookbook framework.

---

## 1. Derivation of the Recurrence Relation

Merge-Sort uses a **Divide-and-Conquer** strategy. The total computational time $T(n)$ required to sort an array of size $n$ is broken down into three operational phases:

1. **Divide:** Finding the midpoint of the array to split it into two equal halves. This takes constant time:
   $$\mathcal{O}(1)$$
2. **Conquer:** Recursively solving the two subproblems. Each subproblem handles exactly half the size of the original array ($n/2$). This requires:
   $$2 \cdot T\left(\frac{n}{2}\right)$$
3. **Combine (Merge):** Merging the two sorted halves back into a single sorted array. Merging requires scanning through all $n$ elements linearly. This requires:
   $$\mathcal{O}(n)$$

### The Recurrence Formula
Combining these phases yields the standard recurrence relation for Merge-Sort:

$$T(n) = 2T\left(\frac{n}{2}\right) + cn$$

*(Where $c$ is a positive constant representing the per-element cost of the merging phase, and $T(1) = \mathcal{O}(1)$ is the base case).*

---

## 2. Solving with the Master Theorem

The Master Theorem provides a cookbook solution for recurrence relations of the generic form:
$$T(n) = aT\left(\frac{n}{b}\right) + f(n)$$

### Step 1: Extract the Parameters
From our Merge-Sort recurrence $T(n) = 2T(n/2) + cn$, we extract:
* **$a = 2$**: The number of recursive subproblems generated at each step.
* **$b = 2$**: The factor by which the subproblem size is reduced.
* **$f(n) = cn = \mathcal{O}(n^1)$**: The cost of the work done outside the recursive calls. Therefore, the exponent of the driving function is **$d = 1$**.

### Step 2: Calculate the Critical Value
We compute and compare $\log_b(a)$ against the driving exponent $d$:
$$\log_b(a) = \log_2(2) = 1$$

### Step 3: Apply the Master Theorem Case Criteria
The theorem evaluates three standard cases based on the relationship between $\log_b(a)$ and $d$:
1. **Case 1:** If $\log_b(a) > d$, then $T(n) = \mathcal{O}(n^{\log_b a})$.
2. **Case 2: If $\log_b(a) = d$, then $T(n) = \mathcal{O}(n^d \log n)$.**
3. **Case 3:** If $\log_b(a) < d$, then $T(n) = \mathcal{O}(f(n))$.

Since our critical value $\log_2(2) = 1$ is **exactly equal** to our driving exponent $d = 1$, the recurrence falls precisely into **Case 2**.

### Final Complexity Conclusion
Substituting our specific parameters into the Case 2 formula yields:
$$T(n) = \mathcal{O}(n^1 \log n) = \mathcal{O}(n \log n)$$

---

## 3. Step-by-Step Numerical Verification ($n = 8$)

Using an example array of size $n = 8$ (e.g., `[3, 1, 4, 1, 5, 9, 2, 6]`) and assuming a base merging constant $c = 1$:

* **Level 0 ($n=8$):** $T(8) = 2T(4) + 8$
* **Level 1 ($n=4$):** $T(4) = 2T(2) + 4$
* **Level 2 ($n=2$):** $T(2) = 2T(1) + 2$
* **Level 3 ($n=1$):** $T(1) = 1$ *(Base case constraint)*

### Bottom-Up Mathematical Substitution
1. $T(2) = 2(1) + 2 = \mathbf{4}$
2. $T(4) = 2(4) + 4 = \mathbf{12}$
3. $T(8) = 2(12) + 8 = \mathbf{32}$

### Visualizing the Work Distribution Matrix
This breakdown perfectly demonstrates the total work scaling across the tree layers. The recursion tree has $\log_2(8) = 3$ operational splitting levels doing exactly 8 units of merging work at each level ($8 + 8 + 8 = 24$), plus the 8 individual base cases ($T(1) = 1$) evaluating at the very bottom layer ($24 + 8 = 32$).
