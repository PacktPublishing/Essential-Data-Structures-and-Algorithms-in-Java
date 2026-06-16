/**
 * Represents a single entry (file or directory) in the directory tree.
 *
 * <p>The tree is modelled as a <em>left-child / right-sibling</em> linked structure:
 * <ul>
 *   <li>{@code child} points to the <strong>first</strong> child of a directory.</li>
 *   <li>{@code next}  points to the <strong>next sibling</strong> at the same level.</li>
 * </ul>
 *
 * <pre>
 *  root
 *   ├─ dir1  ──next──► dir2  ──next──► file1.txt
 *   │   └─child
 *   │       nested_file.txt ──next──► nested_dir
 *   │                                    └─child
 *   │                                        deep_file.txt
 *   └─ ...
 * </pre>
 */
class DirectoryEntry {

    /** Display name of this file or directory (no path separators). */
    String name;

    /** {@code true} if this entry is a directory; {@code false} for a file. */
    boolean isDirectory;

    /**
     * Link to the next sibling entry at the same level in the parent directory.
     * {@code null} if this is the last entry at its level.
     */
    DirectoryEntry next;

    /**
     * Link to the first child entry.
     * Only meaningful (non-null) when {@code isDirectory == true}.
     */
    DirectoryEntry child;

    /**
     * Constructs a new directory entry.
     *
     * @param name        the name of this file or directory
     * @param isDirectory {@code true} to create a directory node; {@code false} for a file
     */
    public DirectoryEntry(String name, boolean isDirectory) {
        this.name        = name;
        this.isDirectory = isDirectory;
    }

    /** @return {@code true} if this entry represents a directory */
    public boolean isDirectory() { return this.isDirectory; }

    /** @return the first child entry, or {@code null} if this is a file or empty directory */
    public DirectoryEntry getChild() { return this.child; }

    /** @return the next sibling entry at the same level, or {@code null} if none */
    public DirectoryEntry getNext() { return this.next; }

    /** @param child the new first child of this directory entry */
    public void setChild(DirectoryEntry child) { this.child = child; }

    /** @param next the new next sibling of this entry */
    public void setNext(DirectoryEntry next) { this.next = next; }
}

/**
 * Manages a virtual directory tree stored as a left-child / right-sibling linked list.
 *
 * <h2>Supported Operations</h2>
 * <ul>
 *   <li>Add entries (files or directories) at the root level or under any existing directory.</li>
 *   <li>Look up an entry by its full path (e.g. {@code "dir1/nested_dir"}).</li>
 *   <li>Check whether an entry with a given <em>name</em> exists anywhere in the tree.</li>
 *   <li>Pretty-print the entire tree with indentation.</li>
 * </ul>
 *
 * <h2>Known Constraints</h2>
 * <ul>
 *   <li>Entry names must not contain the path separator {@code '/'}.</li>
 *   <li>No duplicate-name checking is performed at insertion time.</li>
 * </ul>
 */
public class DirectoryManagerNestedVersion {

    /** Root of the entire directory tree (first entry at the top level). */
    private DirectoryEntry head;

