package com.domain.nvm.morningfriend.features.puzzle.labyrinth.data;

public class Labyrinth {

    private int size;
    private Passages passages;
    private int playerTile;

    public Labyrinth(int size) {
        this.size = size;
    }

    public void setPassages(Passages passages) {
        this.passages = passages;
    }


}
