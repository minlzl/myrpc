package com.lzl.rpc.consumer.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class MinStack {

    Deque<Integer> data;

    Deque<Integer> min;

    public MinStack() {
        data = new ArrayDeque<>();
        min = new ArrayDeque<>();
    }

    public void push(int val) {
        data.push(val);
        if (min.isEmpty() || min.peek() > val) {
            min.push(val);
        } else {
            min.push(min.peek());
        }
    }

    public void pop() {
        data.pop();
        min.pop();
    }

    public int top() {
        return data.peek();
    }

    public int getMin() {
        return min.peek();
    }
}
