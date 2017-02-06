package com.domain.nvm.morningfriend.features.puzzle.labyrinth.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Labyrinth {

    public enum Direction {LEFT, RIGHT, UP, DOWN}

    public static final int INVALID_TILE = -1;

    private int size;
    private int tilesNum;
    private Passages passages;

    private int playerTile;

    public Labyrinth(int size) {
        this.size = size;
        tilesNum = size*size;
    }

    public void setPassages(Passages passages) {
        this.passages = passages;
    }

    /**
     * Generate random passages through this labyrinth
     */
    public void generatePassages() {
        passages = new Passages(tilesNum);
        //TODO: generate random passages
    }

    public int getTilesNumber() {
        return tilesNum;
    }

    public int getSize() {
        return size;
    }

    public boolean isInGoalTile() {
        return false;
    }

    public List<Integer> getNeighborTiles(int tileIdx) {
        List<Integer> neighbors = new ArrayList<>();
        for (Direction dir: Direction.values()) {
            int neighbor = getNextTile(tileIdx, dir);
            if (neighbor != INVALID_TILE) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    /**
     * Get index of the tile one step from source tile in specified direction
     * Assume tileIdx is a valid index of tile in the labyrinth
     * @param tileIdx source tile index
     * @param direction direction to look for the neighbor
     * @return
     */
    public int getNextTile(int tileIdx, Direction direction) {
        int row = tileIdx / size;
        int col = tileIdx % size;
        switch (direction) {
            case DOWN:
                if (row < size-1)
                    return (row+1)*size+col;
                else
                    return INVALID_TILE;
            case UP:
                if (row > 0)
                    return (row-1)*size+col;
                else
                    return INVALID_TILE;
            case LEFT:
                if (col > 0)
                    return tileIdx - 1;
                else
                    return INVALID_TILE;
            case RIGHT:
                if (col < size-1)
                    return tileIdx + 1;
                else
                    return INVALID_TILE;
            default:
                return INVALID_TILE;
        }

    }

    /**
     * True if player can move from start tile one tile in given direction
     * @param tileIdx start tile index (from 0 to size*size-1)
     * @param direction direction to move in
     */
    public boolean canMove(int tileIdx, Direction direction) {
        return (new Random()).nextBoolean();
    }

    public boolean canMove(int tileX, int tileY, Direction direction) {
        return canMove(tileX + tileY*size, direction);
    }

}
