package com.domain.nvm.morningfriend.ui.puzzle;

public interface Puzzle {
    void init(Difficulty difficulty);
    void setPuzzleHost(PuzzleHost host);
    boolean isSolved();

    enum Difficulty {EASY, MEDIUM, HARD}
    enum PuzzleType {SQUARES, GRAPH}
}
