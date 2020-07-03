package com.d9nich.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @param <V> marks of node
 * @author d9nich
 */
public interface Graph<V> {
    /**
     * Return the number of vertices in the graph
     */
    int getSize();

    /**
     * Return the vertices in the graph
     */
    java.util.List<V> getVertices();


    /**
     * Return the object for the specified vertex index
     */
    V getVertex(int index);

    /**
     * Return the index for the specified vertex object
     */
    int getIndex(V v);

    /**
     * Return the neighbors of vertex with the specified index
     */
    java.util.List<Integer> getNeighbors(int index);

    /**
     * Return the degree for a specified vertex
     */
    int getDegree(int v);

    /**
     * Print the edges
     */
    void printEdges();

    /**
     * Clear the graph
     */
    void clear();

    /**
     * Add a vertex to the graph
     */
    boolean addVertex(V vertex);

    /**
     * Add an edge to the graph
     */
    boolean addEdge(int u, int v);

    /**
     * Add an edge to the graph
     */
    boolean addEdge(Edge e);

    /**
     * Remove a vertex v from the graph, return true if successful
     */
    boolean remove(V v);

    /**
     * Remove an edge (u, v) from the graph, return true if successful
     */
    boolean remove(int u, int v);

    /**
     * Obtain a depth-first search tree
     */
    UnweightedGraph<V>.SearchTree dfs(int v);

    /**
     * Obtain a breadth-first search tree
     */
    UnweightedGraph<V>.SearchTree bfs(int v);

    /**
     * @return shows is graph connected. (There are path from every vertex to every vertex)
     */
    default boolean isConnected() {
        UnweightedGraph<V>.SearchTree tree = dfs(0);
        return tree.getNumberOfVerticesFound() == getSize();
    }

    /**
     * @param filepath path to text file, where program should write text representation of graph.
     */
    default void toTextFile(String filepath) {
        try (PrintWriter output = new PrintWriter(new File(filepath))) {
            final int size = getSize();
            output.println(size);
            for (int i = 0; i < size; i++) {
                List<Integer> neighbors = getNeighbors(i);
                if (neighbors.size() == 0)
                    continue;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(i).append(" ");
                neighbors.forEach(e -> stringBuilder.append(e).append(" "));
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                output.println(stringBuilder.toString());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Check the filepath!");
        }
    }

    private static int isFull(boolean[] array) {
        for (int i = 0; i < array.length; i++) {
            if (!array[i])
                return i;
        }
        return -1;
    }

    /**
     * @return the list of components.
     * A connected component is a maximal connected
     * subgraph in which every pair of vertices are connected by a path
     */

    default List<List<Integer>> getConnectedComponents() {
        final List<List<Integer>> lists = new ArrayList<>();
        final boolean[] isSet = new boolean[getSize()];
        int nextIndex = 0;
        while (nextIndex != -1) {
            List<Integer> set = dfs(nextIndex).getSearchOrder();
            set.forEach(e -> isSet[e] = true);
            Collections.sort(set);
            lists.add(set);
            nextIndex = isFull(isSet);
        }

        return lists;
    }
}
