package com.domain.nvm.morningfriend.features.puzzle.labyrinth.data;

import java.util.Collections;
import java.util.List;

/**
 * Generator that creates random spanning tree for a given labyrinth that will serve as
 * passages through that labyrinth
 */
public class PassagesGeneratorDFS {

    private Labyrinth labyrinth;
    private Passages passages;
    private boolean[] visited;

    private PassagesGeneratorDFS(Labyrinth lab) {
        labyrinth = lab;
        visited = new boolean[labyrinth.getTilesNumber()];
        passages = new Passages(labyrinth.getTilesNumber());

    }

    private Passages generate() {
        visit(0);
        return passages;
    }

    /**
     * Visit given tile and all its neighbors in depth-first manner,
     * connecting this tile to some unexplored neighbor
     * @param tileIdx
     */
    private void visit(int tileIdx) {
        visited[tileIdx] = true;
        List<Integer> neighbors = labyrinth.getNeighborTiles(tileIdx);
        Collections.shuffle(neighbors);
        for (int neighbor: neighbors) {
            if (!visited[neighbor]) {
                passages.connect(tileIdx, neighbor);
                visit(neighbor);
            }
        }
    }

    public static Passages generatePassages(Labyrinth labyrinth) {

        return new PassagesGeneratorDFS(labyrinth).generate();
    }
}
