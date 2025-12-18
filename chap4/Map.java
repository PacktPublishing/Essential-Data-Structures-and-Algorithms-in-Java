import java.util.ArrayList;
import java.util.List;

public class Map<K, V> implements MapADT<K, V> {
    
    private static class Entry<K, V> {
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private List<Entry<K, V>> entries;
    
    public Map() {
        entries = new ArrayList<>();
    }
    
    @Override
    public V put(K key, V value) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }
        entries.add(new Entry<>(key, value));
        return null;
    }
    
    @Override
    public V get(Object key) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }
    
    @Override
    public V remove(Object key) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).key.equals(key)) {
                return entries.remove(i).value;
            }
        }
        return null;
    }
    
    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }
    
    @Override
    public int size() {
        return entries.size();
    }
}