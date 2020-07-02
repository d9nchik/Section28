package com.d9nich;

public class Edge {
    int u;
    int v;

    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (u != edge.u) return false;
        return v == edge.v;
    }

    @Override
    public int hashCode() {
        int result = u;
        result = 31 * result + v;
        return result;
    }
}
