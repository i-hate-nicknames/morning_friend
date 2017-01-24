package com.domain.nvm.morningfriend.puzzle;

public interface PuzzleHost {
    void onPuzzleSolved();
    void onPuzzleSolutionBroken();
    void onPuzzleTouched();
    void snooze();
}
