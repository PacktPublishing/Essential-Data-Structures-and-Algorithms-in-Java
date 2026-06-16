class DirectoryEntry {
    String name;
    boolean isDirectory;
    DirectoryEntry next;

    public DirectoryEntry(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }
    public boolean isDirectory() {
        return this.isDirectory;
    }
}

public class DirectoryManager {
    private DirectoryEntry head; // Head of the linked list

    public void addEntry(String name, boolean isDirectory) {
        DirectoryEntry newNode = new DirectoryEntry(name, isDirectory);
        if (head == null) {
            head = newNode;
        } else {
            DirectoryEntry current = head;
            while (current.next != null && !current.next.isDirectory()) {
                // Traverse the list until you find the end of list or 
                // a directory node.
                current = current.next;
            }
            if (current.next == null) {
                // If we're at the end of the list (no directory found), 
                // append the new node.
                current.next = newNode;
            } else {
                // If a directory was found ahead, then insert the new 
                // node before that directory. This keeps directories 
                // grouped together at the end of list. This also 
                // keeps all the files in their insertion order.
                newNode.next = current.next;
                current.next = newNode;
            }
        }
    }

    public boolean isEntryExist(String name) {
        DirectoryEntry current = head;
        while (current != null) {
            if (current.name.equals(name)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public static void main(String[] args) {
        DirectoryManager manager = new DirectoryManager();
        manager.addEntry("dir", true); // Create directory "dir"
        manager.addEntry("file.txt", false); // Add file "file.txt" to "dir"
        System.out.println(manager.isEntryExist("dir")); // Output: true
    }
}
