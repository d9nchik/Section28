package com.d9nich;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class UnweightedGraphTest {
    String[] vertices = {"Seattle", "San Francisco", "Los Angeles",
            "Denver", "Kansas City", "Chicago", "Boston", "New York",
            "Atlanta", "Miami", "Dallas", "Houston"};
    // Edge array for graph in Figure 28.1
    int[][] edges = {
            {0, 1}, {0, 3}, {0, 5},
            {1, 0}, {1, 2}, {1, 3},
            {2, 1}, {2, 3}, {2, 4}, {2, 10},
            {3, 0}, {3, 1}, {3, 2}, {3, 4}, {3, 5},
            {4, 2}, {4, 3}, {4, 5}, {4, 7}, {4, 8}, {4, 10},
            {5, 0}, {5, 3}, {5, 4}, {5, 6}, {5, 7},
            {6, 5}, {6, 7},
            {7, 4}, {7, 5}, {7, 6}, {7, 8},
            {8, 4}, {8, 7}, {8, 9}, {8, 10}, {8, 11},
            {9, 8}, {9, 11},
            {10, 2}, {10, 4}, {10, 8}, {10, 11},
            {11, 8}, {11, 9}, {11, 10}
    };
    Graph<String> graph1 = new UnweightedGraph<>(vertices, edges);

    String[] names = {"Peter", "Jane", "Mark", "Cindy", "Wendy"};
    java.util.ArrayList<Edge> edgeList
            = new java.util.ArrayList<>();
    Graph<String> graph2;
    Graph<String> graph3 = new UnweightedGraph<>();

    @BeforeEach
    public void setUP() {
        edgeList.add(new Edge(0, 2));
        edgeList.add(new Edge(1, 2));
        edgeList.add(new Edge(2, 4));
        edgeList.add(new Edge(3, 4));
        // Create a graph with 5 vertices
        graph2 = new UnweightedGraph<>(java.util.Arrays.asList(names), edgeList);
    }

    @org.junit.jupiter.api.Test
    void getSize() {
        assertEquals(12, graph1.getSize());
        assertEquals(5, graph2.getSize());
    }

    @org.junit.jupiter.api.Test
    void getVertices() {
        assertEquals(new ArrayList<>(), graph3.getVertices());
        graph3.getVertices().add("Something");
        assertEquals(0, graph3.getSize());
    }

    @org.junit.jupiter.api.Test
    void getVertex() {
        assertEquals("San Francisco", graph1.getVertex(1));
        assertEquals("Jane", graph2.getVertex(1));
        assertThrows(IndexOutOfBoundsException.class, () -> graph2.getVertex(5));
        assertThrows(IndexOutOfBoundsException.class, () -> graph2.getVertex(-1));
    }

    @org.junit.jupiter.api.Test
    void getIndex() {
        assertEquals(9, graph1.getIndex("Miami"));
        assertEquals(3, graph2.getIndex("Cindy"));
        assertEquals(-1, graph3.getIndex("go"));
    }

    @org.junit.jupiter.api.Test
    void getNeighbors() {
        assertThrows(IndexOutOfBoundsException.class, () -> graph2.getNeighbors(5));
        assertThrows(IndexOutOfBoundsException.class, () -> graph2.getNeighbors(-1));
        assertEquals(2, graph2.getNeighbors(0).get(0));
    }

    @org.junit.jupiter.api.Test
    void getDegree() {
        assertThrows(IndexOutOfBoundsException.class, () -> graph2.getDegree(5));
        assertThrows(IndexOutOfBoundsException.class, () -> graph2.getDegree(-1));
        assertEquals(6, graph1.getDegree(4));
    }

    @org.junit.jupiter.api.Test
    void printEdges() {
    }

    @org.junit.jupiter.api.Test
    void clear() {
        graph2.clear();
        assertEquals(0, graph2.getSize());
    }

    @org.junit.jupiter.api.Test
    void addVertex() {
        graph3.addVertex("five");
        assertEquals(1, graph3.getVertices().size());
        assertEquals("five", graph3.getVertex(0));
        graph3.addVertex("five");
        assertEquals(1, graph3.getVertices().size());
        assertEquals("five", graph3.getVertex(0));
    }

    @org.junit.jupiter.api.Test
    void addEdge() {
        graph2.addEdge(new Edge(2, 3));
        assertEquals(5, graph2.getSize());
        assertTrue(graph2.getNeighbors(2).contains(3));
        graph2.addEdge(new Edge(2, 3));
        assertEquals(5, graph2.getSize());
        assertTrue(graph2.getNeighbors(2).contains(3));
        assertThrows(IllegalArgumentException.class, () -> graph2.addEdge(new Edge(-1, 2)));
    }

    @org.junit.jupiter.api.Test
    void testAddEdge() {
        graph2.addEdge(4, 0);
        assertEquals(5, graph2.getSize());
        assertTrue(graph2.getNeighbors(4).contains(0));
        graph2.addEdge(4, 0);
        assertEquals(5, graph2.getSize());
        assertTrue(graph2.getNeighbors(4).contains(0));
        assertThrows(IllegalArgumentException.class, () -> graph2.addEdge(-1, 2));
    }

    @org.junit.jupiter.api.Test
    void dfs() {
        UnweightedGraph<String>.SearchTree dfs =
                graph1.dfs(graph1.getIndex("Chicago"));
        java.util.List<Integer> searchOrders = dfs.getSearchOrder();
        assertEquals(12, dfs.getNumberOfVerticesFound());
        assertEquals("Chicago", graph1.getVertex(searchOrders.get(0)));
        assertEquals("Seattle", graph1.getVertex(searchOrders.get(1)));
    }

    @org.junit.jupiter.api.Test
    void bfs() {
    }

    @org.junit.jupiter.api.Test
    void remove() {
        graph2.remove(0, 2);
        assertEquals(0, graph2.getNeighbors(0).size());
        assertThrows(IllegalArgumentException.class, () -> graph2.remove(0, 0));
        assertThrows(IllegalArgumentException.class, () -> graph2.remove(-1, 2));
    }

    @org.junit.jupiter.api.Test
    void testRemove() {
        graph2.remove("Mark");
        assertEquals(0, graph2.getNeighbors(2).size());
        assertThrows(IllegalArgumentException.class, () -> graph2.remove(0, 0));
        assertThrows(IllegalArgumentException.class, () -> graph2.remove(-1, 2));
    }

    @Test
    void isConnected() {
        Graph<Integer> graph = new UnweightedGraph<>();
        for (int i = 0; i < 4; i++)
            graph.addVertex(i);
        graph.addEdge(new Edge(0, 1));
        graph.addEdge(2, 3);
        assertFalse(graph.isConnected());
        graph.addEdge(1, 3);
        assertFalse(graph.isConnected());
        graph.addEdge(1, 2);
        assertTrue(graph.isConnected());
    }

    @Test
    void equals() {
        Graph<Integer> graph = new UnweightedGraph<>();
        Graph<Integer> graphCopy = new UnweightedGraph<>();

        for (int i = 0; i < 4; i++) {
            graph.addVertex(i);
            graphCopy.addVertex(i);
        }
        graph.addEdge(new Edge(0, 1));
        graph.addEdge(2, 3);
        assertNotEquals(graphCopy, graph);

        graphCopy.addEdge(0, 1);
        graphCopy.addEdge(2, 3);
        assertEquals(graphCopy, graph);

        graph.addEdge(1, 2);
        assertNotEquals(graphCopy, graph);
    }

    @Test
    void toTextFile() throws FileNotFoundException {
        Graph<Integer> graph = new UnweightedGraph<>();
        for (int i = 0; i < 4; i++)
            graph.addVertex(i);
        graph.addEdge(new Edge(0, 1));
        graph.addEdge(2, 3);
        graph.toTextFile("/tmp/graph.txt");
        File file = new File("/tmp/graph.txt");
        file.deleteOnExit();
        assertTrue(file.exists());


        Graph<Integer> copyGraph = UnweightedGraph.getGraphFromSource(new Scanner(file));

        assertEquals(copyGraph, graph);
    }
}