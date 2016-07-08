package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Graph {

    public static final int MAX_ITEMS = 25;

    private Vertex[] mVertices;
    private HashMap<Vertex, HashSet<Vertex>> mNeighbors;
    private HashSet<Edge> mEdges;

    public Graph() {
        mNeighbors = new HashMap<>();
        mEdges = new HashSet<>();
        mVertices = new Vertex[MAX_ITEMS];
        for (int i = 0; i < MAX_ITEMS; i++) {
            mVertices[i] = null;
        }
    }

    public void addVertex(Vertex v) {
        validateVertex(v, false);
        mNeighbors.put(v, new HashSet<Vertex>());
        mVertices[v.getNum()] = v;
    }

    public void connect(Vertex v1, Vertex v2) {
        validateVertex(v1, true);
        validateVertex(v2, true);
        mNeighbors.get(v1).add(v2);
        mNeighbors.get(v2).add(v1);
        mEdges.add(new Edge(v1, v2));
    }

    public Vertex getVertex(int num) {
        if (num >= MAX_ITEMS) {
            throw new IllegalArgumentException("Invalid Vertex number: " + num);
        }
        return mVertices[num];
    }

    // TODO: implement proper way of storing vertices
    public List<Vertex> getVertices() {
        ArrayList<Vertex> vs = new ArrayList<>();
        int i = 0;
        while (mVertices[i] != null) {
            vs.add(mVertices[i]);
            i++;
        }
        return vs;
    }

    public HashSet<Edge> getEdges() {
        return mEdges;
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
