package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;
import android.util.Pair;

import java.util.HashMap;
import java.util.HashSet;

public class Graph {

    private HashMap<Vertex, HashSet<Vertex>> mNeighbors;

    public Graph() {
        mNeighbors = new HashMap<>();
    }

    public void addVertex(Vertex v) {
        validateVertex(v, false);
        mNeighbors.put(v, new HashSet<Vertex>());
    }

    public boolean connected(Vertex v1, Vertex v2) {
        validateVertex(v1, true);
        validateVertex(v2, true);
        for (Vertex neighbor: mNeighbors.get(v1)) {
            if (neighbor.getNum() == v2.getNum()) {
                return true;
            }
        }
        return false;
    }

    public void connect(Vertex v1, Vertex v2) {
        validateVertex(v1, true);
        validateVertex(v2, true);
        mNeighbors.get(v1).add(v2);
        mNeighbors.get(v2).add(v1);
    }

    public HashSet<Edge> getEdges() {
        HashSet<Edge> set = new HashSet<>();
        for (Vertex v: mNeighbors.keySet()) {
            set.addAll(getEdges(v));
        }
        return set;
    }

    public HashSet<Edge> getEdges(Vertex v) {
        validateVertex(v, true);
        HashSet<Edge> set = new HashSet<>();
        for (Vertex neighbor: mNeighbors.get(v)) {
            set.add(new Edge(v, neighbor));
        }
        return set;
    }

    private void validateVertex(Vertex v, boolean shouldExist) {
        if (mNeighbors.containsKey(v) != shouldExist) {
            throw new IllegalArgumentException("Invalid Vertex: vertex number = " + v.getNum());
        }
    }

    public class Edge {

        private Pair<Vertex, Vertex> vertices;

        public Edge(Vertex v1, Vertex v2) {
            // ensure uniqueness of all pairs
            if (v1.getNum() < v2.getNum()) {
                vertices = new Pair<>(v1, v2);
            }
            else {
                vertices = new Pair<>(v2, v1);
            }
        }

        public boolean intersects(Edge other) {
            if (this == other) {
                return false;
            }
            PointF a, b, c, d;
            a = vertices.first.getPosition();
            b = vertices.second.getPosition();
            c = other.vertices.first.getPosition();
            d = other.vertices.second.getPosition();
            return !Utils.linesTouch(a, b, c, d) && Utils.intersects(a, b, c, d);
        }

        public Vertex getFirst() {
            return vertices.first;
        }

        public Vertex getSecond() {
            return vertices.second;
        }

        @Override
        public boolean equals(Object that) {
            if (!(that instanceof Edge)) {
                return false;
            }
            if (that == this) {
                return true;
            }
            return ((Edge) that).vertices.equals(this.vertices);
        }

        @Override
        public int hashCode() {
            return vertices.hashCode();
        }
    }
}
