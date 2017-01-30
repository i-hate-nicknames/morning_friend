package com.domain.nvm.morningfriend.features.puzzle.data;


public interface Edge<V extends Vertex> {

    V getFirst();
    V getSecond();

    /**
     * Factory class to create edge objects since it's impossible to instantiate type parameters
     * directly
     * @param <E> edge type
     * @param <V> vertex type
     */
    interface EdgeFactory<E extends Edge, V extends Vertex> {

        E makeEdge(V u, V v);
    }

}
