public class ArrayADT<T> {
    private T[] arr;
    private int n; // current number of elements
    
    @SuppressWarnings("unchecked")
    public ArrayADT(int initialCapacity) {
        this.arr = (T[]) new Object[initialCapacity];
        this.n = 0;
    }
    
    /**
     * Retrieves the element at index i
     * Time efficiency: O(1)
     */
    public T get(int i) {
        if (i < 0 || i >= n) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + n);
        }
        return arr[i];
    }
    
    /**
     * Returns the total number of elements in the array
     * Time efficiency: O(1)
     */
    public int length() {
        return n;
    }
    
    /**
     * Returns the current capacity of the underlying array
     */
    public int capacity() {
        return arr.length;
    }
    
    /**
     * Replaces the element at index i with the specified value e
     * Time efficiency: O(1)
     */
    public void set(int i, T e) {
        if (i < 0 || i >= n) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + n);
        }
        arr[i] = e;
    }
    
    /**
     * Inserts a new element e at position i, shifting subsequent elements to the right
     * Time efficiency: O(n) where n is the number of elements
     */
    public void add(int i, T e) {
        if (i < 0 || i > n) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + n);
        }
        
        // Resize if necessary
        if (n >= arr.length) {
            resize();
        }
        
        // Shift elements to the right
        for (int j = n; j > i; j--) {
            arr[j] = arr[j - 1];
        }
        
        // Insert the new element
        arr[i] = e;
        n++;
    }
    
    /**
     * Removes the element at index i from the array, shifting subsequent elements to the left
     * Time efficiency: O(n) where n is the number of elements
     */
    public T delete(int i) {
        if (i < 0 || i >= n) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + n);
        }
        
        T deletedElement = arr[i];
        
        // Shift elements to the left
        for (int j = i; j < n - 1; j++) {
            arr[j] = arr[j + 1];
        }
        
        n--;
        return deletedElement;
    }
    
    /**
     * Resizes the underlying array when it's full
     */
    private void resize() {
        int newCapacity = arr.length * 2;
        T[] newArr = (T[]) new Object[newCapacity];
        
        // Copy existing elements
        for (int i = 0; i < n; i++) {
            newArr[i] = arr[i];
        }
        
        arr = newArr;
    }
    
    public static void main(String[] args) {
        // Create an ArrayADT with initial capacity of 4
        ArrayADT<Integer> array = new ArrayADT<>(4);
        
        System.out.println("=== Array ADT CRUD Operations Demo ===");
        
        // CREATE - Add elements
        System.out.println("\n1. CREATE Operations:");
        System.out.println("Initial array: " + array);
        System.out.println("Length: " + array.length());
        System.out.println("Capacity: " + array.capacity());
        
        array.add(0, 10);
        array.add(1, 20);
        array.add(2, 30);
        array.add(1, 15); // Insert 15 at index 1
        System.out.println("After adding elements: " + array);
        System.out.println("Length: " + array.length());
        
        // READ - Access elements
        System.out.println("\n2. READ Operations:");
        System.out.println("Element at index 0: " + array.get(0));
        System.out.println("Element at index 2: " + array.get(2));
        System.out.println("Element at index 3: " + array.get(3));
        System.out.println("Length: " + array.length());
        
        // UPDATE - Modify elements
        System.out.println("\n3. UPDATE Operations:");
        System.out.println("Before update: " + array);
        array.set(1, 25);
        System.out.println("After setting index 1 to 25: " + array);
        array.set(3, 35);
        System.out.println("After setting index 3 to 35: " + array);
        
        // DELETE - Remove elements
        System.out.println("\n4. DELETE Operations:");
        System.out.println("Before deletion: " + array);
        Integer deleted = array.delete(1);
        System.out.println("Deleted element: " + deleted);
        System.out.println("After deleting index 1: " + array);
        
        array.delete(0);
        System.out.println("After deleting index 0: " + array);
        
        // Additional demonstration with string elements
        System.out.println("\n5. String Elements Demo:");
        ArrayADT<String> stringArray = new ArrayADT<>(3);
        stringArray.add(0, "Hello");
        stringArray.add(1, "World");
        stringArray.add(2, "Java");
        System.out.println("String array: " + stringArray);
        
        stringArray.set(1, "Universe");
        System.out.println("After updating index 1: " + stringArray);
        
        stringArray.add(1, "Beautiful");
        System.out.println("After inserting 'Beautiful' at index 1: " + stringArray);
        
        String removed = stringArray.delete(2);
        System.out.println("Deleted '" + removed + "': " + stringArray);
        
        System.out.println("\n=== Demo Complete ===");
    }
}
