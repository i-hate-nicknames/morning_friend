package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;

import java.util.HashMap;
import java.util.HashSet;

public class Graph {

    public static final int MAX_ITEMS = 25;

    private HashMap<Vertex, HashSet<Vertex>> mNeighbors;
    private HashSet<Edge> mAllEdges;

    public Graph() {
        mNeighbors = new HashMap<>();
    }

    public void addVertex(Vertex v) {
        validateVertex(v, false);
        mNeighbors.put(v, new HashSet<Vertex>());
        mAllEdges = null;
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
        if (mAllEdges == null) {
            mAllEdges = new HashSet<>();
            for (Vertex v: mNeighbors.keySet()) {
                mAllEdges.addAll(getEdges(v));
            }
        }
        return mAllEdges;
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
        if (v.getNum() > MAX_ITEMS-1) {
            throw new IllegalArgumentException(String.format("Vertex number should " +
                    "be >= %d, was = %d", MAX_ITEMS-1, v.getNum()));
        }
        if (mNeighbors.containsKey(v) != shouldExist) {
            throw new IllegalArgumentException("Invalid Vertex: vertex number = " + v.getNum());
        }
    }

    public class Edge {

        private Vertex u, v;

        public Edge(Vertex v1, Vertex v2) {
            if (v1.getNum() < v2.getNum()) {
                u = v1;
                v = v2;
            }
            else {
                u = v2;
                v = v1;
            }
        }

        public boolean intersects(Edge other) {
            if (this == other) {
                return false;
            }
            PointF a, b, c, d;
            a = u.getPosition();
            b = v.getPosition();
            c = other.getFirst().getPosition();
            d = other.getSecond().getPosition();
            return !Utils.linesTouch(a, b, c, d) && Utils.intersects(a, b, c, d);
        }

        public Vertex getFirst() {
            return u;
        }

        public Vertex getSecond() {
            return v;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Edge)) {
                return false;
            }
            Edge that = (Edge) obj;
            if (that == this) {
                return true;
            }
            return that.hashCode() == hashCode();
        }

        @Override
        public int hashCode() {
            // every edge with the same (u, v) will get same hash
            return u.getNum() * MAX_ITEMS + v.getNum();
        }
    }
}
