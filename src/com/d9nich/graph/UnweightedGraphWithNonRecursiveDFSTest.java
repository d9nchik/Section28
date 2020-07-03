package com.d9nich.graph;

import java.io.FileNotFoundException;

class UnweightedGraphWithNonRecursiveDFSTest extends UnweightedGraphTest {
    Graph<String> graph1 = new UnweightedGraphWithNonRecursiveDFS<>(vertices, edges);

    UnweightedGraphWithNonRecursiveDFSTest() throws FileNotFoundException {
    }
}