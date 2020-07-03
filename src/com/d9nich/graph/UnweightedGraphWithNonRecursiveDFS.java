package com.d9nich.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnweightedGraphWithNonRecursiveDFS<V> extends UnweightedGraph<V> {
    public UnweightedGraphWithNonRecursiveDFS() {
    }

    public UnweightedGraphWithNonRecursiveDFS(V[] vertices, int[][] edges) {
        super(vertices, edges);
    }

    public UnweightedGraphWithNonRecursiveDFS(List<V> vertices, List<Edge> edges) {
        super(vertices, edges);
    }

    @Override
    public SearchTree dfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        // Initialize parent[i] to −1
        Arrays.fill(parent, -1);
        // Mark visited vertices
        boolean[] isVisited = new boolean[vertices.size()];
        // Recursively search

        final ArrayList<Integer> stack = new ArrayList<>();
        stack.add(v);
        while (!stack.isEmpty()) {
            final Integer index = stack.get(stack.size() - 1);
            if (!isVisited[index]) {
                isVisited[index] = true;
                searchOrder.add(index);
            }
            for (Edge edge : neighbors.get(index)) {
                final int neighbour = edge.v;
                if (!isVisited[neighbour]) {
                    parent[neighbour] = index;
                    stack.add(neighbour);
                    break;
                }
            }

            if (stack.get(stack.size() - 1).equals(index))
                stack.remove(stack.size() - 1);
        }

        // Return a search tree
        return new SearchTree(v, parent, searchOrder);
    }
}
