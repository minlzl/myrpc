package com.lzl.rpc.consumer.leetcode;

import java.util.*;

public class RandomizedSet {

    List<Integer> nums;
    Map<Integer, Integer> indices;
    Random random;

    public RandomizedSet() {
        nums = new ArrayList<>();
        indices = new HashMap<>();
        random = new Random();
    }

    public boolean insert(int val) {
        if (indices.containsKey(val)) {
            return false;
        }
        indices.put(val, nums.size());
        nums.add(val);
        return true;
    }

    public boolean remove(int val) {
        if (!indices.containsKey(val)) {
            return false;
        }
        Integer index = indices.get(val);
        nums.set(index, nums.get(nums.size() - 1));
        indices.put(nums.get(index), index);
        indices.remove(val);
        nums.remove(nums.size() - 1);
        return true;
    }

    public int getRandom() {
        return nums.get(random.nextInt(nums.size()));
    }

}
