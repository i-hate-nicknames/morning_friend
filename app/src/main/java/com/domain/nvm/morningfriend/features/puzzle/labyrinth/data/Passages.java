package com.domain.nvm.morningfriend.features.puzzle.labyrinth.data;

import com.domain.nvm.morningfriend.features.puzzle.data.Edge;
import com.domain.nvm.morningfriend.features.puzzle.data.UndirectedGraph;
import com.domain.nvm.morningfriend.features.puzzle.data.Vertex;

/**
 * Passages represent all possible paths in labyrinth
 */
public class Passages {

    private UndirectedGraph<SimpleVertex, SimpleEdge> graph;
    private int size;

    public Passages(int size) {
        this.size = size;
        this.graph = new UndirectedGraph<>(size*size-1, new SimpleEdgeFactory());
    }

    public void connect(int a, int b) {

    }

    public boolean isPassable(int a, int b) {
        return false;
    }

    private static class SimpleVertex implements Vertex {
        int n;

        @Override
        public int getNum() {
            return n;
        }

        @Override
        public int hashCode() {
            return n;
        }
    }

    private static class SimpleEdge implements Edge<SimpleVertex> {

        SimpleVertex u, v;

        SimpleEdge(SimpleVertex u, SimpleVertex v) {
            if (v.getNum() < u.getNum()) {
                this.u = v;
                this.v = u;
            }
            else {
                this.u = u;
                this.v = v;
            }
        }

        @Override
        public SimpleVertex getFirst() {
            return u;
        }

        @Override
        public SimpleVertex getSecond() {
            return v;
        }
    }

    private static class SimpleEdgeFactory implements Edge.EdgeFactory<SimpleEdge, SimpleVertex> {

        @Override
        public SimpleEdge makeEdge(SimpleVertex u, SimpleVertex v) {
            return new SimpleEdge(u, v);
        }
    }


}
