package com.domain.nvm.morningfriend.features.puzzle.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generic unweighted undirected graph
 */
public class UndirectedGraph<V extends Vertex, E extends Edge> implements Graph<V, E> {

    private int maxItems;
    private List<V> vertices;
    private HashMap<V, HashSet<V>> neighbors;
    private HashSet<E> edges;
    private Edge.EdgeFactory<E, V> edgeFactory;

    /** Create new undirected graph
     * @param size max number of items in the graph
     * @param edgeFactory factory class for creating edge instances
     */
    public UndirectedGraph(int size, Edge.EdgeFactory<E, V> edgeFactory) {
        maxItems = size;
        neighbors = new HashMap<>();
        edges = new HashSet<>();
        vertices = new ArrayList<>(maxItems);
        this.edgeFactory = edgeFactory;
    }

    @Override
    public int getVertexNum() {
        return vertices.size();
    }

    @Override
    public void addVertex(V v) {
        validateVertex(v, false);
        neighbors.put(v, new HashSet<V>());
        vertices.add(v.getNum(), v);
    }

    @Override
    public void connect(V v1, V v2) {
        validateVertex(v1, true);
        validateVertex(v2, true);
        neighbors.get(v1).add(v2);
        neighbors.get(v2).add(v1);
        edges.add(edgeFactory.makeEdge(v1, v2));
    }

    @Override
    public V getVertex(int num) {
        if (num >= maxItems) {
            throw new IllegalArgumentException("Invalid vertex number: " + num);
        }
        return vertices.get(num);
    }


    @Override
    public List<V> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public Set<E> getEdges() {
        return new HashSet<>(edges);
    }

    private void validateVertex(V v, boolean shouldExist) {
        if (v.getNum() > maxItems-1) {
            throw new IllegalArgumentException(String.format("Vertex number should " +
                    "be >= %d, was = %d", maxItems-1, v.getNum()));
        }
        if (neighbors.containsKey(v) != shouldExist) {
            throw new IllegalArgumentException("Invalid vertex: vertex number = " + v.getNum());
        }
    }

}
