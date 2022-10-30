import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StaticCache<K, V> {
    private LRUCache<K, V> cache;
    private Map<K, DataGetter<V>> getterMap;

    public StaticCache(Integer cacheSize) {
        cache = new LRUCache<K, V>(cacheSize);
        getterMap = new HashMap<>();
    }

    public void set(K key, DataGetter<V> getter) {
        cache.put(key, getter.getValue());
        getterMap.put(key, getter);
    }

    public V get(K key) {
        V val = null;
        try {
            val = cache.get(key);
        } catch (RuntimeException e) {
            DataGetter<V> getter = getterMap.get(key);
            if (getter == null) {
                throw e;
            }
            val = getter.getValue();
            cache.put(key, val);
        }
        return val;
    }

    public Set<K> keySet() {
        return cache.keySet();
    }
}