package com.lzl.rpc.consumer.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class MyQueue {

    Deque<Integer> deque1 = new ArrayDeque<>();
    Deque<Integer> deque2 = new ArrayDeque<>();

    public MyQueue() {
        deque1.clear();
        deque2.clear();
    }

    public void push(int x) {
        deque1.push(x);
    }

    public int pop() {
        if (deque2.isEmpty()) {
            reverse();
        }
        return deque2.pop();
    }

    public int peek() {
        if (deque2.isEmpty()) {
            reverse();
        }
        return deque2.peek();
    }

    public boolean empty() {
        return deque2.isEmpty() && deque1.isEmpty();
    }

    private void reverse() {
        while (!deque1.isEmpty()) {
            deque2.push(deque1.poll());
        }
    }
}
