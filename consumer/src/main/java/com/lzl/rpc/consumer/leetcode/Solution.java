package com.lzl.rpc.consumer.leetcode;


import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

public class Solution {


    public int countPaths(int n, int[][] roads) {
        long[][] g = new long[n][n];
        for (long[] row : g) {
            Arrays.fill(row, Long.MAX_VALUE / 2);
        }
        for (int[] r : roads) {
            int x = r[0], y = r[1], d = r[2];
            g[x][y] = d;
            g[y][x] = d;
        }
        long[] dis = new long[n];
        Arrays.fill(dis, 1, n, Long.MAX_VALUE / 2);
        int[] f = new int[n];
        f[0] = 1;
        boolean[] done = new boolean[n];
        while (true) {
            int x = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && (x < 0 || dis[i] < dis[x])) {
                    x = i;
                }
            }
            if (x == n - 1) {
                return f[n - 1];
            }
            done[x] = true;
            for (int y = 0; y < n; y++) {
                long newDis = dis[x] + g[x][y];
                if (newDis < dis[y]) {
                    dis[y] = newDis;
                    f[y] = f[x];
                } else if (newDis == dis[y]) {
                    f[y] = (f[y] + f[x]) % (int) (1e9 +7);
                }
            }
        }
    }
    public int[] divisibilityArray(String word, int m) {
        int[] res = new int[word.length()];
        long cur = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            cur =  (cur * 10 + (c - '0')) % m;
            res[i] = (cur == 0) ? 1 : 0;
        }
        return res;
    }
    public int minimumPossibleSum(int n, int target) {
        final int MOD = (int)(1e9) + 7;
        int m = target / 2;
        if (n <= m) {
            return (int) ((long) (1 + n) * n / 2 % MOD);
        }
        return (int)(((long) (1 + m) * m / 2 + ((long) target + target + ( n - m) - 1) * (n - m) / 2) % MOD);
    }



































    static class UnionFind {
        int[] parent;
        double[] weight;

        public UnionFind(int n) {
            this.parent = new int[n];
            this.weight = new double[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                weight[i] = 1.0;
            }
        }

        public void union(int x, int y, double value) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return;
            }
            parent[rootX] = rootY;
            weight[rootX] = weight[y] * value / weight[x];
        }

        public int find(int x) {
            if (x != parent[x]) {
                int origin = parent[x];
                parent[x] = find(parent[x]);
                weight[x] *= weight[origin];
            }
            return parent[x];
        }

        public double isConnected(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return weight[x] / weight[y];
            } else {
                return -1.0;
            }
        }
    }

    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) {
            this.val = val;
        }
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(){}
        TreeNode(int value) {
            this.val = value;
        }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}
