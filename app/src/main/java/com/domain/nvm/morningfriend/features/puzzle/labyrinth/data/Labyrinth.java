package com.domain.nvm.morningfriend.features.puzzle.labyrinth.data;

import java.util.Random;

public class Labyrinth {

    public enum Direction {LEFT, RIGHT, UP, DOWN}

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
