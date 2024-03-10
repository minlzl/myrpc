package com.lzl.rpc.consumer.leetcode;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MedianFinder {

    PriorityQueue<Integer> queueMin;
    PriorityQueue<Integer> queueMax;

    public MedianFinder() {
        queueMin = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        queueMax = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
    }

    public void addNum(int num) {
        if (queueMin.isEmpty() || num <= queueMin.peek()) {
            queueMin.offer(num);
            if (queueMax.size() + 1 < queueMin.size()) {
                queueMax.offer(queueMin.poll());
            }
        } else {
            queueMax.offer(num);
            if (queueMax.size() > queueMin.size()) {
                queueMin.offer(queueMax.poll());
            }
        }
    }

    public double findMedian() {
        if (queueMin.size() > queueMax.size()) {
            return queueMin.peek();
        }
        return (queueMin.peek() + queueMax.peek()) * 1.0 / 2;
    }
}
