## 题目

运用你所掌握的数据结构，设计和实现一个 LRU (最近最少使用) 缓存机制。它应该支持以下操作： 获取数据 get 和 写入数据 put 。 

获取数据 get(key) - 如果密钥 (key) 存在于缓存中，则获取密钥的值（总是正数），否则返回 -1。 
写入数据 put(key, value) - 如果密钥已经存在，则变更其数据值；如果密钥不存在，则插入该组「密钥/数据值」。当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值，从而为新的数据值留出空间。 



 进阶: 

 你是否可以在 O(1) 时间复杂度内完成这两种操作？ 



 示例: 

```
 LRUCache cache = new LRUCache( 2 /* 缓存容量 */ );

cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // 返回  1
cache.put(3, 3);    // 该操作会使得密钥 2 作废
cache.get(2);       // 返回 -1 (未找到)
cache.put(4, 4);    // 该操作会使得密钥 1 作废
cache.get(1);       // 返回 -1 (未找到)
cache.get(3);       // 返回  3
cache.get(4);       // 返回  4
```

## 解题思路

1. 借助HashMap，快速定位缓存key值
2. 借助双向链表 维护一份缓存key的优先级信息（链表头结点存储最新数据，尾结点就是最老的数据，如超过缓存容量，则会删除最老的数据）

为什么要用双向链表？用单向链表不行吗？
用双向链表的目的是为了删除添加节点时时间复杂度降到O(1)，效率更高

```java
class LRUCache {

        /**
        * 
*/
        private Map<Integer, Node> cache;
        private int capacity;
        private Node head = new Node();
        private Node tail = new Node();
        private int size;

        public LRUCache(int capacity) {
           this.capacity = capacity;
           this.cache = new HashMap<>();
           head.next = tail;
           tail.pre = head;
        }

        public int get(int key) {
            Node node = cache.get(key);
            if (node == null) {
                return -1;
            }
            delNode(node);
            addNodeToHead(node);
            return node.value;
        }

        public void put(int key, int value) {
            Node newNode = new Node(key, value);
            Node nowNode = cache.get(key);
            if (nowNode == null) {
                cache.put(key, newNode);
                addNodeToHead(newNode);
                size++;
                if (size > capacity) {
                    Node tailNode = tail.pre;
                    delNode(tailNode);
                    cache.remove(tailNode.key);
                    size--;
                }
            } else {
                cache.put(key, newNode);
                delNode(nowNode);
                addNodeToHead(newNode);
            }
        }

        private void addNodeToHead(Node node) {
            Node next = head.next;
            head.next = node;
            node.pre = head;
            node.next = next;
            next.pre = node;

        }

        private void delNode(Node node) {
            Node pre = node.pre;
            Node next = node.next;
            pre.next = next;
            next.pre = pre;
            node = null;
        }


        class Node {
            private int key;
            private int value;
            private Node next;
            private Node pre;

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }

            public Node() {

            }
        }
}
```