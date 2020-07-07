package com.d9nich.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UnweightedGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>(); // Store vertices
    protected List<List<Edge>> neighbors
            = new ArrayList<>(); // Adjacency Edge lists

    /**
     * Construct an empty graph
     */
    public UnweightedGraph() {
    }

    /**
     * Construct a graph from vertices and edges stored in arrays
     */
    public UnweightedGraph(V[] vertices, int[][] edges) {
        for (V vertex : vertices) addVertex(vertex);
        createAdjacencyLists(edges);
    }

    /**
     * Construct a graph from vertices and edges stored in List
     */
    public UnweightedGraph(List<V> vertices, List<Edge> edges) {
        for (V vertex : vertices) addVertex(vertex);
        createAdjacencyLists(edges);
    }

    /**
     * Create adjacency lists for each vertex
     */
    private void createAdjacencyLists(int[][] edges) {
        for (int[] edge : edges) addEdge(edge[0], edge[1]);
    }

    /**
     * Create adjacency lists for each vertex
     */
    private void createAdjacencyLists(List<Edge> edges) {
        for (Edge edge : edges)
            addEdge(edge.u, edge.v);
    }

    /**
     * Return the number of vertices in the graph
     */
    @Override
    public int getSize() {
        return vertices.size();
    }

    /**
     * Return the vertices in the graph
     */
    @Override
    public List<V> getVertices() {
        return new ArrayList<>(vertices);
    }

    /**
     * Return the object for the specified vertex
     */
    @Override
    public V getVertex(int index) {
        return vertices.get(index);
    }

    /**
     * Return the index for the specified vertex object
     */
    @Override
    public int getIndex(V v) {
        return vertices.indexOf(v);
    }

    /**
     * Return the neighbors of the specified vertex
     */
    @Override
    public List<Integer> getNeighbors(int index) {
        List<Integer> result = new ArrayList<>();
        for (Edge e : neighbors.get(index))
            result.add(e.v);
        return result;
    }

    /**
     * Return the degree for a specified vertex
     */
    @Override
    public int getDegree(int v) {
        return neighbors.get(v).size();
    }

    /**
     * Print the edges
     */
    @Override
    public void printEdges() {
        for (int u = 0; u < neighbors.size(); u++) {
            System.out.print(getVertex(u) + " (" + u + "): ");
            for (Edge e : neighbors.get(u)) {
                System.out.print("(" + getVertex(e.u) + ", " +
                        getVertex(e.v) + ") ");
            }
            System.out.println();
        }
    }

    /**
     * Clear the graph
     */
    @Override
    public void clear() {
        vertices.clear();
        neighbors.clear();
    }

    /**
     * Add a vertex to the graph
     */
    @Override
    public boolean addVertex(V vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            neighbors.add(new ArrayList<>());
            return true;
        }
        return false;
    }

    /**
     * Add an edge to the graph
     */
    @Override
    public boolean addEdge(Edge e) {
        if (e.u < 0 || e.u > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.u);
        if (e.v < 0 || e.v > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.v);
        if (!neighbors.get(e.u).contains(e)) {
            neighbors.get(e.u).add(e);
            return true;
        }
        return false;
    }

    /**
     * Add an edge to the graph
     */
    @Override
    public boolean addEdge(int u, int v) {
        return addEdge(new Edge(u, v));
    }

    /**
     * Obtain a DFS tree starting from vertex v
     */
    @Override
    public SearchTree dfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        // Initialize parent[i] to −1
        Arrays.fill(parent, -1);
        // Mark visited vertices
        boolean[] isVisited = new boolean[vertices.size()];
        // Recursively search
        dfs(v, parent, searchOrder, isVisited);
        // Return a search tree
        return new SearchTree(v, parent, searchOrder);
    }

    /**
     * Recursive method for DFS search
     */
    private void dfs(int v, int[] parent, List<Integer> searchOrder, boolean[] isVisited) {
        // Store the visited vertex
        searchOrder.add(v);
        isVisited[v] = true; // Vertex v visited
        for (Edge e : neighbors.get(v)) {// e.u is v
            if (!isVisited[e.v]) {// e.v is w in Listing 28.8
                parent[e.v] = v; // The parent of vertex w is v
                dfs(e.v, parent, searchOrder, isVisited); // Recursive search
            }
        }
    }

    /**
     * @param input stream from which we can read data
     * @return created graph from data of stream
     */
    public static UnweightedGraph<Integer> getGraphFromSource(Scanner input) {
        UnweightedGraph<Integer> unweightedGraph = new UnweightedGraph<>();
        final int vertexNumber = input.nextInt();
        for (int i = 0; i < vertexNumber; i++)
            unweightedGraph.addVertex(i);

        input.nextLine();
        while (input.hasNext()) {
            final String line = input.nextLine();
            String[] numbers = line.split("[ ]+");
            int vertex = Integer.parseInt(numbers[0]);
            for (int i = 1; i < numbers.length; i++)
                unweightedGraph.addEdge(vertex, Integer.parseInt(numbers[i]));

        }
        input.close();
        return unweightedGraph;
    }

    /**
     * Starting bfs search from vertex v
     */
    @Override
    public SearchTree bfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        // Initialize parent[i] to -1
        Arrays.fill(parent, -1);
        java.util.LinkedList<Integer> queue =
                new java.util.LinkedList<>(); // list used as a queue e.v
        boolean[] isVisited = new boolean[vertices.size()];
        queue.offer(v); // Enqueue v
        isVisited[v] = true; // Mark it visited
        while (!queue.isEmpty()) {
            int u = queue.poll(); // Dequeue to u
            searchOrder.add(u); // u searched
            for (Edge e : neighbors.get(u)) {// Note that e.u is u
                if (!isVisited[e.v]) {// e.v is w in Listing 28.11
                    queue.offer(e.v); // Enqueue w
                    parent[e.v] = u; // The parent of w is u
                    isVisited[e.v] = true; // Mark it visited
                }
            }
        }
        return new SearchTree(v, parent, searchOrder);
    }

    /**
     * @return has graph cycles or not
     */
    @Override
    public boolean isCyclic() {
        boolean[] visitedInApproach = new boolean[getSize()];
        boolean[] isVisited = new boolean[getSize()];
        for (int i = 0; i < getSize(); i++)
            if (!isVisited[i] && isCyclic(i, visitedInApproach, isVisited))
                return true;
        return false;
    }

    /**
     * @param v                 number of vertex we want to explore
     * @param visitedInApproach nodes that forms full path from current to root
     * @param isVisited         nodes we already visited
     * @return weather we found cycle on our attempt
     */
    private boolean isCyclic(int v, boolean[] visitedInApproach, boolean[] isVisited) {
        isVisited[v] = true; // Vertex v visited
        visitedInApproach[v] = true;
        for (Edge e : neighbors.get(v)) {// e.u is v
            if (!isVisited[e.v]) {
                if (isCyclic(e.v, visitedInApproach, isVisited))// Recursive search
                    return true;
            } else if (visitedInApproach[e.v])
                return true;
        }
        visitedInApproach[v] = false;
        return false;
    }

    /**
     * @param u starting vertex
     * @return a List that contains all the vertices in a cycle starting from
     * u. If the graph doesn’t have any cycles, the method returns null.
     */
    public List<Integer> getACycle(int u) {
        ArrayList<Integer> cyclePath = new ArrayList<>();
        boolean[] isVisited = new boolean[getSize()];
        isVisited[u] = true;
        cyclePath.add(u);
        for (Integer neighbour : getNeighbors(u))
            if (foundCycle(neighbour, cyclePath, isVisited, u))
                return cyclePath;
        return null;
    }

    /**
     * @param isVisited array of bool
     * @return is array contains only true values
     */
    private static boolean isFull(boolean[] isVisited) {
        for (boolean bool : isVisited)
            if (!bool)
                return false;
        return true;
    }

    /**
     * @return returns list of number sequence, that forms hamilton cycle.
     * If hamilton cycle is not found return null.
     */
    @Override
    public List<Integer> getHamiltonianCycle() {
        ArrayList<Integer> cyclePath = new ArrayList<>();
        boolean[] isVisited = new boolean[getSize()];

        for (int i = 0; i < getSize(); i++) {
            if (getHamiltonCycle(i, cyclePath, isVisited, i))
                return cyclePath;
        }
        return null;
    }

    /**
     * @param vertex    which vertex we explore now
     * @param cyclePath path of vertices, that forms cycle
     * @param isVisited list of state of form, that are visited or not
     * @param endVertex node which should be end of our path
     * @return is cycle found on current step or not
     */
    private boolean getHamiltonCycle(int vertex, ArrayList<Integer> cyclePath, boolean[] isVisited, int endVertex) {
        cyclePath.add(vertex);
        isVisited[vertex] = true;
        boolean full = isFull(isVisited);

        for (Integer neighbour : getNeighbors(vertex)) {
            if (neighbour == endVertex && full)
                return true;

            if (!isVisited[neighbour] && getHamiltonCycle(neighbour, cyclePath, isVisited, endVertex))
                return true;
        }

        isVisited[vertex] = false;
        cyclePath.remove(cyclePath.size() - 1);
        return false;
    }


    /**
     * @param vertex    which vertex we explore now
     * @param cyclePath path of vertices, that forms cycle
     * @param isVisited list of state of form, that are visited or not
     * @param endVertex node which should be end of our path
     * @return is cycle found on current step or not
     */
    private boolean foundCycle(int vertex, ArrayList<Integer> cyclePath, boolean[] isVisited, int endVertex) {
        cyclePath.add(vertex);
        isVisited[vertex] = true;
        for (Integer neighbour : getNeighbors(vertex)) {
            if (neighbour == endVertex)
                return true;

            if (!isVisited[neighbour] && foundCycle(neighbour, cyclePath, isVisited, endVertex))
                return true;
        }
        cyclePath.remove(cyclePath.size() - 1);
        return false;
    }

    /**
     * @return A graph is bipartite if its vertices can be divided
     * into two disjoint sets such that no edges exist between vertices in the same set.
     */
    @Override
    public boolean isBipartite() {
        int[] colours = new int[getSize()];
        Arrays.fill(colours, -1);
        for (int i = 0; i < colours.length; i++)
            if (colours[i] == -1) {
                colours[i] = 1;
                if (isNotBipartiteColourVertex(i, colours))
                    return false;
            }
        return true;
    }

    /**
     * @return a List that contains two sublists, each of which contains a
     * set of vertices. If the graph is not bipartite, the method returns null .
     */
    @Override
    public List<List<Integer>> getBipartite() {
        int[] colours = new int[getSize()];
        Arrays.fill(colours, -1);
        for (int i = 0; i < colours.length; i++)
            if (colours[i] == -1) {
                colours[i] = 1;
                if (isNotBipartiteColourVertex(i, colours))
                    return null;
            }

        List<List<Integer>> lists = new ArrayList<>();
        ArrayList<Integer> firstPart = new ArrayList<>();
        ArrayList<Integer> secondPart = new ArrayList<>();
        lists.add(firstPart);
        lists.add(secondPart);

        for (int i = 0; i < colours.length; i++)
            if (colours[i] == 1)
                firstPart.add(i);
            else
                secondPart.add(i);

        return lists;
    }


    /**
     * @param vertex  for which children we are searching colours and checking, if they`re valid
     * @param colours list of colours linked to each node
     * @return can be graph bipartite in case of this vertex.
     */
    private boolean isNotBipartiteColourVertex(int vertex, int[] colours) {
        int colour = colours[vertex];
        for (Integer neighbour : getNeighbors(vertex))
            if (colours[neighbour] == -1) {
                colours[neighbour] = (colour + 1) % 2;
                if (isNotBipartiteColourVertex(neighbour, colours))
                    return true;
            } else if (colours[neighbour] == colour)
                return true;
        return false;
    }

    /**
     * Remove vertex v and return true if successful
     */
    @Override
    public boolean remove(V v) {
        if (!vertices.contains(v))
            throw new NoSuchFieldError(v + " not found.");
        int index = getIndex(v);
        neighbors.remove(neighbors.get(index));
        vertices.remove(v);
        neighbors.forEach(edges -> edges.removeIf(edge -> edge.v == index));
        return true; // Implementation left as an exercise
    }

    /**
     * Remove edge (u, v) and return true if successful
     */
    @Override
    public boolean remove(int u, int v) {
        if (u < 0 || u > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + u);
        if (v < 0 || v > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + v);
        if (neighbors.get(u).contains(new Edge(u, v))) {
            neighbors.get(u).remove(new Edge(u, v));
            return true;
        }
        return false;// Implementation left as an exercise
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnweightedGraph<?> that = (UnweightedGraph<?>) o;

        if (!vertices.equals(that.vertices)) return false;
        return neighbors.equals(that.neighbors);
    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + neighbors.hashCode();
        return result;
    }

    /**
     * Tree inner class inside the UnweightedGraph class
     */
    public class SearchTree {
        private final int root; // The root of the tree
        private final int[] parent; // Store the parent of each vertex
        private final List<Integer> searchOrder; // Store the search order

        /**
         * Construct a tree with root, parent, and searchOrder
         */
        public SearchTree(int root, int[] parent,
                          List<Integer> searchOrder) {
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }

        /**
         * Return the root of the tree
         */
        public int getRoot() {
            return root;
        }

        /**
         * Return the parent of vertex v
         */
        public int getParent(int v) {
            return parent[v];
        }

        /**
         * Return an array representing search order
         */
        public List<Integer> getSearchOrder() {
            return searchOrder;
        }

        /**
         * Return number of vertices found
         */
        public int getNumberOfVerticesFound() {
            return searchOrder.size();
        }

        /**
         * Return the path of vertices from a vertex to the root
         */
        public List<V> getPath(int index) {
            ArrayList<V> path = new ArrayList<>();
            do {
                path.add(vertices.get(index));
                index = parent[index];
            }
            while (index != -1);
            return path;
        }

        /**
         * Print a path from the root to vertex v
         */
        public void printPath(int index) {
            List<V> path = getPath(index);
            System.out.print("A path from " + vertices.get(root) + " to " +
                    vertices.get(index) + ": ");
            for (int i = path.size() - 1; i >= 0; i--)
                System.out.print(path.get(i) + " ");
        }

        /**
         * Print the whole tree
         */
        public void printTree() {
            System.out.println("Root is: " + vertices.get(root));
            System.out.print("Edges: ");
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] != -1) {
                    // Display an edge
                    System.out.print("(" + vertices.get(parent[i]) + ", " +
                            vertices.get(i) + ") ");
                }
            }
            System.out.println();
        }
    }
}
