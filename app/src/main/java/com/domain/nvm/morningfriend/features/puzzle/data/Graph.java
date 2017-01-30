package com.domain.nvm.morningfriend.features.puzzle.data;

import java.util.List;
import java.util.Set;

public interface Graph<V extends Vertex, E extends Edge> {

    int getVertexNum();
    void addVertex(V v);
    void connect(V v1, V v2);
    V getVertex(int num);
    List<V> getVertices();
    Set<E> getEdges();
}
