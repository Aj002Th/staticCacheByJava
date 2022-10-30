public class Node<K, V> {
    K k;
    V v;
    Node<K, V> next;
    Node<K, V> prev;

    public Node() {
        next = prev = null;
    }

    public Node(K k, V v) {
        this.k = k;
        this.v = v;
        next = prev = null;
    }
}
