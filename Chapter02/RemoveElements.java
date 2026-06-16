/**
 * Removes all nodes with specific value from linked list
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 */
public class RemoveElements {
    // Definition for singly-linked list node
    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * Removes all nodes with given value from linked list
     * @param head head of linked list
     * @param val value to remove
     * @return new head of modified linked list
     */
    public ListNode removeElements(ListNode head, int val) {
        // Create dummy node to handle edge case where head needs to be removed
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // Current pointer to traverse the list
        ListNode current = dummy;
        
        // Traverse the list until we reach the end
        while (current.next != null) {
            // If next node has the value to remove
            if (current.next.val == val) {
                // Skip the node with target value by linking current to next's next
                current.next = current.next.next;
            } else {
                // If no removal needed, move current pointer forward
                current = current.next;
            }
        }
        
        // Return the new head (skip dummy node)
        return dummy.next;
    }
    
    // Helper method to create linked list from array
    public ListNode createList(int[] arr) {
        if (arr.length == 0) return null;
        ListNode head = new ListNode(arr[0]);
        ListNode current = head;
        for (int i = 1; i < arr.length; i++) {
            current.next = new ListNode(arr[i]);
            current = current.next;
        }
        return head;
    }
    
    // Helper method to print linked list
    public void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val);
            if (head.next != null) System.out.print(" -> ");
            head = head.next;
        }
        System.out.println();
    }
    
    // Example usage
    public static void main(String[] args) {
        RemoveElements solution = new RemoveElements();
        
        // Test case 1
        ListNode head1 = solution.createList(new int[]{1, 2, 6, 3, 4, 5, 6});
        System.out.print("Test 1 - Before removal: ");
        solution.printList(head1);
        ListNode result1 = solution.removeElements(head1, 6);
        System.out.print("Test 1 - After removal: ");
        solution.printList(result1);
        
        // Test case 2
        ListNode head2 = solution.createList(new int[]{});
        System.out.print("Test 2 - Before removal: ");
        solution.printList(head2);
        ListNode result2 = solution.removeElements(head2, 1);
        System.out.print("Test 2 - After removal: ");
        solution.printList(result2);
    }
}

