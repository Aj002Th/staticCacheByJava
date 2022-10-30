import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class LRUCache<K, V> {
    private DoubleList<K, V> doubleList;
    private Map<K, Node<K, V>> map;
    private final int CACHESIZE;

    public LRUCache(int size) {
        CACHESIZE = size;
        doubleList = new DoubleList<>();
        map = new HashMap<>();
    }

    public void put(K key, V value) {
        Node<K, V> node = map.get(key);

        if (node == null) {
            // 添加数据，添加到队尾
            Node<K, V> node1 = new Node<>(key, value);
            map.put(key, node1);
            doubleList.addLast(node1);
        } else {
            // 之前有数据，说明需要移动该数据在队列中的位置
            // 先删除掉旧数据
            doubleList.removeNode(node);
            node.v = value;
            // 将数据添加到队尾
            doubleList.addLast(node);
        }

        // 判断存放元素数目是否超出阈值
        if (map.size() > CACHESIZE) {
            // 超过阈值，淘汰掉队头数据即最近最少使用的数据
            Node<K, V> head = doubleList.head.next;
            doubleList.removeNode(head);
            // 同时将map中对应数据清除掉
            map.remove(head.k);
        }
    }

    public V get(K key) {
        Node<K, V> node = map.get(key);
        if (node == null) {
            throw new RuntimeException("Key不存在");
        }

        // 使用过该数据则需要将数据移动到队尾
        doubleList.removeNode(node);
        doubleList.addLast(node);
        return node.v;
    }

    // 用来展示缓存的 key 有哪些
    public Set<K> keySet() {
        return map.keySet();
    }

    // 用于测试
    public static void main(String[] args) {
        LRUCache<Integer, Integer> lru = new LRUCache<>(2);
        lru.put(1, 3);
        lru.put(2, 4);
        lru.get(1);
        lru.put(3, 5);
        System.out.println(lru.keySet());
    }
}
