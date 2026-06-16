/**
 * Finds middle node of linked list using fast and slow pointers
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 */
public class MiddleNodeFinder {
    // Definition for singly-linked list node
    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * Finds middle node of linked list
     * @param head head of linked list
     * @return middle node of list
     */
    public ListNode middleNode(ListNode head) {
        // Initialize both pointers to start at head
        ListNode slow = head;
        ListNode fast = head;
        
        // Move fast pointer twice as fast as slow pointer
        // When fast reaches end, slow will be at middle
        while (fast != null && fast.next != null) {
            slow = slow.next;        // Move slow pointer one step
            fast = fast.next.next;   // Move fast pointer two steps
        }
        
        // When fast reaches end, slow points to middle node
        return slow;
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
        MiddleNodeFinder solution = new MiddleNodeFinder();
        
        // Test case 1
        ListNode head1 = solution.createList(new int[]{1, 2, 3, 4, 5});
        System.out.print("Test 1 - List: ");
        solution.printList(head1);
        ListNode middle1 = solution.middleNode(head1);
        System.out.println("Test 1 - Middle node value: " + middle1.val);
        
        // Test case 2
        ListNode head2 = solution.createList(new int[]{1, 2, 3, 4, 5, 6});
        System.out.print("Test 2 - List: ");
        solution.printList(head2);
        ListNode middle2 = solution.middleNode(head2);
        System.out.println("Test 2 - Middle node value: " + middle2.val);
    }
}

