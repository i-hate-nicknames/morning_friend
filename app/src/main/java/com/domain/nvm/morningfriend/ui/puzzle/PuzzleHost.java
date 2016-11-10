package com.domain.nvm.morningfriend.ui.puzzle;

public interface PuzzleHost {
    void onPuzzleSolved();
    void onPuzzleSolutionBroken();
    void onPuzzleTouched();
    void snooze();
}
