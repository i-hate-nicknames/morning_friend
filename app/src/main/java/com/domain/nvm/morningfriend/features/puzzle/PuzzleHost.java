package com.domain.nvm.morningfriend.features.puzzle;

public interface PuzzleHost {
    void onPuzzleSolved();
    void onPuzzleSolutionBroken();
    void onPuzzleTouched();
    void snooze();
}
