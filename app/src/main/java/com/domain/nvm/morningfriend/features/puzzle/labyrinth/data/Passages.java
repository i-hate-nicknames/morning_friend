package com.domain.nvm.morningfriend.features.puzzle.labyrinth.data;

import com.domain.nvm.morningfriend.features.puzzle.data.Edge;
import com.domain.nvm.morningfriend.features.puzzle.data.UndirectedGraph;
import com.domain.nvm.morningfriend.features.puzzle.data.Vertex;

/**
 * Passages represent all possible paths in labyrinth
 */
public class Passages {

    private UndirectedGraph<SimpleVertex, SimpleEdge> graph;
    private int vertexNum;
    // unfortunate ugly consequence that we can't use Integer as a vertex
    // and now have to store vertices separately to be able to map int to vertex
    private SimpleVertex[] vertices;

    public Passages(int tilesNum) {
        vertexNum = tilesNum;
        this.graph = new UndirectedGraph<>(vertexNum, new SimpleEdgeFactory());
        initGraph();
    }

    private void initGraph() {
        vertices = new SimpleVertex[vertexNum];
        for (int i = 0; i < vertexNum; i++) {
            SimpleVertex v = new SimpleVertex(i);
            graph.addVertex(v);
            vertices[i] = v;
        }
    }

    public void connect(int a, int b) {
        SimpleVertex u = vertices[a];
        SimpleVertex v = vertices[b];
        graph.connect(u, v);
    }

    public boolean isPassable(int a, int b) {
        SimpleVertex u = vertices[a];
        SimpleVertex v = vertices[b];
        return graph.areConnected(u, v);
    }

    private static class SimpleVertex implements Vertex {
        int n;

        SimpleVertex(int n) {
            this.n = n;
        }

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
