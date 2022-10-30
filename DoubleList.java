class DoubleList<K, V> {
    Node<K, V> head;
    Node<K, V> tail;

    public DoubleList() {
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.prev = head;
    }

    // 将数据添加到队尾
    public void addLast(Node<K, V> node) {
        node.next = tail;
        node.prev = tail.prev;
        tail.prev.next = node;
        tail.prev = node;
    }

    // 删除队列中的数据
    public void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}
