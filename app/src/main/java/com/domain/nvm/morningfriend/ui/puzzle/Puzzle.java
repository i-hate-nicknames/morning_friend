package com.domain.nvm.morningfriend.ui.puzzle;

import com.domain.nvm.morningfriend.Alarm;

public interface Puzzle {
    void init(Alarm.Difficulty difficulty);
    void setPuzzleHost(PuzzleHost host);
    boolean isSolved();
}
