/**
 * Merges two sorted linked lists into one sorted list
 * Time Complexity: O(m + n)
 * Space Complexity: O(1)
 */
public class MergeSortedLists {
    // Definition for singly-linked list node
    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * Merges two sorted linked lists
     * @param list1 first sorted linked list
     * @param list2 second sorted linked list
     * @return merged sorted linked list
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Create a dummy node to simplify the logic and avoid edge cases
        ListNode dummy = new ListNode(0);
        // Current pointer to build the merged list
        ListNode current = dummy;
        
        // Traverse both lists simultaneously
        while (list1 != null && list2 != null) {
            // Compare values and attach the smaller node
            if (list1.val <= list2.val) {
                current.next = list1;  // Attach list1 node
                list1 = list1.next;    // Move list1 pointer forward
            } else {
                current.next = list2;  // Attach list2 node
                list2 = list2.next;    // Move list2 pointer forward
            }
            current = current.next;    // Move current pointer forward
        }
        
        // Attach remaining nodes from either list (if any)
        current.next = (list1 != null) ? list1 : list2;
        
        // Return the merged list (skip the dummy node)
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
        MergeSortedLists solution = new MergeSortedLists();
        
        // Test case 1
        ListNode list1 = solution.createList(new int[]{1, 2, 4});
        ListNode list2 = solution.createList(new int[]{1, 3, 4});
        ListNode merged1 = solution.mergeTwoLists(list1, list2);
        System.out.print("Test 1 - Merged list: ");
        solution.printList(merged1);
        
        // Test case 2
        ListNode list3 = solution.createList(new int[]{});
        ListNode list4 = solution.createList(new int[]{});
        ListNode merged2 = solution.mergeTwoLists(list3, list4);
        System.out.print("Test 2 - Merged list: ");
        solution.printList(merged2);
    }
}

