package com.lzl.rpc.consumer.leetcode;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    static class DLinkNode {
        int key;
        int value;
        DLinkNode pre;
        DLinkNode next;
        public DLinkNode() {}
        public DLinkNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Map<Integer, DLinkNode> cache = new HashMap<>();
    private int size;
    private final int capacity;
    private final DLinkNode head;
    private final DLinkNode tail;

    public LRUCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        head = new DLinkNode();
        tail = new DLinkNode();
        head.next = tail;
        tail.pre = head;
    }

    public int get(int key) {
        DLinkNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        moveToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        DLinkNode node = cache.get(key);
        if (node == null) {
            node = new DLinkNode(key, value);
            size++;
            if (size > capacity) {
                DLinkNode tail = removeTail();
                cache.remove(tail.key);
                addToHead(node);
                cache.put(key, node);
                size--;
            } else {
                cache.put(key, node);
                addToHead(node);
            }
        } else {
            node.value = value;
            moveToHead(node);
        }
    }

    private void removeNode(DLinkNode node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }

    private void addToHead(DLinkNode node) {
        node.next = head.next.pre;
        head.next.pre = node;
        head.next = node;
        node.pre = head;
    }

    private void moveToHead(DLinkNode node) {
        removeNode(node);
        addToHead(node);
    }

    private DLinkNode removeTail() {
        DLinkNode pre = tail.pre;
        removeNode(pre);
        return pre;
    }
}