    // ─────────────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Adds a new entry at the <em>root</em> level of the directory tree.
     * Convenience overload of {@link #addEntry(String, boolean, String)}.
     *
     * @param name        name of the new file or directory
     * @param isDirectory {@code true} for a directory; {@code false} for a file
     */
    public void addEntry(String name, boolean isDirectory) {
        addEntry(name, isDirectory, null);
    }

    /**
     * Adds a new entry either at the root level or as a child of the specified parent directory.
     *
     * @param name        name of the new file or directory
     * @param isDirectory {@code true} for a directory; {@code false} for a file
     * @param parentPath  slash-separated path of the parent directory (e.g. {@code "dir1/nested_dir"}),
     *                    or {@code null} to add at the root level
     */
    public void addEntry(String name, boolean isDirectory, String parentPath) {
        DirectoryEntry newNode = new DirectoryEntry(name, isDirectory);

        if (head == null) {
            // Tree is completely empty – this becomes the sole root entry
            head = newNode;
        } else if (parentPath == null) {
            // No parent specified → append at root level
            addAtRoot(newNode);
        } else {
            // Locate the parent directory and attach as its child
            addAsChild(newNode, parentPath);
        }
    }

    /**
     * Looks up an entry by its full slash-separated path.
     *
     * <p>Examples:
     * <ul>
     *   <li>{@code findEntry("dir1")}               → the root-level "dir1" node</li>
     *   <li>{@code findEntry("dir1/nested_dir")}     → "nested_dir" inside "dir1"</li>
     * </ul>
     *
     * @param path the slash-separated path to resolve (must not start with {@code '/'})
     * @return the matching {@link DirectoryEntry}, or {@code null} if not found
     */
    public DirectoryEntry findEntry(String path) {
        if (path == null || path.isEmpty()) return null;

        String[]       components = path.split("/");
        DirectoryEntry current    = head;   // Start scanning from the root-level sibling chain

        for (int i = 0; i < components.length; i++) {
            String component = components[i];
            if (current == null) return null;   // Ran out of entries before exhausting the path

            // Linear-scan across siblings to find the matching name
            DirectoryEntry found = null;
            for (DirectoryEntry temp = current; temp != null; temp = temp.next) {
                if (temp.name.equals(component)) {
                    found = temp;
                    break;
                }
            }

            if (found == null) return null;                    // Component not found at this level

            if (i == components.length - 1) return found;      // Reached the target component

            // Descend one level: the next component must be a child of `found`
            current = found.child;
        }

        return null;   // Unreachable in practice; guards against an empty components array
    }

    /**
     * Checks whether <em>any</em> entry with the given name exists anywhere in the tree.
     *
     * @param name the bare entry name to search for (not a path)
     * @return {@code true} if at least one entry with that name exists; {@code false} otherwise
     */
    public boolean isEntryExist(String name) {
        return containsEntry(head, name);
    }

    /**
     * Prints the entire directory tree to standard output, indented to reflect depth.
     * Directory entries are displayed with a trailing {@code '/'}.
     */
    public void printDirectoryTree() {
        printDirectoryTree(head, 0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Appends {@code newNode} at the end of the root-level sibling chain.
     *
     * <p>Traverses the {@code next} links from {@code head} until it reaches the last node,
     * then attaches {@code newNode} there.  This preserves insertion order at the root level.
     *
     * @param newNode the entry to append
     */
    private void addAtRoot(DirectoryEntry newNode) {
        // Walk to the last root-level sibling
        DirectoryEntry prev    = null;
        DirectoryEntry current = head;
        while (current != null) {
            prev    = current;
            current = current.next;
        }

        if (prev == null) {
            head = newNode;   // Handles the edge-case where head somehow became null
        } else {
            prev.next = newNode;   // Append after the last root sibling
        }
    }

    /**
     * Locates the directory at {@code parentPath} and attaches {@code newNode} as its
     * <em>last</em> child, preserving insertion order.
     *
     * @param newNode    the entry to attach
     * @param parentPath slash-separated path of the target parent directory
     */
    private void addAsChild(DirectoryEntry newNode, String parentPath) {
        DirectoryEntry parent = findEntry(parentPath);

        if (parent == null || !parent.isDirectory) {
            System.out.println("Parent directory not found or not a directory: " + parentPath);
            return;
        }

        if (parent.child == null) {
            // Directory is empty – this becomes its only child
            parent.child = newNode;
        } else {
            DirectoryEntry cursor = parent.child;
            while (cursor.next != null) {
                cursor = cursor.next;
            }
            cursor.next = newNode;
        }
    }

    /**
     * Recursively searches the subtree rooted at {@code entry} for any node whose
     * {@link DirectoryEntry#name} equals {@code name}.
     *
     * <p>The traversal follows both the {@code child} link (depth) and the {@code next} link
     * (breadth within the same level), effectively performing a pre-order DFS.
     *
     * @param entry the current node to inspect (may be {@code null})
     * @param name  the target name
     * @return {@code true} if a matching entry is found in this subtree
     */
    private boolean containsEntry(DirectoryEntry entry, String name) {
        if (entry == null) return false;

        // Check the current node
        if (entry.name.equals(name)) return true;

        // Recurse into children first (depth), then siblings (breadth)
        return containsEntry(entry.child, name)
            || containsEntry(entry.next,  name);
    }

    /**
     * Recursively prints the subtree rooted at {@code entry} with depth-based indentation.
     * Each level of depth adds two spaces of leading whitespace.
     *
     * @param entry the current node to print (may be {@code null})
     * @param depth the current indentation level (0 = root)
     */
    private void printDirectoryTree(DirectoryEntry entry, int depth) {
        if (entry == null) return;

        // Print this entry with appropriate indentation
        String indent = "  ".repeat(depth);
        System.out.println(indent + entry.name + (entry.isDirectory ? "/" : ""));

        // Recurse into children (one level deeper), then siblings (same level)
        printDirectoryTree(entry.child, depth + 1);
        printDirectoryTree(entry.next,  depth);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Demo
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        DirectoryManagerNestedVersion manager = new DirectoryManagerNestedVersion();

        // ── Root-level entries ──────────────────────────────────────────────
        manager.addEntry("dir1",     true);    // directory
        manager.addEntry("dir2",     true);    // directory
        manager.addEntry("file1.txt",false);   // file

        // ── Nested entries ──────────────────────────────────────────────────
        manager.addEntry("nested_dir",      true,  "dir1");             // dir inside dir1
        manager.addEntry("nested_file.txt", false, "dir1");             // file inside dir1
        manager.addEntry("deep_file.txt",   false, "dir1/nested_dir");  // file inside nested_dir

        // ── Existence checks ────────────────────────────────────────────────
        System.out.println("dir1 exists:       " + manager.isEntryExist("dir1"));        // true
        System.out.println("nested_dir exists: " + manager.isEntryExist("nested_dir"));  // true
        System.out.println("missing.txt exists:" + manager.isEntryExist("missing.txt")); // false

        // ── Directory tree ──────────────────────────────────────────────────
        System.out.println("\nDirectory Tree:");
        manager.printDirectoryTree();
    }
}