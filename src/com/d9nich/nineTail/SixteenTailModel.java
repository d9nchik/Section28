package com.d9nich.nineTail;

import com.d9nich.graph.Edge;
import com.d9nich.graph.UnweightedGraph;

import java.util.ArrayList;
import java.util.List;

public class SixteenTailModel {
    public final static int NUMBER_OF_NODES = 65_536;
    protected UnweightedGraph<Integer>.SearchTree tree;

    /**
     * Construct a model
     */
    public SixteenTailModel() {
        // Create edges
        List<Edge> edges = getEdges();

        // Create a graph
        UnweightedGraph<Integer> graph = new UnweightedGraph<>();
        for (int i = 0; i < NUMBER_OF_NODES; i++)
            graph.addVertex(i);
        edges.forEach(graph::addEdge);
        // Obtain a BSF tree rooted at the target node
        tree = graph.bfs(NUMBER_OF_NODES - 1);
    }

    public static int getFlippedNode(char[] node, int position) {
        int row = position / 4;
        int column = position % 4;
        flipACell(node, row, column);
        flipACell(node, row - 1, column);
        flipACell(node, row + 1, column);
        flipACell(node, row, column - 1);
        flipACell(node, row, column + 1);

        return getIndex(node);
    }

    public static void flipACell(char[] node, int row, int column) {
        if (row >= 0 && row <= 3 && column >= 0 && column <= 3) {
            // Within the boundary
            if (node[row * 4 + column] == 'H')
                node[row * 4 + column] = 'T'; // Flip from H to T
            else
                node[row * 4 + column] = 'H'; // Flip from T to H
        }
    }

    public static int getIndex(char[] node) {
        int result = 0;
        for (int i = 0; i < 16; i++)
            if (node[i] == 'T')
                result = result * 2 + 1;
            else
                result *= 2;
        return result;
    }

    public static char[] getNode(int index) {
        char[] result = new char[16];
        for (int i = 0; i < 16; i++) {
            if (index % 2 == 0)
                result[15 - i] = 'H';
            else
                result[15 - i] = 'T';
            index = index / 2;
        }
        return result;
    }

    public static void printNode(char[] node) {
        for (int i = 0; i < 16; i++)
            if (i % 4 != 3)
                System.out.print(node[i]);
            else
                System.out.println(node[i]);
        System.out.println();
    }

    /**
     * Create all edges for the graph
     */
    private List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>(); // Store edges
        for (int u = 0; u < NUMBER_OF_NODES; u++) {
            for (int k = 0; k < 16; k++) {
                char[] node = getNode(u); // Get the node for vertex u
                if (node[k] == 'H') {
                    int v = getFlippedNode(node, k);
                    // Add edge (v, u) for a legal move from node u to node v
                    edges.add(new Edge(v, u));
                }
            }
        }
        return edges;
    }

    public List<Integer> getShortestPath(int nodeIndex) {
        return tree.getPath(nodeIndex);
    }

}
